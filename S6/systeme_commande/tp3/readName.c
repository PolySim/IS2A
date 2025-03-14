#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#define TAILLE_MAX 128

int main()
{
  int fd = open("desdevises.txt", O_RDONLY);
  char buffer[TAILLE_MAX];

  if (fd == -1) {
    perror("Erreur lors de l'ouverture du fichier");
    return 1;
  }

  ssize_t bytes_read = read(fd, buffer, sizeof(buffer) - 1);

  if (bytes_read == -1) {
    perror("Erreur lors de la lecture");
    close(fd);
    return 1;
  }

  buffer[bytes_read] = '\0';
  printf("Nom : %s\n", buffer);

  close(fd);

  return 0;
}