#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
  if (argc != 2) {
    perror("Nombre de params");
    exit(1);
  }
  int seuil = atoi(argv[1]);
  int child1, child2;
  int tube[2];
  int input;

  if (pipe(tube) < 0) {
    perror("Pipe");
    exit(1);
  }

  child1 = fork();
  if (child1 < 0) {
    perror("child1");
    exit(1);
  }

  if (child1 == 0) {
    if (close(tube[0]) < 0) {
      perror("Close child 1 tube 0");
      exit(1);
    }
    scanf("%d", &input);
    while (input != 0) {
      if (input % 2 == 0) {
        write(tube[1], &input, sizeof(int));
      }
      scanf("%d", &input);
    }
    if (close(tube[1]) < 0) {
      perror("Close child 1 tube 1");
      exit(1);
    }
  } else {
    child2 = fork();
    if (child2 < 0) {
      perror("child2");
      exit(1);
    }

    if (child2 == 0) {
      if (close(tube[1]) < 0) {
        perror("Close child 2 tube 1");
        exit(1);
      }
      while (read(tube[0], &input, sizeof(int))) {
        if (input > seuil) {
          printf("%d\n", input);
        }
      }
    } else {
      if (close(tube[0]) < 0) {
        perror("Close pere tube 0");
        exit(1);
      }
      if (close(tube[1]) < 0) {
        perror("Close pere tube 1");
        exit(1);
      }
      waitpid(child1, NULL, 0);
      waitpid(child2, NULL, 0);
      printf("Fin de la fonction\n");
    }
  }

  return 0;
}
