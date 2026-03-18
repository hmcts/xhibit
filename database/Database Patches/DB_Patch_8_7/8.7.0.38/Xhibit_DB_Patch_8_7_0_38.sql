/*
 * Filename:    Xhibit_DB_Patch_8_7_0_38.sql (CREST to XHIBIT functionality release)
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
 * 27/09/18		D.Burden		CTX-1576
 * 28-09-2018	C.Cash			ctx-2080 - AQ bridge tiggers for xhb_defendant_on_case
 * 03/10/18		J Uphill		CTX-2697
 * 03-10-2018   J.Riley         ctx-2160 - RREC 2nd review re-work
 * 03-10-2018	C.Cash			ctx-2474 - xhb_def_hearing_record update
 * 04-10-2018	J.Uphill		ctx-2637, ctx-2638 - remove public view parameter
 * 05-10-2018	N.Walters		ctx-2726 - adding new security roles for QACAS
 * 11-10-2018	N.Walters		ctx-2779 - adding a fix in for NLE issue with common_defendants_grouped 
 * 16-10-2018   J.Riley         CTX-2160 - Add RREC_Types.sql 
 * 18-10-2018   J.Riley         CTX-2227 - Add delete_case_ctx procedure to housekeeping pkg
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_38_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/* CTX-2160 */
@@RREC_Types.sql

/*ctx-1576*/
@@xhb_get_xml_reports_h.sql;
@@xhb_get_xml_reports_b.sql;

/*ctx-2629*/
@@xhb_get_distribution_list_xml_h.sql;
@@xhb_get_distribution_list_xml_b.sql;

@@xhb_list_distribution_pkg_b.sql;

/* ctx-2080 */
@@wmb_message_route_insert.sql;
@@xhb_doc_coa_i_wsm.sql;
@@xhb_doc_coa_u_wsm.sql;


@@xhb_report_pkg_h.sql;
@@xhb_report_pkg_b.sql;

/*ctx-2398, ctx-2637 */
@@xhb_view_schedule_pkg_h.sql
@@xhb_view_schedule_pkg_b.sql;

/*ctx-2720*/
@@xhb_ref_chamber_crest_seq_create.sql;

/*CTX-2697*/
@@xhb_disposal2_bir_tr.sql;
@@xhb_disposal2_bur_tr.sql;

/*ctx-2726*/
@@xhb_security_role_insert.sql
@@xhb_security_group_role_insert.sql

/*ctx-2779*/
@@xhb_case_linking_pkg_b.sql;


/* ctx-2227 */
@@xhb_housekeeping_pkg_pks.sql
@@xhb_housekeeping_pkg_pkb.sql


COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.38', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.38', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.38', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.38', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.38', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
