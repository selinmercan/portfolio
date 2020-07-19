#ifndef RBBST_H
#define RBBST_H

#include <iostream>
#include <exception>
#include <cstdlib>
#include <algorithm>
#include "bst.h"

struct KeyError { };

/**
* A special kind of node for an AVL tree, which adds the balance as a data member, plus
* other additional helper functions. You do NOT need to implement any functionality or
* add additional data members or helper functions.
*/
template <typename Key, typename Value>
class AVLNode : public Node<Key, Value>
{
public:
    // Constructor/destructor.
    AVLNode(const Key& key, const Value& value, AVLNode<Key, Value>* parent);
    virtual ~AVLNode();

    // Getter/setter for the node's this->height.
    char getBalance () const;
    void setBalance (char balance);
    void updateBalance(char diff);

    // Getters for parent, left, and right. These need to be redefined since they
    // return pointers to AVLNodes - not plain Nodes. See the Node class in bst.h
    // for more information.
    virtual AVLNode<Key, Value>* getParent() const override;
    virtual AVLNode<Key, Value>* getLeft() const override;
    virtual AVLNode<Key, Value>* getRight() const override;

protected:
    char balance_;
};

/*
  -------------------------------------------------
  Begin implementations for the AVLNode class.
  -------------------------------------------------
*/

/**
* An explicit constructor to initialize the elements by calling the base class constructor and setting
* the color to red since every new node will be red when it is first inserted.
*/
template<class Key, class Value>
AVLNode<Key, Value>::AVLNode(const Key& key, const Value& value, AVLNode<Key, Value> *parent) :
    Node<Key, Value>(key, value, parent), balance_(0)
{

}

/**
* A destructor which does nothing.
*/
template<class Key, class Value>
AVLNode<Key, Value>::~AVLNode()
{

}

/**
* A getter for the balance of a AVLNode.
*/
template<class Key, class Value>
char AVLNode<Key, Value>::getBalance() const
{
    return balance_;
}

/**
* A setter for the balance of a AVLNode.
*/
template<class Key, class Value>
void AVLNode<Key, Value>::setBalance(char balance)
{
    balance_ = balance;
}

/**
* Adds diff to the balance of a AVLNode.
*/
template<class Key, class Value>
void AVLNode<Key, Value>::updateBalance(char diff)
{
    balance_ += diff;
}

/**
* An overridden function for getting the parent since a static_cast is necessary to make sure
* that our node is a AVLNode.
*/
template<class Key, class Value>
AVLNode<Key, Value> *AVLNode<Key, Value>::getParent() const
{
    return static_cast<AVLNode<Key, Value>*>(this->parent_);
}

/**
* Overridden for the same reasons as above.
*/
template<class Key, class Value>
AVLNode<Key, Value> *AVLNode<Key, Value>::getLeft() const
{
    return static_cast<AVLNode<Key, Value>*>(this->left_);
}

/**
* Overridden for the same reasons as above.
*/
template<class Key, class Value>
AVLNode<Key, Value> *AVLNode<Key, Value>::getRight() const
{
    return static_cast<AVLNode<Key, Value>*>(this->right_);
}


/*
  -----------------------------------------------
  End implementations for the AVLNode class.
  -----------------------------------------------
*/


template <class Key, class Value>
class AVLTree : public BinarySearchTree<Key, Value>
{
public:
    virtual void insert (const std::pair<const Key, Value> &new_item); // TODO
    virtual void remove(const Key& key);  // TODO
protected:
    virtual void nodeSwap( AVLNode<Key,Value>* n1, AVLNode<Key,Value>* n2);
    void insert_fix(AVLNode<Key,Value>* p);
    void removeFix(AVLNode<Key,Value>* n, int diff);
    void right_zigzig(AVLNode<Key,Value>* z, AVLNode<Key,Value>* x, AVLNode<Key,Value>* y);
    void left_zigzig(AVLNode<Key,Value>* z, AVLNode<Key,Value>* x, AVLNode<Key,Value>* y);
    void right_zigzag(AVLNode<Key,Value>* x, AVLNode<Key,Value>* y, AVLNode<Key,Value>* z);
    void left_zigzag(AVLNode<Key,Value>* x, AVLNode<Key,Value>* y, AVLNode<Key,Value>* z);
    // Add helper functions here


};

/*
 * Recall: If key is already in the tree, you should 
 * overwrite the current value with the updated value.
 */
template<class Key, class Value>
void AVLTree<Key, Value>::insert (const std::pair<const Key, Value> &new_item)
{   
    //BinarySearchTree<Key, Value>::insert(new_item);
    if(this->root_==NULL){//if you are inserting the root
        AVLNode<Key, Value>* newnode= new AVLNode<Key, Value>(new_item.first, new_item.second, NULL);
        this->root_=newnode;
    }
    else{
        AVLNode<Key, Value>* temp= (AVLNode<Key, Value>*)this->root_;
        while(temp->getLeft()!=NULL||temp->getRight()!=NULL){//starts from the root and checks if the value to be inserted
            if(new_item.first>temp->getKey() && temp->getRight()!=NULL){//is larger or smaller than the current node
                temp=temp->getRight();//and traverses across the tree to find the right place
            }
            else if(new_item.first<temp->getKey() && temp->getLeft()!=NULL){
                temp=temp->getLeft();
            }
            else{
                break;
            }
        }
        if(new_item.first==temp->getKey()){//if the node found has the same key as the value to be inserted then
            temp->setValue(new_item.second);//overwrites the value of the key with the new value
        }
        else{
            AVLNode<Key, Value>* newnode= new AVLNode<Key, Value>(new_item.first, new_item.second, temp);
            newnode->setLeft(NULL);//else creates a new node and adds it to the right or left of that node, depending
            newnode->setRight(NULL);//on the key
            if(temp->getKey()>new_item.first){
                if(temp->getLeft()==NULL){
                    temp->setLeft(newnode);
                    newnode->setParent(temp);
                }
                else{
                    temp->getLeft()->setParent(newnode);
                    newnode->setLeft(temp->getLeft());
                    temp->setLeft(newnode);
                }
            }
            else if(temp->getKey()<new_item.first){
                if(temp->getRight()==NULL){
                    temp->setRight(newnode);
                    newnode->setParent(temp);
                }
                else{
                    temp->getRight()->setParent(newnode);
                    newnode->setRight(temp->getRight());
                    temp->setRight(newnode);
                }
            }   
        }
    }
    AVLNode<Key, Value>* temp=(AVLNode<Key, Value>*)this->internalFind(new_item.first);
    temp->setBalance(0);
    insert_fix(temp);
// TODO
}

template<class Key, class Value>
void AVLTree<Key, Value>::insert_fix(AVLNode<Key,Value>* p){
    if(p==NULL || p->getParent()==NULL)return;
    AVLNode <Key, Value>* g=p->getParent();
    if(p==g->getRight()){
        g->updateBalance(1);
        if(g->getBalance()==0)return;
        else if(g->getBalance()==1)insert_fix(p->getParent());
        else if(g->getBalance()==2){
            if(p->getRight()!=NULL){
                right_zigzig(g, p, p->getRight());
                p->setBalance(0);
                g->setBalance(0);
            }
            else if(p->getLeft()!=NULL){
                AVLNode <Key, Value>* n=p->getLeft();
                right_zigzag(g, p, p->getLeft());
                if(n->getBalance()==1){
                    p->setBalance(0);
                    g->setBalance(-1);
                    n->setBalance(0);
                }
                else if(n->getBalance()==0){
                    p->setBalance(0);
                    g->setBalance(0);
                    n->setBalance(0);
                }
                else if(n->getBalance()==-1){
                    p->setBalance(1);
                    g->setBalance(0);
                    n->setBalance(0);
                }
            }
        }
    }
    else if(p==g->getLeft()){
        g->updateBalance(-1);
        if(g->getBalance()==0)return;
        else if(g->getBalance()==-1)insert_fix(p->getParent());
        else if(g->getBalance()==-2){
            if(p->getLeft()!=NULL){
                left_zigzig(g, p, p->getLeft());
                p->setBalance(0);
                g->setBalance(0);
            }
            else if(p->getRight()!=NULL){
                AVLNode <Key, Value>* n=p->getRight();
                left_zigzag(g, p, p->getRight());
                if(n->getBalance()==-1){
                    p->setBalance(0);
                    g->setBalance(1);
                    n->setBalance(0);
                }
                else if(n->getBalance()==0){
                    p->setBalance(0);
                    g->setBalance(0);
                    n->setBalance(0);
                }
                else if(n->getBalance()==1){
                    p->setBalance(-1);
                    g->setBalance(0);
                    n->setBalance(0);
                }
            }
        }
    }
}

template<class Key, class Value>
void AVLTree<Key, Value>::right_zigzig(AVLNode<Key,Value>* z, AVLNode<Key,Value>* x, AVLNode<Key,Value>* y){
    AVLNode<Key, Value>* T3=x->getLeft();
    z->setRight(T3);
    if(T3!=NULL)T3->setParent(z);
    x->setLeft(z);
    x->setParent(z->getParent());
    if(z->getParent()!=NULL){
        if(z==z->getParent()->getRight())z->getParent()->setRight(x);
        else z->getParent()->setLeft(x);
    }
    x->setParent(z->getParent());
    z->setParent(x);
    if(z==this->root_)this->root_=x;
}

template<class Key, class Value>
void AVLTree<Key, Value>::left_zigzig(AVLNode<Key,Value>* z, AVLNode<Key,Value>* x, AVLNode<Key,Value>* y){
    AVLNode<Key, Value>* T3=x->getRight();
    if(T3!=NULL)T3->setParent(z);
    z->setLeft(T3);
    x->setRight(z);
    x->setParent(z->getParent());
    if(z->getParent()!=NULL){
        if(z==z->getParent()->getRight())z->getParent()->setRight(x);
        else z->getParent()->setLeft(x);
    }
    z->setParent(x); 
    if(z==this->root_)this->root_=x;
}

template<class Key, class Value>
void AVLTree<Key, Value>::left_zigzag(AVLNode<Key,Value>* x, AVLNode<Key,Value>* y, AVLNode<Key,Value>* z){
    AVLNode<Key, Value>* T1=z->getRight();
    AVLNode<Key, Value>* T2=z->getLeft();
    x->setLeft(T1);
    if(T1!=NULL)T1->setParent(x);
    y->setRight(T2);
    if(T2!=NULL)T2->setParent(y);
    z->setParent(x->getParent());
    if(x->getParent()!=NULL){
        if(x==x->getParent()->getRight())x->getParent()->setRight(z);
        else x->getParent()->setLeft(z);
    }
    z->setParent(x->getParent());
    z->setLeft(y);
    z->setRight(x);
    x->setParent(z);
    y->setParent(z);
    if(x==this->root_)this->root_=z;
}

template<class Key, class Value>
void AVLTree<Key, Value>::right_zigzag(AVLNode<Key,Value>* x, AVLNode<Key,Value>* y, AVLNode<Key,Value>* z){
    AVLNode<Key, Value>* T1=z->getLeft();
    AVLNode<Key, Value>* T2=z->getRight();
    x->setRight(T1);
    if(T1!=NULL)T1->setParent(x);
    y->setLeft(T2);
    if(T2!=NULL)T2->setParent(y);
    if(x->getParent()!=NULL){
        if(x==x->getParent()->getRight())x->getParent()->setRight(z);
        else x->getParent()->setLeft(z);
    }
    z->setParent(x->getParent());
    z->setRight(y);
    y->setParent(z);
    z->setLeft(x);
    x->setParent(z);
    if(x==this->root_)this->root_=z;
}
/*
 * Recall: The writeup specifies that if a node has 2 children you
 * should swap with the predecessor and then remove.
 */
template<class Key, class Value>
void AVLTree<Key, Value>:: remove(const Key& key)
{
    AVLNode<Key, Value>* temp=(AVLNode<Key, Value>*)this->internalFind(key);
    if(temp!=NULL){
        int diff=0;
        if(temp->getParent()!=NULL){
            if(temp==temp->getParent()->getRight())diff=-1;
            else diff=1;
        }
        temp=temp->getParent();
        BinarySearchTree<Key, Value>::remove(key);        
        if(diff!=0)removeFix(temp, diff);
       
    }

    // TODO
}

template<class Key, class Value>
void AVLTree<Key, Value>::removeFix(AVLNode<Key,Value>* n, int diff){
    if(n==NULL)return;
    AVLNode<Key, Value>* p=n->getParent();
    int ndiff;
    if(p!=NULL){
        if(n==p->getLeft())ndiff=1;
        else ndiff=-1;
    }
    if(diff==-1){
        if(n->getBalance()+diff==-2){
            if(this->height(n->getLeft())>this->height(n->getRight())){//if the taller of the two children is the left side
                AVLNode<Key, Value>* c=n->getLeft();
                if(c->getBalance()==-1){
                    left_zigzig(n, c, c->getLeft());
                    n->setBalance(0);
                    c->setBalance(0);
                    removeFix(p, ndiff);
                }
                else if(c->getBalance()==0){
                    left_zigzig(n, c, c->getLeft());
                    n->setBalance(-1);
                    c->setBalance(1);
                }
                else if(c->getBalance()==1){
                    AVLNode<Key, Value>* g=c->getRight();
                    left_zigzag(n, c, g);
                    if(g->getBalance()==1){
                        n->setBalance(0);
                        c->setBalance(-1);
                        g->setBalance(0);
                    }
                    else if(g->getBalance()==0){
                        n->setBalance(0);
                        c->setBalance(0);
                        g->setBalance(0);
                    }
                    else if(g->getBalance()==-1){
                        n->setBalance(1);
                        c->setBalance(0);
                        g->setBalance(0);
                    }
                    removeFix(p, ndiff);
                }
            }
        }
        else if(n->getBalance()+diff==-1)n->setBalance(-1);
        else if(n->getBalance()+diff==0){
            n->setBalance(0);
            removeFix(p, ndiff);
        }
    }
    else if(diff==1){
        if(n->getBalance()+diff==2){//if the difference exceeds the maximum difference allowed for AVLs
            if(this->height(n->getLeft())<this->height(n->getRight())){
                AVLNode<Key, Value>* c=n->getRight();
                if(c->getBalance()==1){
                    right_zigzig(n, c, c->getRight());
                    n->setBalance(0);
                    c->setBalance(0);
                    removeFix(p, ndiff);
                }
                else if(c->getBalance()==0){
                    right_zigzig(n, c, c->getRight());
                    n->setBalance(1);
                    c->setBalance(-1);
                }
                else if(c->getBalance()==-1){
                    AVLNode<Key, Value>* g=c->getLeft();
                    right_zigzag(n, c, g);
                    if(g->getBalance()==-1){
                        n->setBalance(0);
                        c->setBalance(1);
                        g->setBalance(0);
                    }
                    else if(g->getBalance()==0){
                        n->setBalance(0);
                        c->setBalance(0);
                        g->setBalance(0);
                    }
                    else if(g->getBalance()==1){
                        n->setBalance(-1);
                        c->setBalance(0);
                        g->setBalance(0);
                    }
                    removeFix(p, ndiff);
                }
            }
        }
        else if(n->getBalance()+diff==1)n->setBalance(1);
        else if(n->getBalance()+diff==0){
            n->setBalance(0);
            removeFix(p, ndiff);
        }
    }
}
template<class Key, class Value>
void AVLTree<Key, Value>::nodeSwap( AVLNode<Key,Value>* n1, AVLNode<Key,Value>* n2)
{
    BinarySearchTree<Key, Value>::nodeSwap(n1, n2);
    char tempB = n1->getBalance();
    n1->setBalance(n2->getBalance());
    n2->setBalance(tempB);
}


#endif
