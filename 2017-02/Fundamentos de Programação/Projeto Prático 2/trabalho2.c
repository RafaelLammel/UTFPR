/// Aluno: Rafael Lammel Marinheiro - Matricula: 1986856
/// Aluno: Ruanitto Docini - Matricula: 1908561
#include <string.h>
#include <stdlib.h>

#include "trabalho2.h"

void mudaGanho (double* dados, unsigned long n_amostras, double ganho){
    //Multiplicamos cada membro do vetor por um numero fornecido pra alterar o ganho;
    int i;
    for(i = 0; i < n_amostras; i++)
        dados[i]=dados[i]*ganho;

}

void ruidoPeriodico (double* dados, unsigned long n_amostras, unsigned short intervalo){
    //Adicionamos 1 ou -1 alternadamente para termos os estalos;
    int i, cont = 0;
    for(i = 0; i < n_amostras; i+=intervalo)
        if(cont == 0){
            dados[i] = 1;
            cont = 1;
        }
        else{
            dados[i] = -1;
            cont = 0;
        }


}

void removeRuido (double* dados, unsigned long n_amostras){
    //Pegamos a mediana de um valor na posição do vetor e seus dois vizinhos
    //E colocamos o resultado na posição atual;
    double *aux = dados;
    int i;
    for(i = 1; i < n_amostras-1; i++){
        if((aux[i] < aux[i-1] && aux[i-1] < aux[i+1]) ||
           (aux[i] > aux[i-1] && aux[i-1] > aux[i+1]))
            dados[i] = aux[i-1];
        else if ((aux[i] > aux[i-1] && aux[i-1] > aux[i+1]) ||
                (aux[i] < aux[i-1] && aux[i-1] < aux[i+1]))
            dados[i] = aux[i+1];
    }

}
