# Commandes R

## Installation de packages

```r
install.packages("dplyr")
library(dplyr)
```

## Lecture d'un fichier

```r
commune <- read.csv("communes.csv", sep = ";")
immeubles <- read.csv("immeubles.csv", sep = ";")
```

## Gestion de colonnes    

```r
insee <- commune %>% select('INSEE') # Obligation d'avoir le package dplyr
commune <- commune %>% filter(INSEE %in% c("75056"))
```

## Structure de la table

```r
dim(commune) # Dimention de la table comme nb_lignes, nb_colonnes
str(commune)
head(commune) # 7 premières lignes
colnames(commune) # noms des colonnes
```

## Valeurs manquantes

```r
valeurs_manquantes <- which(is.na(communes), arr.ind = TRUE)
# Remplacer les valeurs manquantes
communes[11, 3] <- "Hello"
```

## JOIN

```r
fusion <- inner_join(commune, immeubles, by="ID") # ou merge(commune, immeuble, by="ID")
```

## Function like sql

```r
fusion %>% group_by(REG) %>% summarise(Pop_Moy=mean(PTOT, na.rm = T)) %>% arrange(desc(Pop_Moy)) # Groupe par région, calcul de la moyenne grâce à mean
```

## Creation de variables

```r
fusion <- fusion %>% mutate(DONATION=PTOT * 100) 
```

## Create graphique

```r
library(ggplot2)
data("diamonds")
plot(diamonds$clarity, col="#A23456", xlab = "Label X", ylab = "Label X", main = "Titre") # Choisi automatiquement le meilleur type de graphique

pie() # Création d'un graphique circulaire

```