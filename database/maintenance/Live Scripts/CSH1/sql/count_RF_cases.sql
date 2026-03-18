set termout off;
spool &1
select count(*) from xhb_case where charge_import_indicator='RF' and trunc(last_update_date) = trunc(sysdate);
exit;
