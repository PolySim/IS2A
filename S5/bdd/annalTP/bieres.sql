drop table sert ;
drop table aime ;
drop table biere ;
drop table amateur ;
drop table Bar ;

create table bar (
  num_b integer primary key,
  nom varchar(30) not null,
  adresse varchar(100) not null
) ;

create table amateur (
  num_a integer primary key,
  prenom varchar(30) not null,
  nom varchar(30) not null,  
  bar_favori integer not null references Bar
) ;

create table biere (
  code integer primary key,
  designation varchar(30) unique not null
) ;

create table aime (
  num_a integer references amateur,
  code integer references biere,
  note integer check (note >=1 and note <=10),
  primary key(num_a, code)
) ;

create table sert (
  num_b integer references bar,
  code integer references biere,
  prix  float not null,
  primary key(num_b,code)
) ;

insert into bar(num_b, nom, adresse) values (1,'Bar des anges', '3 rue des poissons');
insert into bar values (2, 'Baratin' , '6 rue des lilas');
insert into bar values (3, 'Bar parallele', '5 rue corot');
insert into bar values (4, 'Bar a mine', '3 avenue foch') ;
insert into bar values (5, '10 puissance 5', 'Polytech Lille') ;

insert into amateur(num_a,prenom, nom,bar_favori) values (1, 'Paul', 'Simon', 1);
insert into amateur values (2, 'Bruce', 'Springsteen', 4);
insert into amateur values (3, 'Johnny', 'Cash', 2);
insert into amateur values (4, 'Harvey', 'Keitel', 4);
insert into amateur values (5, 'Matt', 'Damon', 1) ;
insert into amateur values (6, 'Harper', 'Simon', 2);
insert into amateur values (7, 'Jeremie', 'Dequidt', 5);

insert into biere (code, designation) values  (1, 'Troll');
insert into biere values (2,'Hommelbier');
insert into biere values (3, 'Chti');
insert into biere values (4, 'Maredsous');
insert into biere values (5, 'Grimbergen') ;
insert into biere values (6, 'Karmeliet');
insert into biere values (7, 'Choulette');
insert into biere values (8, 'Rince Cochon');
insert into biere values (9, 'Jupiler');

insert into aime values (1, 3, 6);
insert into aime values (1, 4, 5);
insert into aime values (2, 1, 8);
insert into aime values (2, 4, 9);
insert into aime values (2, 2, 4);
insert into aime values (3, 1, 5);
insert into aime values (3, 2, 7);
insert into aime values (3, 3, 6);
insert into aime values (3, 4, 8);
insert into aime values (3, 5, 2);
insert into aime values (4, 3, 6);
insert into aime values (4, 4, 7);
insert into aime values (5, 1, 10);
insert into aime values (7, 9, 10);

insert into sert(num_b,code, prix) values (1, 2, 4.5);
insert into sert values (1, 3, 6);
insert into sert values (1, 5,3.7);
insert into sert values (2, 1,4.5);
insert into sert values (2, 2, 3.5);
insert into sert values (2, 4, 5);
insert into sert values (3, 1, 4.5);
insert into sert values (3, 3, 3.7);
insert into sert values (2, 5, 3);
insert into sert values (4, 1, 3.8);
insert into sert values (4, 3, 3.9);
insert into sert values (4, 4, 5.5);
insert into sert values (4,5, 3.8) ;
insert into sert values (2, 7, 4.75);
insert into sert values (3, 7, 4.5);
insert into sert values (5, 9, 2.5);


