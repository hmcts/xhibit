/*
 * Filename:    Xhibit_DB_Patch_8_7_0_21.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO         COMMENT
 * ----         ---         -------
 * 28/02/2018   AR          Drop the xhb_ref_email_recipients table (ctx-1466)
 * 06/03/2018   AR          Drop bench_warrant & bench_warrant_def_Sentence columns from xhb_case table (ctx-1468)
 * 06/03/2018   AR          Drop list_date_sent & list_date_received from xhb_def_on_case_ref_sol_firm table (ctx-1470)
 * 06/03/2018   AR          Drop list_date_sent & list_date_received from xhb_prosecutor_ref_sol_firm table (ctx-1472)
 * 06/03/2018   AR          Drop the solicitor_reference, ref_solicitor_firm_id, rep_start_date & rep_end_date
 *                            columns from the xhb_legal_aid_order table (ctx-1474)
 * 06/03/2018   AR          Drop change_type_id column in the xhb_legal_aid_amendment table (ctx-1476)
 * 05/03/2018   Chris Cash  Add the dmi_cad_log tables (ctx-1539)
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_21_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* feature/ctx-1466 */
@@xhb_ref_email_recipients_drop.sql

/* feature/ctx-1468 */
@@xhb_case_alter.sql

/* feature/ctx-1470 */
@@xhb_def_on_case_ref_sol_firm_alter.sql

/* feature/ctx-1472 */
@@xhb_prosecutor_ref_sol_firm_alter.sql

/* feature/ctx-1474 */
@@xhb_legal_aid_order_alter.sql

/* feature/ctx-1476 */
@@xhb_legal_aid_amendment_alter.sql

/* feature/ctx-1539 */
@@xhb_dmi_cad_ref_code_create.sql;
@@xhb_dmi_cad_run_history_create.sql
@@xhb_dmi_cad_run_log_create.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.21', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.21', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.21', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.21', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.21', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
