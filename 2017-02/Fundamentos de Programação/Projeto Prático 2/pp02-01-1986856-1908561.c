/// Aluno: Rafael Lammel Marinheiro - Matricula: 1986856
/// Aluno: Ruanitto Docini - Matricula: 1908561
#include <stdio.h>

int numeroDias(int mes); //Retorna a quantidade de dias que o mês tem;
int anoBissexto(int ano);
char * pegaMes(int mes); //Retorna o nome do mês em Discordiano;

main(){


    int dia, ano, mes, mesD;

    printf("Insira o dia, mes e ano separados por espaços: ");
    scanf("%d %d %d", &dia, &mes, &ano);
    //Para obter o ano discordiano, basta adicionar 1166;
    ano+=1166;

    //Se o ano for bissexto e for 29 de fevereiro, é feriado de St Tib's Day,
    //portanto basta apresentar isso e o ano na tela;
    if(anoBissexto(ano-1166) == 1 && dia == 29 && mes == 2)
            printf("É dia São Tib! de YOLD %d", ano);
    else{
        //Tiramos um do mês pois precisamos apenas dos mesês completos;
        mes-=1;
        while(mes >= 1){
            //Adicionamos ao dia fornecido os dias de todos os meses que já foram
            //para ter o numero de dias completos no ano;
            dia += numeroDias(mes);
            mes--;
        }
        //mesD recebe a divisão inteira dos dias do ano por 73, e obtemos o mês em discordiano;
        //Removemos o numero de meses multiplicado por 73 do numero total de dias, para ter o dia
        //daquele mês em discordiano;
        mesD = (dia-1)/73;
        dia = dia-(mesD*73);

        printf("%s day %d, YOLD %d",pegaMes(mesD),dia,ano);
    }
}

int anoBissexto(int ano){
    if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0))
        return 1;
    return 0;
}

int numeroDias(int mes){

    if (mes == 1 ||
        mes == 3 ||
        mes == 5 ||
        mes == 7 ||
        mes == 8 ||
        mes == 10 ||
        mes == 12)
        return 31;
    else if (mes == 2)
        return 28;
    else
        return 30;
}

char * pegaMes(int mes){

    if(mes == 0)
        return "Chaos";
    if (mes == 1)
        return "Discord";
    if (mes == 2)
        return "Confusion";
    if (mes == 3)
        return "Bureacracy";

    return "The Aftermath";

}
