set termout off;
spool &2
commit;
update xhb_email2 set status='R', attempts=1 where mail_id=&1 ;
commit;
exit;
