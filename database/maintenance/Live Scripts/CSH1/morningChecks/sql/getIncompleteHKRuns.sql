SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF

select count(*) from xhb_hk_results where run_end_date is null and trunc(run_start_date)>=trunc(sysdate-2);
EXIT;
