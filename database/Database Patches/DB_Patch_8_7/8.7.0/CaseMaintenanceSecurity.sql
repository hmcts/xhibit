insert into xhb_security_role values('XHBCreateCase','N');
insert into xhb_security_role values('XHBMaintainCase','N');
insert into xhb_security_role values('XHBDeleteCase','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBCreateCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBCreateCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBCreateCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBCreateCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBMaintainCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBMaintainCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBMaintainCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBMaintainCase','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBDeleteCase','Y','Y');
commit;
