<<<<<<< Updated upstream
# library(dplyr)
=======
library(dplyr)
>>>>>>> Stashed changes

#commune <- read.csv("communes.csv", sep = ";")
#immeuble <- read.csv("immeubles.csv", sep = ";")

<<<<<<< Updated upstream
merge(commune, immeubles, by="INSEE")
# commune %>% select('INSEE')
=======
fusion <- inner_join(communes, immeubles, by="INSEE")

library(ggplot2)

data("diamonds")

plot(diamonds$clarity, col="#A23456")
>>>>>>> Stashed changes
