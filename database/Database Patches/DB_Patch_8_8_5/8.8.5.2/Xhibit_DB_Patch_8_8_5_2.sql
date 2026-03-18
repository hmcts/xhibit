/*
 * Filename:    Xhibit_DB_Patch_8_8_5_2.sql (PCSC 2022 Fixes release)
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
 * 19/05/2022	M.Harris		XLC4-80
 * 24/05/2022	L.Gittins		XLC4-86
 *
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_5_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@xhb_ref_disposal_type_update.sql;

/* XLC4-86 */
@@xhb_ref_app_res_d20_map_update.sql;

/* Update xhb_case */
@@update_xhb_case.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.5.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.5.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.5.2', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.5.2', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.5.2', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
