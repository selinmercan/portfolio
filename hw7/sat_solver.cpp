#include <iostream>
#include <fstream>
#include <sstream>
#include <map>
#include <vector>
#include <cstdlib>
#include "avlbst.h"
using namespace std;
bool total_returns_true(AVLTree<int, int>* variables, vector<vector<int> >& clause);
bool total_returns_false(AVLTree<int, int>* variables, vector<vector<int> >& clause);
bool clause_returns_true(AVLTree<int, int>* variables, vector<int>& clause);
bool solver(AVLTree<int, int>* variables, vector<vector<int> >& clause, int num_variables, int current_variable, ofstream& os, int& final_value){
	if(total_returns_false(variables, clause)){//if there are unassigned integers, this function will return false. it will only
		return false;//return true if the whole expression becomes false with the addition of a new assignment to a variable
	}
	if(total_returns_true(variables, clause)){//if the whole expression returns true, print the variables
		for(AVLTree<int, int>::iterator it=variables->begin(); it!=variables->end(); ++it){
			os<<it->first<<" = "<<it->second<<endl;
		} 
		return true;
	}
	for(int i=1; i>=0; i--){
		variables->insert(current_variable, i);
		if(solver(variables, clause, num_variables, current_variable+1, os, final_value)==true){
			final_value=1;
			break;
		}
		else {
			final_value=-1;//final value is used in order to see whether there is no way of finding a solution to the problem
		}//if the final value is set as -1, it means we have left the for loop with no solution returning true.
	}

	if(final_value==-1)return false;
	return true;
}

int main(int argc, char* argv[]){
	ifstream ifile(argv[1]);
	string parse;
	int num_clauses;
	int num_variables;
	vector<vector<int> > clauses;
	AVLTree<int, int> variables;
	while(getline(ifile, parse)){
		if(parse.empty())continue;
		if(parse[0]=='c')continue;
		if(parse[0]=='p'){
			stringstream ss(parse);
			string x;
			ss>>x;
			ss>>x;
			ss>>num_variables;
			ss>>num_clauses;
		}
		else{
			stringstream ss(parse);
			int vars;
			vector<int> one_clause;
			while(ss>>vars){
				if(vars==0)break;
				one_clause.push_back(vars);
			}
			clauses.push_back(one_clause);
		}
	}
	ofstream ofile;
	ofile.open(argv[2]);
	int val=0;
	bool result=solver(variables, clauses, num_variables, 1, ofile, val);
	if(result==false)ofile<<"No solution"<<endl;
	ofile.close();
	return 0;
}

bool total_returns_false(AVLTree<int, int>* variables, vector<vector<int> >& clause){
	for(uint i=0; i<clause.size(); i++){//this function returns true if the whole expression becomes false. 
		int unassigned=0;//if there are still unassigned variables that might change the result of the whole expression,
		int true_=0;//the function returns false. If the whole expression is true, the function returns false.
		for(uint k=0; k<clause[i].size(); k++){
			int result_;
			if(variables.find(abs(clause[i][k]))==variables.end()){
				unassigned++;
				continue;
			}
			if(clause[i][k]<1&&variables[abs(clause[i][k])]==1){//if the clause is less than 1, we invert the assignment
				result_=0;//of the expression
			}
			else if(clause[i][k]<1&&variables[abs(clause[i][k])]==0){
				result_=1;
			}
			else result_=variables[abs(clause[i][k])];
			if(result_==1)true_++;
		}
		if(true_==0&&unassigned==0)return true;//if there are no variables that are assigned as true and no unassigned variables
	}//so if the whole clause is false, it would result in the expression becoming false
	return false;
}

bool total_returns_true(AVLTree<int, int>* variables, vector<vector<int> >& clause){//this function returns true if the whole expression is true. if there are clauses with all of their  
	for(uint i=0; i<clause.size(); i++){//variables unassigned, the function returns false.
		if(!clause_returns_true(variables, clause[i]))return false;
	}
	return true;
}

bool clause_returns_true(AVLTree<int, int>* variables, vector<int>& clause){
	int true_=0;
	for(uint i=0; i<clause.size(); i++){
		int result_;
		if(variables.find(abs(clause[i]))==variables.end())continue;
		if(variables[abs(clause[i])]==1&&clause[i]<1)result_=0;
		else if(variables[abs(clause[i])]==0&&clause[i]<1)result_=1;
		else result_=variables[abs(clause[i])];
		if(result_==1)true_++;
	}
	if(true_>0)return true;
	return false;
}