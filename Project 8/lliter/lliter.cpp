#include <iostream>
#include "lliter.h"
void create_list(Item* &head, int min, int max, Item* &new_list);
//void reduce_list(Item* &head, int min, int max);
// You may add prototypes for helper functions here
Item* extractRange(Item* &head, int min, int max)
{
	if(head==NULL) return NULL;
	Item* to_be_extracted(0);
	create_list(head, min, max, to_be_extracted);
	return to_be_extracted;
  // Add your code here
  
}
void create_list(Item* &head, int min, int max, Item* &new_list){
	if(head==NULL) {
		new_list=NULL;
		return;
	}
	if(head->val <= max && head->val >= min){
		new_list=head;
		head=head->next;
		create_list(head, min, max, new_list->next);
	}
	else create_list(head->next, min, max, new_list);
}





