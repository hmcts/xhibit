/*
 * Filename:    Xhibit_DB_Patch_8_8_0_1.sql (Legislatove 2021 functionality release)
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
 * 16/02/2021	S.Atwell		Created
 * 16/02/2021   W.Hawkins       XLC2-15: Added scripts for xlc2-13/14 as well. DISOBLG, DISDISC and DISTOT
 * 22/02/2021	S.Atwell		XLC2-78: Fixed obsoleting of disposals - XLC2-78
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_0_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*XLC2-13*/
@insert_ref_disp_line_disoblg.sql
@insert_ref_disp_menu_disoblg.sql
@insert_ref_disp_type_disoblg.sql

/*XLC2-14*/
@insert_ref_disp_line_disdisc.sql
@insert_ref_disp_menu_disdisc.sql
@insert_ref_disp_type_disdisc.sql

/*XLC2-15*/
@insert_ref_disp_line_distot.sql
@insert_ref_disp_menu_distot.sql
@insert_ref_disp_type_distot.sql

@ref_disp_line_execute.sql

/* XLC2-78 */
@update_ref_disp_driving_orders.sql

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.0.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.0.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.0.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.0.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.0.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.0.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
