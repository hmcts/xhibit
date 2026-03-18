/*
 * Filename:    Cjit_DB_Patch_8_1.sql
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
   31/07/2006   C RANAWEERA		database changes made to comprise 
                     			release 8.1
   20/09/2006   S MILES                 Changed to reflect standard for release 8.1
   19/10/2006   K Shah	    		Altered the calling order so that pkgs are created before triggers
 *
 */ 
set echo on
set term off
column filename new_value spool_filename
  select 'CJIT_DBPATCH_8_1_'||
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
 * Changes, additions or deletion of sequences
 */

/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes to CJI_ table triggers as a result of any CJI_ table modifications
 */


/*
 * Changes, additions or deletion of standing data
 */

@@cji_data_load.sql

/*************************/
/* update Version No     */
/*************************/

DELETE FROM CJI_VERSION;

INSERT INTO CJI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('CJSE','8_1',sysdate,'CJIT','CJSE Database Schema 8_1',1);


commit;


spool off





