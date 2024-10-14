#include <stdio.h>
#include <stdbool.h>

bool isLetter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

bool isVowel(char c) {
    return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y' || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y');
}

void soundexCode(){
    char abbreviation[5];
    char c, lastC;
    int i = 1;
    printf("Entrez votre nom : ");
    lastC = getchar();
    if (!isLetter(lastC)) {
        printf("Votre nom ne commence pas par une lettre\n");
        return;
    }
    abbreviation[0] = lastC;
    c = getchar();
    while (isLetter(c)) {
        if ((!isVowel(c)) && c != lastC) {
            abbreviation[i] = c;
            i++;
        }
        if (i == 4) {
            printf("Votre code est : %s\n", abbreviation);
            return;
        }
        lastC = c;
        c = getchar();
    }
    printf("Votre code est : %s\n", abbreviation);
}


int main(void) {
    soundexCode();
    return 0;
}