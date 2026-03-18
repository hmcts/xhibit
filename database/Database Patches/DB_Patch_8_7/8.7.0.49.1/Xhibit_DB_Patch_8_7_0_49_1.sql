/*
 * Filename:    Xhibit_DB_Patch_8_7_0_49.1.sql (CREST to XHIBIT functionality release)
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
 * 07/01/2019	M.Newman		CTX-3319 REDEL package changes
 * 04/01/2019	L.Westall		CTX 3459 Added Trial Time Est.
 * 04/01/2018   M.Harris        ctx-3460 
 * 07/01/2019	J.Boparai		CTX 3476 To Display charges assigned to cases (Running List report)
 * 08/01/2019	D.Burden		CTX-3500
 *08/01/2019    E.Patterson CTX-3497
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_49_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3395*/
@@update_cji_document_type_data.sql;

/* ctx-3460 */
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/*CTX-3319*/
@@xhb_redel_pkg_h.sql;
@@xhb_redel_pkg_b.sql;

/*ctx-3476*/
@@xhb_report_pkg_h.sql;

/*ctx-3476*/
@@xhb_report_pkg_b.sql;


/*ctx-3500*/
@@update_xhb_ref_cracked_effective.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.49.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.49.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.49.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.49.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.49.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
