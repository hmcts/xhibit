/*
 * Filename:    Xhibit_DB_Patch_8_7_0_33.sql (CREST to XHIBIT functionality release)
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
 * 19/07/18		D.Burden		ctx-2375 Firm List Publish
 * 20/07/18	N.Walters	CTX-2446
 * 20/07/18	G.Brar		ctx-2311: prlis report bug
 * 20/07/2018	Chris Vincent	CTX-2382: new column on XHB_SCHEDULED_HEARING
 * 23/07/2018	John Uphill	CTX-1088: new column on XHB_CASE_ON_LIST
 * 25/07/18		J. Riley		ctx-2227: delete_case_ctx added to xhb_housekeeping
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_33_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-2372*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_list_distribution_pkg_b.sql;


/*ctx-2311*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-2382*/
@@xhb_scheduled_hearing_alter.sql;
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-1088*/
@@xhb_case_on_list_alter.sql;

/*ctx-2227*/
@@xhb_housekeeping_pkg_pks.sql;
@@xhb_housekeeping_pkg_pkb.sql;
COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.33', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.33', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.33', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.33', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.33', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
