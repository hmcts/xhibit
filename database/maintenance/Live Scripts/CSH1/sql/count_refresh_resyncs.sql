set termout off;
spool &2
select count(*) from xhb_case where charge_import_indicator='&1' ;
exit;
