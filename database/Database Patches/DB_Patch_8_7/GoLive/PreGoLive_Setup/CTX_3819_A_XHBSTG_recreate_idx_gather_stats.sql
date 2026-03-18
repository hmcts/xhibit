/*************************************************************************************************/
/*                                                                                               */
/*  CTX_3819_A : Recreate XHBSTG_CRTRDAY_COMP_IDX and gather table stats again - issues from     */
/*               the Live DRY RUN 09th Mar   added                                               */
/*                                                                                               */
/*  Schema : DATA_MIG - user DATA_MIG                                                            */
/*************************************************************************************************/
BEGIN
    DBMS_OUTPUT.ENABLE(1000000);
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('CTX_3819_A : Recreate XHBSTG_CRTRDAY_COMP_IDX and gather table stats again               ');
    DBMS_OUTPUT.PUT_LINE('#########################################################################################');
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Recreating XHBSTG_CRTRDAY_COMP_IDX');
END;
/
DROP INDEX XHBSTG_CRTRDAY_COMP_IDX
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Index XHBSTG_CRTRDAY_COMP_IDX dropped');
END;
/
CREATE INDEX XHBSTG_CRTRDAY_COMP_IDX ON XHBSTG_COURTROOM_DAY_DM(trunc(list_date),decode(list_type,'D','Daily','W','Warned','F','Firm'),site_code,courtroom_no,nvl(xhibit_etl_status,'N'),xhibit_enrich_date) LOCAL
/
BEGIN
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'XHBSTG_CRTRDAY_COMP_IDX ON XHBSTG_COURTROOM_DAY_DM(trunc(list_date),decode(list_type,''D'',''Daily'',''W'',''Warned'',''F'',''Firm''),site_code,courtroom_no,nvl(xhibit_etl_status,''N''),xhibit_enrich_date) LOCAL - Created');
END;
/
BEGIN
    DBMS_OUTPUT.PUT_LINE('                                                                                ');
    DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'CTX_3819_A : Index XHBSTG_CRTRDAY_COMP_IDX recreated Successfully');
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
   dbms_output.put_line('---------------------------------------------------------------------------');
   dbms_output.put_line('Beginning to set gather_table_stats on DATA_MIG.XHBSTG_COURTROOM_DAY_DM ...');
   dbms_output.put_line('---------------------------------------------------------------------------');
   dbms_output.put_line(' ');
   FOR i IN (select table_name from user_tables WHERE table_name = 'XHBSTG_COURTROOM_DAY_DM')
   LOOP
       exec_sql := 'dbms_stats.gather_table_stats(ownname => ''DATA_MIG'',tabname => '''||i.table_name||''',cascade => TRUE)';
       dbms_output.put_line(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||'Executing statement : '||exec_sql);
       EXECUTE IMMEDIATE 'BEGIN '||exec_sql||'; END;';
       select table_name,last_analyzed into l_tab_name,l_last_analyzed from user_tables where table_name = i.table_name;
       dbms_output.put_line(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS')||': '||l_tab_name||' - LAST_ANALYZED - '||to_char(l_last_analyzed,'DD-MON-YYYY HH24:MI:SS'));
   END LOOP;
   dbms_output.put_line(' ');
   dbms_output.put_line('-----------------------------------------------------------------------');
   dbms_output.put_line('Setting gather_table_stats on DATA_MIG schema User Tables Complete.    ');
   dbms_output.put_line('-----------------------------------------------------------------------');
END;
/

