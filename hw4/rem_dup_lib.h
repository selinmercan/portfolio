#ifndef REMDUPLIB_H
#define REMDUPLIB_H
#include <sstream>
#include <fstream>
#include <string>
#include <iostream>

struct Item {
  Item(int v, Item* n) { val = v; next = n; }
  int val;
  Item* next;
};

// Deletes consecutive equal values from the list
void removeConsecutive(Item* head);

Item* concatenate(Item* head1, Item* head2);

Item* concatenate_Helper1(Item* head1, Item* total);

Item* concatenate_Helper2(Item* head1, Item* head2, Item* total);

Item* readLists_helper(Item*& head, std::stringstream& ss);

void readLists(Item*& head1, Item*& head2, std::ifstream& ifile, std::ofstream& ofile);

#endif
