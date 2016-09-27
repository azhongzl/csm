drop table if exists category;

create table category (
	id binary(16),
	name varchar(100) not null,
	description varchar(255),
	create_date timestamp,
	modify_date timestamp,
    primary key (id)
) engine=InnoDB;