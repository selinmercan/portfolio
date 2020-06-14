#include <string>
#include <set>
#include <list>
#include <vector>
#include <iostream>
#include "user.h"
using namespace std;

User::User(std::string name){
	username=name;
}

string User::name() const{
	return username;
}

User::~User(){
	for(list<Tweet*>::iterator it=tweets_.begin(); it!=tweets_.end(); ++it){
		delete (*it);
	}
	
}

std::set<User*> User::followers() const{
	return followers_;
}

std::set<User*> User::following() const{
	return following_;
}

std::list<Tweet*> User::tweets() const{
	return tweets_;
}

void User::addFollower(User* u){
	followers_.insert(u);
}

void User::addFollowing(User* u){
	following_.insert(u);
}

void User::addTweet(Tweet* t){
	tweets_.push_back(t);
}

bool User::operator==(const User& other) const{
	if(other.name()==username) return true;
	return false;
}

std::vector<Tweet*> User::getFeed(){
	vector<Tweet*> feed;
	for(list<Tweet*>::iterator it=tweets_.begin(); it!=tweets_.end(); ++it){
		feed.push_back(*it);
	}
	for(set<User*>::iterator it=following_.begin(); it!=following_.end(); ++it){
		for(list<Tweet*>::iterator it2=(*it)->tweets_.begin(); it2!=(*it)->tweets_.end(); ++it2){
			feed.push_back(*it2);
		}
	}
	sort(feed.begin(), feed.end(), TweetComp());
	return feed;
}