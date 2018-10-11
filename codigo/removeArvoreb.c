#include "arvoreb.h"

/*Remove chave de nó folha (Caso 1)*/
Arvore* remover_de_folha (Arvore *a, int index){
   int i = 0;
    a->n--;
    for (i = index; i < a->n ; i++){
        a->chaves[i] = a->chaves[i+1];
    }
   return a;
}


/*Remove quando a chave está em um nó que não é folha. (Todos os casos 2)*/
Arvore* remover_de_nao_folha (Arvore *a, int index){
   TIPO k = a->chaves[index];
   TIPO predecessor, sucessor;
   Arvore* maxEsq = buscaMaxEsq(a->filhos[index]);
   Arvore* minDir = buscaMinDir(a->filhos[index+1]);
   /*Verfica se o filho à esquerda tem número de chaves maior que o mínimo (Caso 2A)*/
   if (maxEsq->n >= T){
      //a->chaves[index] = a->filhos[index]->chaves[a->filhos[index]->n-1];
      //a->filhos[index] = remover(a->filhos[index],a->filhos[index]->chaves[a->filhos[index]->n-1]);
      predecessor = maxEsq->chaves[maxEsq->n-1];
      a->chaves[index] = predecessor;
      a->filhos[index] = remover(a->filhos[index],predecessor);
   }
   /*Verfica se o filho à direita tem número de chaves maior que o mínimo (Caso 2B)*/
   else if (minDir->n >= T){
      //a->chaves[index] = a->filhos[index+1]->chaves[0];
      //a->filhos[index+1] = remover(a->filhos[index+1],a->filhos[index+1]->chaves[0]);
      sucessor = minDir->chaves[0];
      a->chaves[index] = sucessor;
      a->filhos[index+1] = remover(a->filhos[index+1],sucessor);
   }
   /*Quando nenhum dos filhos tem número de chaves maior que o mínimo (Caso 2C)*/
   else{
     predecessor = maxEsq->chaves[maxEsq->n-1];
     a->chaves[index] = predecessor;

   }

   return a;
}


//Função para verificar se raiz ficou vazia
Arvore *verificar_raiz_vazia (Arvore *raiz){
    /*Se após a remoção a raiz tiver 0 chaves, tornar o primeiro filho a nova raiz se existir filho; caso contrário ajustar a raiz para NULL. Liberar a raiz antiga*/
   if(raiz->n == 0){
    Arvore* a = raiz;
     raiz = raiz->filhos[0];
     free(a);
   }

   return raiz;
}



/*Função que retorna o index da primeira chave maior ou igual à chave*/
int buscar_index_remocao (Arvore *a, TIPO chave) {

   int i = 0;

   /*Procurando a chave no vetor de chaves */
   while ((i < a->n) && (chave > a->chaves[i])) {
     i = i + 1;
   }

   return i;
}

/*Descrição: Verifica qual caso de remoção deve ser aplicado*/
Arvore *remover (Arvore *a, TIPO k){
   int index;

   /*Se a árvore está vazia, nenhuma remoção pode ser feita*/
   if (a == NULL) {
      printf("Árvore vazia!");
      return a;
   }

   index = buscar_index_remocao (a, k);


   //A chave a ser removida está presente neste nó
   if ( index >= 0 && a->chaves[index] == k){
      if (a->folha){
         a = remover_de_folha (a, index);
      }
      else{
         a = remover_de_nao_folha (a, index);
      }
   }
   else{
      //Se este nó é um nó folha, então a chave não está na árvore
      if (a->folha){
  	 printf("\nA chave %c não está na árvore.\n",k);
  	 //printf("\nA chave %d não está na árvore.\n",k);
         return a;
      }

      /*Chama a função recursivamente até que chegue no nó que contém a chave, ou em uma folha, caso a árvore não possua a chave*/
      remover(a->filhos[index],k);
      /*Verifica como ficou o filho após a remoção, e se necessário balancea a árvore (Caso 3A)*/
      if (a->filhos[index]->n < T-1){
        if (index+1 < a->n+1 && a->filhos[index+1]->n > T-1){
            inserir(a->filhos[index],a->chaves[index]);
            a->chaves[index] = a->filhos[index+1]->chaves[0];
            remover(a->filhos[index+1],a->filhos[index+1]->chaves[0]);
        }
        else if (index-1 >= 0 && a->filhos[index-1]->n > T-1){
            inserir(a->filhos[index],a->chaves[index-1]);
            a->chaves[index-1] = a->filhos[index-1]->chaves[a->filhos[index-1]->n-1];
            remover(a->filhos[index-1],a->filhos[index-1]->chaves[a->filhos[index-1]->n-1]);
        }
      }
   }
   a = verificar_raiz_vazia(a);

   return a;
}

