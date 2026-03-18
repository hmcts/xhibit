SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
select court_name from xhb_court
where court_id not in ( select court_id from xhb_hearing_list where trunc(start_date) = trunc(sysdate))
and COURT_ID NOT IN ( 2,3,18,23,45,48,53,60,65,68,70,49 )
order by 1 asc;
EXIT;
