#include <stdio.h>
#define TAILLE_MAX 128

void affichage(FILE* fic, int n)
{
  int i;
  char string[TAILLE_MAX];

  for (i=0; i<n; i++)
    {
      fscanf(fic, "%s\t", string);
      printf("Nom %d : %s\n", i+1, string);
    }
}

int main()
{
  FILE* fic = fopen("fic", "r");

  if (fic == NULL)
    {
      printf("Erreur d'ouverture\n");
      return 1;
    }

  affichage(fic, 3);

  fclose(fic);

	return 0;
}