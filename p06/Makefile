all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-contab.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-contab.o pingpong.o queue.o

pingpong-contab.o: pingpong-contab.c
	gcc -o pingpong-contab.o -c pingpong-contab.c

pingpong.o: pingpong.c
	gcc -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
