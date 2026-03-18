Insert into XHB_SECURITY_ROLE (ROLE_NAME,SYSTEM_ROLE) values ('XHBAppealResultOrder','N');

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBAppealResultOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBAppealResultOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBAppealResultOrder','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBAppealResultOrder','Y','Y');
