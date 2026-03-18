/*
 * Filename:    DB_Patch_7.6.2.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        11th Novemeber 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 11/11/2005	K Shah			Database changes made to comprise release 7.6.2
 * 15/11/2005	C Ranaweera		XHB_MTBL_ADAPTOR_CONFIG insert change - MTBL_ADAPTOR_CONFIG_ID=4
 *
 */ 

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */
INSERT INTO XHB_MTBL_ADAPTOR_CONFIG ( MTBL_ADAPTOR_CONFIG_ID, ADAPTOR_TYPE, PARAMETERS , SERVER_TYPE, SERVER_ADDRESS, USERNAME, PASSWORD, DOCUMENT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 4, 'EMAIL', NULL, 'CONF', NULL, 'tstmail1@gateway-preprod', NULL, NULL, NULL, NULL, NULL, NULL, NULL); 
COMMIT;


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('MERCATOR', 'XHIBIT');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.6.2', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.6.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
