/* 1 */
SELECT * FROM film;
/*
 nfilm |             titre             | anneeproduction |           realisateur            | ncat
-------+-------------------------------+-----------------+----------------------------------+------
     1 | Good Morning England          |            2009 | Richard Curtis                   |    3
     2 | La-Haut                       |            2009 | Pete Docter                      |    1
     3 | Inglorious Basterds           |            2009 | Quentin Tarantino                |    6
     4 | The Reader                    |            2009 | Stephen Daldry                   |    5
     5 | Very Bad Trip                 |            2009 | Todd Philips                     |    3
     6 | Whatever Works                |            2009 | Woody Allen                      |    3
     7 | Gran Torino                   |            2009 | Clint Eastwood                   |    2
     8 | Il faut sauver le soldat Ryan |            1998 | Steven Spielberg                 |    6
     9 | Pulp Fiction                  |            1994 | Quentin Tarantino                |    2
    10 | Le Terminal                   |            2004 | Steven Spielberg                 |    5
    11 | Vicky Cristina Barcelona      |            2008 | Woody Allen                      |    3
    12 | Reservoir Dogs                |            1992 | Quentin Tarantino                |    2
    13 | Kill Bill Vol 1               |            2003 | Quentin Tarantino                |    2
    14 | Scoop                         |            2006 | Woody Allen                      |    3
    15 | L echange                     |            2008 | Clint Eastwood                   |    5
    16 | Kill Bill Vol 2               |            2004 | Quentin Tarantino                |    2
    17 | Taken                         |            2009 | Pierre Morel                     |    2
    18 | Charlie et la chocolaterie    |            2005 | Tim Burton                       |    3
    19 | Dark Shadows                  |            2010 | Tim Burton                       |    7
    20 | Public Enemies                |            2009 | Michael Mann                     |    2
    21 | Pirates des Caraibes 4        |            2010 | Rob Marshall                     |    3
    22 | Princesse Mononoke            |            2000 | Hayao Miyazaki                   |    1
    23 | Gladiator                     |            2000 | Ridley Scott                     |    2
    24 | La Vie des autres             |            2007 | Florian Henckel von Donnersmarch |    5
    25 | Ratatouille                   |            2007 | Brad Bird                        |    1
    26 | Va, vis et deviens            |            2003 | Radu Mihaileanu                  |    5
    27 | Slumdog Millionaire           |            2009 | Danny Boyle                      |    5
    28 | Frozen River                  |            2009 | Courtney Hunt                    |    5
    29 | Persepolis                    |            2007 | Marjane Satrapi                  |    1
    30 | Sixieme Sens                  |            2000 | M. Night Shyamalan               |    2
*/

/* 2 */
SELECT UPPER(abonne.nomab || ' ' || abonne.prenomab) as NomPrenom FROM abonne;
/*
    nomprenom
------------------
 DUPONT MARIE
 COHEN RÉBECCA
 NGUYEN LI
 YU HOA
 MARTIN DAMIEN
 DURANT SOPHIE
 ABDALLAH YOUSSEF
 BENATI RAMZI
 DUPONT STÉPHANE
 LEVY SIMON
 LI ZU
 MARQUIS CLOE
(12 lignes)
*/

/* 3 */
SELECT film.titre FROM film WHERE film.anneeproduction < 2005;
/*
             titre
-------------------------------
 Il faut sauver le soldat Ryan
 Pulp Fiction
 Le Terminal
 Reservoir Dogs
 Kill Bill Vol 1
 Kill Bill Vol 2
 Princesse Mononoke
 Gladiator
 Va, vis et deviens
 Sixieme Sens
(10 lignes)

*/

/* 4 */
SELECT film.titre
FROM film
WHERE film.anneeproduction >= 2005 and film.anneeproduction <= 2010
ORDER BY film.titre;
/*
           titre
----------------------------
 Charlie et la chocolaterie
 Dark Shadows
 Frozen River
 Good Morning England
 Gran Torino
 Inglorious Basterds
 La-Haut
 La Vie des autres
 L echange
 Persepolis
 Pirates des Caraibes 4
 Public Enemies
 Ratatouille
 Scoop
 Slumdog Millionaire
 Taken
 The Reader
 Very Bad Trip
 Vicky Cristina Barcelona
 Whatever Works
(20 lignes)
*/

/* 5 */
SELECT film.titre
FROM film
WHERE lower(film.titre) LIKE 'p%';
/*
         titre
------------------------
 Pulp Fiction
 Public Enemies
 Pirates des Caraibes 4
 Princesse Mononoke
 Persepolis
(5 lignes)

*/

/* 6 */
SELECT film.titre
FROM film
WHERE lower(film.titre) LIKE '%se%';
/*
       titre
--------------------
 Reservoir Dogs
 Princesse Mononoke
 Persepolis
 Sixieme Sens
(4 lignes)
*/

/* 7 with join */
SELECT distinct categorie.libelle
FROM categorie
RIGHT JOIN film f on categorie.ncat = f.ncat;
/*
   libelle
--------------
 Dessin Anime
 Comedie
 Action
 Horreur
 Drame
 Historique
(6 lignes)
*/

/* 7 with subquery */
SELECT categorie.libelle
FROM categorie
WHERE categorie.ncat IN (SELECT distinct film.ncat FROM film);
/*
   libelle
--------------
 Dessin Anime
 Action
 Comedie
 Drame
 Historique
 Horreur
(6 lignes)
*/

/* 8 */
SELECT count(distinct film.ncat) as nbCategories
FROM film;
/*
 nbcategories
--------------
            6
(1 ligne)
*/

/* 9 */
SELECT film.ncat, count(film.ncat) as nbFilm
FROM film
GROUP BY film.ncat;
/*
 ncat | nbfilm
------+--------
    3 |      7
    5 |      7
    6 |      2
    2 |      9
    7 |      1
    1 |      4
(6 lignes)

*/

/* 10 */
SELECT count(distinct emprunt.*) * 1.0 / count(distinct abonne.*) as moyenneEmpruntParAbonne
FROM emprunt, abonne;
/*
 moyenneempruntparabonne
-------------------------
      2.7500000000000000
(1 ligne)
 */

/* 11 */
SELECT film.titre
FROM film
LEFT JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame';
/*
        titre
---------------------
 The Reader
 Le Terminal
 L echange
 La Vie des autres
 Va, vis et deviens
 Slumdog Millionaire
 Frozen River
(7 lignes)

*/

/* 12 */
SELECT film.titre, film.anneeproduction
FROM film
WHERE film.realisateur = 'Tim Burton';
/*
           titre            | anneeproduction
----------------------------+-----------------
 Charlie et la chocolaterie |            2005
 Dark Shadows               |            2010
(2 lignes)
*/

/* 13 */
SELECT count(*) as nbFilm
FROM film
WHERE film.realisateur = 'Clint Eastwood';
/*
 nbfilm
--------
      2
(1 ligne)
 */

/* 14 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
LEFT JOIN dvd on film.nfilm = dvd.nfilm
WHERE film.realisateur = 'Woody Allen'
GROUP BY film.titre;
/*
         titre           | nbdvd
--------------------------+-------
 Scoop                    |     1
 Vicky Cristina Barcelona |     2
 Whatever Works           |     1
(3 lignes)
         */

/* 15 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
LEFT JOIN dvd on film.nfilm = dvd.nfilm
GROUP BY film.titre
HAVING count(dvd.*) > 1;
/*
          titre           | nbdvd
--------------------------+-------
 Dark Shadows             |     2
 La-Haut                  |     2
 Vicky Cristina Barcelona |     2
 L echange                |     2
 The Reader               |     2
 Persepolis               |     2
 Princesse Mononoke       |     2
 La Vie des autres        |     2
 Taken                    |     4
 Inglorious Basterds      |     2
 Slumdog Millionaire      |     3
 Good Morning England     |     3
 Pirates des Caraibes 4   |     2
 Kill Bill Vol 2          |     2
 Gran Torino              |     4
(15 lignes)
*/

/* 16 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
GROUP BY film.titre;
/*
             titre             | nbdvd
-------------------------------+-------
 Dark Shadows                  |     2
 La-Haut                       |     2
 Whatever Works                |     1
 Le Terminal                   |     1
 Vicky Cristina Barcelona      |     2
 Pulp Fiction                  |     1
 L echange                     |     2
 Va, vis et deviens            |     1
 The Reader                    |     2
 Persepolis                    |     2
 Ratatouille                   |     1
 Frozen River                  |     1
 Very Bad Trip                 |     1
 Princesse Mononoke            |     2
 La Vie des autres             |     2
 Taken                         |     4
 Il faut sauver le soldat Ryan |     1
 Reservoir Dogs                |     1
 Inglorious Basterds           |     2
 Slumdog Millionaire           |     3
 Good Morning England          |     3
 Pirates des Caraibes 4        |     2
 Scoop                         |     1
 Kill Bill Vol 2               |     2
 Kill Bill Vol 1               |     1
 Charlie et la chocolaterie    |     1
 Gran Torino                   |     4
 Sixieme Sens                  |     1
 Public Enemies                |     1
(29 lignes)
*/

/* 17 */
SELECT abonne.nomab, abonne.prenomab
FROM abonne
JOIN emprunt on abonne.nab = emprunt.nab
JOIN dvd on emprunt.ndvd = dvd.ndvd
JOIN film on dvd.nfilm = film.nfilm
WHERE film.titre = 'Pulp Fiction';
/*
 nomab  | prenomab
--------+----------
 Cohen  | Rébecca
 Durant | Sophie
(2 lignes)
 */

/* 18 */
SELECT film.titre
FROM film
JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame' OR categorie.libelle = 'Action';
/*
        titre
---------------------
 The Reader
 Gran Torino
 Pulp Fiction
 Le Terminal
 Reservoir Dogs
 Kill Bill Vol 1
 L echange
 Kill Bill Vol 2
 Taken
 Public Enemies
 Gladiator
 La Vie des autres
 Va, vis et deviens
 Slumdog Millionaire
 Frozen River
 Sixieme Sens
(16 lignes)
*/

/* 19 */
SELECT distinct film.titre
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
JOIN emprunt on dvd.ndvd = emprunt.ndvd
JOIN abonne on emprunt.nab = abonne.nab
WHERE abonne.nomab = 'Dupont' AND abonne.prenomab = 'Marie';
/*
        titre
----------------------
 Frozen River
 Good Morning England
 Persepolis
(3 lignes)
*/

/* 20 */
SELECT film.titre
FROM film
WHERE not exists(
    SELECT *
    FROM dvd
    WHERE dvd.nfilm = film.nfilm and exists(
        SELECT *
        FROM emprunt
        WHERE emprunt.ndvd = dvd.ndvd
    )
);
/*
             titre
-------------------------------
 La-Haut
 The Reader
 Very Bad Trip
 Il faut sauver le soldat Ryan
 Le Terminal
 Taken
 Charlie et la chocolaterie
 Dark Shadows
 Public Enemies
 Pirates des Caraibes 4
 Princesse Mononoke
 Gladiator
 La Vie des autres
 Ratatouille
 Va, vis et deviens
 Slumdog Millionaire
 Sixieme Sens
(17 lignes)
*/

/* 21 */
SELECT film.titre
FROM film
JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame' AND film.realisateur = 'Woody Allen';
/*
 titre
-------
(0 ligne)
 */

/* 22 */
SELECT distinct film.titre
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
WHERE dvd.anneeachat = 2009 AND dvd.moisachat = 8;
/*
        titre
---------------------
 Gran Torino
 La-Haut
 Slumdog Millionaire
(3 lignes)
*/

/* 23 */
SELECT distinct abonne.nomab, abonne.prenomab
FROM abonne
WHERE abonne.nab in (
    SELECT emprunt.nab
    FROM emprunt
    JOIN dvd on emprunt.ndvd = dvd.ndvd
    JOIN film on dvd.nfilm = film.nfilm
    WHERE film.realisateur = 'Woody Allen' OR film.realisateur = 'Tim Burton'
);
/*
 nomab  | prenomab
--------+----------
 Benati | Ramzi
 Cohen  | Rébecca
 Martin | Damien
(3 lignes)
*/

/* 24 */
SELECT distinct film.titre
FROM film
WHERE not exists(
    SELECT *
    FROM abonne
    WHERE not exists(
        SELECT *
        FROM emprunt
        JOIN dvd on emprunt.ndvd = dvd.ndvd
        WHERE dvd.nfilm = film.nfilm and emprunt.nab = abonne.nab
    )
);
/*
        titre
----------------------
 Good Morning England
(1 ligne)
*/

/* 25 */
WITH minBurton as (
    SELECT min(film.anneeproduction) as minBurton
    FROM film
    WHERE film.realisateur = 'Tim Burton'
)

SELECT distinct film.titre
FROM film
WHERE film.anneeproduction < (SELECT minBurton FROM minBurton);
/*
             titre
-------------------------------
 Il faut sauver le soldat Ryan
 Reservoir Dogs
 Le Terminal
 Pulp Fiction
 Va, vis et deviens
 Kill Bill Vol 2
 Kill Bill Vol 1
 Sixieme Sens
 Gladiator
 Princesse Mononoke
(10 lignes)
*/

/* 26 */
SELECT distinct film.titre
FROM film
WHERE not exists(
    SELECT *
    FROM dvd
    WHERE dvd.nfilm = film.nfilm
);
/*
   titre
-----------
 Gladiator
(1 ligne)
*/

/* 27 */
SELECT a.nomab, a.prenomab
FROM abonne as a
WHERE not exists(
    SELECT *
    FROM film as f
    WHERE realisateur = 'Quentin Tarantino'
    AND not exists(
        SELECT *
        FROM abonne
        JOIN emprunt on abonne.nab = emprunt.nab
        JOIN dvd on emprunt.ndvd = dvd.ndvd
        JOIN film on dvd.nfilm = film.nfilm
        WHERE film.realisateur = 'Quentin Tarantino'
        AND a.nab = abonne.nab
    )
);
/*
 nomab  | prenomab
--------+----------
 Cohen  | Rébecca
 Durant | Sophie
(2 lignes)
 */

/* 28 */
SELECT film.titre
FROM film
WHERE anneeproduction = 2008
OR anneeproduction = 2009;
/*
          titre
--------------------------
 Good Morning England
 La-Haut
 Inglorious Basterds
 The Reader
 Very Bad Trip
 Whatever Works
 Gran Torino
 Vicky Cristina Barcelona
 L echange
 Taken
 Public Enemies
 Slumdog Millionaire
 Frozen River
(13 lignes)
*/

/* 29 */
SELECT film.titre
FROM film
WHERE exists(
    SELECT *
    FROM dvd
    WHERE dvd.nfilm = film.nfilm
    AND dvd.anneeachat = film.anneeproduction
);
/*
        titre
----------------------
 Good Morning England
 La-Haut
 The Reader
 Very Bad Trip
 Whatever Works
 Gran Torino
 Reservoir Dogs
 Kill Bill Vol 1
 L echange
 Kill Bill Vol 2
 Taken
 Dark Shadows
 Public Enemies
 La Vie des autres
 Ratatouille
 Va, vis et deviens
 Slumdog Millionaire
 Frozen River
(18 lignes)
*/

/* 30 */
SELECT film.titre
FROM film
WHERE anneeproduction != 2007
AND anneeproduction != 2008
AND anneeproduction != 2009;
/*
             titre
-------------------------------
 Il faut sauver le soldat Ryan
 Pulp Fiction
 Le Terminal
 Reservoir Dogs
 Kill Bill Vol 1
 Scoop
 Kill Bill Vol 2
 Charlie et la chocolaterie
 Dark Shadows
 Pirates des Caraibes 4
 Princesse Mononoke
 Gladiator
 Va, vis et deviens
 Sixieme Sens
(14 lignes)
*/

/* 31 */
SELECT distinct f_realisateur.realisateur
FROM film as f_realisateur
WHERE not exists(
    SELECT *
    FROM film
    WHERE
        film.realisateur = f_realisateur.realisateur
        AND film.anneeproduction = 2009
);
/*
           realisateur
----------------------------------
 Radu Mihaileanu
 Marjane Satrapi
 M. Night Shyamalan
 Brad Bird
 Hayao Miyazaki
 Steven Spielberg
 Ridley Scott
 Tim Burton
 Florian Henckel von Donnersmarch
 Rob Marshall
(10 lignes)
*/