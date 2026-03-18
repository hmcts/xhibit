/*
 * Filename:    EXISS_DB_Patch_8_8_7_2.sql (XLC functionality release)
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
 * 10/01/23		M.Harris		XLC4-115
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_8_8_7_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC4-115 */
@@exi_ref_type_insert.sql;

COMMIT;

spool off
