/*
 * Filename:    Xhibit_DB_Patch_8_7_2_6.sql (CREST to XHIBIT functionality release)
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
 * 30/04/2019	David Burden	CTX-4121
 * 02/05/2019	Chris Vincent	CTX-4159, CTX-4165 (RREC changes)
 * 02/05/2019	Chris Vincent	CTX-4172 (ADJSS and DEFSS change)
 * 02/05/2019	Chris Vincent	CTX-4171 (CTLRP/CTLRL report changes)
 * 02/05/2019	Chris Vincent	CTX-4183 (RREC changes)
 * 02/05/2019	N Walters	CTX-4170,4141
 * 08/05/2019	Mark Harris		CTX-4085
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_2_6_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-4141*/
@@xhb_get_xml_reports_h.sql;


/*ctx-4121, 4170, 4141*/
@@xhb_get_xml_reports_b.sql;

/* CTX-4159, CTX-4165, CTX-4172, CTX-4171, CTX-4183, CTX-4085 */
@@xhb_report_pkg_b.sql;

/*CTX-4085*/
@@create_xhb_case_committal_date_index.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.2.6', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.2.6', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.2.6', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.2.6', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.2.6', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
