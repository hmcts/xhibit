/*
 * Filename:    EXISS_DB_Patch_8_8_1_0.sql (XLC functionality release)
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
 * 08/07/21		M.Harris		XLC3-3
 * 12/07/21		M.Harris		XLC3-9
 * 12/07/21		M.Harris		XLC3-10
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_8_8_1_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@exi_ref_type_insert.sql;
@exi_ref_type_update.sql;

COMMIT;

spool off
