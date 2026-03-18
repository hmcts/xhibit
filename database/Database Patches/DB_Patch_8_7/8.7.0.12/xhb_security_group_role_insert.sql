insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)  
values ('CS Admin', 'XHBMaintainReferenceData', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)
values ('Court Clerk', 'XHBMaintainReferenceData', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)
values ('After Trials Clerk','XHBMaintainReferenceData', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default, last_update_date, creation_date, created_by, last_updated_by, version)
values ('List Officer','XHBMaintainReferenceData', 'Y','Y', sysdate, sysdate, 'XHIBIT', 'XHIBIT', 1);

commit;