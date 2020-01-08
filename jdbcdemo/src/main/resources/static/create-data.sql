create sequence customer_seq start with  100;
create sequence address_seq start with  100;
create sequence customer1_seq start with  100;
create sequence customer2_seq start with  100;
create table customer(pk bigint default customer_seq.nextval primary key , first_name varchar(255) , last_name varchar(255) , created time , updated time , customer_ext_id bigint) ;
insert into customer values(1,'sergio' , 'piana', null, null,null);
create table address(pk bigint default address_seq.nextval primary key , street varchar(255) , street_number varchar(255) , zip_code varchar(255), city varchar(255), customer_id bigint);
insert into address values(1,'Via di casa mia' , '11' , '20159' , 'Milano', 1);
insert into address values(2,'Via di casa mia' , '11' , '20159' , 'Milano', 1);

create table customer_ext(pk bigint default customer1_seq.nextval primary key , custom1 varchar(255) , custom2 varchar(255) );
//insert into customer_ext values(1,'custom1' , 'custom2' , 1);

create table customer_ext1(pk bigint default customer2_seq.nextval primary key , custom3 varchar(255) , custom4 varchar(255) );
//insert into customer_ext1 values(1,'custom3' , 'custom4' , 1);


create sequence roles_seq;
create table roles (pk bigint default roles_seq.nextval primary key , code varchar(255) , name varchar(255));
insert into roles values(1 , 'admin' , 'administrator');
insert into roles values (2 , 'user' , 'Standard user');


create sequence customer_role_rel_seq;
create table customer_role_rel (pk bigint default customer_role_rel_seq.nextval primary key , source_pk bigint , target_pk bigint , sequence double default 0 );
insert into customer_role_rel values(1, 1,1 ,0);
insert into customer_role_rel values(2, 1,2,0);


create table anagrafica(cod_ana varchar(10) primary key , description varchar(255) ) ;