set termout off;
spool &2
select terminal_name from xhb_terminal where terminal_name=(select terminal_id from aud_user_logins where lower(trim(user_id))=lower(trim('&1')));
select court_name from xhb_court where court_id=(select court_id from xhb_terminal where terminal_name=(select terminal_id from aud_user_logins where lower(trim(user_id))=lower(trim('&1'))));
spool off
exit;
