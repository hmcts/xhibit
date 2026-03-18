set termout off;
spool &1
select trunc(last_day(add_months(sysdate,-1))) from dual;
exit;
