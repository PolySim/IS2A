# Réponses au TP5 - OrientDB

Voici les requêtes et commandes correspondantes pour réaliser le TP 5 sur OrientDB.

## I. Lancement du serveur OrientDB

Le dossier de l'archive a été décompressé. Pour lancer le serveur, exécutez dans un terminal :
```bash
cd /home/s/d/sdesdevi/IS2A/S8/noSQL/TP5/orientdb-community-3.2.53/bin
./server.sh
```
*(Le mot de passe root est `orient`)*

## II. Connexion à OrientDB à l'aide de la console

Dans un autre terminal :
```bash
./console.sh
```
Puis, pour vous connecter :
```orientdb
connect remote:localhost/demodb root orient
```

## III. Franchement, ça ressemble à du SQL !

**III.1.** Voir l'ensemble des classes :
```orientdb
classes
```

**III.2.** Informations sur la classe `Hotels` :
```orientdb
info class Hotels
```

**III.3.** Afficher les éléments de `Hotels` :
```orientdb
select * from Hotels
```

**III.4.** Augmenter la limite à 30 :
```orientdb
select * from Hotels limit 30
```

**III.5.** Compter le nombre d'éléments :
```orientdb
select count(*) from Hotels
```

**III.6.** Afficher l'Id et le Name :
```orientdb
select Id, Name from Hotels
```

**III.7.** Noms d'hôtels commençant par 'Ca' avec une regex :
```orientdb
select Name from Hotels where Name matches '^Ca.*'
```

**III.8.** Noms d'hôtels commençant par 'Ca' avec `left()` :
```orientdb
select Name from Hotels where Name.left(2) = 'Ca'
```

**III.9.** Nombre d'hôtels ayant '*****' dans leur nom :
```orientdb
select count(*) from Hotels where Name matches '.*[*][*][*][*][*].*'
```

**III.10.** Profils nés avant 1980, triés par nom et prénom :
```orientdb
select Name, Surname, Birthday from Profiles where Birthday < '1980-01-01' order by Name, Surname
```

## IV. Des records ? C’est nouveau ça

**IV.1.** Afficher le record numéro 2 (après un `select * from Hotels`) :
```orientdb
display record 2
```

**IV.2.** Afficher le profil numéro 2 :
```orientdb
select * from Profiles
display record 2
```

**IV.3.** Utiliser `load record` avec un `@rid` direct (en remplaçant `#X:Y` par le bon identifiant vu avec `display`) :
```orientdb
load record #X:Y
```

**IV.4.** Utiliser `select` avec l'identifiant :
```orientdb
select * from #X:Y
```

**IV.5.** Utiliser `expand()` :
```orientdb
select expand(#X:Y)
```

## V. Il y a même de l'héritage

**V.1.** Trouver les superclasses d'Hôtels :
```orientdb
info class Hotels
-- Regarder la mention "Superclasses" dans le retour de la commande (ex: Locations, etc)
```

**V.2.** Compter les sous-classes dans `Locations` :
```orientdb
select @class, count(*) from Locations group by @class
```

## VI. Les liaisons

**VI.1.** Informations sur `HasFriend` :
```orientdb
info class HasFriend
```

**VI.2.** Prénoms des deux profils amis via la classe `HasFriend` :
```orientdb
select in.Name, out.Name from HasFriend
```

**VI.3.** Prénom, nom et nombre d'amis :
```orientdb
select Name, Surname, in_HasFriend.size() as nb_amis from Profiles order by nb_amis desc
```

**VI.4.** Pareil avec la liste des prénoms des amis :
```orientdb
select Name, Surname, in_HasFriend.Name as list_amis from Profiles order by in_HasFriend.size() desc
```

## VII. Un peu de Match

**VII.1.** Profils s'appelant Samuel :
```orientdb
MATCH {class: Profiles, as: p, where: (Name = 'Samuel')} RETURN p.Name, p.Surname
```

**VII.2.** Samuel et ses amis :
```orientdb
MATCH {class: Profiles, as: p, where: (Name = 'Samuel')} -HasFriend- {as: friend} RETURN p.Name, p.Surname, friend.Name, friend.Surname
```

**VII.3.** Profils ayant à la fois un ami Samuel et une amie Ann :
```orientdb
MATCH {class: Profiles, as: p} -HasFriend- {as: samuel, where: (Name = 'Samuel')}, {as: p} -HasFriend- {as: ann, where: (Name = 'Ann')} RETURN p.Name, p.Surname
```

## VIII. Un peu de visualisation

**VIII.1.** Se rendre dans Studio (http://localhost:2480/), section Graph.

**VIII.2.** Exécuter les matches comme ci-dessus, mais en finissant par `RETURN $elements` ou `RETURN $pathElements`.

**VIII.3.** Amis de Santo OrientDB qui sont Customers :
```orientdb
MATCH {class: Customers, as: c} -HasFriend- {class: Profiles, as: p, where: (Name='Santo' AND Surname='OrientDB')} RETURN $elements
```

**VIII.4.** Profils ayant mangé au restaurant, visité le "Castello di Legri" et le monument "Redentore" :
```orientdb
MATCH 
  {class: Profiles, as: p} -HasEaten- {class: Restaurants}, 
  {as: p} -HasVisited- {class: Monuments, where: (Name = 'Castello di Legri')}, 
  {as: p} -HasVisited- {class: Monuments, where: (Name = 'Redentore')} 
RETURN p.Name
```

## IX. Et du coup, on aura quel genre de questions au TP noté ?

**IX.1.** Femme amie de Frank et Wayne :
```orientdb
MATCH 
  {class: Profiles, as: frank, where: (Name = 'Frank')} -HasFriend- {as: woman}, 
  {class: Profiles, as: wayne, where: (Name = 'Wayne')} -HasFriend- {as: woman} 
RETURN woman.Name, woman.Surname
```

**IX.2.** Gary ami avec Francis, qui lui vient d'Indonésie :
```orientdb
MATCH 
  {class: Profiles, as: gary, where: (Name = 'Gary')} -HasFriend- {as: francis, where: (Name = 'Francis')} -HasStayed- {class: Countries, where: (Name = 'Indonesia')} 
RETURN gary.Surname
```
*(Ajustez le nom du lien if `HasStayed` s'appelle différemment dans le schéma)*

**IX.3.** Les deux amis (Jersey / Zimbabwe) qui ont visité Castle Pergine et mangé au restaurant Imbarcadero :
```orientdb
MATCH 
  {class: Profiles, as: p1} -HasStayed- {class: Countries, where: (Name = 'Jersey')}, 
  {class: Profiles, as: p2} -HasStayed- {class: Countries, where: (Name = 'Zimbabwe')}, 
  {as: p1} -HasVisited- {class: Monuments, as: m, where: (Name = 'Castle Pergine')}, 
  {as: p2} -HasVisited- {as: m}, 
  {as: p1} -HasEaten- {class: Restaurants, as: r, where: (Name = 'Imbarcadero')}, 
  {as: p2} -HasEaten- {as: r} 
RETURN p1.Name, p1.Surname, p2.Name, p2.Surname
```
*(De même, vérifiez les noms exacts des liens sur le schéma : `HasStayed`, `IsFrom`, etc).*
