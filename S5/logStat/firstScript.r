# library(dplyr)

commune <- read.csv("communes.csv", sep = ";")
immeubles <- read.csv("immeubles.csv", sep = ";")

merge(commune, immeubles, by="INSEE")
# commune %>% select('INSEE')