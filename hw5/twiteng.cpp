#include "twiteng.h"
#include "hsort.h"
#include <exception>
using namespace std;


 TwitEng::TwitEng(){
 	num_users=0;
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
 		if(user.empty())continue;
 		if(!exists(user)){
 			User* user1=new User(user);
	 		while(!ss.fail()){
	 			string followed;
	 			ss>>followed;
	 			if(followed.empty())continue;
	 			if(!exists(followed)){
		 			User* user2=new User(followed);
		 			user1->addFollowing(user2);
		 			user2->addFollower(user1);
		 			users.insert(user2);
		 		}
		 		else{
		 			user1->addFollowing(find_User(followed));
		 			find_User(followed)->addFollower(user1);
		 		}
	 		}
	 		users.insert(user1);
	 	}
	 	else{
	 		User* temp=find_User(user);
	 		while(!ss.fail()){
	 			string followed;
	 			ss>>followed;
	 			if(followed.empty())continue;
	 			if(!exists(followed)){
		 			User* user2=new User(followed);
		 			temp->addFollowing(user2);
		 			user2->addFollower(temp);
		 			users.insert(user2);
		 		}
		 		else{
		 			temp->addFollowing(find_User(followed));
		 			find_User(followed)->addFollower(temp);
		 		}
	 		}
	 	}
 	}

 	while(getline(ifile, parse)){
 		if(parse.empty()) continue;
 		int position=0;
 		while(isspace(parse[position]))position++;
 		if(!isdigit(parse[position])) return true;
 		//DateTime* date_time=new DateTime();
 		DateTime date_time;
 		stringstream s1(parse);
 		s1>>date_time;
 		while(!isalpha(parse[position]))position++;
 		stringstream ss(parse.substr(position));
 		string text, username;
 		ss>>username;
 		while(1){
 			string temp;
 			ss>>temp;
 			if(ss.fail())break;
 			text+=temp+ " ";
 		}
 		text=text.substr(0, text.size()-1);
 		addTweet(username, date_time, text); 

 		
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
	 	int i=0;
	 	string temp1;
	 	while(ss>>temp1){
	 		if(temp1[0]=='@'){
	 			temp1=temp1.substr(1);
	 			find_User(temp1)->add_Mentioned(new_tweet);
	 			if(i==0){
	 				User* excluded=find_User(temp1);
					temp->add_Hidden(new_tweet, excluded);
	 			}
	 		}
	 		if(temp1[0]=='#'){
	 			temp1=temp1.substr(1);
	 			convUpper(temp1);
	 			if(hashtags[temp1].find(new_tweet)==hashtags[temp1].end())hashtags[temp1].insert(new_tweet); 
	 			if(new_tweet->hashTags().find(temp1.substr(1))==new_tweet->hashTags().end())new_tweet->hashTags().insert(temp1.substr(1));
	 		}
	 		i++;
	 	}
	 	temp->addTweet(new_tweet);
	 	tweets.push_back(new_tweet);
	 	
	 }
	 else{
	 	throw invalid_argument("user does not exist");
	 }


 }

 void TwitEng::add_Follower(const std::string& username, const std::string& to_follow){
 	User* tofollow=find_User(to_follow);
 	User* username_=find_User(username);
 	username_->addFollowing(tofollow);
 	tofollow->addFollower(username_);
 }

 /**
  * Searches for tweets with the given words and strategy
  * @param words is the hashtag terms in any case without the '#'
  * @param strategy 0=AND, 1=OR
  * @return the tweets that match the search
  */
 std::vector<Tweet*> TwitEng::search(std::vector<std::string>& terms, int strategy){
 	vector<Tweet*> texts; 
 	if(terms.empty())return texts;
 	if(terms.size()==1){
 		string temp=terms[0];
 		convUpper(temp);
 		if(hashtags.find(temp)==hashtags.end())return texts;
 		for(set<Tweet*>::iterator it=hashtags[temp].begin(); it!=hashtags[temp].end(); ++it){
 			texts.push_back(*it);
 		}
 		hsort(texts, t_comp());
 		return texts;
 	}
 	else{
 		int k=0;
 		if((uint)k<terms.size())convUpper(terms[k]);
 		else return texts;
 		while(hashtags.find(terms[k])==hashtags.end()){
 			k++;
 			if((uint)k>=terms.size())break; //checks if the hashtags asked for is found, if it reaches
 		}									//the size of the terms vector and none of the hashtags are found
 		if((uint)k>=terms.size())return texts;//we just return an empty vector
 		int j=k+1;
 		if((uint)j<terms.size())convUpper(terms[j]);//if the other hashtag is inside the vector size
		else{
			set<Tweet*> returning=hashtags[terms[k]];
			for(set<Tweet*>::iterator it=returning.begin(); it!=returning.end(); ++it){
				texts.push_back(*it); //if k-th term was the end of the terms vector so only one of the hashtags exist
			}
			hsort(texts, t_comp());
			return texts;
 		}
 		while(hashtags.find(terms[j])==hashtags.end()){//if kth term was not the last term on the terms vector
 			j++;
 			if((uint)j>=terms.size())break;
 		}//this is done because in order to consecutively compute intersection, the intersection of the first two sets
	 	set<Tweet*> set1=hashtags[terms[k]];//is needed to create an intersect set and use it as a parameter in the intersection
	 	set<Tweet*> set2=hashtags[terms[j]];//function for the other terms 
	 	if(strategy==0){
	 		set<Tweet*> intersect= intersection(set1, set2);
	 		if((uint)j==terms.size()-1){//if j-th term was the last term in the terms vector
	 			for(set<Tweet*>::iterator it=intersect.begin(); it!=intersect.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			hsort(texts, t_comp());
	 			return texts;
	 		}
	 		else{ //if the j-th term was not the last term on the terms vector 
	 			for(unsigned int i=j+1; i<terms.size(); i++){
	 				convUpper(terms[i]);
	 				if(hashtags.find(terms[i])==hashtags.end())continue;
	 				intersect= intersection(intersect, hashtags[terms[i]]);
	 			}
	 			for(set<Tweet*>::iterator it=intersect.begin(); it!=intersect.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			hsort(texts, t_comp());
	 			return texts;
	 		}
	 	}
	 	else{
	 		set<Tweet*> union_of=union_(set1, set2);
	 		if((uint)j==terms.size()-1){
	 			for(set<Tweet*>::iterator it=union_of.begin(); it!=union_of.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			hsort(texts, t_comp());
	 			return texts;
	 		}
	 		else{
	 			for(unsigned int i=j+1; i<terms.size(); i++){
	 				convUpper(terms[i]);
	 				if(hashtags.find(terms[i])==hashtags.end())continue;
	 				union_of= union_(union_of, hashtags[terms[i]]);
	 			}
	 			for(set<Tweet*>::iterator it=union_of.begin(); it!=union_of.end(); ++it){
	 				texts.push_back(*it);
	 			}
	 			hsort(texts, t_comp());
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
 		ofstream ofile2;
 		string name=(*it)->name()+".feed";
 		string name2=(*it)->name()+".mentions";
 		ofile.open(name);
 		ofile2.open(name2);
 		ofile<<(*it)->name() << endl;
 		ofile2<<(*it)->name() << endl;
 		vector<Tweet*> feed=(*it)->getFeed();
 		vector<Tweet*> mentions=(*it)->getMentionedFeed();
 		for(unsigned int i=0; i<feed.size(); i++){
 			ofile<<*(feed[i]) << endl;
 		}
 		for(unsigned int i=0; i<mentions.size(); i++){
 			ofile2<<*(mentions[i]) << endl;
 		}
 		ofile2.close();
 		ofile.close();
 	}

 }

 void TwitEng::dumpSave(string& filename){
 	ofstream ofile;
 	filename+=".dat";
 	ofile.open(filename);
 	ofile<<num_users<<endl;
 	for(set<User*>::iterator it=users.begin(); it!=users.end(); ++it){
 		ofile<<(*it)->name()<<" ";
 		set<User*> followings=(*it)->following();
 		for(set<User*>::iterator it2=followings.begin(); it2!=followings.end(); ++it2){
 			ofile<<(*it2)->name()<<" ";
 		}
 		ofile<<endl;
 	}
 	for(std::list<Tweet*>::iterator it=tweets.begin(); it!=tweets.end(); ++it){
 		ofile<<(**it)<<endl;
 	}
 	ofile.close();
 }

