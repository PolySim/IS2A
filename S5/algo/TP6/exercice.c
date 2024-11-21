#define N 3
#include <stdio.h>

// Question 1

void initMatrix(int mat[][N], int n) {
    int i, j;
    printf("Entrer les valeurs de la matrice :\n");
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            scanf("%d", &mat[i][j]);
        }
    }
}

// Question 2

void printMatrix(int mat[][N], int n) {
    int i, j;
    for (i = 0; i < n; i++) {
        for (j = 0; j < n; j++) {
            printf("%d ", mat[i][j]);
        }
        printf("\n");
    }
}

// Question 3

int sumLine(int mat[][N], int n, int i) {
    int j, sum = mat[i][0];
    for (j=1; j < n; j++) {
        sum += mat[i][j];
    }
    return sum;
}

// Question 4

int sumColumn(int mat[][N], int n, int j) {
    int i, sum = mat[0][j];
    for (i=1; i < n; i++) {
        sum += mat[i][j];
    }
    return sum;
}

// Question 5

void sumDiagonal(int mat[][N], int n, int *sumDiag, int *sumDiag2) {
    int i;
    *sumDiag = mat[0][0];
    *sumDiag2 = mat[0][n - 1];
    for (i = 1; i < n; i++) {
        *sumDiag += mat[i][i];
        *sumDiag2 += mat[i][n - i - 1];
    }
}

// Question 6

int isMagic(int mat[][N], int n) {
    int i = 0, sumDiag1, sumDiag2;
    sumDiagonal(mat, n, &sumDiag1, &sumDiag2);
    if (sumDiag1 != sumDiag2) {
        return 0;
    }
    while (i < n && sumLine(mat, n, i) == sumDiag1 && sumColumn(mat, n, i) == sumDiag1) {
        i++;
    }
    return i == n;
}

// Question 7

void initVecteur0(int vect[], int n) {
    int i;
    for (i= 0; i < n; i++) {
        vect[i] = 0;
    }
}

int vecteurEverySame(int vect[], int n) {
    int i;
    for (i = 1; i < n; i++) {
        if (vect[i] != vect[0]) {
            return 0;
        }
    }
    return 1;
}

int isMagicNormal(int mat[][N], int n) {
    int i = 0, j = 0, vect[3 * N + 1], result = 1, vectAmount[2 * N + 2];
    initVecteur0(vect, 3 * n + 1);
    initVecteur0(vectAmount, 2 * n + 2);
    while (i < n && result) {
        vectAmount[2 * n] += mat[i][i];
        vectAmount[2 * n + 1] += mat[i][n - 1 - i];
        while (j < n && result) {
            vectAmount[i] += mat[i][j];
            vectAmount[n + j] += mat[i][j];
            if (vect[mat[i][j]] != 0) {
                result = 0;
            }
            vect[mat[i][j]] = mat[i][j];
            j++;
        }
        i++;
        j = 0;
    }
    return result && vecteurEverySame(vectAmount, 2 * n + 2);
}

int main(void) {
    int mat[N][N];

    initMatrix(mat, N);
    printMatrix(mat, N);

    printf("Is the matrix a normal magic matrix ? %d\n", isMagicNormal(mat, N));

    return 0;
}