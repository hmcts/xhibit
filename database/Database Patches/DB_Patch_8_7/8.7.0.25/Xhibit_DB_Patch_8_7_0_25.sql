/*
 * Filename:    Xhibit_DB_Patch_8_7_0_25.sql (CREST to XHIBIT functionality release)
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
 * 25/04/2018	C.Cash 	        ctx-113 - dbms_scheduler.create_job
 * 27/04/2018   C.Cash          ctx-1911 - xhb_def_hearing_record updates
 * 30/04/2018	C.Vincent		ctx-1035 - XHB_LISTING_PKG package update   
 * 30/04/2018   C.Cash          ctx-1914 - xhb_case change and create new table
 * 03/05/2018   C.Cash          ctx-1952 - def on case wsm triggers
 * 04/05/2018   C.Cash			ctx-1969/59 alter tables.
 * 03/05/2018   C.Cash          ctx-1913 - xhb_report_log create.  xhb_case alter. xhb_pub_running_list create
 * 04/05/2018   C.Cash          ctx-1912 - xhb_monetary_order_tracking creation
 * 08/05/2018   C.Cash          ctx-1906 - xhb_ref_cracked_effedtive, xhb_ref_even_description, xhb_mis_event table DDL
 * 08/05/2018   C.Cash          ctx-1970 - change FK's on xhb_case_on_list
 * 09/05/2018   C.Cash          ctx-1971 - create xhb_def_on_case_on_list
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_25_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-113 */
@@xhb_reset_case_sequences.sql;
@@dbms_scheduler_job_create.sql;

/* CTX-1911 */
@@xhb_def_hearing_record_alter.sql;

/*CTX-1035*/
@@XHB_LISTING_PKG.sql

/* CTX-1914 */
@@xhb_case_alter.sql;
@@xhb_case_group_seq_generator_create.sql;

/* CTX-1969/59 */
@@xhb_legal_aid_order_alter.sql;
@@xhb_case_listing_entry_alter.sql;

/* CTX-1952 */
@@xhb_defendant_on_case_dd_i_wsm.sql;
@@xhb_defendant_on_case_dd_u_wsm.sql;

/* CTX-1913 */
@@xhb_report_log_create.sql;
@@xhb_pub_running_list_create.sql;
@@xhb_case_1913_alter.sql;
@@xhb_populate_report_log;
@@execute_populate_report_log.sql;

/* CTX-1912 */
@@xhb_monetary_order_tracking_create.sql;


/* CTX-1906 */
@@xhb_ref_cracked_effective_create.sql;
@@xhb_ref_cracked_effective_insert.sql;
@@xhb_ref_event_description_create.sql;
@@xhb_mis_event_log_create.sql;

/* CTX-1949 */
@@xhb_reset_court_log_event_desc_sequence.sql;
@@xhb_court_log_event_desc_insert.sql

/* CTX-1970 */
@@xhb_case_on_list_alter.sql;

/* CTX-1971 */
@@xhb_def_on_case_on_list_create.sql;

/* CTX-1984 */
@@xhb_security_role.sql;

/* CTX-1984 */
@@xhb_security_group_role.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.25', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.25', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.25', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.25', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.25', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


