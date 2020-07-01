# CMake generated Testfile for 
# Source directory: /home/student/hw-mercan/hw5/hw3_tests/user_tests
# Build directory: /home/student/hw-mercan/hw5/hw3_tests/user_tests
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(Constructor.BlankString "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Constructor.BlankString")
add_test(Constructor.NormalString "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Constructor.NormalString")
add_test(Feed.NotFollowing "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Feed.NotFollowing")
add_test(Feed.FollowingButNoTweets "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Feed.FollowingButNoTweets")
add_test(Feed.Small "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Feed.Small")
add_test(Feed.Medium "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Feed.Medium")
add_test(Feed.Large "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Feed.Large")
add_test(Follower.NewUser "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Follower.NewUser")
add_test(Follower.UserAlreadyInSet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Follower.UserAlreadyInSet")
add_test(Following.NewUser "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Following.NewUser")
add_test(Following.UserAlreadyInSet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Following.UserAlreadyInSet")
add_test(Tweets.BlankTweet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Tweets.BlankTweet")
add_test(Tweets.NormalTweet "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Tweets.NormalTweet")
add_test(Tweets.ListTweets "/usr/bin/valgrind" "--tool=memcheck" "--leak-check=yes" "--trace-children=no" "/home/student/hw-mercan/hw5/hw3_tests/user_tests/user_tests" "--gtest_filter=Tweets.ListTweets")
