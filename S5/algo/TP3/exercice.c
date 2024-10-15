#include <stdio.h>

void decomp3(int number, int* centaines, int* dizaines, int* unites)
{
    *centaines = number / 100;
    *dizaines = (number / 10) % 10;
    *unites = number % 10;
}

void permute(int* number1, int* number2) {
    int temp = *number1;
    *number1 = *number2;
    *number2 = temp;
}

void minMax(int* number1, int* number2) {
    if (*number1 > *number2) {
        permute(number1, number2);
    }
}

void ordonne3(int* number1, int* number2, int* number3) {
    minMax(number1, number2);
    minMax(number1, number3);
    minMax(number2, number3);
}

int kaprekar(int number) {
    int centaines, dizaines, unites, n1, n2;
    decomp3(number, &centaines, &dizaines, &unites);
    ordonne3(&unites, &dizaines, &centaines);
    n1 = centaines * 100 + dizaines * 10 + unites;
    n2 = unites * 100 + dizaines * 10 + centaines;
    return n1 - n2;
}

void suiteKaprekar(int* number, int* result) {
    int i = 1, lastKaprekar = *number;
    *number = kaprekar(*number);
    while (*number != lastKaprekar) {
        lastKaprekar = *number;
        *number = kaprekar(*number);
        i++;
    }
    *result = i;
}

int main(void)
{
    int result, number, centaine, dizaine, unite;

    scanf("%d %d %d", &centaine, &dizaine, &unite);
    number= centaine * 100 + dizaine * 10 + unite;

    suiteKaprekar(&number, &result);
    printf("Nombre de permutations : %d\n", result);
    printf("Limite : %d", kaprekar(901));
    return 0;
}