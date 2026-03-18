SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF

           select ((to_date(to_char(max(xhb_hearing_list.creation_date), 'DD/MM/YYYY hh24:MI'),'DD/MM/YYYY hh24:mi') - to_date(concat(to_char(sysdate-1, 'DD/MM/YYYY'), ' 19:30'),'DD/MM/YYYY hh24:mi'))*24*60) from xhb_hearing_list, xhb_court where xhb_hearing_list.court_id = xhb_court.court_id and xhb_hearing_list.creation_date > to_date('01/01/2008', 'dd/mm/yyyy') order by max(xhb_hearing_list.creation_date) asc;

EXIT;
