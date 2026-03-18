/*
 * Filename:    DB_Patch_7_6_CJIT.sql
 *
 * System:      All Environments
 * Date:        25/10/05
 *
 */

/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 07/10/05	SS			extracted CJIT changes from main 7.6 release patch to create a CJIT patch in it's own right.
 * 25/10/05	CR			Changes to tablespace names & storage for CSDBPRD2 database. Removal of changes already in Production.
 */ 

/*
 * Changes to CJI_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of CJI_ tables, indexes and foreign keys
 */



CREATE TABLE CJIT.MTBL_HTTP_CONNECTION_LOG
(
  CONNECTION_TIME  DATE,
  SYSTEM           VARCHAR2(32 BYTE),
  MAPNAME          VARCHAR2(64 BYTE),
  RETURN_CODE      VARCHAR2(192 BYTE),
  REL_ID	   VARCHAR2(250 BYTE)
)
TABLESPACE DATAD
STORAGE (INITIAL 512K NEXT 512K PCTINCREASE 0);

CREATE INDEX MTBL_HTTP_LOG_M1 ON CJIT.MTBL_HTTP_CONNECTION_LOG(CONNECTION_TIME) 
TABLESPACE DATAX 
STORAGE (INITIAL 512K NEXT 512K PCTINCREASE 0);


CREATE TABLE CJIT.MTBL_LOOKUP
(
  LOOKUP_TYPE       VARCHAR2(20)           NOT NULL,
  LOOKUP_CODE       VARCHAR2(20)           NOT NULL,
  LOOKUP1           VARCHAR2(255),
  LOOKUP2           VARCHAR2(255),
  LOOKUP3           VARCHAR2(255),
  LOOKUP4           VARCHAR2(255),
  LOOKUP5           VARCHAR2(255),
  LOOKUP6           VARCHAR2(255),
  LOOKUP7           VARCHAR2(255),
  LOOKUP8           VARCHAR2(255),
  LAST_UPDATE_DATE  DATE                        NOT NULL,
  CREATION_DATE     DATE                        NOT NULL,
  CREATED_BY        VARCHAR2(30)           NOT NULL,
  LAST_UPDATED_BY   VARCHAR2(30)           NOT NULL,
  VERSION           NUMBER(5)                   NOT NULL
)
TABLESPACE DATAD
STORAGE (INITIAL 512K NEXT 512K PCTINCREASE 0);


/*
 * Changes, additions or deletion of views
 */



/*
 * Changes, additions or deletion of sequences
 */



/*
 * Changes to CJI_ table triggers as a result of any CJI_ table modifications
 */


CREATE OR REPLACE TRIGGER CJIT.MTBL_LOOKUP_BIR_TR
  BEFORE INSERT
  ON CJIT.MTBL_LOOKUP 
  FOR EACH ROW
BEGIN



  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/

CREATE OR REPLACE TRIGGER CJIT.MTBL_LOOKUP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON CJIT.MTBL_LOOKUP 
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';


   SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;


END;
/




/*
 * Changes, additions or deletion of packages/procedures/functions
 */



CREATE OR REPLACE PACKAGE CJIT.mtbl_cjse_pkg AS
    PROCEDURE insert_http_log(p_system IN VARCHAR2,
                              p_mapname IN VARCHAR2,
                              p_map_return_code IN VARCHAR2,
			      p_doc_id IN VARCHAR2);
END mtbl_cjse_pkg;
/

CREATE OR REPLACE PACKAGE BODY CJIT.mtbl_cjse_pkg AS
    PROCEDURE insert_http_log(p_system IN VARCHAR2,
                              p_mapname IN VARCHAR2,
                              p_map_return_code IN VARCHAR2,
			      p_doc_id IN VARCHAR2)
	IS
        l_date       DATE :=	SYSDATE	;				 
    BEGIN
        INSERT INTO CJIT.MTBL_HTTP_CONNECTION_LOG VALUES
	     (l_date,p_system,p_mapname,p_map_return_code,p_doc_id);
    END insert_http_log; 
END mtbl_cjse_pkg;

/

conn / as sysdba;

@@01_grants.sql

conn cjit/cjit


@@m4ora8_1_CJIT.sql
@@m4ora8_2_CJIT.sql
@@m4ora8_col_CJIT.sql




/*
 * Changes, additions or deletion of standing data
 */

insert into CJIT.cji_parameter_value values ('BITSOFFLINEInterval',60);
insert into CJIT.cji_parameter_value values ('AggOFFLINEInterval',60);
insert into CJIT.cji_parameter_value values ('CJIPOFFLINEInterval',60);
commit;


INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'CJIP_EVENT', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', ' -CERT -PKEY -CA  -KPASS',  NULL, NULL
, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'CJIT_SEND', '~HTTP~', '~PROXY~', NULL, NULL, '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', NULL,  NULL, NULL, 'CJIT', 'CJIT', 5); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'POLL_CJIP', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', '-CERT -PKEY -CA  -KPASS',  NULL, NULL, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'CJIT_STATUS', '~HTTP~', '~PROXY~', NULL, NULL, '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', NULL,  TO_Date( '07/21/2005 12:11:30 PM', 'MM/DD/YYYY HH:MI:SS AM')
,  TO_Date( '07/19/2005 02:46:03 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'CJIT', 'CJIT', 5); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'BITS_DOC_REG', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', '-CERT -PKEY -CA  -KPASS',  NULL,  NULL, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'USER_PASS', 'CJIT', '~USER~', '~PASS~', NULL, NULL, NULL, NULL, NULL, NULL
,  NULL, NULL, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'CJIT_INBOUND', '~HTTP~', '~PROXY~', NULL, NULL, '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', NULL, NULL, NULL, 'CJIT', 'CJIT', 5); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'CJIP_DOC_DEREG', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', ' -CERT -PKEY -CA  -KPASS',  NULL, NULL, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'BITS_DOC_SUBMIT', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', '-CERT -PKEY -CA  -KPASS', NULL,  NULL, 'CJIT', 'CJIT', 1); 
INSERT INTO CJIT.MTBL_LOOKUP ( LOOKUP_TYPE, LOOKUP_CODE, LOOKUP1, LOOKUP2, LOOKUP3, LOOKUP4, LOOKUP5,
LOOKUP6, LOOKUP7, LOOKUP8, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY,
VERSION ) VALUES ( 
'HTTP_SERVER', 'POLL_BITS', '~HTTP~', '~PROXY~', '~KPASS~', '~PEM~', '~USER~', '~PASS~', '-SPROTO SSLv3 -TUNNELING -I 500', '-CERT -PKEY -CA  -KPASS', NULL, NULL, 'CJIT', 'CJIT', 1); 

commit;

/* This script will be used to update Production 7_X_v0_4  to 7_6_v0_1 */



/**********************************/
/* Update to reference new schema */
/**********************************/

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 1;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RunningList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 2;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice WarnedList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 3;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice FirmList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 4;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Indictment-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 5;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice Skeleton-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 6;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityRehabOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 7;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 8;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityPunishmentRehabOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 9;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice RemandOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 10;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BailOrder-v4-2.xsd' 
WHERE DOCUMENT_TYPE_ID = 11;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice ImprisonmentOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 12;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice YoungOffenderOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 13;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommittalRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 14;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice AppealRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 15;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice TrialRecordSheet-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 16;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice BenchWarrant-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 20;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice DailyList-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 21;

UPDATE CJI_DOCUMENT_TYPE SET SCHEMA_NAME = 'http://www.courtservice.gov.uk/schemas/courtservice CommunityOrder-v4-2.xsd'
WHERE DOCUMENT_TYPE_ID = 28;

commit;

/**********************************/
/* Update to reference new stylesheet */
/**********************************/


Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/dailyListHtml.xsl'
 where DOCUMENT_TYPE_ID =1;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/runningListHtml.xsl'
 where DOCUMENT_TYPE_ID =2;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/warnedListHtml.xsl'
 where DOCUMENT_TYPE_ID =3;

Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/firmListHtml.xsl'
 where DOCUMENT_TYPE_ID =4;


Update CJI_DOCUMENT_TYPE set stylesheet_name = 
'http://www.courtservice.gov.uk/transforms/courtservice/dailyPrisonListHtml.xsl'
 where DOCUMENT_TYPE_ID =21;

commit;


INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 23, 'DLD', 'Daily List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');
INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 24, 'FLD', 'Firm List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');
INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 25, 'WLD', 'Warned List for Distribution',  'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');
INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 26, 'DLL', 'Daily List Letters', 'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');
INSERT INTO CJI_DOCUMENT_TYPE ( DOCUMENT_TYPE_ID, INTERNAL_CODE, INTERNAL_NAME, EXTERNAL_NAME, STYLESHEET_NAME, SECURITY_CLASSIFICATION, BITS_DOCUMENT_CONFIG, BITS_TRANSFORM_CONFIG, REGISTERED_IN_CJSE, REGISTER_AGAINST_LOCATION, REGISTER_AGAINST_CRN, REGISTER_AGAINST_DEFENDANT, SCHEMA_NAME )
VALUES ( 27, 'FLL', 'Firm List Letters', 'Unknown', 'Unknown', 'NPM', 'Unknown', 'Unknown', 'N', 'N', 'N', 'N', 'Unknown');
COMMIT;



/*************************/
/* update Version No     */
/*************************/


UPDATE CJI_VERSION Set
SCHEMA_VERSION = '7_6_v0_1',
last_update_date = sysdate,
Updated_by = 'XHIBIT',
Display_name = 'CJSE Database Schema 7_6_v0_1',
Display_seq = 1
Where SCHEMA_NAME = 'CJSE';

commit;







