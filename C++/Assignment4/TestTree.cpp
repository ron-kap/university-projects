#include "treenode.h"
#include "tree.h"

#include <iostream>
#include <sstream> 

using std::cout;
using std::endl;
using std::ostringstream;



class JustAnInt {
    
public:
    
    int x;
    
    bool operator<(const JustAnInt & other) const {
        return (x < other.x);
    }
};


int main() {
    
    int retval = 0;

	{

		BinarySearchTree<int> testTree;
		testTree.insert(5);
		testTree.insert(1);
		testTree.insert(4);
		testTree.insert(6);
		testTree.insert(8);
		testTree.insert(7);
		testTree.insert(3);

		ostringstream test1;
		testTree.write(test1);
		cout << test1.str() << "\n";

		testTree.insert(4);
		testTree.insert(1);

		ostringstream test2;
		testTree.write(test2);
		cout << test2.str() << "\n";
	}




	{
		BinarySearchTree<int> tree;

		tree.insert(5);

		{
			ostringstream s;
			tree.write(s);

			if (s.str() == " 5 ") {
				cout << "1) Pass: adding 5 to the tree yields the tree containing 5 and only 5\n";
			}
			else {
				cout << "1) Fail: adding 5 to the tree should yield the tree containing 5 and only 5 but it gives " << s.str() << "\n";
				++retval;
			}
		}

		tree.insert(1);
		tree.insert(2);
		tree.insert(6);

		{
			ostringstream s1;
			tree.write(s1);

			if (s1.str() == " 1  2  5  6 ") {
				cout << "2) Pass: adding 1, 2 and 6 to the tree containing 5 yields the tree  \" 1  2  5  6 \"\n";
			}
			else {
				cout << "2) Fail: adding 1, 2 and 6 to the tree containing 5 should yield the tree  \" 1  2  5  6 \" but it gives \"" << s1.str() << "\"\n";
				++retval;
			}
		}


		{
			auto six = tree.find(6);

			if (six && six->data == 6) {
				cout << "3) Pass: found 6 in the tree \" 1  2  5  6 \"\n";
			}
			else {
				cout << "3) Fail: 6 is in the tree \" 1  2  5  6 \" but find hasn't found a note containing 6\n";
				++retval;
			}
		}

		{
			auto three = tree.find(3);

			if (!three) {
				cout << "4) Pass: didn't find 3 in the tree \" 1  2  5  6 \", which doesn't contain it" << "\n";
			}
			else {
				cout << "4) Fail: looked for 3 in the tree \" 1  2  5  6 \", which doesn't contain 3 and got non-null node containing \"" << three->data << "\n";
				++retval;
			}

		}

		BinarySearchTree<int> parentTest = tree;

				
			tree.insert(3);
			tree.insert(4);
			tree.insert(8);
			tree.insert(7);
			tree.insert(9);
			tree.insert(10);

			BinarySearchTree<int> treeb = tree;

		{

			ostringstream s2;
			treeb.write(s2);


			if (s2.str() == " 1  2  3  4  5  6  7  8  9  10 ") {
			//if (!three) {
				cout << "5) Pass: " << s2.str() << "\n";
			}
			else {
				cout << "5) Fail: " << s2.str() << "\n";
				++retval;
			}

		}

		BinarySearchTree<int> treec;

		treec.insert(8);
		treec.insert(4);
		/*treec.insert(10);
		treec.insert(1);
		treec.insert(2);
		treec.insert(3);
		treec.insert(5);
		treec.insert(6);
		treec.insert(7);
		treec.insert(9);
		treec.insert(11);
		treec.insert(12);
		treec.insert(13);
		treec.insert(14);
		treec.insert(15);
		treec.insert(16);
		treec.insert(17);
		treec.insert(18);
		treec.insert(19);
		treec.insert(20);
		treec.insert(21);*/

		ostringstream s31;
		treec.write(s31);
		cout << "         "  << s31.str() << "\n";

		treec = treeb;

		{

			ostringstream s3;
			treec.write(s3);

			if (s3.str() == " 1  2  3  4  5  6  7  8  9  10 ") {
				cout << "6) Pass: " << s3.str() << "\n";
			}
			else {
				cout << "6) Fail: " << s3.str() << "\n";
				++retval;
			}

		}

		
		BinarySearchTree<int> treed;

		{

			ostringstream s4;
			treed.write(s4);
			cout << s4.str() << "\n";


			BinarySearchTree<int> treee;	// = treed;
			treee = treed;

			treee.insert(8);
			treee.insert(15);
			treee.insert(3);

			ostringstream s5;
			treee.write(s5);
			ostringstream stest;
			treed.write(stest);

			cout << stest.str() << "\n";	// treee should NOT change treed
			cout << s5.str() << "\n";

			

		}

		ostringstream s61;
		tree.write(s61);

		cout << "         " << s61.str() << "\n";

		tree.insert(8);
		tree.insert(5);
		tree.insert(9);
		tree.insert(2);

		{

			ostringstream s6;
			tree.write(s6);


			if (s6.str() == " 1  2  3  4  5  6  7  8  9  10 ") {
				cout << "7) Pass: " << s6.str() << "\n";
			}
			else {
				cout << "7) Fail: " << s6.str() << "\n";
				++retval;
			}

		}

		cout << endl;
	}
	
    
    {
        
        // compiler errors here mean you tried to do something other than 'operator<' when comparing data in the tree
        BinarySearchTree<JustAnInt> tree;
        tree.insert(JustAnInt{42});
    }
    
    return retval;
    
}
