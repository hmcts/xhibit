/*
 * Filename:    DB_Patch_8_0_4_1_XHIBIT.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   30/10/2006   K SHAH			database changes made to comprise 
                     			release 8.0.4.1 
 *
 */ 
set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_8_0_4_1_XHIBIT_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename
/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of sequences
 */



/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.0.4.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.0.4.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.0.4.1', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.0.4.1', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off