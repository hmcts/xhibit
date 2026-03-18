/*
 * Filename:    Xhibit_DB_Patch_8_7_0_40.sql (CREST to XHIBIT functionality release)
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
 * 22/10/2018	C.Vincent		Change to procedure for report RELCJ for CTX-2817
 * 24/10/2018   J.Riley         Change to procedure for report RELCJ for CTX-2816
 * 23/10/2018	M.Harris		CTX-2841 Add new prison location to prison list
 * 22/10/2018	C.Cash  		Create procedure for ctx-2896
 * 24/10/2018	J.Riley 		CTX-2703 Correction to court address in NHA report 
 * 25/10/2018	J.Uphill		Change to procedures for storing xml documents for CTX-2911
 * 25/10/2018	C.Vincent		Change to procedure for report PRLIS for CTX-2843
 * 24/10/2018	J.Riley 		CTX-2885 Correction to sort order in DRSR report 
 * 25/10/2018	C.Vincent		Change to procedure for report CTLRL and CTLRP for CTX-2932
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_40_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-2841 */
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/* CTX-2911 */
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;

/* CTX-2817, CTX-2816, CTX-2703, ctx-2885, CTX-2843, CTX-2911, CTX-2932 */
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* CTX-2896 */
@@xhb_populate_rcs_screen.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.40', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.40', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.40', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.40', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.40', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
