set termout off
set echo off
set serveroutput off
set headsep off
set verify off

column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_PostETLGoLive_allCourts_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/

set echo on
set verify on
set term on
spool &&spool_filename

@@execute_xhb_populate_ref_chamber_all.sql;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;
@@execute_xhb_pop_xhb_offence_crest_seq.sql;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;

COMMIT;