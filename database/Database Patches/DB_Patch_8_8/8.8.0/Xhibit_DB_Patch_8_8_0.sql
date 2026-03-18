/*
 * Filename:    Xhibit_DB_Patch_8_8_0.sql (Legislatove 2021 functionality release)
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
 * 04/01/2021	S.Atwell		Created basic template file for Legislative 2021 project
 * 19/01/2021	S.Atwell		XLC2-6: New scripts for adding new XHB_D20_OFFENCE_LINK table
 * 25/01/2021	W.Hawkins		XLC2-12: Added script to remove driving orders from menu (set obsolete)
 * 26/01/2021	S.Atwell		XLC2-9: New scripts for adding new XHB_REF_APP_RES_D20_MAP table
 * 25/01/2021	R.McArthur		XLC2-16: Added scripts to insert DISINT Interim Disqualification
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

-- Create new xhb_d20_offence_link table and associated objects: XLC2-6
@xhb_d20_offence_link_create.sql;

-- New columns on XHB_DEFENDANT_ON_OFFENCE, XHB_D20_OFFENCE_CODES and XHB_REF_DISPOSAL: XLC2-1, XLC2-7 and XLC2-8
@xhb_defendant_on_offence_alter.sql
@xhb_d20_offence_codes_alter.sql
@xhb_ref_disposal_type_alter.sql

/*XLC2-12*/
@update_ref_disp_dirving_orders.sql

-- Create new xhb_ref_app_res_d20_ma table and associated objects: XLC2-9
@xhb_ref_app_res_d20_map_create.sql

/*XLC2-16*/
@insert_ref_disp_line_disint.sql
@insert_ref_disp_menu_disint.sql
@insert_ref_disp_type_disint.sql

@ref_disp_line_execute.sql

-- XLC2-30 - Increased column sizes for freetext changes
@update_freetext_column_lengths.sql

/* XLC2-47 CAD */
@XLC2-47_XDDC_inserts.sql
@XHB_CREATE_DMI_CAD_PKG.b.sql


DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.9', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
