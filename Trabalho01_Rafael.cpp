#include <iostream>
#include <string>
#include <fstream>
#include <stdlib.h>
#include <time.h>
#define MAX 120000000
using namespace std;

struct seqList
{
    string *person = (string*)malloc(sizeof(string)*MAX);
    int n;
};

struct node
{
    char person[50];
    node *next;
};

int MSeq, MLinked, CSeq, CLinked;
float posFound;
double seqTime, linkedTime;

void readFile(seqList *sl, node *header, node *tail);
void insertPerson(char *person);
void createFile(seqList *sl, node *header);
void showInfo(int pos);

///Functions for Sequential List
void showSeqList(seqList *sl);
void addFirstSeq(seqList *sl, string person);
void addLastSeq(seqList *sl, string person);
void addAtSeq(seqList *sl, string person, int pos);
void removeFirstSeq(seqList *sl);
void removeLastSeq(seqList *sl);
void removeAtSeq(seqList *sl, int pos);
string findPersonSeq(seqList *sl, string RG);

///Functions for Linked list
void showLinkedList(node *header);
void addFirstLinked(node *header, node *tail, char *person);
void addLastLinked(node *header, node *tail, char *person);
void addAtLinked(node *header, char *person, int pos);
void removeFirstLinked(node *header);
void removeLastLinked(node *header);
void removeAtLinked(node *header, int pos);
void findPersonLinked(node *header, char *RG, char *person);

int main()
{
    int command = 0, pos;
    char name[25], RG[25], person[25];
    clock_t ticks[2];
    seqList sl;
    sl.n = 0;
    node *header = (node*)malloc(sizeof(node));
    node *tail = (node*)malloc(sizeof(node));
    header->next = NULL;
    tail->next = NULL;
    while (command != 11)
    {
        MSeq = 0;
        MLinked = 0;
        CSeq = 0;
        CLinked = 0;
        cout << "1. Inserir um elemento no inicio da lista" << endl;
        cout << "2. Inserir um elemento no final da lista" << endl;
        cout << "3. Inserir um elemento na posicao especificada" << endl;
        cout << "4. Retirar o primeiro elemento da lista" << endl;
        cout << "5. Retirar o ultimo elemento da lista" << endl;
        cout << "6. Retirar um elemento na posicao especificada" << endl;
        cout << "7. Procurar um elemento pelo RG" << endl;
        cout << "8. Mostrar a lista na tela" << endl;
        cout << "9. Salvar a lista em um arquivo" << endl;
        cout << "10. Ler a lista de um arquivo" << endl;
        cout << "11. Sair do sistema" << endl;
        cout << "Escolha uma opcao: ";
        cin >> command;
        system("clear");
        switch(command)
        {
        case 1:
            insertPerson(person);
            ticks[0] = clock();
            addFirstSeq(&sl,person);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            addFirstLinked(header,tail,person);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            cout << endl;
            cout << "Nome e RG: " << person << endl;
            showInfo(1);
            break;
        case 2:
            insertPerson(person);
            ticks[0] = clock();
            addLastSeq(&sl,person);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            addLastLinked(header,tail,person);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            cout << endl;
            cout << "Nome e RG: " << person << endl;
            showInfo(sl.n);
            break;
        case 3:
            insertPerson(person);
            cout << "Insira a posicao desejada: ";
            cin >> pos;
            ticks[0] = clock();
            addAtSeq(&sl,person,pos);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            addAtLinked(header,person,pos);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            cout << endl;
            cout << "Nome e RG: " << person << endl;
            showInfo(pos);
            break;
        case 4:
            cout << endl;
            cout << "Nome e RG: " << sl.person[0] << endl;
            ticks[0] = clock();
            removeFirstSeq(&sl);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            removeFirstLinked(header);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            showInfo(1);
            break;
        case 5:
            cout << endl;
            cout << "Nome e RG: " << sl.person[sl.n-1] << endl;
            ticks[0] = clock();
            removeLastSeq(&sl);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            removeLastLinked(header);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            showInfo(sl.n+1);
            break;
        case 6:
            cout << "Insira a posicao desejada: ";
            cin >> pos;
            cout << endl;
            cout << "Nome e RG: " << sl.person[pos-1] << endl;
            ticks[0] = clock();
            removeAtSeq(&sl,pos);
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            removeAtLinked(header,pos);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            showInfo(pos);
            break;
        case 7:
            cout << "Digite o RG: ";
            cin >> RG;
            ticks[0] = clock();
            cout << findPersonSeq(&sl,RG) << endl;
            ticks[1] = clock();
            seqTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            ticks[0] = clock();
            findPersonLinked(header,RG,person);
            ticks[1] = clock();
            linkedTime = (ticks[1] - ticks[0]) * 1000.0 / CLOCKS_PER_SEC;
            cout << person << endl;
            person[0] = '\0';
            showInfo(posFound);
            break;
        case 8:
            cout << "Lista Sequencial: " << endl;
            cout << endl;
            showSeqList(&sl);
            cout << "\nLista Encadeada: " << endl;
            showLinkedList(header);
            cout << endl;
            break;
        case 9:
            createFile(&sl,header);
        case 10:
            readFile(&sl,header,tail);
            break;
        case 11:
            cout << "Tenha um bom dia!" << endl;
            break;
        default:
            cout << "Esse comando não existe!" << endl;
            break;
        }
    }
    free(header);
    return 0;
}

void readFile(seqList *sl, node *header, node *tail)
{
    string line;
    char person[50];
    int i;
    ifstream myfile ("NomeRG100M.txt");
    if (myfile.is_open())
    {
        while ( getline (myfile,line) )
        {
            line[line.size()] = '\0';
            for (i = 0; i < line.size(); i++)
                person[i] = line[i];
            person[i] = '\0';
            addLastSeq(sl,person);
            addLastLinked(header,tail,person);
        }
        myfile.close();
    }
    else
        cout << "Nao foi possivel ler o arquivo!";
}
void insertPerson(char *person)
{
    string name, RG, p;
    int i;
    cout << "Insira o nome: ";
    cin >> name;
    cout << "Insira o RG: ";
    cin >> RG;
    p = name+','+RG;
    for (i = 0; i < p.size(); i++)
        person[i] = p[i];
    person[i] = '\0';
}
void createFile(seqList *sl, node *header)
{
    ofstream myfileSeq ("listaSequencial.txt");
    ofstream myfileEncad ("listaEncadeada.txt");
    if (myfileSeq.is_open())
    {
        for (int i = 0; i < sl->n; i++)
            myfileSeq << sl->person[i] << "\n";
        myfileSeq.close();
    }
    else
        cout << "Nao foi possivel criar o arquivo para lista sequencial!";
    if (myfileEncad.is_open())
    {
        header = header->next;
        while (header != NULL)
        {
            myfileEncad << header->person << "\n";
            header = header->next;
        }
        myfileEncad.close();
    }
    else
        cout << "Nao foi possivel criar o arquivo para lista encadeada!";
}
void showInfo(int pos)
{
    cout << "Comparacoes lista sequencial: " << CSeq << endl;
    cout << "Comparacoes lista encadeada: " << CLinked << endl;
    cout << "Movimentacoes lista sequencial: " << MSeq << endl;
    cout << "Movimentacoes lista encadeada: " << MLinked << endl;
    cout << "Tempo de execucao lista sequencial: " << seqTime << endl;
    cout << "Tempo de execucao lista encadeada: " << linkedTime << endl;
    cout << "Posicao na lista: " << pos << endl;
    cout << endl;
}
void showSeqList(seqList *sl)
{
    for (int i = 0; i < sl->n; i++)
        cout << sl->person[i] << endl;
}
void addFirstSeq(seqList *sl, string person)
{
    if (sl->n == 0)
    {
        sl->person[sl->n] = person;
        MSeq++;
        sl->n++;
        CSeq++;
    }
    else
    {
        for(int i = sl->n; i > 0; i--)
        {
            sl->person[i] = sl->person[i-1];
            MSeq++;
        }
        sl->person[0] = person;
        MSeq++;
        sl->n++;
        CSeq++;
    }
}
void addLastSeq(seqList *sl, string person)
{
    sl->person[sl->n] = person;
    sl->n++;
    MSeq++;
}
void addAtSeq(seqList *sl, string person, int pos)
{
    pos--;
    if (pos >= sl->n)
    {
        sl->person[sl->n] = person;
        MSeq++;
        sl->n++;
        CSeq++;
    }
    else
    {
        for(int i = sl->n; i > pos; i--)
        {
            sl->person[i] = sl->person[i-1];
            MSeq++;
        }
        sl->person[pos] = person;
        MSeq++;
        sl->n++;
        CSeq++;
    }
}
void removeFirstSeq(seqList *sl)
{
    int i;
    if (sl->n > 0)
    {
        for(i = 0; i < sl->n; i++)
        {
            sl->person[i] = sl->person[i+1];
            MSeq++;
        }
        sl->n--;
    }
}
void removeLastSeq(seqList *sl)
{
    if(sl->n > 0)
        sl->n--;
}
void removeAtSeq(seqList *sl, int pos)
{
    if(pos == 1)
        removeFirstSeq(sl);
    else
    {
        pos--;
        if (sl->n > 0 && pos <= sl->n && pos > 0)
        {
            CSeq++;
            for(int i = pos; i < sl->n; i++)
            {
                sl->person[i] = sl->person[i+1];
                MSeq++;
            }
            sl->n--;
        }
    }
}
string findPersonSeq(seqList *sl, string RG)
{
    string aux;
    posFound = 0;
    for (long i = 0; i < sl->n; i++)
    {
        for (long j = 0; j < sl->person[i].size(); j++)
            if(sl->person[i][j] > 47 && sl->person[i][j] < 58)
            {
                aux = aux+sl->person[i][j];
                MSeq++;
                CSeq++;
            }
        aux[aux.size()] = '\0';
        if (aux == RG)
        {
            posFound = i+1;
            return "Nome e RG(Sequencial): " + sl->person[i];
        }
        aux.clear();
    }
    return "";
}
void showLinkedList(node *header)
{
    header = header->next;
    cout << endl;
    while(header!=NULL)
    {
        cout << header->person << endl;
        header = header->next;
    }
}
void addFirstLinked(node *header, node *tail, char *person)
{
    if (header==NULL)
        addLastLinked(header,tail,person);
    else
    {
        node *aux = (node*)malloc(sizeof(node));
        for (int i = 0; i < 50; i++)
            aux->person[i] = person[i];
        aux->next = header->next;
        header->next = aux;
        MLinked = 3;
    }
}
void addLastLinked(node *header, node *tail, char *person)
{
    node *aux = (node*)malloc(sizeof(node));
    for (int i = 0; i < 50; i++)
        aux->person[i] = person[i];
    aux->next = NULL;
    if(header->next == NULL)
    {
        header->next = aux;
        tail->next = aux;
        MLinked+=2;
    }
    else
    {
        tail->next->next = aux;
        tail->next = aux;
        MLinked+=2;
    }
    CLinked++;
}
void addAtLinked(node *header, char *person, int pos)
{
    node *aux = (node*)malloc(sizeof(node));
    for (int i = 0; i < 50; i++)
        aux->person[i] = person[i];
    for(int i = 0; i < pos-1; i++)
    {
        header = header->next;
        MLinked++;
    }
    aux->next = header->next;
    header->next = aux;
    MLinked++;
}
void removeFirstLinked(node *header)
{
    header->next = header->next->next;
    MLinked++;
}
void removeLastLinked(node *header)
{
    while (header->next->next != NULL)
    {
        header = header->next;
        MLinked++;
    }
    header->next = NULL;
    MLinked++;
}
void removeAtLinked(node *header, int pos)
{
    for(int i = 0; i < pos-1; i++)
    {
        header = header->next;
        MLinked++;
    }
    header->next = header->next->next;
    MLinked++;
}
void findPersonLinked(node *header, char *RG, char *person)
{
    int counter;
    char aux[20];
    int found;
    header = header->next;
    while(header->person != NULL)
    {
        counter = 0;
        found = 1;
        for (int i = 0; i < 50; i++)
            if(header->person[i] > 47 && header->person[i] < 58)
            {
                aux[counter] = header->person[i];
                counter++;
                MLinked++;
                CLinked++;
            }
        for(int i = 0; i < counter; i++)
            if(aux[i] != RG[i])
            {
                found = 0;
                break;
                MLinked++;
                CLinked++;
            }
        if(found == 1)
        {
            for (int i = 0; i < 50; i++)
            {
                person[i] = header->person[i];
                MLinked++;
            }
            break;
        }
        header = header->next;
        MLinked++;
    }
}
