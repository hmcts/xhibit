/*
 * Filename:    Xhibit_DB_Patch_8_7_5_0.sql (CREST to XHIBIT functionality release)
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
 * 19/07		N.Walters		Fixing issue with lists being cut off if the charges log is greater than 2000 (when it should be 4000)
 * 23/07/2019	C Vincent		Merging CTX-4420 changes from develop_R4 to develop_ongoing
 * 23/07/2019	C Vincent		XHB_CASE_LINKING_PKG.get_cases_by_group_number change to return case listing entry id for a case.  CTX-4426
 * 25/07/2019	C Vincent		xhb_get_xml_reports_b changes to handle NULL values in IS_COURT_ROOM_LIST_ENTRY.  CTX-4430
 * 26/07/2019	C Vincent		Change to sort order of List Results screen query.  CTX-4431
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-4423*/
@@xhb_report_pkg_h.sql;

/* CTX-4415, CTX-4414, CTX-4430 */
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_report_pkg_b.sql;

/* CTX-4420 */
@@xhb_terminal_pkg_h.sql;
@@xhb_terminal_pkg_b.sql;
@@xhb_search_pkg_b.sql;

/* CTX-4428 */
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-4426 */
@@xhb_case_linking_pkg_b.sql;

/* CTX-4431 */
@@xhb_listing_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
