#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

int fun;

int fun_fun(int x);
int g();

int main(void){
int x, y;
x = (fun_fun(y) + 4) ;
fun_fun(y);
g();
printf("%d", g());
printf("%d", fun_fun(y));

return 0;
}

int fun_fun(int x) {
return 0;
}

int g() {
return 0;
}

