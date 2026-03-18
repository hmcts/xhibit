/*
 * Filename:    Xhibit_DB_Patch_8_7_0_16.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 * 06/11/2017	J.Boparai	Database package update new store procedure (get_ref_calendar)
 * 21/11/2017	M.harris	feature/CTX-569
 * 06/12/17		Chris Kudzin	feature/ctx-215
 *  19/12/2017  David Burden CTX-880 Added NFIX report to XHB_REPORT_PKG
 * 18/12/2017	Ross Edwards	Case Update Prosecutor Agency OBS_IND column adding
 * 20/12/2017   C.Cash		CTX-713 and CTX-1305 
 * 04/01/2018   C.Cash		CTX-924 and 1326
 * 11/01/2018   M.Harris	Include CASE_DIARY_FIXTURE as foreign key references are required
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_16_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*feature/CTX-569*/
@@xhb_case_diary_fixture_create.sql;

/*Feature CTX-188 prosecutor updates*/
@@xhb_case_prosecutor_agency_alter.sql
/*feature/CTX-867*/
@@xhb_report_pkg
@@XHB_REPORT_LOG


/* feature/CTX-737 */
@@xhb_ref_calendar_alter.sql

/*feature/CTX-215*/
@@XHBSearchPkg.sql;

/*feature/CTX-1325*/
@@xhb_case_non_avail_days_create.sql;

@@dmi_cad_error_log_tbl.sql
@@xhb_housekeeping_pkg.sql

/*CTX-924*/
@@xhb_case_listing_entry.sql;
@@xhb_diary_note_entry.sql;
@@xhb_ref_listing_data.sql;

/* feature/CTX-1324 */
@@xhb_list_create.sql

/* feature/CTX-926 */
@@xhb_case_on_list_create.sql

/*feature/CTX-927*/
@@xhb_sitting_on_list_create.sql;


/* feature/CTX-1326 */
@@create_xhb_fixture_deft_attending.sql

COMMIT;


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.16', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.16', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.16', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.16', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.16', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off