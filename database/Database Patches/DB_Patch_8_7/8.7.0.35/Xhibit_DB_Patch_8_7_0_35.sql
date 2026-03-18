/*
 * Filename:    Xhibit_DB_Patch_8_7_0_35.sql (CREST to XHIBIT functionality release)
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
 * 08/08/18		David Burden	ctx-2377
 * 16/08/18     GBrar           ctx-2346 ADJSS - There is no data on List field date though Report is listed
 * 22/08/18     J.Uphill        ctx-1064 - Updates to XHB_LISTING_PKG, XHB_CASE_DIARY_FIXTURE and XHB_REF_LISTING_DATA
 * 23/08/18		C Vincent		ctx-2599 - removing procedure from xhb_listing_pkg
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_35_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-2377*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_list_distribution_pkg_b.sql;

/* ctx-2346 */
@@xhb_report_pkg_h.sql
@@xhb_report_pkg_b.sql

/*CTX-1064*/
@@xhb_case_diary_fixture_indexes.sql;
@@xhb_ref_listing_data_insert.sql;

/* ctx-2599 */
@@xhb_listing_pkg_h.sql
@@xhb_listing_pkg_b.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.35', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.35', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.35', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.35', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.35', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
