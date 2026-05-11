# TP1 : Données Manquantes

## W. Heyse

1- Chargez le jeu de données mtcars compris dans R avec la commande data(mtcars). On ne s'intéressera qu'au 7 premières colonnes. Faites-en une rapide analyse descriptive.

2- Simulez trois jeux de données comportant chacun environ 20% de données manquantes sur les trois variables de votre choix, hors mpg. Chaque jeu correspondra à une des trois typologies de données manquantes : MCAR, MAR, MNAR.

3- Pour chacun des jeux de données créés, visualisez les données manquantes. Il faudra à minima présenter la matrice des données manquantes et les différences de distribution entre les données manquantes et non manquantes. Décrivez les indices de chaque typologie de données manquantes.

4- En utilisant la fonction imputePCA du package missMDA. Analysez l'impact de l'imputation sur les données initiales (distribution des variables, différences en terme de norme, ...).

5- Ajustez un modèle de régression expliquant la variable mpg appris sur chacun des jeux de données. Comparez les résultats obtenus sur un modèle appris sur les données complètes (variance des estimateurs).

6- À l'aide de la fonction mice du package mice, réalisez une imputation multiple des trois jeux de données. Combien de jeu de données générez-vous ?

7- À l'aide de la fonction densityplot, comparez les valeurs imputées et les valeurs observées (vous pourrez aussi reproduire ce graphe en ajoutant la densité du jeu de données complet, surtout pour les données MNAR). Analysez ces graphes, semble-t-il y avoir de grandes différences entre les valeurs imputées et observées ?

8- Sur les jeux de données imputés par mice, à l'aide de la fonction with ajustez le même modèle de régression que précédemment et poolez les résultats avec la fonction pool. Comparez les variances inter-imputation et variance totale (poolée).

9- Comparez les paramètres obtenus : coefficients, variance aux paramètres du modèle obtenu sur les données complètes pour chaque typologie de données manquantes.
