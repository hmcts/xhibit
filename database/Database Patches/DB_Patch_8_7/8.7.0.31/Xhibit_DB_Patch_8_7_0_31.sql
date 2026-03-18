/*
 * Filename:    Xhibit_DB_Patch_8_7_0_31.sql (CREST to XHIBIT functionality release)
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
 * 25/06/2018	M.Newman		CTX-1996 stored procedures
 * 14-6-2018    G.Grewal		ctx-644
 * 19-6-2018    N.Toft			ctx-670
 * 26/06/2018	C.Vincent		CTX-2280
 * 20-06-2018   M.Harris        ctx-2269 and ctx-2270
 * 26/06/2018	D.Burden		CTX-1206
 * 29/06/2018	C.Vincent		CTX-2261
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_31_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-1996*/
@@xhb_case_linking_pkg.sql
@@xhb_case_linking_pkg_body.sql

/*ctx-2280*/
@@xhb_case_on_list_alter.sql;
@@xhb_case_diary_fixture_alter.sql

/*ctx-644, ctx-2259 and ctx-2270*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;
/*ctx-2269*/

/*ctx-644, ctx-2259 and ctx-2270*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;
/*ctx-2269*/
@@xhb_list_last_unpublished_v_drop.sql;
@@xhb_list_last_published_v_drop.sql;

/*CTX-2261*/
@@xhb_case_pkg_h.sql;
@@xhb_case_pkg_b.sql;

/*ctx-670*/
@@update_triggers_for_crestid.sql;

/*ctx-2161*/
@@xhb_case_alter.sql;
@@aud_case_alter.sql;
@@xhb_case_ai_tr.sql;
@@xhb_case_bur_tr.sql;
/*ctx-1206*/
@@xhb_view_schedule_pkg_h.sql;
@@xhb_view_schedule_pkg_b.sql;
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* ctx-2401 */
@@xhb_document_recipient_alter.sql

/* case linking */
@@xhb_security_role_insert.sql;
@@xhb_security_group_role_insert;

/*CTX-2344*/
@@xhb_disposal2_bir_tr.sql;
@@xhb_disposal2_bur_tr.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.31', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.31', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.31', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.31', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.31', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


