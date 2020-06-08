#include <iostream>
#include <fstream>
#include <cmath>
#include <sstream>
#include <string>
#include <map>
#include <set>
#include <vector>
#include "setops.h"
#include <cctype>
#include <algorithm>

using namespace std;

int main(int argc, char* argv[]){
	if(argc>=4){
		ifstream ifile(argv[1]);
		ifstream command_file(argv[2]);
		ofstream output;
		output.open(argv[3]);
		if(ifile.fail()) cout << "Error opening input file" << endl;
		if(command_file.fail()) cout << "Error opening command file" << endl;
		if(output.fail()) cout << "Error opening output file" << endl;
		else{
			string parse;
			map<string, set<string> > m1;
			while(getline(ifile, parse)){
				int position=0;
				if(parse.empty()) continue;
				string name;
				char temp=',';
				while(true){
					if(isspace(parse[position])){
						while(isspace(parse[position])) position++;
						if(parse[position]!=temp) name+=" ";
					}
					if(parse[position]==temp) break;

					name+=parse[position];
					position++;
				}
				position++;
				while(true){
					if((unsigned int)position>=parse.size()-1) break;
					string major;
					while(isspace(parse[position])) position++;
					if((unsigned int)position>=parse.size()-1) break;
					while(isalpha(parse[position])){
						major+=parse[position];
						position++;
					}
					for(unsigned int i=0; i<major.size(); i++){
						if(major[i]>=65 && major[i]<=90) continue;
						else{
							major[i]-=32;
						}
					}
					m1[major].insert(name);
					if((unsigned int)position>=parse.size()-1) break;
				}
			}

			string major_list;
			while(getline(command_file, major_list)){
				if(major_list.empty()) continue;
				vector<string> majors;
				string major;
				stringstream ss(major_list);
				while(ss>>major){
					majors.push_back(major);
				}
				if(majors.size()>1){
					vector<string> upped_majors;
					for(unsigned int i=0; i<majors.size(); i++){
						string temp=majors[i];
						for(unsigned int k=0; k<temp.size(); k++){
							if(temp[k]>=65 && temp[k]<=90) continue;
							else{
								temp[k]-=32;
							}
						}
						upped_majors.push_back(temp);
					}
					set<string> intersection=((m1[upped_majors[0]]) & (m1[upped_majors[1]]));
					for(unsigned int i=2; i<majors.size(); i++){
						intersection= intersection & m1[upped_majors[i]];
					}
					for(unsigned int i=0; i<majors.size(); i++){
						output<< majors[i] << " ";
					}
					output << endl;
					for(set<string>::iterator it=intersection.begin(); it!=intersection.end(); ++it){
						output<<*it << endl;
					}
				}
				else{
					output<<majors[0] << endl;
					for(unsigned int i=0; i<majors[0].size(); i++){
						if(majors[0][i]>=65 && majors[0][i]<=90) continue;
						else{
							majors[0][i]-=32;
						}
					}
					for(set<string>::iterator it=m1[majors[0]].begin(); it!=m1[majors[0]].end(); ++it){
						output << *it;
						output << endl;
					}
				}
				output << endl;
				
			}
			output.close();
		}
	}
	else{
		cout << "Missing input, command, or output file" << endl;
	}
	return 0;
}