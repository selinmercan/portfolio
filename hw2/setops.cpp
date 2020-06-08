#include <iostream>
#include <stdexcept>
#include <cstring>
#include "setops.h"
#include <stdexcept>
#include <stdio.h> 
#include <stdlib.h> 

std::set<std::string> operator&(const std::set<std::string>& s1, 
                                const std::set<std::string>& s2){
	std::set<std::string> intersection;
	for(std::set<std::string>::iterator it=s1.begin(); it!=s1.end(); ++it){
		if(s2.find(*it)!=s2.end()) intersection.insert(*it);
	}
	return intersection;
}

// Computes the union of s1 and s2
std::set<std::string> operator|(const std::set<std::string>& s1, 
                                const std::set<std::string>& s2){
	std::set<std::string> union_of;
	for(std::set<std::string>::iterator it=s1.begin(); it!=s1.end(); ++it){
		union_of.insert(*it);
	}
	for(std::set<std::string>::iterator it=s2.begin(); it!=s2.end(); ++it){
		if(union_of.find(*it)==union_of.end())union_of.insert(*it);
	}
	return union_of;
}

// Computes the difference of s1 - s2
std::set<std::string> operator-(const std::set<std::string>& s1,
                                const std::set<std::string>& s2){
	std::set<std::string> difference;
	for(std::set<std::string>::iterator it=s1.begin(); it!=s1.end(); ++it){
		if(s2.find(*it)==s2.end())difference.insert(*it);
	}
	return difference;
}