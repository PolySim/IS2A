#define NB_ELEMENTS 4
#define LENGTH_1 4
#define LENGTH_2 5
#include <stdio.h>

void createVecteur(int v[], int length){
    int input, i, lastInput;
    scanf("%d", &lastInput);
    v[0] = lastInput;
    for (i = 1; i < length; i++){
        scanf("%d", &input);
        while (input < lastInput){
            printf("Veuillez entrer un nombre supérieur à %d\n", lastInput);
            scanf("%d", &input);
        }
        lastInput = input;
        v[i] = input;
    }
}

void printVecteur(int v[], int length){
    int i;
    for (i = 0; i < length; i++){
        printf("%d ", v[i]);
    }
}

void sumVecteur(int v[], int length, int *ppos, int *pneg){
    int i;
    for (i = 0; i < length; i++){
        if (v[i] > 0) {
            *ppos = v[i];
        } else {
            *pneg = v[i];
        }
    }
}

void concatVecteur(int v1[], int length1, int v2[], int length2, int v3[]){
    int i_v1 = 0, i_v2 = 0;
    while (i_v1 < length1 || i_v2 < length2){
        if (i_v1 == length1) {
            for (i_v2 = i_v2; i_v2 < length2; i_v2++){
                v3[i_v1 + i_v2] = v2[i_v2];
            }
        }
        if (i_v2 == length2) {
            for (i_v1 = i_v1; i_v1 < length1; i_v1++){
                v3[i_v1 + i_v2] = v1[i_v1];
            }
        }

        if (v1[i_v1] < v2[i_v2]) {
            v3[i_v1 + i_v2] = v1[i_v1];
            i_v1++;
        } else {
            v3[i_v1 + i_v2] = v2[i_v2];
            i_v2++;
        }
    }
}

int inVecter(int v[], int length, int value){
    int i= 0, isIn = 0;
    while (i < length && isIn == 0){
        if (v[i] == value){
            isIn = 1;
        }
        i++;
    }
    return isIn == 1;
}

void resetVecteur(int v[], int length) {
    int i;
    for (i = 0; i < length; i++){
        v[i] = 0;
    }
}

void intersectionVecteur(int v1[], int length1, int v2[], int length2, int v3[]){
    int i, currentIndex = 0;
    resetVecteur(v3, length1 + length2);
    for (i = 0; i < length1; i++){
        if (inVecter(v2, length2, v1[i])){
            v3[currentIndex] = v1[i];
            currentIndex++;
        }
    }
}

int main(void){
    int v[NB_ELEMENTS];
    int v1[LENGTH_1] = {-1, 3, 6, 9};
    int v2[LENGTH_2] = {0, 1, 3, 4, 5};
    int v3[LENGTH_1 + LENGTH_2];
    int vFusion[LENGTH_1 + LENGTH_2];
    concatVecteur(v1, LENGTH_1, v2, LENGTH_2, v3);
    intersectionVecteur(v1, LENGTH_1, v2, LENGTH_2, vFusion);
    printVecteur(vFusion, LENGTH_1 + LENGTH_2);

    return 0;
}