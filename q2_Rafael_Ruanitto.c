///Nome: Rafael L. Marinheiro - Matricula: 1986856
///Nome: Ruanitto R. Docini - Matricula: 1908561
#include <stdio.h>
#include <math.h>
long long pow5 (long long n){ //Função pra fazer um numero elevado a 5;

    long long result;
    result = n*n;
    result *= result * n;
    return result;
}

main(){

    long long a, b, c, d, e, soma, vetor[250], i;

    for (i = 1; i < 250; i++)
        vetor[i] = pow5(i);

    for (a = 4; a > 0; a++)
        for (b = a-1; b>0; b--)
            for (c = b-1; c>0; c--)
                for (d = c-1; d>0; d--){
                    soma = vetor[a]+vetor[b]+vetor[c]+vetor[d];
                    e = pow(soma,(1.f/5.f)); //se a soma de cada termo^5 = e^5, podemos dizer que e = (soma)^1/5;
                    if (soma == pow5(e)){
                        printf("%llu^5 + %llu^5 + %llu^5 + %llu^5 = %llu^5",d,c,b,a,e);
                        a = -1;
                        b = -1;
                        c = -1;
                        d = -1;
                        e = -1;
                    }
                }
}
