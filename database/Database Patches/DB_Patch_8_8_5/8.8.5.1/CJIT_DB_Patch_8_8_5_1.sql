/*
 * Filename:    CJIT_DB_Patch_8_8_5_1.sql (XLC release)
 *
 * To be run against CJIT schema
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 11/05/2022	B Hingston	PCSC Bill release
 */

set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DB_Patch_8_8_5_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


-- Update the table CJI_VERSION

UPDATE CJI_VERSION SET SCHEMA_VERSION='8_8_5', DISPLAY_NAME='CJSE Database Schema 8_8_5' WHERE SCHEMA_NAME='CJSE';

-- Update the table CJI_DOCUMENT_TYPE

@@CJIT_database_schema_names.sql;

COMMIT;

spool off
