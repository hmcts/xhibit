/*
 * Filename:    Xhibit_DB_Patch_8_8_4_0.sql (D20 2021 Fixes release)
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
 * 01/02/2022	M.Harris		Initial version
 * 15/02/2022	M.Harris		XLC4-3
 * 15/02/2022	M.Harris		XLC4-7
 * 15/02/2022	M.Harris		XLC4-8
 * 15/02/2022	M.Harris		XLC4-9
 * 16/02/2022	M.Harris		XLC4-10
 * 17/02/2022	M.Harris		XLC4-11
 * 08/03/2022	L.Gittins		XLC4-36
 *
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_4_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC4-3 */
@@insert_ref_disp_menu_STS1821.sql;
@@XHB_REF_EVENT_DESCRIPTION_Insert.sql;
/* XLC4-7 */
@@update_ref_disp_menu_DETTO.sql;
/* XLC4-8 */
@@update_ref_disp_menu_DTCO.sql;
/* XLC4-9 */
@@update_ref_disp_menu_CJCO.sql;
/* XLC4-10 */
@@update_ref_disp_menu_ORDVAR.sql;
/* XLC4-11 */
@@delete_ref_disp_menu_ICWD_ICWE.sql;
/* XLC4-36 */
@@update_ref_disp_menu_STS.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.4.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.4.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.4.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.4.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.4.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
