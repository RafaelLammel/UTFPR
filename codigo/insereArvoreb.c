#include "arvoreb.h"

/*Descrição: Divide o nó em dois quando ele está cheio*/
Arvore* dividir_no (Arvore *x, int i, Arvore *y) {
    int j = 0;
   //Cria um novo nó que será o filho à direita da chave mediana;
   Arvore *z = criar();
   z->folha = y->folha;
   z->n = T-1;
   //Faz com que Z receba as chaves maiores após a mediana de y;
   for (j = 0; j < T-1; j++){
        z->chaves[j] = y->chaves[j+T];
   }
   //Verifica se y é uma folha e se for, passa os filhos das chaves maiores para Z;
   if (!y->folha){
       for(j = 0; j < T; j++){
            z->filhos[j] = y->filhos[j+T];
       }
   }
   y->n = T-1;
   //Movendo os filhos para abrir espaço para o novo nó Z;
   for (j = x->n+1; j >= i+1; j--){
        x->filhos[j] = x->filhos[j-1];
   }
   x->filhos[i+1] = z;
   //movendo as chaves para colocar a mediana de Y em X;
   for (j = x->n; j >= i; j--){
        x->chaves[j] = x->chaves[j-1];
   }
   x->chaves[i] = y->chaves[T-1]; //jogando a mediana para cima;
   x->n = x->n+1; //acrescentando n;
   return x;
}

/*Descrição: Insere chave na arvore quando seu nó não está cheio*/
Arvore* inserir_arvore_nao_cheia (Arvore *x, TIPO k) {
   int i = x->n;
   if(x->folha){
        //"liberando" espaço para o K entrar.
        while(i>0 && k < x->chaves[i-1]){
            x->chaves[i] = x->chaves[i-1];
            i=i-1;
        }
        x->chaves[i] = k;
        x->n++;
   }
   else{
        //encontrando o filho aonde o K deve entrar.
        while(i>0 && k < x->chaves[i-1]){
            i--;
        }
        if(x->filhos[i]->n == (2*T)-1){  //Verificar se o filho está cheio.
            dividir_no(x,i,x->filhos[i]);
            if (k > x->chaves[i]){
              i=i+1;
            }
        }
        inserir_arvore_nao_cheia (x->filhos[i], k);
   }
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
