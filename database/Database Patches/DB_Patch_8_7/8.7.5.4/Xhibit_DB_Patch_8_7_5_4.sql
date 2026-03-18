/*
 * Filename:    Xhibit_DB_Patch_8_7_5_4.sql (CREST to XHIBIT functionality release)
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
 * 16/08/19		N Walters		CTX-4468
 * 20/08/2019	C Vincent		CTX-4510 - ARO currency formatting fix
 * 20/08/2019	C Vincent		CTX-4488 - Annotated Warned List change
 * 22/08/2019	N Walters		CTX-4513 - defid live issue
 * 22/08/2019	N Walters		CTX-4450 - fixing minor issue
 * 23/08/2019	C Vincent		CTX-4517 - RREC start date fix
 * 23/08/2019	C Vincent		CTX-4496 - Reports omitting certain case statuses
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_4_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4485 */
@@xhb_get_xml_reports_h.sql;

/* CTX-4485, CTX-4468, CTX-4488 */
@@xhb_get_xml_reports_b.sql;

/* CTX-4510 */
@@xhb_orders_pkg_b.sql;

/* CTX-4468*/
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-4513*/
@@xhb_defendant_bir_tr.sql

/*CTX-4450*/
@@xhb_courtel_list_bir_tr.sql;

/* CTX-4496 */
@@xhb_report_pkg_h.sql;

/* CTX-4496, CTX-4517 */
@@xhb_report_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.4', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.4', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.4', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.4', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.4', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
