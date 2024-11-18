# 3 Conclusion

Le terme flottant est un abbreviation pour nombre à <i style="color: #A23456">virgule</i> flottante. 
La représentation des flottants est fixée par la norme <i style="color: #A23456">IEEE 754</i>. 
Elle est consituée d'un bit de signe, d'un exposant et d'un <i style="color: #A23456">mantisse</i>, 
qui correspondent (en gros) à la suite des "décimales" en base deux du flottant.
Par exemple, sur un flottant 64 bits, elle comporte <i style="color: #A23456">52</i> bits, plus un bit
"gratuit", En plus des flottants normaux, il existe des flottants <i style="color: #A23456">spéciaux</i>.
 Par exemple, le calcul 1/0 produit le flottant <i style="color: #A23456">inf</i>;
le calcul √−1 produit le flottant <i style="color: #A23456">nan</i> (un sigle qui signifie "Not a Number").
Le paquetage <i style="color: #A23456">numpy</i> permet de préciser le type de flottant à utiliser.
Par exemple, le type flottant 64 bits se note <i style="color: #A23456">np.float64</i>.
Les calculs avec des flottants sont sujets aux erreurs <i style="color: #A23456">d'arrondi</i>, 
En particulier, il faut se méfier de la <i style="color: #A23456">comparaison</i> de deux flottants
de valeurs très <i style="color: #A23456">différentes</i>, des formules mathématiquement 
<i style="color: #A23456">complexe</i> ne le sont pas toujours avec des flottants et, dans
des cas extrêmes, une toute petite erreur d’arrondi sur les données initiales peut avoir des conséquences
arbitrairement grandes à la fin d’un long calcul. C’est ce qu’on appelle l’eﬀet 
<i style="color: #A23456">catastrophique</i>.