/*
 * Filename:    DB_Patch_7_4_5_6_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 *
 * Date:        5th May 2005
 */


/*
 * Updating of table XHB_VERSION
 */


DELETE FROM XHB_VERSION WHERE SCHEMA_NAME = 'MERCATOR';

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.4.5.6', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

/