SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 50
SET HEADING ON
SET HEADING OFF

select court_name  from xhb_court  
where COURT_ID NOT IN ( 2,3,18,23,60,45,48, 49,53,65,68,70 ) minus
select c.court_name
from xhb_xml_document x, xhb_court c
where x.CREATION_DATE (+) between TO_DATE( to_char(sysdate, 'DD-MON-YYYY'), 'dd-mon-yyyy')and TO_DATE( to_char(sysdate, 'DD-MON-YYYY'),'dd-mon-yyyy') + 1
AND x.court_id (+) = c.court_id
and x.document_type (+) = 'DL'
and trunc(date_created) = trunc(sysdate)+1
and document_TITLE like 'Daily List FINAL%'
order by court_name;
exit
