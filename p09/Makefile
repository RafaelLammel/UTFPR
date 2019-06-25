all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-sleep.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-sleep.o pingpong.o queue.o

pingpong-sleep.o: pingpong-sleep.c
	gcc -Wall -o pingpong-sleep.o -c pingpong-sleep.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
