/*
 * Filename:    NLE_DB_Patch_8_7_0_PreGoLive.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 05-07-2019	N Walters	CTX-4385
 */


set echo on
set term off
column filename new_value spool_filename
  select 'NLE_DB_Patch_8_7_0_PreGoLive'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set verify on
set term on
spool &&spool_filename

@@NLE_ONLY_update_video_link_required.sql;
select to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS') from dual;

COMMIT;

spool off
