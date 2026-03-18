set termout on;
select count(*) from xhb_document_control where status='FD' and creation_date>=sysdate-4/24 and creation_date<=sysdate-1/24;
set termout off;
exit;

