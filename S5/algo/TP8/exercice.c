#define SIZE_STR 128
#define MAX_PERSONS 10
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
  int day;
  int month;
  int year;
} Date;

typedef struct {
  char lastName[SIZE_STR];
  char firstName[SIZE_STR];
  char mobile[SIZE_STR];
  Date birthDate;
} Person;

typedef struct {
  Person persons[MAX_PERSONS];
  int lastPerson;
} Annuaire;

Annuaire initAnnuaire(char *fileName) {
  Annuaire annuaire;
  Person person;
  FILE *file = fopen(fileName, "r");
  annuaire.lastPerson = -1;
  if (!file) {
    printf("Impossible d'ouvrir le fichier %s\n", fileName);
    return annuaire;
  }

  fscanf(file, "%s %s %s %d %d %d", person.lastName, person.firstName, person.mobile, &person.birthDate.day, &person.birthDate.month, &person.birthDate.year);
  while(!feof(file) && annuaire.lastPerson < MAX_PERSONS) {
    annuaire.lastPerson++;
    annuaire.persons[annuaire.lastPerson] = person;
    fscanf(file, "%s %s %s %d %d %d", person.lastName, person.firstName, person.mobile, &person.birthDate.day, &person.birthDate.month, &person.birthDate.year);
  }

  fclose(file);
  return annuaire;
}

void printPerson(Person person) {
  if (person.mobile[0] == '\0') {
    printf("Personne non trouvée\n");
    return;
  }
  printf("%s %s %s %d/%d/%d\n", person.lastName, person.firstName, person.mobile, person.birthDate.day, person.birthDate.month, person.birthDate.year);
}
  

void printAnnuaire(Annuaire annuaire) {
  printf("Nombre de personnes : %d\n", annuaire.lastPerson + 1);
  for (int i = 0; i <= annuaire.lastPerson; i++) {
    printPerson(annuaire.persons[i]);
  }
}

int compareStrings(char *str1, char *str2) {
  int i = 0, res = 1;
  while (str1[i] != '\0' && str2[i] != '\0' && res) {
    if (str1[i] != str2[i]) {
      res = 0;
    }
    i++;
  }
  return res;
}

// To dico
Person findPerson(Annuaire annuaire, char *mobile) {
  Person person;
  int i = 0;
  printf("Recherche de la personne avec le mobile : %s\n", mobile);
  while(person.mobile[0] == '\0' && i < MAX_PERSONS) {
    if (compareStrings(annuaire.persons[i].mobile, mobile)) {
      person = annuaire.persons[i];
    }
    i++;
  }
  return person;
}

Person initPerson(char *lastName, char *firstName, char *mobile, int day, int month, int year) {
  Person person;
  strcpy(person.lastName, lastName);
  strcpy(person.firstName, firstName);
  strcpy(person.mobile, mobile);
  person.birthDate.day = day;
  person.birthDate.month = month;
  person.birthDate.year = year;
  return person;
}

// Sorted
void addPerson(Annuaire *annuaire, Person person) {
  if ((*annuaire).lastPerson > MAX_PERSONS) {
    printf("Annuaire plein\n");
    return;
  }
  (*annuaire).persons[(*annuaire).lastPerson + 1] = person;
  (*annuaire).lastPerson++;
}

void deletePerson(Annuaire *annuaire, char *mobile) {
  int i = 0, personFound = 0;
  while(i <= (*annuaire).lastPerson ) {
    if (!personFound && compareStrings((*annuaire).persons[i].mobile, mobile)) {
      personFound = 1;
    }
    if (personFound && i+1 < (*annuaire).lastPerson) {
      (*annuaire).persons[i] = (*annuaire).persons[i + 1];
    }
    i++;
  }
  if (personFound) {
    (*annuaire).lastPerson--;
  }
}

void saveAnnuaire(Annuaire annuaire, char *fileName) {
  FILE *file = fopen(fileName, "w");
  if (!file) {
    printf("Impossible d'ouvrir le fichier %s\n", fileName);
    return;
  }
  for (int i = 0; i <= annuaire.lastPerson; i++) {
    fprintf(file, "%s %s %s %d %d %d", annuaire.persons[i].lastName, annuaire.persons[i].firstName, annuaire.persons[i].mobile, annuaire.persons[i].birthDate.day, annuaire.persons[i].birthDate.month, annuaire.persons[i].birthDate.year);
    if (i < annuaire.lastPerson) {
      fprintf(file, "\n");
    }
  }
  fclose(file);
}

int main(int argc, char *argv[]) {
  Annuaire annuaire;
  Person person;

  annuaire = initAnnuaire(argv[1]);

  printAnnuaire(annuaire);

//  printPerson(findPerson(annuaire, "0769389805"));
//  printPerson(findPerson(annuaire, "0269389805"));
//  printPerson(findPerson(annuaire, "02693d89805"));

//  person = initPerson("Lavielle", "Zoe", "0102030405", 05, 05, 2004);
//  printf("\n");
//  printf("Ajout de la personne : ");
//  printPerson(person);
//  addPerson(&annuaire, person);
//  printf("\n");
//  printAnnuaire(annuaire);
//  printf("\n");

//  printf("Suppression de la personne avec le mobile : %s\n \n", person.mobile);
//  deletePerson(&annuaire, person.mobile);
//
//  printAnnuaire(annuaire);

//  saveAnnuaire(annuaire, argv[2]);

  return 0;
}