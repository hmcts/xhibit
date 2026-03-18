/*
 * Filename:    Cjit_DB_Patch_8_2.sql
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
 *  30/10/2007	D RAI			Changes for release 8.2
 
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Cjit_DB_Patch_8_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename





/*
 * Changes to CJI_ table definitions, indexes and foreign keys
 */





 /*
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
 * Changes, additions or deletion of packages/procedures/functions
 */





/*
 * Changes to CJI_ table triggers as a result of any CJI_ table modifications
 */



/*
 * Changes, additions or deletion of standing data
 */


@@cjit_data_load.sql



spool off
