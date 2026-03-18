/*
 * Filename:    CJIT_DB_Patch_Monarch.sql
 *
 * To be run against CJIT schema
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
  select 'CJIT_DB_Patch_Monarch_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


-- Update the table CJI_VERSION

UPDATE CJI_VERSION SET SCHEMA_VERSION='8_8_8', DISPLAY_NAME='CJSE Database Schema 8_8_8' WHERE SCHEMA_NAME='CJSE';

-- Update the table CJI_DOCUMENT_TYPE

@@CJIT_database_schema_names.sql;

COMMIT;

spool off
