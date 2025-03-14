#include <stdio.h>

int fib(int a)
{
  if ((a==0) || (a==1))
    return(1);
  else 
    return(fib(a-1)+fib(a-2));
}

int main(int argc, char *argv[])
{char a;

  fprintf(stdout,"fib(5)=%d\n",fib(5));
  fscanf(stdin,"%c",&a);
}
