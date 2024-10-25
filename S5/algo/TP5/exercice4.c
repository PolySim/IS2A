#define SIZE 128
#include <stdio.h>

int length_word (char word[]) {
    int i = 0;
    while (word[i] != '\0') {
        i++;
    }
    return i;
}

int isWordEqual(char word[], char str[], int i_start) {
    int i = 0, isEqual = 1, word_length = length_word(word);
    while (isEqual && i < word_length && word[i] != '\0' && str[i + i_start] != '\0') {
        if (word[i] != str[i + i_start]) {
            isEqual = 0;
        }
        i++;
    }
    return isEqual;
}

int findOccurences(char str[], char word[], int i_start) {
    while (str[i_start] != '\0' && !isWordEqual(word, str, i_start)) {
        i_start++;
    }
    if (str[i_start] == '\0') {
        return -1;
    } else {
        return i_start;
    }
}

int countOccurences(char str[], char word[]) {
    int i = 0, count = 0, finish = 0, occurenes;
    while (str[i] != '\0' && !finish) {
        occurenes = findOccurences(str, word, i);
        if (occurenes == -1) {
            finish = 1;
        } else {
            count++;
            i += length_word(word) + occurenes;
        }
    }
    return count;
}

int main(void) {
    char word[SIZE] = "aabb";
    char str[SIZE] = "aabbabaaabbab";
    printf("%d", countOccurences(str, word));
    return 0;
}