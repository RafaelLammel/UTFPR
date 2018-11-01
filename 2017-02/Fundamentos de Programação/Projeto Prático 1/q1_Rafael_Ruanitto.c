///Nome: Rafael L. Marinheiro - Matricula: 1986856
///Nome: Ruanitto R. Docini - Matricula: 1908561
#include <stdio.h>
#include <math.h>

void desenhaCalendario(int ano, int mes); //Desenha na tela o calendario do mes e ano especificado;
void pegaMes(int mes); //Serve para imprimir na tela o nome do m�s dado;
int numeroDias(int mes); //Retorna quantos dias tem no m�s;
int anoBissexto(int ano); //Retorna 1 se um ano for bissexto, e 0 se n�o for;

main(){

	int mes, ano;

	printf("Insira o ano e o mes: ");
	scanf("%d %d", &ano, &mes);
	desenhaCalendario(ano,mes);

}

void desenhaCalendario(int ano, int mes){

	int qtdDias, i, contaDias = 0, k, h, j;

	printf("            ");
	qtdDias = numeroDias(mes);
	pegaMes(mes);
	printf (" de %d", ano);
	printf("\nDOM   SEG   TER   QUA   QUI   SEX   SAB\n");

    if (mes == 2 && anoBissexto(ano))
        qtdDias+=1;

    /*Verifica se o mes fornecido � janeiro ou fevereiro, pois na f�rmula utilizada
    eles tem que ser usados como mes "13" e "14" respectivamente, e caso seja, o ano precisa
    ser o ano anterior;*/
    if (mes < 3){
        mes+=12;
        ano-=1;
    }

    k = ano%100;
    j = ano/100;
    h = 1+(((mes+1)*13)/5)+k+(k/4)+(j/4)+5*j; //F�rmula que verifica em qual dia da semana
    h %= 7;

    //Nesse if/else fazemos a convers�o do resultado acima,
    //j� que nosso calendario comeca no Domingo, e n�o no sabado;
    if (h == 0)
        h = 6;
    else
        h-=1;

    //Printamos espa�os nos dias "vazios" do come�o do calendario
    //(do mes anterior) at� chegarmos no dia primeiro;
    for (i = 0; i < h; i++){
        printf("      ");
        contaDias++; //Variavel que acumula at� o numero 7, seu uso � mais presente no for abaixo;
    }

	for (i = 1; i <= qtdDias; i ++){

		if(i < 10)
			printf("  "); //Printa 2 espa�os se o numero tiver s� um algarismo;
		else
			printf(" "); //Printa 1 espa�o se o numero tiver 2 algarismos;

		printf("%d", i);
		printf("   ");

		contaDias++;
		if(contaDias == 7){ //Se a contagem chegar ao numero 7, ele pula uma linha e reinicia a contagem (fechou uma semana);
			printf("\n");
			contaDias = 0;
		}
	}
}

int anoBissexto(int ano){
	if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0))
		return 1;
	return 0;
}

void pegaMes(int mes){
	if (mes == 1)
		printf("JANEIRO");
	if (mes == 2)
		printf("FEVEREIRO");
	if (mes == 3)
		printf("MARCO");
	if (mes == 4)
		printf("ABRIL");
	if (mes == 5)
		printf("MAIO");
	if (mes == 6)
		printf("JUNHO");
	if (mes == 7)
		printf("JULHO");
	if (mes == 8)
		printf("AGOSTO");
	if (mes == 9)
		printf("SETEMBRO");
	if (mes == 10)
		printf("OUTUBRO");
	if (mes == 11)
		printf("NOVEMBRO");
	if (mes == 12)
		printf("DEZEMBRO");
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
