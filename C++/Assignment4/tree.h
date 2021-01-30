#ifndef TREE_H
#define TREE_H

#include "treenode.h"


template<typename T>
class BinarySearchTree {
    private:
    unique_ptr<TreeNode<T>> root;						

    public:
    BinarySearchTree() {

    }
	
	// copy cons
	BinarySearchTree(const BinarySearchTree<T> & toCopy) {
		// this is new tree
		if (toCopy.root) {
			copyTree(root, toCopy.root);
		}
		else {
			root = nullptr;
		}
	}
	
	void copyTree(unique_ptr<TreeNode<T>> & copying, const unique_ptr<TreeNode<T>> & toCopy) const {
		
		if (toCopy) {
			copying = unique_ptr<TreeNode<T>>(new TreeNode<T>(toCopy->data));	
			copyTree(copying->leftChild, toCopy->leftChild);
			if (copying->leftChild) {	
				copying->leftChild->parent = copying.get();
			}
			copyTree(copying->rightChild, toCopy->rightChild);
			if (copying->rightChild) {
				copying->rightChild->parent = copying.get();
			}
		}
		else {
			copying = nullptr;
		}
	}

	void operator=(const BinarySearchTree & toCopy) {			
		if (toCopy.root) {									
			copyTree(root, toCopy.root);
		}
		else {
			root = nullptr;
		}
	}

	void write(ostream & text) const {
		if (root) {
			root->write(text);
		}
		else {
			return;
		}
    }

	TreeNode<T>* insert(const T & insertData) {
		if (!root) {
			root = unique_ptr<TreeNode<T>>(new TreeNode<T>(insertData));
			return root.get();
		}

				// TESTING
		TreeNode<T> * parent = nullptr;
		TreeNode<T> * current = root.get();
		while (current) {
			parent = current;

			if (insertData < current->data) {
				current = (current->leftChild).get();
			}
			else if (current->data < insertData) {
				current = (current->rightChild).get();
			}
			else {
				return current;
			}
		}

		if (insertData < parent->data) {
			parent->setLeftChild(new TreeNode<T>(insertData));	
			return parent->leftChild.get();
		}
		else {
			parent->setRightChild(new TreeNode<T>(insertData));	
			return parent->rightChild.get();
		}
				// TESTING COMP
    }

	TreeNode<T>* find(const T & findData) const {
		
				// TESTING
		TreeNode<T> * current = root.get();
		while (current) {

			if (findData < current->data) {
				current = (current->leftChild).get();
			}
			else if (current->data < findData) {
				current = (current->rightChild).get();
			}
			else {
				return current;
			}
		}

		return current;
	}
};


// do not edit below this line

#endif
