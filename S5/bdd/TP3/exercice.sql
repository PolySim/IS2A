/* 1 */
SELECT * FROM film;

/* 2 */
SELECT UPPER(abonne.nomab || ' ' || abonne.prenomab) as NomPrenom FROM abonne;

/* 3 */
SELECT film.titre FROM film WHERE film.anneeproduction < 2005;

/* 4 */
SELECT film.titre
FROM film
WHERE film.anneeproduction >= 2005 and film.anneeproduction <= 2010
ORDER BY film.titre;

/* 5 */
SELECT film.titre
FROM film
WHERE lower(film.titre) LIKE 'p%';

/* 6 */
SELECT film.titre
FROM film
WHERE lower(film.titre) LIKE '%se%';

/* 7 with join */
SELECT distinct categorie.libelle
FROM categorie
RIGHT JOIN film f on categorie.ncat = f.ncat;

/* 7 with subquery */
SELECT categorie.libelle
FROM categorie
WHERE categorie.ncat IN (SELECT distinct film.ncat FROM film);

/* 8 */
SELECT count(distinct film.ncat) as nbCategories
FROM film;

/* 9 */
SELECT film.ncat, count(film.ncat) as nbFilm
FROM film
GROUP BY film.ncat;

/* 10 */
SELECT count(distinct emprunt.*) * 1.0 / count(distinct abonne.*) as moyenneEmpruntParAbonne
FROM emprunt, abonne;

/* 11 */
SELECT film.titre
FROM film
LEFT JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame';

/* 12 */
SELECT film.titre, film.anneeproduction
FROM film
WHERE film.realisateur = 'Tim Burton';

/* 13 */
SELECT count(*) as nbFilm
FROM film
WHERE film.realisateur = 'Clint Eastwood';

/* 14 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
LEFT JOIN dvd on film.nfilm = dvd.nfilm
WHERE film.realisateur = 'Woody Allen'
GROUP BY film.titre;

/* 15 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
LEFT JOIN dvd on film.nfilm = dvd.nfilm
GROUP BY film.titre
HAVING count(dvd.*) > 1;

/* 16 */
SELECT film.titre, count(dvd.*) as nbDvd
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
GROUP BY film.titre;

/* 17 */
SELECT abonne.nomab, abonne.prenomab
FROM abonne
JOIN emprunt on abonne.nab = emprunt.nab
JOIN dvd on emprunt.ndvd = dvd.ndvd
JOIN film on dvd.nfilm = film.nfilm
WHERE film.titre = 'Pulp Fiction';

/* 18 */
SELECT film.titre
FROM film
JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame' OR categorie.libelle = 'Action';

/* 19 */
SELECT distinct film.titre
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
JOIN emprunt on dvd.ndvd = emprunt.ndvd
JOIN abonne on emprunt.nab = abonne.nab
WHERE abonne.nomab = 'Dupont' AND abonne.prenomab = 'Marie';

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

/* 21 */
SELECT film.titre
FROM film
JOIN categorie on film.ncat = categorie.ncat
WHERE categorie.libelle = 'Drame' AND film.realisateur = 'Woody Allen';

/* 22 */
SELECT distinct film.titre
FROM film
JOIN dvd on film.nfilm = dvd.nfilm
WHERE dvd.anneeachat = 2009 AND dvd.moisachat = 8;

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

/* 25 */
WITH minBurton as (
    SELECT min(film.anneeproduction) as minBurton
    FROM film
    WHERE film.realisateur = 'Tim Burton'
)

SELECT distinct film.titre
FROM film
WHERE film.anneeproduction < (SELECT minBurton FROM minBurton);

/* 26 */
SELECT distinct film.titre
FROM film
WHERE not exists(
    SELECT *
    FROM dvd
    WHERE dvd.nfilm = film.nfilm
);