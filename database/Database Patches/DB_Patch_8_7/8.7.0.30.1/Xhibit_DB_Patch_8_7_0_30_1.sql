/*
 * Filename:    Xhibit_DB_Patch_8_7_0_30_1.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO          	COMMENT
 * ----         ---          	-------
 * 28-06-2018   B Hingston	ctx-2331 New XHB_Defendant_History table
 * 28-06-2018   B Hingston	ctx-2332 Updated XHB_REF_CRACKED_EFFECTIVE table
 * 28-06-2018	B Hingston	ctx-2341 Updated XHB_COURT table
 * 29-06-2018	B Hingston	ctx-2342 Updated xhb_disposal2 table
 * 29-06-2018 	B Hingston	ctx-2124 Update court tables
 * 02-07-2018	B Hingston	ctx-2358 Update xhb_def_hearing_record
 * 02-07-2018	B Hingston	ctx-2325 New Court_Room_Usage and Judge_usage tables
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_30_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* ctx-2331 */
@@XHB_Defendant_History_Create.sql;

/* ctx-2332 */
@@xhb_ref_cracked_effective_alter.sql;
@@xhb_ref_cracked_effective_update.sql;

/*ctx-2341 & ctx2124*/
@@xhb_court_alter.sql;

/*ctx-2342 */
@@xhb_disposal2_alter.sql;

/*ctx-2124 */
@@xhb_court_satellite_alter.sql;
@@xhb_court_site_alter.sql;

/*ctx-2358 */
@@xhb_def_hearing_record_alter.sql;

/*ctx-2325 */
@@xhb_court_room_usage_create.sql;
@@xhb_judge_usage_create.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.30.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.30.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.30.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.30.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.30.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


