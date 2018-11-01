#include <iostream>
#include <fstream>
#include <string>
#include <stdlib.h>
#include <string.h>
#include <locale>
#define TEXT "HistoriaSemAcento.txt"
#define CLEAR "cls"
using namespace std;

struct rtxt
{
    char word[50];
    int occ[100];
};

struct rtList
{
    rtxt rt[1500];
    int n;
};

char* readText();
void reverseText(char *text, rtList *rtL);
void sortRT(rtList *rt);
void showRT(rtList *rtL);
int searchWordIndex(rtList *rtL, char *word);
void phraseExample(int index, char *text);

int main ()
{

    char *text, word[50];
    char opt;
    int option = 0, index, c, cont;
    locale loc;
    rtList rtL;
    rtL.n = 0;
    while (option != 4)
    {
        cout << "Escolha uma opcao: " << endl;
        cout << "1. Ler o arquivo de texto" << endl;
        cout << "2. Inverter o arquivo de texto" << endl;
        cout << "3. Pesquisar uma palavra" << endl;
        cout << "4. Sair do programa" << endl;
        cout << "Insira a opcao desejada: ";
        cin >> option;
        system(CLEAR);
        switch(option)
        {
        case 1:
            text = readText();
            rtL.n = 0;
            break;
        case 2:
            cout << "Arquivo original:" << endl;
            cout << text << "\n" << endl;
            cout << "Arquivo invertido:\n" << endl;
            if(rtL.n == 0)
            {
                reverseText(text,&rtL);
                sortRT(&rtL);
            }
            showRT(&rtL);
            cout << "\n";
            break;
        case 3:
            if(rtL.n != 0)
            {
                cout << "Insira a palavra que deseja buscar: ";
                cin >> word;
                for(int i = 0; word[i] != '\0'; i++)
                    word[i] = tolower(word[i],loc);
                index = searchWordIndex(&rtL,word);
                if(index >= 0)
                {
                    cout << "Numero de ocorrencias: ";
                    for (c = 0; rtL.rt[index].occ[c] != 0; c++);
                    cout << c << endl;
                    cont = 0;
                    c--;
                    do
                    {
                        cout << "Posicao N no arquivo: " << rtL.rt[index].occ[cont] << endl;
                        cout << "Exemplo de frase com a palavra na ocorrencia: ";
                        phraseExample(rtL.rt[index].occ[cont],text);
                        cout << endl;
                        cont++;
                        if (cont <= c)
                        {
                            cout << "Deseja mostrar a proxima ocorrencia (s/n)? ";
                            cin >> opt;
                        }
                    }while(cont <= c && (opt != 'n' || opt != 'N'));
                }
                else
                    cout << "Nao achei a palavra!\n";
                cout << endl;
            }
            else
                cout << "Por favor crie um arquivo invertido antes! (Opcao 2)\n" << endl;
            break;
        case 4:
            break;
        default:
            cout << "Comando nao existe!\n" << endl;
            break;
        }
    }
}

char* readText()
{
    char *text;
    int length;
    ifstream myfile (TEXT);
    if(myfile)
    {
        myfile.seekg (0, myfile.end);
        length = myfile.tellg();
        myfile.seekg (0, myfile.beg);
        text = new char [length];
        myfile.read(text,length);
        cout << "Texto lido com sucesso!\n" << endl;
    }
    else
        cout << "Erro ao abrir o arquivo" << endl;
    return text;
}

void reverseText(char *text, rtList *rtL)
{
    char word[50];
    long i, j = 0, k, l;
    locale loc;
    for (i = 0; text[i]!='\0'; i++)
    {
        if((text[i] != ' ' && text[i+1] != '\0') &&
                ((text[i] >= 65 && text[i] <= 90) ||
                 (text[i] >= 97 && text[i] <= 122)))
        {
            word[j] = text[i];
            j++;
        }
        else
        {
            word[j] = '\0';
            for(int s = 0; s < j; s++)
                word[s] = tolower(word[s],loc);
            if(j != 0)
            {
                for (k = 0; k < rtL->n; k++)
                    if(strcmp(rtL->rt[k].word,word) == 0)
                        break;
                if(strcmp(rtL->rt[k].word,word) == 0)
                {
                    for (l = 0; rtL->rt[k].occ[l] != 0; l++);
                    rtL->rt[k].occ[l] = i+1-strlen(rtL->rt[l].word);
                }
                else
                {
                    strcpy(rtL->rt[rtL->n].word,word);
                    rtL->rt[rtL->n].occ[0] = i+1-strlen(rtL->rt[rtL->n].word);
                    rtL->n++;
                }
            }
            strcpy(word,"");
            j=0;
        }
    }
}

void sortRT(rtList *rtL)
{
    char word[50];
    int pos, i, j;
    rtxt rt;
    for (i = 0; i < rtL->n; i++)
    {
        pos = 0;
        strcpy(word,rtL->rt[i].word);
        for (j = i; j < rtL->n; j++)
        {
            if(strcmp(word,rtL->rt[j].word) > 0)
            {
                strcpy(word,rtL->rt[j].word);
                pos = j;
                rt = rtL->rt[j];
            }
        }
        if(pos != 0)
        {
            rtL->rt[pos] = rtL->rt[i];
            rtL->rt[i] = rt;
        }
    }
}

void showRT(rtList *rtL)
{
    for (int i = 0; i < rtL->n; i++)
    {
        cout << rtL->rt[i].word;
        for (int j = 0; rtL->rt[i].occ[j] != 0; j++)
            cout << " " << rtL->rt[i].occ[j];
        cout << endl;
    }
}

int searchWordIndex(rtList *rtL, char *word)
{
    for(int i = 0; i < rtL->n; i++)
    {
        if(strcmp(word,rtL->rt[i].word) == 0)
            return i;
    }
    return -1;
}

void phraseExample(int index, char *text)
{
    int firstIndex, lastIndex;
    for (firstIndex = index-1; ((text[firstIndex] >= 65 && text[firstIndex] <= 90) ||
                                (text[firstIndex] >= 97 && text[firstIndex] <= 122)) || text[firstIndex] == ' '; firstIndex--);

    for (lastIndex = index+1; ((text[lastIndex] >= 65 && text[lastIndex] <= 90) ||
                                (text[lastIndex] >= 97 && text[lastIndex] <= 122)) || text[lastIndex] == ' '; lastIndex++);
    firstIndex++;
    while (firstIndex < lastIndex)
    {
        cout << text[firstIndex];
        firstIndex++;
    }
}
