set feedback off
set termout off
set trimspool on
set headsep off
set colsep ,
set pagesize 0
set linesize 32000
set serveroutput on size 200000
set verify off

/****************** set column variables ******************/

column tab_name new_val tab_name
column ddl_filename new_val ddl_filename

/************** assign column variables **************/

select lower(table_name) tab_name, 'court_'||'&court_id'||'_'||lower(table_name)||'_ddl.sql' ddl_filename from user_tables where lower(table_name) = '&2';

set termout on
spool &ddl_log_dir/&tmp_file
select '                                                                                        ' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Creating DDL file '||'&ddl_filename'||' for table : '||UPPER('&tab_name') from dual;
spool off

!cat &ddl_log_dir/&tmp_file >> &ddl_log_dir/&log_file
!rm &ddl_log_dir/&tmp_file

set termout off
spool &ddl_dir/&ddl_filename

DECLARE
 CURSOR TabCur IS
 SELECT table_name,owner,tablespace_name,
        initial_extent,next_extent,
        pct_used,pct_free,pct_increase,degree
   FROM all_tables
  WHERE owner=upper('&1')
    AND table_name=UPPER('&2');
--
 CURSOR ColCur(TableName varchar2) IS
 SELECT column_name col1,
        DECODE (data_type,
                'LONG',       'LONG   ',
                'LONG RAW',   'LONG RAW  ',
                'RAW',        'RAW  ',
                'DATE',       'DATE   ',
                'CHAR',       'CHAR' || '(' || data_length || ') ',
                'VARCHAR2',   'VARCHAR2' || '(' || data_length || ') ',
                'NUMBER',     'NUMBER' ||
                DECODE (NVL(data_precision,0),0, ' ',' (' || data_precision ||
                DECODE (NVL(data_scale, 0),0, ') ',',' || DATA_SCALE || ') '))) ||
        DECODE (NULLABLE,'N', 'NOT NULL','  ') col2
   FROM all_tab_columns
  WHERE table_name=TableName
    AND owner=UPPER('&1')
 ORDER BY column_id;
--
 ColCount    NUMBER(5);
 MaxCol      NUMBER(5);
 FillSpace   NUMBER(5);
 ColLen      NUMBER(5);
--
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
 MaxCol:=0;
 --
 FOR TabRec in TabCur LOOP
    SELECT MAX(column_id) INTO MaxCol FROM all_tab_columns
     WHERE table_name=TabRec.table_name
       AND owner=TabRec.owner;
    --
    IF TabRec.table_name = 'CASE_SUBJECT_APPEARANCE' THEN
       dbms_output.put_line('CREATE TABLE XHBSTG_CASE_SUB_APPEARANCE_DM');
    ELSE
       dbms_output.put_line('CREATE TABLE XHBSTG_'||TabRec.table_name||'_DM');
    END IF;
    dbms_output.put_line('( ');
    --
    ColCount:=0;
    FOR ColRec in ColCur(TabRec.table_name) LOOP
      ColLen:=length(ColRec.col1);
      FillSpace:=40 - ColLen;
      dbms_output.put(ColRec.col1);
      --
      FOR i in 1..FillSpace LOOP
         dbms_output.put(' ');
      END LOOP;
      --
      dbms_output.put(ColRec.col2);
      ColCount:=ColCount+1;
      --
      IF (ColCount < MaxCol) THEN
         dbms_output.put_line(',');
      ELSE
         dbms_output.put_line(',');
         dbms_output.put_line('CREST_COURT_ID                          VARCHAR2(3),');
         dbms_output.put_line('XHIBIT_COURT_ID                         VARCHAR2(3),');
         dbms_output.put_line('XHIBIT_ETL_STATUS                       CHAR(1),');
         dbms_output.put_line('XHIBIT_LOADED_DATE                      DATE DEFAULT SYSDATE,');
         dbms_output.put_line('XHIBIT_ENRICH_DATE                      DATE,');
         dbms_output.put_line('XHIBIT_ETL_DATE                         DATE,');
         dbms_output.put_line('XHIBIT_ETL_ERR_MESSAGE                  VARCHAR2(500)');
         dbms_output.put_line(')');
      END IF;
    END LOOP;
    --
--    dbms_output.put_line('TABLESPACE '||TabRec.tablespace_name);
--    dbms_output.put_line('PCTFREE '||TabRec.pct_free);
--    dbms_output.put_line('PCTUSED '||TabRec.pct_used);
--    dbms_output.put_line('STORAGE ( ');
--    dbms_output.put_line('  INITIAL     '||TabRec.initial_extent);
--    dbms_output.put_line('  NEXT        '||TabRec.next_extent);
--    dbms_output.put_line('  PCTINCREASE '||TabRec.pct_increase);
--    dbms_output.put_line(' )');
--    dbms_output.put_line('PARALLEL '||TabRec.degree);
    dbms_output.put_line('/');
 END LOOP;
END;
/
spool off


set termout on
spool &ddl_log_dir/&tmp_file

select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Generation of DDL file '||'&ddl_filename'||' complete.' from dual;
spool off

set termout off
!cat &ddl_log_dir/&tmp_file >> &ddl_log_dir/&log_file
!rm &ddl_log_dir/&tmp_file

