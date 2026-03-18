/*
 * Filename:    Xhibit_DB_Patch_8_7_0_19.sql (CREST to XHIBIT functionality release)
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		    WHO		    COMMENT
 * ----         ---     	-------
 * 25/01/2018   M.Newman	    CTX-1379 adding XHB_BW_HISTORY table and triggers.
 * 02/02/2018   M.Newman    	CTX-1344 XHB_SEARCH_PKG_body remove "ref_Chamber_Id AS chamber_Id"
 * 07/02/2018   C.Kudzin    	CTX-1425 amending xhb_case and AUD_case JP's from 255 to 35.
 * 07/02/2018   M.Newman  	    CTX-1446 Add current_prison_status column to XHB_DEFENDANT
 * 09/02/2018   C.Cash      	ctx-1482 Changing precision scale of court_id on xhb_ref_judge_ticket
 * 12/02/2018   C.Cash	    	ctx-1401 Add foreign key constraint to xhb_fixture_deft_attending
 *
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_7_0_19_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* feature/ctx-1344 */
@@XHB_SEARCH_PKG_body.sql

/*feature/ctx-1425*/
@@xhb_case_alter_1425.sql;

/* feature/ctx-1446 */
@@xhb_defendant_alter.sql

/* feature/ctx-1379 */
@@xhb_bw_history_create.sql

/* feature/ctx-1482 */
@@xhb_ref_judge_ticket_alter.sql

/* feature/ctx-1401 */
@@xhb_fixture_deft_attending_alter.sql

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.7.0.19', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.7.0.19', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.7.0.19', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.7.0.19', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.7.0.19', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off
