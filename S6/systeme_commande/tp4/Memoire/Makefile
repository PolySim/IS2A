#########################################################
# Tanguy Risset, 2005
#########################################################
CC            =gcc 
LD	      =ld
CFLAGS	+=     -g

#Sur ma machine 2007
#GCCLIB=/usr/lib/gcc-lib/i486-linux/3.3.5
#Sur ma machine 2008
#GCCLIB=/usr/lib/gcc/i486-linux-gnu/4.2.3
#Sur tc-info1 en 2005
#GCCLIB=/usr/lib/gcc-lib/i586-mandrake-linux-gnu/3.3.2
#Sur tc-info1 en 2008
GCLIB=/usr/lib/gcc-lib/i486-linux-gnu/3.3.6

all: help


help: 
	@echo "cibles possibles: "  
	@echo " vmem1 vmem2 fibdyn fibstat clean"

vmem1: vmem1.c
	${CC}  ${CFLAGS} vmem1.c -lm -o vmem1 

vmem2: vmem2.c
	${CC}  ${CFLAGS} vmem2.c -lm -o vmem2 

fibdyn: fib.c 
	gcc  fib.c  -o fibdyn

fibstat: fib.c 
	gcc  fib.c -static -o fibstat

clean:
	-rm -f *~ *.out fib fibdyn fibstat 	 *.o

dummy:
