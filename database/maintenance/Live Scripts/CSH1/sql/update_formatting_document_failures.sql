set termout on;
update xhb_document_control set status='ND' where status='FD' and creation_date>=sysdate-4/24 and creation_date<=sysdate-1/24;
set termout off;
commit;
exit;

