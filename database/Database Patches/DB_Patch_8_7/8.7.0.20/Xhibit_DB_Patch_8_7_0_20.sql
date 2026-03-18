/*
 * Filename:    Xhibit_DB_Patch_8_7_0_20.sql (CREST to XHIBIT functionality release)
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
 * 09/02/18     M. Newman	CTX-1447: Amending XHB_DEFENDANT.CURRENT_PRISON_STATUS from nullable to not nullable. 
 * 23/02/18		G. Grewal	CTX-1116: Removed two columns from XHB_REF_CALENDAR.
 * 23/02/18		G. Grewal	CTX-1594: Alter tables xhb_ref_judge_ticket and audit table to make court_id not null.
 * 23/02/18		G. Grewal   CTX-1492: Move two columns to correct position in the trigger.
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_20_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* feature/ctx-1116 */
@@xhb_ref_calendar_alter.sql

/* defect/ctx-1594 */
@@xhb_ref_judge_ticket_alter.sql

/* defect/ctx-1492 */
@@xhb_ref_solicitor_firm_bur_tr.sql

/* CTX-1447 */
/* SA - Will not run this script any more as part of main release weekend for performance reasons - it will be run as part of a separat release */
/* @@xhb_bw_history_alter.sql */

COMMIT;


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.20', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.20', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.20', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.20', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.20', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
