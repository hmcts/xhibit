/*
 * Filename:    Xhibit_DB_Patch_8_7_3_1.sql (CREST to XHIBIT functionality release)
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
 * 09/05/2019	Mark Harris		CTX-4192
 * 10/05/2019	Mark Harris		CTX-3397
 * 13/05/2019	Mark Harris		CTX-4121
 * 13/05/2019	Nia Walters	    CTX-4185
 * 14/05/2019	Chris Vincent	CTX-4206 + CTX-4210 (RAGE report changes)
 * 16/05/2019	Chris Vincent	CTX-4172 (ADJSS/DEFSS changes)
 * 16/05/2019	Chris Vincent	CTX-4198 + CTX-4199 (RREC report changes)
 * 17/05/2019	Mark Groen		CTX-4216 
 * 17/05/2019	Chris Vincent	CTX-4220 (Daily List changes)
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_3_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*ctx-4192*/
@@xhb_listing_pkg_b.sql;

/*ctx-4121, CTX-4220*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/*ctx-3397*/
@@xhb_view_schedule_pkg_h.sql;
@@xhb_view_schedule_pkg_b.sql;

/* CTX-4206, CTX-4210, CTX-4172, CTX-4198, CTX-4199 */
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* CTX-4216 */
@@xhb_defendant_on_case_alter.sql;

/*ctx-4185*/
@@courtel_list_amend.sql

/*CTX-4129*/
@@AddAppealResultOrderRole.sql;
@@xhb_orders_pkg_h.sql;
@@xhb_orders_pkg_b.sql;


COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.3.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.3.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.3.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.3.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.3.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
