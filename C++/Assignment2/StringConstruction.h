#ifndef STRINGCONSTRUCTION_H
#define STRINGCONSTRUCTION_H

#include <string>
using std::string;

// TODO: your code goes here:

#include <vector>
using std::vector;

#include <iostream>
using std::cout;


//private:

void kmpTable(string w, int t[]) {
	int p = 0;
	t[0] = -1;
	for (int i = 1; i < w.size(); ++i) {
		p = t[i - 1];

		while(p >= 0) {
			if (w[p] == w[i - 1]) {
				break; // exit
			}
			else {
				p = t[p];
			}
		}
		t[i] = p + 1;
	}
}

bool kmp(string word, string target) {
	vector<string> matches;
	int j = 0;
	int k = 0;
	int T[word.size()];
	kmpTable(word, T);
	bool found = false;
	while (j < target.size()) {
		
		if (word[k] == target[j]) {
			j += 1;
			k += 1;
			if (k == word.size()) {
				matches.push_back();
				found = true;
				return found;
			}
		}
		else if (k == -1) {
			j += 1;
			k = 0;
		}
		else {
			k = T[k];
		}
	}
	return found;
}

//public:

int stringConstruction(string targetString, int appendCost, int cloneCost) {
    string newString = "";
	int totalCost = 0;

	newString.append(1, targetString[0]);
	totalCost += appendCost;
    int c = 0;
    bool targetMatched = false;
    while(!targetMatched) {
		// KMP
			
			string tempSub = "";
				tempSub = targetString.substr(newString.size(), ((targetString.size() - newString.size()) - c));
			
			if (kmp(tempSub, newString) == true) {	//reverse, checks (incre) if there is a substr in target that matches in newString
				if ((tempSub.size() * appendCost) >= cloneCost) {
					string tempStr = tempSub;
					cout << "tempStr (ini): " << tempStr << "\n";
					for (int i = 0; i < ((targetString.size() - tempSub.size()) - 1); ++i) {
						tempStr.append(1, targetString[(tempSub.size() + 1) + i]);
						cout << "tempStr: " << tempStr << "\n";
						if (kmp(tempStr, newString) == true) {
							cout << "yes: " << tempStr << "\n";
						}
						else {
							
						}
					}
					
						newString.append(tempSub);
						totalCost += cloneCost;
						c = 0;
						cout << "Clone-		" << tempSub << " :: for newString- " << newString << "\n";
						
						if (newString == targetString) {
							targetMatched = true;
							break;
						}
					}
					
				
				else {
					newString.append(tempSub);
					totalCost += ((tempSub.size()) * appendCost);
					c = 0;
					cout << "Append-		" << tempSub << " :: for newString- " << newString << "\n";
					
					if (newString == targetString) {
						targetMatched = true;
						break;
					}
				}

			}
			else {
				c += 1;
				if (tempSub.size() == 1) {
					newString.append(tempSub);
					totalCost += appendCost;
					c = 0;
					cout << "Append (F)-	" << tempSub << " :: for newString- " << newString << "\n";
				}
			}
if (newString == targetString) {
            targetMatched = true;
        }

		}


        
    
    
    return totalCost;
}

// do not write or edit anything below this line

#endif
