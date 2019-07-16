all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-preempcao.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-preempcao.o pingpong.o queue.o

pingpong-preempcao.o: pingpong-preempcao.c
	gcc -Wall -o pingpong-preempcao.o -c pingpong-preempcao.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
