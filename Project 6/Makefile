all: sentences sat_solver

sentences: sentences.cpp
	g++ -g -Wall -std=c++11 sentences.cpp -o sentences

sat_solver: sat_solver.cpp
	g++ -g -Wall -std=c++11 sat_solver.cpp -o sat_solver

clean:
	rm sentences
	rm sat_solver
