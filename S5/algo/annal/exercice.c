#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define NB_JOURS 365
#define STR_SIZE 128

typedef struct {
  int jour, mois, hauteur;
} RelJ;

typedef struct {
  RelJ rel[NB_JOURS];
  int nbR;
} RelA;

RelA initRelA(char *filePath) {
  RelA relA;
  RelJ relJ;
  FILE *file = fopen(filePath, "r");
  if (!file) {
    printf("Impossible d'ouvrir le fichier %s\n", filePath);
    relA.nbR = -1;
    return relA;
  }
  relA.nbR = 0;
  fscanf(file, "%d %d %d", &relJ.jour, &relJ.mois, &relJ.hauteur);
  relA.rel[relA.nbR] = relJ;
  relA.nbR++;
  while(!feof(file)) {
    fscanf(file, "%d %d %d", &relJ.jour, &relJ.mois, &relJ.hauteur);
    relA.rel[relA.nbR] = relJ;
    relA.nbR++;
  }
  fclose(file);
  return relA;
};

void printRelA(RelA relA) {
  printf("Nombre de rel : %d\n", relA.nbR);
  for (int i = 0; i < relA.nbR; i++) {
    printf("Jour : %d  Mois : %d Hauteur : %d\n", relA.rel[i].jour, relA.rel[i].mois, relA.rel[i].hauteur);
  }
}

int pluie(RelA relA, int mois) {
  int start = 0, end = relA.nbR - 1, currentIndex = relA.nbR / 2;

  while (start <= end && relA.rel[currentIndex].mois != mois) {
    if (relA.rel[currentIndex].mois > mois) {
      end = currentIndex - 1;
    } else {
      start = currentIndex + 1;
    }
    currentIndex = (start + end) / 2;
  }

  return start <= end;
}

void plusMoins(RelA relA, int *moins, int *plus) {
  int i = 1, minPluie = relA.rel[0].hauteur, maxPluie = relA.rel[0].hauteur, currentPluie = relA.rel[0].hauteur, currentMois = relA.rel[0].mois;

  while (i < relA.nbR && relA.rel[i].mois == relA.rel[i + 1].mois) {
    currentPluie += relA.rel[i].hauteur;
    i++;
  }

  *moins = currentMois;
  *plus = currentMois;
  minPluie = currentPluie;
  maxPluie = currentPluie;
  currentPluie = relA.rel[i + 1].hauteur;
  currentMois = relA.rel[i + 1].mois;

  for (i = i + 1; i < relA.nbR; i++) {
    if (currentMois != relA.rel[i].mois) {
      if (currentPluie < minPluie) {
        minPluie = currentPluie;
        *moins = currentMois;
      }
      if (currentPluie > maxPluie) {
        maxPluie = currentPluie;
        *plus = currentMois;
      }
      currentPluie = relA.rel[i].hauteur;
      currentMois = relA.rel[i].mois;
    } else {
      currentPluie += relA.rel[i].hauteur;
    }
  }
  if (currentPluie < minPluie) {
    minPluie = currentPluie;
    *moins = currentMois;
  }
  if (currentPluie > maxPluie) {
    maxPluie = currentPluie;
    *plus = currentMois;
  }
}

void ajout(RelA *relA, int jour, int mois, int hauteur) {
  if ((*relA).nbR == NB_JOURS) {
    printf("RelA pleine\n");
    return;
  }
  if (mois < (*relA).rel[(*relA).nbR - 1].mois || (mois == (*relA).rel[(*relA).nbR - 1].mois && jour <= (*relA).rel[(*relA).nbR - 1].jour)) {
    printf("Data anterieure\n");
    return;
  }
  (*relA).rel[(*relA).nbR].jour = jour;
  (*relA).rel[(*relA).nbR].mois = mois;
  (*relA).rel[(*relA).nbR].hauteur = hauteur;
  (*relA).nbR++;
  printf("Ajout de la rel %d %d %d\n", (*relA).rel[(*relA).nbR - 1].jour, (*relA).rel[(*relA).nbR - 1].mois, (*relA).rel[(*relA).nbR - 1].hauteur);
}

int main(int argc, char *argv[]) {
  char * dataPath = argv[1];
  int moins, plus;
  RelA relA = initRelA(dataPath);

  printRelA(relA);

  printf("\n");
  printf("Pluie en janvier : %d\n", pluie(relA, 1));
  printf("Pluie en Aout : %d\n", pluie(relA, 8));

  plusMoins(relA, &moins, &plus);
  printf("Pluie moins: %d \nPluie plus: %d\n", moins, plus);

  printf("\n");
  ajout(&relA, 30, 4, 10);
  ajout(&relA, 19, 5, 10);
  ajout(&relA, 30, 6, 10);

  printf("\n");
  printRelA(relA);


  return 0;
}