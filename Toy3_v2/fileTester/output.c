#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void scoping(int n, int m, char* message);
int glob();

char* message;

int n, m, k;


int main(void){
k = 6 ;
while (k >= 1) {
printf("%s", "Inserisci n: ");
scanf("%d", n);
printf("%s", "Inserisci m: ");
scanf("%d", m);
printf("%s%d%s%d", "I valori inseriti sono ", , " e ",  , '
);
scoping(n, m, message);
k = k - 1 ;
}
printf("%s",  , '
);
printf("%d",  , '
);

return 0;
}

void scoping(int n, int m, char* message) {
message = "level 1" ;
if (n <= 1) {
char* message;
if (m <= 1) {
char* message;
printf("%s",  , '
);
}
else {
if (m > 1 and m < 5) {
char* message;
printf("%s",  , '
);
}
else {
char* message;
printf("%s",  , '
);
}
}
printf("%s",  , '
);
}
else {
char* message;
if (m <= 1) {
char* message;
printf("%s",  , '
);
}
else {
if (m > 1 and m < 5) {
char* message;
printf("%s",  , '
);
}
else {
char* message;
printf("%s",  , '
);
}
}
printf("%s",  , '
);
}
printf("%s",  , '
);
}

int glob() {
return 100;
}

