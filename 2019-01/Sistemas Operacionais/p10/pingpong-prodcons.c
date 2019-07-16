#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "pingpong.h"
#include "queue.h"

// operating system check
#if defined(_WIN32) || (!defined(__unix__) && !defined(__unix) && (!defined(__APPLE__) || !defined(__MACH__)))
#warning Este codigo foi planejado para ambientes UNIX (LInux, *BSD, MacOS). A compilacao e execucao em outros ambientes e responsabilidade do usuario.
#endif

#define FSIZE 5

int item;
semaphore_t s_buffer, s_item, s_vaga;
task_t p1, p2, p3, c1, c2;
typedef struct buffer{
  struct buffer *prev, *next;
  int valor;
}buffer;

buffer *b;

void produtor(void * arg)
{
  while(1)
  {
    task_sleep(1);
    buffer *aux = malloc (sizeof(buffer));
    item = random()%100;
    aux->valor = item;
    sem_down(&s_vaga);
    sem_down(&s_buffer);
    queue_append((queue_t**)&b, (queue_t*)aux);
    sem_up(&s_buffer);
    sem_up(&s_item);
    printf("%s produziu %d\n", (char*)arg, item);
  }
  task_exit(0);
}

void consumidor(void * arg)
{
  while(1)
  {
    buffer *i;
    sem_down(&s_item);
    sem_down(&s_buffer);
    i = (buffer*) queue_remove((queue_t**)&b,(queue_t*)b);
    sem_up(&s_buffer);
    sem_up(&s_vaga);
    printf("                              %s consumiu %d\n", (char*)arg, i->valor);
    task_sleep(1);
  }
  task_exit(0);
}

int main()
{
  pingpong_init();

  //Apenas um por vez tem acesso ao Buffer;
  sem_create(&s_buffer,1);
  //Inicia com 0 items no Buffer
  sem_create(&s_item,0);
  //5 vagas no Buffer
  sem_create(&s_vaga,5);

  task_create(&p3,produtor,"p3");
  task_create(&p2,produtor,"p2");
  task_create(&p1,produtor,"p1");
  task_create(&c1,consumidor,"c1");
  task_create(&c2,consumidor,"c2");

  task_join(&p1);
  /*task_join(&p2);
  task_join(&p3);
  task_join(&c1);
  task_join(&c2);*/

  sem_destroy(&s_buffer);
  sem_destroy(&s_item);
  sem_destroy(&s_vaga);

  task_exit(0);
  exit(0);
}
