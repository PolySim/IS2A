drop table if exists passe;
drop table if exists bus;
drop table if exists station;

create table bus
(
    id  int primary key,
    nom text
);

create table station
(
    id  int primary key,
    nom text
);

create table passe
(
    ref_bus     int references bus (id),
    ref_station int references station (id),
    primary key (ref_bus, ref_station)
);

insert into bus (id, nom)
values (1, 'F45'),
       (2, 'C04'),
       (3, 'D21');

insert into station (id, nom)
values (1, 'Bois'),
       (2, 'Mairie'),
       (3, 'Poste'),
       (4, 'Plage'),
       (5, 'Port');

insert into passe (ref_bus, ref_station)
values (1, 1),
       (1, 2),
       (1, 5),
       (2, 4),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 4),
       (3, 5),
       (3, 3);

/*
select max(id) as max_id
from bus;
*/

/*
create or replace function next_bus_id() returns int
as
$$
select max(id) + 1 as max_id
from bus
$$
    language 'sql';
*/

create or replace function next_bus_id() returns int
as
$$
declare
    max_id int;
begin
    select max(id) + 1
    into max_id
    from bus;
    return max_id;
end
$$
    language 'plpgsql';

/*select next_bus_id();*/

create or replace function nb_stations(id_bus int) returns int
as
$$
select count(ref_station)
from passe
where ref_bus = id_bus;
$$
    language 'sql';

/*select nom, nb_stations(id)
from bus;
*/

create or replace function liste_bus(id_station int) returns text
as
$$
declare
    list_bus text;
    declare
    bus      record;
begin
    list_bus := '';
    for bus in
        select b.nom
        from bus b
                 join passe p on b.id = p.ref_bus
        where p.ref_station = id_station
        loop
            list_bus := list_bus || bus.nom || ', ';
        end loop;
    return rtrim(list_bus, ', ');
end;
$$
    language 'plpgsql';

/*
select id, liste_bus(id)
from station;
*/

create or replace procedure ajouter_bus(nom text, id int)
as
$$
insert into bus
VALUES (id, nom);
$$
    language 'sql';

create or replace procedure ajouter_bus(bus_name text)
as
$$
declare
    id_max int;
begin
    perform * from bus where nom = bus_name;
    if found then
        raise exception 'Le bus % existe déjà', bus_name;
    end if;
    select max(id) + 1 into id_max from bus;
    insert into bus VALUES (id_max, bus_name);
end;
$$
    language 'plpgsql';



call ajouter_bus('G35');


create or replace function print_length_name_bus() returns trigger as
$$
begin
    raise notice 'Le nom du bus % a une longueur de %', new.nom, length(new.nom);
    return new;
end;
$$
    language 'plpgsql';

create trigger print_length_name_bus
    before update or insert
    on bus
    for each row
execute procedure print_length_name_bus();


create or replace function bus_name_regex() returns trigger as
$$
begin
    if not textregexeq(new.nom, '^([A-Z]|[a-z])[0-9]{2}$') then
        raise exception 'Le nom du bus % ne respecte pas le format', new.nom;
    end if;
    return new;
end;
$$
    language 'plpgsql';

create trigger bus_name_regex
    before insert or update
    on bus
    for each row
execute procedure bus_name_regex();