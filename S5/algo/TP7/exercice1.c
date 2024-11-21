#include <stdio.h>
#define SIZE 64

// Question 1

int numberChar(char str[]) {
  int i = 0;
  while (str[i] != '\0') {
    i++;
  }
  return i;
}

void reverseString(char str[]) {
  int i = 0, n = numberChar(str);
  char tmp;
  while(i < n / 2) {
    tmp = str[i];
    str[i] = str[n - i - 1];
    str[n - i - 1] = tmp;
    i++;
  }
}

// Question 2

void createWord(char str[], char word[], int start, int end) {
  int i = start;
  for (i = start; i < end; i++) {
    word[i - start] = str[i];
  }
  word[i - start] = '\0';
}

void applyReverse(char str[], char word[], int start, int end) {
  int i = start;
  for (i = start; i < end; i++) {
    str[i] = word[i - start];
  }
}

void reverseEveryWord(char str[]) {
  int i = 0, index_space = 0;
  char word[SIZE];
  while (str[i] != '\0') {
    if (str[i] == ' ') {
      createWord(str, word, index_space, i);
      reverseString(word);
      applyReverse(str, word, index_space, i);
      index_space = i + 1;
    }
    i++;
  }
}

int main(void) {
  char str[SIZE] = "Hello World !";
//  char word[SIZE];

  printf("The string is : %s\n", str);
//  reverseString(str);
//  printf("The reversed string is : %s\n", str);

//  createWord(str, word, 0, 5);
//  printf("The first word is : %s\n", word);
//
//  createWord(str, word, 6, 11);
//  printf("The second word is : %s\n", word);
  reverseEveryWord(str);
  printf("The reversed string is : %s\n", str);
  return 0;
}