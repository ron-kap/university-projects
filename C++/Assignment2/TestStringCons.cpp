#include "StringConstruction.h"

#include <iostream>
using std::cout;
using std::endl;
using std::ostream;

#include <vector>
using std::vector;


struct Testcase {
    string target;
    int appendCost;
    int cloneCost;
    int bestCost;
    
    string notes;
};

ostream & operator<<(ostream & o, const Testcase & t) {
    o << "Target string: " << t.target << "\n";
    o << "Cost of appending an arbitrary character: " << t.appendCost << "\n";
    o << "Cost of cloning a substring, and appending: " << t.cloneCost << "\n";
    o << "Cheapest way to make the string should be: " << t.bestCost << "\n";
    
    return o;
}


int main() {
	
    int retval = 0;
    
    vector<Testcase> testcases{  
        {"xzxpzxzxpq", 10, 11, 71, "    Append 'x' (cost 10), Append 'z' (cost 10), Append 'x' (cost 10), Append 'p' (cost 10), Append 'z' (cost 10),\n    Clone 'xzxp' (cost 11), Append 'q' (cost 10)"}
        
    };
    
    
    for (size_t i = 0; i < testcases.size(); ++i) {        
        
        cout << "Testcase " << i << "\n";
        cout << testcases[i];
        cout << "\nRunning your function\n";
        
        int costWas = stringConstruction(testcases[i].target, testcases[i].appendCost, testcases[i].cloneCost);
        
        if (costWas == testcases[i].bestCost) {
            cout << " -- Test passed, correct cost was returned\n\n\n";
        } else {
            cout << " -- Test failed, incorrect cost of " << costWas << " was returned\n";
            cout << "    To help your debugging, the cheapest way to make the string should be:\n";
            cout << testcases[i].notes << "\n\n\n";
            
            ++retval;
        }
                
    }
    return retval;
	
}


