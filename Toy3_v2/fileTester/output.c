#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

char* message;

int n, m, k;


void scoping(int n, int m, char* message) {
message = "level 1" ;
if ((n <= 1)) {
char* message;
if ((m <= 1)) {
char* message;
printf("%s%c", message , '\n');
}
else {
if (((m > 1) && (m < 5))) {
char* message;
printf("%s%c", message , '\n');
}
else {
char* message;
printf("%s%c", message , '\n');
}
}
printf("%s%c", message , '\n');
}
else {
char* message;
if ((m <= 1)) {
char* message;
printf("%s%c", message , '\n');
}
else {
if (((m > 1) && (m < 5))) {
char* message;
printf("%s%c", message , '\n');
}
else {
char* message;
printf("%s%c", message , '\n');
}
}
printf("%s%c", message , '\n');
}
printf("%s%c", message , '\n');
}

int glob() {
return 100;
}


int main(void){
k = 6 ;
while ((k >= 1)) {
printf("%s", "Inserisci n: ");
scanf("%d", &n);
printf("%s", "Inserisci m: ");
scanf("%d", &m);
printf("%s%d%s%d%c", "I valori inseriti sono ", n, " e ", m , '\n');
scoping(n, m, message);
k = (k - 1) ;
}
printf("%s%c", message , '\n');
printf("%d%c", glo);
 , '\n');

return 0;
}

