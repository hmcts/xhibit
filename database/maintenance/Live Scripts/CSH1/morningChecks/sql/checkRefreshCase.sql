SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
select  charge_import_indicator from xhb_case where case_number ='20110101' and case_type='T' and court_id=1;
EXIT;
