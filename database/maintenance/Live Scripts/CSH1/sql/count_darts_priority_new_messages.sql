set termout off;
spool &1
select count(*) from dar_priority_new_messages ;
exit;
