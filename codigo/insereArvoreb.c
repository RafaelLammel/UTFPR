#include "arvoreb.h"

/*Descrição: ????*/
Arvore* dividir_no (Arvore *x, int i, Arvore *y) {
   /*Completar!!!!!!!!!!!!!!*/
   printf("Completar\n");
   return x;
}

/*Descrição: ????*/
Arvore* inserir_arvore_nao_cheia (Arvore *x, TIPO k) {
   /*Completar!!!!!!!!!!!!!!*/
   printf("Completar\n");
   return x;
}

/*Função para inserir uma chave em uma árvore B:*/
Arvore *inserir (Arvore *raiz, TIPO chave) {
   Arvore *r = raiz;
   if (r->n == (2*T - 1)) {
      Arvore *s = criar(); 
      s->folha = FALSE;
      s->filhos[0] = r;
      s = dividir_no (s, 0, r);
      s = inserir_arvore_nao_cheia (s, chave);
      return s; 
   }
   else {
      return inserir_arvore_nao_cheia (r, chave);
   }
}




