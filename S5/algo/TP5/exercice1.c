#define SIZE 128
#include <stdio.h>

int isLetter(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

void addLetters(char str[], char c, int k, int i_start) {
    int i;
    for (i = i_start; i < i_start + k; i++) {
        str[i] = c;
    }
}

void decode(char str_encode[], char str_decode[], int size) {
    int isFirst = 1, i = 0, k = 1, i_decode= 0;
    while (i < size - 1 && str_encode[i] != '\0') {
        if (isFirst) {
            addLetters(str_decode, str_encode[i], k, i_decode);
            i_decode += k;
            isFirst = 0;
            k = 1;
        } else {
            if (isLetter(str_encode[i]) || str_encode[i + 1] == '\0') {
                str_decode[i_decode] = str_encode[i];
                i_decode++;
            } else {
                isFirst = 1;
                k = (str_encode[i] - '0') + 1;
            }
        }
        i++;
    }
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

int main(void)
{
    char str_encode[SIZE] = "a3b4d03324";
    char str_decoded[SIZE] = "abbbbddddd322224";
    char str_decode[SIZE];
    decode(str_encode, str_decode, SIZE);
    printf("%d", isEqual(str_decode, str_decoded, SIZE));
    return 0;
}