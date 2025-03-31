#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <json-c/json.h>

int main(int argc, char **argv)
{
  char* filename = malloc(strlen(argv[1]) + 5);
  char buffer[2048];
  strcpy(filename, argv[1]);
  strcat(filename, ".json");

  FILE *file = fopen(filename, "r");
  if (file == NULL)
  {
    printf("Erreur d'ouverture du fichier %s\n", filename);
    return 1;
  }

  struct json_object *parsed_json;
  struct json_object *automaton;
  int n_auto, i, n_trans, j;
  struct json_object *error;

  struct json_object *automaton_1;
  struct json_object *id;
  struct json_object *isInitial;
  struct json_object *isFinal;
  struct json_object *transitionsOut;

  struct json_object *transitionsOut_1;
  struct json_object *nextState;
  struct json_object *terminal;

  fread(buffer, 2048, 1, file);
  fclose(file);

  parsed_json = json_tokener_parse(buffer);
  json_object_object_get_ex(parsed_json, "automaton", &automaton);
  n_auto = json_object_array_length(automaton);
  json_object_object_get_ex(parsed_json, "error", &error);

  printf("digraph automate {\n");
  printf("\trankdir=LR;\n\n");

  for (i = 0; i < n_auto; i++) {
    automaton_1 = json_object_array_get_idx(automaton, i);
    json_object_object_get_ex(automaton_1, "id", &id);
    json_object_object_get_ex(automaton_1, "isInitial", &isInitial);
    json_object_object_get_ex(automaton_1, "isFinal", &isFinal);

    if (json_object_get_boolean(isFinal)) printf("\t\tnode [shape = doublecircle]; %s;\n", json_object_get_string(id));
    else printf("\t\tnode [shade = circle]; %s ;\n", json_object_get_string(id));

    if (json_object_get_boolean(isInitial)) {
      printf("\t\tnode [shape = point]; init ;\n");
      printf("\t\tinit -> %s;\n", json_object_get_string(id));
    }
  }

  printf("\n");

  for (i = 0; i < n_auto; i++) {
    automaton_1 = json_object_array_get_idx(automaton, i);
    json_object_object_get_ex(automaton_1, "id", &id);
    json_object_object_get_ex(automaton_1, "transitionsOut", &transitionsOut);
    n_trans = json_object_array_length(transitionsOut);

    for (j = 0; j < n_trans; j++) {
      transitionsOut_1 = json_object_array_get_idx(transitionsOut, j);
      json_object_object_get_ex(transitionsOut_1, "nextState", &nextState);
      json_object_object_get_ex(transitionsOut_1, "terminal", &terminal);
      printf("\t\t%s -> %s [ label = \"%s\" ];\n", json_object_get_string(id), json_object_get_string(nextState), json_object_get_string(terminal));
    }
  }

  printf("}\n");

  free(filename);

  return 0;
}