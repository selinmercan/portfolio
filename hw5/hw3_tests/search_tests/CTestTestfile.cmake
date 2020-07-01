# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/search_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/search_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(AndTest.NoHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=AndTest.NoHashTags")
add_test(AndTest.MissingHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=AndTest.MissingHashTags")
add_test(AndTest.TwoHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=AndTest.TwoHashTags")
add_test(AndTest.OneHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=AndTest.OneHashTags")
add_test(OrTest.NoHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=OrTest.NoHashTags")
add_test(OrTest.MissingHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=OrTest.MissingHashTags")
add_test(OrTest.TwoHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=OrTest.TwoHashTags")
add_test(OrTest.OneHashTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=OrTest.OneHashTags")
add_test(OrTest.DoubleTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=OrTest.DoubleTags")
add_test(AndTest.DoubleTags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_tests" "--gtest_filter=AndTest.DoubleTags")
add_test(SearchRuntime.And "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_runtime_tests" "--gtest_filter=SearchRuntime.And")
add_test(SearchRuntime.Or "/home/student/hw-mercan/hw5/hw3_tests/search_tests/search_runtime_tests" "--gtest_filter=SearchRuntime.Or")
