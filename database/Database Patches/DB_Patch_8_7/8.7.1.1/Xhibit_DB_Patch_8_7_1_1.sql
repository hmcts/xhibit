/*
 * Filename:    Xhibit_DB_Patch_8_7_1_1.sql (CREST to XHIBIT functionality release)
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
 * 29/01/2019 	Chris Vincent	CTX-3622 - RCS screen changes
 * 30/01/2019	J.Boparai		CTX-3611-LIFX sort order
 * 30/01/2019	Gurinder Brar	ctx-3260
 * 01/02/2019   E.Patterson     CTX-2873
 * 31/01/2019	M.Harris		ctx-2747
 * 04/02/2019	C.Vincent		CTX-3632 - Daily Court Room List Changes
 * 06/02/2019   E.Patterson     CTX-3652
 * 07/02/2019	M.Harris		CTX-3637
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_1_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3622*/
@@RCS_unique_constraints.sql;
@@xhb_rcs_pkg_h.sql;
@@xhb_rcs_pkg_b.sql;
/*ctx-3260,ctx-3652*/
@@xhb_report_pkg_b.sql;

/*ctx-2747*/
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;
/* CTX-3632 */
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;
/*CTX-3637*/
@@xhb_charges_pkg_h.sql;
@@xhb_charges_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.1.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.1.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.1.1', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.1.1', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.1.1', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
