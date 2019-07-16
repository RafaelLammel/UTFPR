// PingPongOS - PingPong Operating System
// Prof. Carlos A. Maziero, DAINF UTFPR
// Versão 1.0 -- Março de 2015
//
// Estruturas de dados internas do sistema operacional

#include <ucontext.h>

#ifndef __DATATYPES__
#define __DATATYPES__

enum stat{pronta, suspensa, adormecida, terminada};
enum statSemBarr{destruido, ativo};

// Estrutura que define uma tarefa
typedef struct task_t
{
  struct task_t *prev, *next;
  int tid;
  int status;
  int staticPrio;
  int dynamicPrio;
  float quantum;
  int exitCode;
  int tarefab;
  int acordaEm;
  unsigned int createTime;
  unsigned int exitTime;
  unsigned int processTime;
  unsigned int act;
  ucontext_t context;
} task_t ;

// estrutura que define um semáforo
typedef struct
{
  task_t *fila;
  int value;
  int status;
} semaphore_t ;

// estrutura que define um mutex
typedef struct
{
  // preencher quando necessário
} mutex_t ;

// estrutura que define uma barreira
typedef struct
{
  task_t *fila;
  int N;
  int n;
  int status;
} barrier_t ;

// estrutura que define uma fila de mensagens
typedef struct
{
  void *msg;
  semaphore_t s_msg;
  semaphore_t s_vaga;
  semaphore_t s_mequeue;
  int size;
  int nmsg;
  int status;
} mqueue_t ;

#endif
