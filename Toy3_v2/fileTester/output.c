#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

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

char *p;

char *r;

char *f;



int main(void){
char *p = allocate_string(256);
char *r = allocate_string(256);
strcpy(r, "ciao");
char *f = allocate_string(256);
strcpy(f, "mondo");
char *x = allocate_string(256);
strcpy(x, "ciao");
char *y = allocate_string(256);
strcpy(y, "mondo");
char *z = allocate_string(256);
strcpy(z, "mio");
char *a = allocate_string(256);
strcpy(a, "bella");
char *b = allocate_string(256);
char *c = allocate_string(256);
strcpy(c, "stronza");
char *d = allocate_string(256);

return 0;
}

