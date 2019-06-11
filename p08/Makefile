all: saida
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

saida: pingpong-join.o pingpong.o queue.o
	gcc -Wall -o saida pingpong-join.o pingpong.o queue.o

pingpong-join.o: pingpong-join.c
	gcc -Wall -o pingpong-join.o -c pingpong-join.c

pingpong.o: pingpong.c
	gcc -Wall -o pingpong.o -c pingpong.c

queue.o: queue.c
	gcc -Wall -o queue.o -c queue.c

clean:
	rm -f *.o

execClean:
	rm -f saida
