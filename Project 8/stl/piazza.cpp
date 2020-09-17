#include "piazza.h"
using namespace std;

// Helper function to convert a string of text to individual words
std::vector<std::string> makeWords(const std::string& s)
{
  std::vector<std::string> words;
  std::stringstream ss(s);
  std::string temp;
  while(ss >> temp){
    words.push_back(temp);
  }
  return words;
}

// Complete the constructor below as needed
Piazza::Piazza()
{

}

// Complete the destructor below as needed
Piazza::~Piazza()
{

}



// Runs in O(log(n)), assuming n >> t where n is the number  
// of existing posts and t is the number of words in the post.
void Piazza::addPost(const std::string& data, bool pinned)
{
  // You may change this line and add the remainder of your
  // implementation
  std::vector<std::string> words = makeWords(data);
  push_front(data);
  if(pinned)pinned_posts.push_back(data);
  for(uint i=0; i<words.size(); i++){
    matches[words[i]]+=1;
  }
  size++;

  
}

// Complete the code below.
// Remember this must run in O(n*log(t)) where n is the number
// of posts and t is the number of words per post
size_t Piazza::numMatches(const std::string& term)
{
  return matches[term];

}

// Complete the code below.
// Must run in O(i)
const std::string& Piazza::getIthMostRecentPost(size_t i)
{
  if(i>=size)throw std::out_of_range("index is out of range");
  
    Post* temp=head;
    for(uint k=0; k<i; k++)temp=temp->next;
  
  return temp->text;
}

// Complete the code below.
// Must run in O(i)
const std::string& Piazza::getIthMostRecentPinnedPost(size_t i)
{
  int b=0;
  for(uint k=0; k<pinned_posts.size()-i-1; k++)b++;
  return pinned_posts[b];
}

