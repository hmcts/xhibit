/*
 * Filename:    Xhibit_DB_Patch_8_7_3_3.sql (CREST to XHIBIT functionality release)
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
 * 30/05/2019	Chris Vincent	CTX-4257 and CTX-4258 (ADJSS/DEFSS changes)
 * 30/05/2019	Chris Vincent	CTX-4238 (new CTLRP Exclusion report)
 * 30/05/2019	Chris Vincent	CTX-4206 (RAGE changes)
 * 30/05/2019	Chris Vincent	CTX-4265 (NFIX changes)
 * 31/05/2019	Chris Vincent	CTX-4274 (ADJSS/DEFSS changes)
 * 31/05/2019	David Burden	CTX-4275
 * 31/05/2019	Chris Vincent	CTX-4268 (UNLC/OUTC changes)
 * 04/06/2019	Chris Vincent	CTX-4243 (add comments to queries for performance identification)
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_3_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4238 */
@@xhb_report_pkg_h.sql;

/* CTX-4257, CTX-4258, CTX-4238, CTX-4206, CTX-4265, CTX-4268, CTX-4243 */
@@xhb_report_pkg_b.sql;

@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/* CTX-4269*/
@@xhb_orders_pkg_h.sql;
@@xhb_orders_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.3.3', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.3.3', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.3.3', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.3.3', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.3.3', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
