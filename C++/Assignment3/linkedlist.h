#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include "node.h"

#include <utility>


// Do not add any #include statements here.  If you have a convincing need for adding a different `#include` please post in the forum on KEATS.

// TODO your code goes here:

template<typename T>
class LinkedList{
public:
    Node<T> * head;
    Node<T> * tail;
    int count;

    LinkedList() : head(nullptr), tail(nullptr), count(0) {
        // empty constructor
    }
	
	~LinkedList() {
		Node<T> * toDel = head;

		while (toDel) {
			Node<T> * nextDel = toDel->next;
			delete toDel;
			toDel = nextDel;
		}
	}

    void push_front(const T & itemToPush) {
		if (head) {
			Node <T> * newNode = new Node<T>(itemToPush);
			newNode->previous = nullptr;
			newNode->next = head;
			head->previous = newNode;
			head = newNode;
		}
		else {
			Node<T> * newNode = new Node<T>(itemToPush);
			head = newNode;
			tail = newNode;
			newNode->previous = nullptr;
			newNode->next = nullptr;
		}
		++count;
    }

    T& front() const {
			return head->data;
    }

    void push_back(const T & itemToPush) {
		if (tail) {
			Node<T> * newNode = new Node<T>(itemToPush);
			newNode->next = nullptr;
			newNode->previous = tail;
			tail->next = newNode;
			tail = newNode;
		}
		else {
			Node <T> * newNode = new Node<T>(itemToPush);
			head = newNode;
			tail = newNode;
			newNode->previous = nullptr;
			newNode->next = nullptr;
		}
		++count;
    }

    T& back() const {
			return tail->data;
    }

    int size() const {
		return count;
    }

	NodeIterator<T> begin() const {
		return NodeIterator<T>(head);
	}

	NodeIterator<T> end() const {
		return NodeIterator<T>(tail->next);	
	}

	void reverse() {
		Node<T> * current = head;

		while (current) {
			Node<T> * temp = current->next;
			current->next = current->previous;
			current->previous = temp;

			if (!temp) {
				tail = head;
				head = current;
			}

			current = temp;
		}
	}
};


// do not edit below this line

#endif
