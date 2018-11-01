/// Aluno: Rafael Lammel Marinheiro - Matricula: 1986856
/// Aluno: Ruanitto Docini - Matricula: 1908561
#include <stdio.h>
#include <string.h>

//Função que tem todas as comparações das estruturas com as sequências e retorna 1 se achar, e 0 se não.
int tabela(char est, char seq[]);

int main()
{

    char est[10], seq[100], aux[3];
    int i = 0,j,cont = 0, posicao;

    printf("Insira a estrutura primaria: ");
    fgets(est,10,stdin);
    printf("Insira a sequencia genetica: ");
    fgets(seq,100,stdin);
    fputs(seq,stdout);
    //Enquanto não chegarmos ao fim da sequencia, temos que comparar com a estrutura primaria;
    while(seq[i] != '\0'){
            //Se o i+1 for divisivel por 3, significa que temos um gene e podemos quebrar;
            if ((i+1)%3 == 0){
                for(j = 0; j < 3; j++){
                    aux[j] = seq[i-2+j];
            }
            aux[3] = '\0';
            //Aqui fazemos a ocmparação dos genes com a estrutura e caso todas as estruturas derem certo, fazemos;
            //um break para imprimir o resultado fora do While;
            if(tabela(est[cont],aux) == 1){
                cont++;
                if(est[cont+1] == '\0'){
                    i-= (cont*3)-1;
                    break;
                }
            }
            else
                cont = 0;
            }
        i++;
    }
    if(cont != 0 && est[cont+1] == '\0'){
    //Imprimimos a sequencia genética e a estrutura, formato exigido;AA
        while(i>0){
            printf(" ");
            i--;
        }
        for(j = 0; j < cont; j++)
            printf("%c  ",est[j]);
    }
    else
        printf("Não achei a sequencia!");
    return cont;

}

int tabela(char est, char seq[]){

    int match = 0;

    if(est == 'A'){
        if(strcmp("ttt",seq) == 0 ||
           strcmp("ttc",seq) == 0
           )
            match = 1;
    }
    else if (est == 'B'){
        if(strcmp("tta",seq) == 0 ||
           strcmp("ttg",seq) == 0 ||
           strcmp("ctt",seq) == 0 ||
           strcmp("ctc",seq) == 0 ||
           strcmp("cta",seq) == 0 ||
           strcmp("ctg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'C'){
        if(strcmp("att",seq) == 0 ||
           strcmp("atc",seq) == 0 ||
           strcmp("ata",seq) == 0
           )
            match = 1;
    }
    else if (est == 'D'){
        if(strcmp("atg",seq) == 0)
            match = 1;
    }
    else if (est == 'E'){
        if(strcmp("gtt",seq) == 0 ||
           strcmp("gtc",seq) == 0 ||
           strcmp("gta",seq) == 0 ||
           strcmp("gtg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'F'){
        if(strcmp("tct",seq) == 0 ||
           strcmp("tcc",seq) == 0 ||
           strcmp("tca",seq) == 0 ||
           strcmp("tcg",seq) == 0 ||
           strcmp("agt",seq) == 0 ||
           strcmp("agc",seq) == 0
           )
            match = 1;
    }
    else if (est == 'G'){
        if(strcmp("cct",seq) == 0 ||
           strcmp("ccc",seq) == 0 ||
           strcmp("cca",seq) == 0 ||
           strcmp("ccg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'H'){
        if(strcmp("act",seq) == 0 ||
           strcmp("acc",seq) == 0 ||
           strcmp("aca",seq) == 0 ||
           strcmp("acg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'I'){
        if(strcmp("gct",seq) == 0 ||
           strcmp("gcc",seq) == 0 ||
           strcmp("gca",seq) == 0 ||
           strcmp("gcg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'J'){
        if(strcmp("tat",seq) == 0 ||
           strcmp("tac",seq) == 0
           )
            match = 1;
    }
    else if (est == 'K'){
        if(strcmp("taa",seq) == 0 ||
           strcmp("tag",seq) == 0 ||
           strcmp("tga",seq) == 0
           )
            match = 1;
    }
    else if (est == 'L'){
        if(strcmp("cat",seq) == 0 ||
           strcmp("cac",seq) == 0
           )
            match = 1;
    }
    else if (est == 'M'){
        if(strcmp("caa",seq) == 0 ||
           strcmp("cag",seq) == 0
           )
            match = 1;
    }
    else if (est == 'N'){
        if(strcmp("aat",seq) == 0 ||
           strcmp("aac",seq) == 0
           )
            match = 1;
    }
    else if (est == 'O'){
        if(strcmp("aaa",seq) == 0 ||
           strcmp("aag",seq) == 0
           )
            match = 1;
    }
    else if (est == 'P'){
        if(strcmp("gat",seq) == 0 ||
           strcmp("gac",seq) == 0
           )
            match = 1;
    }
    else if (est == 'Q'){
        if(strcmp("tgt",seq) == 0 ||
           strcmp("tgc",seq) == 0
           )
            match = 1;
    }
    else if (est == 'R'){
        if(strcmp("tgg",seq) == 0)
            match = 1;
    }
    else if (est == 'S'){
        if(strcmp("cgt",seq) == 0 ||
           strcmp("cgc",seq) == 0 ||
           strcmp("cga",seq) == 0 ||
           strcmp("cgg",seq) == 0 ||
           strcmp("aga",seq) == 0 ||
           strcmp("agg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'T'){
        if(strcmp("ggt",seq) == 0 ||
           strcmp("ggc",seq) == 0 ||
           strcmp("gga",seq) == 0 ||
           strcmp("ggg",seq) == 0
           )
            match = 1;
    }
    else if (est == 'U'){
        if(strcmp("gaa",seq) == 0 ||
           strcmp("gag",seq) == 0
           )
            match = 1;
    }
    return match;
}
