#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

int globVar = 0;

int main()
{
  int locVar = 0;
  int* myTab;

  myTab = (int*) malloc(10 * sizeof(int));

  printf("my PID = %d\n", getpid());
  printf("Adrresse globVar = %p\n", &globVar);
  printf("Adrresse locVar = %p\n", &locVar);
  printf("Adrresse myTab = %p\n", myTab);
  printf("Addresse malloc = %p\n", malloc);
  printf("Addresse main = %p\n", main);

  scanf("%d", &globVar);

  return 0;
}