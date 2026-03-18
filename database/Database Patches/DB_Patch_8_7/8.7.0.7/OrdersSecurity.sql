insert into xhb_security_role values('XHBCreateList','N');
insert into xhb_security_role values('XHBMaintainList','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBCreateList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBMaintainList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBCreateList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBMaintainList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBCreateList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBMaintainList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBCreateList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBMaintainList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Usher','XHBCreateList','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Usher','XHBMaintainList','Y','Y');


commit;
