#include <stdio.h>

int main()
{
  int count = 0;

  while(1){
    if(count % 2 == 0){
      printf("%d", count);
      count++;
    }
    else count++;
  }

  return 0;
}
