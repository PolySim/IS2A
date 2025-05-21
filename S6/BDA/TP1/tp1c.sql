drop sequence if exists seq_simple;
drop sequence if exists seq_start10;
drop sequence if exists seq_start2by2;
drop sequence if exists seq_start10dec;
drop sequence if exists seq_cycle;
drop table if exists voiture;
drop table if exists voiture_log;
drop sequence if exists voiture_seq;

create sequence voiture_seq
    start with 0
    increment by 1
    minvalue 0
    /*maxvalue 4*/
    maxvalue 456975999
    cycle
;

create or replace function int2siv(number integer) returns text
as
$$
declare
    result text;
begin
    result := '';
    result := chr(65 + (number::INTEGER) % 26) || result;
    result := '-' || chr(65 + (number::INTEGER / 26) % 26) || result;
    result := '-' || to_char((number::INTEGER / 26 / 26) % 1000, 'fm000') || result;
    result := chr(65 + (number::INTEGER / 26 / 26 / 1000) % 26) || result;
    result := chr(65 + (number::INTEGER / 26 / 26 / 1000 / 26) % 26) || result;
    return result;
end;
$$
    language 'plpgsql';

create table voiture
(
    /*id  varchar(10) primary key default 'v' || to_char(nextval('voiture_seq'), 'fm000'),*/
    id  varchar(10) primary key default int2siv(nextval('voiture_seq')::INTEGER),
    nom text
);

create table voiture_log
(
    id               varchar(10),
    nom              text,
    date_suppression timestamp default now()
);

create sequence seq_simple;

/*select nextval('seq_simple');*/

create sequence seq_start10
    start with 10;

/*select nextval('seq_start10');*/

create sequence seq_start2by2
    start with 2
    increment by 2;

/*
select nextval('seq_start2by2');
select nextval('seq_start2by2');
*/

create sequence seq_start10dec
    start with 10
    increment by -1
    maxvalue 10
;
/*
select nextval('seq_start10dec');
select nextval('seq_start10dec');
*/

create sequence seq_cycle
    start with 2
    minvalue 2
    maxvalue 4
    cycle
;
/*
select nextval('seq_cycle');
select nextval('seq_cycle');
select nextval('seq_cycle');
select nextval('seq_cycle');
select nextval('seq_cycle');
select nextval('seq_cycle');
*/

create or replace function insert_voiture() returns trigger
as
$$
declare
    first_voiture record;
    nb_voiture    int;
begin
    /*select count(*) into nb_voiture from voiture;
    if (nb_voiture < 4) then
        return new;
    end if;
    select * into first_voiture from voiture limit 1;
    insert into voiture_log (id, nom) values (first_voiture.id, first_voiture.nom);
    delete from voiture where id = first_voiture.id;*/
    return new;
end;
$$
    language 'plpgsql';

create trigger insert_voiture
    before insert
    on voiture
    for each row
execute procedure insert_voiture()
;

insert into voiture (nom)
values ('porche'),
       ('twingo'),
       ('clio'),
       ('audi'),
       ('mercedes')
;


select *
from voiture;


/*
select *
from voiture_log;
*/

/*
26*26*1000*26*26=456_976_000
*/

/*
select int2siv(0);
select int2siv(456975999);
select int2siv(126186513);
select int2siv(114920367);
*/