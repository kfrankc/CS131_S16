// gcc -fno-stack-protector stacksmash.c

/* adapted from "Smashing the Stack for Fun and Profit"
   http://www.insecure.org/stf/smashstack.txt
*/

#include <stdio.h>

/* when this function is called a new stack frame is pushed onto the stack,
   with space for buffer, i, and xAddr */
void function() {
    char buffer[8];
    int i;
    int* xAddr;

    i = 0;
    printf("i was %d\n", i);

    buffer[-4] = 5;
    printf("i is now %d\n", i);
    
    xAddr = (int*) &buffer + 8;
    *xAddr += 16; // this doesn't work

}

int main() {
    int x = 34;
    function();
    printf("x is %d\n", x);
    return 0;
}
