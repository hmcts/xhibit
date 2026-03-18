/*
 * Filename:    DB_Patch_7_4_4_1.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        30th March 2005
 */

/*
 * Updating of table XHB_VERSION
 */
DELETE FROM XHB_VERSION WHERE SCHEMA_NAME = 'MERCATOR';

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.4.4.1', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
