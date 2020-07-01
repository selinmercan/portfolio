# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/handler_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/handler_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(AndHandlerTest.CanHandle "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=AndHandlerTest.CanHandle")
add_test(AndHandlerTest.ProcessAndOneHashtag "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=AndHandlerTest.ProcessAndOneHashtag")
add_test(AndHandlerTest.ProcessAndTwoHashtags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=AndHandlerTest.ProcessAndTwoHashtags")
add_test(AndHandlerTest.ProcessAndManyHashtag "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=AndHandlerTest.ProcessAndManyHashtag")
add_test(AndHandlerTest.ProcessAndNoResults "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=AndHandlerTest.ProcessAndNoResults")
add_test(OrHandlerTest.CanHandle "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=OrHandlerTest.CanHandle")
add_test(OrHandlerTest.ProcessOrOneHashtag "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=OrHandlerTest.ProcessOrOneHashtag")
add_test(OrHandlerTest.ProcessOrTwoHashtags "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=OrHandlerTest.ProcessOrTwoHashtags")
add_test(OrHandlerTest.ProcessOrManyHashtag "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=OrHandlerTest.ProcessOrManyHashtag")
add_test(OrHandlerTest.ProcessOrNoResults "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=OrHandlerTest.ProcessOrNoResults")
add_test(TweetHandlerTest.CanHandle "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=TweetHandlerTest.CanHandle")
add_test(TweetHandlerTest.AddTweetThenSearch "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=TweetHandlerTest.AddTweetThenSearch")
add_test(TweetHandlerTest.AddTweetInvalidUser "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=TweetHandlerTest.AddTweetInvalidUser")
add_test(TweetHandlerTest.CaseInsensitiveHashtagTest "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/handler_tests/handlers_tests" "--gtest_filter=TweetHandlerTest.CaseInsensitiveHashtagTest")
