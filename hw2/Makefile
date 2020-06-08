all: majors setops_test

setops_test: setops_test.cpp setops.o
	g++ -Wall -std=c++11 -g setops.o setops_test.cpp -o setops_test

setops.o: setops.cpp setops.h
	g++ -Wall -std=c++11 -g -c setops.cpp -o setops.o

majors: majors.cpp setops.o 
	g++ -Wall -std=c++11 -g majors.cpp setops.o -o majors

clean:
	rm setops_test
	rm setops.o
	rm majors