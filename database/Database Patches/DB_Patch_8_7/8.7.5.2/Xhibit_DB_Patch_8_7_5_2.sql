/*
 * Filename:    Xhibit_DB_Patch_8_7_5_2.sql (CREST to XHIBIT functionality release)
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
 * 06/08/2019	C Vincent	Annotated Warned List changes.  CTX-4448 and CTX-4456
 * 07/08/2019	N Walters	CTX-4467
 * 08/08/2019	C Vincent		Determine case status changes.  CTX-4462
 * 08/08/2019	C Vincent		Public Rep Delete changes.  CTX-4465
 * 08/08/2019	N Walters	CTX-4470
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4448 and CTX-4456 */
@@xhb_get_xml_reports_b.sql;

/* CTX-4467 */
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;

/* CTX-4462 */
@@xhb_case_pkg_b.sql;
/*CTX-4470*/
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-4465 */
@@xhb_def_on_case_ref_sol_firm_alter.sql;
@@xhb_prosecutor_ref_sol_firm_alter.sql;
@@xhb_def_on_case_ref_sol_firm_update.sql;
@@xhb_prosecutor_ref_sol_firm_update.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.2', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.2', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.2', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
