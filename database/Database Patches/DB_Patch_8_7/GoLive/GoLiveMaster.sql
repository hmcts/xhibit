ACCEPT in_xhb_court_id PROMPT "Enter XHIBIT Court ID : "
set termout off
set echo off
set serveroutput off
set headsep off
set verify off

column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_GoLive_CourtID_'||'&&in_xhb_court_id'||'_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual;

set term on
spool &&spool_filename

column court_name new_val court_name

SELECT court_name as court_name from xhibit.xhb_court where court_id = nvl(&&in_xhb_court_id,NULL);

SELECT ' ' from dual;

EXEC IF '&&court_name' is NULL THEN DBMS_OUTPUT.PUT_LINE('Supplied XHIBIT COURT ID '||&&in_xhb_court_id ||' is NOT Valid, please retry with a valid XHIBIT_COURT_ID...'); DBMS_OUTPUT.PUT_LINE('                           '); END IF;

SELECT ' ' from dual;

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&&court_name' is NULL THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Supplied XHIBIT Court ID is InValid ... Please retry with Valid XHIBIT Court_ID ...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); RAISE_APPLICATION_ERROR(-20000,'Invalid XHIBIT Court ID '||&&in_xhb_court_id||'  supplied, please retry with valid Court ID ...'); END IF;

set term on
spool &&spool_filename append

ACCEPT  in_confirm_court PROMPT "You have selected &court_name , Please confirm in capitals (YES/NO) : "

EXEC IF '&&in_confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); END IF;

set termout off
spool off

WHENEVER SQLERROR EXIT
EXEC IF '&in_confirm_court'<>'YES' THEN DBMS_OUTPUT.PUT_LINE('############################################################################################'); DBMS_OUTPUT.PUT_LINE('#                                                                                          #'); DBMS_OUTPUT.PUT_LINE(to_char(sysdate,'DD/MM/YYYY-HH24:MI:SS')||':> Script cancelled as COURT not Confirmed, please run again if needed...'); DBMS_OUTPUT.PUT_LINE('##'); DBMS_OUTPUT.PUT_LINE('############################################################################################'); RAISE_APPLICATION_ERROR(-20000,'Script cancelled as COURT not confirmed, please run again if needed...'); END IF;

set echo on
set verify on
set term on
spool &&spool_filename append
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_xhb_offence.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_xhb_defendant.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_xhb_def_hearing_record.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_xhb_case.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_no_def.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_set_case_number_sequences.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_ref_system_codes_update.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_populate_pub_running_list.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_populate_ref_system_code.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_ref_system_code.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_populate_ref_calendar.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_load_xhb_ref_hearing_types.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_fix_xhb_address_postcode.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_dm_update_report_log.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_dm_upd_misc_app_cases.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_update_xhb_charge.sql &&in_xhb_court_id;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;

COMMIT;
spool off
