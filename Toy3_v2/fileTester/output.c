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
    temp = reallocate_string(temp, len1 + len2 + 1);
    strcpy(temp, s1);
    strcat(temp, s2);
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



int main(void){
char* temp = allocate_string(1);
char *mamma = allocate_string(256);
char *x = allocate_string(256);
char *y = allocate_string(256);
char *z = allocate_string(256);
safe_strcpy(&x, "ciao" );
safe_strcpy(&y, "mamma" );
safe_strcpy(&z, "ciao" );
if ((strcmp(safe_strcat(x, safe_strcat(y, z)), safe_strcat("ciao", safe_strcat("mamma", "ciao"))) == 0)) {
printf("%s%c", "sono uguali" , '\n');
}
else {
printf("%s%c", "sono diversi" , '\n');
}
safe_strcpy(&x, safe_strcat(y, z) );
printf("%s%c", x , '\n');
free(mamma);
free(x);
free(y);
free(z);
free(temp);

return 0;
}

