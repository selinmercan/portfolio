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

# Utility rule file for debug-AndTest.TwoHashTags.

# Include the progress variables for this target.
include search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/progress.make

search_tests/CMakeFiles/debug-AndTest.TwoHashTags:
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold --progress-dir=/home/student/hw-mercan/hw5/hw3_tests/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Debugging AndTest.TwoHashTags with GDB..."
	cd /home/student/hw-mercan/hw5 && gdb --args /home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests --gtest_filter=AndTest.TwoHashTags

debug-AndTest.TwoHashTags: search_tests/CMakeFiles/debug-AndTest.TwoHashTags
debug-AndTest.TwoHashTags: search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/build.make

.PHONY : debug-AndTest.TwoHashTags

# Rule to build all files generated by this target.
search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/build: debug-AndTest.TwoHashTags

.PHONY : search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/build

search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/clean:
	cd /home/student/hw-mercan/hw5/hw3_tests/search_tests && $(CMAKE_COMMAND) -P CMakeFiles/debug-AndTest.TwoHashTags.dir/cmake_clean.cmake
.PHONY : search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/clean

search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/depend:
	cd /home/student/hw-mercan/hw5/hw3_tests && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/student/hw-mercan/hw5/hw3_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests /home/student/hw-mercan/hw5/hw3_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests /home/student/hw-mercan/hw5/hw3_tests/search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : search_tests/CMakeFiles/debug-AndTest.TwoHashTags.dir/depend

