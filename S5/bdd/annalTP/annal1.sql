/* 1 */
SELECT bar.nom
FROM bar
WHERE exists(
    SELECT *
    FROM amateur
    WHERE amateur.bar_favori = bar.num_b
)
ORDER BY bar.nom;

/* 2 */
SELECT biere.designation
FROM biere
JOIN sert on sert.code = biere.code
WHERE sert.num_b = 3;

/* 3 */
SELECT amateur.nom, COUNT(aime.*) as nb_aime
FROM amateur
JOIN aime on amateur.num_a = aime.num_a
GROUP BY amateur.nom;

/* 4 */
SELECT distinct bar.nom
FROM bar
JOIN sert on bar.num_b = sert.num_b
WHERE exists(
    SELECT *
    FROM amateur
    JOIN aime on amateur.num_a = aime.num_a
    WHERE
        amateur.nom = 'Simon' AND amateur.prenom = 'Paul'
        AND aime.code = sert.code
);

/* 5 */
SELECT AVG(sert.prix) as Prix_moyen
FROM sert
JOIN biere on sert.code = biere.code
WHERE biere.designation = 'Maredsous';

/* 6 */
SELECT amateur.nom, amateur.prenom
FROM amateur
WHERE not exists(
    SELECT *
    FROM biere
    WHERE not exists(
        SELECT *
        FROM aime
        WHERE
            aime.code = biere.code
            AND aime.num_a = amateur.num_a
    )
);

/* 7 */
WITH biere_servi as (
    SELECT count(*) as number, bar.num_b
    FROM bar
    JOIN sert on bar.num_b = sert.num_b
    GROUP BY bar.num_b
)

SELECT bar.num_b
FROM bar
JOIN biere_servi on biere_servi.num_b = bar.num_b
WHERE biere_servi.number > 3;

/* 8 */
SELECT biere.designation, AVG(aime.note) as moyenne_note
FROM biere
LEFT JOIN aime on biere.code = aime.code
GROUP BY biere.designation;

/* 9 */
SELECT biere.designation
FROM biere
JOIN aime on biere.code = aime.code
JOIN amateur on aime.num_a = amateur.num_a
WHERE amateur.nom = 'Springsteen' AND amateur.prenom = 'Bruce' AND exists(
    SELECT *
    FROM sert
    WHERE sert.code = biere.code AND amateur.bar_favori = sert.num_b
);

/* 10 */
UPDATE bar SET nom = 'Bar au mètre' WHERE nom = 'Baratin';