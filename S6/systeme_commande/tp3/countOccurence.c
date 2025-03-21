#include <stdio.h>

int main(int argc, char* argv[])
{
  int occurrences = 0;

  if (argc < 3) {
    printf("Il faut deux arguments\n");
    return 1;
  }

  FILE* f = fopen(argv[1], "r");
  char c;
  while ((c = fgetc(f)) != EOF) {
    if (c == argv[2][0]) {
      occurrences++;
    }
  }

  printf("Le caractère %c apparaît %d fois\n", argv[2][0], occurrences);

  return 0;
}