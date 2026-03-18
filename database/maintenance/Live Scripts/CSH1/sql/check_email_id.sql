set termout off;
spool &2
select mail_id || ',' || subject from xhb_email2 where mail_id=&1 ;
exit;
