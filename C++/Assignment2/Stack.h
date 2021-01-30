#ifndef STACK_H
#define STACK_H

#include <vector>
using std::vector;

// TODO: Your code goes here

#include <string>
using std::string;

#include <iostream>
using std::cout;
using std::stod;

#include <sstream>
using std::istringstream;

class Stack {
protected:
	vector<double> vectorStack;

public:
	Stack() {
		// empty constructor
	}

	bool empty() {
		if (vectorStack.size() == 0) {
			return true;
		}
		else {
			return false;
		}
	}

	double pop() {
		if (empty() == true) {	
			cout << "The Stack is empty and cannot 'pop'." << "\n";
			return 0;	
		}

		double tempPop = vectorStack[0];
		vectorStack.erase(vectorStack.begin());
		return tempPop;
	}

	void push(double numToPush) {
		vectorStack.insert(vectorStack.begin(), numToPush);
	}
};

double evaluate(string RPN) {
	double result = 0;
	istringstream ss(RPN);
	string token;
	Stack stack1;

	while (std::getline(ss, token, ' ')) {
		if (token == "+") {
			double num1 = stack1.pop();
			double num2 = stack1.pop();
			result = num2 + num1;
			stack1.push(result);
		}
		else if (token == "-") {
			double num1 = stack1.pop();
			double num2 = stack1.pop();
			result = num2 - num1;
			stack1.push(result);
		}
		else if (token == "*") {
			double num1 = stack1.pop();
			double num2 = stack1.pop();
			result = num2 * num1;
			stack1.push(result);
		}
		else if (token == "/") {
			double num1 = stack1.pop();
			double num2 = stack1.pop();
			result = num2 / num1;
			stack1.push(result);
		}
		else {
			// token is a number
			stack1.push(stod(token));
		}
	}
	return result;	
}

// Do not write anything below this line

#endif