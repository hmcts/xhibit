/*
 * Filename:    Xhibit_DB_Patch_8_7_0_44.sql (CREST to XHIBIT functionality release)
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
 * 26-11-2018	C.Vincent		CTX-3193 - changes to the RRCA report
 * 28-11-2018	D.Burden		CTX-3093
 * 28-11-2018	C.Kudzin		CTX-3221
 * 28-11-2018	D.Burden		CTX-3014
 * 28-11-2018	J.Riley			CTX-3151
 * 29-11-2018	G.Brar			CTX-3222 database trigger change required for the LFIX report
 * 29-11-2018   C.Vincent		CTX-3202 changes for the record courtroom statistics screen
 * 29-11-2018   G.Brar		    CTX-3241
 * 30-11-2018	N.Toft			CTX-3154
 * 30-11-2018	C.Vincent		CTX-3199 - changes to the CFIX query in the reports package
 * 30-11-2018	C.Vincent		CTX-3226 changes to the RAGE report query
 * 02-12-2018	N.Toft			CTX-3153
 * 02-12-2018	N.Toft			CTX-3152
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_44_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3193, ctx-3014, ctx-3199, ctx-3226*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-3014*/
@@xhb_record_sheet_pkg_h.sql
@@xhb_record_sheet_pkg_b.sql

/*CTX-3221*/
/*CTX-3256*/
@@xhb_list_distribution_pkg.sql;
@@xhb_list_distribution_pkg_b.sql;


/*CTX-3221 CTX-3241 */
@@xhb_report_pkg_b.sql;

/* CTX-3222 */
@@xhb_casediaryfixture_bur_tr.sql;

/*CTX-3151, CTX-3154, CTX-3153, CTX-3152*/
@@xhb_ref_advocate_h.sql;
@@xhb_ref_advocate_b.sql;

/* CTX-3202 */
@@xhb_rcs_pkg_h.sql
@@xhb_rcs_pkg_b.sql
@@XHB_COURT_ROOM_USAGE_BIR_TR.sql
@@XHB_COURT_ROOM_USAGE_BUR_TR.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.44', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.44', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.44', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.44', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.44', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
