/*
 * Filename:    DB_Patch_7_4_5_7_1_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 *
 * Date:        24th May 2005
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

CREATE TABLE MTBL_LIST_CHECK (
	DEJ_ID NUMBER(8) NOT NULL,
	court_ID NUMBER(8) NOT NULL,
	DOCUMENT_TYPE VARCHAR2(30) NOT NULL,
	LIST_TYPE VARCHAR2(30) NOT NULL,
	HTTP_STATUS VARCHAR2(1) NULL,
	LIST_STATUS VARCHAR2(1) NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL)
TABLESPACE MERCATORD
	STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);


ALTER TABLE MTBL_LIST_CHECK ADD (PRIMARY KEY (DEJ_ID, court_ID, DOCUMENT_TYPE) 
USING INDEX TABLESPACE XHIBITX 
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0)); 


 


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
INSERT INTO MTBL_HTTP_RETRY VALUES ('Gzip_Http_Retrun.mmc', 5, 2);
commit;

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION where DISPLAY_NAME='Database';
 
 
INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.4.5.7.1', sysdate , 'RELEASE', 'Database', 3); 


COMMIT;
