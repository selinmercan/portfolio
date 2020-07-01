# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/tweet_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/tweet_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(TweetComparator.LessThanIsTrue "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetComparator.LessThanIsTrue")
add_test(TweetComparator.LessThanIsFalse "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetComparator.LessThanIsFalse")
add_test(TweetComparator.BothAreEqual "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetComparator.BothAreEqual")
add_test(TweetConstructor.Default "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetConstructor.Default")
add_test(TweetConstructor.NoText "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetConstructor.NoText")
add_test(TweetConstructor.WithText "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=TweetConstructor.WithText")
add_test(OStream.EmptyTweet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=OStream.EmptyTweet")
add_test(OStream.ShortTweet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=OStream.ShortTweet")
add_test(OStream.LongTweet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/tweet_tests/tweet_tests" "--gtest_filter=OStream.LongTweet")
