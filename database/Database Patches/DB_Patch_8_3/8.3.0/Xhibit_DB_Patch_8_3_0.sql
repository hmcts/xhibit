/*
 * Filename:    Xhibit_DB_Patch_8_3.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 *  27/03/2009	M HEWITT	Changes for DB release 8.3
 *  28/05/2009	K Davies	Added CCN1263 to this release
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * DB schema updates
 */
@CCN0228_database_objects
@CCN0365_database_objects
@CCN0400_database_objects
@CCN1263_database_objects

/* 
*  Updates to xhb_defendant_on_case occur in CCN0228 and CCN0400
*  so the trigger xhb_defendantoncase_bur_tr is updated here,
*  after both table updates have been appied.  Normally the
*  trigger would be updated in the CCN*_database_objects.sql file.
*/
@xhb_defendantoncase_bur_tr.sql


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.3.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.3.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.3', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.3', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off


