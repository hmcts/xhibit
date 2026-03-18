/*
 * Filename:    EXISS_DB_Patch_Monarch.sql
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
 * 12/02/2025	S.Atwell		Monarch updates to be included in PDDA release
 */

set echo on
set term off
column filename new_value spool_filename
  select 'EXISS_DB_Patch_Monarch_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@exi_ref_type_update.sql;

COMMIT;

spool off
