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
column xhibit_court_id new_val xhibit_court_id
column log_dir new_val log_dir
column log_file new_val log_file
column tmp_file new_val tmp_file
column result_log_file new_val result_log_file

/****************** set sysdt variable to populate runtime ***************/

SELECT to_char(SYSDATE,'YYYYMMDD_HH24MISS') as sysdt from dual;

/********************* set default court_id_valid to N **************/
  
SELECT NVL((SELECT 'Y' from xhibit.xhb_court where crest_court_id = nvl('&&crest_court_id',NULL)),'N') as court_is_valid from dual;

/********************* set dbms_output_flag input - default is N **************/

SELECT decode(nvl(upper('&&dbms_output'),'OFF'),'ON','ON','OFF','OFF','OFF') as dbms_output_flag from dual;


set termout on
spool C2X_DM_ETL_2_err_log_&sysdt_var\.log 

/******************** set folder names ****************/

DECLARE
court_id varchar(10);
xhibit_court_id varchar(10);
court_is_valid char(1);
BEGIN
 SELECT crest_court_id as court_id,court_id as xhibit_court_id INTO court_id,xhibit_court_id FROM xhibit.xhb_court WHERE crest_court_id = nvl('&&crest_court_id',NULL);

      DBMS_OUTPUT.PUT_LINE('COURT ID VALID, PROCEEDING');

EXCEPTION WHEN NO_DATA_FOUND THEN

          DBMS_OUTPUT.PUT_LINE('############################################################################################');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Invalid CREST COURT ID Supplied - No DATA FOUND!!!!');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Please retry running C2X_DM_ETL_2 script with a Valid CREST COURT ID!');
          DBMS_OUTPUT.PUT_LINE('#                                                                                          #');
          DBMS_OUTPUT.PUT_LINE('############################################################################################');
END;
/

spool off
set termout off

WHENEVER SQLERROR EXIT
EXEC IF '&court_is_valid'='N' THEN RAISE_APPLICATION_ERROR(-20000,'INVALID COURT SUPPLIED, PLEASE RETRY...'); END IF;
   
/**************** set folder names ******************/

select crest_court_id as court_id,court_id as xhibit_court_id,
'court_'||crest_court_id||'_C2X_DM_ETL_2_log_files' as log_dir
from xhibit.xhb_court where crest_court_id = nvl('&&crest_court_id',NULL);
 
/******************* if running in UNIX - enable the below i.e.  create folders if not existing **************/

--!mkdir -p &log_dir

/*************  set filenames ******************/

select 'court_'||'&court_id'||'_C2X_DM_ETL_2_'||'&sysdt_var'||'.log' as log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_ETL_2_RESULTS_'||'&sysdt_var'||'.log' as result_log_file from dual;
select 'court_'||'&court_id'||'_C2X_DM_ETL_2_tmp_'||'&sysdt_var'||'.log' as tmp_file from dual;

/***************** spool to log file set header info ****************/

set termout on
spool &log_file

select '############################################################################################' from dual;
select '###              DATA MIGRATION ETL 2 SCRIPT LOG                                         ###' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Starting C2X_DM_ETL_2 script run for Court - '||'&court_id' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/*************** EXECUTE C2X_DM_ETL PROCEDURES and spool the results to the log file ***************/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_case_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_case_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_case_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_case_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_case_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_defendant_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_defendant_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_defendant_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_defendant_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_defendant_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_cpa_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_cpa_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_cpa_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_cpa_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_cpa_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_prsf_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_prsf_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_prsf_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_prsf_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_prsf_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_charges_log_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_charges_log_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_charges_log_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_charges_log_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_charges_log_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_order_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_order_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_legal_aid_order_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_order_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_order_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_amend_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_amend_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_legal_aid_amend_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_amend_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_legal_aid_amend_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_history_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_history_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_case_history_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_history_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_history_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_list_entry_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_list_entry_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_c_list_entry_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_list_entry_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_list_entry_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_diary_ne_no_case_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_diary_ne_no_case_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_diary_ne_no_case_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_diary_ne_no_case_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_diary_ne_no_case_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_diary_fixture_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_diary_fixture_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_c_diary_fixture_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_diary_fixture_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_c_diary_fixture_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_dir_for_case_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_dir_for_case_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_dir_for_case_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_dir_for_case_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_dir_for_case_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_nad_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_nad_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_case_nad_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_nad_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_nad_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_chamber_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_chamber_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_ref_chamber_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_chamber_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_chamber_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_rsf_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_mail_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_mail_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_rsf_mail_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_mail_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rsf_mail_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_jud_tckt_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_jud_tckt_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_ref_jud_tckt_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_jud_tckt_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_ref_jud_tckt_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_bw_history_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_bw_history_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_bw_history_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_bw_history_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_bw_history_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_mon_ord_track_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_mon_ord_track_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_mon_ord_track_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_mon_ord_track_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_mon_ord_track_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_site_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_site_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_court_site_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_site_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_court_site_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_croom_usage_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_croom_usage_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_croom_usage_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_croom_usage_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_croom_usage_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_jud_usage_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_jud_usage_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_jud_usage_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_jud_usage_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_jud_usage_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_def_history_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_def_history_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_def_history_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_def_history_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_def_history_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_lists_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_lists_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_lists_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_lists_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_lists_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_sit_on_list_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_sit_on_list_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_sit_on_list_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_sit_on_list_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_sit_on_list_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_case_on_list_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_on_list_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_on_list_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_doc_on_list_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_on_list_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_on_list_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_offence_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_offence_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_offence_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_offence_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_offence_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_history_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_history_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_doc_history_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_history_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_doc_history_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rpa_mail_with_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rpa_mail_with_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.update_xhb_rpa_mail_with_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rpa_mail_with_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.update_xhb_rpa_mail_with_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_out_bw_history_crest ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_out_bw_history_crest' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_out_bw_history_crest(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_out_bw_history_crest COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_out_bw_history_crest ***********/

/***************  START : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_fixture ***********/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>EXECUTING PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_fixture' from dual; 
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
data_mig.xhb_data_migration_process_pkg.upd_xhb_case_on_list_fixture(&court_id);
end;
/

spool off

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>PROCEDURE DATA_MIG.XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_fixture COMPLETED' from dual; 
select '                                                                                        ' from dual;
select '############################################################################################' from dual;
select '                                                                                        ' from dual;

spool off

/***************  END : XHB_DATA_MIGRATION_PROCESS_PKG.upd_xhb_case_on_list_fixture ***********/


/******************** all procedures DONE *****************/

set termout on
spool &log_file append

select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>C2X_DM_ETL_2 script run COMPLETE' from dual;
select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
select '----------------------------------------------------------------------------------------' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':>Now Calling script etl_data_migration_report.sql to generate DM report ...' from dual;
select '                                                                                        ' from dual;

spool off

set verify on
set feedback on

@etl_data_migration_report.sql &court_id  &xhibit_court_id 

set verify off
set feedback off

spool &log_file append

select '----------------------------------------------------------------------------------------' from dual;
select '                                                                                        ' from dual;
select to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> etl_data_migration_report.sql NOW complete, please check report ....' from dual;
select '                                                                                        ' from dual;
select '########################################################################################' from dual;
select '                                                                                        ' from dual;
EXIT
