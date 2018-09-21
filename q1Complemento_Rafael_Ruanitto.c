/// Aluno: Rafael Lammel Marinheiro - Matricula: 1986856
/// Aluno: Ruanitto Docini - Matricula: 1908561
#include <stdio.h>

void precisaoSimples(float A){

    float soma, prec;
    A = 0.5;
    soma = 1+A;

    while(soma > 1){
        A=A/2;
        soma = 1+A;
    }
    prec = A*2;
    printf("%.24f\n",prec);
}

void precisaoDupla(double A){


    double soma, prec;
    A = 0.5;
    soma = 1+A;

    while(soma > 1){
        A/=2;
        soma = 1+A;
    }
    prec = A*2;
    printf("%.32lf",prec);

}

void epsUsuario(double A, int valor){


    double soma, prec;
    A = 0.5;
    soma = valor+A;

    while(soma > valor){
        A/=2;
        soma = valor+A;
    }
    prec = A*2;
    printf("%.55f",prec);

}

int main(){

    float num;
    double num2;
    precisaoSimples(num);
    precisaoDupla(num2);
    epsUsuario(num2,1000000);

}
