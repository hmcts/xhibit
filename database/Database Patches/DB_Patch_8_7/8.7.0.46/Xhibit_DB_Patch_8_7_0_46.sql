/*
 * Filename:    Xhibit_DB_Patch_8_7_0_46.sql (CREST to XHIBIT functionality release)
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
 * 11-12-2018	C.Vincent		RRCA Report changes.  CTX-2964, CTX-3193, CTX-3282, CTX-3283, CTX-3298
 * 11-12-2018	C.Vincent		CTX-3339 changes to the RAGE report
 * 11-12-2018   G.Brar          CTX-3285
 * 11-12-2018   G.Brar          CTX-3342
 * 12-12-2018	M.Harris		CTX-3335
 * 12-12-2018	David Burden	CTX-3299 - Added XML Declaration to published lists
 * 12-12-2018	D.Burden		CTX-3307 
 * 13-12-2018	C.Vincent		NHA Report changes.  CTX-3345, CTX-3383
 * 14-12-2018	N.Walters	CTX-3183 Checking in files for Sujatha S
 * 14-12-2018	C.Vincent		Introduced unique constraints on tables used by RCS screen.  CTX-3224
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_46_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*CTX-3335*/
/*CTX-3299*/
@@xhb_get_xml_reports_b.sql;
@@xhb_get_distribution_list_xml_b.sql;

/* CTX-2964, CTX-3193, CTX-3282, CTX-3283, CTX-3298 */
/* CTX-3345, CTX-3383 */
@@RRCA_GTT.sql
@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/* CTX-3307 */
@@xhb_get_xml_reports_b.sql;

/* CTX-3183 */
@@xhb_list_distribution_pkg_b.sql

/* CTX-3224 */
@@RCS_unique_constraints.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.5.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.46', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.46', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.46', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.46', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.46', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
