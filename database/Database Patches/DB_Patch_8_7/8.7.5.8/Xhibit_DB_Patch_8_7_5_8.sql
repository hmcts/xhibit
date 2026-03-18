/*
 * Filename:    Xhibit_DB_Patch_8_7_5_8.sql (CREST to XHIBIT functionality release)
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
 * 16/09/2019   S Sethuraman    CTX-4490, CTX-4491 - CASE Housekeeping Changes
 * 17/09/2019	Chris Vincent	CTX-4507, CTX-4508 - MISC Housekeeping Changes
 * 17/09/2019	S Sethuraman	CTX-4494, CTX-4495 - Case Housekeeping Chnages
 * 17/09/2019	Chris Vincent	CTX-4576 - NHA changes
 * 18/09/2019	C Vincent		CTX-4577, CTX-4580, CTX-4582 - ARO Change
 * 18/09/2019	C Vincent		CTX-4574 - housekeeping changes to ignore cases with no case history
 * 19/09/2019	Chris Vincent	CTX-4583 - NFIX changes
 * 20/09/2019	S Sethuraman	CTX-4498, CTX-4499, CTX-4500 - Listings Housekeeping Chnages
 * 20/09/2019	C Vincent		CTX-4581 - fixing delete case housekeeping constraint error
 * 23/09/2019	C Vincent		CTX-4592 - Splitting delete of XHB_DIARY_NOTE_ENTRY for performance
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_8_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4577, CTX-4580, CTX-4582 */
@@xhb_orders_pkg_b.sql;

/* CTX-4576, CTX-4583 */
@@xhb_report_pkg_b.sql;

/* CTX-4490, CTX-4491, CTX-4494, CTX-4495 */
@@ctx_4490_DML_Statement.sql;
@@ctx_4491_DML_Statement.sql;
@@ctx_4498_DML_Statement.sql;
@@ctx_4500_DML_Statement.sql;
@@xhb_housekeeping_pkg_h.sql;
@@xhb_housekeeping_pkg_b.sql;
@@XHB_CASE_BUR_TR.trg;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.8', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.8', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.8', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.8', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.8', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
