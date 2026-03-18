set termout off;
spool &1
--select user_id from aud_user_logins where rownum<2;
select user_id from aud_user_logins where logged_in='Y' and rownum<2;
spool off
exit;
