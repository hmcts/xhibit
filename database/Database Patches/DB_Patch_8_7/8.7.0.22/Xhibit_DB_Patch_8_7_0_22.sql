/*
 * Filename:    Xhibit_DB_Patch_8_7_0_22.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO          COMMENT
 * ----         ---          -------
 * 05/03/2018   Chris Cash  DDL Scripts for  (ctx-1725)
 * 05/03/2018   Chris Cash  DDL Scripts for  (ctx-1726)
 * 05/03/2018   Chris Cash  DDL Scripts for  (ctx-1727)
 * 19/03/2018   Chris Cash  FS table changes (ctx-1674)
 * 09/03/2018   AR           CTX-1612, CASE_DESCRIPTION COLUMN WIDTH
 * 21/3/2018    Chris Cash	 ctx-1657 add DATE_TRANS_TO to xhb_case
 * 22/03/2018   AR           CTX-1402, PARENT_GUARDIAN_NAME column width
 * 22/03/2018   Chris Cash   CTX-1721, Change length of field in xhb_legal_aid_order
 * 22/03/2018   Chris Cash   CTX-1423, Populate xhb_ref_system_code and execute
 * 19/03/2018   C.Kudzin 	 CTX-1724 adding new security roles
 * 20/07/2018	S.Atwell	Remove call to xhb_defendant_alter as this is now fixed in the 8.7.0 script. This ha sbeen done for performance reasons.
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_22_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-1724*/
@@xhb_security_role_insert.sql;
@@xhb_security_group_role_insert.sql;



/* feature/ctx-172567 */
@@xhb_diary_note_entry_alter.sql
@@xhb_case_on_list_alter.sql
@@xhb_sitting_on_list_alter.sql


/* feature/ctx-1674 */
/* SA - Removing for performance reasons - fixed original script in 8.7.0.16 */
/*@@xhb_case_diary_fixture_alter_2.sql */
@@xhb_case_on_list_alter_2.sql
@@xhb_diary_note_entry_alter_2.sql
@@xhb_fixture_deft_attending_alter_2.sql
@@xhb_sitting_on_list_alter_2.sql
@@xhb_case_listing_entry_alter.sql

/* feature/ctx-1612 */
/* SA - Removing for performance reasons - fixed original script in 8.7.0.3 */
/* @@xhb_case_alter.sql */


/* feature/ctx-1657 */
@@xhb_case_alter_2.sql

/* feature/ctx-1402 */
/* SA - Removing for performance reasons - fixed original script in 8.7.0 */
/* @@xhb_defendant_alter.sql */

/* feature/ctx-1721 */
/* SA - Removing for performance reasons - fixed original script in 8.7.0.6 */
/*@@xhb_legal_aid_order_alter.sql */

/* feature/ctx-1723 */
@@xhb_populate_ref_system_code.sql
/* SA - Removing for performance reasons - this will be run as part of a script outside of the release weekend */
/* @@xhb_ref_system_code_execute.sql */

/* feature/ctx-1504*/
@@xhb_order_update.sql



COMMIT;


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.22', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.22', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.22', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.22', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.22', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
