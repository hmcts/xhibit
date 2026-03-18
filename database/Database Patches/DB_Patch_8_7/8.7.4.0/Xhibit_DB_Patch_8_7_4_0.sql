/*
 * Filename:    Xhibit_DB_Patch_8_7_4_0.sql (CREST to XHIBIT functionality release)
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
 * 06/06/2019	Chris Vincent	CTX-4268 (OUTC/UNLC changes)
 * 11/06/2019	Mark Harris		CTX-4301
 * 13/06/2019	Mark Harris		CTX-4300
 * 14/06/2019	Chris Vincent	CTX-4317 - update script for legal aid orders
 * 14/06/2019	Chris Vincent	CTX-4299 (NHA changes)
 * 17/06/2019	Chris Vincent	CTX-4323 (RAGE changes)
 * 17/06/2019	Chris Vincent	CTX-4313 (Audit trigger update)
 * 18/06/2019	Chris Vincent	CTX-4205 (AUD_DEFENDANT_ON_OFFENCE index changes)
 * 20/06/2019	Chris Vincent	CTX-4336 (CTLRL changes)
 * 21/06/2019	Chris Vincent	CTX-4345 + CTX-4346 (Case Summary Changes)
 * 24/06/2019	Chris Vincent	CTX-4350 (ADJSS changes)
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_4_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* CTX-4205 */
@@aud_defendant_on_offence_alter.sql;

/* CTX-4313 */
@@xhb_defendantoncase_bur_tr.sql;

/* CTX-4317 */
@@update_legal_aid_orders.sql;

/* CTX-4336 */
@@xhb_report_pkg_h.sql;
/* CTX-4268, CTX-4299, CTX-4323, CTX-4336, CTX_4350 */
@@xhb_report_pkg_b.sql;

@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

@@xhb_list_distribution_pkg_b.sql;

@@ESCAPE_BROKER_RELEASE_CHARS.sql;

/*CTX-4301*/
@@xhb_get_distribution_list_xml_b.sql;

/*CTX-4300*/
@@xhb_housekeeping_pkg_b.sql;

/* CTX-4345, CTX-4346 */
@@xhb_listing_pkg_h.sql;
@@xhb_listing_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.4.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.4.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.4.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.4.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.4.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
