set feedback off
set termout off
set trimspool on
set headsep off
set colsep ,
set pagesize 0
set linesize 32000
set serveroutput on
set verify off

/************  set column variables  ******************/

column sysdt new_val sysdt_var
column court_id new_val court_id
column crest_user new_val crest_user
column ddl_dir new_val ddl_dir
column ddl_log_dir new_val ddl_log_dir
column ddl_sql_dir new_val ddl_sql_dir
column sql_exec_file new_val sql_exec_file
column log_file new_val log_file
column tmp_file new_val tmp_file

/******************** set folder names ****************/

select nvl(lcd_stats_code,'999') as court_id, 
'court_'||nvl(lcd_stats_code,'999')||'_crest_ddl_log_files' as ddl_log_dir,
'court_'||nvl(lcd_stats_code,'999')||'_crest_ddl_sql_files' as ddl_sql_dir,
'court_'||nvl(lcd_stats_code,'999')||'_crest_ddl_files' as ddl_dir
from home_court;

/*******************  create folders if not existing **************/

!mkdir -p &ddl_sql_dir
!mkdir -p &ddl_log_dir
!mkdir -p &ddl_dir

/****************** set sysdt variable to populate runtime ***************/

SELECT to_char(SYSDATE,'YYYYMMDD_HH24MISS') sysdt from dual;

/*************  set filenames ******************/

select 'court_'||'&court_id'||'_C2X_DM_CREST_DDL_TABLES_LIST_'||'&sysdt_var'||'.sql' as sql_exec_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_CREST_generate_DDL_'||'&sysdt_var'||'.log' as log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_CREST_DDL_tmp_'||'&sysdt_var'||'.log' as tmp_file from dual;

/***************** spool to log file set header info ****************/

set termout on
spool &ddl_log_dir/&log_file

select '########################################################################################' from dual;
select '###              DATA MIGRATION LOG - CREST TABLES - generate DDL lOg                ###' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting to generate DDL of Crest Tables in '||'&ddl_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>LOG files created in '||'&ddl_log_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Creating C2X_DM_CREST_DDL_TABLES_LIST_YYMMDD_HH24MISS.sql in '||'&ddl_sql_dir'||'/ folder' from dual; 
select '                                                                                        ' from dual;

spool off

/*************** spool the calls to generate_crest_ddl.sql with parameters tablename and court_id to sql executable file ***************/

set termout off
spool &ddl_sql_dir/&sql_exec_file

DECLARE
crest_table_name varchar2(200) := '';
courtid varchar2(3) := '';
l_user varchar2(100) := '';
BEGIN
select nvl(lcd_stats_code,'999') as court_id into courtid from home_court;
select user  as crest_user into l_user from dual;
FOR i in 
(select distinct table_name as table_name from user_tables 
where table_name in ('CASE','HOME_COURT','SUBJECT','CASE_SUBJECT','CASE_PARTY_SOF','CASE_OPPOSER',
'LEGAL_AID_ORDER','CASE_NOTE','NON_AVAIL_DATES','JUDGE_TICKET','BW_HISTORY','DISPOSAL','COURTROOM_LOCATION',
'COURTROOM','COURTROOM_USAGE','JUDGE_USAGE','WARNED_LIST_DETAILS','COMMITTAL_CHARGE','CHAMBERS','LEGAL_AID_AMENDMENT',
'CASE_HISTORY','CASE_HEARING_DAY','CASE_SUBJECT_APPEARANCE','SOLICITOR_FIRM','SUBJECT_HISTORY','LISTS','COURTROOM_DAY',
'CHARGE','CSU_HISTORY') order by table_name)
loop
select lower(i.table_name) into crest_table_name from dual;
dbms_output.put_line('@generate_crest_ddl.sql '||l_user||' '||crest_table_name||' '||courtid);
end loop;
end;
/

spool off

set termout on
spool &ddl_log_dir/&tmp_file

select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>CREST Tables DDL List court_XXX_C2X_DM_CREST_TABLES_LIST_YYMMDDHH24MISS.sql created' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting to generate ddl for tables one by one' from dual;

spool off

!cat &ddl_log_dir/&tmp_file >> &ddl_log_dir/&log_file

!rm &ddl_log_dir/&tmp_file

@&ddl_sql_dir/&sql_exec_file

set termout on
spool &ddl_log_dir/&tmp_file

select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DDL generation of CREST Tables - COMPLETE' from dual;
select '                                                                                        ' from dual;
select '########################################################################################' from dual;

spool off

set termout off
!cat &ddl_log_dir/&tmp_file >> &ddl_log_dir/&log_file

!rm &ddl_log_dir/&tmp_file
