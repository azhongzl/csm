drop table if exists csm_chat_message;
drop table if exists csm_faq;
drop table if exists csm_faq_category;
drop table if exists csm_role_permission;
drop table if exists csm_user_role;
drop table if exists csm_permission;
drop table if exists csm_role;
drop table if exists csm_user;

create table csm_user (
	id binary(16) not null,
	username varchar(100) not null,
	password char(64) not null,
	salt char(16) not null,
	active bit not null,
	admin bit not null,
    primary key (id)
) engine=InnoDB;

create table csm_role (
	id binary(16) not null,
	name varchar(100) not null,
    primary key (id)
) engine=InnoDB;

create table csm_permission (
	id binary(16) not null,
	name varchar(100) not null,
	permission varchar(100) not null,
    primary key (id)
) engine=InnoDB;

create table csm_user_role (
	id binary(16) not null,
	user_id binary(16) not null,
	role_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

create table csm_role_permission (
	id binary(16) not null,
	role_id binary(16) not null,
	permission_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

create table csm_faq_category (
	id binary(16) not null,
	name varchar(100) not null,
	description varchar(255),
	active bit not null,
	create_account_id binary(16) not null,
	create_date_time timestamp not null,
	modify_account_id binary(16) not null,
	modify_date_time timestamp not null,
    primary key (id)
) engine=InnoDB;

create table csm_faq (
	id binary(16) not null,
	category_id binary(16) not null,
	question varchar(255) not null,
	answer varchar(500) not null,
	attachments varchar(255),
	active bit not null,
	create_account_id binary(16) not null,
	create_date_time timestamp not null,
	modify_account_id binary(16) not null,
	modify_date_time timestamp not null,
    primary key (id)
) engine=InnoDB;

create table csm_chat_message (
	id binary(16) not null,
	room_id binary(16) not null,
	sender_id binary(16) not null,
	create_date_time timestamp not null,
	from_admin bit not null,
	message varchar(500) not null,
    primary key (id)
) engine=InnoDB;