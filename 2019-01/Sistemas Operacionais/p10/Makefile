all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-prodcons.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-prodcons.o pingpong.o queue.o

pingpong-prodcons.o: pingpong-prodcons.c
	gcc -Wall -o pingpong-prodcons.o -c pingpong-prodcons.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
