#include "twiteng.h"

using namespace std;


 TwitEng::TwitEng(){

 }

 TwitEng::~TwitEng(){
 	for(set<User*>::iterator it=users.begin(); it!=users.end(); ++it){
 		delete (*it);
 	}

 }

 User* TwitEng::find_User(string username){
  convUpper(username);
  for(set<User*>::iterator it=users.begin(); it!=users.end();++it){
  	string temp=(*it)->name();
  	convUpper(temp);
  	if(temp==username) return *it;
  }
  return 0;
 }
 bool TwitEng::exists(string username){
 	convUpper(username);
 	for(set<User*>::iterator it=users.begin(); it!=users.end(); ++it){
 		string temp=(*it)->name();
 		convUpper(temp);
 		if(temp==username) return true;
 	}
 	return false;
 }

 /**
  * Parses the Twitter database and populates internal structures
  * @param filename of the database file
  * @return true if there is an error, false if successful
  */
 bool TwitEng::parse(char* filename){
 	ifstream ifile(filename);
 	ifile>>num_users;
 	if(ifile.fail())return true;
 	string parse;
 	getline(ifile, parse);
 	for(int i=0; i<num_users; i++){
 		getline(ifile, parse);
 		stringstream ss(parse);
 		string user;
 		ss>>user;
 		if(!exists(user)){
 			User* user1=new User(user);
	 		while(!ss.fail()){
	 			string followed;
	 			ss>>followed;
	 			if(!exists(followed)){
		 			User* user2=new User(followed);
		 			user1->addFollowing(user2);
		 			users.insert(user2);
		 		}
		 		else{
		 			user1->addFollowing(find_User(followed));
		 		}
	 		}
	 		users.insert(user1);
	 	}
	 	else{
	 		User* temp=find_User(user);
	 		while(!ss.fail()){
	 			string followed;
	 			ss>>followed;
	 			if(!exists(followed)){
		 			User* user2=new User(followed);
		 			temp->addFollowing(user2);
		 			users.insert(user2);
		 		}
		 		else{
		 			temp->addFollowing(find_User(followed));
		 		}
	 		}
	 	}
 	}

 	while(getline(ifile, parse)){
 		if(parse.empty()) continue;
 		int position=0;
 		while(isspace(parse[position]))position++;
 		if(!isdigit(parse[position])) return true;
 		DateTime* date_time=new DateTime();
 		stringstream s1(parse);
 		s1>>*date_time;
 		while(!isalpha(parse[position]))position++;
 		stringstream ss(parse.substr(position));
 		string text, username;
 		ss>>username;
 		while(!ss.fail()){
 			string temp;
 			ss>>temp;
 			text+=temp+ " ";
 		}
 		addTweet(username, *date_time, text); 

 		
 	}
 	return false;

 }//how do i open the file

 /**
  * Allocates a tweet from its parts and adds it to internal structures
  * @param username of the user who made the tweet
  * @param timestamp of the tweet
  * @param text is the actual text of the tweet as a single string
  */
 //do I create a new user if i cannot find the username??
 void TwitEng::addTweet(const std::string& username, const DateTime& time, const std::string& text){
 	if(exists(username)){
	 	User* temp=find_User(username);
	 	Tweet* new_tweet=new Tweet(temp, time, text);
	 	stringstream ss(text);
	 	while(!ss.fail()){
	 		string temp1;
	 		ss>>temp1;
	 		if(temp1[0]=='#'){
	 			temp1=temp1.substr(1);
	 			convUpper(temp1);
	 			if(hashtags[temp1].find(new_tweet)==hashtags[temp1].end())hashtags[temp1].insert(new_tweet); 
	 			if(new_tweet->hashTags().find(temp1.substr(1))==new_tweet->hashTags().end())new_tweet->hashTags().insert(temp1.substr(1));
	 		}
	 	}

	 	temp->addTweet(new_tweet);
	 }
	 else{
	 	User* temp= new User(username);
	 	users.insert(temp);
	 	Tweet* new_tweet=new Tweet(temp, time, text);
	 	stringstream ss(text);
	 	while(!ss.fail()){
	 		string temp1;
	 		ss>>temp1;
	 		if(temp1[0]=='#'){
	 			temp1=temp1.substr(1);
	 			convUpper(temp1);
	 			if(hashtags[temp1].find(new_tweet)==hashtags[temp1].end())hashtags[temp1].insert(new_tweet); 
	 			if(new_tweet->hashTags().find(temp1.substr(1))==new_tweet->hashTags().end())new_tweet->hashTags().insert(temp1.substr(1));
	 		}
	 	}

	 	temp->addTweet(new_tweet);

	 }


 }

 /**
  * Searches for tweets with the given words and strategy
  * @param words is the hashtag terms in any case without the '#'
  * @param strategy 0=AND, 1=OR
  * @return the tweets that match the search
  */
 std::vector<Tweet*> TwitEng::search(std::vector<std::string>& terms, int strategy){
 	vector<Tweet*> texts; 
 	if(terms.size()==1){
 		string temp=terms[0];
 		convUpper(temp);
 		for(set<Tweet*>::iterator it=hashtags[temp].begin(); it!=hashtags[temp].end(); ++it){
 			texts.push_back(*it);
 		}
 		return texts;
 	}
 	else{
 		convUpper(terms[0]);
 		convUpper(terms[1]);
	 	set<Tweet*> set1=hashtags[terms[0]];
	 	set<Tweet*> set2=hashtags[terms[1]];
	 	if(strategy==0){
	 		set<Tweet*> intersect= intersection(set1, set2);
	 		if(terms.size()<=2){
	 			for(set<Tweet*>::iterator it=intersect.begin(); it!=intersect.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			return texts;
	 		}
	 		else{
	 			for(unsigned int i=2; i<terms.size(); i++){
	 				convUpper(terms[i]);
	 				intersect= intersection(intersect, hashtags[terms[i]]);
	 			}
	 			for(set<Tweet*>::iterator it=intersect.begin(); it!=intersect.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			return texts;
	 		}
	 	}
	 	else{
	 		set<Tweet*> union_of=union_(set1, set2);
	 		if(terms.size()<=2){
	 			for(set<Tweet*>::iterator it=union_of.begin(); it!=union_of.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			return texts;
	 		}
	 		else{
	 			for(unsigned int i=2; i<terms.size(); i++){
	 				convUpper(terms[i]);
	 				union_of= union_(union_of, hashtags[terms[i]]);
	 			}
	 			for(set<Tweet*>::iterator it=union_of.begin(); it!=union_of.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			return texts;
	 		}
	 	}
	 }

 }

//write a helper function
 set<Tweet*> TwitEng::intersection(set<Tweet*>& s1, set<Tweet*>& s2){
 	std::set<Tweet*> intersection;
 	for(std::set<Tweet*>::iterator it=s1.begin(); it!=s1.end(); ++it){
 		if(s2.find(*it)!=s2.end()) intersection.insert(*it);
 	}
 	return intersection;
 }

set<Tweet*> TwitEng::union_(set<Tweet*>& s1, set<Tweet*>& s2){
	std::set<Tweet*> union_of;
	for(std::set<Tweet*>::iterator it=s1.begin(); it!=s1.end(); ++it){
		union_of.insert(*it);
	}
	for(std::set<Tweet*>::iterator it=s2.begin(); it!=s2.end(); ++it){
		if(union_of.find(*it)==union_of.end())union_of.insert(*it);
	}
	return union_of;
}

 /*
  * Dump feeds of each user to their own file
  */
 void TwitEng::dumpFeeds(){
 	for(set<User*>::iterator it=users.begin(); it!=users.end(); ++it){
 		ofstream ofile;
 		string name=(*it)->name()+".feed";
 		ofile.open(name);
 		ofile<<(*it)->name() << endl;
 		vector<Tweet*> feed=(*it)->getFeed();
 		for(unsigned int i=0; i<feed.size(); i++){
 			ofile<<*(feed[i]) << endl;
 		}
 		ofile.close();
 	}

 }

