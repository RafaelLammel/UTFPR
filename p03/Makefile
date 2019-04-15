all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-dispatcher.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-dispatcher.o pingpong.o queue.o

pingpong-dispatcher.o: pingpong-dispatcher.c
	gcc -o pingpong-dispatcher.o -c pingpong-dispatcher.c

pingpong.o: pingpong.c
	gcc -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
