#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>


int fun(int x) {
return 0;
}

int g() {
return 0;
}


int main(void){
int x, y;
x = (fun(y) + 4) ;
fun(y);
g();
printf("%d", g());
printf("%d", fun(y));

return 0;
}

