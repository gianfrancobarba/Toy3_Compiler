#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define INITIAL_SIZE 32
#define INCREMENT_SIZE 32

char* temp;

// Funzione per allocare dinamicamente una stringa
char* allocate_string(size_t size) {
    char* str = (char*)malloc(size);
    if (str == NULL) {
        printf("Errore di allocazione!\n");
        exit(1);
    }
    str[0] = '\0';  // Inizializza la stringa vuota
    return str;
}

// Funzione per riallocare dinamicamente una stringa
char* reallocate_string(char* str, size_t new_size) {
    char* temp = (char*)realloc(str, new_size);
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
    *dest = (char*)realloc(*dest, src_len);
    if (*dest == NULL) {
        printf("Errore di allocazione in safe_strcpy!\n");
        exit(1);
    }
    strcpy(*dest, src);
}

// Funzione per concatenare due stringhe in modo sicuro con gestione dinamica della memoria
char* safe_strcat(const char* s1, const char* s2) {
    if (!s1 && !s2) return allocate_string(1);
    if (!s1) return strdup(s2);
    if (!s2) return strdup(s1);

    size_t len1 = strlen(s1);
    size_t len2 = strlen(s2);
    char* new_str = (char*)malloc(len1 + len2 + 1);
    if (!new_str) {
        printf("Errore di allocazione in safe_strcat!\n");
        exit(1);
    }
    strcpy(new_str, s1);
    strcat(new_str, s2);
    return new_str;
}

// Funzione per convertire diversi tipi in stringa in modo sicuro
char* to_string(void* value, const char* type) {
    char* temp = (char*)malloc(32);
    if (!temp) {
        printf("Errore di allocazione in to_string!\n");
        exit(1);
    }
    if (strcmp(type, "int") == 0) {
        sprintf(temp, "%d", *(int*)value);
    } else if (strcmp(type, "double") == 0) {
        sprintf(temp, "%lf", *(double*)value);
    } else if (strcmp(type, "bool") == 0) {
        strcpy(temp, *(int*)value ? "true" : "false");
    } else if (strcmp(type, "char") == 0) {
        temp[0] = *(char*)value;
        temp[1] = '\0';
    } else if (strcmp(type, "string") == 0) {
        free(temp);
        return strdup((char*)value);
    } else {
        strcpy(temp, "UNKNOWN");
    }
    return temp;
}

// Funzione per leggere una stringa dinamica evitando problemi di buffer
void safe_scanf(char** dest) {
    size_t size = INITIAL_SIZE;
    if (*dest == NULL) {
        *dest = (char*)malloc(size);
    } else {
        *dest = (char*)realloc(*dest, size);
    }
    if (*dest == NULL) {
        printf("Errore di allocazione!\n");
        exit(1);
    }
    size_t len = 0;
    int c;
    while ((c = getchar()) == '\n');
    if (c != EOF) {
        (*dest)[len++] = (char)c;
    }
    while ((c = fgetc(stdin)) != '\n' && c != EOF) {
        if (len + 1 >= size) {
            size += INCREMENT_SIZE;
            *dest = (char*)realloc(*dest, size);
        }
        (*dest)[len++] = (char)c;
    }
    (*dest)[len] = '\0'; // Termina correttamente la stringa
}

char *message;

int n, m, k;


void scoping(int n, int m, char* message);
int glob();

int main(void){
temp = allocate_string(1);
message = allocate_string(256);
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



free(temp);

return 0;
}

void scoping(int n, int m, char* message) {
safe_strcpy(&message, "level 1" );
if ((n <= 1)) {
char *message = allocate_string(256);
safe_strcpy(&message, "level 2.1");
int n  = 10;
if ((m <= 1)) {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.1");
printf("%s%c", message , '\n');
free(message);
}
else {
if (((m > 1) && (m < 5))) {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.2");
printf("%s%c", message , '\n');
free(message);
}
else {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.3");
printf("%s%c", message , '\n');
free(message);
}
}
printf("%s%c", message , '\n');
free(message);
}
else {
char *message = allocate_string(256);
safe_strcpy(&message, "level 2.2");
if ((m <= 1)) {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.4");
printf("%s%c", message , '\n');
free(message);
}
else {
if (((m > 1) && (m < 5))) {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.5");
printf("%s%c", message , '\n');
free(message);
}
else {
char *message = allocate_string(256);
safe_strcpy(&message, "level 3.6");
printf("%s%c", message , '\n');
free(message);
}
}
printf("%s%c", message , '\n');
free(message);
}
printf("%c", message , '\n');
}

int glob() {
return 100;
}

