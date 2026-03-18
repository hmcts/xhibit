/*
 * Filename:    Xhibit_DB_Patch_8_7_0_34.sql (CREST to XHIBIT functionality release)
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
 * 03/08/18     AndyR           ctx-2473 XHB_DEF_HEARING_RECORD.FORMA_COURT_CLERK
 * 06/08/18		David Burden	ctx-1101
 * 06/08/18     M.Harris        ctx-2259 Update the XHB_LISTING_PKG and XHB_CASE_LINKING_PKG
 * 07/08/18		Chris V			CTX-2483 changes to xhb_case_pkg for miscellaneous appeal cases
 * 08/08/18     John Riley      ctx-2145 RUMO Report SP
 * 10/08/18		Chris V			CTX-2521 introduce new column on XHB_CASE_ON_LIST
 * 13/08/18		J.Boparai		ctx-2128 OBW store procedure update
 * 16/08/18     C.Cash		ctx-2542 - Created branch for Abe Dennis
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_34_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-2483*/
@@xhb_case_pkg_b.sql;

/*ctx-2372*/
@@xhb_def_hearing_record_alter.sql;

/*ctx-1101*/
@@xhb_get_xml_reports_b.sql;

/*CTX-2521*/
@@xhb_case_on_list_alter.sql;

/*CTX-2259*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;
@@xhb_case_linking_pkg_b.sql;

/* ctx-2145 */
@@xhb_report_pkg_h.sql
@@xhb_report_pkg_b.sql

/* ctx-2542 */
@@wmb_message_pkg.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.34', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.34', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.34', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.34', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.34', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
