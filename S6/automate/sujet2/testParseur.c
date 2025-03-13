#include <stdio.h>
#include <json-c/json.h>

int main(int argc, char **argv) {
	FILE *fp;
	char buffer[2048];
	
	/* struct json_object : Structure contenant une arborescence d objets json */
	struct json_object *parsed_json; // Structure pour le stockage de l arboescence complete du fichier
	struct json_object *name;        // Structure pour le stockage de l objet de cle name
	struct json_object *age;		 // Structure pour le stockage de l objet de cle age
	struct json_object *friends;	 // Structure pour le stockage de l objet de cle friends
	struct json_object *friend;		 // Structure pour le stockage d un objet du tableau friends
	
	int n_friends; // Nombre d amis
	int i;         // Indice de boucle

	/* Copie du contenu du fichier passe en paramaetre dans la variable buffer */
	fp = fopen(argv[1],"r");
	fread(buffer, 2048, 1, fp);
	fclose(fp);

	/* Parsing json du contenu de la variable buffer */
	parsed_json = json_tokener_parse(buffer);
	
	/* Recuperation de la valeur de la cle name */
	json_object_object_get_ex(parsed_json, "name", &name);
	/* json_object_get_string(json_object)                                         */
	/* Renvoie la valeur chaine de caracteres de l objet json_object               */
	/* Si l objet n est pas une chaine de caracteres, la fonction renvoie son type */
	printf("Name: %s\n", json_object_get_string(name));
	
	/* Recuperation de la valeur de la cle age */
	json_object_object_get_ex(parsed_json, "age", &age);
	/* json_object_get_int(json_object)                             */
	/* Renvoie de la valeur entiere de l objet json_object          */
	/* Si l objet n est pas un entier, la fonction renvoie son type */
	printf("Age: %d\n", json_object_get_int(age));

	/* Recuperation de la valeur de la cle friends */
	json_object_object_get_ex(parsed_json, "friends", &friends);
	/* json_object_array_length(json_object)                      */
	/* Renvoie la longueur de l objet json_object de type tableau */
	n_friends = json_object_array_length(friends);
	printf("Found %d friends\n",n_friends);

	/* Parcours de l objet friends de type tableau */
	for(i=0;i<n_friends;i++) {
		/* Recuperation de la l element d indice i du tableau */
		friend = json_object_array_get_idx(friends, i);
		printf("%d. %s\n",i+1,json_object_get_string(friend));
	}
	
	return 0;
}
