/*
 * Filename:    Xhibit_DB_Patch_8_7_0_43.sql (CREST to XHIBIT functionality release)
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
 * 20-11-2018	N.Toft			CTX-3141
 * 19-11-2018	M.Harris		CTX-3075
 * 20-11-2018	M.Harris		CTX-2999
 * 20-11-2018	L.Westall		CTX-3158
 * 21-11-2018   L.Westall		CTX-3117/CTX-3121
 * 21-11-2018	C.Vincent		CTX-3129 changes to the NTRSF report
 * 22-11-2018	C.Vincent		CTX-3188 changes to the NFIX Report
 * 23-11-2018   L.Westall       CTX-3183
 * 23-11-2018	C.Vincent		CTX-3164 database trigger change required for the LFIX report
 * 24-11-2018	D.Burden		CTX-3090
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_43_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3141*/
@@xhb_ref_advocate_crest_seq_create.sql;

/*CTX-3158*/
@@xhb_report_pkg_b.sql;

/*CTX-2999*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
@@xhb_get_distribution_list_xml_h.sql;
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-3129, CTX-3188, CTX-3117, CTX-3121 */
@@xhb_report_pkg_b.sql;

/* CTX-3183 */
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;

/* CTX-3164 */
@@xhb_casediaryfixture_bur_tr.sql
/*ctx-3090*/
@@xhb_recordsheet_table.sql;
@@xhb_recordsheet_bir_tr.sql;
@@xhb_recordsheet_bur_tr.sql;
@@xhb_wmb_message_route_inserts.sql;
@@XHB_RECORDSHEET_PREVIEW_I_WSM.sql;
@@XHB_RECORDSHEET_PREVIEW_U_WSM.sql;


COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.43', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.43', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.43', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.43', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.43', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off