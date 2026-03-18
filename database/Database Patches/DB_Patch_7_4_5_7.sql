/*
 * Filename:    DB_Patch_7_4_5_7.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        20th May 2005
 */
-- Additions or deletion of XHB_ tables, indexes and foreign keys
--
CREATE TABLE MTBL_LIST_CHECK (
	DEJ_ID NUMBER(8) NOT NULL,
	court_ID NUMBER(8) NOT NULL,
	DOCUMENT_TYPE VARCHAR2(30) NOT NULL,
	LIST_TYPE VARCHAR2(30) NOT NULL, 
	HTTP_STATUS VARCHAR2(1) NULL,
	LIST_STATUS VARCHAR2(1) NULL, 
	LAST_UPDATE_DATE DATE NOT NULL, 
	CREATION_DATE DATE NOT NULL
) 
TABLESPACE XHIBITD
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

-- Changes to XHB_ table definitions, indexes and foreign keys
--

ALTER TABLE MTBL_LIST_CHECK ADD (
PRIMARY KEY (DEJ_ID, court_ID, DOCUMENT_TYPE
) 
USING INDEX TABLESPACE XHIBITX 
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0)); 
 

-- Changes, additions or deletion of views
--


-- Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
--


--  Changes, additions or deletion of sequences
--


--  Changes to XHB_ table triggers as a result of any XHB_ table modifications
--


--  Changes, additions or deletion of packages/procedures/functions
--



--Changes, additions or deletion of standing data
--



/*
 * Updating of table XHB_VERSION
 */


DELETE FROM XHB_VERSION WHERE SCHEMA_NAME = 'MERCATOR';

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.4.5.7', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

/