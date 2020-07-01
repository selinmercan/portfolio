# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/twiteng_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/twiteng_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(Parse.Empty "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.Empty")
add_test(Parse.FileDoesNotExist "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.FileDoesNotExist")
add_test(Parse.ImproperFormat "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.ImproperFormat")
add_test(Parse.Short "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.Short")
add_test(Parse.Medium "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.Medium")
add_test(Parse.Long "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=Parse.Long")
add_test(AddTweet.NoText "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=AddTweet.NoText")
add_test(AddTweet.AddShort "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=AddTweet.AddShort")
add_test(AddTweet.AddMedium "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=AddTweet.AddMedium")
add_test(AddTweet.AddLong "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/twiteng_tests/twiteng_tests" "--gtest_filter=AddTweet.AddLong")
