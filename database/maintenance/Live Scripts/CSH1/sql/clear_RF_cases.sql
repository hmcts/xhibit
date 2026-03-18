set termout off;
spool &1
update xhb_case set charge_import_indicator='S' where charge_import_indicator='RF' and trunc(last_update_date) = trunc(sysdate);
commit;
exit;
