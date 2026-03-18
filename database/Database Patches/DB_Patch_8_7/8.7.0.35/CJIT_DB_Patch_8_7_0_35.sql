/*
 * Filename:    CJIT_DB_Patch_8_7_0_35.sql (CREST to XHIBIT functionality release)
 *
 * To be run against CJIT schema
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 08/08/18		David Burden	ctx-2377
 */

set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DB_Patch_8_7_0_35_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-2377*/
@@CJI_DOCUMENT_TYPE_GRANT.sql

COMMIT;

spool off
