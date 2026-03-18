/*
 * Filename:    Xhibit_DB_Patch_8_7_0_39.sql (CREST to XHIBIT functionality release)
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
 * 16-10-2018	M.Harris		ctx-2820 - Add ref_chamber_id as a duplicate of chamber_id for new ui and backwards compatibility
 * 22-10-2018	J.Riley  		ctx-2160 - Add RREC_Types, corrections to RREC report
 * 17/10/2018	J.Uphill		CTX-2782
 * 18-10-2018	C.Vincent		ctx-2759 - correct highlighting rule in xhb_report_pkg_b for CTLRP report query
 * 18-10-2018   J.Riley         ctx-2816 - Review correction for RELCJ report
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_39_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* ctx-2160 */
@@RREC_Types.sql

/*ctx-2820*/
@@xhb_search_pkg_b.sql

/* ctx-2816 */
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* CTX-2782 */
@@mtbl_adaptor_config_insert.sql;
@@xhb_list_distribution_pkg_h.sql;
@@xhb_list_distribution_pkg_b.sql;
@@xhb_report_pkg_b.sql;
@@xhb_get_distribution_list_xml_b.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.39', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.39', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.39', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.39', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.39', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
