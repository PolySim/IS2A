#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  int cr, ent = 0;
  if ((cr = fork()) < 0) {
    perror("erreur exécution de fork");
    exit(1);
  }
  if (cr == 0) {
    printf("dans le fils, valeur de ent avant incrémentation : %d\n", ent);
    ent = ent + 1;
    printf("dans le fils, valeur de ent après incrémentation : %d\n", ent);
  } else {
    int pid;
    printf("dans le pere, valeur de ent avant incrémentation : %d\n", ent);
    ent = ent + 2;
    printf("dans le pere, valeur de ent après incrémentation : %d\n", ent);
    if ((pid = wait(NULL)) < 0) {
      perror("erreur exécution de wait");
      exit(1);
    }
  }
  return 0;
}
