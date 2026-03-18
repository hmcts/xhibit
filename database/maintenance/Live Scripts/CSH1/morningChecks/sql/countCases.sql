SET ECHO OFF
SET FEEDBACK OFF
SET PAGESIZE 9999
SET LINESIZE 500
SET HEADING ON
SET HEADING OFF
select count(*) from xhb_hearing
where hearing_id in (
  select hearing_id from XHB_SCHEDULED_HEARING
  where sitting_id in (
    select sitting_id from XHB_SITTING
      where list_id in (
        select list_id from XHB_HEARING_LIST
        where last_update_date between (to_date(concat(to_char(sysdate-1, 'DD/MM/YYYY'), ' 18:45'),'DD/MM/YYYY hh24:mi'))
         and to_date(concat(to_char(sysdate-1, 'DD/MM/YYYY'), ' 21:15'),'DD/MM/YYYY hh24:mi') 
      )
   )
);
EXIT;
