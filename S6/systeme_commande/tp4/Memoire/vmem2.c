#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <sys/timeb.h>
#include <sys/time.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
  char *a;
  int i,j,block_size,ii,l,iter; 
  long alloc_size;
  struct timeval debut_temps_physique,fin_temps_physique;
  clock_t debut_nb_clock_cycles,fin_nb_clock_cycles;
 
  // get cache size via /proc/cpuinfo
  alloc_size=pow(2,15);
  block_size=6*pow(2,12);
  a=(char *)malloc(alloc_size*sizeof(char));
  fprintf(stdout,"allocating %ld bytes\n",alloc_size);
  fprintf(stdout,"block size =%d bytes\n",block_size);
 
  iter=100000;

  fprintf(stdout,"nombre d'itérations=%d\n",iter);
  gettimeofday(&debut_temps_physique,0);
  debut_nb_clock_cycles=clock();

  for (j=0;j<iter;j++)
    {
      for(i=0;i<alloc_size/block_size;i++)
        {
          for (ii=0;ii<block_size;ii++)
            {
              l=i*block_size+ii;
              a[l]='a';
            }
        }
    }
  gettimeofday(&fin_temps_physique,0);
  fin_nb_clock_cycles=clock();
  printf("première boucle:\n");
  printf("Temps physique (seconde) = %d\n",
         (int)(fin_temps_physique.tv_sec-debut_temps_physique.tv_sec));
  printf("Temps processus (mseconde) = %ld\n",
         (fin_nb_clock_cycles-debut_nb_clock_cycles)/(CLOCKS_PER_SEC/1000));

  gettimeofday(&debut_temps_physique,0);
  debut_nb_clock_cycles=clock();

  for (j=0;j<iter;j++)
    {
      for (ii=0;ii<block_size;ii++)
        {
          for(i=0;i<alloc_size/block_size;i++)
            {
              l=i*block_size+ii;
              a[l]='a';
            }
        }
    }
  gettimeofday(&fin_temps_physique,0);
  fin_nb_clock_cycles=clock();

  printf("Deuxième boucle:\n");
  printf("Temps physique (seconde) = %d\n",
         (int)(fin_temps_physique.tv_sec-debut_temps_physique.tv_sec));
  printf("Temps processus (mseconde) = %ld\n",
         (fin_nb_clock_cycles-debut_nb_clock_cycles)/(CLOCKS_PER_SEC/1000));

  return(0);
}
