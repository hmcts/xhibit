/*
 * Filename:    Xhibit_DB_Patch_8_7_5_9.sql (CREST to XHIBIT functionality release)
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
 * 25/09/2019	C Vincent		CTX-4567 - script to correct trial cases with a null class code
 * 25/09/2019   C Vincent	    CTX-4599 - ARO changes
 * 25/09/2019   C Vincent	    CTX-4600 - RCS package performance change
 * 25/09/2019   N Walters		CTX-4598 - Changing to add 'summons' at the end of BRSS
 * 26/09/2019	C Vincent		CTX-4594 - script to obsolete xbh_def_on_case_on_list records where the parent is obsolete
 * 27/09/2019	C Vincent		CTX-4609 - Data fix script for correcting offence and defendant on offence records
 * 30/09/2019	C Vincent		CTX-4601, CTX-4603 - NHA mark cases as updated performance changes.
 * 30/09/2019	C Vincent		CTX-4605 - Ordering of disposals in ARO incorrect
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_9_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4609 */
@@xhb_offence_fix.sql;
@@xhb_defendant_on_offence_fix.sql;

/* CTX-4567 */
@@xhb_case_no_class_code_fix.sql;

/* CTX-4594 */
@@xhb_def_on_case_on_list_fix.sql;

/* CTX-4599, CTX-4605 */
@@xhb_orders_pkg_b.sql;

/* CTX-4600 */
@@xhb_rcs_pkg_b.sql;

/* CTX-4598 */
@@xhb_order_type_update.sql;

/* CTX-4597*/
@@LOCOL_updates.sql;

/* CTX-4601, CTX-4603 */
@xhb_report_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.9', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.9', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.9', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.9', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.9', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
