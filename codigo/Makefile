all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: buscaArvoreb.o criaArvoreb.o insereArvoreb.o mainArvoreb.o printArvoreb.o removeArvoreb.o
	gcc -o saida buscaArvoreb.o criaArvoreb.o insereArvoreb.o mainArvoreb.o printArvoreb.o removeArvoreb.o

buscaArvoreb.o: buscaArvoreb.c arvoreb.h
	gcc -o buscaArvoreb.o -c buscaArvoreb.c

criaArvoreb.o: criaArvoreb.c arvoreb.h
	gcc -o criaArvoreb.o -c criaArvoreb.c

insereArvoreb.o: insereArvoreb.c arvoreb.h
	gcc -o insereArvoreb.o -c insereArvoreb.c

mainArvoreb.o: mainArvoreb.c arvoreb.h
	gcc -o mainArvoreb.o -c mainArvoreb.c

printArvoreb.o: printArvoreb.c arvoreb.h
	gcc -o printArvoreb.o -c printArvoreb.c

removeArvoreb.o: removeArvoreb.c arvoreb.h
	gcc -o removeArvoreb.o -c removeArvoreb.c

clean:
	rm -f *.o

execClean:
	rm -f saida
