all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-maintask.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-maintask.o pingpong.o queue.o

pingpong-maintask.o: pingpong-maintask.c
	gcc -Wall -o pingpong-maintask.o -c pingpong-maintask.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
