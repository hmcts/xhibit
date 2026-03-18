/*
 * Filename:    Xhibit_DB_Patch_8_7_0_40.1.sql (CREST to XHIBIT functionality release)
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
 * 30/10/2018	N.Walters		adding proc for ref data
 * 17-10-2018	B.Hingston	CTX-2766 - population of XHB_REF_EVENT_DESCRIPTION
 * 01-11-2018   J. Riley        CTX-2925 and CTX-2927 Add custody case indicator (*) and an outer join to UNLC report
 * 01-11-2018	D.Burden		CTX-2939
 * 01-11-2018 	D.Burden	CTX-2760
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_40_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-617 */
@@xhb_ref_chamber_pkg_h.sql;
@@xhb_ref_chamber_pkg_b.sql;

/*ctx-2766*/
@@XHB_REF_EVENT_DESCRIPTION_Insert.sql;
/*ctx-2645*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;


/* CTX-2925, CTX-2927 * CTX-2948/
@@xhb_report_pkg_h.sql
@@xhb_report_pkg_b.sql
/*ctx-2741*/
@@xhb_view_schedule_pkg_b

/*ctx-2760*/
@@xhb_get_xml_reports_b.sql;

/*ctx-2939*/
@@xhb_view_schedule_pkg_b.sql

/*ctx-2872*/
@@xhb_report_pkg_b.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.40.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.40.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.40.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.40.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.40.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off