set termout off;
spool &1
select trunc(last_day(add_months(sysdate,-2))+1) from dual;
exit;
