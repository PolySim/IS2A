#include <stdio.h>
#include <curl/curl.h>

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

  curl = curl_easy_init();
  if (curl) {
    // Ouverture du fichier reponse
    response_file = fopen("resultat1.json", "wb");
    if (!response_file) {
      printf("Error opening file for writing.\n");
      return 1;
    }

    // Configuration de la requete
    curl_easy_setopt(curl, CURLOPT_URL, "https://opendata.paris.fr/api/explore/v2.1/catalog/datasets/liste_des_prenoms/records?where=prenoms%20%3D%20%22Alice%22&order_by=annee&limit=20"); // URL de la requete
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
  }

  return 0;
}