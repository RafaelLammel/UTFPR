#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <time.h>
#define MAX 12000000
#define FILE "NomeRG1M.txt"
#define CLEAR "clear"
using namespace std;

int C, M;

struct person
{
    char name[50];
    int RG;
};

struct seqList
{
    person *p;
    int n;
};

struct node
{
    person p;
    node *next;
    node *prev;
};

person insertPerson();

///Functions for Sequential List
void addFirstSeq(person p, seqList* sl);
void addLastSeq(person p, seqList* sl);
void addAtSeq(person p, seqList* sl, int pos);
void removeFirstSeq(seqList* sl);
void removeLastSeq(seqList* sl);
void removeAtSeq(seqList* sl, int pos);
int seqSearchSeq(seqList* sl, int RG);
int binarySearchSeq(seqList* sl, int RG);
void showListSeq(seqList* sl);
void readFileSeq(seqList* sl);
void makeFileSeq(seqList* sl);
//Sort Functions
void selectionSortSeq(seqList* sl);
void insertionSortSeq(seqList* sl);
void bubbleSortSeq(seqList* sl);
void shellSortSeq(seqList* sl);
void quickSortSeq(seqList* sl, int start, int ending);
void mergeSortSeq(seqList* sl, int l, int r);
void mergeSeq(seqList* sl, int l, int r, int middle);

///Functions for Linked List
void addFirstLinked(person p, node* header);
void addLastLinked(person p, node* header, node* tail);
void addAtLinked(person p, node* header, int pos);
void removeFirstLinked(node* header);
void removeLastLinked(node* header);
void removeAtLinked(node* header, int pos);
int seqSearchLinked(node* header, int RG);
void showListLinked(node* header);
void readFileLinked(node* header, node* tail);
void makeFileLinked(node* header);
//Sort Functions
void selectionSortLinked(node* header);
void insertionSortLinked(node* header);
void bubbleSortLinked(node* header);
void shellSortLinked(node* header);
void quickSortLinked(node* header);
void mergeSortLinked(node* header);

int main()
{
    int command = 0, sortCommand = 0, searchCommand = 0, listUsed = 0, pos, RG;
    double time;
    person p;
    seqList sl;
    node *header = (node*)malloc(sizeof(node));
    node *tail = (node*)malloc(sizeof(node));
    clock_t ticks[2];
    sl.n = 0;
    sl.p = (person*)malloc(sizeof(person)*MAX);
    header->next = NULL;
    tail->next = NULL;
    tail->prev = NULL;
    header->prev = NULL;
    while (listUsed != 1 && listUsed != 2)
    {
        cout << "Qual lista deve ser utilizada?" << endl;
        cout << "1. Sequencial" << endl;
        cout << "2. Encadeada" << endl;
        cout << "Escolha uma opcao: ";
        cin >> listUsed;
        system(CLEAR);
        if(listUsed != 1 && listUsed != 2)
            cout << "Essa opcao nao existe!!" << endl;
    }
    while (command != 12)
    {
        C = 0;
        M = 0;
        time = 0;
        if(listUsed == 1)
            cout << "LISTA SEQUENCIAL" << endl;
        else if (listUsed == 2)
            cout << "LISTA ENCADEADA" << endl;
        cout << "1. Inserir um elemento no inicio da lista" << endl;
        cout << "2. Inserir um elemento no final da lista" << endl;
        cout << "3. Inserir um elemento na posicao especificada" << endl;
        cout << "4. Retirar o primeiro elemento da lista" << endl;
        cout << "5. Retirar o ultimo elemento da lista" << endl;
        cout << "6. Retirar um elemento na posicao especificada" << endl;
        cout << "7. Procurar um elemento pelo RG" << endl;
        cout << "8. Ordenar lista" << endl;
        cout << "9. Mostrar a lista na tela" << endl;
        cout << "10. Salvar a lista em um arquivo" << endl;
        cout << "11. Ler a lista de um arquivo" << endl;
        cout << "12. Sair do sistema" << endl;
        cout << "Escolha uma opcao: ";
        cin >> command;
        system(CLEAR);
        switch(command)
        {
        case 1:
            p = insertPerson();
            if(listUsed == 1)
            {
                ticks[0] = clock();
                addFirstSeq(p,&sl);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Nome: " << sl.p[0].name << endl;
                cout << "RG: " << sl.p[0].RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: 1" << endl;
            }
            if (listUsed == 2)
            {
                ticks[0] = clock();
                addFirstLinked(p,header);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Nome: " << header->next->p.name << endl;
                cout << "RG: " << header->next->p.RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: 1" << endl;
            }
            break;
        case 2:
            p = insertPerson();
            if(listUsed == 1)
            {
                ticks[0] = clock();
                addLastSeq(p,&sl);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Nome: " << sl.p[sl.n-1].name << endl;
                cout << "RG: " << sl.p[sl.n-1].RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << sl.n-1 << endl;
            }
            if(listUsed == 2)
            {
                ticks[0] = clock();
                addLastLinked(p,header,tail);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Nome: " << p.name << endl;
                cout << "RG: " << p.RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << C+1 << endl;
            }
            break;
        case 3:
            p = insertPerson();
            cout << "Insira a posicao desejada: ";
            cin >> pos;
            if(listUsed == 1)
            {
                ticks[0] = clock();
                addAtSeq(p,&sl,pos);
                ticks[1] = clock();
                cout << "Nome: " << sl.p[pos-1].name << endl;
                cout << "RG: " << sl.p[pos-1].RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << pos-- << endl;
            }
            if(listUsed == 2)
            {
                ticks[0] = clock();
                addAtLinked(p,header,pos);
                ticks[1] = clock();
                cout << "Nome: " << p.name << endl;
                cout << "RG: " << p.RG << endl;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << pos << endl;
            }
            break;
        case 4:
            if(listUsed == 1)
            {
                cout << "Nome: " << sl.p[0].name << endl;
                cout << "RG: " << sl.p[0].RG << endl;
                ticks[0] = clock();
                removeFirstSeq(&sl);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: 1" << endl;
            }
            if(listUsed == 2)
            {
                cout << "Nome: " << header->next->p.name << endl;
                cout << "RG: " << header->next->p.RG << endl;
                ticks[0] = clock();
                removeFirstLinked(header);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: 1" << endl;
            }
            break;
        case 5:
            if(listUsed == 1)
            {
                cout << "Nome: " << sl.p[sl.n-1].name << endl;
                cout << "RG: " << sl.p[sl.n-1].RG << endl;
                ticks[0] = clock();
                removeLastSeq(&sl);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << sl.n << endl;
            }
            if(listUsed == 2)
            {
                cout << "Nome: " << endl;
                cout << "RG: " << endl;
                ticks[0] = clock();
                removeLastLinked(header);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << C << endl;
            }
            break;
        case 6:
            cout << "Escolha a posicao: ";
            cin >> pos;
            if(listUsed == 1)
            {
                cout << "Nome: " << sl.p[pos-1].name << endl;
                cout << "RG: " << sl.p[pos-1].RG << endl;
                ticks[0] = clock();
                removeAtSeq(&sl,pos);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << pos << endl;
            }
            if(listUsed == 2)
            {
                cout << "Nome: " << endl;
                cout << "RG: " << endl;
                ticks[0] = clock();
                removeAtLinked(header,pos);
                ticks[1] = clock();
                time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                cout << "Comparacoes: " << C << endl;
                cout << "Movimentacoes: " << M << endl;
                cout << "Tempo de execucao: " << time << endl;
                cout << "Posicao na lista: " << pos << endl;
            }
            break;
        case 7:
            cout << "Qual metodo de busca quer usar?" << endl;
            cout << "1. Busca Sequencial" << endl;
            cout << "2. Busca Binaria" << endl;
            cin >> searchCommand;
            cout << "Insira o RG para que eu ache o que procura: ";
            cin >> RG;
            if (listUsed == 1)
            {
                if(searchCommand == 1)
                {
                    pos = seqSearchSeq(&sl,RG);
                    if(pos >= 0)
                    {
                        cout << "Nome: " << sl.p[pos].name << endl;
                        cout << "RG: " << sl.p[pos].RG << endl;
                    }
                    else
                        cout << "Nao achei esse RG na lista!" << endl;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                    cout << "Posicao na lista: " << pos << endl;
                }
                else if (searchCommand == 2)
                {
                    pos = binarySearchSeq(&sl,RG);
                    if(pos >= 0)
                    {
                        cout << "Nome: " << sl.p[pos].name << endl;
                        cout << "RG: " << sl.p[pos].RG << endl;
                    }
                    else
                        cout << "Nao achei esse RG na lista!" << endl;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                    cout << "Posicao na lista: " << pos+1 << endl;
                }
                else
                    cout << "Esse comando nao existe!";
            }
            if (listUsed == 2)
            {
                if(searchCommand == 1)
                {
                    pos = seqSearchLinked(header,RG);
                    if(pos < 0)
                        cout << "Nao achei esse RG na lista!" << endl;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                    cout << "Posicao na lista: " << pos << endl;
                }
                if(searchCommand == 2)
                {
                    ///Implementar Busca Binaria Encadeada;
                }
            }
            break;
        case 8:
            cout << "Qual metodo de ordenacao deseja usar?" << endl;
            cout << "1. Selection Sort" << endl;
            cout << "2. Insertion Sort" << endl;
            cout << "3. Bubble Sort" << endl;
            cout << "4. Shell Sort" << endl;
            cout << "5. Quick Sort" << endl;
            cout << "6. Merge Sort" << endl;
            cout << "Escolha uma opcao: ";
            cin >> sortCommand;
            system(CLEAR);
            switch(sortCommand)
            {
            case 1:
                if (listUsed == 1)
                {
                    ticks[0] = clock();
                    selectionSortSeq(&sl);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                if(listUsed == 2)
                {
                    selectionSortLinked(header);
                }
                break;
            case 2:
                if (listUsed == 1)
                {
                    ticks[0] = clock();
                    insertionSortSeq(&sl);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                if (listUsed == 2)
                {
                    insertionSortLinked(header);
                }
                break;
            case 3:
                if(listUsed == 1)
                {
                    ticks[0] = clock();
                    bubbleSortSeq(&sl);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                if(listUsed == 2)
                {
                    bubbleSortLinked(header);
                }
                break;
            case 4:
                if (listUsed == 1)
                {
                    ticks[0] = clock();
                    shellSortSeq(&sl);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                break;
            case 5:
                if(listUsed == 1)
                {
                    ticks[0] = clock();
                    quickSortSeq(&sl,0,sl.n);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                break;
            case 6:
                if(listUsed == 1)
                {
                    ticks[0] = clock();
                    mergeSortSeq(&sl,0,sl.n-1);
                    ticks[1] = clock();
                    time = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
                    cout << "Comparacoes: " << C << endl;
                    cout << "Movimentacoes: " << M << endl;
                    cout << "Tempo de execucao: " << time << endl;
                }
                break;
            default:
                cout << "Esse comando nao existe!" << endl;
                break;
            }
            break;
        case 9:
            if (listUsed == 1)
                showListSeq(&sl);
            if (listUsed == 2)
                showListLinked(header);
            break;
        case 10:
            if (listUsed == 1)
                makeFileSeq(&sl);
            if (listUsed == 2)
                makeFileLinked(header);
            break;
        case 11:
            if (listUsed == 1)
                readFileSeq(&sl);
            if (listUsed == 2)
                readFileLinked(header,tail);
            break;
        case 12:
            break;
        default:
            cout << "Esse comando nao existe!" << endl;
            break;
        }
    }
}

person insertPerson()
{
    person p;
    cout << "Por favor, insira o nome: ";
    cin >> p.name;
    cout << "Por favor, insira o RG: ";
    cin >> p.RG;
    system(CLEAR);
    return p;
}

void readFileSeq(seqList* sl)
{
    string line;
    bool isName;
    int pos;
    char RG[20];
    person p;
    ifstream myfile (FILE);
    if (myfile.is_open())
    {
        while ( getline (myfile,line) )
        {
            isName = true;
            pos = 0;
            for (int i = 0; i < line.size(); i++)
            {
                if(isName)
                {
                    if(line[i] == ',')
                    {
                        isName = false;
                        p.name[pos] = '\0';
                        pos = 0;
                    }
                    else
                    {
                        p.name[pos] = line[i];
                        pos++;
                    }
                }
                else
                {
                    RG[pos] = line[i];
                    pos++;
                }
            }
            RG[pos] = '\0';
            p.RG = atoi(RG);
            addLastSeq(p,sl);
        }
        myfile.close();
    }
    else
        cout << "Unable to open file";
}

void makeFileSeq(seqList* sl)
{
    ofstream myfile ("NailedItSeq.txt");
    if (myfile.is_open())
    {
        for (int i = 0; i < sl->n; i++)
            myfile << sl->p[i].name << "," << sl->p[i].RG << endl;
        myfile.close();
    }
    else
        cout << "Unable to open file";
}

void readFileLinked(node* header, node* tail)
{
    string line;
    bool isName;
    int pos;
    char RG[20];
    person p;
    ifstream myfile (FILE);
    if (myfile.is_open())
    {
        while ( getline (myfile,line) )
        {
            isName = true;
            pos = 0;
            for (int i = 0; i < line.size(); i++)
            {
                if(isName)
                {
                    if(line[i] == ',')
                    {
                        isName = false;
                        p.name[pos] = '\0';
                        pos = 0;
                    }
                    else
                    {
                        p.name[pos] = line[i];
                        pos++;
                    }
                }
                else
                {
                    RG[pos] = line[i];
                    pos++;
                }
            }
            RG[pos] = '\0';
            p.RG = atoi(RG);
            addLastLinked(p,header,tail);
        }
        myfile.close();
    }
    else
        cout << "Unable to open file";
}

void makeFileLinked(node* header)
{
    ofstream myfile ("NailedItLinked.txt");
    if (myfile.is_open())
    {
        header = header->next;
        while(header != NULL)
        {
            myfile << header->p.name << "," << header->p.RG << endl;
            header = header->next;
        }
        myfile.close();
    }
    else
        cout << "Unable to open file";
}

void addFirstSeq(person p, seqList* sl)
{
    if(sl->n == 0)
    {
        sl->p[0] = p;
        sl->n++;
        C++;
        M++;
    }
    else
    {
        for (int i = sl->n; i > 0; i--)
        {
            sl->p[i] = sl->p[i-1];
            M++;
        }
        sl->p[0] = p;
        M++;
        sl->n++;
        C++;
    }
}

void addLastSeq(person p, seqList* sl)
{
    sl->p[sl->n] = p;
    M++;
    sl->n++;
}

void addAtSeq(person p, seqList* sl, int pos)
{
    pos--;
    if (pos >= sl->n)
    {
        sl->p[sl->n] = p;
        M++;
        sl->n++;
        C++;
    }
    else
    {
        for(int i = sl->n; i > pos; i--)
        {
            sl->p[i] = sl->p[i-1];
            M++;
        }
        sl->p[pos] = p;
        M++;
        sl->n++;
        C++;
    }
}

void removeFirstSeq(seqList* sl)
{
    for (int i = 0; i < sl->n; i++)
    {
        sl->p[i] = sl->p[i+1];
        M++;
    }
    sl->n--;
}

void removeLastSeq(seqList* sl)
{
    sl->n--;
}

void removeAtSeq(seqList* sl, int pos)
{
    pos--;
    while (pos < sl->n)
    {
        sl->p[pos] = sl->p[pos+1];
        pos++;
        M++;
    }
    sl->n--;
}

int seqSearchSeq(seqList* sl, int RG)
{
    for (int i = 0; i <= sl->n-1; i++)
    {
        if(sl->p[i].RG == RG)
        {
            C++;
            return i;
        }
        C++;
    }
    return -1;
}

int binarySearchSeq(seqList* sl, int RG)
{
    int bottom = 0, top = sl->n-1, middle;
    while (bottom <= top)
    {
        middle = (bottom+top)/2;
        if(RG == sl->p[middle].RG)
        {
            C++;
            return middle;
        }
        if(RG < sl->p[middle].RG)
            top = middle-1;
        else
            bottom = middle+1;
        C+=3;
    }
    return -1;
}

void showListSeq(seqList* sl)
{
    for (int i = 0; i < sl->n; i++)
        cout << sl->p[i].name << "," << sl->p[i].RG << endl;
    cout << endl;
}

void selectionSortSeq(seqList* sl)
{
    person lower;
    int pos;
    for (int i = 0; i < sl->n; i++)
    {
        pos = 0;
        lower = sl->p[i];
        for (int j = i; j < sl->n; j++)
        {
            if (sl->p[j].RG < lower.RG)
            {
                lower = sl->p[j];
                M++;
                pos = j;
            }
            C++;
        }
        if(pos != 0)
        {
            sl->p[pos] = sl->p[i];
            M++;
            sl->p[i] = lower;
        }
    }
}

void insertionSortSeq(seqList* sl)
{
    person p;
    int j;
    for (int i = 1; i < sl->n; i++)
    {
        p = sl->p[i];
        j = i-1;
        while(j >= 0 && sl->p[j].RG > p.RG)
        {
            sl->p[j+1] = sl->p[j];
            M++;
            j--;
            C++;
        }
        C++;
        sl->p[j+1] = p;
    }
}

void bubbleSortSeq(seqList* sl)
{
    bool tradeMade;
    person temp;
    int listSize = sl->n-1;
    do
    {
        tradeMade = false;
        for(int i = 0; i < listSize; i++)
        {
            if(sl->p[i].RG > sl->p[i+1].RG)
            {
                temp = sl->p[i];
                sl->p[i] = sl->p[i+1];
                sl->p[i+1] = temp;
                M+=2;
                tradeMade = true;
            }
            C++;
        }
        listSize--;
    }
    while(tradeMade);
}

void shellSortSeq(seqList* sl)
{
    int gap = 1;
    int i, j;
    person p;
    while(gap < sl->n)
    {
        gap=3*gap+1;
    }
    while(gap > 1)
    {
        gap /= 3;
        for(i = gap; i < sl->n; i++)
        {
            p = sl->p[i];
            j = i-gap;
            while(j >= 0 && p.RG < sl->p[j].RG)
            {
                sl->p[j+gap] = sl->p[j];
                j -= gap;
                M++;
            }
            C++;
            sl->p[j+gap] = p;
        }
    }
}

void quickSortSeq(seqList* sl, int start, int ending)
{
    int i = start, j = ending-1, pivot = sl->p[(start+ending)/2].RG;
    person p;
    while(i<=j)
    {
        while (sl->p[i].RG < pivot)
        {
            i++;
            C++;
        }
        while (sl->p[j].RG > pivot)
        {
            j--;
            C++;
        }
        if (i<=j)
        {
            p = sl->p[i];
            sl->p[i] = sl->p[j];
            sl->p[j] = p;
            M+=2;
            i++;
            j--;
        }
    }
    if(j > start)
        quickSortSeq(sl,start,j+1);
    if (i < ending)
        quickSortSeq(sl,i,ending);
}

void mergeSortSeq(seqList* sl, int l, int r)
{
    if (r>l)
    {
        int middle = (l+r)/2;
        mergeSortSeq(sl,l,middle);
        mergeSortSeq(sl,middle+1,r);
        mergeSeq(sl,l,r,middle);
        C++;
    }
}

void mergeSeq(seqList* sl,  int l, int r, int middle)
{
    int i, j, k;
    int n1 = middle - l + 1;
    int n2 = r - middle;
    seqList L, R;
    L.n = 0;
    L.p = (person*)malloc(sizeof(person)*n1);
    R.n = 0;
    R.p = (person*)malloc(sizeof(person)*n2);
    for(i = 0; i < n1; i++)
        addLastSeq(sl->p[i+l],&L);
    for(j = 0; j < n2; j++)
        addLastSeq(sl->p[middle+1+j],&R);
    i = 0;
    j = 0;
    k = l;
    while(i<n1 && j < n2)
    {
        if(L.p[i].RG <= R.p[j].RG)
        {
            sl->p[k] = L.p[i];
            i++;
            M++;
        }
        else
        {
            sl->p[k] = R.p[j];
            j++;
            M++;
        }
        k++;
        C++;
    }
    while (i<n1)
    {
        sl->p[k] = L.p[i];
        i++;
        k++;
        M++;
        C++;
    }
    while (j<n2)
    {
        sl->p[k] = R.p[j];
        j++;
        k++;
        M++;
        C++;
    }
    free(L.p);
    free(R.p);
}

void addFirstLinked(person p, node* header)
{
    node *aux = (node*)malloc(sizeof(node));
    aux->next = header->next;
    aux->p = p;
    header->next = aux;
    aux->prev = header;
    M++;
}

void addLastLinked(person p, node* header, node* tail)
{
    node *aux = (node*)malloc(sizeof(node));
    aux->next = NULL;
    aux->p = p;
    if(header->next == NULL)
    {
        header->next = aux;
        tail->next = aux;
        tail->prev = header;
    }
    else
    {
        tail->next->next = aux;
        tail->next = aux;
    }

}

void addAtLinked(person p, node* header, int pos)
{
    node *aux = (node*)malloc(sizeof(node));
    aux->p = p;
    for (int i = 0; i < pos-1; i++)
    {
        header = header->next;
    }
    aux->next = header->next;
    header->next = aux;
    aux->prev = header;
    M++;
}

void removeFirstLinked(node* header)
{
    if(header->next != NULL)
        header->next = header->next->next;
}

void removeLastLinked(node* header)
{
    if (header->next != NULL)
    {
        while (header->next->next != NULL)
        {
            header = header->next;
            C++;
        }
        header->next = NULL;
    }
}

void removeAtLinked(node* header, int pos)
{
    if(header->next != NULL)
    {
        for (int i = 0; i < pos-1; i++)
        {
            header = header->next;
            M++;
        }
        header->next = header->next->next;
    }
}

int seqSearchLinked(node* header, int RG)
{
    int i = 1;
    header = header->next;
    while(header != NULL)
    {
        if(header->p.RG == RG)
        {
            cout << "Nome: " << header->p.name << endl;
            cout << "RG: " <<  header->p.RG << endl;
            return i;
        }
        C++;
        header = header->next;
        i++;
    }
    return -1;
}

void showListLinked(node* header)
{
    header = header->next;
    while (header != NULL)
    {
        cout << header->p.name << "," << header->p.RG << endl;
        header = header->next;
    }
    cout << endl;
}

void selectionSortLinked(node* header)
{
    node *i, *j;
    for(i = header; i!=NULL && i->next!=NULL; i=i->next)
    {
        node *lower;
        lower = i;
        for(j = i->next; j!=NULL; j=j->next)
        {
            if(j->p.RG < lower->p.RG)
            {
                lower=j;
            }
        }
        if(lower!=i)
        {
            person aux;
            aux = lower->p;
            lower->p = i->p;
            i->p = aux;
        }
    }
}

void insertionSortLinked(node* header)
{
    node *i, *j;
    person p;
    for(i = header->next->next; i!=NULL; i=i->next)
    {
        p = i->p;
        for(j = i->prev; j!=NULL && j->p.RG > p.RG; j=j->prev)
        {
            j->next->p = j->p;
        }
        j->next->p = p;
    }
}

void bubbleSortLinked(node* header)
{
    node *i;
    bool tradeMade;
    person aux;
    do
    {
        tradeMade = false;
        for(i = header->next; i->next != NULL; i=i->next)
        {
            if(i->p.RG > i->next->p.RG)
            {
                aux = i->p;
                i->p = i->next->p;
                i->next->p = aux;
                tradeMade = true;
            }
        }
    }while(tradeMade);
}

void shellSortLinked(node* header)
{

}

void quickSortLinked(node* header)
{

}

void mergeSortLinked(node* header)
{

}
