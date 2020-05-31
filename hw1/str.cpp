#include <iostream>
#include <stdexcept>
#include <cstring>
#include "str.h"
#include <stdexcept>
#include <stdio.h> 
#include <stdlib.h> 

using namespace std;

// Add your implementations here.

std::istream& operator>>(std::istream& istr, Str& s)
{
  std::string stemp;
  istr >> stemp;
  s = stemp.c_str();
  return istr;
}

Str::Str() { 
	data_= new char[1];
	data_[0]='\0';
}

Str::Str(const char* s){
	if(s==NULL) {
		data_= new char[1];
		data_[0]='\0';
	}
	else{
		int n=0;
		while(s[n]!='\0') n++;
		data_=new char[n+1];
		for(int i=0; i<n; i++){
			data_[i] = s[i];
		}
		data_[n]='\0';
	}
}

Str::Str(const Str& s){
	if(s.data_==NULL){
		data_= new char[1];
		data_[0]='\0';
	}
	else{
		int n=0;
		while(s.data_[n]!='\0') n++;
		data_=new char[n+1];
		for(int i=0; i<n; i++){
			data_[i] = s.data_[i];
		}
		data_[n]='\0';
	}
}

Str& Str::operator=(const char* s){
	delete[] this->data_;
	if(s==NULL) {
		data_= new char[1];
		data_[0]='\0';
	}
	else{
		int n=0;
		while(s[n]!='\0') n++;
		this->data_=new char[n+1];
		for(int i=0; i<n; i++){
			data_[i] = s[i];
		}
		this->data_[n]='\0';
	}
	return *this;
}

Str& Str::operator=(const Str& rhs){
	delete[] this->data_;
	if(rhs.data_==NULL){
		data_= new char[1];
		data_[0]='\0';
	}
	else{
		this->data_=new char[rhs.size()+1];
		for(unsigned int i=0; i<rhs.size(); i++){
			data_[i] = rhs.data_[i];
		}
		this->data_[rhs.size()]='\0';
	}
	return *this;
}

Str& Str::operator+=(const Str& s){
	if(s.data_!=NULL){
		char* appended;
		appended=new char[this->size()+s.size()+1];
		int index=0;
		for(unsigned int i=0; i<this->size(); i++){
			appended[i]=data_[i];
			index++;
		}
		int j=0;
		for(unsigned int i=index; i<this->size()+s.size(); i++){
			appended[i]=s.data_[j];
			j++;
			index++;
		}
		appended[this->size()+s.size()]='\0';
		data_=appended;
	}
	return *this;
}

Str& Str::operator+=(const char* s){
	if(s!=NULL){
		int newsize=0;
		while(s[newsize]!='\0'){
			newsize++;
		}
		char* newstring= new char[this->size()+newsize+1];
		for(unsigned int i=0; i<this->size(); i++){
			newstring[i]=this->data_[i];
		}
		int j=0;
		for(unsigned int i=this->size(); i<this->size()+newsize; i++){
			newstring[i]=s[j];
			j++;
		}
		newstring[this->size()+newsize]='\0';
		data_=newstring;
	}
	return *this;
}

size_t Str::size() const{
	return strlen(this->data_);
}

char& Str::operator[](unsigned int i){
	if(i<0 || i>=this->size()) {
		throw std::out_of_range("Index is out of range");
		exit(0);
	}
	return this->data_[i]; 
}

char const& Str::operator[](unsigned int i) const{
	if(i<0 || i>=this->size()) {
		throw std::out_of_range("Index is out of range");
		exit(0);
	}
	return this->data_[i];
}

Str Str::operator+(const char* rhs) const{
	if(rhs==NULL) return *this;
	Str temp;
	int newsize=0;
	while(rhs[newsize]!='\0'){
		newsize++;
	}
	char* newstring= new char[this->size()+newsize+1];
	for(unsigned int i=0; i<this->size(); i++){
		newstring[i]=this->data_[i];
	}
	int j=0;
	for(unsigned int i=this->size(); i<this->size()+newsize; i++){
		newstring[i]=rhs[j];
		j++;
	}
	newstring[this->size()+newsize]='\0';
	temp.data_=newstring;
	return temp;
}

Str Str::operator+(const Str& rhs) const{
	if(rhs.data_==NULL) return *this;
	Str temp;
	char* newstring= new char[this->size()+rhs.size()+1];
	for(unsigned int i=0; i<this->size(); i++){
		newstring[i]=this->data_[i];
	}
	int j=0;
	for(unsigned int i=this->size(); i<this->size()+rhs.size(); i++){
		newstring[i]=rhs.data_[j];
		j++;
	}
	newstring[this->size()+rhs.size()]='\0';
	temp.data_=newstring;
	return temp;
}

bool Str::operator==(const Str& rhs) const{
	if(strcmp(this->data_,rhs.data_)==0) return true;
	return false;
}

bool Str::operator==(const char* rhs) const{
	if (strcmp(this->data_, rhs)==0) return true;
	return false;
}

bool Str::operator<(const Str& rhs) const{
	if(strcmp(this->data_, rhs.data_)<0)return true;
	return false;
}

bool Str::operator<(const char* rhs) const{
	if(strcmp(this->data_, rhs)<0)return true;
	return false;
}

bool Str::operator>(const Str& rhs) const{
	if(strcmp(this->data_, rhs.data_)>0)return true;
	return false;
}

bool Str::operator>(const char* rhs) const{
	if (strcmp(this->data_, rhs)>0)return true;
	return false;
}

bool Str::operator!=(const Str& rhs) const{
	if(strcmp(this->data_, rhs.data_)!=0) return true;
	return false;
}

bool Str::operator!=(const char* rhs) const{
	if(strcmp(this->data_,rhs)!=0)return true;
	return false;
}

std::ostream &operator<<(std::ostream& os, const Str& s){
	for(unsigned int i=0; i<s.size(); i++){
		os << s.data_[i];
	}
	return os;
}

