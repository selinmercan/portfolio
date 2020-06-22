#include "rem_dup_lib.h"

using namespace std;

void removeConsecutive(Item* head){
	if(head->next==nullptr) return;
	if(head->next->val==head->val){
		Item* temp(0);
		if(head->next->next!=nullptr){
			temp=head->next->next;
			delete head->next;
			head->next=temp;
			return removeConsecutive(head);
		}
		else{
			delete head->next;
			head->next=nullptr;
			return removeConsecutive(head);
		}
	}
	return removeConsecutive(head->next);

}

Item* concatenate(Item* head1, Item* head2){
	Item* temp=head1;
	Item* temporary=concatenate_Helper1(head1, temp);
	Item* concatenated=concatenate_Helper2(head1, head2, temporary);
	return concatenated;
}

Item* concatenate_Helper1(Item* head1, Item* total){
	if(total->next==nullptr) return total;
	return concatenate_Helper1(head1, total->next);
}

Item* concatenate_Helper2(Item* head1, Item* head2, Item* total){
	if(head2==nullptr) return head1;
	total->next=head2;
	return concatenate_Helper2(head1, head2->next, total->next);
}

void readLists(Item*& head1, Item*& head2, ifstream& ifile, ofstream& ofile){
	string parse;
	getline(ifile, parse);
	stringstream ss(parse);
	head1=readLists_helper(head1, ss);
	getline(ifile, parse);
	stringstream s2(parse);
	head2=readLists_helper(head2, s2);
	removeConsecutive(head1);
	Item* head3=concatenate(head1, head2);
	while(head3!=NULL){
		ofile<<head3->val<<" ";
		head3=head3->next;
	}
	
}

Item* readLists_helper(Item*& head, stringstream& ss){
	int y;
	ss>>y;
	head=new Item(y, nullptr);
	Item* temp=head;
	while(true){
		int x;
		ss>>x;
		if(ss.fail())break;
		temp->next=new Item(x, nullptr);
		temp=temp->next;
	}
	return head;
}

