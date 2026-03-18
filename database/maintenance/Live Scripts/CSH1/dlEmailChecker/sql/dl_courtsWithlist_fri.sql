SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF

select distinct(c.court_name)
from xhb_xml_document x, xhb_court c
where x.CREATION_DATE (+) between TO_DATE( to_char(sysdate, 'DD-MON-YYYY'), 'dd-mon-yyyy')and TO_DATE( to_char(sysdate, 'DD-MON-YYYY'),'dd-mon-yyyy') +3 
AND x.court_id (+) = c.court_id
and x.document_type (+) = 'DL'
and trunc(date_created) = trunc(sysdate)+3
and document_TITLE like 'Daily List FINAL%'
order by court_name;
exit
