all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-barrier.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-barrier.o pingpong.o queue.o

pingpong-barrier.o: pingpong-barrier.c
	gcc -Wall -o pingpong-barrier.o -c pingpong-barrier.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
