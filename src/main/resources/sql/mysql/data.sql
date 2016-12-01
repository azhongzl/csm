insert into csm_user_group (id, name, admin, chat) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', 1, 0);

insert into csm_role (id, name) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root');

insert into csm_permission (id, name, permission) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', '*');

insert into csm_user_group_role (id, user_group_id, role_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));

insert into csm_role_permission (id, role_id, permission_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));

insert into csm_user (id, username, password, salt, active, user_group_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', '84c59723099cc04ec1772ebc455643a0d29ce6e38c945f9cc9f759a6c3b21136', '31471bc8bc333a18', 1, unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));