#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void sommac(double a, double d, double b, char* size, double* result);
char* stampa(char* messaggio);

int c;
double a, b, x;
char* taglia, ans1, ans;
double risultato;

int main(void){
    a = 1 ;
    b = 2.2 ;
    x = 3 ;
    risultato = 0.0 ;
    ans = "no" ;
    sommac(a, x, b, taglia, risultato );
    stampa("La somma di " + a + " e " + b + " incrementata di " + c + " è " + taglia );
    stampa("Ed è pari a " + risultato );
    printf("%s", "Vuoi continuare? (si/no) - inserisci due volte la risposta
    " );
    scanf("%s", &ans );
    scanf("%s", &ans1 );
    while (ans == "si") {
        printf("%s", "Inserisci un intero: " );
        scanf("%lf", a );
        printf("%s", "Inserisci un reale: " );
        scanf("%lf", b );
        sommac(a, x, b, taglia, risultato );
        stampa("La somma di " + a + " e " + b + " incrementata di " + c + " è " + taglia );
        stampa("Ed è pari a " + risultato );
        printf("%s", "Vuoi continuare? (si/no): " );
        scanf("%s", &ans );
    }
    printf("%s", "" );
    printf("%s", "Ciao" );

    return 0;
}

void sommac(double a, double d, double b, char* size, double* result) {
result = a + b + c + d ;
if (result > 100) {
size = "grande" ;
}
else {
if (result > 50) {
size = "media" ;
}
else {
size = "piccola" ;
}
}
}

char* stampa(char* messaggio) {
int i;
while (i < 4) {
printf("%s", "" );
i = i + 1 ;
}
printf("%s",  );
return "ok";
}

