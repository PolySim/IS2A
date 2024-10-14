#include <stdio.h>

void minmax2(int *pa, int *pb) {
    int tmp;
    if (*pa > *pb) {
        tmp = *pa;
        *pa = *pb;
        *pb = tmp;
    }
}

void tri(int *pa, int *pb, int *pc) {
    minmax2(pa, pb);
    minmax2(pa, pc);
    minmax2(pb, pc);
}

int main(void) {
    int a, b, c;
    scanf("%d %d %d", &a, &b, &c);
    tri(&a, &b, &c);
    printf("%d %d %d", a, b, c);
    return 0;
}