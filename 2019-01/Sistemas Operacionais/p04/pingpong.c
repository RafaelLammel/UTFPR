#include <ucontext.h>
#include <stdio.h>
#include <stdlib.h>
#include "pingpong.h"
#include "queue.h"
#define STACKSIZE 32768		/* tamanho de pilha das threads */
#define TA -1

int taskid, userTasks;
task_t contextMain, dispatcher, *current, *filaProntas;

task_t *scheduler();
void dispatcher_body ();

// funções gerais ==============================================================

void pingpong_init()
{
    /* desativa o buffer da saida padrao (stdout), usado pela função printf */
    setvbuf (stdout, 0, _IONBF, 0);
    taskid = 0, userTasks = 0;
    contextMain.tid = taskid;
    task_create(&dispatcher,dispatcher_body,NULL);
    current = &contextMain;
}

// gerência de tarefas =========================================================

int task_create (task_t *task, void (*start_func)(void *), void *arg)
{
    char *stack;

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
    task->staticPrio = 0;
    task->dynamicPrio = 0;
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
    swapcontext(&(aux->context),&(task->context));
    return 0;
}

int task_id ()
{
    return current->tid;
}

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
    next = aux;
    do
    {
        if(aux->dynamicPrio <= next->dynamicPrio)
            next = aux;
        aux = aux->next;
    }while(aux != filaProntas);
    do
    {
        if(aux != next)
        {
            aux->dynamicPrio+=TA;
        }
        aux = aux->next;
    }while(aux != filaProntas);
    next->dynamicPrio = next->staticPrio;
    queue_remove((queue_t**) &filaProntas,(queue_t*)next);
    return next;
}

void dispatcher_body () // dispatcher é uma tarefa
{
    task_t *next;
    while ( userTasks > 0 )
    {
        next = scheduler() ; // scheduler é uma função
        if (next)
        {
            // ações antes de lançar a tarefa "next", se houverem
            task_switch (next) ; // transfere controle para a tarefa "next"
            userTasks--;
        }
    }
    task_exit(0) ; // encerra a tarefa dispatcher
}
