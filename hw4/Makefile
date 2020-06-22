# Let's declare some variables
CC       = g++
CPPFLAGS = -Wall -g
BIN_DIR  = bin
GTEST_LL = -I /usr/include/gtest/ -l gtest -l gtest_main -pthread
all:deque_test rem_dup laundry
# IMPORTANT: $(GTEST_LL) needs to come *after* the source file
deque_test: deque_test.cpp deque.h
	$(CC) $(CPPFLAGS) -std=c++11 deque_test.cpp $(GTEST_LL) -o deque_test

rem_dup:rem_dup.cpp rem_dup_lib.o rem_dup_lib.h
	$(CC) $(CPPFLAGS) -std=c++11 rem_dup.cpp rem_dup_lib.o -o rem_dup

rem_dup_lib.o: rem_dup_lib.cpp rem_dup_lib.h
	$(CC) $(CPPFLAGS) -c -std=c++11 rem_dup_lib.cpp -o rem_dup_lib.o

laundry: laundry.cpp deque.h stack.h 
	$(CC) $(CPPFLAGS) laundry.cpp -o laundry

.PHONY: tests clean

tests: $(BIN_DIR)/deque_test
	-@$(BIN_DIR)/deque_test
	-@echo "--- All Tested!---"

clean:
	-@rm -rf deque_test
	-@rm -rf laundry
	-@rm -rf deque_test
	-@rm -rf rem_dup_lib.o
	-@echo "--- All Clean!---"