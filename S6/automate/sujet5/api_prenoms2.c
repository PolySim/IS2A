#include <stdio.h>
#include <curl/curl.h>
#include <json-c/json.h>
// -I/opt/homebrew/include -L/opt/homebrew/lib -ljson-c

// Fonction de rappel : Ecriture des donnes dans un fichier
size_t write_callback(char *ptr, size_t size, size_t nmemb, void *userdata) {
  FILE *stream = (FILE *)userdata;
  if (stream && fwrite(ptr, size, nmemb, stream)) {
    return nmemb;
  }
  return 0;
}

int main(void) {
  CURL *curl;
  CURLcode res;
  FILE *response_file;
  FILE *result_fileJSON;

  curl = curl_easy_init();
  if (curl) {
    // Ouverture du fichier reponse
    response_file = fopen("resultat2.json", "wb");
    if (!response_file) {
      printf("Error opening file for writing.\n");
      return 1;
    }

    // Configuration de la requete
    curl_easy_setopt(curl, CURLOPT_URL, "https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/liste_des_prenoms/records?select=year(annee)%20as%20annee,%20sum(nombre_total_cumule)%20as%20s&where=prenoms%20%3D%20%22Alice%22&group_by=annee&order_by=annee&limit=20"); // URL de la requete
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback); // Fonction de rappel (callback)
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, response_file); // Fichier d ecriture utilise dans la fonction de rappel

    // Execution de la requete
    res = curl_easy_perform(curl);
    if (res != CURLE_OK) {
      fprintf(stderr, "curl_easy_perform() failed: %s\n", curl_easy_strerror(res));
    }

    // Fermeture du fichier reponse
    fclose(response_file);

    // Liberation memoire
    curl_easy_cleanup(curl);

    result_fileJSON = fopen("resultat2.json", "r");
    if (result_fileJSON == NULL) {
      printf("Error opening file for reading.\n");
      return 1;
    }

    char buffer[2048];
    fread(buffer, 2048, 1, result_fileJSON);
    fclose(result_fileJSON);

    struct json_object *parsed_json;
    struct json_object *result;
    struct json_object *re;
    struct json_object *annee;
    struct json_object *s;
    int n_annee, i;

    parsed_json = json_tokener_parse(buffer);
    json_object_object_get_ex(parsed_json, "results", &result);
    n_annee = json_object_array_length(result);

    printf("annees \t Somme\n");
    for (i = 0; i < n_annee; i++) {
      re = json_object_array_get_idx(result, i);
      json_object_object_get_ex(re, "annee", &annee);
      json_object_object_get_ex(re, "s", &s);
      printf("%d \t %d\n", json_object_get_int(annee), json_object_get_int(s));
    }
  }

  return 0;
}