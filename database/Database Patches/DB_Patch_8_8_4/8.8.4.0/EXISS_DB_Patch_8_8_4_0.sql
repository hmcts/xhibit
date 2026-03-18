/*
 * Filename:    EXISS_DB_Patch_8_8_4_0.sql (XLC functionality release)
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
 * 16/02/22		M.Harris		XLC4-3
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_8_8_4_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC4-3 */
@@exi_ref_type_insert.sql;

COMMIT;

spool off
