/*
 * Filename:    Xhibit_DB_Patch_8_7_9_0.sql (CREST to XHIBIT functionality release)
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
 * 04/02/2020   M.Harris        ctx-4660 xhb_config_prop_update AND xhb_housekeeping_pkg
 * 22/04/2020   M.Harris        xlc-64 Add Disposal TMSS
 * 23/04/2020   M.Harris        xlc-67 Add Disposal TMCS
 * 30/04/2020   O.Khan			xlc-79 xhb_ref_hearing_type_insert_PST
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_9_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-4660*/
@xhb_config_prop_update.sql;
@xhb_housekeeping_pkg_h.sql;
@xhb_housekeeping_pkg_b.sql;
@xhb_case_history_insert.sql;

/*XLC-52 and 55*/
@insert_ref_disp_menu_aamrss;
@insert_ref_disp_type_aamrss;
@insert_ref_disp_line_aamrss;
@insert_ref_disp_menu_aamrcs;
@insert_ref_disp_type_aamrcs;
@insert_ref_disp_line_aamrcs;
@ref_disp_line_execute;
/*XLC-64*/
@insert_ref_disp_menu_tmss.sql;
/*XLC-67*/
@insert_ref_disp_menu_tmcs.sql;

/*XLC-58*/
@update_ref_disp_menu_aamr.sql;

/*XLC-78*/
@xhb_hearing_type_insert_ast.sql;
@hearing_type_ast_execute.sql;


/*XLC-79*/
@xhb_ref_hearing_type_insert_PST.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.6.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.9.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.9.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.9.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.9.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.9.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
