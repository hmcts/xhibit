/*
 * Filename:    Xhibit_DB_Patch_8_7_0_18.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 * 24/01/2018   C.Kudzin	CTX-1374 prosecutor ref sol firm and def on case ref sol firm changes - adding OBS_Ind
 * 21/11/2017	M.harris	feature/CTX-569
 * 12/01/2018   C.Cash		CTX-1340 DDl and trigger changes
 * 28/01/2018   G.Grewal    feature/CTX-1393 XHB_REF_JUDGE_TICKET add COURT_ID and OBS_IND
 * 29/01/2018   C.Cash		ctx-1392 changes.  Added indexes and dropped xhb_time_rqmt_deft_attending
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_18_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/* feature/ctx-1374 */
@@xhb_prosecutor_ref_sol_firm_alter.sql
@@xhb_def_on_case_ref_sol_firm_alter.sql

/* feature/CTX-569 */
@@xhb_case_alter.sql;
@@xhb_defendant_on_case_alter.sql;
@@xhb_case_diary_time_rqmt_drop.sql; 
@@xhb_case_listing_entry_alter.sql;
@@xhb_ref_listing_data_insert.sql;

/* feature/ctx-1340 */
@@xhb_diary_note_entry_alter.sql;

/* feature/ctx-1393 */
@@xhb_ref_judge_ticket_alter.sql;

/* feature/ctx-1392 */
@@create_indexes_listings.sql;
@@xhb_time_rqmt_deft_attending_drop.sql;

/* feature/ctx-1400*/
@@xhb_report_pkg.sql;


COMMIT;

DELETE FROM XHB_VERSION;


INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.18', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.18', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.18', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.18', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.18', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
