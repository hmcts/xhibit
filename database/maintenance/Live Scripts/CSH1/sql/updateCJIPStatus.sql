set termout on;
update CJI_PARAMETER_VALUE set PARAMETER_VALUE='ONLINE' where PARAMETER_NAME='CJIP_STATUS';
update CJI_PARAMETER_VALUE set PARAMETER_VALUE=(select to_char(sysdate,'YYYY-MM-DD')||'T'||to_char(sysdate-1/720,'HH24:MI:SS')||'.0000000+01:00' from dual) where PARAMETER_NAME='CJIPLastMessageTime';
set termout off;
commit;
exit;

