/// Aluno: Rafael Lammel Marinheiro - Matricula: 1986856
/// Aluno: Ruanitto Docini - Matricula: 1908561
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

float precisaoSimples(){

    float soma, prec, A = 0.5;
    soma = 1+A;

    while(soma > 1){
        A=A/2;
        soma = 1+A;
    }
    prec = A*2;
    return prec;

}

float funcaoB(float x){
    return (pow(x,2)-pow(2,x));

}

float funcaoA(float x){

    return pow(x,3)-(9*x)+(3);

}

void bisseccao(float x1, float x2){

    float x, e = precisaoSimples();

    if(funcaoA(x1)*funcaoA(x2)<0){
        x = (x1+x2)/2;
        while(abs(funcaoA(x)) > e){
            if(funcaoA(x1)*funcaoA(x) <= 0)
                x2 = x;
            else
                x1 = x;
            x = (x1+x2)/2;
        }
        printf("A raiz é %.5f", x);
    }
    else
        printf("Não há raizes no intervalo dado");

}

int main(){

    bisseccao(-5,-2);

}
