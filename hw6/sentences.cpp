#include <vector>
#include <string>
#include <fstream>
#include <sstream>
#include <iostream>

using namespace std;

void readWords(char* filename, vector<vector<string> >& words);

// client interface
void generateSentences(vector<vector<string> >& words, ofstream& ofile);

// recursive helper function
void genHelper(vector<vector<string> >& words,
	       ofstream& ofile,
	       int currentOption,
	       string sentence,
	       int& numSentences);

void readWords(char* filename, vector<vector<string> >& words)
{
  ifstream ifile(filename);
  string parse;
  int i=0;
  words[0].push_back("The");
  while(getline(ifile, parse)){
    if(parse.empty())continue;
    stringstream ss(parse);
    string word;
    while(ss>>word){
      words[i].push_back(word);
    }
    i++;
  }
}

void generateSentences(vector<vector<string> >& words, ofstream& ofile)
{
  int x=0;
  genHelper(words, ofile, 0, "", x);
  ofile.close();
}

void genHelper(vector<vector<string> >& words,
	       ofstream& ofile,
	       int currentOption,
	       string sentence,
	       int& numSentences)
{
  if((uint)currentOption==words.size()){//once there are no more options left (it is the end of the sentence) 
    ofile<<sentence<<"."<< endl;//the program backtracks and tries other versions of an option, so if we go back to 
    numSentences++;//the adverb vector, the program creates a new sentence with another adverb in the vector
    return;
  }
  for(uint i=0; i<words[currentOption].size(); i++){//goes through every option vector
    genHelper(words, ofile, currentOption+1, sentence+words[currentOption][i], numSentences);
  }
}

int main(int argc, char* argv[])
{
  if(argc < 3){
    cerr << "Usage ./sentences input_file output_file" << endl;
  }
  vector<vector<string> > words;

  // Parse the file
  readWords(argv[1], words);

  // Check the status of parsing
  if(words.size() < 4){
    cerr << "Input file requires 4 lines of words" << endl;
    return 1;
  }

  // Open the output file
  ofstream ofile(argv[2]);
 
  // generate all the sentence options writing them to ofile
  generateSentences(words, ofile);

  ofile.close();

  return 0;
}
