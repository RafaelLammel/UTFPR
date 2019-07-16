/*
Projeto 10 - Autores:
Kelvin James
Rafael Lammel Marinheiro
*/
#include <ucontext.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/time.h>
#include <string.h>
#include "pingpong.h"
#include "queue.h"
#define STACKSIZE 32768		/* tamanho de pilha das threads */
#define TA -1

// estrutura que define um tratador de sinal (deve ser global ou static)
struct sigaction action ;

// estrutura de inicialização to timer
struct itimerval timer;

int taskid, userTasks, tick;
unsigned int clock;
task_t contextMain, dispatcher, *current, *filaProntas, *filaSuspensas, *filaAdormecidas;

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
    clock = 0;
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
    task_yield();
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
        task->createTime = clock;
        task->processTime = 0;
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
    task->tarefab = -1;
#ifdef DEBUG
    printf("task_create: criou a tarefa: %d\n", task->tid);
#endif
    return task->tid;
}

void task_exit (int exitCode)
{
    task_t *aux = filaSuspensas;
    current->exitTime = clock;
    printf("Task: %d exit: execution time: %d ms, processor time: %d ms, activations: %d\n",current->tid,
    current->exitTime-current->createTime,current->processTime,current->act);
#ifdef DEBUG
    printf("task_exit: tarefa %d sendo encerrada\n", current->tid);
#endif
    //Verifica se a tarefa é dispatcher, se for retorna para a Main;
    current->exitCode = exitCode;
    current->status = terminada;
    if(filaSuspensas != NULL)
    {
        do{
            task_t *remove = aux;
            aux = aux->next;
            if(remove->tarefab == current->tid){
                task_resume(remove);
            }
        }while(aux!=filaSuspensas && filaSuspensas != NULL);
    }
    //Verifica se a tarefa é dispatcher, se for retorna para a Main;
    if(current == &dispatcher)
        task_switch(&contextMain);
    //Computa o numero de vezes que Dispatcher é ativado;
    else
    {
        dispatcher.act++;
        task_switch(&dispatcher);
    }
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
            queue_append((queue_t**)queue,(queue_t*)current);
            task_switch(&dispatcher);
        }
    }
}

void task_resume (task_t *task)
{
    queue_remove((queue_t**)&filaSuspensas, (queue_t*)task);
    queue_append((queue_t**)&filaProntas, (queue_t*)task);
    userTasks++;
}

// operações de escalonamento ==================================================

void task_yield ()
{
    queue_append((queue_t**) &filaProntas, (queue_t*) current);
    userTasks++;

    //Computa quantas vezes Dispatcher foi ativado;
    dispatcher.act++;
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

// operações de sincronização ==================================================

// a tarefa corrente aguarda o encerramento de outra task
int task_join (task_t *task)
{
    if(task->status != terminada && task != NULL)
    {
        current->tarefab = task->tid;
        task_suspend(NULL,&filaSuspensas);
        return task->exitCode;
    }
    return -1;
}

// operações de gestão do tempo ================================================

// suspende a tarefa corrente por t segundos
void task_sleep (int t)
{
    queue_append((queue_t**)&filaAdormecidas,(queue_t*)current);
    current->acordaEm = t*1000+clock;
    task_switch(&dispatcher);
}

unsigned int systime()
{
  return clock;
}

// operações de IPC ============================================================

// semáforos

// cria um semáforo com valor inicial "value"
int sem_create (semaphore_t *s, int value)
{
  s->status = ativo;
  s->fila = NULL;
  s->value = value;
  return 0;
}

// requisita o semáforo
int sem_down (semaphore_t *s)
{
  if(s != NULL && s->status != destruido)
  {
    s->value = s->value - 1;
    if(s->value < 0)
    {
      current->status = suspensa;
      task_suspend(NULL,&(s->fila));
      if(s->status == destruido)
        return -1;
    }
    return 0;
  }
  return -1;
}

// libera o semáforo
int sem_up (semaphore_t *s)
{
  if(s != NULL && s->status != destruido)
  {
    s->value = s->value + 1;
    if(s->value <= 0)
    {
      task_t *aux = s->fila;
      task_t *u = (task_t*) queue_remove((queue_t**)&(s->fila),(queue_t*)aux);
      queue_append((queue_t**)&filaProntas,(queue_t*)u);
      u->status = pronta;
      userTasks++;
    }
    return 0;
  }
  return -1;
}

// destroi o semáforo, liberando as tarefas bloqueadas
int sem_destroy (semaphore_t *s)
{
  s->status = destruido;
  while(queue_size((queue_t*)s->fila) > 0)
  {
    task_t *aux = s->fila;
    task_t *u = (task_t*) queue_remove((queue_t**)&(s->fila),(queue_t*)aux);
    u->status = pronta;
    queue_append((queue_t**)&filaProntas,(queue_t*)u);
    userTasks++;
  }
  return 0;
}

// barreiras

// Inicializa uma barreira
int barrier_create (barrier_t *b, int N)
{
  if(N > 0)
  {
    b->fila = NULL;
    b->N = N;
    b->n = 0;
    b->status = ativo;
    return 0;
  }
  return -1;
}

// Chega a uma barreira
int barrier_join (barrier_t *b)
{
  if(b != NULL && b->status != destruido)
  {
    b->n = (b->n)+1;
    if(b->N == b->n)
    {
      while(queue_size((queue_t*)b->fila) > 0)
      {
        task_t *aux = b->fila;
        if(aux->tid == 0 || queue_size((queue_t*)b->fila) > 1)
          aux = aux->next;
        task_t *u = (task_t*) queue_remove((queue_t**)&(b->fila),(queue_t*)aux);
        u->status = pronta;
        queue_append((queue_t**)&filaProntas,(queue_t*)u);
        userTasks++;

      }
      b->n = 0;
    }
    else
    {
      current->status = suspensa;
      queue_append((queue_t**)&(b->fila),(queue_t*)current);
      task_switch(&dispatcher);
      if(b->status == destruido)
        return -1;
    }
    return 0;
  }
  return -1;
}

// Destrói uma barreira
int barrier_destroy (barrier_t *b)
{
  b->status = destruido;
  while(queue_size((queue_t*)b->fila) > 0)
  {
    task_t *aux = b->fila;
    task_t *u = (task_t*) queue_remove((queue_t**)&(b->fila),(queue_t*)aux);
    u->status = pronta;
    queue_append((queue_t**)&filaProntas,(queue_t*)u);
    userTasks++;
  }
  return 0;
}

// filas de mensagens

// cria uma fila para até max mensagens de size bytes cada
int mqueue_create (mqueue_t *queue, int max, int size)
{
  if(max >= 0 && queue != NULL && size >= 0)
  {
    sem_create(&(queue->s_vaga),max);
    sem_create(&(queue->s_msg),0);
    sem_create(&(queue->s_mequeue),1);
    queue->size = size;
    queue->nmsg = 0;
    queue->msg = malloc(sizeof(size));
    queue->status = ativo;
    return 0;
  }
  return -1;
}

// envia uma mensagem para a fila
int mqueue_send (mqueue_t *queue, void *msg)
{
  if(queue != NULL && msg != NULL)
  {
    sem_down(&(queue->s_vaga));
    if(queue->status == destruido)
      return -1;
    sem_down(&(queue->s_mequeue));
    memcpy(queue->msg + queue->nmsg*queue->size,msg,queue->size);
    (queue->nmsg)++;
    sem_up(&(queue->s_mequeue));
    sem_up(&(queue->s_msg));
    return 0;
  }
  return -1;
}

// recebe uma mensagem da fila
int mqueue_recv (mqueue_t *queue, void *msg)
{
  if(queue != NULL && msg != NULL)
  {
    sem_down(&(queue->s_msg));
    if(queue->status == destruido)
      return -1;
    sem_down(&(queue->s_mequeue));
    memcpy(msg, queue->msg, queue->size);
    for(int i = 1; i < (queue->nmsg); i++)
      memcpy(queue->msg+(i-1)*queue->size, queue->msg + i*queue->size, queue->size);
    (queue->nmsg)--;
    sem_up(&(queue->s_mequeue));
    sem_up(&(queue->s_vaga));
    return 0;
  }
  return -1;
}

// destroi a fila, liberando as tarefas bloqueadas
int mqueue_destroy (mqueue_t *queue)
{
  if(queue != NULL)
  {
    sem_destroy(&(queue->s_msg));
    sem_destroy(&(queue->s_vaga));
    free(queue->msg);
    queue->status = destruido;
    return 0;
  }
  return -1;
}

// informa o número de mensagens atualmente na fila
int mqueue_msgs (mqueue_t *queue)
{
  if(queue != NULL)
  {
    return queue->nmsg;
  }
  return -1;
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
    while ( userTasks > 0 || filaAdormecidas != NULL)
    {
        if(filaAdormecidas != NULL)
        {
            task_t *aux = filaAdormecidas;
            do{
                task_t *remove = aux;
                aux = aux->next;
                if(remove->acordaEm <= clock)
                {
                    queue_append((queue_t**)&filaProntas,queue_remove((queue_t**)&filaAdormecidas,(queue_t*)remove));
                    ++userTasks;
                }
            }while(aux!=filaAdormecidas && filaAdormecidas != NULL);
        }
        if(filaProntas != NULL)
        {
            next = scheduler() ; // scheduler é uma função
            if (next)
            {
                //Enquanto houverem tarefas de usuário, scheduler seleciona
                //a próxima tarefa de acordo com a política implementada
                //e retorna para que possa ser executada;
                next->quantum = 20;
                //Computa quantas vezes a tarefa foi ativada;
                next->act++;
                task_switch (next) ; // transfere controle para a tarefa "next"
                userTasks--;
            }
        }
    }
    task_exit(0) ; // encerra a tarefa dispatcher
}

void tratador (int signum)
{
    //Variavel para armazenar o tempo total de execução
    //do programa;
    clock++;
    //Verifica se é uma tarefa de usuário, se for retira um quantum
    //e logo em seguida verifica se o quantum acabou para trocar de tarefa;
    if(current != &dispatcher)
    {
        current->quantum--;
        //Computa o tempo total de processamento da tarefa;
        current->processTime++;
        if(current->quantum == 0)
        {
            task_yield();
        }
    }
}
