drop table if exists Client;
drop table if exists Produit;
drop table if exists Facture;
drop table if exists FacrureProduit;

create table Client (
    code varchar(64) primary key,
    nom varchar(64),
    prenom varchar(64)
);

create table Produit (
    code varchar(64) primary key,
    nom varchar(64),
    prix double precision,
    nombreEnStock integer
);

create table Facture (
    numero integer primary key,
    dateVente date
);

create table FactureProduit (
    refCodeProduit varchar(64) references Produit(code),
    refNumeroFacture integer references Facture(numero),
    nombreExemplaires integer,
    primary key (refCodeProduit, refNumeroFacture, nombreExemplaires)
);

insert into Client values ('C2', 'Doe', 'Joe');

insert into Produit values ('P1', 'Pain', 0.5, 100);
insert into Produit values ('P2', 'Lait', 1.5, 50);
insert into Produit values ('P3', 'Beurre', 2.5, 30);
insert into Produit values ('P4', 'Fromage', 3.5, 20);

insert into Facture values (1, '2020-01-01');

insert into FactureProduit values ('P1', 1, 2);
insert into FactureProduit values ('P2', 1, 1);


