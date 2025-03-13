#include <stdio.h>
#include <json-c/json.h>

int main(int argc, char **argv) {
	FILE *fp;
	char buffer[2048];
	
	/* struct json_object : Structure contenant une arborescence d objets json */
	struct json_object *parsed_json; // Structure pour le stockage de l arboescence complete du fichier
	struct json_object *boursier;        // Structure pour le stockage de l objet de cle name

	/* Copie du contenu du fichier passe en paramaetre dans la variable buffer */
	fp = fopen(argv[1],"r");
	fread(buffer, 2048, 1, fp);
	fclose(fp);

	/* Parsing json du contenu de la variable buffer */
	parsed_json = json_tokener_parse(buffer);
	
	/* Recuperation de la valeur de la cle name */
	json_object_object_get_ex(parsed_json, "Boursier", &boursier);
	/* json_object_get_string(json_object)                                         */
	/* Renvoie la valeur chaine de caracteres de l objet json_object               */
	/* Si l objet n est pas une chaine de caracteres, la fonction renvoie son type */
	printf("Boursier: %d\n", json_object_get_boolean(boursier));

	return 0;
}
