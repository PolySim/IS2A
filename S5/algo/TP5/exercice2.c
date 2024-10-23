#define SIZE 128
#include <stdio.h>

int isLetter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

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

int countLetters(char str[], char c, int i_start) {
    int count = 1;
    while (str[i_start + 1] == c) {
        count++;
        i_start++;
    }
    return count;
}

void encode(char str[], char str_encode[], int size) {
    int i = 1, k = 0, i_encode = 1;
    str_encode[0] = str[0];
    while (i < size - 1 && str[i] != '\0') {
        k = countLetters(str, str[i], i) - 1;
        if (str[i + 1] == '\0') {
            str_encode[i_encode] = str[i];
        } else {
            if (isLetter(str[i])) {
                if (k == 0) {
                    str_encode[i_encode] = str[i];
                    i_encode++;
                } else {
                    str_encode[i_encode] = k + '0';
                    str_encode[i_encode + 1] = str[i];
                    i_encode += 2;
                }
            } else {
                str_encode[i_encode] = k + '0';
                str_encode[i_encode + 1] = str[i];
                i_encode += 2;
            }
        }
        i += k + 1;
    }
}

int main (void) {
    char str_encoded[SIZE] = "a3b4d03324";
    char str_encode[SIZE];
    char str[SIZE] = "abbbbddddd322224";
    encode(str, str_encode, SIZE);
    printf("%d", isEqual(str_encoded, str_encode, SIZE));
    return 0;
}