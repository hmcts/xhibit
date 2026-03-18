/*
 * Filename:    DB_Patch_7_6_2_5.sql
 *
 * System:      Production
 *
 * Date:        16 Dec 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 16/12/2005	AB			Initial version
 * 21/12/2005	NE			Removed deletions - now in 7.7.1
 *
 */ 


/*
 * Updating of table XHB_VERSION
 */
 
DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('MERCATOR');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.6.2.5', sysdate, 'RELEASE', 'Mercator', 5); 

COMMIT;
