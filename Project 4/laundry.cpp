#include <iostream>
#include "stack.h"
#include <fstream>
using namespace std;
int main(int argc, char* argv[]){
	ifstream ifile(argv[1]);
	ofstream ofile;
	ofile.open(argv[2]);
	Stack<int> s1;
	while(true){
		cout << "here"<< endl;
		int n;
		ifile>>n;
		if(ifile.fail())break;
		cout << n << endl;
		if(n==0||n==-1){
			s1.push(n);
		}
		else{
			for(int i=0; i<n; i++){
				if(s1.top()==0){
					cout << "black" << endl;
					ofile<<"black"<<" ";
				}
				else{
					cout << "white" << endl;
					ofile<<"white"<<" ";
				}
				s1.pop();
			}
			ofile<<endl;
		}
		cout << s1.top() << endl;
	}
	ofile.close();
	return 0;
}
