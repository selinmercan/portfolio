#include <string>
#include <set>
#include <list>
#include <vector>
#include <iostream>
#include "user.h"
#include "hsort.h"
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

void User::add_Mentioned(Tweet* t){
	mentioned_.push_back(t);
}

void User::add_Hidden_list(Tweet *t){
	hidden_.insert(t);
}
void User::add_Hidden(Tweet* t, const User* excluded){
	for(set<User*>::iterator it=this->followers_.begin(); it!=this->followers_.end(); ++it){
		if((*it)->name()!=excluded->name())(*it)->add_Hidden_list(t);
	}
}

bool User::follows(const User& other){
	for(set<User*>::iterator it=this->following_.begin(); it!=this->following_.end(); ++it){
		if((*it)->name()==other.name()) return true;
	}
	return false;
}
bool User::operator==(const User& other) const{
	if(other.name()==username) return true;
	return false;
}

bool User::operator!=(const User& other) const{
	if(other.name()==username) return false;
	return true;
}

std::vector<Tweet*> User::getFeed(){
	vector<Tweet*> feed;
	for(list<Tweet*>::iterator it=tweets_.begin(); it!=tweets_.end(); ++it){
		feed.push_back(*it);
	}
	for(set<User*>::iterator it=following_.begin(); it!=following_.end(); ++it){
		for(list<Tweet*>::iterator it2=(*it)->tweets_.begin(); it2!=(*it)->tweets_.end(); ++it2){
			if(hidden_.find(*it2)==hidden_.end())feed.push_back(*it2);
		}
	}
	//sort(feed.begin(), feed.end(), TweetComp());
	hsort(feed, TweetComp());
	return feed;
}

std::vector<Tweet*> User::getMentionedFeed(){
	vector<Tweet*> feed;
	for(list<Tweet*>::iterator it=mentioned_.begin(); it!=mentioned_.end(); ++it){
		feed.push_back(*it);
	}
	hsort(feed, TweetComp());
	return feed;
}
