#include "rem_dup_lib.h"
#include <iostream>
#include <fstream>
using namespace std;
int main(int argc, char* argv[]){
	ifstream ifile(argv[1]);
	Item* head1(0);
	Item* head2(0);
	ofstream ofile;
	ofile.open(argv[2]);
	readLists(head1, head2, ifile, ofile);
	ofile.close();
	return 0;
}
