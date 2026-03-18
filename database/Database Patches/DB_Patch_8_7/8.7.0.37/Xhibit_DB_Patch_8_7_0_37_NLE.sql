/*
 * Filename:    Xhibit_DB_Patch_8_7_0_37.sql (CREST to XHIBIT functionality release)
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
 * 17/09/18     C.Cash		ctx-2684 Performance tuning
 * 17/09/18     C.Cash		ctx-2094 - remove court order types drop down
 * 18/09/18     C.Cash		ctx-2696 - ref calendar index
 * 17/09/2018	D.Burden	CTX-1073 - Updated Warned List publish 
 * 19/09/18     C.Cash		ctx-2451 - alter xhb_case_diary_fixture
 * 19/09/18     C.Cash		ctx-2710 - alter xhb_diary_note_entry bir trigger
 * 18/09/18     M.Harris	ctx-2695 - speed improve list results
 * 22/09/18     C.Cash		ctx-2539 - populate xhb_pub_running_list
 * 21/09/18     J.Riley     ctx-2160 - Add RREC report to reports package, add RREC_Types.sql
 * 26/09/18		S.Atwell	ctx-2350 - Courtel data added to XHB_CONFIG_PROP
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_37_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* ctx-2160 */
@@RREC_Types.sql

/*ctx-2684*/
@@xhb_populate_ref_calendar.sql;
@@xhb_populate_ref_system_code.sql;
@@xhb_update_xhb_offence.sql;
@@xhb_update_no_def.sql;



/*ctx-2094*/
@@xhb_post_merc_ref_data_pkg_h.sql;
@@xhb_post_merc_ref_data_pkg_b.sql;

/*ctx-1073*/
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/*ctx-2696*/
@@xhb_ref_calendar_index.sql;

/* ctx-1955, ctx-2160 */
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-2451*/
@@xhb_case_diary_fixture_alter.sql;

/*ctx-2710*/
@@xhb_diary_note_entry_bir_tr.sql;

/*ctx-2695*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-2539*/
@@xhb_populate_pub_running_list.sql;

/*ctx-2350*/
@populate_xhb_config_prop_for_Courtel_NLE.txt

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.37', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.37', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.37', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.37', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.37', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
