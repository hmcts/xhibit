/*
 * Filename:    Xhibit_DB_Patch_8_7_0_44_1.sql (CREST to XHIBIT functionality release)
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
 * 04-12-2018	C.Vincent		CTX-2941 - changes to the NHA report
 * 05-12-2018	N.Toft			CTX-3142
 * 05-12-2018	M.Harris		CTX-3250
 * 05-12-2018	M.Harris		CTX-3268
 * 05-12-2018	M.Newman		CTX-3156
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_44_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3156*/
@@xhb_security_group_role_insert.sql;

/* CTX-2941*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*CTX-3142*/
@@xhb_ref_advocate_h.sql;
@@xhb_ref_advocate_b.sql;

/*CTX-3250*/
/*CTX-3268*/
@@xhb_get_xml_reports_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.44.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.44.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.44.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.44.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.44.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
