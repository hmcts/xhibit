insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('List Officer', 'XHBManualCaseLink', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('Court Clerk', 'XHBManualCaseUnLink', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);

commit;
