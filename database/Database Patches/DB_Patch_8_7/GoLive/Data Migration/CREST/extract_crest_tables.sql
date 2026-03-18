set feedback off
set termout off
set trimspool on
set headsep off
set colsep '","'
set pagesize 0
set linesize 32000
set serveroutput on
set verify off

/************  set column variables  ******************/

column sysdt new_val sysdt_var
column court_id new_val court_id
column sql_dir new_val sql_dir
column csv_dir new_val csv_dir
column log_dir new_val log_dir
column sql_exec_file new_val sql_exec_file
column log_file new_val log_file
column tmp_file new_val tmp_file
column result_csv_file new_val result_csv_file

/******************** set folder names ****************/

select nvl(lcd_stats_code,'999') as court_id, 
'court_'||nvl(lcd_stats_code,'999')||'_crest_csv_files' as csv_dir, 
'court_'||nvl(lcd_stats_code,'999')||'_crest_csv_log_files' as log_dir, 
'court_'||nvl(lcd_stats_code,'999')||'_crest_csv_sql_files' as sql_dir from home_court;

/*******************  create folders if not existing **************/

!mkdir -p &sql_dir
!mkdir -p &log_dir
!mkdir -p &csv_dir

/****************** set sysdt variable to populate runtime ***************/

SELECT to_char(SYSDATE,'YYYYMMDD_HH24MISS') sysdt from dual;

/*************  set filenames ******************/

select 'court_'||'&court_id'||'_C2X_DM_CREST_TABLES_LIST_'||'&sysdt_var'||'.sql' as sql_exec_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_CREST_extract_to_csv_'||'&sysdt_var'||'.log' as log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_CREST_EXTRACT_RESULTS_'||'&sysdt_var'||'.csv' as result_csv_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_CREST_tmp_'||'&sysdt_var'||'.log' as tmp_file from dual;

/***************** spool to log file set header info ****************/

set termout on
spool &log_dir/&log_file

select '########################################################################################' from dual;
select '###              DATA MIGRATION LOG - CREST TABLES to CSV FILES                      ###' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting extract of Crest Tables to CSV files in '||'&csv_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>LOG files created in '||'&log_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>FINAL EXPORT RESULTS C2X_DM_CREST_EXTRACT_RESULTS_YYMMDDHH24MISS.csv file created in '||'&log_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Creating C2X_DM_CREST_TABLES_LIST_YYMMDD_HH24MISS.sql in '||'&sql_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;

spool off

/*************** spool the calls to generate_crest_csv.sql with parameters tablename and court_id to sql executable file ***************/

set termout off
spool &sql_dir/&sql_exec_file

DECLARE
crest_table_name varchar2(200) := '';
courtid varchar2(3) := '';
BEGIN
select nvl(lcd_stats_code,'999') as court_id into courtid from home_court;
FOR i in (select distinct table_name as table_name from user_tables where table_name in ('CASE','HOME_COURT','SUBJECT','CASE_SUBJECT','CASE_PARTY_SOF','CASE_OPPOSER','LEGAL_AID_ORDER','CASE_NOTE','NON_AVAIL_DATES','JUDGE_TICKET','BW_HISTORY','DISPOSAL','COURTROOM_LOCATION','COURTROOM','COURTROOM_USAGE','JUDGE_USAGE','WARNED_LIST_DETAILS','COMMITTAL_CHARGE','CHAMBERS','LEGAL_AID_AMENDMENT','CASE_HISTORY','CASE_HEARING_DAY','CASE_SUBJECT_APPEARANCE','SOLICITOR_FIRM','SUBJECT_HISTORY','LISTS','COURTROOM_DAY','CASE_HEARING_DAY','CHARGE','CSU_HISTORY','RELEASE_JUDGE','OPPOSER') order by table_name)
loop
select lower(i.table_name) into crest_table_name from dual;
dbms_output.put_line('@generate_crest_csv.sql '||crest_table_name||' '||courtid);
end loop;
end;
/

spool off

set termout on
spool &log_dir/&tmp_file

select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>CREST Tables List court_XXX_C2X_DM_CREST_TABLES_LIST_YYMMDDHH24MISS.sql created' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting to extract tables to csv one by one' from dual;

spool off

!cat &log_dir/&tmp_file >> &log_dir/&log_file

!rm &log_dir/&tmp_file

!echo "CSV_FILENAME                                          |  CSV_RECORD_COUNT |  DB_TABLE_ROWCOUNT |" > &log_dir/&result_csv_file

@&sql_dir/&sql_exec_file

set termout on
spool &log_dir/&tmp_file

select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Extract of CREST Tables csv files - COMPLETE' from dual;
select '                                                                                        ' from dual;
select '########################################################################################' from dual;

spool off

set termout off
!cat &log_dir/&tmp_file >> &log_dir/&log_file

!rm &log_dir/&tmp_file
