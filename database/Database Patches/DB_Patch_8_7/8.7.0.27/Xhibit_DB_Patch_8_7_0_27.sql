/*
 * Filename:    Xhibit_DB_Patch_8_7_0_27.sql (CREST to XHIBIT functionality release)
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
 * 03-5-2018    C.Cash          ctx-1992 - xhb_ref_sol_firm alter
 * 25-5-2018	C.Cash			ctx-1915
 * 25-5-2018	C.Cash			ctx-1916
 * 25-5-2018	C.Kudzin		ctx-2108
 * 26-5-2018	C.Cash		    ctx-2084
 * 05-6-2018    G.Brar          ctx-2081
 * 11-6-2018    G.Brar          ctx-2081
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_27_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-1915*/
@@xhb_defendant_on_case_alter.sql;

/*ctx-2108*/
@@xhb_sys_audit_insert.sql;

/*ctx-2084*/
@@xhb_case_on_list_alter.sql;

/*ctx-2081*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;
/*CTX 1992*/
@@xhb_ref_solicitor_firm_alter.sql;



COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.27', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.27', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.27', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.27', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.27', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


