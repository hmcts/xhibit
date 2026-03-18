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

CREATE TABLE CJIT.MTBL_HTTP_CONNECTION_LOG
(
  CONNECTION_TIME  DATE,
  SYSTEM           VARCHAR2(32 BYTE),
  MAPNAME          VARCHAR2(64 BYTE),
  RETURN_CODE      VARCHAR2(192 BYTE),
  REL_ID	   VARCHAR2(250 BYTE)
)
;


CREATE INDEX MTBL_HTTP_LOG_M1 ON CJIT.MTBL_HTTP_CONNECTION_LOG
(CONNECTION_TIME)
;

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
TABLESPACE XHIBITD
PCTUSED    40
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCACHE
NOPARALLEL;

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


@@code_release_control.sql

DROP PACKAGE XHB_CONNECTED_USER_PKG;

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

conn xhibit/xhibit


/*
 * Changes, additions or deletion of standing data
 */

insert into CJIT.cji_parameter_value values ('BITSOFFLINEInterval',60);
insert into CJIT.cji_parameter_value values ('AggOFFLINEInterval',60);
insert into CJIT.cji_parameter_value values ('CJIPOFFLINEInterval',60);
commit;
INSERT INTO XHB_CREST_IMPORT_TYPE VALUES ('CS', 'CASE Synchronisation'); 
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

