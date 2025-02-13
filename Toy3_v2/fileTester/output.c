#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int fun(int a, int* b, char* x);


int main(void){
int x;

return 0;
}

int fun(int a, int* b, char* x) {
if (a > *b) {
x = "a" ;
return a;
}
else {
x = "b" ;
return *b;
}
return *b;
}

