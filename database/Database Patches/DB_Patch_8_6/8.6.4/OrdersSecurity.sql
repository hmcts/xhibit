insert into xhb_security_role values('XHBMonetaryOrdersCopyOrder','N');
insert into xhb_security_role values('XHBMonetaryOrdersCreateOrder','N');
insert into xhb_security_role values('XHBMonetaryOrdersViewOrder','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBMonetaryOrdersCopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBMonetaryOrdersCopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBMonetaryOrdersCopyOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBMonetaryOrdersCreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBMonetaryOrdersCreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBMonetaryOrdersCreateOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBMonetaryOrdersViewOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBMonetaryOrdersViewOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBMonetaryOrdersViewOrder','Y','Y');
commit;
