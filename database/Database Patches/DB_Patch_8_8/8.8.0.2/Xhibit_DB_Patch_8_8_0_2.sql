/*
 * Filename:    Xhibit_DB_Patch_8_8_0_2.sql (Legislatove 2021 functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE         WHO             COMMENT
 * ----         ---             -------
 * 19/02/2021	S.Atwell		Created and added XLC2-84
 * 04/03/2021	S.Atwell		More changes: New indexes, XLC2-89, XLC2-97, XLC2-99
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_0_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/* XLC2-84 */
@populate_xhb_ref_app_res_d20_map.sql

/* Indexes on new tables */
@create_2021_func_release_new_indexes.sql

/* XLC2-89 */
@XHB_REF_EVENT_DESCRIPTION_Insert.sql

/* XLC2-97 */
@xhb_d20_offence_link_add_column.sql

/* XLC2-99 */
@populate_xhb_ref_disposal_type.sql

/* XLC2-100 */
@populate_xhb_d20_offence_codes.sql


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.0.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.0.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.0.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.0.2', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.0.2', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.0.2', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
