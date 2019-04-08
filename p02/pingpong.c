#include "pingpong.h"
#include <ucontext.h>
#include <stdio.h>
#include <stdlib.h>
#define STACKSIZE 32768		/* tamanho de pilha das threads */

int taskid;
ucontext_t contextMain;

// funções gerais ==============================================================

void pingpong_init()
{
  /* desativa o buffer da saida padrao (stdout), usado pela função printf */
  setvbuf (stdout, 0, _IONBF, 0);
  taskid = 0;
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
     task->tid = taskid++;
  }
  makecontext(&(task->context),(void*)(*start_func),1,arg);
  return task->tid;
}

void task_exit (int exitCode)
{

}

int task_switch (task_t *task)
{
  swapcontext(&contextMain,&(task->context));
  return 0;
}

int task_id ()
{

}
