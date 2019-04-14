all: tasks1 tasks2 tasks3
	@echo " "
	@echo "Compilação concluída!!"
	@echo " "

tasks1: pingpong-tasks1.o pingpong.o
	gcc -Wall -o tasks1 pingpong-tasks1.o pingpong.o

tasks2: pingpong-tasks2.o pingpong.o
	gcc -Wall -o tasks2 pingpong-tasks2.o pingpong.o

tasks3: pingpong-tasks3.o pingpong.o
	gcc -Wall -o tasks3 pingpong-tasks3.o pingpong.o

pingpong-tasks1.o: pingpong-tasks1.c datatypes.h pingpong.h
	gcc -o pingpong-tasks1.o -c pingpong-tasks1.c

pingpong-tasks2.o: pingpong-tasks2.c datatypes.h pingpong.h
	gcc -o pingpong-tasks2.o -c pingpong-tasks2.c

pingpong-tasks3.o: pingpong-tasks3.c datatypes.h pingpong.h
	gcc -o pingpong-tasks3.o -c pingpong-tasks3.c

pingpong.o: pingpong.c datatypes.h pingpong.h
	gcc -o pingpong.o -c pingpong.c

clean:
	rm -f *.o

execClean:
	rm -f tasks1 tasks2 tasks3
