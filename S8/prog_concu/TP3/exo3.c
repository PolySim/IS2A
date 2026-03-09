#include <pthread.h>
#include <stdio.h>
#define MAX 10

int tab[MAX];
int nb = 0; // cumul des valeurs anormales (sup ou inf aux seuils)

void lire() {
  int i;
  printf("saisir %d entiers\n", MAX);
  for (i = 0; i < MAX; i++)
    scanf("%d", &tab[i]);
}

void affiche() {
  int i;
  printf("entiers saisis :");
  for (i = 0; i < MAX; i++)
    printf("%d ", tab[i]);
  printf("\n");
}

void *moyenne(void *arg) {
  int i, moy = tab[0];
  for (i = 1; i < MAX; i++)
    moy = moy + tab[i];
  printf("moyenne des entiers = %f\n", (float)moy / MAX);
  return NULL;
}

void *supSeuil(void *seuil_arg) {
  int seuil = *(int *)seuil_arg;
  int i;
  for (i = 0; i < MAX; i++)
    if (tab[i] > seuil)
      nb++;

  *(int *)seuil_arg = nb;
  return NULL;
}

void *infSeuil(void *seuil_args) {
  int seuil = *(int *)seuil_args;
  int i;
  for (i = 0; i < MAX; i++)
    if (tab[i] < seuil)
      nb++;
  *(int *)seuil_args = nb;
  return NULL;
}

void minMax(int *min, int *max) {
  int i;
  *min = tab[0];
  *max = tab[0];
  for (i = 1; i < MAX; i++) {
    if (tab[i] < *min)
      *min = tab[i];
    else if (tab[i] > *max)
      *max = tab[i];
  }
}

int main() {
  int seuilInf, seuilSup, min, max;

  lire();
  affiche();

  printf("saisir les seuils sup et inf : ");

  scanf("%d%d", &seuilSup, &seuilInf);

  pthread_t thread_moy, thread_sup, thread_inf;
  pthread_attr_t attr_detached;
  pthread_attr_init(&attr_detached);
  pthread_attr_setdetachstate(&attr_detached, PTHREAD_CREATE_DETACHED);

  pthread_create(&thread_moy, &attr_detached, moyenne, NULL);
  pthread_create(&thread_sup, NULL, supSeuil, &seuilSup);
  pthread_create(&thread_inf, NULL, infSeuil, &seuilInf);
  minMax(&min, &max);

  printf("min = %d, max = %d\n", min, max);

  pthread_join(thread_sup, NULL);
  printf("fin de fonction supSeuil : nb = %d\n", seuilSup);
  pthread_join(thread_inf, NULL);
  printf("fin de fonction infSeuil : nb = %d\n", seuilInf);

  pthread_exit(NULL);
  return (0);
}
