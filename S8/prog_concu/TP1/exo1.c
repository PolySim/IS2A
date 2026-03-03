#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
  int cr;
  if ((cr = fork()) < 0) {
    perror("erreur exécution de fork");
    exit(1);
  }
  for (;;) {
    if (cr == 0) {
      printf("c'est le fils qui ecrit \n");
      sleep(1);
    } else {
      printf("c'est le pere qui ecrit\n");
      sleep(2);
    }
  }
  return 0;
}
