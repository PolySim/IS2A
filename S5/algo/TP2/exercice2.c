#include <stdio.h>

void lireDate(int *pday,int *pmonth,int *pyear) {
    printf("Entrez la date souhaitée (jj mm aaaa) : ");
    scanf("%d %d %d", pday, pmonth, pyear);
}

void lire2Dates(int *pday1,int *pmonth1,int *pyear1,int *pday2,int *pmonth2,int *pyear2) {
    lireDate(pday1,pmonth1,pyear1);
    lireDate(pday2,pmonth2,pyear2);
}

int compareDates(int *pday1,int *pmonth1,int *pyear1,int *pday2,int *pmonth2,int *pyear2) {
    if (*pyear1 > *pyear2) {
        return 1;
    } else if (*pyear1 < *pyear2) {
        return -1;
    } else {
        if (*pmonth1 > *pmonth2) {
            return 1;
        } else if (*pmonth1 < *pmonth2) {
            return -1;
        } else {
            if (*pday1 > *pday2) {
                return 1;
            } else if (*pday1 < *pday2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

int main (void) {
    int day1, month1, year1, day2, month2, year2;
    lire2Dates(&day1, &month1, &year1, &day2, &month2, &year2);
    printf("%d", compareDates(&day1, &month1, &year1, &day2, &month2, &year2));
    return 0;
}