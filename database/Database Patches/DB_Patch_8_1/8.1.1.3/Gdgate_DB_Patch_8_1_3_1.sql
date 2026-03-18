/*
 * Filename:    DB_Patch_Gdgate_8_1_3_1.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in gdg_version updates.
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   15/05/2007	K SHAH			Changes for Release 8.1.3.1
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_Gdgate_8_1_3_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to GDG_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of GDG_ tables, indexes and foreign keys
 */

/*  Change 3(2) on Wiki   */

alter table
gdg_config_properties
add (property_is_static varchar2(1));

update gdg_config_properties
set property_is_static = decode(property_code,'MSG_CONSEC_FAIL_COUNT','N','MSG_MALFORMED_XML_FAIL_COUNT','N','Y');

alter table
gdg_config_properties
add (constraint config_props_is_static_chk CHECK                                      
 (property_is_static IN ('Y','N') AND property_is_static IS NOT NULL));





/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any GDG_ table modifications
 */

/*  Change 3(2) on Wiki on table resulting in audit table change  */

alter table
aud_config_properties
add (property_is_static varchar2(1));

update aud_config_properties
set property_is_static = decode(property_code,'MSG_CONSEC_FAIL_COUNT','N','MSG_MALFORMED_XML_FAIL_COUNT','N','Y');

/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to GDG_ table triggers as a result of any GDG_ table modifications
 */

CREATE OR REPLACE TRIGGER gdg_config_prop_au_tr
   AFTER UPDATE
   ON gdg_config_properties
   FOR EACH ROW
   WHEN (new.property_is_static = 'Y')
BEGIN
   UPDATE gdg_table_timestamp
      SET last_updated = SYSDATE
    WHERE NAME = 'GDG_CONFIG_PROPERTIES';
END;
/
show errors;

/*
 * Changes, additions or deletion of packages/procedures/functions
 */
@@gdg_code_release_control_8131.sql

/*
 * Changes, additions or deletion of standing data
 */

@@GDGATE_data_load.sql

/*
 * Updating of version table 
 */

DELETE FROM GDG_VERSION;

INSERT INTO GDG_VERSION
            (SCHEMA_NAME,SCHEMA_VERSION,LAST_UPDATE_DATE,UPDATED_BY,DISPLAY_NAME,DISPLAY_SEQ) 
VALUES ('GDGATE','8_1_3_1',sysdate,'GDGATE','GDGATE Database Schema 8_1_3',1);

COMMIT;

spool off
