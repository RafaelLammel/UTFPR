// PingPongOS - PingPong Operating System
// Prof. Carlos A. Maziero, DAINF UTFPR
// Versão 1.0 -- Março de 2015
//
// Estruturas de dados internas do sistema operacional

/*
Modificações - Autores:
Kelvin James
Rafael Lammel Marinheiro
*/

#include <ucontext.h>

#ifndef __DATATYPES__
#define __DATATYPES__

enum stat{pronta, suspensa};

// Estrutura que define uma tarefa
typedef struct task_t
{
  struct task_t *prev, *next;
  int tid;
  int status;
  int staticPrio;
  int dynamicPrio;
  int quantum;
  unsigned int createTime;
  unsigned int exitTime;
  unsigned int processTime;
  unsigned int act;
  ucontext_t context;
} task_t ;

// estrutura que define um semáforo
typedef struct
{
  // preencher quando necessário
} semaphore_t ;

// estrutura que define um mutex
typedef struct
{
  // preencher quando necessário
} mutex_t ;

// estrutura que define uma barreira
typedef struct
{
  // preencher quando necessário
} barrier_t ;

// estrutura que define uma fila de mensagens
typedef struct
{
  // preencher quando necessário
} mqueue_t ;

#endif