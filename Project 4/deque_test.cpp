#include "deque.h"
#include "gtest/gtest.h"
using namespace std;
class DequeTest: public testing::Test{
protected:
	DequeTest(){

	}

	virtual  ~DequeTest() {
		// You can do clean-up work that doesn't throw exceptions here.		
	}

	// If the constructor and destructor are not enough for setting up
	// and cleaning up each test, you can define the following methods:
	virtual void SetUp() {
		// Code here will be called immediately after the constructor (right
		// before each test).
		deque_.push_front(1);
	}

	virtual void TearDown() {
		// Code here will be called immediately after each test (right
		// before the destructor).
	}

	// Objects declared here can be used by all tests in the test case.
	Deque<int> deque_;

};

TEST_F(DequeTest, AddFront){
	deque_.push_front(6);
	EXPECT_EQ(deque_[0], 6);
	deque_.push_front(11);
	EXPECT_EQ(deque_[0], 11);
}

TEST_F(DequeTest, AddBack){
	deque_.push_back(7);
	EXPECT_EQ(deque_[1], 7);
	deque_.push_back(12);
	EXPECT_EQ(deque_[2], 12);
	deque_.push_front(7);
	deque_.push_back(8);
	deque_.push_front(3);
	deque_.push_back(9);
	EXPECT_EQ(deque_[5], 9);
}

TEST_F(DequeTest, PopBack){
	deque_.pop_back();
	EXPECT_TRUE(deque_.empty());
	deque_.push_back(1);
	deque_.push_front(8);
	deque_.pop_back();
	EXPECT_EQ(deque_[0], 8);
	//EXPECT_EQ(deque_[1], 1);
}

TEST_F(DequeTest, PopFront){
	deque_.pop_front();
	EXPECT_TRUE(deque_.empty());
	deque_.push_back(7);
	EXPECT_TRUE(!deque_.empty());
	deque_.push_front(8);
	EXPECT_TRUE(!deque_.empty());
	deque_.pop_front();
	EXPECT_TRUE(!deque_.empty());
	EXPECT_EQ(deque_[0], 7);
}

TEST_F(DequeTest, size){
	EXPECT_EQ(deque_.size(), 1);
	deque_.pop_back();
	EXPECT_EQ(deque_.size(), 0);
	deque_.pop_back();
	EXPECT_EQ(deque_.size(), 0);
}

TEST_F(DequeTest, IsEmpty){
	EXPECT_FALSE(deque_.empty());
	deque_.pop_back();
	EXPECT_TRUE(deque_.empty());
}
