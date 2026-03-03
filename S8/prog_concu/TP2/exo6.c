#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  int tube_1[2];
  int child;
  int input;

  if (pipe(tube_1) < 0) {
    perror("Pipe tube_1");
    exit(1);
  }

  child = fork();

  if (child == 0) {
    int petit_child;
    int tube_2[2];

    if (pipe(tube_2) < 0) {
      perror("Pipe tube_2");
      exit(1);
    }

    petit_child = fork();

    if (petit_child == 0) {
      if (close(tube_1[0])) {
        perror("Close petit_child tube 1 0");
        exit(1);
      }
      if (close(tube_1[1])) {
        perror("Close petit_child tube 1 1");
        exit(1);
      }
      if (close(tube_2[1])) {
        perror("Close petit_child tube 2 1");
        exit(1);
      }
      while (read(tube_2[0], &input, sizeof(int))) {
        printf("sum : %d\n", input);
      }
      if (close(tube_2[0])) {
        perror("Close child tube 2 0");
        exit(1);
      }

    } else {
      if (close(tube_1[1])) {
        perror("Close child tube 1 1");
        exit(1);
      }
      if (close(tube_2[0])) {
        perror("Close child tube 2 0");
        exit(1);
      }

      int sum = 0;
      while (read(tube_1[0], &input, sizeof(int))) {
        sum += input;
        write(tube_2[1], &sum, sizeof(int));
      }

      if (close(tube_2[1])) {
        perror("Close child tube 2 1");
        exit(1);
      }
      if (close(tube_1[0])) {
        perror("Close child tube 1 0");
        exit(1);
      }

      if (waitpid(petit_child, NULL, 0) < 0) {
        perror("Wait petit_child");
        exit(1);
      }
    }

  } else {
    if (close(tube_1[0]) < 0) {
      perror("Pere close tube 1 0");
      exit(1);
    }

    scanf("%d", &input);
    while (input != 0) {
      write(tube_1[1], &input, sizeof(int));
      scanf("%d", &input);
    }

    if (close(tube_1[1]) < 0) {
      perror("Pere close tube 1 1");
      exit(1);
    }

    if (waitpid(child, NULL, 0) < 0) {
      perror("Wait child");
      exit(1);
    }

    printf("Fin de programme");
  }

  return 0;
}
