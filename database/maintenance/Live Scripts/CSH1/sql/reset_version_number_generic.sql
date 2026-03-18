set termout off;
set verify off;
ALTER TRIGGER &1 DISABLE;
set termout on;
update &2 set version=1 where version > 95000;
set termout off;
commit;
ALTER TRIGGER &1 ENABLE;
commit;
exit;

