/*
 * Filename:    CJI_DB_Patch_8_7_0_45.sql (CREST to XHIBIT functionality release)
 *
 *
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 10-12-2018	S.Atwell		Missign script for 2 new orders in CJI_DOCUMENT_TYPE
 */


set echo on
set term off
column filename new_value spool_filename
  select 'CJI_DB_Patch_8_7_0_45_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@cji_document_type_new_orders.sql;

COMMIT;

spool off
