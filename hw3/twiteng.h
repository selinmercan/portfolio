#ifndef TWITENG_H
#define TWITENG_H
#include <map>
#include <string>
#include <set>
#include <vector>
#include <sstream>
#include <list>
#include <iostream>
#include <fstream>
#include "user.h"
#include "datetime.h"
#include "util.h"
#include "tweet.h"

class TwitEng
{
 public:
  TwitEng();
  ~TwitEng(); //just delete the users
  /**
   * Parses the Twitter database and populates internal structures
   * @param filename of the database file
   * @return true if there is an error, false if successful
   */
  bool parse(char* filename);//how do i open the file

  /**
   * Allocates a tweet from its parts and adds it to internal structures
   * @param username of the user who made the tweet
   * @param timestamp of the tweet
   * @param text is the actual text of the tweet as a single string
   */
  //do I create a new user if i cannot find the username??
  void addTweet(const std::string& username, const DateTime& time, const std::string& text);

  /**
   * Searches for tweets with the given words and strategy
   * @param words is the hashtag terms in any case without the '#'
   * @param strategy 0=AND, 1=OR
   * @return the tweets that match the search
   */
  std::vector<Tweet*> search(std::vector<std::string>& terms, int strategy);

  std::set<Tweet*> intersection(std::set<Tweet*>& s1, std::set<Tweet*>& s2); //computes the intersection of two sets

  std::set<Tweet*> union_(std::set<Tweet*>& s1, std::set<Tweet*>& s2);//computes the union of two sets

  /**
   * Dump feeds of each user to their own file
   */
  void dumpFeeds();

  User* find_User(std::string username);//returns a pointer to a user when its username is given only 

  bool exists(std::string username);//checks if a user exists from a given username

  /* You may add other member functions */
 private:
  std::set<User*> users; 
  int num_users;
  std::map<std::string, std::set<Tweet*> > hashtags;
  /* Add any other data members or helper functions here  */



};


#endif
