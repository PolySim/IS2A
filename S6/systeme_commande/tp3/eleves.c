#include <stdio.h>
#define TAILLE_MAX 128

struct eleve
{
  char nom[TAILLE_MAX];
  char prenom[TAILLE_MAX];
  double moyenne;
};

void add_eleve(FILE* f_eleves)
{
  struct eleve eleve;

  printf("Saisissez le nom et le prenom de l'eleve:\n");
  scanf("%s %s", eleve.nom, eleve.prenom);
  printf("Saisissez sa moyenne:\n");
  scanf("%lf", &eleve.moyenne);

  fprintf(f_eleves, "%s\t%s\t%.2lf\n", eleve.nom, eleve.prenom, eleve.moyenne);
}

void affiche_eleves(FILE* f_eleves)
{
  struct eleve eleve;

  while (fscanf(f_eleves, "%s\t%s\t%lf", eleve.nom, eleve.prenom, &eleve.moyenne) == 3)
    {
      printf("Nom : %s\t", eleve.nom);
      printf("Prenom : %s\t", eleve.prenom);
      printf("Moyenne : %.2lf\n", eleve.moyenne);
    }
}

int main()
{
  FILE* f_eleves = fopen("eleves.txt", "a+");

  if (f_eleves == NULL)
    {
      printf("Erreur d'ouverture\n");
      return 1;
    }

  add_eleve(f_eleves);
  rewind(f_eleves);
  affiche_eleves(f_eleves);
  fclose(f_eleves);

  return 0;
}