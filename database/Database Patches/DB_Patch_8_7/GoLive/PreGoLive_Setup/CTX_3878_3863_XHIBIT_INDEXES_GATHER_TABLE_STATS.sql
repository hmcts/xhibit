/*************************************************************************************************/
/*  CTX_3878_3863 : APPLY INDEXES AND GATHER TABLE STATS for SELECTED XHIBIT tables              */
/*             Login as USER : XHIBIT                                                            */
/*************************************************************************************************/
BEGIN
    DBMS_OUTPUT.ENABLE(1000000);
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('CTX_3878 : adding recommended Additional Indexes for XHIBIT tables                       ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Adding Additional indexes for XHBSTG tables');
END;
/
CREATE INDEX XHB_EMAIL2_MIME_BODY_BLOB_ID ON XHB_EMAIL2(MIME_BODY_BLOB_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_EMAIL2_MIME_BODY_BLOB_ID ON XHB_EMAIL2(MIME_BODY_BLOB_ID) - Created');
END;
/
CREATE INDEX XHB_COO_CID_CASETYPNUM ON XHB_CASE_OVERNIGHT_REFRESH(COURT_ID, CASE_TYPE_NUMBER)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHB_COO_CID_CASETYPNUM ON XHB_CASE_OVERNIGHT_REFRESH(COURT_ID, CASE_TYPE_NUMBER) - Created');
END;
/
CREATE INDEX AUD_LADVLNK_LEGAIDORDID ON AUD_LEO_ADV_LINK(LEGAL_AID_ORDER_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_LADVLNK_LEGAIDORDID ON AUD_LEO_ADV_LINK(LEGAL_AID_ORDER_ID) - Created');
END;
/
CREATE INDEX AUD_LAO_DOCID_LAOID ON AUD_LEGAL_AID_ORDER(DEFENDANT_ON_CASE_ID, LEGAL_AID_ORDER_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_LAO_DOCID_LAOID ON AUD_LEGAL_AID_ORDER(DEFENDANT_ON_CASE_ID, LEGAL_AID_ORDER_ID) - Created');
END;
/
CREATE INDEX AUD_LAO_LAOID ON AUD_LEGAL_AID_ORDER(LEGAL_AID_ORDER_ID)
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'AUD_LAO_LAOID ON AUD_LEGAL_AID_ORDER(LEGAL_AID_ORDER_ID) - Created');
END;
/
BEGIN
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX_3878 : Necessary XHIBIT indexes created Successfully');
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
   dbms_output.put_line('CTX-3863 : Beginning to set gather_table_stats on XHIBIT schema - SELECTED User Tables ....');
   dbms_output.put_line('---------------------------------------------------------------------------------------------');
   dbms_output.put_line(' ');
   FOR i IN (select table_name from user_tables 
             where table_name in 
             ('AUD_DEFENDANT_ON_OFFENCE','AUD_DEFENDANT_ON_CASE') )
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
