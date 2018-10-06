#include "arvoreb.h"

/*Descrição: Divide o nó em dois quando ele está cheio*/
Arvore* dividir_no (Arvore *x, int i, Arvore *y) {
    printf("Entrei aqui dividir no!\n");
   //Cria um novo nó que receberá os maiores elemntos de y;
   Arvore *z = criar();
   z->folha = y->folha;
   z->n = T-1;
   //Faz com que Z receba as chaves maiores após a mediana de y;
   for (int j = 0; j < T-1; j++){
        z->chaves[j] = y->chaves[j+T+1];
   }
   //Verifica se y é uma folha e se for passa os filhos das chaves maiores para Z;
   if (!y->folha){
       for(int j = 0; j < T; j++){
            z->filhos[j] = y->filhos[j+T+1];
       }
   }
   y->n = T-1;
   //Movendo os filhos para abrir espaço para o novo nó Z;
   for (int j = x->n+1; j > i+1; j--){
        x->filhos[j+1] = x->filhos[j];
   }
   x->filhos[i] = z;
   //movendo as chaves para colocar a mediana de Y em X;
   for (int j = x->n; j > i; j--){
        x->chaves[j+1] = x->chaves[j];
   }
   x->chaves[i] = y->chaves[T];
   x->n = x->n+1;
   return x;
}

/*Descrição: ????*/
Arvore* inserir_arvore_nao_cheia (Arvore *x, TIPO k) {
    printf("Entrei aqui Nao cheia!\n");
   int i = x->n;
   if(x->folha){
        while(i>0 && k < x->chaves[i-1]){
            x->chaves[i] = x->chaves[i-1];
            i=i-1;
        }
        x->chaves[i] = k;
        x->n++;
        return x;
   }
   else{
        while(i>0 && k < x->chaves[i-1]){
            i = i-1;
        }
        //i = i+1;
        if(x->filhos[i]->n = (2*T)-1){
            dividir_no(x,i,x->filhos[i]);
        }
        inserir_arvore_nao_cheia (x->filhos[i], k);
   }
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




