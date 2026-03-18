/*
 * Filename:    Xhibit_DB_Patch_8_7_2_0.sql (CREST to XHIBIT functionality release)
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
 * 11/03/2019 	Chris Vincent	CTX-3835 - NFIX changes
 * 12/03/2019	Chris Vincent	CTX-3817 - Warned List Letter changes
 * 12/03/2019	Chris Vincent	CTX-3847 - LFIX report change
 * 14/03/2019 	David Burden	CTX-3852
 * 14/03/2019	Chris Vincent	CTX-3842 and CTX-3843 - OUTC and UNLC changes
 * 15/03/2019	Chris Vincent	CTX-3827 - RREC changes.
 * 19/03/2019	Chris Vincent	CTX-3866 - Running List change
 * 19/03/2019	Chris Vincent	CTX-3857 - LFIX case type change
 * 19/03/2019	Chris Vincent	CTX-3859 - OUTC and UNLC changes.
 * 20/03/2019	Chris Vincent	CTX-3869 and CTX-3870 - RAGE report changes.
 * 20/03/2019	Chris Vincent	CTX-3879 - RUMO changes
 * 26/03/2019 	Chris Vincent	CTX-3891 - Case Summary sreen change
 * 26/03/2019	Mark Harris		ctx-3892
 * 27/03/2019 	Chris Vincent	CTX-3896 - CTLRL and CTLRP changes
 * 27/03/2019	David Burden	CTX-3799 
 * 27/03/2019	David Burden	CTX-3907 
 * 27/03/2019	Chris Vincent	CTX-3904 - ADJSS/DEFSS change
 * 03/04/2019	Chris Vincent	CTX-3922, CTX-3930 and CTX-3931 - RREC changes
 * 03/04/2019	Mark Harris		sayg 
 * 03/04/2019	Mark Harris		CTX-3916
 * 05/04/2019	Chris Vincent	CTX-3898 - End Bench Warrant Changes
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_2_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-3835, CTX-3847, CTX-3842, CTX-3843, CTX-3827, CTX-3857, CTX-3859, CTX-3869, CTX-3870, CTX-3879, CTX-3896, CTX-3907, CTX-3904, CTX-3922, CTX-3930 and CTX-3931*/
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-3817, CTX-3852 and CTX-3866*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/*ctx-3892, sayg*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

/*ctx-3817 and CTX-3799*/
@@xhb_get_distribution_list_xml_h.sql;
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-3891 */
@@xhb_case_pkg_h.sql;
@@xhb_case_pkg_b.sql;

/*ctx-3816*/
@@xhb_def_on_case_on_list_indexes.sql;

/*CTX-3916*/
@@add_case_number_seq_gen_pk.sql;
@@add_screen_court_room_pk.sql;
@@add_validation_pk.sql;
@@add_version_pk.sql;

/*CTX-3868*/
@@security_role_amend.sql;

/* CTX-3860 */
@@xhb_security_role_insert.sql;
@@xhb_security_group_role_insert.sql;

/*sayg*/
@@xhb_list_bir_tr.sql;
@@xhb_def_on_case_on_list_bir_tr.sql;

/* CTX-3898 */
@@xhb_ref_event_description_update.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.2.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.2.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.2.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.2.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.2.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
