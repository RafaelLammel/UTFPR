///Nome: Rafael L. Marinheiro - Matricula: 1986856
///Nome: Ruanitto R. Docini - Matricula: 1908561
#include <stdio.h>

//Função pra retornar o equivalente em numero arabico para um romano simples (um algarismo);
int converteRomano(char algarismoRom);

main(){
	char algarismoRom;
	int numero = 0, numeroAnterior = 0, numeroArab = 0;
	printf("Insira um numero romano: ");

	scanf("%c",&algarismoRom);
	numero = converteRomano(algarismoRom); //Pegamos o equivalente do digito romano em arabico;
	numeroArab += numero; //Como o primeiro digito só vai ter uma letra, apenas somamos ele ao total;
	numeroAnterior = numero; //Salvamos ele em uma auxiliar para comparar caso precise;

	while(algarismoRom != '\n'){//Ficamos lendo Chars até o usuario apertar ENTER;
		scanf("%c",&algarismoRom);
		numero = converteRomano(algarismoRom);
		if(numeroAnterior < numero){//A unica preocupação é quando tiver um numero romano menor antes de um maior,
		    //Pois quando isso ocorre precisamos fazer algumas alterações no total;
			numeroArab -= numeroAnterior; //Retiramos o valor do numero anterior do total;
			numero -= numeroAnterior; //E retiramos também do numero atual, para soma-lo abaixo;
		}
		numeroArab += numero; //Adicionamos o numero atual ao total;
		numeroAnterior = numero;
	}
	printf("O numero convertido do romano eh: %d", numeroArab);
}

int converteRomano(char algarismoRom){
	int armazena = 0;
	if(algarismoRom == 'I')
		armazena = 1;
	else if(algarismoRom == 'V')
		armazena = 5;
	else if(algarismoRom == 'X')
		armazena = 10;
	else if(algarismoRom == 'L')
		armazena = 50;
	else if(algarismoRom == 'C')
		armazena = 100;
	else if(algarismoRom == 'D')
		armazena = 500;
	else if(algarismoRom == 'M')
		armazena = 1000;
	return armazena;
}
