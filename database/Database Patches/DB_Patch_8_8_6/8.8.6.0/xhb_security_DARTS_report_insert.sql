insert into xhb_security_role (role_name, system_role) values ('XHBReportsDARTS', 'N');

insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('Court Clerk','XHBReportsDARTS','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('After Trials Clerk','XHBReportsDARTS','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('List Officer','XHBReportsDARTS','Y','Y');
insert into xhb_security_group_role (group_name, role_name, is_enabled, is_enabled_by_default) values ('CS Admin','XHBReportsDARTS','Y','Y');

COMMIT;