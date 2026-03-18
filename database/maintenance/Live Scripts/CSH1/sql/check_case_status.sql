set termout off;
spool &4
select charge_import_indicator from xhb_case where case_number=&1 and case_type='&2' and court_id=&3 ;
exit;
