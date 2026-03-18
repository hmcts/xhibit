/*
 * Filename:    DB_Patch_7_6.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        8th June 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 27/04/05	SS			Database changes made to comprise release 7.5
 * 08/06/05	SS			Renoved changes from this script that have already been added in Patch 7.5.
 * 20/08/05	SS			Added changes up to and including 19/09/05 (cutoff for release 7.6).
 * 26/10/05	CR			Added changes up for XHB_CASE_REFRESH_RESYNCH and trigger. 
 */ 

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


CREATE TABLE XHB_COURT_SATELLITE (
       COURT_SATELLITE_ID NUMBER(8) NOT NULL,
       COURT_SITE_ID NUMBER(8) NOT NULL,
       INTERNET_SATELLITE_NAME VARCHAR2(255) NOT NULL,
       LAST_UPDATE_DATE DATE NOT NULL,
       CREATION_DATE DATE NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       VERSION NUMBER(5) NOT NULL
)
TABLESPACE XHIBITD
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

ALTER TABLE XHB_COURT_SATELLITE ADD (PRIMARY KEY (COURT_SATELLITE_ID)
USING INDEX TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0)); 

ALTER TABLE XHB_COURT_SATELLITE ADD (FOREIGN KEY (COURT_SITE_ID) REFERENCES XHB_COURT_SITE);


CREATE TABLE XHB_MTBL_ADAPTOR_CONFIG (
       MTBL_ADAPTOR_CONFIG_ID NUMBER(8)NOT NULL,
       ADAPTOR_TYPE VARCHAR2(5) NOT NULL,
       PARAMETERS VARCHAR2(255) NULL,
       SERVER_TYPE VARCHAR2(10) NULL,
       SERVER_ADDRESS VARCHAR2(64) NULL,
       USERNAME VARCHAR2(64) NULL,
       PASSWORD VARCHAR2(64) NULL,
       DOCUMENT_NAME VARCHAR2(64) NULL,
       LAST_UPDATE_DATE DATE NOT NULL,
       CREATION_DATE DATE NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL, 
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       VERSION NUMBER(5) NOT NULL
)
TABLESPACE XHIBITD
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

ALTER TABLE XHB_MTBL_ADAPTOR_CONFIG ADD (
PRIMARY KEY (MTBL_ADAPTOR_CONFIG_ID) 
USING INDEX TABLESPACE XHIBITX 
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0));



CREATE UNIQUE INDEX xhb_def_court_id_crest_def_idx on XHB_defendant(COURT_ID ASC, CREST_DEFENDANT_ID ASC)
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

ALTER TABLE xhb_psr_request MODIFY (PROBATION_OFFICE_NAME VARCHAR2(50));

ALTER TABLE XHB_SKELETON_SESSION ADD (OBS_IND VARCHAR2(1) DEFAULT 'N');

DROP TABLE XHB_USER_PRINCIPAL;

DROP TABLE XHB_CONNECTED_USER;

CREATE TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM (	DEF_ON_CASE_REF_SOL_FIRM_ID   	NUMBER(8)	NOT NULL,       
						DEFENDANT_ON_CASE_ID       			NUMBER(8)  	NOT NULL,       
						REF_SOLICITOR_FIRM_ID		NUMBER(8)	NULL, 
						CREST_CPF_ID			NUMBER(8)	NOT NULL,
      						REP_TYPE			VARCHAR2(1)	NOT NULL,
						REP_ST_DATE			DATE		NOT NULL,
						REP_END_DATE			DATE		NULL,
						LAST_UPDATE_DATE  		DATE 		NOT NULL,       
						CREATION_DATE     		DATE 		NOT NULL,       
						CREATED_BY        		VARCHAR2(30) 	NOT NULL,       
						LAST_UPDATED_BY   		VARCHAR2(30) 	NOT NULL,       	
						VERSION           		NUMBER(5) 	NOT NULL)         
TABLESPACE XHIBITD         
STORAGE (INITIAL 256K
         NEXT 256K                  
	 PCTINCREASE 0);

ALTER TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM ADD (PRIMARY KEY (DEF_ON_CASE_REF_SOL_FIRM_ID) USING INDEX TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K  PCTINCREASE 0));

ALTER TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM ADD (FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE);

ALTER TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM ADD (FOREIGN KEY (REF_SOLICITOR_FIRM_ID) REFERENCES XHB_REF_SOLICITOR_FIRM);

alter table MTBL_MERC_DL_STORAGE add  (DL_TRIGGER VARCHAR2(1) default 'N');

CREATE TABLE XHB_CASE_REFRESH_RESYNCH
(
  CASE_ID                       NUMBER(8)       NOT NULL,
  LAST_CHARGE_IMPORT_INDICATOR  VARCHAR2(2 BYTE),
  LAST_UPDATE_DATE              DATE            NOT NULL,
  CREATION_DATE                 DATE            NOT NULL,
  CREATED_BY                    VARCHAR2(30 BYTE) NOT NULL,
  LAST_UPDATED_BY               VARCHAR2(30 BYTE) NOT NULL,
  VERSION                       NUMBER(5)       NOT NULL
)
TABLESPACE XHIBITD
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);


ALTER TABLE XHB_CASE_REFRESH_RESYNCH ADD (
  CONSTRAINT XHB_CASE_REFRESH_RESYNCH_FK FOREIGN KEY (CASE_ID) 
    REFERENCES XHB_CASE (CASE_ID));


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


CREATE TABLE AUD_COURT_SATELLITE (
       COURT_SATELLITE_ID NUMBER(8) NOT NULL,
       COURT_SITE_ID NUMBER(8) NOT NULL,
       INTERNET_SATELLITE_NAME VARCHAR2(255) NOT NULL,
       LAST_UPDATE_DATE DATE NOT NULL,
       CREATION_DATE DATE NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       VERSION NUMBER(5) NOT NULL,
       INSERT_EVENT VARCHAR2(1) NOT NULL
)
TABLESPACE AUDITD;


CREATE TABLE AUD_MTBL_ADAPTOR_CONFIG (
       MTBL_ADAPTOR_CONFIG_ID NUMBER(8)NOT NULL,
       ADAPTOR_TYPE VARCHAR2(5) NOT NULL,
       PARAMETERS VARCHAR2(255) NULL,
       SERVER_TYPE VARCHAR2(10) NULL,
       SERVER_ADDRESS VARCHAR2(64) NULL,
       USERNAME VARCHAR2(64) NULL,
       PASSWORD VARCHAR2(64) NULL,
       DOCUMENT_NAME VARCHAR2(64) NULL,
       LAST_UPDATE_DATE DATE NOT NULL,
       CREATION_DATE DATE NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL, 
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       VERSION NUMBER(5) NOT NULL,
       INSERT_EVENT VARCHAR2(1)
)
TABLESPACE AUDITD;


ALTER TABLE aud_psr_request MODIFY (PROBATION_OFFICE_NAME VARCHAR2(50));

ALTER TABLE aud_SKELETON_SESSION ADD (OBS_IND VARCHAR2(1) DEFAULT 'N');

DROP TABLE AUD_USER_PRINCIPAL;

DROP TABLE AUD_CONNECTED_USER;

CREATE TABLE AUD_DEF_ON_CASE_REF_SOL_FIRM (DEF_ON_CASE_REF_SOL_FIRM_ID   	NUMBER(8)	NOT NULL,       
					   DEFENDANT_ON_CASE_ID                 NUMBER(8)  	NOT NULL,       
					   REF_SOLICITOR_FIRM_ID		NUMBER(8)	NULL, 
					   CREST_CPF_ID			        NUMBER(8)	NOT NULL,
      					   REP_TYPE                             VARCHAR2(1)	NOT NULL,
					   REP_ST_DATE			        DATE		NOT NULL,
					   REP_END_DATE			        DATE		NULL,
				           LAST_UPDATE_DATE  		        DATE 		NOT NULL,       
					   CREATION_DATE     		        DATE 		NOT NULL,       
					   CREATED_BY        		        VARCHAR2(30) 	NOT NULL,       
					   LAST_UPDATED_BY   		        VARCHAR2(30) 	NOT NULL,       	
					   VERSION           		        NUMBER(5) 	NOT NULL,
                                           INSERT_EVENT			        VARCHAR2(1)             )         
TABLESPACE AUDITD         
STORAGE (INITIAL 256K
         NEXT 256K                  
	 PCTINCREASE 0);



/*
 * Changes, additions or deletion of sequences
 */


CREATE SEQUENCE XHB_COURT_SATELLITE_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;


CREATE SEQUENCE XHB_MTBL_ADAPTOR_CONFIG_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;

CREATE SEQUENCE XHB_DEF_CASE_SOL_FIRM_SEQ NOMAXVALUE NOMINVALUE NOCACHE  NOCYCLE NOORDER;


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER XHB_COURT_SATELLITE_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_SATELLITE
  FOR EACH ROW
BEGIN

  IF :NEW.COURT_SATELLITE_ID IS NULL THEN

    SELECT XHB_COURT_SATELLITE_SEQ.NEXTVAL
    INTO   :NEW.COURT_SATELLITE_ID
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




CREATE OR REPLACE TRIGGER XHB_COURT_SATELLITE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_SATELLITE
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  IF UPDATING THEN

    l_trig_event := 'U';

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONNECTED_USER') = 1) THEN

    INSERT INTO AUD_COURT_SATELLITE
    VALUES (:OLD.COURT_SATELLITE_ID,
            :OLD.COURT_SITE_ID,
            :OLD.INTERNET_SATELLITE_NAME,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/





CREATE OR REPLACE TRIGGER XHB_MTBL_ADAPTOR_CONFIG_BIR_TR
  BEFORE INSERT
  ON XHB_MTBL_ADAPTOR_CONFIG
  FOR EACH ROW
BEGIN

  IF :NEW.MTBL_ADAPTOR_CONFIG_ID IS NULL THEN

    SELECT XHB_MTBL_ADAPTOR_CONFIG_SEQ.NEXTVAL
    INTO   :NEW.MTBL_ADAPTOR_CONFIG_ID
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



CREATE OR REPLACE TRIGGER XHB_MTBL_ADAPTOR_CONFIG_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_MTBL_ADAPTOR_CONFIG
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  IF UPDATING THEN

    l_trig_event := 'U';

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONNECTED_USER') = 1) THEN

    INSERT INTO AUD_MTBL_ADAPTOR_CONFIG
    VALUES (:OLD.MTBL_ADAPTOR_CONFIG_ID,
            :OLD.ADAPTOR_TYPE,
            :OLD.PARAMETERS,
            :OLD.SERVER_TYPE,
            :OLD.SERVER_ADDRESS,
            :OLD.USERNAME,
            :OLD.PASSWORD,
            :OLD.DOCUMENT_NAME,
	    :OLD.LAST_UPDATE_DATE,
	    :OLD.CREATION_DATE,
	    :OLD.CREATED_BY,
	    :OLD.LAST_UPDATED_BY,
	    :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/


CREATE OR REPLACE TRIGGER XHIBIT.XHB_INTERNET_HTML_BUR_TR
BEFORE DELETE OR UPDATE
ON XHIBIT.XHB_INTERNET_HTML 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORM
ATION */

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

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_IN
FORMATION */

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')

      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;


  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_INTERNET_HTML') = 1) THEN

    INSERT INTO AUD_INTERNET_HTML (INTERNET_HTML_ID,
                                   STATUS,
                                   LAST_UPDATE_DATE,
                                   CREATION_DATE,
                                   CREATED_BY,
                                   LAST_UPDATED_BY,
                                   VERSION,
                                   COURT_ID,
                                   HTML_BLOB_ID,
                                   INSERT_EVENT)
                           VALUES (:OLD.INTERNET_HTML_ID,
                                   :OLD.STATUS,
                                   :OLD.LAST_UPDATE_DATE,
                                   :OLD.CREATION_DATE,
                                   :OLD.CREATED_BY,
                                   :OLD.LAST_UPDATED_BY,
                                   :OLD.VERSION,
                                   :OLD.COURT_ID,
                                   :OLD.HTML_BLOB_ID,
                                   l_trig_event);

  END IF;

END;
/



CREATE OR REPLACE TRIGGER "XHIBIT".XHB_SKELETON_SESSION_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SKELETON_SESSION
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SKELETON_SESSION') = 1) THEN

    INSERT INTO AUD_SKELETON_SESSION
    VALUES (:OLD.skeleton_session_id,
            :OLD.morning_or_afternoon,
            :OLD.notes,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            :OLD.skeleton_day_id,
            :OLD.skeleton_id,
            l_trig_event,
            :OLD.obs_ind);

  END IF;

END;
/


CREATE OR REPLACE TRIGGER XHB_DEF_CASE_SOL_FIRM_BIR_TR
  BEFORE INSERT
  ON XHB_DEF_ON_CASE_REF_SOL_FIRM
  FOR EACH ROW

BEGIN

  IF :NEW.DEF_ON_CASE_REF_SOL_FIRM_ID IS NULL THEN

    SELECT XHB_DEF_CASE_SOL_FIRM_SEQ .NEXTVAL
    INTO   :NEW.DEF_ON_CASE_REF_SOL_FIRM_ID
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



CREATE OR REPLACE TRIGGER XHB_DEF_CASE_SOL_FIRM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEF_ON_CASE_REF_SOL_FIRM
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  IF UPDATING THEN

    l_trig_event := 'U';

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEF_ON_CASE_REF_SOL_FIRM') = 1) THEN

    INSERT INTO AUD_DEF_ON_CASE_REF_SOL_FIRM
    VALUES (:OLD.DEF_ON_CASE_REF_SOL_FIRM_ID,
            :OLD.DEFENDANT_ON_CASE_ID,
            :OLD.REF_SOLICITOR_FIRM_ID,
            :OLD.CREST_CPF_ID,
            :OLD.REP_TYPE,
            :OLD.REP_ST_DATE,
	    :OLD.REP_END_DATE,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/



CREATE OR REPLACE TRIGGER XHIBIT.XHB_CASE_REFRESH_TRIGGER
  BEFORE UPDATE
  ON XHB_CASE
  FOR EACH ROW
DECLARE

  l_charge_import VARCHAR2(2);
  l_case_id	  NUMBER(8)   := NULL;
  tmp		  NUMBER(1)   := NULL;
  tmp2		  NUMBER(8)   := NULL;

BEGIN
  IF INSERTING THEN
	l_case_id := :NEW.case_id;
	l_charge_import := :NEW.charge_import_indicator;
  ELSE
	l_case_id := :OLD.case_id;
	l_charge_import := :OLD.charge_import_indicator;
  END IF;

  SELECT COUNT(*) INTO tmp FROM XHB_CASE_REFRESH_RESYNCH
	WHERE case_id = l_case_id;


  IF tmp > 0 THEN
      UPDATE XHB_CASE_REFRESH_RESYNCH SET LAST_CHARGE_IMPORT_INDICATOR = l_charge_import
	WHERE case_id = l_case_id;
  ELSE
      INSERT INTO XHB_CASE_REFRESH_RESYNCH VALUES (l_case_id, l_charge_import, SYSDATE, SYSDATE, 'XHIBIT', 'XHIBIT', 1);
  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


@@code_release_control.sql

DROP PACKAGE XHB_CONNECTED_USER_PKG;


/*
 * Changes, additions or deletion of standing data
 */

INSERT INTO XHB_CREST_IMPORT_TYPE VALUES ('CS', 'CASE Synchronisation'); 
commit;


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.6', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.6', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.6', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.6', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

