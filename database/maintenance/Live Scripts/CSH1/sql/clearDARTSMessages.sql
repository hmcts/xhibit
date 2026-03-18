set termout off;
spool append &1

select count(*) from dar_message_store where status_code='S' and last_update_date< trunc(sysdate);
delete from dar_message_store where status_code='S' and last_update_date< trunc(sysdate);
commit;

select count(*) from dar_message_store where status_code='F' and status_detail='404 : Courthouse Not Found' and last_update_date< trunc(sysdate);
delete from dar_message_store where status_code='F' and status_detail='404 : Courthouse Not Found' and last_update_date< trunc(sysdate);
commit;

exit;
