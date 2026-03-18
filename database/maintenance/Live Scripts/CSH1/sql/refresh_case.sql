set termout off;
spool append &4
update xhb_case set charge_import_indicator='RF' where case_number=&1 and case_type='&2' and court_id=&3 ;
commit;
update xhb_case set charge_import_indicator='S' where case_number=&1 and case_type='&2' and court_id=&3 ;
commit;
exit;
