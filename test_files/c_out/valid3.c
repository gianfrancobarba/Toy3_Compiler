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

char *message;

int n, m, k;


void scoping(int n, int m, char* message);
int glob();

int main(void){
allocate_string(&tempString, 1);
allocate_string(&message, 256);
safe_strcpy(&message, "level 0");
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    allocate_string(&arrayConcat[i], 256);
}

k = 6;
while ((k >= 1)) {
printf("%s", "Inserisci n: ");
scanf("%d", &n);
printf("%s", "Inserisci m: ");
scanf("%d", &m);
printf("%s%d%s%d%c", "I valori inseriti sono ", n, " e ", m , '\n');
scoping(n, m, message);
k = (k - 1);
}
printf("%s%c", message , '\n');
printf("%d%c", glob() , '\n');
free(message);



free(tempString);
for (int i = 0; i < MAX_CONCAT_SIZE; i++) {
    free(arrayConcat[i]);
}

return 0;
}

void scoping(int n, int m, char* message) {
safe_strcpy(&message, "level 1" );
if ((n <= 1)) {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 2.1");
if ((m <= 1)) {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.1");
printf("%s%c", message , '\n');
free(message);
}
else {
if (((m > 1) && (m < 5))) {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.2");
printf("%s%c", message , '\n');
free(message);
}
else {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.3");
printf("%s%c", message , '\n');
free(message);
}
}
printf("%s%c", message , '\n');
free(message);
}
else {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 2.2");
if ((m <= 1)) {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.4");
printf("%s%c", message , '\n');
free(message);
}
else {
if (((m > 1) && (m < 5))) {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.5");
printf("%s%c", message , '\n');
free(message);
}
else {
char *message;
allocate_string(&message, 256);
safe_strcpy(&message, "level 3.6");
printf("%s%c", message , '\n');
free(message);
}
}
printf("%s%c", message , '\n');
free(message);
}
printf("%s%c", message , '\n');
}

int glob() {
return 100;
}

