# Zoe LAVIEILLE - Simon DESDEVISES

library(dplyr)

# Question 1

dim(data)
# > dim(data)
#[1] 700  14
# Il y a donc 700 iseaux inscrit dans la table

# Question 2

data %>% 
  filter(NUMERO_INVENTAIRE2 %in% c("MDG-O-02030") | NUMERO_INVENTAIRE1 %in% c("MDG-O-02030")) %>%
  select(NOM_LATIN)
#> data %>% 
#  +   filter(NUMERO_INVENTAIRE2 %in% c("MDG-O-02030") | NUMERO_INVENTAIRE1 %in% c("MDG-O-02030")) %>%
#  +   select(NOM_LATIN)
# A tibble: 1 × 1
#NOM_LATIN         
#<chr>             
#  1 Prunella modularis
# Le nom latin de MDG-0-02030 est Prunella modularis 

# Question 3
data %>% filter(NOM_FRANCAIS == "Guifette noire")
# NUMERO_INVENTAIRE2 = MDG-O-01057
data <- data %>% 
  mutate(NOM_FRANCAIS = replace(NOM_FRANCAIS, NOM_FRANCAIS == "Guifette noire", "Titi Polytechus IS2Aius"))
data %>% filter(NUMERO_INVENTAIRE2 == "MDG-O-01057") %>% select(NOM_FRANCAIS)
# > data %>% filter(NUMERO_INVENTAIRE2 == "MDG-O-01057") %>% select(NOM_FRANCAIS)
## A tibble: 1 × 1
#NOM_FRANCAIS           
#<chr>                  
#  1 Titi Polytechus IS2Aius


# Question 4
data %>% 
  count(VILLE_DE_DECOUVERTE) %>% 
  arrange(desc(n)) %>% 
  slice(1) %>%
  select(VILLE_DE_DECOUVERTE)
# > data %>% 
#+   count(VILLE_DE_DECOUVERTE) %>% 
#  +   slice(1) %>%
#  +   select(VILLE_DE_DECOUVERTE)
# A tibble: 1 × 1
#VILLE_DE_DECOUVERTE
#<chr>              
#  1 Hendaye 
# La ville est donc Hendaye

# Question 5
colnames(data)
data %>% filter(FAMILLE == "Gaviidés") %>% filter(ETAT_CONSERVATION == "Bon Etat") %>% count(FAMILLE)
# Renvoi 4
data %>% filter(FAMILLE == "Linaria cannabina") %>% filter(ETAT_CONSERVATION == "Mauvais") %>% count(FAMILLE)
data %>% filter(FAMILLE == "Haematopus ostralegus") %>% filter(ETAT_CONSERVATION == "Mauvais") %>% count(FAMILLE)
# Renvoit qu'il n'y a aucun oiseaux de ces familles

# Question 6

# Question 7

# A
choix <- c("champignons", "sauce", "fromage")
ingredients <- sample(choix, size=700, replace=TRUE)
head(ingredients)
# > choix <- c("champignons", "sauce", "fromage")
# > ingredients <- sample(choix, size=700, replace=TRUE)
# > head(ingredients)
# [1] "champignons" "sauce"       "sauce"       "sauce"       "fromage"     "sauce"    

# B
data$ingredients <- ingredients
colnames(data)
# > colnames(data)
#[1] "NUMERO_INVENTAIRE1"  "NUMERO_INVENTAIRE2"  "NOM_LATIN"          
#[4] "NOM_FRANCAIS"        "SEXE"                "PARTICULARITE"      
#[7] "AGE"                 "FAMILLE"             "VILLE_DE_DECOUVERTE"
#[10] "PAYS_DE_DECOUVERTE"  "DATE_DE_COLLECTE"    "ETAT_CONSERVATION"  
#[13] "ESTIMATION"          "PRIX_ACHAT"          "ingredients" 

# Question 8

# A
data %>% filter(PRIX_ACHAT > ESTIMATION) %>% count()
# > data %>% filter(PRIX_ACHAT > ESTIMATION) %>% count()
# A tibble: 1 × 1
#n
#<int>
#  1   581
# Il y a 581 oiseaux acheté plus cher que l'estimation

# B
# Il y a surement eu une vente aux enchères et donc le prix augmente plus fort que la valeur réel

# C
data %>% group_by(FAMILLE) %>% summarise(prix_median = median(PRIX_ACHAT))
# > data %>% group_by(FAMILLE) %>% summarise(prix_median = median(PRIX_ACHAT))
# A tibble: 54 × 2
#FAMILLE        prix_median
#<chr>                <dbl>
#  1 Accipitridés        40454.
#2 Acrocephalidés      37718.
#3 Aegithalidés        56123.
#4 Alaudidés           23896.
#5 Alcidés             46702.
#6 Anatidés            71563.
#7 Apodidés            15121.
#8 Ardéidés            64167.
#9 Burhinidés         144206.
#10 Caprimulgidés       29544.
# ℹ 44 more rows
# ℹ Use `print(n = ...)` to see more rows

# D

data %>%
  group_by(FAMILLE) %>%
  summarise(prix_median = median(PRIX_ACHAT)) %>%
  arrange(prix_median) %>%
  slice(1)
# > data %>%
#+   group_by(FAMILLE) %>%
#  +   summarise(prix_median = median(PRIX_ACHAT)) %>%
#  +   arrange(prix_median) %>%
#  +   slice(1)
# A tibble: 1 × 2
#FAMILLE        prix_median
#<chr>                <dbl>
#  1 Procellariidés       5951.

# E

# Le prix median signifie que le 

# F

data %>%
  group_by(FAMILLE) %>%
  summarise(prix_median = median(PRIX_ACHAT)) %>%
  arrange(prix_median)

# 1 Procellariidés       5951.
#2 Rallidés             6445.
#3 Frégatidés          12011.
#4 Apodidés            15121.
#5 Gruidés             21432.
#6 Tytonidés           21599.
#7 Fringillidés        22300.
#8 Alaudidés           23896.
#9 Charadriidés        24006.
#10 Pélécanidés         27868.
# Ce sont les 10 familles les moins chère, il ne doit donc pas les manger
