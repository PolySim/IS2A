# Fork - Wait

## Exercice 1

Ecrire, compiler et tester le programme suivant :

```c
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
int main(){
int cr;
if ((cr=fork()) &lt; 0) {
perror("erreur exécution de fork");
exit(1);
}
for (;;) {
if (cr == 0) {
printf("c'est le fils qui ecrit \n");
sleep(1);
}
else {
printf("c'est le pere qui ecrit\n");
sleep(2);
}
}
return 0;
}
```

Q1 : Expliquer le comportement de ce programme.

Q2 : Effectuer la commande `ps xl` dans un autre terminal, repérer les PID et PPID des processus père et fils. Qui est le père du processus père créé au lancement de l'exécution du programme ?

Terminer l'exécution des processus en utilisant la commande `kill`.

## Exercice 2

Ecrire, compiler et tester le programme suivant :

```c
#include <stdio.h>
#include <unistd.h>
#include <sys wait.h="">
int main(){
int cr,ent=0;
if ((cr=fork()) &lt; 0) {
perror("erreur exécution de fork");
exit(1);
}
if (cr == 0) {
printf("dans le fils, valeur de ent avant incrémentation : %d\n", ent);
ent=ent+1;
printf("dans le fils, valeur de ent après incrémentation : %d\n", ent);
}
else {
int pid;
printf("dans le pere, valeur de ent avant incrémentation : %d\n", ent);
ent=ent+2;
printf("dans le pere, valeur de ent après incrémentation : %d\n", ent);
if ((pid = wait(NULL)) &lt; 0) {
perror("erreur exécution de wait");
exit(1);
}
}
return 0;
}
```

Justifier l'affichage obtenu lors de l'exécution (en particulier les valeurs affichées pour la variable `ent`).

## Exercice 3

Ecrire un programme dans lequel le processus père crée deux processus fils. Le père et ses deux fils affichent leur PID et PPID. Synchroniser correctement les processus pour empêcher le père de mourir avant ses fils. Vérifier que cette synchronisation est correctement effectuée en retardant la mort des fils (placer l'instruction `sleep(2)` au début du code du fils 1 et `sleep(4)` au début du code du fils 2, puis inverser ces 2 instructions : `sleep(4)` dans fils 1 et `sleep(2)` dans fils 2).

## Exercice 4

Modifier le programme précédent de façon à avoir un père, un fils et un petit-fils (ie : un fils du processus fils). Synchroniser correctement les processus : un père ne doit pas mourir avant ses fils.</sys></unistd.h></stdio.h></stdlib.h></stdio.h>
