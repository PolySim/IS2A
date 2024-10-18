install.packages("dplyr")
library(dplyr)

commune <- read.csv("communes.csv", sep = ";")
immeubles <- read.csv("immeubles.csv", sep = ";")

commune %>% select('INSEE')