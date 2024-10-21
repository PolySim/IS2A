--
-- Structure de la table abonne
--
-- Drop Table emprunt;
-- Drop table dvd ;
-- Drop Table film;
-- Drop Table categorie;
-- Drop Table abonne;

CREATE TABLE abonne (
   nab integer NOT NULL,
   nomab varchar(40) NOT NULL default '',
   prenomab varchar(40) NOT NULL default '',
   PRIMARY KEY (nab)
);

--
-- Contenu de la table abonne
--


INSERT INTO abonne VALUES (1, 'Dupont', 'Marie');
INSERT INTO abonne VALUES (2, 'Cohen', 'Rébecca');
INSERT INTO abonne VALUES (3, 'NGuyen', 'Li');
INSERT INTO abonne VALUES (4, 'Yu', 'Hoa');
INSERT INTO abonne VALUES (5, 'Martin', 'Damien');
INSERT INTO abonne VALUES (6, 'Durant', 'Sophie');
INSERT INTO abonne VALUES (7, 'Abdallah', 'Youssef');
INSERT INTO abonne VALUES (8, 'Benati', 'Ramzi');
INSERT INTO abonne VALUES (9, 'Dupont', 'Stéphane');
INSERT INTO abonne VALUES (10, 'Levy', 'Simon');
INSERT INTO abonne VALUES (11, 'Li', 'Zu');
INSERT INTO abonne VALUES (12, 'Marquis', 'Cloe');

--
-- Structure de la table categorie
--

create table categorie (
  ncat integer NOT NULL,
  libelle varchar(15) not null,
  PRIMARY KEY (ncat)
);

--
-- Contenu de la table categorie
--

INSERT INTO categorie VALUES (1, 'Dessin Anime');
INSERT INTO categorie VALUES (2, 'Action');
INSERT INTO categorie VALUES (3, 'Comedie');
INSERT INTO categorie VALUES (4, 'Romance');
INSERT INTO categorie VALUES (5, 'Drame');
INSERT INTO categorie VALUES (6, 'Historique');
INSERT INTO categorie VALUES (7, 'Horreur');

--
-- Structure de la table film
--

CREATE TABLE film (
   nfilm integer NOT NULL,
   titre varchar(70) NOT NULL default '',
   anneeProduction integer,
   realisateur varchar(70),
   ncat integer constraint ncat references categorie(ncat),
   PRIMARY KEY (nfilm)
);


--
-- Contenu de la table film
--

INSERT INTO film VALUES (1, 'Good Morning England', 2009, 'Richard Curtis', 3);
INSERT INTO film VALUES (2, 'La-Haut', 2009, 'Pete Docter', 1);
INSERT INTO film VALUES (3, 'Inglorious Basterds', 2009, 'Quentin Tarantino', 6);
INSERT INTO film VALUES (4, 'The Reader', 2009, 'Stephen Daldry', 5);
INSERT INTO film VALUES (5, 'Very Bad Trip', 2009, 'Todd Philips', 3);
INSERT INTO film VALUES (6, 'Whatever Works', 2009, 'Woody Allen', 3);
INSERT INTO film VALUES (7, 'Gran Torino', 2009, 'Clint Eastwood', 2);
INSERT INTO film VALUES (8, 'Il faut sauver le soldat Ryan', 1998, 'Steven Spielberg', 6);
INSERT INTO film VALUES (9, 'Pulp Fiction', 1994, 'Quentin Tarantino', 2);
INSERT INTO film VALUES (10, 'Le Terminal', 2004, 'Steven Spielberg', 5);
INSERT INTO film VALUES (11, 'Vicky Cristina Barcelona', 2008, 'Woody Allen', 3);
INSERT INTO film VALUES (12, 'Reservoir Dogs', 1992, 'Quentin Tarantino', 2);
INSERT INTO film VALUES (13, 'Kill Bill Vol 1', 2003, 'Quentin Tarantino', 2);
INSERT INTO film VALUES (14, 'Scoop', 2006, 'Woody Allen', 3);
INSERT INTO film VALUES (15, 'L echange', 2008, 'Clint Eastwood', 5);
INSERT INTO film VALUES (16, 'Kill Bill Vol 2', 2004, 'Quentin Tarantino', 2);
INSERT INTO film VALUES (17, 'Taken', 2009, 'Pierre Morel', 2);
INSERT INTO film VALUES (18, 'Charlie et la chocolaterie', 2005, 'Tim Burton', 3);
INSERT INTO film VALUES (19, 'Dark Shadows', 2010, 'Tim Burton', 7);
INSERT INTO film VALUES (20, 'Public Enemies', 2009, 'Michael Mann', 2);
INSERT INTO film VALUES (21, 'Pirates des Caraibes 4', 2010, 'Rob Marshall', 3);
INSERT INTO film VALUES (22, 'Princesse Mononoke', 2000, 'Hayao Miyazaki', 1);
INSERT INTO film VALUES (23, 'Gladiator', 2000, 'Ridley Scott', 2);
INSERT INTO film VALUES (24, 'La Vie des autres', 2007, 'Florian Henckel von Donnersmarch', 5);
INSERT INTO film VALUES (25, 'Ratatouille', 2007, 'Brad Bird', 1);
INSERT INTO film VALUES (26, 'Va, vis et deviens', 2003, 'Radu Mihaileanu', 5);
INSERT INTO film VALUES (27, 'Slumdog Millionaire', 2009, 'Danny Boyle', 5);
INSERT INTO film VALUES (28, 'Frozen River', 2009, 'Courtney Hunt', 5);
INSERT INTO film VALUES (29, 'Persepolis', 2007, 'Marjane Satrapi', 1);
INSERT INTO film VALUES (30, 'Sixieme Sens', 2000, 'M. Night Shyamalan', 2);

--
-- Structure de la table dvd
--

CREATE TABLE dvd (
   ndvd integer NOT NULL,
   anneeAchat integer,
   moisAchat integer,
   nfilm integer constraint nfilm references film(nfilm),
   PRIMARY KEY (ndvd)
);


--
-- Contenu de la table dvd
--

INSERT INTO dvd VALUES (1, 2009, 3, 1);
INSERT INTO dvd VALUES (2, 2009, 3, 1);
INSERT INTO dvd VALUES (3, 2009, 7, 1);
INSERT INTO dvd VALUES (4, 2010, 1, 2);
INSERT INTO dvd VALUES (5, 2009, 8, 2);
INSERT INTO dvd VALUES (6, 2010, 3, 3);
INSERT INTO dvd VALUES (7, 2010, 3, 3);
INSERT INTO dvd VALUES (8, 2009, 11, 4);
INSERT INTO dvd VALUES (9, 2009, 12, 4);
INSERT INTO dvd VALUES (10, 2009, 10, 5);
INSERT INTO dvd VALUES (11, 2009, 9, 6);
INSERT INTO dvd VALUES (12, 2009, 8, 7);
INSERT INTO dvd VALUES (13, 2009, 8, 7);
INSERT INTO dvd VALUES (14, 1999, 5, 8);
INSERT INTO dvd VALUES (15, 1995, 4, 9);
INSERT INTO dvd VALUES (16, 2005, 11, 10);
INSERT INTO dvd VALUES (17, 2009, 1, 11);
INSERT INTO dvd VALUES (18, 2009, 5, 11);
INSERT INTO dvd VALUES (19, 1992, 6, 12);
INSERT INTO dvd VALUES (20, 2003, 4, 13);
INSERT INTO dvd VALUES (21, 2007, 2, 14);
INSERT INTO dvd VALUES (22, 2010, 1, 7);
INSERT INTO dvd VALUES (23, 2010, 2, 7);
INSERT INTO dvd VALUES (24, 2008, 12, 15);
INSERT INTO dvd VALUES (25, 2009, 1, 15);
INSERT INTO dvd VALUES (26, 2004, 7, 16);
INSERT INTO dvd VALUES (27, 2009, 4, 16);
INSERT INTO dvd VALUES (28, 2009, 9, 17);
INSERT INTO dvd VALUES (29, 2009, 9, 17);
INSERT INTO dvd VALUES (30, 2009, 11, 17);
INSERT INTO dvd VALUES (31, 2006, 3, 18);
INSERT INTO dvd VALUES (32, 2010, 8, 19);
INSERT INTO dvd VALUES (33, 2011, 1, 19);
INSERT INTO dvd VALUES (34, 2009, 9, 20);
INSERT INTO dvd VALUES (35, 2011, 5, 21);
INSERT INTO dvd VALUES (36, 2011, 5, 21);
INSERT INTO dvd VALUES (37, 2001, 3, 22);
INSERT INTO dvd VALUES (38, 2001, 1, 22);
INSERT INTO dvd VALUES (39, 2007, 5, 24);
INSERT INTO dvd VALUES (40, 2007, 10, 24);
INSERT INTO dvd VALUES (41, 2007, 11, 25);
INSERT INTO dvd VALUES (42, 2003, 9, 26);
INSERT INTO dvd VALUES (43, 2009, 8, 27);
INSERT INTO dvd VALUES (44, 2009, 9, 27);
INSERT INTO dvd VALUES (45, 2009, 8, 27);
INSERT INTO dvd VALUES (46, 2009, 12, 28);
INSERT INTO dvd VALUES (47, 2008, 1, 29);
INSERT INTO dvd VALUES (48, 2008, 1, 29);
INSERT INTO dvd VALUES (49, 2001, 4, 30);
INSERT INTO dvd VALUES (50, 2009, 10, 17);
--
-- Structure de la table emprunt
--

CREATE TABLE emprunt (
   nab integer NOT NULL constraint nab references abonne(nab),
   ndvd integer NOT NULL constraint ndvd references dvd(ndvd),
   datedeb date NOT NULL,
   datefin date,
   PRIMARY KEY (nab,ndvd,datedeb)
);

--
-- Contenu de la table emprunt
--
INSERT INTO emprunt VALUES (1, 1, '2009-04-01', '2009-04-04');
INSERT INTO emprunt VALUES (2, 2, '2009-04-01', '2009-04-05');
INSERT INTO emprunt VALUES (3, 3, '2009-07-01', '2009-07-03');
INSERT INTO emprunt VALUES (4, 1, '2009-04-06', '2009-04-10');
INSERT INTO emprunt VALUES (5, 2, '2009-05-01', '2009-05-02');
INSERT INTO emprunt VALUES (6, 3, '2009-08-22', '2009-08-24');
INSERT INTO emprunt VALUES (7, 3, '2009-07-16', '2009-07-25');
INSERT INTO emprunt VALUES (8, 2, '2009-06-01', '2009-06-04');
INSERT INTO emprunt VALUES (9, 2, '2009-06-13', '2009-06-17');
INSERT INTO emprunt VALUES (10, 1, '2009-03-21', '2009-03-23');
INSERT INTO emprunt VALUES (11, 1, '2009-10-08', '2009-10-12');
INSERT INTO emprunt VALUES (12, 1, '2009-05-25', '2009-05-31');
INSERT INTO emprunt VALUES (2, 6, '2010-04-01', '2010-04-04');
INSERT INTO emprunt VALUES (2, 15, '2009-12-12', '2009-12-13');
INSERT INTO emprunt VALUES (2, 19, '2004-05-03', '2004-05-04');
INSERT INTO emprunt VALUES (2, 20, '2003-11-12', '2003-12-15');
INSERT INTO emprunt VALUES (2, 26, '2004-07-02', '2004-07-03');
INSERT INTO emprunt VALUES (6, 7, '2010-04-01', '2010-04-04');
INSERT INTO emprunt VALUES (6, 15, '2009-12-10', '2009-12-12');
INSERT INTO emprunt VALUES (6, 19, '2004-05-13', '2004-05-14');
INSERT INTO emprunt VALUES (6, 20, '2003-08-12', '2003-08-15');
INSERT INTO emprunt VALUES (6, 27, '2004-07-02', '2004-07-03');
INSERT INTO emprunt VALUES (1, 47, '2008-08-12', '2008-08-15');
INSERT INTO emprunt VALUES (1, 46, '2010-07-02', '2010-07-03');
INSERT INTO emprunt VALUES (1, 47, '2009-03-20', '2009-03-21');
INSERT INTO emprunt VALUES (2, 11, '2009-09-20', '2009-09-21');
INSERT INTO emprunt VALUES (2, 24, '2009-04-20', '2009-04-21');
INSERT INTO emprunt VALUES (5, 24, '2009-09-20', '2009-09-21');
INSERT INTO emprunt VALUES (5, 17, '2009-01-20', '2009-01-23');
INSERT INTO emprunt VALUES (5, 21, '2008-01-20', '2008-01-23');
INSERT INTO emprunt VALUES (8, 17, '2009-02-20', '2009-02-23');
INSERT INTO emprunt VALUES (8, 12, '2009-08-24', '2009-08-27');
INSERT INTO emprunt VALUES (8, 24, '2009-08-24', '2009-08-27');
