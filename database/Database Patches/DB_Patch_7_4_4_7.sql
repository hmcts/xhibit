/*
 * Filename:    DB_Patch_7_4_4_7.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        1st Novemeber 2004
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 22/04/2005	SS			Update to mercator standing data.
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
UPDATE XHB_REF_DISPOSAL_TYPE SET OBS_IND = 'N';

UPDATE XHB_REF_DISPOSAL_LINE SET OBS_IND = 'N';

COMMIT;



/*
 * Updating of table XHB_VERSION
 */


DELETE FROM XHB_VERSION WHERE SCHEMA_NAME IN ('XHIBIT', 'MERCATOR');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.4.4.7', sysdate, 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.4.4.7', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
