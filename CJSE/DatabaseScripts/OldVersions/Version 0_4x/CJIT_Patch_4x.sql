/* This script will be used to update Production from 0_4w to the latest level - 0_4x */
/* This update is adds a version table to the CJSE database in order to keep track of schema versions */


CREATE TABLE CJI_VERSION (
 	SCHEMA_NAME             VARCHAR2(10) NOT NULL, 
 	SCHEMA_VERSION          VARCHAR2(10) NULL, 
 	LAST_UPDATE_DATE        DATE	     NULL,	
 	UPDATED_BY              VARCHAR2(30) NULL,
 	DISPLAY_NAME            VARCHAR2(30) NULL,
 	DISPLAY_SEQ             NUMBER(2) 	NULL,
)
TABLESPACE DATAD
   STORAGE  (
   INITIAL 1M
   NEXT 1M
   PCTINCREASE 0 
  );


INSERT INTO CJI_VERSION VALUES('CJSE','Version 0_4x',sysdate,'RELEASE','CJSE Database Schema 0_4x',1);




/* Changes for CR 79 */

UPDATE CJI_DOCUMENT_TYPE SET STYLESHEET_NAME = 'http://www.courtservice.gov.uk/transforms/courtservice/CommittalRecordSheet-v2-3.xsl'
WHERE DOCUMENT_TYPE_ID = 14;
