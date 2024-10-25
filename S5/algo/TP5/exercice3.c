#define SIZE 128
#include <stdio.h>

int isEqual(char str1[], char str2[], int size) {
    int i = 0, isEqual = 1;
    while (isEqual && i < size && str1[i] != '\0' && str2[i] != '\0') {
        if (str1[i] != str2[i]) {
            isEqual = 0;
        }
        i++;
    }
    return isEqual;
}

void initVector(char v[], int size) {
    int i = 0;
    for (i = 0; i < 26; i++) {
        v[2 * i] = '0';
        v[2 * i + 1] = i + 'a';
    }
}

int findIndex(char c){
    return c - '0' - ('a' - '0');
}

void countLetters(char str[], char v[], int size) {
    int i = 0;
    while (i < size && str[i] != '\0') {
        v[2 * findIndex(str[i])]++;
        i++;
    }
}

int isAnagram(char word1[], char word2[], int size) {
    char v1[SIZE];
    char v2[SIZE];
    initVector(v1, SIZE);
    initVector(v2, SIZE);
    countLetters(word1, v1, SIZE);
    countLetters(word2, v2, SIZE);
    return isEqual(v1, v2, SIZE);
}

int main(void) {
    char word1[SIZE] = "pains";
    char word2[SIZE] = "sapin";
    char word3[SIZE] = "pain";
    printf("%d\n", isAnagram(word1, word2, SIZE));
    printf("%d\n", isAnagram(word1, word3, SIZE));
    return 0;
}