#include <iostream>
#include "datetime.h"
#include <string>
#include <set>
#include <list>
#include <vector>
#include <ctime>
#include <time.h>
using namespace std;
DateTime::DateTime(){
	time_t rawtime;
	struct tm * timeinfo;

	time (&rawtime);
	timeinfo = localtime(&rawtime);
	this->year=timeinfo->tm_year+1900;
	this->month=timeinfo->tm_mon;
	this->day=timeinfo->tm_mday;
	this->hour=timeinfo->tm_hour;
	this->min=timeinfo->tm_min;
	this->sec=timeinfo->tm_sec;

}

DateTime::DateTime(int hh, int mm, int ss, int yr, int mnth, int dy){
	hour=hh;
	min=mm;
	sec=ss;
	year=yr;
	month=mnth;
	day=dy;
}

bool DateTime::operator<(const DateTime& other) const{
	if(year<other.year) return true;
	else if(year==other.year){
		if(month<other.month) return true;
		else if(month==other.month){
			if(day<other.day) return true;
			else if(day==other.day){
				if(hour<other.hour) return true;
				else if(hour==other.hour){
					if(min<other.min) return true;
					else if(min==other.min){
						if(sec<other.sec) return true;
						return false;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	return false;
}


bool DateTime::operator==(const DateTime& other) const{
	if(other.year==year&&other.month==month&&other.day==day&&other.hour==hour&&other.min==min&&other.sec==sec)return true;
	return false;
}

 /* Outputs the timestamp to the given ostream in format:
 *   YYYY-MM-DD HH::MM::SS
 *
 * @return the ostream passed in as an argument
 */
ostream& operator<<(std::ostream& os, const DateTime& other){
	os<<other.year<<"-"<<other.month<<"-"<<other.day<<" "<<other.hour<<":"<<other.min<<":"<<other.sec;
	return os;
}

istream& operator>>(std::istream& is, DateTime& dt){
	string full, hr, minute, second, yr, mnth, dy, blank;
	int position=0;
	getline(is, full);
	int error=0;
	for(int i=0; i<4; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		yr+=full[position];
		position++;
	}
	if(isdigit(full[position])) error+=1;
	position++;

	for(int i=0; i<2; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		mnth+=full[position];
		position++;
	}
	if(isdigit(full[position])) error+=1;
	position++;

	for(int i=0; i<2; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		dy+=full[position];
		position++;
	}
	if(isdigit(full[position])) error+=1;
	position++;

	for(int i=0; i<2; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		hr+=full[position];
		position++;
	}
	if(isdigit(full[position])) error+=1;
	position++;

	for(int i=0; i<2; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		minute+=full[position];
		position++;
	}
	if(isdigit(full[position])) error+=1;
	position++;

	for(int i=0; i<2; i++){
		if(!isdigit(full[position])){
			error+=1;
			break;
		}
		second+=full[position];
		position++;
	}
	if(error==0){
		dt.year=stoi(yr);
		dt.month=stoi(mnth);
		dt.day=stoi(dy);
		dt.hour=stoi(hr);
		dt.min=stoi(minute);
		dt.sec=stoi(second);
	}
	else{
		time_t rawtime;
		struct tm * timeinfo;

		time (&rawtime);
		timeinfo = localtime(&rawtime);
		dt.year=timeinfo->tm_year;
		dt.year+=1900;
		dt.month=timeinfo->tm_mon;
		dt.day=timeinfo->tm_mday;
		dt.hour=timeinfo->tm_hour;
		dt.min=timeinfo->tm_min;
		dt.sec=timeinfo->tm_sec;
	}
	return is;

}
