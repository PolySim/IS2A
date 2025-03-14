#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#define TAILLE_MAX 1024

int main(int argc, char* argv[])
{
  if (argc < 3) {
    printf("Il faut deux arguments\n");
    return 1;
  }
  int file_read = open(argv[1], O_RDONLY);
  int file_write = open(argv[2], O_WRONLY | O_CREAT, 0644);
  char file_content[TAILLE_MAX];
  int i = 0;

  if (file_read == -1 || file_write == -1) {
    perror("Erreur lors de l'ouverture du fichier");
    return 1;
  }

  ssize_t bytes_read = read(file_read, file_content, 5 * sizeof(char));
  while (bytes_read > 0) {
    if (i == 0) {
      write(file_write, file_content, 5);
      i = 1;
    } else i = 0;

    bytes_read = read(file_read, file_content, 5 * sizeof(char));
  }

  close(file_read);
  close(file_write);

  return 0;
}