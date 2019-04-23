#include "queue.h"
#include <stdio.h>

void queue_append (queue_t **queue, queue_t *elem)
{
    queue_t *aux;
    if(queue == NULL)
    {
        printf("ERRO: Fila não existe!\n");
        return;
    }
    if(elem == NULL)
    {
        printf("ERRO: Elemento não existe!\n");
        return;
    }
    if(elem->next != NULL || elem->prev != NULL)
    {
        printf("ERRO: Esse elemento já está em outra fila!\n");
        return;
    }
    if((*queue) == NULL)
    {
        elem->next = elem;
        elem->prev = elem;
        *queue = elem;
    }
    else
    {
        aux = (*queue)->prev;
        aux->next = elem;
        elem->prev = aux;
        (*queue)->prev = elem;
        elem->next = (*queue);
    }
}

queue_t *queue_remove (queue_t **queue, queue_t *elem)
{
    queue_t *aux;
    queue_t *teste;
    if(queue == NULL)
    {
        printf("ERRO: Fila não existe!\n");
        return NULL;
    }
    if((*queue) == NULL)
    {
        printf("ERRO: Fila vazia!\n");
        return NULL;
    }
    if(elem == NULL)
    {
        printf("ERRO: Elemento não existe!\n");
        return NULL;
    }
    aux = (*queue);
    do
    {
        if(aux == elem)
        {
            if(aux->next != aux)
            {
                if(aux == (*queue))
                {
                    (*queue) = aux->next;
                }
                aux->prev->next = aux->next;
                aux->next->prev = aux->prev;
                aux->prev = NULL;
                aux->next = NULL;
                return aux;
            }
            else
            {
                aux->next = NULL;
                aux->prev = NULL;
                (*queue) = NULL;
                return aux;
            }
        }
        aux = aux->next;
    }
    while(aux!=(*queue));
    printf("ERRO: Elemento não está na fila!\n");
    return NULL;
}

int queue_size (queue_t *queue)
{
    int i = 0;
    queue_t *aux;
    if(queue == NULL)
    {
        return i;
    }
    aux = queue;
    do
    {
        i++;
        aux = aux->next;
    }
    while(aux!=queue);
    return i;
}

void queue_print (char *name, queue_t *queue, void print_elem (void*))
{
    queue_t *aux;
    printf("%s",name);
    aux = queue;
    if(queue == NULL)
    {
        printf("[]\n");
    }
    else
    {
        printf("[");
        do
        {
            print_elem(aux);
            aux = aux->next;
            printf(" ");
        }
        while(aux != queue);
        printf("]\n");
    }
}
