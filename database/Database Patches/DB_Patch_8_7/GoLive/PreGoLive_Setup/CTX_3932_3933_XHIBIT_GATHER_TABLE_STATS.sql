/*************************************************************************************************/
/*  ctx_3932 : GATHER TABLE STATS for SELECTED XHIBIT tables                                   */
/*  ctx_3933 : And Amend INDEX for REF_CALENDAR                                             */
/*             Login as USER : XHIBIT                                                            */
/*                
/*************************************************************************************************/
begin
dbms_output.put_line('CTX_3933 : Dropping index XHB_REF_CAL_DATE_I and recreating on CAL_DATE,COURT_ID... ');
end;
/
DROP INDEX XHB_REF_CALENDAR_CAL_DATE_I;
/
CREATE INDEX XHB_REF_CALENDAR_CAL_DATE_I ON XHB_REF_CALENDAR (CAL_DATE,COURT_ID);
/
DECLARE
exec_sql varchar2(250);
l_tab_name varchar2(50);
l_last_analyzed date;
BEGIN
   dbms_output.enable(1000000);
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line('CTX-3932 : Beginning to set gather_table_stats on XHIBIT schema - SELECTED User Tables ....');
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line(' ');
   FOR i IN (select table_name from user_tables 
             where table_name in 
             ('XHB_CASE_LISTING_ENTRY','XHB_REF_CALENDAR') )
   LOOP
       exec_sql := 'dbms_stats.gather_table_stats(ownname => ''XHIBIT'',tabname => '''||i.table_name||''',cascade => TRUE)';
       dbms_output.put_line(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Executing statement : '||exec_sql);
       EXECUTE IMMEDIATE 'BEGIN '||exec_sql||'; END;';
       select table_name,last_analyzed into l_tab_name,l_last_analyzed from user_tables where table_name = i.table_name;
       dbms_output.put_line(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||l_tab_name||' - LAST_ANALYZED - '||to_char(l_last_analyzed,'DD-MON-YYYY HH24:MI:SS'));
   END LOOP;
   dbms_output.put_line(' ');
   dbms_output.put_line('--------------------------------------------------------------------------------');
   dbms_output.put_line('Setting gather_table_stats on XHIBIT schema - selected User Tables Complete.    ');
   dbms_output.put_line('--------------------------------------------------------------------------------');
END;
/

