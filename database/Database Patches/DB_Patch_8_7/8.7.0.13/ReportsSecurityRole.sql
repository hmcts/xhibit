insert into xhb_security_role values('XHBReports','N');
commit;

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBReports','Y','Y');
commit;