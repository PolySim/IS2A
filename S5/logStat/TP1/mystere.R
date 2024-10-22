library(dplyr)

data("iris")
iris

# Nombre moyen deshabitants par region.
resultat1<- iris %>% group_by(Species) %>% summarise(Longueur_Moyenne_PETAL=mean(Sepal.Length,na.rm=T))

# Commune ayant le plus des habitants.
resultat2<- iris %>% arrange(desc(Sepal.Length))