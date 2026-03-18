/*
 * Filename:    Xhibit_DB_Patch_8_8_8_0.sql (PDDA release)
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
 * 28/04/2022	C.Vincent		Initial version PDDA-14
 * 14/06/2022	M.Harris		PDDA-15
 * 07/06/2022	L.Gittins		PDDA-32
 * 21/06/2022	M.Harris		PDDA-16
 */

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_8_8_0_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/* PDDA-14 */
@@xhb_ref_status_codes_create.sql;
@@xhb_ref_pdda_message_type_create.sql;
@@xhb_pdda_batch_create.sql;
@@xhb_pdda_message_create.sql;

/*PDDA-32*/
@@xhb_ref_status_codes_insert.sql;

/* PDDA-15 */
@@xhb_config_prop_seq_update.sql;
@@xhb_config_prop_insert.sql;
@@xhb_public_display_pkg_h.sql;
@@xhb_public_display_pkg_b.sql;

/* PDDA-16 */
@@xhb_config_prop_update.sql;

/* Add package for IWP data to PDDA */
@@xhb_pdda_pkg_b.sql
@@xhb_pdda_pkg_h.sql

/* TESTING */
@@TEST_GENERATE_PDDA_LOAD.sql;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.8.8.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.8.8.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.8.8.0', sysdate , 'RELEASE', 'Database', 3);
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.8.8.0', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.8.8.0', sysdate, 'RELEASE', 'CSH Broker Components', 4);


COMMIT;

spool off
