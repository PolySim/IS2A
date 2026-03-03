#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

int main() {
  printf("lancement de la commande ls\n");
  if (execlp("ls", "ls", "-l", NULL) == -1) {
    perror("echec execlp");
    exit(1);
  }
}
