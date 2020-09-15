#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <stdexcept>
#include <stdio.h> 
#include <stdlib.h>
using namespace std; 

int main(int argc, char *argv[]){
	if(argc>=3){
		ifstream ifile(argv[1]);
		ofstream outputs; 
		outputs.open(argv[2]);
		int count;
		ifile>>count;
		if(ifile.fail() || count== 0){
			int number=0;
			outputs<<number;
		}
		else{
			int *numbers= new int[count];
			for(int i=0; i<count; i++){
				int number;
				ifile>>number;
				numbers[i]=number;
			}
			for(int i=0; i<count/2; i++){
				int output=numbers[i]+numbers[count-i-1];
				outputs<<output;
				outputs<< endl;
			}
			if(count%2!=0){
				int output=numbers[((count/2))]*2;
				outputs<<output;
			}
			delete[] numbers;
		}
		outputs.close();
	}
	else{
		cout <<"Missing input or output file" << endl;
	}
	return 0;


}
