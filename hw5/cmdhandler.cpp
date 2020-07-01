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
	std::cout<<returned.size()<< " matches:" << std::endl;
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
	std::cout<<returned.size()<< " matches:" << std::endl;
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
	if(eng->exists(username)==false)return HANDLER_ERROR;
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

FollowHandler::FollowHandler()
{

}

FollowHandler::FollowHandler(Handler* next)
  : Handler(next)
{

}

bool FollowHandler::canHandle(const std::string& cmd) const
{
	return cmd == "FOLLOW";

}

Handler::HANDLER_STATUS_T FollowHandler::process(TwitEng* eng, std::istream& instr) const
{
	if(instr.fail()) return HANDLER_ERROR;
	string following, followed;
	instr>>following;
	instr>>followed;
	eng->add_Follower(following, followed);
	return HANDLER_OK;
}

SaveHandler::SaveHandler()
{

}

SaveHandler::SaveHandler(Handler* next)
  : Handler(next)
{

}

bool SaveHandler::canHandle(const std::string& cmd) const
{
	return cmd == "SAVE";

}

Handler::HANDLER_STATUS_T SaveHandler::process(TwitEng* eng, std::istream& instr) const
{
	if(instr.fail()) return HANDLER_ERROR;
	string file;
	instr>>file;
	cout << file << endl;
	eng->dumpSave(file);
	return HANDLER_OK;
}



