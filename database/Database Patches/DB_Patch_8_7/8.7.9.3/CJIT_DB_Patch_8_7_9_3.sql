/*
 * Filename:    CJIT_DB_Patch_8_7_9_3.sql (XLC release)
 *
 * To be run against CJIT schema
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 04/06/20	N Walters	Merging in Brian's code
 */

set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DB_Patch_8_7_9_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@CJIT_database_amendments.sql

COMMIT;

spool off
