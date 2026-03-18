/*
 * Filename:    Xhibit_DB_Patch_Monarch.sql
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
  select 'Xhibit_DB_Patch_Monarch_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


@@xhb_ref_disp_retention_policy_insert.sql;
@@insert_update_xhb_ref_system_code_ADVOCATE_TYPE.sql
@@update_xhb_system_code_ADVOCATE_TYPE.sql
@@insert_xhb_system_code_HO_PROC_BREACH.sql

@@update_ref_disp_menu_SVRO.sql

@@xhb_update_translation_data.sql
@@xhb_update_honours_data.sql
@@update_xhb_system_code_QC.sql

@@CJIT_database_schema_names.sql;

@@update_xhb_search_package.sql;

spool off
