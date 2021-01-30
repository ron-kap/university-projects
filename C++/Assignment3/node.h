#ifndef NODE_H
#define NODE_H

#include <iostream>
using std::cout;
using std::cerr;
using std::endl;

// Do not add any #include statements here.  If you have a convincing need for adding a different `#include` please post in the forum on KEATS.

// TODO your code for the Node class goes here:
// (Note the 'NodeIterator' class will report lots of errors until Node exists


template<typename T>
class Node {

    public:
    
    T data;
    Node * next;
    Node * previous;

    Node(T newData)
        : data(newData), next(nullptr), previous(nullptr) {
            // empty constructor
    }
};


template<typename T>
class NodeIterator {
  
private:
    
    Node<T>* current;
    
public:
    

    NodeIterator(Node<T>* currentIn)
        : current(currentIn) {        
    }

    T & operator*() {
        return current->data;
    }

    // TODO: complete the code for NodeIterator here

    NodeIterator & operator++() {
        current = current->next;
        return *this;
    }

    bool operator==(const NodeIterator & second) const{
        return current == second.current;
    }

    bool operator!=(const NodeIterator & second) const{
        return current != second.current;
    }  
};

// do not edit below this line

#endif
