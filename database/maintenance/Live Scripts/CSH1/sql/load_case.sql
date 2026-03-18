set termout off;
spool append &4
update xhb_case set charge_import_indicator='R' where case_number=&1 and case_type='&2' and court_id=&3 ;
commit;
exit;
