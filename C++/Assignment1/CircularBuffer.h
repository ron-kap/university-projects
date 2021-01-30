#ifndef CIRCULAR_BUFFER_H
#define CIRCULAR_BUFFER_H

// NB: Do not add any extra #include statements to this file
#include "SimpleVector.h"
#include <iostream>

/** TODO: complete the definition of the class CircularBuffer here
 *
 * Its constructor should take the size of the buffer as an argument
 *
 * It needs to have the functions:
 * - count() which returns how many things are in the buffer
 * - full() which returns true iff the buffer is full
 * - add() which takes an element and adds it to the buffer (you can assume the buffer is not full)
 * - remove() which removes the next element from the buffer (you can assume the buffer is not empty)
 */
    class CircularBuffer {
    protected:
        SimpleVector<char> buffer;
        // stores the position to add to next.
        int addNext = 0;
        // stores the position to remove from next.
        int removeNext = 0;

    public:

        CircularBuffer(int bufferSize) : buffer(bufferSize) {
            // fails tests 0&1 without setting below.
            for(int i = 0 ; i < bufferSize ; ++i) {
                buffer[i] = 0;
            }
        }

        int count() {
            int counter = 0;
            for (int i = 0; i < buffer.size(); ++i) {
                if (!(buffer[i] == 0)) {
                    counter = counter + 1;
                }
            }
            return counter;
        }

        bool full() {
            int counter = 0;
            for (int i = 0; i < buffer.size(); ++i) {
                if (!(buffer[i] == 0)) {
                    counter = counter + 1;
                }
            }

            if ((buffer.size()) == counter) {
                return true;
            } else {
                return false;
            }
        }

        void add(char charToAdd) {
            buffer[addNext] = charToAdd;
            addNext = (addNext + 1) % buffer.size();
        }

        char remove() {
            char temp = buffer[removeNext];
            buffer[removeNext] = 0;
            removeNext = (removeNext + 1) % buffer.size();

            return temp;
        }

    };


// don't write any code below this line

#endif
