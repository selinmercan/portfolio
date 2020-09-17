#include "cmdhandler.h"
#include "util.h"
using namespace std;

QuitHandler::QuitHandler()
{

}

QuitHandler::QuitHandler(Handler* next)
  : Handler(next)
{

}

bool QuitHandler::canHandle(const std::string& cmd) const
{
	return cmd == "QUIT";

}

Handler::HANDLER_STATUS_T QuitHandler::process(TwitEng* eng, std::istream& instr) const
{
	eng->dumpFeeds();
	return HANDLER_QUIT;
}

AndHandler::AndHandler()
{

}

AndHandler::AndHandler(Handler* next)
  : Handler(next)
{

}

bool AndHandler::canHandle(const std::string& cmd) const
{
	return cmd == "AND";

}

Handler::HANDLER_STATUS_T AndHandler::process(TwitEng* eng, std::istream& instr) const
{
	vector<string> tags;
	string temp;
	getline(instr, temp);
	if(instr.fail()) return HANDLER_ERROR;
	stringstream ss(temp);
	while(!ss.fail()){
		string temp2;
		ss>>temp2;
		if(temp2.empty()) continue;
		tags.push_back(temp2);
	}
	vector<Tweet*> returned;
	returned=eng->search(tags, 0);
	displayHits(returned);
	return HANDLER_OK;
}

OrHandler::OrHandler()
{

}

OrHandler::OrHandler(Handler* next)
  : Handler(next)
{

}

bool OrHandler::canHandle(const std::string& cmd) const
{
	return cmd == "OR";

}

Handler::HANDLER_STATUS_T OrHandler::process(TwitEng* eng, std::istream& instr) const
{
	vector<string> tags;
	string temp;
	getline(instr, temp);
	if(instr.fail()) return HANDLER_ERROR;
	stringstream ss(temp);
	while(!ss.fail()){
		string temp2;
		ss>>temp2;
		if(temp2.empty()) continue;
		//if(temp2.empty()) return HANDLER_ERROR;
		tags.push_back(temp2);
		//size++;
	}
	vector<Tweet*> returned;
	returned=eng->search(tags, 1);
	displayHits(returned);
	return HANDLER_OK;
}

TweetHandler::TweetHandler()
{

}

TweetHandler::TweetHandler(Handler* next)
  : Handler(next)
{

}

bool TweetHandler::canHandle(const std::string& cmd) const
{
	return cmd == "TWEET";

}

Handler::HANDLER_STATUS_T TweetHandler::process(TwitEng* eng, std::istream& instr) const
{
	if(instr.fail()) return HANDLER_ERROR;
	string username;
	instr>>username;
	string text;
	while(!instr.fail()){
		string temp1;
		instr>>temp1;
		text+=temp1+ " ";
	}
	DateTime time;
	eng->addTweet(username, time, text);
	return HANDLER_OK;
}




