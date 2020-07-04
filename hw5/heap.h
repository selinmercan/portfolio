#ifndef HEAP_H
#define HEAP_H
#include <functional>
#include <stdexcept>
#include <vector>

template <typename T, typename PComparator = std::less<T> >
class Heap
{
 public:
  /// Constructs an m-ary heap for any m >= 2
  Heap(int m, PComparator c = PComparator());

  /// Destructor as needed
  ~Heap();

  /// Adds an item
  void push(const T& item);

  /// returns the element at the top of the heap 
  ///  max (if max-heap) or min (if min-heap)
  T const & top() const;

  /// Removes the top element
  void pop();

  /// returns true if the heap is empty
  bool empty() const;

 private:
 std::vector<T> heap;
 PComparator comp;
 void trickle_Up(std::vector<T>& data, int loc, PComparator& c, int k);
 void heapify(std::vector<T>& data, int loc, size_t effsize, PComparator& c, int k);
 int k;
  /// Add whatever helper functions and data members you need below




};

template <typename T, typename PComparator>
Heap<T, PComparator>::Heap(int m, PComparator c){
  comp=c;
  k=m;
}

template <typename T, typename PComparator>
Heap<T, PComparator>::~Heap(){}
// We will start top() for you to handle the case of 
// calling top on an empty heap
template <typename T, typename PComparator>
T const & Heap<T,PComparator>::top() const
{
  // Here we use exceptions to handle the case of trying
  // to access the top element of an empty heap
  if(empty()){
    throw std::logic_error("can't top an empty heap");
  }

  return heap[0];
  // If we get here we know the heap has at least 1 item
  // Add code to return the top element



}


// We will start pop() for you to handle the case of 
// calling top on an empty heap
template <typename T, typename PComparator>
void Heap<T,PComparator>::pop()
{
  if(empty()){
    throw std::logic_error("can't pop an empty heap");
  }

  heap[0]=heap[heap.size()-1];
  heap.pop_back();
  heapify(heap, 0, heap.size(), comp, k);

}

template <typename T, typename PComparator>
bool Heap<T,PComparator>::empty() const{
  if(heap.size()==0)return true;
  return false;
}

template <typename T, typename PComparator>
void Heap<T,PComparator>::heapify(std::vector<T>& data, int loc, size_t effsize, PComparator& c, int k){
  int count=0;
  for(int i=1; i<=k; i++){//checks whether the location is a leaf node so whether all of the children
    if(k*loc+i>=(int)effsize)count++;//exceed the efficient size of the vector
  }
  if(count==k)return;//if count is equal to the size of the number of children possible, end the function
  int wanted_child=k*loc+1;
  for(int i=2; i<=k; i++){
    if(k*loc+i<(int)effsize)if(c(data[k*loc+i],data[wanted_child]))wanted_child=k*loc+i;
  }//if a child is in the bounds of a vector and if the comparator returns true, wanted child becomes that child
  if(c(data[wanted_child], data[loc])){
    T temp=data[loc];
    data[loc]=data[wanted_child];
    data[wanted_child]=temp;
    heapify(data, wanted_child, effsize, c, k);
  }
}

template <typename T, typename PComparator>
void Heap<T, PComparator>::trickle_Up(std::vector<T>& data, int loc, PComparator& c, int k){
  int parent=(loc-1)/k;
  while(parent>=0&&c(data[loc], data[parent])){
    T temp=data[parent];
    data[parent]=data[loc];
    data[loc]=temp;
    loc=parent;
    parent=(loc-1)/k;
  }
}

template <typename T, typename PComparator>
void Heap<T,PComparator>::push(const T& item){
  heap.push_back(item);
  trickle_Up(heap, heap.size()-1, comp, k);
}
#endif

