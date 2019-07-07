all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-mqueue.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-mqueue.o pingpong.o queue.o -lm

pingpong-mqueue.o: pingpong-mqueue.c
	gcc -Wall -o pingpong-mqueue.o -c pingpong-mqueue.c -lm

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
