#include <stdio.h>
#include <json-c/json.h>

int main(int argc, char **argv) {
	FILE *fp;
	char buffer[2048];

	struct json_object *parsed_json;
	struct json_object *boursier;
	struct json_object *id;
    struct json_object *nom;
    struct json_object *prenom;
    struct json_object *age;
    struct json_object *moyenne;

	fp = fopen(argv[1],"r");
	fread(buffer, 2048, 1, fp);
	fclose(fp);

	parsed_json = json_tokener_parse(buffer);
	
	json_object_object_get_ex(parsed_json, "Boursier", &boursier);
    json_object_object_get_ex(parsed_json, "Id", &id);
    json_object_object_get_ex(parsed_json, "Nom", &nom);
    json_object_object_get_ex(parsed_json, "Prénom", &prenom);
    json_object_object_get_ex(parsed_json, "Age", &age);
    json_object_object_get_ex(parsed_json, "Moyenne", &moyenne);

	printf("Boursier: %d\n", json_object_get_boolean(boursier));
    printf("Id: %s\n", json_object_get_string(id));
    printf("Nom: %s\n", json_object_get_string(nom));
    printf("Prénom: %s\n", json_object_get_string(prenom));
    printf("Age: %d\n", json_object_get_int(age));
    printf("Moyenne: %f\n", json_object_get_double(moyenne));

	return 0;
}
