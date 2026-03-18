set termout off;
spool &1
select count(*) from aud_user_logins where logged_in='Y' and last_update_date > sysdate - 15/1440 ;
exit;
