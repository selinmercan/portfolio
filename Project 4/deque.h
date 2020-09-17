#ifndef DEQUE_H
#define DEQUE_H
#include <stdexcept>
/**
 * Implements a templated, array-based, 
 * double-ended queue.
 *
 * The Deque should allow for amortized O(1) 
 * insertion and removal from either end. 
 * It should dynamically resize, when it becomes
 * full, but never needs to shrink.  
 *
 * It should use a circular buffer approach
 * by tracking the "front" and "back" location.
 *
 */


template <typename T>
class Deque
{
 public:
  /* START - DO NOT CHANGE ANY OF THESE LINES */
  /**
   *  Default constructor 
   *   Create an array of size 1 by default
   */
  Deque();

  /**
   *  Default constructor 
   *   Create an array of the given capacity.
   */
  Deque(size_t capacity);

  /**
   *  Destructor
   */
  ~Deque();

  /**
   *  Returns the number of elements in the Deque. 
   *   - Must run in O(1)
   */
  size_t size() const;

  /**
   *  returns true if the deque is empty
   *   - Must run in O(1)
   */
  bool empty() const;

  /**
   *  returns  the i-th element from the front
   *   - Must run in O(1)
   *   - Must throw std::range_error if i is
   *      an invalid location
   */
  T& operator[](size_t i);

  /**
   *  returns  the i-th element from the front
   *   - Must run in O(1)
   *   - Must throw std::range_error if i is
   *      an invalid location
   */
  T const & operator[](size_t i) const;

  /**
   *  Inserts a new item at the back of the
   *  Deque
   *   - Must run in amortized O(1)
   */
  void push_back(const T& item);

  /**
   *  Inserts a new item at the front of the
   *  Deque
   *   - Must run in amortized O(1)
   */
  void push_front(const T& item);

  /**
   *  Removes the back item from the Deque
   *   - Must run in amortized O(1)
   *   - Simply return if the deque is empty
   */
  void pop_back();

  /**
   *  Removes the front item from the Deque
   *   - Must run in amortized O(1)
   *   - Simply return if the deque is empty
   */
  void pop_front();
  /* END - DO NOT CHANGE ANY OF THESE LINES */
 private:
  T* deque_array;
  int size_;
  int array_size;
  int front_index;
  int back_index;
  /* Add data members and private helper 
   * functions below 
   */

  
};

/* Implement each Deque<T> member function below 
 * taking care to meet the runtime requirements
 */





#endif

template<typename T>
Deque<T>::Deque(){
  deque_array= new T[1];
  array_size=1;
  back_index=0;
  front_index=0;
  size_=0;
}

template<typename T>
Deque<T>::~Deque(){
  delete[] deque_array;
}

template<typename T>
Deque<T>::Deque(size_t capacity){
  deque_array=new T[capacity];
  array_size=capacity;
  back_index=0;
  front_index=0;
  size_=0;
}

template<typename T>
size_t Deque<T>::size() const{
  return size_;
}

template<typename T>
bool Deque<T>::empty() const{
  if(size_==0)return true;
  return false;
}

template<typename T>
T& Deque<T>::operator[](size_t i){
  if(i>=(unsigned int)size_ || i<0){
    throw std::range_error("index out of range");
  }
  else{
    return deque_array[(front_index+i)%array_size];
  }
}

template<typename T>
T const & Deque<T>::operator[](size_t i) const{
  if(i>=(unsigned int)size_ || i<0){
    throw std::range_error("index out of range");
  }
  else{
    return deque_array[(front_index+i)%array_size];
  }
}

template<typename T>
void Deque<T>::push_back(const T& item){
  if(size_==array_size){
    int k=0;
    array_size*=2;
    T* temp_array= new T[array_size];
    if(front_index<back_index)
    {  
      for(int i=front_index; i<size_; i++){
        temp_array[k]=deque_array[i];
        k++;
       }
    }
    else if(front_index==back_index){
      temp_array[0]=deque_array[0];
      k++;
    }
    else{
      for(int i=front_index; i<size_; i++){
        temp_array[k]=deque_array[i];
        k++;
      }
      if (back_index==0)temp_array[k]=deque_array[0];
      else{   
        for(int i=0; i<back_index;i++){
               temp_array[k]=deque_array[i];
               k++;
        }
      }
    }
    delete[] deque_array;
    front_index=0;
    back_index=k;
    deque_array=temp_array;
    deque_array[back_index]=item;
  }
  else{
    back_index++;
    back_index=(back_index)%(array_size);
    deque_array[back_index]=item;

    
  }
  size_++;
}

template<typename T>
void Deque<T>:: push_front(const T& item){
  if(size_==array_size){
    int k=1;
    array_size*=2;
    T* temp_array= new T[array_size];
    if(front_index<back_index)
    {  for(int i=0; i<size_; i++){
        temp_array[i+1]=deque_array[i];
        //k++;
       }
    }
    else if(front_index==back_index){
      temp_array[1]=deque_array[0];
      k++;
    }
    else{
      for(int i=front_index; i<size_; i++){
        temp_array[k]=deque_array[i];
        k++;
      }
      if(back_index==0)temp_array[k]=deque_array[0];
      else{
        for(int i=0; i<back_index;i++){
          temp_array[k]=deque_array[i];
          k++;
        }
      }
    }
    front_index=0;
    back_index=k-1;
    delete[] deque_array;
    deque_array=temp_array;
    deque_array[front_index]=item;   
  }
  else{
    
    front_index--;
    front_index+=array_size;
    front_index=(front_index)%array_size;
    deque_array[front_index]=item;
    
  }
  size_++;
}

template<typename T>
void Deque<T>::pop_back(){
  if(this->empty())return;
  back_index--;
  back_index=back_index%array_size;
  size_--;
}

template<typename T>
void Deque<T>::pop_front(){
  if(this->empty())return;
  front_index++;
  front_index=front_index%array_size;
  size_--;
}


