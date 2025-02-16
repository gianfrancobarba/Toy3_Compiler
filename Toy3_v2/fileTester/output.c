#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

#define INITIAL_SIZE 32
#define INCREMENT_SIZE 32

char* temp;

// Funzione per allocare dinamicamente una stringa
char* allocate_string(size_t size) {
    char* str = (char*)malloc(size * sizeof(char));
    if (str == NULL) {
        printf("Errore di allocazione!\n");
        exit(1);
    }
    str[0] = '\0';  // Inizializza la stringa vuota
    return str;
}

// Funzione per riallocare dinamicamente una stringa
char* reallocate_string(char* str, size_t new_size) {
    char* temp = (char*)realloc(str, new_size * sizeof(char));
    if (temp == NULL) {
        printf("Errore di riallocazione!\n");
        exit(1);
    }
    return temp;
}

// Funzione per copiare una stringa in modo sicuro con gestione dinamica della memoria
void safe_strcpy(char** dest, const char* src) {
    size_t src_len = strlen(src) + 1;
    *dest = reallocate_string(*dest, src_len);
    strcpy(*dest, src);
}

// Funzione per concatenare due stringhe in modo sicuro con gestione dinamica della memoria
char* safe_strcat(const char* s1, const char* s2) {
    if (!s1 && !s2) return allocate_string(1);
    if (!s1) return strdup(s2);
    if (!s2) return strdup(s1);

    size_t len1 = strlen(s1);
    size_t len2 = strlen(s2);
    char* new_temp = reallocate_string(temp, len1 + len2 + 1);
    if (!new_temp) {
        printf("Errore di allocazione in safe_strcat!\n");
        exit(1);
    }
    temp = new_temp;
    strcpy(temp, s1);
    strcat(temp, s2);
    return temp;
}

// Funzione per convertire diversi tipi in stringa usando temp
char* to_string(void* value, const char* type) {
    if (strcmp(type, "int") == 0) {
        temp = reallocate_string(temp, 32);
        sprintf(temp, "%d", *(int*)value);
    } else if (strcmp(type, "double") == 0) {
        temp = reallocate_string(temp, 32);
        sprintf(temp, "%lf", *(double*)value);
    } else if (strcmp(type, "bool") == 0) {
        temp = reallocate_string(temp, 6);
        strcpy(temp, *(int*)value ? "true" : "false");
    } else if (strcmp(type, "char") == 0) {
        temp = reallocate_string(temp, 2);
        temp[0] = *(char*)value;
        temp[1] = '\0';
    } else if (strcmp(type, "string") == 0) {
        return (char*)value;
    } else {
        temp = reallocate_string(temp, 16);
        strcpy(temp, "UNKNOWN");
    }
    return temp;
}

// Funzione per leggere una stringa dinamica evitando problemi di buffer
void safe_scanf(char** dest) {
    size_t size = INITIAL_SIZE;
    if (*dest == NULL) {
        *dest = (char*)malloc(size * sizeof(char));
    } else {
        *dest = reallocate_string(*dest, size);
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
            *dest = reallocate_string(*dest, size);
        }
        (*dest)[len++] = (char)c;
    }
    (*dest)[len] = '\0'; // Termina correttamente la stringa
}

char *mamma;

int c  = 1;

double a, b, x;

char *taglia;
char *ans1;
char *ans;

double risultato;


char* stampa(char* messaggio);

int main(void){
temp = allocate_string(1);
mamma = allocate_string(256);
taglia = allocate_string(256);
ans1 = allocate_string(256);
ans = allocate_string(256);
a = 1;
b = 2.2;
x = 3;
risultato = 0.0;
safe_strcpy(&ans, "no" );
stampa(safe_strcat("la somma di ", safe_strcat(to_string(&a, "double"), safe_strcat(" e ", safe_strcat(to_string(&b, "double"), safe_strcat(" incrementata di ", safe_strcat(to_string(&c, "int"), safe_strcat(" è ", taglia))))))));
free(mamma);
free(taglia);
free(ans1);
free(ans);



free(temp);

return 0;
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

