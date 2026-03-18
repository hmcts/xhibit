/*
 * Filename:    Xhibit_DB_Patch_8_7_0_41.sql (CREST to XHIBIT functionality release)
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
 * 05-11-2018 	D.Burden		CTX-2955
 * 05-11-2018   J. Riley        CTX-2895, 2963 Fixes to NFIX report, and function getPhoneNum 
 * 06-11-2018	J.Boparai		CTX-2645
 * 07-11-2018   J. Riley        CTX-2964 Fix to RRCA report (filter on end_date)
 * 06-11-2018	D.Burden		CTX-3004
 * 07-11-2018	N.Walters		CTX-3025 and CTX-3026
 * 08-11-2018	C.Vincent		CTX-2973 fix to CTLRP report
 * 08-11-2018	C.Vincent		CTX-3041 fix to OUTC and UNLC reports
 * 08-11-2018	C.Vincent		CTX-2822, CTX-2839, CTX-2846, CTX-2855, CTX-2856 for LFIX report
 * 09-11-2018	J.Parthiban		CTX-2941
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_41_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*ctx-2955*/
@@xhb_get_xml_reports_b.sql;

/* ctx-2895, CTX-2963, ctx-2645, CTX-2973, CTX-3041, CTX-2822, CTX-2839, CTX-2846, CTX-2855, CTX-2856, ctx-2964 */
/*ctx-2645. ctx-2941*/

@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-3004*/
@@xhb_get_distribution_list_xml_b.sql;

/*ctx-3025 and 3026*/
@@xhb_config_prop_insert.sql;
COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.41', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.41', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.41', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.41', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.41', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off