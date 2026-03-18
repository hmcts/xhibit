set termout off;
spool &3
commit;
update xhb_email2 set status='R', attempts=1, subject='&2' where mail_id=&1 ;
commit;
exit;
