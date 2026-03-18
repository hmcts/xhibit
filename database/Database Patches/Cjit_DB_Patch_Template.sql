/*
 * Filename:    DB_Patch_CJIT_X_X.sql
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   06/04/2006	K SHAH			Created
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_Cjit_X_X_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to CJI_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of CJI_ tables, indexes and foreign keys
 */


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any CJI_ table modifications
 */


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to CJI_ table triggers as a result of any CJI_ table modifications
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table CJI_VERSION
 */

DELETE FROM CJI_VERSION;

INSERT INTO CJI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('CJSE','X_X_X',sysdate,'CJIT','CJSE Database Schema X_X_X',1);



COMMIT;

spool off
