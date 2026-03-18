insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('Court Clerk', 'XHBAddIndictment', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('CS Admin', 'XHBAddIndictment', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('Court Clerk', 'XHBRemoveDeft', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('CS Admin', 'XHBRemoveDeft', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);

commit;
