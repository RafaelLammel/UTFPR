all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: contexts.o
	gcc -Wall -o saida contexts.o

contexts.o: contexts.c
	gcc -o contexts.o -c contexts.c

clean:
	rm -f *.o

execClean:
	rm -f saida
