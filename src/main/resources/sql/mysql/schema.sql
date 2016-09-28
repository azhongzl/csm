drop table if exists faq;
drop table if exists category;
drop table if exists account;

create table account (
	id binary(16) not null,
	username varchar(100) not null,
	password char(64) not null,
	salt char(16) not null,
	active bit not null,
    primary key (id)
) engine=InnoDB;

create table category (
	id binary(16) not null,
	name varchar(100) not null,
	description varchar(255),
	active bit not null,
	create_account_id binary(16) not null,
	create_date timestamp not null,
	modify_account_id binary(16) not null,
	modify_date timestamp not null,
    primary key (id)
) engine=InnoDB;

create table faq (
	id binary(16) not null,
	category_id binary(16) not null,
	question varchar(255) not null,
	answer varchar(500) not null,
	keywords varchar(255) not null,
	attachments varchar(255),
	active bit not null,
	create_account_id binary(16) not null,
	create_date timestamp not null,
	modify_account_id binary(16) not null,
	modify_date timestamp not null,
    primary key (id)
) engine=InnoDB;