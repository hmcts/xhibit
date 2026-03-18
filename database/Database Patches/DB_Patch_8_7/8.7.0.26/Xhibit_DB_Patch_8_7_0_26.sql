/*
 * Filename:    Xhibit_DB_Patch_8_7_0_26.sql (CREST to XHIBIT functionality release)
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
 * 02/05/2018 	D.Burden	CTX-1040
 * 14/05/2018 	M.Harris		CTX-1005 Add new views for use in the xhb_listing_pkg
 * 11/05/2018   C.Cash	        ctx-2058 Default value on xhb_case
 * 11/05/2018   C.Cash		ctx-2060 Audit change on xhb_cracked_ineffective
 * 15/05/2018	C.Vincent		CTX-1703 rebuild check constraint on xhb_list
 * 17/05/2018	C.Cash			CTX-2062 Insert into xhb_sys_audit
 * 16/05/2018	C.Cash 		CTX-1405 xhb_case_history create
 * 15/05/2018	C.Vincent		CTX-1703 rebuild check constraint on xhb_list
 * 16/05/2018   J.Uphill		CTX-1876
 * 11/05/2018   C.Cash		ctx-2069 Audit change on xhb_cracked_ineffective
 * 11/05/2018   C.Cash		ctx-2060 Add court_site_id to xhb_case_diary_fixture
 * 22/05/2018   G.Brar      ctx-1891
 * 23/05/2018	C.Cash			ctx-1978 - Populate court calendar.  Build dbms_scheduler job
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_26_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-1005 */
@@xhb_list_last_published_view.sql;
@@xhb_list_last_unpublished_view.sql;
/*CTX-1040 and CTX-1005*/
@@xhb_listing_pkg_h.sql
@@xhb_listing_pkg_b.sql

/* CTX-2058 */
@@xhb_case_alter.sql;

/*ctx-2060*/
@@xhb_ref_cracked_effective_audit_alter.sql;

/*CTX-1703*/
@@xhb_list_alter.sql;

/*CTX-2062*/
@@xhb_sys_audit_insert.sql;

/*CTX-1405*/
@@xhb_case_history_create.sql;

/*CTX-1703*/
@@xhb_list_alter.sql;

/* CTX-1876 */
@@xhb_case_on_list_alter.sql;

/*ctx-2069*/
@@xhb_case_diary_fixture_alter.sql;

/*ctx-2060*/
@@xhb_ref_cracked_effective_audit_alter.sql;

/*ctx-1891*/
@@xhb_reports_pkg_h.sql;
@@xhb_reports_pkg_b.sql;


/*ctx-1978*/
/* SA - Removing call for performance reasons - it will be run as part of a separate release */
/* @@xhb_populate_ref_calendar.sql; */
@@dbms_scheduler_create_calendar_job.sql;


/*ctx-2099*/
@@legal_aid_drop_revocation_fk.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.26', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.26', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.26', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.26', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.26', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


