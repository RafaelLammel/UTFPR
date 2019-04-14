all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: queue.o testafila.o
	gcc -Wall -o saida queue.o testafila.o

queue.o: queue.c queue.h
	gcc -o queue.o -c queue.c

testafila.o: testafila.c queue.h
	gcc -o testafila.o -c testafila.c

clean:
	rm -f *.o

execClean:
	rm -f saida
