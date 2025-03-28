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

char *operazione;

double input1, input2, answer, result, res1, res2, res3, res4;

bool flag;


void somma(double input1, double input2, double* result);
void sottrazione(double input1, double input2, double* result);
double moltiplicazione(double input1, double input2);
double divisione(double input1, double input2);
void tutteleoperazioni(double input1, double input2, double* res1, double* res2, double* res3, double* res4);
char* stampa(char* stringa);

int main(void){
allocate_string(&tempString, 1);
allocate_string(&operazione, 256);
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    allocate_string(&arrayConcat[i], 256);
}

flag = true;
while (flag) {
printf("%s", "Inserisci l'operazione da effettuare (somma, sottrazione, divisione, moltiplicazione, tutteleoperazioni): ");
safe_scanf(&operazione);
printf("%s", "Inserisci il primo input: ");
scanf("%lf", &input1);
printf("%s", "Inserisci il secondo input: ");
scanf("%lf", &input2);
printf("%s%s%s%lf%s%lf%c", "hai scelto l'operazione ", operazione, " con gli argomenti ", input1, " e ", input2 , '\n');
if ((strcmp(operazione, "somma") == 0)) {
somma(input1, input2, &result);
}
else {
if ((strcmp(operazione, "sottrazione") == 0)) {
sottrazione(input1, input2, &result);
}
else {
if ((strcmp(operazione, "divisione") == 0)) {
result = divisione(input1, input2);
}
else {
if ((strcmp(operazione, "moltiplicazione") == 0)) {
result = moltiplicazione(input1, input2);
}
else {
if ((strcmp(operazione, "tutteleoperazioni") == 0)) {
tutteleoperazioni(input1, input2, &res1, &res2, &res3, &res4);
}
else {
printf("%s%c", "Operazione non consentita" , '\n');
}
}
}
}
}
if ((strcmp(operazione, "tutteleoperazioni") != 0)) {
printf("%s%lf%c", "Il risultato è: ", result , '\n');
}
else {
printf("%s%c", stampa(strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("i risultati delle 4 operazioni sono \n", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(to_string((double[]){res1},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("\n", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(to_string((double[]){res2},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("\n", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(to_string((double[]){res3},"double"), strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("\n", strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat(to_string((double[]){res4},"double"), "\n"))))))))))))))))) , '\n');
}
printf("%s", "Vuoi continuare? (1 yes/0 no): ");
scanf("%lf", &answer);
if ((answer == 1)) {
flag = true;
}
else {
flag = false;
}
}
free(operazione);



free(tempString);
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    free(arrayConcat[i]);
}

return 0;
}

void somma(double input1, double input2, double* result) {
*result = (input1 + input2);
}

void sottrazione(double input1, double input2, double* result) {
*result = (input1 - input2);
}

double moltiplicazione(double input1, double input2) {
return (input1 * input2);
}

double divisione(double input1, double input2) {
if ((input2 == 0)) {
printf("%s%c", "Errore: divisione per zero" , '\n');
return 0.0;
}
return (input1 / input2);
}

void tutteleoperazioni(double input1, double input2, double* res1, double* res2, double* res3, double* res4) {
somma(input1, input2, &*res1);
sottrazione(input1, input2, &*res2);
*res3 = moltiplicazione(input1, input2);
*res4 = divisione(input1, input2);
}

char* stampa(char* stringa) {
return strcpy(arrayConcat[(currentIndex++)%MAX_CONCAT_SIZE], safe_strcat("Ciao! ", stringa));
}

