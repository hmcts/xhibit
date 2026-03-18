/*
 * Filename:    Xhibit_DB_Patch_8_8_5_0.sql (D20 2021 Fixes release)
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
 * 02/03/2022	M.Harris		XLC2-419
 * 31/03/2022	M.Harris		XLC4-52
 * 01/04/2022	L.Gittins		XLC4-53
 * 31/03/2022	M.Harris		XLC4-60
 * 01/04/2022	M.Harris		XLC4-48
 * 01/04/2022	M.Harris		XLC4-49
 * 04/04/2022	M.Harris		XLC4-50
 * 04/04/2022	L.Gittins		XLC4-54
 * 04/04/2022	M.Harris		XLC4-51
 * 04/04/2022	L.Gittins		XLC4-55
 * 05/04/2022	L.Gittins		XLC4-56
 * 05/04/2022	L.Gittins		XLC4-57
 * 05/04/2022	L.Gittins		XLC4-58
 * 05/04/2022	L.Gittins		XLC4-59
 * 11/04/2022	M.Harris		XLC4-68
 * 11/04/2022	M.Harris		XLC4-69
 * 08/04/2022	L.Gittins		XLC4-61
 * 27/04/2022	M.Harris		XLC4-71
 * 06/05/2022	M.Harris		XLC4-78
 * 06/05/2022	L.Gittins		XLC4-79
 *
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_5_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* XLC2-419 */
@@xhb_ref_app_res_d20_map_update.sql;
/* XLC4-52 */
@@update_ref_disp_menu_CWRKYR.sql;
/* XLC4-53 */
@@update_ref_disp_menu_PACOYR.sql;
/* XLC4-60 */
@@update_ref_disp_menu_PSACTYR.sql;
/* XLC4-48 */
@@insert_ref_disp_menu_IMPMESW.sql;
/* XLC4-49 */
@@insert_ref_disp_menu_DLFMESW.sql;
/* XLC4-50 */
@@delete_ref_disp_menu_YRMORE.sql;
@@insert_ref_disp_menu_EWMYR.sql;
/* XLC4-51 */
@@insert_ref_disp_menu_ACTYR.sql;
/* XLC4-54 */
@@update_ref_disp_menu_PRACTYR.sql;
/* XLC4-55 */
@@update_ref_disp_menu_RESRQYR.sql;
/* XLC4-56 */
@@update_ref_disp_menu_MHTRTYR.sql;
/* XLC4-57 */
@@update_ref_disp_menu_DRGRQYR.sql;
/* XLC4-58 */
@@update_ref_disp_menu_SPVRQYR.sql;
/* XLC4-59 */
@@update_ref_disp_menu_ATCRQYR.sql;
/* XLC4-68 */
@@xhb_remand_reason_description_create.sql;
@@xhb_remand_reason_description_insert.sql;
/* XLC4-69 */
@@xhb_remand_reason_create.sql;
/* XLC4-61 */
@@update_ref_disp_menu_DET.sql;
/* XLC4-71 */
@@xhb_housekeeping_pkg_b.sql;
/* XLC4-78 */
@@update_ref_disp_menu_REMMAG.sql;
/* XLC4-79 */
@@update_ref_disp_menu_REMJUV.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.5.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.5.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.5.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.5.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.5.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
