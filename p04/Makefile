all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-scheduler.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-scheduler.o pingpong.o queue.o

pingpong-scheduler.o: pingpong-scheduler.c
	gcc -o pingpong-scheduler.o -c pingpong-scheduler.c

pingpong.o: pingpong.c
	gcc -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
