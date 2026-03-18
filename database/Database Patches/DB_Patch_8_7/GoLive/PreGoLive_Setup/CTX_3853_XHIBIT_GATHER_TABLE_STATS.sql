/*************************************************************************************************/
/*  CTX_3853 : GATHER TABLE STATS for SELECTED XHIBIT tables                                   */
/*             Login as USER : XHIBIT                                                            */
/*                AND also DROP INDEX XHB_OFFENCE_DM_IDX
/*************************************************************************************************/
DROP INDEX XHB_OFFENCE_DM_IDX
/
BEGIN
DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Index XHB_OFFENCE_DM_IDX - Dropped');
DBMS_OUTPUT.PUT_LINE('                                                                                ');
END;
/
DECLARE
exec_sql varchar2(250);
l_tab_name varchar2(50);
l_last_analyzed date;
BEGIN
   dbms_output.enable(1000000);
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line('CTX-3853 : Beginning to set gather_table_stats on XHIBIT schema - SELECTED User Tables ....');
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line(' ');
   FOR i IN (select table_name from user_tables 
             where table_name in 
             ('XHB_REF_LISTING_DATA','XHB_LIST', 'XHB_COURT', 'XHB_CASE', 'XHB_REF_HEARING_TYPE', 'XHB_COURT_ROOM', 'XHB_COURT_SITE') )
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
