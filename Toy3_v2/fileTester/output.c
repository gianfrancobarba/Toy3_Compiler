#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void moltiplicazione(double x, double y, double* res, bool* grande);
char* saluto();

double sommagrande, sommapiccola;

int i;

double x, y, risultato;

bool grande, nonusata;


int main(void){
sommagrande = 0, sommapiccola = 0 ;
printf("%s", "Questo programma permette di svolgere una serie di moltiplicazioni" );
printf("%s", "sommando i risultati < 100 in sommagrande e quelli < 100 in sommapiccola" );
i = -1 ;
while (i <= 0) {
char* saluto;
printf("%s", "Quante moltiplicazioni vuoi svolgere? (inserire intero > 0)" );
scanf("%d", &i );
printf("%s", saluto );
}
while (i > 0) {
x = -1 ;
y = -1 ;
while (notx > 0 and y > 0) {
char* saluto;
printf("%s%d%s", "Moltiplicazione ", i, ": inserisci due numeri positivi" );
scanf("%lf%lf", &x, &y );
printf("%s", saluto );
}
moltiplicazione(x, y, risultato, grande );
printf("%lf", risultato );
if (grande) {
printf("%s", "il risultato è grande" );
sommagrande = sommagrande + risultato ;
}
else {
printf("%s", "il risultato è piccolo" );
sommapiccola = sommapiccola + risultato ;
}
i = i - 1 ;
}
printf("%s%lf", "
 sommagrande è ", sommagrande );
printf("%s%lf", "sommapiccola è ", sommapiccola );

return 0;
}

void moltiplicazione(double x, double y, double* res, bool* grande) {
double risultato = x * y, nonusata;
if (x * y >= 100) {
grande = true ;
}
else {
grande = false ;
}
res = risultato ;
}

char* saluto() {
return "ciao";
}

