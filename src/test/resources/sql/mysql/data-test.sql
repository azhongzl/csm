insert into csm_user (id, username, password, salt, active, admin) values(unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), 'user', '1fbba1433812e09ca3ca25050d3e5bf3dd5a1739b82ae0293ecde54ec70ebbf7', '4124beff23b65c41', 1, 1);
insert into csm_user (id, username, password, salt, active, admin) values(unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), 'cust1', '1fbba1433812e09ca3ca25050d3e5bf3dd5a1739b82ae0293ecde54ec70ebbf7', '4124beff23b65c41', 1, 0);
insert into csm_user (id, username, password, salt, active, admin) values(unhex(replace('11d5f5f7-d6eb-1034-a268-c6b53a0158b7','-','')), 'cust2', '1fbba1433812e09ca3ca25050d3e5bf3dd5a1739b82ae0293ecde54ec70ebbf7', '4124beff23b65c41', 1, 0);

insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), 'General Ledger', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('11d5f5f7-d6eb-1034-a268-c6b53a0158b7','-','')), 'AP', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('19b7ca19-d6eb-1034-a268-c6b53a0158b7','-','')), 'AR', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('2189b421-d6eb-1034-a268-c6b53a0158b7','-','')), 'HR', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('2a18df1d-d6eb-1034-a268-c6b53a0158b7','-','')), 'EDI', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('347cbf6c-d6eb-1034-a268-c6b53a0158b7','-','')), 'Product & Kit', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('3cbef937-d6eb-1034-a268-c6b53a0158b7','-','')), 'Manufacture', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('43bdc050-d6eb-1034-a268-c6b53a0158b7','-','')), 'Order Status', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('4ca9a57d-d6eb-1034-a268-c6b53a0158b7','-','')), 'Inventory', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('53605f57-d6eb-1034-a268-c6b53a0158b7','-','')), 'Job Order', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('5b890a49-d6eb-1034-a268-c6b53a0158b7','-','')), 'Cust Service', '', 1, unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('629045d4-d6eb-1034-a268-c6b53a0158b7','-','')), 'RMA', '', 1, unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq_category (id, name, description, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('6b58cac2-d6eb-1034-a268-c6b53a0158b7','-','')), 'Marketing', '', 1, unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('490aa897-d6ea-1034-a268-c6b53a0158b7','-','')), now());

insert into csm_faq (id, category_id, question, answer, attachments, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), 'General Ledger question for Kuzcolighting 1', 'General Ledger answer 1', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq (id, category_id, question, answer, attachments, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('11d5f5f7-d6eb-1034-a268-c6b53a0158b7','-','')), unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), 'General Ledger question for LLC 2', 'General Ledger answer 2', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());
insert into csm_faq (id, category_id, question, answer, attachments, active, create_account_id, create_date_time, modify_account_id, modify_date_time) values(unhex(replace('19b7ca19-d6eb-1034-a268-c6b53a0158b7','-','')), unhex(replace('5a0452fe-d6ea-1034-a268-c6b53a0158b7','-','')), 'General Ledger question for world 3', 'General Ledger answer 3', '', 1, unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now(), unhex(replace('1c93b13d-d6ea-1034-a268-c6b53a0158b7','-','')), now());