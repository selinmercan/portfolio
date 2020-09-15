#include <fstream>
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
using namespace std;

bool includes(vector<string>& x, string y);

int main(int argc, char *argv[]){
	ifstream ifile(argv[1]);
	if(ifile.fail()) cout << "Error opening file" << endl;
	else{
		vector<string> users;
		vector<string> hashtags;
		string tweet;
		string word;
		int num_tweets=0;
		while(getline(ifile, tweet)){
			if(tweet.empty()) continue;
			num_tweets++;
			stringstream ss(tweet);
			while(ss>>word){
				if(word[0]=='@'){
					if(users.size()==0){
						users.push_back(word);
					}
					else if(!includes(users, word)) users.push_back(word);
				}
				else if(word[0]=='#'){
					if(hashtags.size()==0) hashtags.push_back(word);
					else if(!includes(hashtags, word)) hashtags.push_back(word);
				}
			}
		}
		cout << "1. Number of tweets=" << num_tweets << endl;
		cout << "2. Unique users" << endl;
		for(unsigned int i=0; i<users.size(); i++){
			cout << users[i].substr(1) << endl;
		}
		cout << "3. Unique hashtags" << endl;
		for(unsigned int i=0; i<hashtags.size(); i++){
			cout << hashtags[i].substr(1) << endl;
		}
	}
	
}

bool includes(vector<string>& x, string y){
	for(unsigned int i=0; i<x.size(); i++){
		if(x[i]==y) return true;
	}
	return false;
}
