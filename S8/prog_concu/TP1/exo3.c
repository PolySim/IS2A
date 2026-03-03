#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  int first_child, second_child;

  first_child = fork();
  if (first_child < 0) {
    perror("Create first child");
    exit(1);
  }
  if (first_child == 0) {
    sleep(4);
    printf("C'est le premier fils qui écrit, pid: %d , ppid: %d\n", getpid(),
           getppid());
    return 0;
  }

  second_child = fork();
  if (second_child < 0) {
    perror("Create second child");
    exit(1);
  }
  if (second_child == 0) {
    sleep(2);
    printf("C'est le second fils qui écrit, pid: %d , ppid: %d\n", getpid(),
           getppid());
    return 0;
  }

  printf("C'est le père qui écrit, pid: %d , ppid: %d\n", getpid(), getppid());
  waitpid(first_child, NULL, 0);
  waitpid(second_child, NULL, 0);

  return 0;
}
