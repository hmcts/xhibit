SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF

select lists_deleted from XHB_HK_RESULTS where trunc(run_start_date)=trunc(sysdate) and run_type='L';
EXIT;
