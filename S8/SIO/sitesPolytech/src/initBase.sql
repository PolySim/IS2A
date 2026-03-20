insert into ecole(nom,url, latit, longit) values
('Angers','https://polytech-angers.fr/fr/index.html',47.4805797,-0.5945924);
insert into ecole(nom,url, latit, longit) values
  ('Annecy-Chambery','https://www.univ-smb.fr/polytech/', 45.91949018977083,6.1578369140625) ;
insert into ecole(nom,url, latit, longit) values
  ('Clermont','https://polytech-clermont.fr/',45.783088, 3.082352) ;
insert into ecole(nom,url, latit, longit) values
  ('Dijon','https://polytech.ube.fr/', 47.31227,5.07394);
insert into ecole(nom,url, latit, longit) values
  ('Grenoble','https://polytech.grenoble-inp.fr/',45.1935303594224,5.767650604248047) ;
insert into ecole(nom,url, latit, longit) values
  ('Lyon','https://polytech.univ-lyon1.fr/', 46.03704576329356,4.080069065093994) ;
insert into ecole(nom,url, latit, longit) values
  ('Marseille','https://polytech.univ-amu.fr/',43.3385460,5.4113996) ;
insert into ecole(nom,url, latit, longit) values
  ('Montpellier',' https://www.polytech.umontpellier.fr/',43.611944,3.877222) ;
insert into ecole(nom,url, latit, longit) values
('Nice-Sophia','https://polytech.univ-cotedazur.fr/',43.615, 7.071944 ) ;
insert into ecole(nom,url, latit, longit) values
  ('Orléans','https://www.univ-orleans.fr/fr/polytech',47.8444692, 1.9396871) ;
insert into ecole(nom,url, latit, longit) values
  ('Nancy','http://polytech-nancy.univ-lorraine.fr',48.68439,6.18496) ;
insert into ecole(nom,url, latit, longit) values
  ('Nantes','https://polytech.univ-nantes.fr/', 47.21806, -1.55278) ;
insert into ecole(nom,url, latit, longit) values
  ('Paris-Saclay','hhttps://www.polytech.universite-paris-saclay.fr/',48.70433003805475,2.1667098999023438) ;
insert into ecole(nom,url, latit, longit) values
  ('Paris-Sorbonne','https://www.polytech.sorbonne-universite.fr/',48.856578,2.351828) ;
insert into ecole(nom,url, latit, longit) values
  ('Tours','https://polytech.univ-tours.fr/',47.363536675068296,0.6873321533203125) ;



insert into domaine(id,nom) values
  (1, 'Eau, environnement, aménagement'),
  (2, 'Electronique et systèmes numériques'),
  (3, 'Energétique, génie des procédés'),
  (4, 'Génie biologique et alimentaire'),
  (5, 'Génie biomédical, instrumentation'),
  (6, 'Génie civil'),
  (7, 'Génie industriel') ,
  (8, 'Informatique'),
  (9, 'Matériaux') ,
  (10, 'Mathématiques appliquées et modélisation'),
  (11, 'Mécanique'),
  (12, 'Systèmes électriques') ;

  insert into domaine_ecole(domaine_id, ecoles_nom) values
    (1,'Annecy-Chambery'), (1,'Grenoble'),
    (1,'Montpellier'),(1,'Nice-Sophia'), (1, 'Orléans'),
    (1,'Paris-Sorbonne'),(1,'Tours') ;

  insert into domaine_ecole(domaine_id, ecoles_nom) values
    (2,'Annecy-Chambery'), (2,'Clermont'), (2,'Dijon'), (2,'Grenoble'),
    (2,'Marseille'),(2,'Montpellier'),(2,'Nancy'),(2,'Nantes') , (2,'Nice-Sophia'), (2, 'Orléans'),
    (2,'Paris-Saclay'),(2,'Paris-Sorbonne'),(2,'Tours') ;

 insert into domaine_ecole(domaine_id, ecoles_nom) values
    (3,'Annecy-Chambery'), (3,'Clermont'),
    (3,'Marseille'),(3,'Montpellier'),(3,'Nancy'),(3,'Nantes') ,  (3, 'Orléans');

 insert into domaine_ecole(domaine_id, ecoles_nom) values
    (4,'Clermont'),
    (4,'Marseille'),(4,'Montpellier'),(4,'Nantes') , (4,'Nice-Sophia'),
    (4,'Paris-Sorbonne') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (5,'Grenoble'),  (5,'Lyon'),
    (5,'Marseille'), (5, 'Orléans'),
    (5,'Paris-Saclay');

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (6,'Annecy-Chambery'), (6,'Clermont'), (6,'Grenoble'),
    (6,'Marseille'),(6,'Montpellier'),(6,'Nantes') , (6,'Nice-Sophia'), (6, 'Orléans'),
    (6,'Paris-Sorbonne') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (7,'Annecy-Chambery'), (7,'Clermont'), (7,'Dijon'), (7,'Grenoble'),
    (7,'Lyon'),(7,'Marseille'), (7,'Nancy'),(7, 'Orléans') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (8,'Annecy-Chambery'), (8,'Dijon'), (8,'Grenoble'),
    (8,'Lyon'),(8,'Marseille'),(8,'Montpellier'),(8,'Nancy'),(8,'Nantes') , (8,'Nice-Sophia'),
    (8,'Paris-Saclay'),(8,'Tours') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (9,'Annecy-Chambery'), (9,'Clermont'), (9,'Dijon'), (9,'Grenoble'),
    (9,'Lyon'),(9,'Marseille'),(9,'Montpellier'),(9,'Nantes') ,
    (9,'Paris-Saclay'),(9,'Paris-Sorbonne'),(9,'Tours') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (10,'Clermont'),
    (10,'Lyon') , (10,'Nice-Sophia'),(10,'Tours') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (11,'Annecy-Chambery'),
    (11,'Lyon'),(11,'Marseille'),(11,'Montpellier'),(11,'Nancy'), (11, 'Orléans'),
    (11,'Paris-Sorbonne'),(11,'Tours') ;

insert into domaine_ecole(domaine_id, ecoles_nom) values
    (12,'Clermont'),  (12,'Dijon'),
    (12,'Nantes') ,
    (12,'Paris-Saclay'),(12,'Tours') ;
