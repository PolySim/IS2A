#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
  int child_somme, child_produit;

  if (argc != 3) {
    fprintf(stderr, "Usage: %s n1 n2\n", argv[0]);
    return 1;
  }

  child_somme = fork();
  if (child_somme < 0) {
    perror("Creation child_somme");
    exit(1);
  }

  if (child_somme == 0) {
    execl("./somme", "somme", argv[1], argv[2], NULL);
    perror("./somme");
    exit(1);
  } else {
    child_produit = fork();
    if (child_produit < 0) {
      perror("Creation child_produit");
      exit(1);
    }

    if (child_produit == 0) {
      execl("./produit", "produit", argv[1], argv[2], NULL);
      perror("./produit");
      exit(1);
    } else {
      waitpid(child_somme, NULL, 0);
      printf("Somme effectué\n");

      waitpid(child_produit, NULL, 0);
      printf("Produit effectué\n");
    }
  }

  return 0;
}
