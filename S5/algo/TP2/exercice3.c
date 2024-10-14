#include <stdio.h>

int somme(int nb1, int nb2) {
    return nb1 + nb2;
}

void minMax(int nb1, int nb2, int nb3, int *pmin, int *pmax) {
    *pmin = nb1;
    *pmax = nb1;
    if (nb2 > *pmax) {
        *pmax = nb2;
    }
    if (nb2 < *pmin) {
        *pmin = nb2;
    }
    if (nb3 > *pmax) {
        *pmax = nb3;
    }
    if (nb3 < *pmin) {
        *pmin = nb3;
    }
}

void traitementSuite(double *pmoyenne, int *pmin, int *pmax) {
    int number, index = 0;
    printf("Entrez un nombre : ");
    scanf("%d", &number);
    *pmin = number;
    *pmax = number;
    *pmoyenne = number / 1.0;
    while (number != 0) {
        index++;
        printf("Entrez un nombre : ");
        scanf("%d", &number);
        *pmoyenne += number / 1.0;
        minMax(*pmin, *pmax, number, pmin, pmax);
    }
    if (index != 0) {
        *pmoyenne /= index;
    }
}

int main(void) {
    double moyenne;
    int min, max;
    traitementSuite(&moyenne, &min, &max);
    printf("Le moyenne est : %f\n", moyenne);
    printf("Le minimum est : %d\n", min);
    printf("Le maximum est : %d\n", max);
    return 0;
}