/*
 * Filename:    Xhibit_DB_Patch_8_7_0_28.sql (CREST to XHIBIT functionality release)
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
 * 07-06-2018   D.Burden        ctx-2162 
 * 07-06-2018	N.Walters		CTX-1938 insert id into crest columns
 * 08-06-2018   C.Cash          ctx-1978
 * 08/06/18	    M.Newman		CTX-2209
 * 08-06-18		C Cash			ctx-2159
 * 08-06-2018   M.Harris     	ctx-971 Add missing note types
 * 11-06-2018   C.Cash          ctx-1916
 * 11-06-2018   M.Newman        ctx-2210 Insert data into each db for case group seq
 * 11-06-2018   M.Harris        ctx-1902 Insert the new NOTE_TYPE records
 * 11-06-2018   C.Cash          ctx-1916
 * 12-06-2018   C.Cash		    ctx-2141 xhb_housekeeping_pkg
 * 13/06/2018	C Vincent		CTX-2222
 * 13-06-2018	B.Hingston		ctx-2065
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_28_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*ctx-2081*/
@@xhb_case_diary_fixture_alter.sql;

/*ctx-2209*/
@@case_group_generator_pkg.sql;

/*ctx-1916*/
@@xhb_court_alter.sql;
@@xhb_court_room_alter.sql;
@@xhb_court_site_alter.sql;

/*ctx-971*/
@@xhb_ref_listing_data_insert.sql;

/*ctx-2159*/
@@aud_ref_calendar_alter.sql;

/*ctx-1902*/
@@xhb_ref_listing_data_insert_new.sql;
@@xhb_ref_listing_data_update.sql;
@@xhb_ref_listing_data_delete_old.sql;

/*ctx-2210*/
@@load_case_group_seq_data_TST4.sql;

/*ctx-2162*/
@@xhb_reports_log_alter.sql;
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-2141*/
@@xhb_housekeeping_pkg_pkb.sql;
@@xhb_housekeeping_pkg_pks.sql;


/*ctx-1978*/
@@xhb_populate_ref_calendar.sql;
@@dbms_scheduler_create_calendar_job.sql;

/* SA - Commenting out so that it can be ran at a later date */
/*@@execute_xhb_populate_ref_calendar.sql; */

/*CTX 1938*/
@@update_triggers_for_crestid.sql;

/*CTX-2222*/
@@xhb_listing_pkg_b.sql;

/* CTX-1984 */
@@xhb_security_role.sql;

/* CTX-1984 */
@@xhb_security_group_role.sql;

/*ctx-2065*/
@@xhb_ref_listing_data_insert_predefined_list_note.sql;
 
COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.28', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.28', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.28', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.28', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.28', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
