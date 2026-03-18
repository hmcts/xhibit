/*
 * Filename:    CJIT_DB_Patch_8_8_0_6.sql (XLC release)
 *
 * To be run against CJIT schema
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 26/04/2021	B Hingston	initial vesion for 2021 Functional release
 */

set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DB_Patch_8_8_0_6_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


-- Update the table CJI_VERSION

UPDATE CJI_VERSION SET SCHEMA_VERSION='8_8_0', DISPLAY_NAME='CJSE Database Schema 8_8_0' WHERE SCHEMA_NAME='CJSE';

-- Update the table CJI_DOCUMENT_TYPE

@@CJIT_database_schema_names.sql;

COMMIT;

spool off
