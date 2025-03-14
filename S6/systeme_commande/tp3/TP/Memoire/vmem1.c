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
  int i,j,power, temps_physique,temps_processus; 
  unsigned long size;
  struct timeval debut_temps_physique,fin_temps_physique;
  clock_t debut_nb_clock_cycles,fin_nb_clock_cycles;
 
  temps_physique=0;
  temps_processus=0;
  power=32;
  size=pow(2,power)+pow(2,power-2);
  fprintf(stdout,"Allocating 2^%d+2^%d=%ld bytes\n",power,power-2,size);

  // Use calloc instead of malloc because malloc is capped at 2GB for
  // 32-bits architectures
  a=(char *)calloc(size,sizeof(char));
  for (j=0;j<5;j++)
    {
      gettimeofday(&debut_temps_physique,0);
      debut_nb_clock_cycles=clock();
      /*********************** measured loop ******************/
      for(i=0;i<size;i++)
	{
	  a[i]='a';
	}
      /*********************** end of measured loop ****************/
      gettimeofday(&fin_temps_physique,0);
      fin_nb_clock_cycles=clock();
      fprintf(stdout,"Physical time (seconde) = %d\n",
	      (int)(fin_temps_physique.tv_sec-debut_temps_physique.tv_sec));
      fprintf(stdout,"Process time (seconde) = %d\n",
	      (int)((fin_nb_clock_cycles-debut_nb_clock_cycles)/CLOCKS_PER_SEC));
      fprintf(stdout,"-----------------------------------------\n");
      temps_physique+=fin_temps_physique.tv_sec-debut_temps_physique.tv_sec;
      temps_processus+=(fin_nb_clock_cycles-debut_nb_clock_cycles)/CLOCKS_PER_SEC;
    
    }
 
  fprintf(stdout,"Total physical time (second) = %d\n",temps_physique);
  fprintf(stdout,"Total process time (second) = %d\n",temps_processus);
  free(a);

  return(0);

}
