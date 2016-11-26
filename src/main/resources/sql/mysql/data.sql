insert into csm_user_group (id, name, admin) values(unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), 'System', 1);

insert into csm_role (id, name) values(unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), 'SystemRole');

insert into csm_permission (id, name, permission) values(unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), 'Assign permission', 'itdoes');

insert into csm_user_group_role (id, user_group_id, role_id) values(unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')));

insert into csm_role_permission (id, role_id, permission_id) values(unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')));