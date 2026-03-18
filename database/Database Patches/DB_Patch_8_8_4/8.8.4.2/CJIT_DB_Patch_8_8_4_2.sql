/*
 * Filename:    CJIT_DB_Patch_8_8_4_2.sql (XLC release)
 *
 * To be run against CJIT schema
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 10/03/2022	B Hingston	Terrorism & Sentencing release
 */

set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DB_Patch_8_8_4_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


-- Update the table CJI_VERSION

UPDATE CJI_VERSION SET SCHEMA_VERSION='8_8_4', DISPLAY_NAME='CJSE Database Schema 8_8_4' WHERE SCHEMA_NAME='CJSE';

-- Update the table CJI_DOCUMENT_TYPE

@@CJIT_database_schema_names.sql;

COMMIT;

spool off
