/*************************************************************************************************/
/*  CTX_3911 : APPLY INDEXES, ADD PKs AND GATHER TABLE STATS for SELECTED XHIBIT tables          */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
BEGIN
    DBMS_OUTPUT.ENABLE(1000000);
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('CTX_3911 : adding recommended Additional Indexes for XHIBIT tables                       ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Adding Additional indexes for XHBSTG tables');
END;
/
ALTER TABLE AUD_LEGAL_AID_ORDER ADD PRIMARY KEY(LEGAL_AID_ORDER_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'PRIMARY KEY ADDED ON AUD_LEGAL_AID_ORDER(LEGAL_AID_ORDER_ID) - Created');
END;
/
ALTER TABLE AUD_SCHED_HEARING_DEFENDANT ADD PRIMARY KEY(SCHEDULED_HEARING_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'PRIMARY KEY ADDED ON AUD_SCHED_HEARING_DEFENDANT(SCHEDULED_HEARING_ID) - Created');
END;
/
CREATE INDEX AUD_SCH_HRG_DEF_SCHHRG_ID ON AUD_SCHED_HEARING_DEFENDANT(SCHEDULED_HEARING_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_SCH_HRG_DEF_SCHHRG_ID ON AUD_SCHED_HEARING_DEFENDANT(SCHEDULED_HEARING_ID) - Created');
END;
/
BEGIN
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX_3911 : Necessary XHIBIT indexes and PKs created Successfully');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
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
   dbms_output.put_line('CTX-3911 : Beginning to set gather_table_stats on XHIBIT schema - SELECTED User Tables ....');
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line(' ');
   FOR i IN (select table_name from user_tables 
             where table_name in 
             ('AUD_SCHED_HEARING_DEFENDANT') )
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
