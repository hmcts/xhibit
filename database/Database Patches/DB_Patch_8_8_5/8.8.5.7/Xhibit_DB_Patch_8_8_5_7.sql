/*
 * Filename:    Xhibit_DB_Patch_8_8_5_7.sql (Split Monarch changes release)
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
 * 05/10/2022	L.Gittins		XLC4-101
 * 06/10/2022	C.Vincent		XLC4-109: update_xhb_system_code_HO_PROC_BREACH.sql script
 * 09/01/23     M.Harris        XLC4-117
 * 09/01/23		L.Gittins		XLC4-116
 * 09/01/23     M.Harris        XLC4-115
 * 06/03/23		L.Gittins		XLC4-128
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_5_7'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

@@update_ref_disp_menu_ISHPO.sql;
@@update_ref_disp_menu_SHPO.sql;
@@insert_ref_disp_menu_SVRO.sql;
@@XHB_REF_EVENT_DESCRIPTION_Insert.sql;
@@update_xhb_system_code_HO_PROC_BREACH.sql;
@@update_ref_disp_menu_RESOR.sql;
@@update_ref_disp_menu_DWLT.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.5.6', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.5.7', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.5.7', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.5.7', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.5.7', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
