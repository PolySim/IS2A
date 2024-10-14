#include <stdio.h>

int chiffre(char c) {
    return c >= '0' && c <= '9';
}

int minuscule(char c) {
    return c >= 'a' && c <= 'z';
}

int majuscule(char c) {
    return c >= 'A' && c <= 'Z';
}

void analysePhrase(void) {
    char c;
    c = getchar();
    while (c != '.') {
        if (chiffre(c)) {
            printf("%c est un chiffre\n", c);
        } else if (minuscule(c)) {
            printf("%c est une minuscule\n", c);
        } else if (majuscule(c)) {
            printf("%c est une majuscule\n", c);
        } else {
            printf("%c est un caractère inconnu\n", c);
        }
        c = getchar();
    }
}

int main(void) {
    analysePhrase();
    return 0;
}