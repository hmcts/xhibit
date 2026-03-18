ACCEPT crest_court_id PROMPT "Enter CREST COURT ID : "
ACCEPT dbms_output PROMPT "Enter DBMS_OUTPUT ENABLE FLAG (ON/OFF) default OFF : "

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

column court_is_valid new_val court_is_valid
column dbms_output_flag new_val dbms_output_flag
column sysdt new_val sysdt_var
column court_id new_val court_id
column court_name new_val court_name
column confirm_court new_val confirm_court
column log_dir new_val log_dir
column log_file new_val log_file
column tmp_file new_val tmp_file
column result_log_file new_val result_log_file

/****************** set sysdt variable to populate runtime ***************/

SELECT to_char(SYSDATE,'YYYYMMDD_HH24MISS') as sysdt from dual;

/********************* set default court_id_valid to N **************/

SELECT  'N' as court_is_valid from dual;

SELECT 'Y',court_name as court_is_valid,court_name from xhibit.xhb_court where crest_court_id = nvl('&&crest_court_id',NULL);

/********************* set dbms_output_flag input - default is N **************/

SELECT decode(nvl(upper('&&dbms_output'),'OFF'),'ON','ON','OFF','OFF','OFF') as dbms_output_flag from dual;


set termout on
spool C2X_DM_ETL_1_err_log_&sysdt_var\.log 

/******************** set folder names ****************/

DECLARE
court_id varchar(10);
court_is_valid char(1);
BEGIN
      SELECT crest_court_id as court_id INTO court_id FROM xhibit.xhb_court WHERE crest_court_id = nvl('&&crest_court_id',NULL);

      DBMS_OUTPUT.PUT_LINE('COURT ID VALID, PROCEEDING');

EXCEPTION WHEN NO_DATA_FOUND THEN

          DBMS_OUTPUT.PUT_LINE('############################################################################################');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Invalid CREST COURT ID '||&&crest_court_id||' Supplied - No DATA FOUND!!!!');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Please retry running C2X_RUN_DM_ETL_1 script with a Valid CREST COURT ID!');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE('############################################################################################');
END;
/

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&court_is_valid'='N' THEN RAISE_APPLICATION_ERROR(-20000,'INVALID COURT SUPPLIED, PLEASE RETRY...'); END IF;

/******************* Get User Confirmation on the Court that is entered ****/

set termout on
spool C2X_DM_ETL_1_err_log_&sysdt_var\.log append
 
ACCEPT confirm_court PROMPT "You have selected &court_name , Please confirm in capitals (YES/NO) : "  

EXEC IF '&&confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); END IF;

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); RAISE_APPLICATION_ERROR(-20000,'Script cancelled as COURT not confirmed, please run again if needed...'); END IF;

/**************************************************************************/
   
/**************** set folder names ******************/

select crest_court_id as court_id,
'court_'||crest_court_id||'_C2X_DM_ETL_1_log_files' as log_dir
from xhibit.xhb_court where crest_court_id = nvl('&&crest_court_id',NULL);
 
/******************* if running in UNIX - enable the below i.e.  create folders if not existing **************/

--!mkdir -p &log_dir

/*************  set filenames ******************/

select 'court_'||'&court_id'||'_C2X_DM_ETL_1_'||'&sysdt_var'||'.log' as log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_ETL_1_RESULTS_'||'&sysdt_var'||'.log' as result_log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_ETL_1_tmp_'||'&sysdt_var'||'.log' as tmp_file from dual;

/***************** spool to log file set header info ****************/

set termout on
spool &log_file

select '############################################################################################' from dual;
select '###              DATA MIGRATION ETL 1 SCRIPT LOG                                         ###' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting C2X_DM_ETL_1 script run for Court - '||'&court_id' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/*************** EXECUTE C2X_DM_ETL PROCEDURES and spool the results to the log file ***************/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.parse_xhbstg_courtroom_day ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.parse_xhbstg_courtroom_day' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DBMS_OUTPUT FLAG is '||'&dbms_output_flag' from dual;
select '                                                                                        ' from dual;

set termout off

DECLARE
BEGIN
if nvl(upper('&dbms_output_flag'),'OFF')='ON' then
   dbms_output.enable(1000000);
else
   dbms_output.disable;
end if;
data_mig.xhb_data_migration_process_pkg.parse_xhbstg_courtroom_day(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.parse_xhbstg_courtroom_day COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.parse_xhbstg_courtroom_day ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_court_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_court_with_crest' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DBMS_OUTPUT FLAG is '||'&dbms_output_flag' from dual;
select '                                                                                        ' from dual;

set termout off

DECLARE
BEGIN
if nvl(upper('&dbms_output_flag'),'OFF')='ON' then
   dbms_output.enable(1000000);
else
   dbms_output.disable;
end if;
data_mig.xhb_data_migration_process_pkg.update_xhb_court_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_court_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_court_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_room_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_room_with_crest' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DBMS_OUTPUT FLAG is '||'&dbms_output_flag' from dual;
select '                                                                                        ' from dual;

set termout off

DECLARE
BEGIN
if nvl(upper('&dbms_output_flag'),'OFF')='ON' then
   dbms_output.enable(1000000);
else
   dbms_output.disable;
end if;
data_mig.xhb_data_migration_process_pkg.upd_xhb_court_room_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_room_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_room_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.list_crest_cases_not_in_xhibit ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.list_crest_cases_not_in_xhibit' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DBMS_OUTPUT FLAG is '||'&dbms_output_flag' from dual;
select '                                                                                        ' from dual;

set termout off

DECLARE
BEGIN
if nvl(upper('&dbms_output_flag'),'OFF')='ON' then
   dbms_output.enable(1000000);
else
   dbms_output.disable;
end if;
data_mig.xhb_data_migration_process_pkg.list_crest_cases_not_in_xhibit(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.list_crest_cases_not_in_xhibit COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.list_crest_cases_not_in_xhibit ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.merge_crest_cases_into_xhibit ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.merge_crest_cases_into_xhibit' from dual; 
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>DBMS_OUTPUT FLAG is '||'&dbms_output_flag' from dual;
select '                                                                                        ' from dual;

set termout off

DECLARE
BEGIN
if nvl(upper('&dbms_output_flag'),'OFF')='ON' then
   dbms_output.enable(1000000);
else
   dbms_output.disable;
end if;
data_mig.xhb_data_migration_process_pkg.merge_crest_cases_into_xhibit(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.merge_crest_cases_into_xhibit COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.merge_crest_cases_into_xhibit ***********/

/******************** all procedures DONE *****************/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>C2X_DM_ETL_1 script run COMPLETE' from dual;
select '                                                                                        ' from dual;
select '########################################################################################' from dual;

spool off

set termout off
EXIT

