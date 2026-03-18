insert into xhb_security_role values('XHBOrdersCopyOrder','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBOrdersCopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBOrdersCopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBOrdersCopyOrder','Y','Y');
commit;

insert into xhb_security_role values('XHBD20CopyOrder','N');
insert into xhb_security_role values('XHBD20CreateOrder','N');
insert into xhb_security_role values('XHBD20ViewOrder','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBD20CopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBD20CopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBD20CopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBD20CreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBD20CreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBD20CreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBD20ViewOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBD20ViewOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBD20ViewOrder','Y','Y');

commit;