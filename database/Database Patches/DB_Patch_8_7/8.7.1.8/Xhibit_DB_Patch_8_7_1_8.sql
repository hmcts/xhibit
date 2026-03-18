/*
 * Filename:    Xhibit_DB_Patch_8_7_1_8.sql (CREST to XHIBIT functionality release)
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
 * 26/03/2019	Mark Harris		ctx-3892
 * 26/03/2019 	Chris Vincent	CTX-3891 - Case Summary sreen change
 * 27/03/2019 	Chris Vincent	CTX-3896 - CTLRL and CTLRP changes
 * 27/03/2019	David Burden	CTX-3799 
 * 27/03/2019	David Burden	CTX-3907 
 * 28/03/2019	Mark Harris		CTX-3910
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_1_8_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-3891 */
@@xhb_case_pkg_h.sql;
@@xhb_case_pkg_b.sql;

/* CTX-3896, CTX-3907 */
@@xhb_report_pkg_b.sql;

/*ctx-3892*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*CTX-3799*/
@@xhb_get_distribution_list_xml_b.sql;

/*CTX-3910*/
@@xhb_formatting_pkg_h.sql;
@@xhb_formatting_pkg_b.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_offence_desc_update.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.1.8', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.1.8', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.1.8', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.1.8', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.1.8', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
