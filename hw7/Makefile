all: bst avlbst sat_solver

bst: bst.h
	g++ -g -Wall -std=c++11 bst.h -o bst

avlbst: bst.h avlbst.h
	g++ -g -Wall -std=c++11 avlbst.h -o avlbst

sat_solver: avlbst.h sat_solver.cpp
	g++ -g -Wall -std=c++11 sat_solver.cpp -o sat_solver

clear:
	-rm bst
	-rm avlbst
	-rm sat_solver

