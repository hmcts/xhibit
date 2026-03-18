/*
 * Filename:    DB_Patch_7_7.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        17 Novemeber 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 17/11/2005	K Shah			Database changes made to comprise release 7.7
 * 16/12/2005   I Lo			Remove audit table rebuilds, storage settings, ensure indexes go into separate tablespace
 *					call code release control script, change auditing code for aud_court, aud_display_document 
 * 19/12/2005	I Lo			Add missing terminators (;) to insert statements
 *
 */ 

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

ALTER TABLE XHIBIT.XHB_XML_DOCUMENT ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_XML_DOCUMENT ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_XML_DOCUMENT ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.XHB_XML_DOCUMENT ADD MINOR_SCHEMA_VERSION NUMBER(2);


ALTER TABLE XHIBIT.XHB_DOCUMENT_CONTROL ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_DOCUMENT_CONTROL ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_DOCUMENT_CONTROL ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.XHB_DOCUMENT_CONTROL ADD MINOR_SCHEMA_VERSION NUMBER(2);


ALTER TABLE XHIBIT.XHB_FORMATTING ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_FORMATTING ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.XHB_FORMATTING ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.XHB_FORMATTING ADD MINOR_SCHEMA_VERSION NUMBER(2);

ALTER TABLE XHB_COURT ADD COUNTRY VARCHAR(2);
ALTER TABLE XHB_COURT ADD LANGUAGE VARCHAR(2);

ALTER TABLE XHB_DISPLAY_DOCUMENT ADD COUNTRY VARCHAR(2);
ALTER TABLE XHB_DISPLAY_DOCUMENT ADD LANGUAGE VARCHAR(2); 


 /*
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

DROP TABLE XHIBIT.XHB_REF_TRANSLATION;

CREATE TABLE XHIBIT.XHB_REF_TRANSLATION
(
  REF_TRANSLATION_ID NUMBER(8) NOT NULL,
  KEY VARCHAR2(4000) NOT NULL,
  TRANSLATION VARCHAR2(4000) NOT NULL,
  CONTEXT VARCHAR2(4000) NULL,
  EXACT_MATCH VARCHAR2(1) DEFAULT 'Y' NOT NULL CONSTRAINT XHB_REF_TRANS_EXACT_MATCH_CHK CHECK (EXACT_MATCH = 'Y' OR EXACT_MATCH = 'N'),
  LANGUAGE VARCHAR2(2) NOT NULL,
  COUNTRY VARCHAR2(2) NULL,
  OBS_IND VARCHAR2(1) DEFAULT 'N' NOT NULL CONSTRAINT XHB_REF_TRANS_OBS_IND_CHK CHECK (OBS_IND = 'Y' OR OBS_IND = 'N'),
  LAST_UPDATE_DATE DATE NOT NULL,
  CREATION_DATE DATE NOT NULL,
  CREATED_BY VARCHAR2(30) NOT NULL,
  LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
  VERSION NUMBER(5) NOT NULL,
  CONSTRAINT PK_XHB_REF_TRANSLATION PRIMARY KEY (REF_TRANSLATION_ID ) USING INDEX TABLESPACE XHIBITX
)
TABLESPACE XHIBITD
;

/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

ALTER TABLE XHIBIT.AUD_XML_DOCUMENT ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_XML_DOCUMENT ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_XML_DOCUMENT ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.AUD_XML_DOCUMENT ADD MINOR_SCHEMA_VERSION NUMBER(2);

--------------------------

ALTER TABLE XHIBIT.AUD_DOCUMENT_CONTROL ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_DOCUMENT_CONTROL ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_DOCUMENT_CONTROL ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.AUD_DOCUMENT_CONTROL ADD MINOR_SCHEMA_VERSION NUMBER(2);

--------------------------------

ALTER TABLE XHIBIT.AUD_FORMATTING ADD LANGUAGE VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_FORMATTING ADD COUNTRY VARCHAR2(2);
ALTER TABLE XHIBIT.AUD_FORMATTING ADD MAJOR_SCHEMA_VERSION NUMBER(2);
ALTER TABLE XHIBIT.AUD_FORMATTING ADD MINOR_SCHEMA_VERSION NUMBER(2);

-----------------------------------------

ALTER TABLE AUD_COURT ADD COUNTRY VARCHAR(2);
ALTER TABLE AUD_COURT ADD LANGUAGE VARCHAR(2);

-------------------------------------------------------------

ALTER TABLE AUD_DISPLAY_DOCUMENT ADD COUNTRY VARCHAR(2);
ALTER TABLE AUD_DISPLAY_DOCUMENT ADD LANGUAGE VARCHAR(2); 

-------------------------------------------


CREATE TABLE XHIBIT.AUD_REF_TRANSLATION
(
  REF_TRANSLATION_ID NUMBER(8) NOT NULL,
  KEY VARCHAR2(4000) NOT NULL,
  TRANSLATION VARCHAR2(4000) NOT NULL,
  CONTEXT VARCHAR2(4000) NULL,
  EXACT_MATCH VARCHAR2(1) DEFAULT 'Y' NOT NULL CONSTRAINT AUD_XHB_REF_TRANS_EX_MAT_CHK CHECK (EXACT_MATCH = 'Y' OR EXACT_MATCH = 'N'),
  LANGUAGE VARCHAR2(2) NOT NULL,
  COUNTRY VARCHAR2(2) NULL,
  OBS_IND VARCHAR2(1) DEFAULT 'N' NOT NULL CONSTRAINT AUD_XHB_REF_TRANS_OBS_IND_CHK CHECK (OBS_IND = 'Y' OR OBS_IND = 'N'),
  LAST_UPDATE_DATE DATE NOT NULL,
  CREATION_DATE DATE NOT NULL,
  CREATED_BY VARCHAR2(30) NOT NULL,
  LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
  VERSION NUMBER(5) NOT NULL,
  INSERT_EVENT VARCHAR2(1) NOT NULL
)
TABLESPACE AUDITD
;


/*
 * Changes, additions or deletion of sequences
 */

DROP SEQUENCE XHIBIT.XHB_REF_TRANSLATION_ID_SEQ;

CREATE SEQUENCE XHIBIT.XHB_REF_TRANSLATION_ID_SEQ
  START WITH 0
  NOMAXVALUE
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */
DROP TRIGGER XHB_REF_TRANSLATION_BIR_TR;

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_REF_TRANSLATION_BIR_TR

  BEFORE INSERT
  ON XHB_REF_TRANSLATION 
  FOR EACH ROW

BEGIN

  IF :NEW.REF_TRANSLATION_ID IS NULL THEN

    SELECT XHB_REF_TRANSLATION_ID_SEQ.NEXTVAL
    INTO   :NEW.REF_TRANSLATION_ID
    FROM   DUAL;

  END IF;

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


DROP TRIGGER XHB_REF_TRANSLATION_BUR_TR;

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_REF_TRANSLATION_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_TRANSLATION
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */

END;

/


CREATE OR REPLACE TRIGGER XHB_COURT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT
  FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURT_BUR_TR */
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT') = 1) THEN

    INSERT INTO AUD_COURT
           (COURT_ID,
            COURT_TYPE,
            CIRCUIT,
            COURT_NAME,
            CREST_COURT_ID,
            COURT_PREFIX,
            SHORT_NAME,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            ADDRESS_ID,
            CREST_IP_ADDRESS,
            IN_SERVICE_FLAG,
            OBS_IND,
            PROBATION_OFFICE_NAME,
            INTERNET_COURT_NAME,
            DISPLAY_NAME,
            COURT_CODE,
            COUNTRY,
            LANGUAGE,
            INSERT_EVENT)
    VALUES (:old.COURT_ID,
            :old.COURT_TYPE,
            :old.CIRCUIT,
            :old.COURT_NAME,
            :old.CREST_COURT_ID,
            :old.COURT_PREFIX,
            :old.SHORT_NAME,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.ADDRESS_ID,
            :old.CREST_IP_ADDRESS,
            :old.IN_SERVICE_FLAG,
            :old.OBS_IND,
            :old.PROBATION_OFFICE_NAME,
            :old.INTERNET_COURT_NAME,
            :old.DISPLAY_NAME,
            :old.COURT_CODE,
	    :old.country,
	    :old.LANGUAGE,
            l_trig_event);

  END IF;

END;

/


CREATE OR REPLACE TRIGGER XHB_DISPLAY_DOCUMENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPLAY_DOCUMENT
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */
        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY_DOCUMENT') = 1) THEN

    INSERT INTO AUD_DISPLAY_DOCUMENT
           (DISPLAY_DOCUMENT_ID,
            DESCRIPTION_CODE,
            DEFAULT_PAGE_DELAY,
            MULTIPLE_COURT_YN,
            CREATED_BY,
            CREATION_DATE,
            LAST_UPDATED_BY,
            LAST_UPDATE_DATE,
            VERSION,
            COUNTRY,
            LANGUAGE,
            INSERT_EVENT)
    VALUES (:OLD.DISPLAY_DOCUMENT_ID,
            :OLD.DESCRIPTION_CODE,
            :OLD.DEFAULT_PAGE_DELAY,
            :OLD.MULTIPLE_COURT_YN,
            :OLD.CREATED_BY,
            :OLD.CREATION_DATE,
            :OLD.LAST_UPDATED_BY,
            :OLD.LAST_UPDATE_DATE,
            :OLD.VERSION,
	    :OLD.COUNTRY,
	    :OLD.LANGUAGE,
            l_trig_event);

  END IF;

END;

/

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_DOCUMENT_CONTROL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_CONTROL
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_CONTROL') = 1) THEN

    INSERT INTO AUD_DOCUMENT_CONTROL (DOC_CONTROL_ID,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DISTRIBUTION_TYPE,
                                      MIME_TYPE,
                                      DOCUMENT_TYPE,
                                      LAST_UPDATE_DATE,
                                      CREATION_DATE,
                                      CREATED_BY,
                                      LAST_UPDATED_BY,
                                      VERSION,
                                      FORMATTING_ID,
                                      COURT_ID,
                                      DISTRIBUTED_DATE,
                                      XML_DOCUMENT_ID,
                                      FORMATTED_DOCUMENT_BLOB_ID,
				      LANGUAGE,
				      COUNTRY,
				      MAJOR_SCHEMA_VERSION,
				      MINOR_SCHEMA_VERSION,
                                      INSERT_EVENT)
                              VALUES (:OLD.DOC_CONTROL_ID,
                                      :OLD.STATUS,
                                      :OLD.EXPIRY_DATE,
                                      :OLD.DISTRIBUTION_TYPE,
                                      :OLD.MIME_TYPE,
                                      :OLD.DOCUMENT_TYPE,
                                      :OLD.LAST_UPDATE_DATE,
                                      :OLD.CREATION_DATE,
                                      :OLD.CREATED_BY,
                                      :OLD.LAST_UPDATED_BY,
                                      :OLD.VERSION,
                                      :OLD.FORMATTING_ID,
                                      :OLD.COURT_ID,
                                      :OLD.DISTRIBUTED_DATE,
                                      :OLD.XML_DOCUMENT_ID,
                                      :OLD.FORMATTED_DOCUMENT_BLOB_ID,
				      :OLD.LANGUAGE,
				      :OLD.COUNTRY,
				      :OLD.MAJOR_SCHEMA_VERSION,
				      :OLD.MINOR_SCHEMA_VERSION,
                                      l_trig_event);

  END IF;

END XHB_DOCUMENT_CONTROL_BUR_TR;

/

CREATE OR REPLACE TRIGGER XHB_FORMATTING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_FORMATTING
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMATTING') = 1) THEN

    INSERT INTO AUD_FORMATTING
           (formatting_id,
            date_in,
            format_status,
            distribution_type,
            mime_type,
            document_type,
            last_update_date,
            creation_date,
            created_by,
            last_updated_by,
            version,
            COURT_ID,
	    formatted_document_blob_id,
	    xml_document_clob_id,
	    language,
	    country,
 	    major_schema_version,
	    minor_schema_version,
            insert_event)
    VALUES (:old.formatting_id,
            :old.date_in,
            :old.format_status,
            :old.distribution_type,
            :old.mime_type,
            :old.document_type,
            :old.last_update_date,
            :old.creation_date,
            :old.created_by,
            :old.last_updated_by,
            :old.version,
            :old.COURT_ID,
	    :old.formatted_document_blob_id,
	    :old.xml_document_clob_id,
	    :old.language,
	    :old.country,
	    :old.major_schema_version,
	    :old.minor_schema_version,
            l_trig_event);

  END IF;

END;

/


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_XML_DOCUMENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_XML_DOCUMENT
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XML_DOCUMENT') = 1) THEN

    INSERT INTO AUD_XML_DOCUMENT (XML_DOCUMENT_ID,
                                  DATE_CREATED,
                                  DOCUMENT_TITLE,
                                  STATUS,
                                  EXPIRY_DATE,
                                  DOCUMENT_TYPE,
                                  LAST_UPDATE_DATE,
                                  CREATION_DATE,
                                  CREATED_BY,
                                  LAST_UPDATED_BY,
                                  VERSION,
                                  COURT_ID,
                                  XML_DOCUMENT_CLOB_ID,
				  LANGUAGE,
				  COUNTRY,
  				  MAJOR_SCHEMA_VERSION,
				  MINOR_SCHEMA_VERSION,
                                  INSERT_EVENT)
                          VALUES (:OLD.XML_DOCUMENT_ID,
                                  :OLD.DATE_CREATED,
                                  :OLD.DOCUMENT_TITLE,
                                  :OLD.STATUS,
                                  :OLD.EXPIRY_DATE,
                                  :OLD.DOCUMENT_TYPE,
                                  :OLD.LAST_UPDATE_DATE,
                                  :OLD.CREATION_DATE,
                                  :OLD.CREATED_BY,
                                  :OLD.LAST_UPDATED_BY,
                                  :OLD.VERSION,
                                  :OLD.COURT_ID,
                                  :OLD.XML_DOCUMENT_CLOB_ID,
				  :OLD.LANGUAGE,
				  :OLD.COUNTRY,
				  :OLD.MAJOR_SCHEMA_VERSION,
				  :OLD.MINOR_SCHEMA_VERSION,
                                  l_trig_event);

  END IF;

END XHB_XML_DOCUMENT_BUR_TR;

/

/*
 * Changes, additions or deletion of packages/procedures/functions
 */




/*
 * Changes, additions or deletion of standing data
 */
INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'CourtDetail','cy','XHIBIT','N');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'CourtList','cy','XHIBIT','N');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'DailyList','cy','XHIBIT','Y');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'AllCourtStatus','cy','XHIBIT','Y');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'SummaryByName','cy','XHIBIT','Y');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'JuryCurrentStatus','cy','XHIBIT','Y');

INSERT into xhb_display_document (country, created_by,default_page_delay,description_code,language,last_updated_by,multiple_court_yn) VALUES ('GB', 'XHIBIT', 10,'AllCaseStatus','cy','XHIBIT','Y');

UPDATE XHB_DISPLAY_DOCUMENT SET LANGUAGE = 'en' where LANGUAGE IS NULL;

UPDATE XHB_DISPLAY_DOCUMENT SET COUNTRY = 'GB' where COUNTRY IS NULL;

UPDATE XHB_COURT SET LANGUAGE='cy', COUNTRY='GB' WHERE COURT_NAME IN ('CARDIFF', 'MERTHYR TYDFIL', 'SWANSEA', 'CHESTER');


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.7', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.7', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.7', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.7', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

/*
 * Call translation script
 */

@@translation.sql
@@code_release_control_77.sql
