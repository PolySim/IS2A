#include <stdio.h>
#include <stdlib.h>

// Question 1

int somme(int argc, char *argv[]) {
  int i = 0, sum = atoi(argv[1]);
  for (i = 2; i < argc; i++) {
    sum += atoi(argv[i]);
  }
  return sum;
}

// Question 2

int compareStrings(char *str1, char *str2) {
  int i = 0;
  while (str1[i] != '\0' && str2[i] != '\0') {
    if (str1[i] != str2[i]) {
      return 0;
    }
    i++;
  }
  return 1;
}

int calcul(int argc, char *argv[]) {
  char *operation = argv[1];
  int result = atoi(argv[2]);
  for (int i = 3; i < argc; i++) {
    if (compareStrings(operation, "somme")) {
      result += atoi(argv[i]);
    } else if (compareStrings(operation, "sous")) {
      result -= atoi(argv[i]);
    } else if (compareStrings(operation, "mult")) {
      result *= atoi(argv[i]);
    } else if (compareStrings(operation, "div")) {
      result /= atoi(argv[i]);
    }
  }
  return result;
}

int main(int argc, char *argv[]) {
//  printf("Somme des args : %d\n", somme(argc, argv));
//  printf("Résultat : %d\n", calcul(argc, argv));
  return 0;
}