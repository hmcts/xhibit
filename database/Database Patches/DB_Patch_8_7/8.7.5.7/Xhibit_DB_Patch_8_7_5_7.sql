/*
 * Filename:    Xhibit_DB_Patch_8_7_5_7.sql (CREST to XHIBIT functionality release)
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
 * 09/09/2019	C Vincent		CTX-4552: changes to prevent housekeeping cases from being returned 
 * 12/09/2019	N Walters		CTX-4572: escaping dodgy character
 * 13/09/2019	C Vincent		CTX-4503, CTX-4504, CTX-4505, CTX-4506, CTX-4548 and CTX-4549 - Judge Housekeeping Changes
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_5_7_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/* CTX-4552 */
@@xhb_case_linking_pkg_b.sql;
@@xhb_unauthorised_cases_pkg_b.sql;

/* CTX-4572 */
@@xhb_orders_pkg_b.sql;

/* CTX-4503, CTX-4504, CTX-4505, CTX-4506, CTX-4548 and CTX-4549 */
@@xhb_hk3_create.sql;
@@xhb_housekeeping_pkg_h.sql;
@@xhb_housekeeping_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.5.7', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.5.7', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.5.7', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.5.7', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.5.7', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
