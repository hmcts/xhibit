SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
update xhb_case set charge_import_indicator='RF' where case_number ='20110101' and case_type='T' and court_id=1;
commit;
update xhb_case set charge_import_indicator='S' where case_number ='20110101' and case_type='T' and court_id=1;
commit;
EXIT;
