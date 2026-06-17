insert into question (qno, libelle, active) values (1, 'Quel est votre langage prefere ?', false);
insert into question (qno, libelle, active) values (2, 'Quelle base de donnees preferez-vous ?', false);
insert into question (qno, libelle, active) values (3, 'Souhaitez-vous plus de TP Spring ?', false);

insert into choix (cno, libchoix, statut, nb_choix, qno) values (1, 'Java', true, 0, 1);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (2, 'Python', false, 0, 1);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (3, 'JavaScript', false, 0, 1);

insert into choix (cno, libchoix, statut, nb_choix, qno) values (4, 'H2', true, 0, 2);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (5, 'PostgreSQL', false, 0, 2);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (6, 'MongoDB', false, 0, 2);

insert into choix (cno, libchoix, statut, nb_choix, qno) values (7, 'Oui', true, 0, 3);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (8, 'Non', false, 0, 3);
insert into choix (cno, libchoix, statut, nb_choix, qno) values (9, 'Sans avis', false, 0, 3);
