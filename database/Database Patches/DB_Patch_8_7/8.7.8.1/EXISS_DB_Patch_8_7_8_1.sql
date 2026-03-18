/*
 * Filename:    EXISS_DB_Patch_8_7_8_1.sql (XLC functionality release)
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
 * 08/04/20	N Walters	XLC-21
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_8_7_8_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@exi_ref_type_update


COMMIT;

spool off
