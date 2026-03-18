set feedback off
set termout off
set trimspool on
set headsep off
set colsep '","'
set pagesize 0
set linesize 32000
set serveroutput on 
set verify off

/****************** set column variables ******************/

column tab_name new_val tab_name
column filename new_val filename
column qry_tmp_head new_val qry_tmp_head
column qry_tmp_data new_val qry_tmp_data
column qry_filename new_val qry_filename
column db_rowcount new_val db_rowcount

/************** assign column variables **************/

select lower(table_name) tab_name, 'court_'||'&court_id'||'_'||lower(table_name)||'.csv' filename, lower(table_name)||'_qry.sql'  qry_filename from user_tables where lower(table_name) = '&1';
select lower(table_name)||'_tmp_h.sql' qry_tmp_head, lower(table_name)||'_tmp_d.sql' qry_tmp_data from user_tables where lower(table_name) = '&1';
select count(*) as db_rowcount from &tab_name;

set termout on
spool &log_dir/&tmp_file
select '                                                                                        ' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Creating csv file '||'&filename'||' for table : '||UPPER('&tab_name') from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Extracting '||count(*)||' rows from table : '||UPPER('&tab_name') from &tab_name;
spool off

!cat &log_dir/&tmp_file >> &log_dir/&log_file
!rm &log_dir/&tmp_file

set termout off
spool &csv_dir/&qry_filename

DECLARE
l_start NUMBER := 1;
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
dbms_output.put_line('SELECT ''"''|| ');
FOR i in (select column_name from all_tab_columns where lower(table_name) = '&tab_name' order by column_id)
loop
  if l_start > 1 then
     dbms_output.put_line('||''","''||');
  end if;
  dbms_output.put_line('REPLACE(REPLACE('||i.column_name||',chr(10),''''),''"'','':'')');
  l_start := l_start + 1;
end loop;

dbms_output.put_line('||''","''||&2||''"''');
dbms_output.put_line(' FROM '|| '&tab_name;');
end;
/
spool off

spool &csv_dir/&qry_tmp_head 
DECLARE
HEADER_TXT VARCHAR2(4000) := '';
PART_HEADER VARCHAR2(4000) := '';
l_header_length NUMBER(8);
l_start NUMBER(8);
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
FOR i in (select column_name from all_tab_columns where lower(table_name) = '&tab_name' order by column_id)
loop
HEADER_TXT := HEADER_TXT||i.column_name||',';
end loop;
HEADER_TXT := HEADER_TXT||'COURT_ID'||',';
HEADER_TXT := SUBSTR(HEADER_TXT,1,LENGTH(HEADER_TXT)-1);
l_header_length:=LENGTH(HEADER_TXT);
if l_header_length <= 200 then
     dbms_output.put_line(HEADER_TXT);
else
  l_start := 1;
  PART_HEADER := HEADER_TXT;
  loop
  PART_HEADER:= SUBSTR(HEADER_TXT,l_start,least(200,(l_header_length-l_start+1)));
  l_start:=l_start+LENGTH(PART_HEADER);
  dbms_output.put_line(PART_HEADER);
  if l_start > l_header_length then
     exit;
  end if;
  end loop;
end if;
end;
/
spool off 

!tr -d '\n' < &csv_dir/&qry_tmp_head > &csv_dir/&filename

spool &csv_dir/&qry_tmp_data 
@&csv_dir/&qry_filename 
spool off

!echo "" >> &csv_dir/&filename
!cat &csv_dir/&qry_tmp_data >> &csv_dir/&filename

set termout on
spool &log_dir/&tmp_file

select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Export to csv file '||UPPER('&filename')||' complete.' from dual;
spool off

!wc -l &csv_dir/&filename | awk {'print $1-1 " rows exported to csv file " $2'} >> &log_dir/&tmp_file
!wc -l &csv_dir/&filename | awk {'print $1-1 " rows exported to csv file " $2'} 

set termout off
!cat &log_dir/&tmp_file >> &log_dir/&log_file
!rm &log_dir/&tmp_file

!wc -l &csv_dir/&filename | awk {'printf"%-80s| %-18d| %-19d|\n",$2,$1-1,&db_rowcount'} | sed 's/court_[0-9][0-9][0-9]_crest_csv_files\///g' > &log_dir/&tmp_file
!cat &log_dir/&tmp_file >> &log_dir/&result_csv_file 
!rm &log_dir/&tmp_file


!rm &csv_dir/&qry_tmp_head
!rm &csv_dir/&qry_tmp_data
!rm &csv_dir/&qry_filename
