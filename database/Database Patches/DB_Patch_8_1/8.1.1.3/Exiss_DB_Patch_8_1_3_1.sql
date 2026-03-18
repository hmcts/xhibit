/*
 * Filename:    Exiss_DB_Patch_8_1_3_1.sql
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in EXI_version updates.
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   15/05/2007	K SHAH			Created
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Exiss_DB_Patch_8_1_3_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to EXI_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of EXI_tables, indexes and foreign keys
 */

/*  Change 5 on Wiki   */

update exi_ref_type rt
set rt.version = '0.22'
where rt.group_id = (
    select group_id 
    from exi_ref_group 
    where external_name = 'XHIBITEvent'
);

update exi_ref_type rt
set rt.version = '1.0',
    rt.schema_name = 'http://schemas.cjse.gov.uk/messages/exception/2006-06'
where rt.group_id = (
    select group_id 
    from exi_ref_group 
    where external_name = 'DELIVERERROR'
);

commit;


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
 * Changes, additions or deletion of packages/procedures/functions
 */

/*  Change 6 on Wiki   */

@@exi_code_release_control_8131.sql

/*
 * Changes to EXI_ table triggers as a result of any EXI_ table modifications
 */


/*
 * Changes, additions or deletion of standing data
 */


/*
 * Updating of table VERSION for EXISS
 */

DELETE FROM EXI_VERSION;


INSERT INTO EXI_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('EXISS','8_1_3_1',sysdate,'EXISS','EXISS Database Schema 8_1_3_1',1);

COMMIT;

spool off
