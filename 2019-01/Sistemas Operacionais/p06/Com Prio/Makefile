all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-contab-prio.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-contab-prio.o pingpong.o queue.o

pingpong-contab-prio.o: pingpong-contab-prio.c
	gcc -Wall -o pingpong-contab-prio.o -c pingpong-contab-prio.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
