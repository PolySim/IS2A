#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

int main() {
  int child_who, child_ps, child_ls;

  child_who = fork();
  if (child_who < 0) {
    perror("Creation child_who");
    exit(1);
  }

  if (child_who == 0) {
    execlp("who", "who", NULL);
  }

  if (waitpid(child_who, NULL, 0) < 0) {
    perror("Wait who");
    exit(1);
  }
  child_ps = fork();
  if (child_ps < 0) {
    perror("Creation child_ps");
    exit(1);
  }

  if (child_ps == 0) {
    execlp("ps", "ps", "-x", NULL);
  }

  if (waitpid(child_ps, NULL, 0) < 0) {
    perror("Wait ps");
    exit(1);
  }
  child_ls = fork();
  if (child_ls < 0) {
    perror("Creation child_ls");
    exit(1);
  }

  if (child_ls == 0) {
    execlp("ls", "ls", "-la", NULL);
  }

  if (waitpid(child_ls, NULL, 0) < 0) {
    perror("Wait ls");
    exit(1);
  }

  return 0;
}
