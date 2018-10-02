all: saida
	@echo " "
	@echo "Copilação concluída!!"
	@echo " "

saida: buscaArvoreb.o criaArvoreb.o insereArvoreb.o mainArvoreb.o printArvoreb.o removeArvoreb.o
  gcc -o saida buscaArvoreb.o criaArvoreb.o insereArvoreb.o mainArvoreb.o printArvoreb.o removeArvoreb.o

buscaArvoreb.o: buscaArvoreb.c
  gcc -o buscaArvoreb.o -c buscaArvoreb.c

criaArvoreb.o: criaArvoreb.c
  gcc -o criaArvoreb.o -c criaArvoreb.c

insereArvoreb.o: insereArvoreb.c
  gcc -o insereArvoreb.o -c insereArvoreb.c

mainArvoreb.o: mainArvoreb.c
  gcc -o mainArvoreb.o -c mainArvoreb.c

printArvoreb.o: printArvoreb.c
  gcc -o printArvoreb.o -c printArvoreb.c

removeArvoreb.o: removeArvoreb.c
  gcc -o removeArvoreb.o -c removeArvoreb.c

clean:
  rm -f *.o

execClean:
  rm -f saida
