/*
 * Filename:    DB_Patch_7_6_2_4.sql
 *
 * System:      Production
 *
 * Date:        15 Dec 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 15/12/2005	IL			Initial version
 *
 */ 


/*
 * Changes, additions or deletion of standing data
 */

UPDATE XHB_MTBL_ADAPTOR_CONFIG SET PARAMETERS = '-TV+ -PORT' WHERE ADAPTOR_TYPE = 'FTP'; 

/*
 * Updating of table XHB_VERSION
 */
 
DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('MERCATOR');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.6.2.4', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
