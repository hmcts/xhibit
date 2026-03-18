/*
 * Filename:    DB_Patch_Exiss_X_X.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   07/07/2006	K SHAH			Created
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_Exiss_X_X_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to EXI_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of EXI_ tables, indexes and foreign keys
 */


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any EXI_ table modifications
 */


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to EXI_ table triggers as a result of any EXI_ table modifications
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of version table 
 */

DELETE FROM EXI_VERSION;


INSERT INTO EXI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('EXISS','X_X_X',sysdate,'EXISS','EXISS Database Schema X_X_X',1);

COMMIT;

spool off
