#ifndef TREENODE_H
#define TREENODE_H

#include <iostream>
using std::cout;
using std::cerr;
using std::endl;
using std::ostream;

#include <memory>
using std::unique_ptr;

#include <utility>
using std::pair;


template<typename T>
class TreeNode {
    public:
    T data;
	unique_ptr<TreeNode> leftChild;						
    unique_ptr<TreeNode> rightChild;				
    TreeNode<T> * parent;

    TreeNode(T dataStore) : data(dataStore), parent(nullptr) {

    }

    void setLeftChild(TreeNode* child) {
		leftChild = unique_ptr<TreeNode>(child);
        child->parent = this; 
    }

    void setRightChild(TreeNode* child) {
        rightChild = unique_ptr<TreeNode>(child);
        child->parent = this;
    }

    void write(ostream & text) const{
        if(leftChild) {
            leftChild->write(text);
        }
        text << " " ;
        text << data;
        text << " " ;
        if(rightChild) {
            rightChild->write(text);
        }
    }
};


// do not edit below this line

#endif
