#include "arvoreb.h"

/*Função para buscar uma chave em uma árvore B:*/
int buscar (Arvore *a, TIPO chave) {

   int i = 0;

   /*Procurando a chave no vetor de chaves: */
   while ((i < a->n) && (chave > a->chaves[i])) {
     i = i + 1; 
   }
   
   if (chave == a->chaves[i]) {
      /*Achou a chave!*/
      return i;
   }  
   else if (a->folha) {
      /*Não achou a chave!*/
      return NOT_FOUND;
   }
   else {
      /*Buscando a chave no filho: */
      if (a->folha == FALSE) {
         return buscar (a->filhos[i], chave);
      }
   }
}
