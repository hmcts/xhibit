/*
 * Filename:    EXISS_DB_Patch_8_8_5_0.sql (XLC functionality release)
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
 * 01/04/22		M.Harris		XLC4-48
 * 13/04/22		L.Gittins		XLC4-61
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_8_8_5_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC4-48 */
@@exi_ref_type_insert.sql;
/* XLC4-61 */
@@exi_ref_type_update.sql;

COMMIT;

spool off
