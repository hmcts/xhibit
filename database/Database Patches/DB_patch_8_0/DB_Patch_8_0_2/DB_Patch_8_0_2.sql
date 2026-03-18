/*
 * Filename:    DB_Patch_8_0_2.sql
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
   08/06/2006   K Shah			Changes for release 8.0.2
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_8_0_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

/*
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
 * Changes, additions or deletion of data
 */



/*
 * Updating of table XHB_VERSION
 */

update xhb_version
set schema_version = '8.0.2',
last_UPDATE_DATE = sysdate
where display_name = 'Database';

update xhb_version
set schema_version = '8.0.2',
last_UPDATE_DATE = sysdate
where display_name = 'Mercator';


COMMIT;

spool off
