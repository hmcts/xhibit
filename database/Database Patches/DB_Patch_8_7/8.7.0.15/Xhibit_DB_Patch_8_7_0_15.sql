/*
 * Filename:    Xhibit_DB_Patch_8_7_0_15.sql (CREST to XHIBIT functionality release)
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
 *  23/11/17	Matt Newman	feature/ctx-501
 *  23/11/2017  David Burden CTX-867 Added creation script for xhb_report
 *  28/11/2017  N.Walters       creating for C.Cash as he doesn't have correct vm to do this
 *	05/12/2017	N.Toft			feature/ctx-1115 Creating a new security role called 
 *								XHBMaintainChamberData and assigning to CS Admin
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_15_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*feature/CTX-867*/
@@xhb_offence_alter.sql;

/* SA - Removed call to this for performance reasons - will be called in a separate release outside of release weekend or made part of Data Migration */
/* @@xhb_offence_update.sql */

/*feature/CTX-867*/
@@xhb_report_pkg

/* CTX 628, 629, 630, 631 */
@@xhb_courtel_list_create.sql

/* feature/CTX-1115 */
@@xhb_security_role_insert.sql;
@@xhb_security_group_role_insert.sql; 

COMMIT;


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.15', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.15', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.15', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.15', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.15', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
