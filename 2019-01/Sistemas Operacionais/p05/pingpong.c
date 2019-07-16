/*
Projeto 05 - Autores:
Kelvin James
Rafael Lammel Marinheiro
*/
#include <ucontext.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/time.h>
#include "pingpong.h"
#include "queue.h"
#define STACKSIZE 32768		/* tamanho de pilha das threads */
#define TA -1

// estrutura que define um tratador de sinal (deve ser global ou static)
struct sigaction action ;

// estrutura de inicialização to timer
struct itimerval timer;

int taskid, userTasks, tick;
task_t contextMain, dispatcher, *current, *filaProntas;

task_t *scheduler();
void dispatcher_body ();
void tratador (int signum);

// funções gerais ==============================================================

void pingpong_init()
{
    /* desativa o buffer da saida padrao (stdout), usado pela função printf */
    setvbuf (stdout, 0, _IONBF, 0);
    taskid = 0, userTasks = 0;
    contextMain.tid = taskid;
    //Criamos o dispatcher como uma tarefa;
    task_create(&dispatcher,dispatcher_body,NULL);
    current = &contextMain;
    //Iniciando o Signal
    action.sa_handler = tratador ;
    sigemptyset (&action.sa_mask) ;
    action.sa_flags = 0 ;
    if (sigaction (SIGALRM, &action, 0) < 0)
    {
        perror ("Erro em sigaction: ") ;
        exit (1) ;
    }
    //Iniciando o timer
    timer.it_value.tv_usec = 1000 ;      // primeiro disparo, em micro-segundos
    timer.it_value.tv_sec  = 0 ;      // primeiro disparo, em segundos
    timer.it_interval.tv_usec = 1000 ;   // disparos subsequentes, em micro-segundos
    timer.it_interval.tv_sec  = 0 ;   // disparos subsequentes, em segundos
    if (setitimer (ITIMER_REAL, &timer, 0) < 0)
    {
        perror ("Erro em setitimer: ") ;
        exit (1) ;
    }
}

// gerência de tarefas =========================================================

int task_create (task_t *task, void (*start_func)(void *), void *arg)
{
    char *stack;

    //Salva o atual contexto (que está sendo criado) no ponteiro task;
    getcontext (&(task->context));

    stack = malloc (STACKSIZE) ;
    if (stack)
    {
        task->context.uc_stack.ss_sp = stack ;
        task->context.uc_stack.ss_size = STACKSIZE;
        task->context.uc_stack.ss_flags = 0;
        task->context.uc_link = 0;
        task->tid = ++taskid;
    }
    else
    {
        perror ("Erro na criação da pilha: ");
        exit (1);
    }
    //Setando prioridades dinamicas e estáticas
    task->staticPrio = 0;
    task->dynamicPrio = 0;
    //Colocando a função passada para ser executada ao iniciar task;
    makecontext(&(task->context),(void*)(*start_func),1,arg);
    if(task != &dispatcher) // Dispatcher não pode estar na fila;
    {
        queue_append((queue_t**) &filaProntas, (queue_t*) task);
        userTasks++;
    }
#ifdef DEBUG
    printf("task_create: criou a tarefa: %d\n", task->tid);
#endif
    return task->tid;
}

void task_exit (int exitCode)
{
#ifdef DEBUG
    printf("task_exit: tarefa %d sendo encerrada\n", current->tid);
#endif
    //Verifica se a tarefa é dispatcher, se for retorna para a Main;
    if(current == &dispatcher)
        task_switch(&contextMain);
    else
        task_switch(&dispatcher);
}

int task_switch (task_t *task)
{
#ifdef DEBUG
    printf("task_switch: %d -> %d\n", current->tid, task->tid);
#endif
    task_t *aux;
    aux = current;
    current = task;
    //Troca o contexto para o passado por parâmetro e salva o atual;
    swapcontext(&(aux->context),&(task->context));
    return 0;
}

int task_id ()
{
    return current->tid;
}

//Não utilizada nesse projeto;
void task_suspend (task_t *task, task_t **queue)
{
    if (queue != NULL)
    {
        if(task != NULL)
        {
            task->status = suspensa;
            queue_remove((queue_t**) &filaProntas,(queue_t*)task);
            queue_append((queue_t**)queue,(queue_t*)task);
        }
        else
        {
            current->status = suspensa;
            queue_remove((queue_t**) &filaProntas,(queue_t*)current);
            queue_append((queue_t**)queue,(queue_t*)current);
        }
    }
}

//Não utilizada nesse projeto;
void task_resume (task_t *task)
{
    if(task->next != NULL && task->prev != NULL)
    {
        task->next->prev = task->prev;
        task->prev->next = task->next;
    }
    task->status = pronta;
    task->next = NULL;
    task->prev = NULL;
    queue_append((queue_t**)&filaProntas, (queue_t*)task);
}

// operações de escalonamento ==================================================

void task_yield ()
{
    if(current->tid != 0) // o contexto Main não pode estar na fila de prontas, se não o programa encerra antes do esperado;
    {
        queue_append((queue_t**) &filaProntas, (queue_t*) current);
        userTasks++;
    }
    task_switch(&dispatcher);
}

void task_setprio (task_t *task, int prio)
{
    if(task != NULL)
    {
        task->staticPrio = prio;
        task->dynamicPrio = prio;
    }
    else
    {
        current->staticPrio = prio;
        current->dynamicPrio = prio;
    }
}

int task_getprio (task_t *task)
{
    if(task != NULL)
        return task->staticPrio;
    return current->staticPrio;
}

// Funções que não estão no Header ===========================================

task_t *scheduler()
{
    task_t *next, *aux;
    aux = filaProntas;

    //Código usado com prioridades; Não utilizado nesta versão do projeto;

    /*next = aux;
    do
    {
        if(aux->dynamicPrio <= next->dynamicPrio)
            next = aux;
        aux = aux->next;
    }
    while(aux != filaProntas);
    do
    {
        if(aux != next)
        {
            aux->dynamicPrio+=TA;
        }
        aux = aux->next;
    }
    while(aux != filaProntas);
    next->dynamicPrio = next->staticPrio;*/

    //Remove a cabeça da fila de prontas e retorna
    //ela para o dispatcher (FIFO);
    next = (task_t*) queue_remove((queue_t**) &filaProntas,(queue_t*)aux);
    return next;
}

void dispatcher_body () // dispatcher é uma tarefa
{
    task_t *next;
    while ( userTasks > 0 )
    {
        //Enquanto houverem tarefas de usuário, scheduler seleciona
        //a próxima tarefa de acordo com a política implementada
        //e retorna para que possa ser executada;
        next = scheduler() ; // scheduler é uma função
        if (next)
        {
            next->quantum = 20;
            task_switch (next) ; // transfere controle para a tarefa "next"
            userTasks--;
        }
    }
    task_exit(0) ; // encerra a tarefa dispatcher
}


void tratador (int signum)
{
    //Verifica se é uma tarefa de usuário, se for retira um quantum
    //e logo em seguida verifica se o quantum acabou para trocar de tarefa;
    if(current != &dispatcher && current != &contextMain)
    {
        current->quantum--;
        if(current->quantum == 0)
        {
            task_yield();
        }
    }
}
