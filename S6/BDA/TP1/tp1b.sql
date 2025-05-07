DROP table if exists Affectation;
DROP table if exists Employe;
DROP table if exists Projet;

CREATE TABLE Projet
(
    nProj    VARCHAR(10) PRIMARY KEY,
    nomProj  VARCHAR(30),
    nbEmpMax INTEGER
);

CREATE TABLE Employe
(
    nEmp      VARCHAR(10) PRIMARY KEY,
    nomEmp    VARCHAR(30),
    prenomEmp VARCHAR(30),
    temps     INTEGER
);

CREATE TABLE Affectation
(
    nProj    VARCHAR(10) REFERENCES Projet,
    nEmp     VARCHAR(10) references Employe,
    pourcent INTEGER,
    PRIMARY KEY (nProj, nEmp)
);


insert into Projet (nProj, nomProj, nbEmpMax)
values ('pr001', 'Creation SI Auchan', 1),
       ('pr002', 'Rehabilitation SI BNPP', 3),
       ('pr003', 'Evolution BD La Redoute', 4);

insert into Employe (nEmp, nomEmp, prenomEmp, temps)
values ('em01', 'Dupont', 'Laurent', 70),
       ('em02', 'Durant', 'Marie', 100);

insert into Affectation
values ('pr001', 'em01', 20),
       ('pr003', 'em01', 40),
       ('pr002', 'em02', 30);


create or replace function affiche_affectation(numEmp varchar(10)) returns bool
as
$$
declare
    emp     record;
    project record;
begin
    select * into emp from Employe where nEmp = numEmp;
    if not found then
        return false;
    end if;
    raise notice 'employe : %', numEmp;
    raise notice ' %  %', emp.nomEmp, emp.prenomEmp;
    raise notice '  Travaillant à  %', emp.temps;
    for project in select *
                   from Projet
                            join Affectation on Projet.nProj = Affectation.nProj
                   where Affectation.nEmp = numEmp
        loop
            raise notice '-> Projet % : %', project.nProj, project.nomProj;
            raise notice ' -> quotite : %', project.pourcent;
        end loop;
    return true;
end;
$$
    language 'plpgsql';

/*select affiche_affectation('em01');*/

create or replace function isPossibleAddEmp() returns trigger
as
$$
declare
    project record;
    nbEmp   int;
begin
    select * into project from Projet where nProj = new.nProj;
    select count(*) into nbEmp from Affectation where nProj = new.nProj;
    if nbEmp >= project.nbEmpMax then
        raise exception 'Impossible d''ajouter l''employé % au projet %', new.nEmp, new.nProj;
    end if;
    return new;
end;
$$
    language 'plpgsql';

create trigger isPossibleAddEmp
    before insert or update
    on Affectation
    for each row
execute procedure isPossibleAddEmp();

/*
insert into Affectation
values ('pr001', 'em02', 20);
*/

/*create or replace function empCanAffection(new record) returns bool
as
$$
declare
    employee record;
    nbTemps  int;
begin
    select * into employee from Employe where nEmp = new.nEmp;
    select sum(pourcent) into nbTemps from Affectation where nEmp = new.nEmp;
    if (employee.temps < nbTemps + new.pourcent) then
        return false;
    end if;
    return true;
end;
$$
    language 'plpgsql';*/

create or replace function empCanAffection() returns trigger
as
$$
declare
    employee record;
    nbTemps  int;
begin
    select * into employee from Employe where nEmp = new.nEmp;
    select sum(pourcent) into nbTemps from Affectation where nEmp = new.nEmp;
    if (employee.temps < nbTemps + new.pourcent) then
        raise exception 'Impossible d''ajouter l''employé % au projet %', new.nEmp, new.nProj;
    end if;
    return new;
end;
$$
    language 'plpgsql';

create trigger empCanAffection
    before insert or update
    on Affectation
    for each row
execute procedure empCanAffection();

/*
insert into Affectation
values ('pr002', 'em01', 30);
*/

create or replace procedure affecte_auto(numProj varchar(10))
as
$$
declare
    nb_employee_restant int;
    nb_employee_already int;
    employee            record;
begin
    select nbEmpMax into nb_employee_restant from Projet where nProj = numProj;
    select count(*) into nb_employee_already from Affectation where nProj = numProj;
    nb_employee_restant := nb_employee_restant - nb_employee_already;
    while nb_employee_restant > 0
        loop
            select *
            into employee
            from Employe
            where nEmp not in (select nEmp from Affectation where nProj = numProj)
            limit 1;
            if not found then
                raise notice 'Aucun employé disponible pour le projet %', numProj;
                return;
            end if;
            BEGIN
                insert into Affectation
                values (numProj, employee.nEmp, 10);
                nb_employee_restant := nb_employee_restant - 1;
            EXCEPTION
                WHEN unique_violation THEN
                    raise notice 'L''employé % est déjà affecté au projet %', employee.nEmp, numProj;
                    continue;
            END;
        end loop;
end;
$$
    language 'plpgsql';

call affecte_auto('pr003');