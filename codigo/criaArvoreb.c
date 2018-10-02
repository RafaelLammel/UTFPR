#include "arvoreb.h"

/*Função para criar e inicializar uma árvore B:*/
Arvore* criar () {
   Arvore *a = (Arvore *)malloc(sizeof(Arvore));
   a->n = 0;
   a->folha = TRUE;
   return a;
}
