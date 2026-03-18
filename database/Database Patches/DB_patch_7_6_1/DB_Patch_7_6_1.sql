/*
 * Filename:    DB_Patch_7_6_1.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *              Preproduction, Production
 *
 * Date:        04th November 2005
 */


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


@@code_release_control.sql

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('MERCATOR', 'XHIBIT', 'JAVASERVER');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.6.1', sysdate, 'RELEASE', 'Mercator', 4); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.6.1', sysdate, 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.6.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

COMMIT;
