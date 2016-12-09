insert into csm_user_group (id, name, admin, chat) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', 1, 0);
insert into csm_role (id, name) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root');
insert into csm_permission (id, name, permission) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', '*');
insert into csm_user_group_role (id, user_group_id, role_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));
insert into csm_role_permission (id, role_id, permission_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));
insert into csm_user (id, username, password, salt, active, user_group_id) values(unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')), 'root', '84c59723099cc04ec1772ebc455643a0d29ce6e38c945f9cc9f759a6c3b21136', '31471bc8bc333a18', 1, unhex(replace('1db1a22e-757a-4113-b2a3-8797d91fc457','-','')));

insert into csm_permission (id, name, permission) values(unhex(replace('ba690893-ff53-4280-9919-59b150ff0eec','-','')), 'User management', 'user');
insert into csm_permission (id, name, permission) values(unhex(replace('1c01e7ab-fab4-4b1a-b9c1-a594c963ea13','-','')), 'User group management', 'userGroup');
insert into csm_permission (id, name, permission) values(unhex(replace('2118098e-6fe9-4127-bb7f-1c2e0677b665','-','')), 'Role management', 'role');
insert into csm_permission (id, name, permission) values(unhex(replace('b203dfb9-0bde-45b7-b9bb-949337000574','-','')), 'Permission management', 'permission');
insert into csm_permission (id, name, permission) values(unhex(replace('a9f7e473-6140-4fea-a67a-5b4fda0b38fd','-','')), 'Faq category management', 'faqCategory');
insert into csm_permission (id, name, permission) values(unhex(replace('9b3da7a4-b4c5-4d28-9a3f-dc766c6d6315','-','')), 'Faq management', 'faq');
insert into csm_permission (id, name, permission) values(unhex(replace('f672d155-072e-419d-815c-43f7ba6ef96a','-','')), 'Chat management', 'chat');
insert into csm_permission (id, name, permission) values(unhex(replace('4cbd16e8-070f-44cd-8b0c-ca0272561a18','-','')), 'Search index recreation', 'search:createIndex');