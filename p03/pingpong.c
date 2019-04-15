#include <ucontext.h>
#include <stdio.h>
#include <stdlib.h>
#include "pingpong.h"
#include "queue.h"
#define STACKSIZE 32768		/* tamanho de pilha das threads */
#define N 100

typedef struct filatasks_t
{
   struct filatasks_t *prev;
   struct filatasks_t *next;
   task_t *t;
} filatasks_t ;

int taskid, userTasks;
task_t contextMain, *current, *dispatcher;
filatasks_t item;
filatasks_t *filaProntas;

// funções gerais ==============================================================

task_t *scheduler()
{
    filatasks_t *aux;
    aux = (filatasks_t*) queue_remove((queue_t**)&filaProntas,(queue_t*)filaProntas);
    return aux->t;
}

void dispatcher_body ()
{
    task_t* next;
    while ( userTasks > 0 )
    {
        next = scheduler();
        if (next)
        {
            task_switch (next);
        }
        userTasks--;
    }
    task_exit(0);
}


void pingpong_init()
{
    /* desativa o buffer da saida padrao (stdout), usado pela função printf */
    setvbuf (stdout, 0, _IONBF, 0);
    taskid = 0;
    userTasks = 0;
    contextMain.tid = taskid;
    current = &contextMain;
    task_create(dispatcher,(void *)dispatcher_body,NULL);
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
    makecontext(&(task->context),(void*)(*start_func),1,arg);
#ifdef DEBUG
    printf("task_create: criou a tarefa: %d\n", task->tid);
#endif
    userTasks++;
    return task->tid;
}

void task_exit (int exitCode)
{
#ifdef DEBUG
    printf("task_exit: tarefa %d sendo encerrada\n", current->tid);
#endif
    task_switch(&contextMain);
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

// operações de escalonamento ==================================================

void task_yield ()
{
    item.next = NULL;
    item.prev = NULL;
    item.t = current;
    queue_append((queue_t**) &filaProntas,(queue_t*) &item);
    task_switch(dispatcher);
}

// =============================================================================
