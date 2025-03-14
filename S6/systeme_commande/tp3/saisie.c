#include <stdio.h>
#define TAILLE_MAX 128

void saisie(FILE* fic, int n)
{
  int i;
  char string[TAILLE_MAX];

  for (i=0; i<n; i++)
  {
    printf("Saisissez un nom (encore %d):\n", n-i);
    scanf("%s", string);
    fprintf(fic, "%s\t", string);
  }
}

int main()
{
  FILE* fic = fopen("fic", "w");

  if (fic == NULL)
  {
    printf("Erreur d'ouverture\n");
    return 1;
  }

  saisie(fic, 3);

  printf("Fichier ecrit\n");

  fclose(fic);

  return 0;
}