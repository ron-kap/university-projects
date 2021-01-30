#ifndef TREEMAP_H
#define TREEMAP_H

#include "tree.h"

template<typename Key, typename Value>
class KeyValuePair {
    
public:
    
    const Key k;
    Value v;
      

	KeyValuePair(Key newKey, Value newValue) : k(newKey), v(newValue) {

	}
    
	KeyValuePair(Key newKey) : k(newKey) {

	}

	bool operator<(const KeyValuePair<Key,Value> & compare) const {		
		if (k < compare.k) {
			return true;
		}
		else {
			return false;
		}
	}
};



template<typename Key, typename Value>
ostream & operator<< (ostream & o, const KeyValuePair<Key,Value> & kv){
    o << kv.k << "," << kv.v;
    return o;
}



template<typename Key, typename Value>
class TreeMap {
  
private:
    BinarySearchTree<KeyValuePair<Key,Value> > tree;
    
public:

	KeyValuePair<Key,Value> * insert(const Key & k, const Value & v) {	
		return &(tree.insert(KeyValuePair<Key, Value>(k, v))->data);
	}

	void write(ostream & o) const {
		tree.write(o);
	}

	// TODO your code for TreeMap goes here:


	KeyValuePair<Key,Value> * find(const Key & findKey) const {	
		KeyValuePair<Key,Value> temp = KeyValuePair<Key,Value>(findKey);
		KeyValuePair<Key, Value> * temp2 = &tree.find(temp)->data;
		return temp2;
	}
};


// do not edit below this line

#endif