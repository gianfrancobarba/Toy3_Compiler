#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>



 // DICHIARAZIONI FUNZIONI E VARIABILI DI SERVIZIO

#define INITIAL_SIZE 32
#define INCREMENT_SIZE 32
#define MAX_CONCAT_SIZE 1024

char* tempString;
char* arrayConcat[MAX_CONCAT_SIZE];
int currentIndex = 0;

// Funzione per allocare dinamicamente una stringa
void allocate_string(char** str, size_t size) {
    *str = (char *) malloc(size);
    if (*str == NULL) {
        printf("Errore di allocazione!\n");
        exit(1);
    }
    (*str)[0] = '\0'; // Inizializza la stringa vuota
}

// Funzione per riallocare dinamicamente una stringa
char* reallocate_string(char* str, size_t new_size) {
    char* temp = (char*) realloc(str, new_size);
    if (temp == NULL) {
        printf("Errore di riallocazione!\n");
        free(str);
        exit(1);
    }
    return temp;
}

// Funzione per copiare una stringa in modo sicuro con gestione dinamica della memoria
void safe_strcpy(char** dest, const char* src) {
    if (!src) return;
    size_t src_len = strlen(src) + 1;
    *dest = (char*) realloc(*dest, src_len);
    if (*dest == NULL) {
        printf("Errore di allocazione in safe_strcpy!\n");
        exit(1);
    }
    strcpy(*dest, src);
}

// Funzione per concatenare due stringhe in modo sicuro con gestione dinamica della memoria
char* safe_strcat(const char* s1, const char* s2) {
    if (!s1 && !s2) return NULL;
    if (!s1) return strdup(s2);
    if (!s2) return strdup(s1);
    size_t len1 = strlen(s1);
    size_t len2 = strlen(s2);
    char* new_temp;
    allocate_string(&new_temp, len1 + len2 + 1);
    strcpy(new_temp, s1);
    strcat(new_temp, s2);
    safe_strcpy(&tempString, new_temp);
    free(new_temp);
    return tempString;
}

// Funzione per leggere una stringa dinamica evitando problemi di buffer
void safe_scanf(char** dest) {
    size_t size = INITIAL_SIZE;
    if (*dest == NULL) {
        allocate_string(dest, size);
    } else {
        *dest = (char*) realloc(*dest, size);
    }
    if (*dest == NULL) {
        printf("Errore di allocazione!\n");
        exit(1);
    }
    size_t len = 0;
    int c;
    while ((c = getchar()) == '\n'){}
    if (c != EOF) {
        (*dest)[len++] = (char)c;
    }
    while ((c = fgetc(stdin)) != '\n' && c != EOF) {
        if (len + 1 >= size) {
            size += INCREMENT_SIZE;
            *dest = (char*) realloc(*dest, size);
        }
        (*dest)[len++] = (char)c;
    }
    (*dest)[len] = '\0'; // Termina correttamente la stringa
}

// Funzione per convertire diversi tipi in stringa
const char* to_string(void* value, const char* type) {
    static char buffer[INITIAL_SIZE]; // Buffer statico condiviso
    if (strcmp(type, "int") == 0) {
        snprintf(buffer, INITIAL_SIZE, "%d", *(int*)value);
    } else if (strcmp(type, "double") == 0) {
        snprintf(buffer, INITIAL_SIZE, "%.6f", *(double*)value);
    } else if (strcmp(type, "bool") == 0) {
        return *(int*)value ? "true" : "false";
    } else if (strcmp(type, "char") == 0) {
        buffer[0] = *(char*)value;
        buffer[1] = '\0';
    } else if (strcmp(type, "string") == 0) {
        return (char*)value;
    } else {
        return "UNKNOWN";
    }
    return buffer;
}




// INIZIO DEL CODICE GENERATO

int c  = 1;

double a, b, x;

char *taglia;
char *ans1;
char *ans;

double risultato;


void sommac(int a, int d, double b, char* size, double* result);
char* stampa(char* messaggio);

int main(void){
allocate_string(&tempString, 1);
allocate_string(&taglia, 256);
allocate_string(&ans1, 256);
allocate_string(&ans, 256);
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    allocate_string(&arrayConcat[i], 256);
}

a = 1;
b = 2.2;
x = 3;
risultato = 0.0;
safe_strcpy(&ans, "no" );
sommac(a, x, b, taglia, &risultato);
stampa(strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("la somma di ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(a},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" e ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(b},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" incrementata di ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(c},"int"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" è ", taglia)))))))))))))));
stampa(strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("ed è pari a ", risultato}, "double"))));
printf("%s", "vuoi continuare? (si/no) - inserisci due volte la risposta\n");
safe_scanf(&ans);
safe_scanf(&ans1);
while ((strcmp(ans, "si") == 0)) {
printf("%s", "inserisci un intero: ");
scanf("%lf", &a);
printf("%s", "inserisci un reale: ");
scanf("%lf", &b);
sommac(a, x, b, taglia, &risultato);
stampa(strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("la somma di ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(a},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" e ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(b},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" incrementata di ", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(c},"int"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(" è ", taglia)))))))))))))));
stampa(strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("ed è pari a ", risultato}, "double"))));
printf("%s", "vuoi continuare? (si/no): ");
safe_scanf(&ans);
}
printf("%s%c", "" , '\n');
printf("%s%c", "ciao" , '\n');
free(taglia);
free(ans1);
free(ans);



free(tempString);
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    free(arrayConcat[i]);
}

return 0;
}

void sommac(int a, int d, double b, char* size, double* result) {
*result = (a + (b + (c + d)));
if ((*result > 100)) {
safe_strcpy(&size, "grande" );
}
else {
if ((*result > 50)) {
safe_strcpy(&size, "media" );
}
else {
safe_strcpy(&size, "piccola" );
}
}
}

char* stampa(char* messaggio) {
int i  = 0;
while ((i < 4)) {
printf("%s%c", "" , '\n');
i = (i + 1);
}
printf("%s%c", messaggio , '\n');
return "ok";
}

