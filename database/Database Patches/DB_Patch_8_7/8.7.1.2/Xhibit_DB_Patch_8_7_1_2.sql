/*
 * Filename:    Xhibit_DB_Patch_8_7_1_2.sql (CREST to XHIBIT functionality release)
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
 * 11/02/2019 	David Burden	CTX-3669
 * 13/02/2019 	Chris Vincent	CTX-3689 - New column to XHB_DEF_ON_CASE_ON_LIST
 * 14/02/2019	Chris Vincent	CTX-3691 - Changes to XHB_GET_XML_REPORTS for the new column introduced in CTX-3689
 * 14/02/2019	Chris Vincent	CTX-3692 - XHB_LIST_DISTRIBUTION_PKG changes
 * 13/02/2019 	M. Newman		CTX-3694
 * 15/02/2019	M.Harris		CTX-3697
 * 18/02/2019	Chris Vincent	CTX-3718 - Changes to the Listings Package
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_1_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-3689 */
@@xhb_def_on_case_on_list_alter.sql;

/* CTX-3691 */
@@xhb_get_xml_reports_b.sql

/*ctx-3697*/
@@xhb_directions_for_case_delete.sql;
@@xhb_directions_for_case_alter.sql;

/* ctx-3718 */
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-3669*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* CTX-3692, CTX-3694 */
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.1.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.1.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.1.2', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.1.2', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.1.2', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
