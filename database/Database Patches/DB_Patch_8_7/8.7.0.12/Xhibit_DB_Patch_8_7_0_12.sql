/*
 * Filename:    Xhibit_DB_Patch_8_7_0_12.sql (CREST to XHIBIT functionality release)
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
 *  17/10/2017	G.Grewal	feature/CTX-597
 *  19/10/2017  G.Grewal    feature/CTX-598 
 *  23/10/2017  G.Grewal    feature/CTX-599
 *  26/10/2017  G.Grewal    feature/CTX-600
 *  27/10/2017  G.Grewal    feature/CTX-601
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_12_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* feature/CTX-597 */
@@xhb_security_role_insert.sql;
@@xhb_security_group_role_insert.sql; 

/* feature/CTX-598 */
@@xhb_ref_calendar_alter.sql;

/* feature/CTX-599 */
@@xhb_ref_chamber_alter.sql;

/* feature/CTX-600 */
@@xhb_ref_judge_ticket_create.sql;

/* feature/CTX-601 */
@@xhb_ref_solicitor_firm_alter.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.12', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.12', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.12', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.12', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.12', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
