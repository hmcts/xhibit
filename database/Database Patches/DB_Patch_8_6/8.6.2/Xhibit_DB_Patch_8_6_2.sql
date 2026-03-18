/*
 * Filename:    Xhibit_DB_Patch_8_6_2.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 *
 *
 * HISTORY
 * =======
 * DATE		WHO		COMMENT
 * ----         ---     	-------
 *  26/06/2014	B Hingston	Changes for DB release 8.6.2
 *
 */


set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_6_2_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

--UPdates for amended Remand Order
INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
VALUES (21, 'RC', 1, 'XHIBIT', 'XHIBIT',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Remand in Custody (5038)');

UPDATE XHB_ORDER_TYPE SET CODE = 'RC_pre_LASBO', DESCRIPTION = 'Remand in Custody (5038)_pre_LASBO' WHERE ORDER_TYPE_ID = 8;

INSERT INTO XHB_ORDER_TEMPLATE VALUES (21,  '/metadata/OrderFOPTransform.xslt', '/metadata/RemandOrder_Narrative.xml', '/metadata/RemandOrderTemplate.xml', 1, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 21, NULL);

UPDATE XHB_ORDER_TEMPLATE SET NARRATIVE_TEMPLATE_NAME = '/metadata/RemandOrder_Narrative_pre_LASBO.xml', EDITOR_TEMPLATE_NAME = '/metadata/RemandOrderTemplate_pre_LASBO.xml', OBS_IND = 'Y' WHERE ORDER_TEMPLATE_ID = 8;

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION)
VALUES (21, 21, NULL, 0);

UPDATE XHB_ORDER_TYPE_MAPPING SET REPLACED_BY = 21 WHERE ORDER_TYPE_MAPPING_ID = 8;



@@RFS4224_database_objects.sql;

@@court_log_events.sql;
@@court_log_events_witness_read_appeal.sql;

@@xhb_post_merc_ref_data_pkg_b.sql;

COMMIT;

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.6.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.6.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.6.2', sysdate , 'RELEASE', 'Database', 3); 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.6.2', sysdate, 'RELEASE', 'Mercator_Spoke', 4);

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.6.2', sysdate, 'RELEASE', 'CSH Broker Components', 4);

COMMIT;

spool off


