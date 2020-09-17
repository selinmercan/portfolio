#include <iostream>
#include <string>
#include <string>
#include <vector>
#include <set>
#include "datetime.h"
#include "tweet.h"
#include "user.h"
using namespace std;
 Tweet::Tweet(){

 }

 /**
  * Constructor 
  */
 Tweet::Tweet(User* user, const DateTime& time, const std::string& text){
  user_=user;
  time_=time;
  text_=text;
 }

 /**
  * Gets the timestamp of this tweet
  *
  * @return timestamp of the tweet
  */
 DateTime const & Tweet::time() const{
  return time_;
 }

 /**
  * Gets the actual text of this tweet
  *
  * @return text of the tweet
  */
 std::string const & Tweet::text() const{
  return text_;
 }

 /**
  * Returns the hashtagged words without the '#' sign
  *
  */
 std::set<std::string> Tweet:: hashTags() const{
  return hashtagged;
 }


 /**
  * Return true if this Tweet's timestamp is less-than other's
  *
  * @return result of less-than comparison of tweet's timestamp
  */
 bool Tweet:: operator<(const Tweet& other) const{
  if(this->time()<other.time())return true;
  else if(this->time()==other.time()){
    if(this->user()->name()<other.user()->name())return true;
  }
  return false;
 } 

 bool Tweet::operator==(const Tweet& other) const{
  if(other.time()==time_&&other.text()==text_&&other.user()->name()==user_->name())return true;
  return false;
 }

 /**
  * Outputs the tweet to the given ostream in format:
  *   YYYY-MM-DD HH::MM::SS username tweet_text
  *
  * @return the ostream passed in as an argument
  */
 std::ostream& operator<<(std::ostream& os, const Tweet& t){
  os<<t.time()<<" "<< t.user_->name();
  if(!t.text().empty())os<<" " <<t.text();
  return os;
 }

 /* Create any other public or private helper functions you deem 
    necessary */

 User* Tweet:: user() const{
  return user_;
 }//when you output a user, you need to output their username 


