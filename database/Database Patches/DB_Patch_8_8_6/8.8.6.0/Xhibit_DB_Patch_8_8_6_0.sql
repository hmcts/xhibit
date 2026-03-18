/*
 * Filename:    Xhibit_DB_Patch_8_8_6_0.sql (Darts Variable Retention release)
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
 * 16/11/2021	M.Harris		Initial version
 * 23/11/2021	M.Harris		DVR-7
 * 24/11/2021	M.Harris		DVR-6
 * 25/11/2021	M.Harris		DVR-2
 * 29/11/2021	M.Harris		DVR-3
 * 30/11/2021	M.Harris		DVR-4
 * 02/12/2021	M.Harris		DVR-5
 * 23/11/2021	L.Gittins		DVR-13
 * 05/01/2022	L.Gittins		DVR-15
 * 24/01/2022	L.Gittins		DVR-16
 * 16/12/2021	M.Harris		DVR-9
 * 11/02/2022	M.Harris		DVR-61
 * 03/05/2022	M.Harris		DVR-77 
 *
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_6_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*DVR-7*/
@@xhb_ref_dar_retention_policies_create.sql;
/*DVR-6*/
@@xhb_ref_disp_retention_policy_create.sql;
/*DVR-2*/
@@xhb_dar_retention_policy_create.sql;
/*DVR-3*/
@@xhb_case_update.sql;
/*DVR-4*/
@@xhb_defendant_on_case_update.sql;
/*DVR-5*/
@@xhb_defendant_on_offence_update.sql;
/*DVR-13*/
@@xhb_security_DARTS_report_insert.sql;
/*DVR-61*/
@@insert_xhb_config_prop.sql;
/*DVR-15*/
@@xhb_report_pkg_b.sql;
@@xhb_report_pkg_h.sql;
/*DVR-16*/
@@xhb_housekeeping_pkg_h.sql;
@@xhb_housekeeping_pkg_b.sql;
/*DVR-9*/
@@xhb_ref_dar_retention_policies_insert.sql;
@@xhb_ref_disp_retention_policy_insert.sql;
/*DVR-77*/
@@xhb_court_log_event_desc_insert.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.6.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.6.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.6.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.6.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.6.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
