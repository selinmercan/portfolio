# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/datetime_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/datetime_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(DateTimeComparator.LessThanIsTrue "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=DateTimeComparator.LessThanIsTrue")
add_test(DateTimeComparator.LessThanIsFalse "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=DateTimeComparator.LessThanIsFalse")
add_test(DateTimeComparator.BothAreEqual "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=DateTimeComparator.BothAreEqual")
add_test(DateTimeConstructor.Default "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=DateTimeConstructor.Default")
add_test(DateTimeConstructor.Alt "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=DateTimeConstructor.Alt")
add_test(IStream.ProperFormat "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=IStream.ProperFormat")
add_test(IStream.ImproperFormat "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=IStream.ImproperFormat")
add_test(OStream.Default "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=OStream.Default")
add_test(OStream.Normal "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/datetime_tests/datetime_tests" "--gtest_filter=OStream.Normal")
