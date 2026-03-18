/*
 * Filename:    Xhibit_DB_Patch_8_0_3_3.sql
 *
 * Must login with system dba privs to run
 * this script.
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  01/09/2006	Kadu Shah		Changes for release 8.0.3.3
*/ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_0_3_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@xhb_code_release_control_8033.sql


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('JAVASERVER', 'XHIBIT') ;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.0.3.3', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.0.3.3', sysdate , 'RELEASE', 'Database', 3); 


COMMIT;

spool off
