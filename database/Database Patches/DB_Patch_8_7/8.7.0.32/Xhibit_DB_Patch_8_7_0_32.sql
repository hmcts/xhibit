/*
 * Filename:    Xhibit_DB_Patch_8_7_0_32.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO          	COMMENT
 * ----         ---          	-------
 * 05/07/18		M. Newman		ctx-2034: Manual case unlinking stored proc updates
 * 10/07/18		D.Burden		ctx-2372
 * 12/07/18		C.Vincent		ctx-2282: xhb_listing_pkg changes for List Results screen
 * 17/07/18		B.Hingston		ctx-2406: xhb_def_hearing_record changes
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_32_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-2034*/
@@xhb_case_linking_pkg.sql;
@@xhb_case_linking_pkg_body.sql;

/*ctx-2282*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-2372*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;
@@xhb_view_schedule_pkg_b.sql;


/*ctx-1127*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-1289*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-2406*/
@@xhb_def_hearing_record_alter.sql;




COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.32', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.32', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.32', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.32', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.32', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


