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
	//string temp;
	//getline(instr, temp);
	if(instr.fail()) return HANDLER_ERROR;
	//stringstream ss(temp);
	string temp2;
	while(instr>>temp2){
		if(temp2.empty()) continue;
		tags.push_back(temp2);
	}
	//for(int i=0; i<tags.size(); i++)std::cout<<tags[i]<<std::endl;
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
	if(instr.fail()) return HANDLER_ERROR;
	string temp2;
	while(instr>>temp2){
		if(temp2.empty()) continue;
		tags.push_back(temp2);
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
	string username;
	instr>>username;
	cout << username << endl;
	string text;
	string temp1;
	getline(instr, text);
	if(instr.fail()) return HANDLER_ERROR;
	if(eng->exists(username)==false)return HANDLER_ERROR;
	text=trim(text);
	DateTime time;
	cout << text << endl;
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
	string following, followed;
	instr>>following;
	if(instr.fail()) return HANDLER_ERROR;
	if(following.empty())return HANDLER_ERROR;
	if(eng->exists(following)) return HANDLER_ERROR;
	instr>>followed;
	if(instr.fail()) return HANDLER_ERROR;
	if(followed.empty())return HANDLER_ERROR;
	if(eng->exists(followed))return HANDLER_ERROR;
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
	string file;
	instr>>file;
	if(instr.fail()) return HANDLER_ERROR;
	if(file.empty())return HANDLER_ERROR;
	eng->dumpSave(file);
	return HANDLER_OK;
}



