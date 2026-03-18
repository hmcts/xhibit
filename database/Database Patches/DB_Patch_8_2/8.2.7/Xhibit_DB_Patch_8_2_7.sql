/*
 * Filename:    Xhibit_DB_Patch_8_2_7.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  12/01/2009	M HEWITT		Changes for DB release 8.2.7
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_2_7_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */





/*
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
 * Changes, additions or deletion of packages/procedures/functions
 */

@xhb_view_schedule_pkg_h.sql
@xhb_view_schedule_pkg_b.sql

@xhb_public_display_pkg_h.sql
@xhb_public_display_pkg_b.sql

@xhb_psr_request_pkg_h.sql
@xhb_psr_request_pkg_b.sql

/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */




/*
 * Changes, additions or deletion of standing data
 */




/*
 * Updating of table XHB_SYS_AUDIT
 */





/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.2.16', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.2.16', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.2.7', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off


