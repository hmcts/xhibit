/*
 * Filename:    Xhibit_DB_Patch_8_8_6_20.sql (Darts Variable Retention release)
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
 * 28/09/2023	M.Harris		XLC4-163
 * 29/09/2023	M.Harris		XLC4-164 
 * 02/10/2023	M.Harris		XLC4-165
 * 02/10/2023	M.Harris		XLC4-166 
 * 02/10/2023	M.Harris		XLC4-167 
 * 04/10/2023	M.Harris		XLC4-169
 *
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_6_20_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC4-163 */
@@insert_ref_disp_menu_SDPOC.sql;
/* XLC4-164 */
@@insert_ref_disp_menu_SDPOV.sql;
/* XLC4-165 */
@@insert_ref_disp_menu_SDPOD.sql;
/* XLC4-166 */
@@insert_ref_disp_menu_SDPOR.sql;
/* XLC4-167 */
@@insert_ref_disp_menu_SFOP.sql;
@@XHB_REF_MON_ORD_DISPOSALS_Insert.sql;
/* XLC4-163 */
@@XHB_REF_EVENT_DESCRIPTION_Insert.sql;
/* XLC4-169 */
@@xhb_ref_disp_retention_policy_insert.sql;
TRUNCATE TABLE XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.6.20', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.6.20', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.6.20', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.6.20', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.6.20', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
