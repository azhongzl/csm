drop table if exists csm_user_group;
create table csm_user_group (
	id binary(16) not null,
	name varchar(100) not null,
	admin bit not null,
	chat bit not null,
	super_id binary(16),
    primary key (id)
) engine=InnoDB;

drop table if exists csm_role;
create table csm_role (
	id binary(16) not null,
	name varchar(100) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_permission;
create table csm_permission (
	id binary(16) not null,
	name varchar(100) not null,
	permission varchar(255) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_user_group_role;
create table csm_user_group_role (
	id binary(16) not null,
	user_group_id binary(16) not null,
	role_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_role_permission;
create table csm_role_permission (
	id binary(16) not null,
	role_id binary(16) not null,
	permission_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_user;
create table csm_user (
	id binary(16) not null,
	username varchar(100) not null,
	password char(64) not null,
	salt char(16) not null,
	active bit not null,
	user_group_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_faq_category;
create table csm_faq_category (
	id binary(16) not null,
	name varchar(100) not null,
	description varchar(255),
	create_user_id binary(16) not null,
	create_date_time timestamp not null,
	modify_user_id binary(16) not null,
	modify_date_time timestamp not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_faq;
create table csm_faq (
	id binary(16) not null,
	category_id binary(16) not null,
	question varchar(255) not null,
	answer varchar(500) not null,
	attachments varchar(255),
	active bit not null,
	create_user_id binary(16) not null,
	create_date_time timestamp not null,
	modify_user_id binary(16) not null,
	modify_date_time timestamp not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_chat_message;
create table csm_chat_message (
	id binary(16) not null,
	room_id binary(16) not null,
	sender_id binary(16) not null,
	create_date_time timestamp not null,
	from_admin bit not null,
	message varchar(500) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_chat_customer_user_group;
create table csm_chat_customer_user_group (
	id binary(16) not null,
	customer_user_id binary(16) not null,
	user_group_id binary(16) not null,
	operator_user_id binary(16) not null,
    primary key (id)
) engine=InnoDB;

drop table if exists csm_chat_unhandled_customer;
create table csm_chat_unhandled_customer (
	id binary(16) not null,
	user_id binary(16) not null,
	create_date_time timestamp not null,
    primary key (id)
) engine=InnoDB;