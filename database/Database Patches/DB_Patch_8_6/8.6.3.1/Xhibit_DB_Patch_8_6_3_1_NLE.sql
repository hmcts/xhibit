/*
 * Filename:    Xhibit_DB_Patch_8_6_3.sql (Spring release 2015)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 *  23/09/2015	S Atwell	Changes for DB for NLE only release 8.6.3.1
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_6_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@xhb_nle_managed_cases_create.sql;

@@xhb_data_reset_pkg_h.sql;
@@xhb_data_reset_pkg_b.sql;

COMMIT;
spool off


