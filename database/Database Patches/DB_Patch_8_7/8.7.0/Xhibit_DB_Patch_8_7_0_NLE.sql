/*
 * Filename:    Xhibit_DB_Patch_8_7.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 *  03/05/2017	B Hingston	Changes for DB release 8.7
 *  30/06/2017  M Harris        Changes to stop broker triggering updates
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* Changes required for Maintain Case functionality */
@@Case_maintenance_changes.sql;
@@load_ref_monitoring_category_data.sql;
@@load_case_number_seq_data_NLE.sql;
@@update_court_police_force_data_NLE.sql;
@@CaseMaintenanceSecurity.sql;

/* Changes to stop broker triggering updates */
/* Removing for Pre-Pilot - will need to reinstate when Pilot goes Live */
/* @@wmb_message_route.sql; */

/* case generator package */
@case_number_generator_pkg.sql


/* HK update for XHB_CHARGES_LOG */
@@xhb_housekeeping_pkg_b.sql



COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


