# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.5

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/student/hw-mercan/hw5/hw3_tests

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/student/hw-mercan/hw5/hw3_tests

# Include any dependencies generated for this target.
include search_tests/CMakeFiles/search_runtime_tests.dir/depend.make

# Include the progress variables for this target.
include search_tests/CMakeFiles/search_runtime_tests.dir/progress.make

# Include the compile flags for this target's objects.
include search_tests/CMakeFiles/search_runtime_tests.dir/flags.make

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o: search_tests/CMakeFiles/search_runtime_tests.dir/flags.make
search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o: search_tests/runtime_tests.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/student/hw-mercan/hw5/hw3_tests/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o"
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && /usr/bin/c++   $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o -c /home/student/hw-mercan/hw5/hw3_tests/search_tests/runtime_tests.cpp

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.i"
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && /usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/student/hw-mercan/hw5/hw3_tests/search_tests/runtime_tests.cpp > CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.i

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.s"
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && /usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/student/hw-mercan/hw5/hw3_tests/search_tests/runtime_tests.cpp -o CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.s

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.requires:

.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.requires

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.provides: search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.requires
	$(MAKE) -f search_tests/CMakeFiles/search_runtime_tests.dir/build.make search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.provides.build
.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.provides

search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.provides.build: search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o


# Object files for target search_runtime_tests
search_runtime_tests_OBJECTS = \
"CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o"

# External object files for target search_runtime_tests
search_runtime_tests_EXTERNAL_OBJECTS =

search_tests/search_runtime_tests: search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o
search_tests/search_runtime_tests: search_tests/CMakeFiles/search_runtime_tests.dir/build.make
search_tests/search_runtime_tests: search_tests/libhw3_search.a
search_tests/search_runtime_tests: testing_utils/libtesting_utils.a
search_tests/search_runtime_tests: /usr/lib/libgtest_main.a
search_tests/search_runtime_tests: /usr/lib/libgtest.a
search_tests/search_runtime_tests: testing_utils/kwsys/libkwsys.a
search_tests/search_runtime_tests: testing_utils/libperf/libperf.a
search_tests/search_runtime_tests: search_tests/CMakeFiles/search_runtime_tests.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/student/hw-mercan/hw5/hw3_tests/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable search_runtime_tests"
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/search_runtime_tests.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
search_tests/CMakeFiles/search_runtime_tests.dir/build: search_tests/search_runtime_tests

.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/build

search_tests/CMakeFiles/search_runtime_tests.dir/requires: search_tests/CMakeFiles/search_runtime_tests.dir/runtime_tests.cpp.o.requires

.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/requires

search_tests/CMakeFiles/search_runtime_tests.dir/clean:
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && $(CMAKE_COMMAND) -P CMakeFiles/search_runtime_tests.dir/cmake_clean.cmake
.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/clean

search_tests/CMakeFiles/search_runtime_tests.dir/depend:
	cd /home/student/hw-mercan/hw5/hw3_tests && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/student/hw-mercan/hw5/hw3_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests /home/student/hw-mercan/hw5/hw3_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests/CMakeFiles/search_runtime_tests.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : search_tests/CMakeFiles/search_runtime_tests.dir/depend

