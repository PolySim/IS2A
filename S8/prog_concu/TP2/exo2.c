#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  char fich[256];

  printf("Entrez le nom du fichier : ");
  scanf("%s", fich);

  int pid = fork();
  if (pid < 0) {
    perror("fork");
    exit(1);
  }

  if (pid == 0) {
    execlp("wc", "wc", "-l", fich, NULL);
    perror("wc");
    exit(1);
  } else {
    wait(NULL);
  }

  return 0;
}
