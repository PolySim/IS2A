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
    struct json_object *adresse;
    struct json_object *numero;
    struct json_object *rue;
    struct json_object *code_postal;
    struct json_object *etu;
    json_type type;

    fp = fopen(argv[1],"r");
    fread(buffer, 2048, 1, fp);
    fclose(fp);

    parsed_json = json_tokener_parse(buffer);

    int n_etu; // Nombre d amis
    int i;         // Indice de boucle

    n_etu = json_object_array_length(parsed_json);

    for(i=0;i<n_etu;i++) {
      etu = json_object_array_get_idx(parsed_json, i);
      json_object_object_get_ex(etu, "Boursier", &boursier);
      json_object_object_get_ex(etu, "Id", &id);
      json_object_object_get_ex(etu, "Nom", &nom);
      json_object_object_get_ex(etu, "Prénom", &prenom);
      json_object_object_get_ex(etu, "Age", &age);
      json_object_object_get_ex(etu, "Moyenne", &moyenne);
      json_object_object_get_ex(etu, "Adresse", &adresse);
      json_object_object_get_ex(adresse, "Numéro", &numero);
      json_object_object_get_ex(adresse, "Rue", &rue);
      json_object_object_get_ex(adresse, "Code postal", &code_postal);

      printf("Boursier: %d\n", json_object_get_boolean(boursier));
      printf("Id: %s\n", json_object_get_string(id));
      printf("Nom: %s\n", json_object_get_string(nom));
      printf("Prénom: %s\n", json_object_get_string(prenom));
      printf("Age: %d\n", json_object_get_int(age));
      type = json_object_get_type(moyenne);
      if(type == json_type_null) {
        printf("Moyenne : null\n");
      } else {
        printf("Moyenne: %.2f\n", json_object_get_double(moyenne));
      }
      printf("Numéro: %d\n", json_object_get_int(numero));
      printf("Rue: %s\n", json_object_get_string(rue));
      printf("Code postal: %d\n", json_object_get_int(code_postal));
      printf("\n\n\n");
    }

    return 0;
}
