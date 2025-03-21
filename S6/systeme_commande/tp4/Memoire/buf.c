#include <stdio.h>
#include <string.h>

char secret[] = "Jryy qbar lbhat cnqnjna!";

struct token {
  char buf[15];
  int pass;
};

void rot13(char * str, int size) {
  int i = 0;
  
  /* Stop whenever size is reached or a '\0' is found  */
  for(i = 0; (str[i] != '\0') && (i<size) ; i++ ){
    
    /* If the letter is between a and m you can simply sum it. */
    if( *(str+i) >= 'a' && *(str+i) < 'n') {
      *(str+i) += 13;      
      
      /* If the letter is between the n and z you have to do the opposite.*/
    } else if(*(str+i) >= 'n' && *(str+i) <= 'z') {
      *(str+i) -= 13;
      
      /* Same for capital letters */
    } else if(*(str+i) >= 'N' && *(str+i) <= 'Z') {
      *(str+i) -= 13;
      
    } else if(*(str+i) >= 'A' && *(str+i) < 'N') {
      *(str+i) += 13;
    }
  }
}

int main(void) {
  struct token myToken;
  myToken.pass = 0;
  
  printf("The secret is reserved for those who can enter the password:\n");
  
  gets(myToken.buf);
  
  if(strcmp(myToken.buf, "IloveOS")) {
    printf ("\nIncorrect Password \n");
  } else {
    printf ("\nPassword OK\n");
    myToken.pass = 1;
  }
  
  if(myToken.pass) {
    /* Now start secret things! */
    /* We undo the rotation on the secret */
    rot13(secret, 24);
    /* We print the secret */
    printf("%s\n", secret);
  }
  
  return 0;
}
