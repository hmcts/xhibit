/*
 * Filename:    DB_Patch_7_0.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        17th November 2004
 */

/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 17/01/2005	KL	18		Add new package xhb_connected_user_pkg
 * 17/01/2005	KL	19		Modify USER_NAME column on table XHB_CONNECTED_USER so it contains VARCHAR2(255)
 * 17/01/2005	KL	20		Remove column PROCESS_TYPE from table XHB_FORMATTING
 * 20/01/2005	KL	21		Drop and recreate index xhb_court_log_event_desc_type
 * 20/01/2005	KL	23		Create new indexes for connected user details
 * 27/01/2005	SO	22		Create new table xhb_ref_calendar 
 * 27/01/2005	SO	24		Update package body Xhb_Connected_User_Pkg
 * 28/01/2005	SO	25		Added script m4ora8.sql from Abbas Hussain
 *  7/02/2005	SO	27		Update package xhb_list_distribution_pkg
 * 
 *
 */ 

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

CREATE TABLE XHB_CONNECTED_USER (
       CONNECTED_USER_ID         NUMBER(8)    NOT NULL,
       COMPONENT                 VARCHAR2(30) NOT NULL,
       USER_NAME                 VARCHAR2(100) NOT NULL,
       TERMINAL_NAME             VARCHAR2(30) NULL,
       TERMINAL_ID               NUMBER(8)    NULL,
       IP_ADDRESS                VARCHAR2(30) NULL,
       LOCATION                  VARCHAR2(170) NULL,
       LAST_ACCESS_TIME          DATE         NULL,
       LAST_UPDATE_DATE          DATE         NOT NULL,
       CREATION_DATE             DATE         NOT NULL,
       CREATED_BY                VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY           VARCHAR2(30) NOT NULL,
       VERSION                   NUMBER(5)    NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 256K
                  NEXT 256K
                  PCTINCREASE 0);


CREATE TABLE XHB_USER_PRINCIPAL (
       USER_PRINCIPAL_ID         NUMBER(8)    NOT NULL,
       CONNECTED_USER_ID         NUMBER(8)    NOT NULL,
       PRINCIPAL                 VARCHAR2(30) NOT NULL,
       LAST_UPDATE_DATE          DATE         NOT NULL,
       CREATION_DATE             DATE         NOT NULL,
       CREATED_BY                VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY           VARCHAR2(30) NOT NULL,
       VERSION                   NUMBER(5)    NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 256K
                  NEXT 256K
                  PCTINCREASE 0);

ALTER TABLE XHB_CONNECTED_USER
       ADD (CONSTRAINT XHB_CONNECTED_USER_PK PRIMARY KEY (CONNECTED_USER_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 256K
                NEXT 256K
                PCTINCREASE 0));

ALTER TABLE XHB_USER_PRINCIPAL
       ADD (CONSTRAINT XHB_USER_PRINCIPAL_PK PRIMARY KEY (USER_PRINCIPAL_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 256K
                NEXT 256K
                PCTINCREASE 0));

ALTER TABLE XHB_USER_PRINCIPAL
      ADD (CONSTRAINT CONNECTED_USER_ID_FK FOREIGN KEY (CONNECTED_USER_ID) REFERENCES XHB_CONNECTED_USER);

ALTER TABLE XHB_PLEA ADD (ALT_UNCODED_OFFENCE_DESC VARCHAR2(240) NULL); 
ALTER TABLE XHB_VERDICT ADD (ALT_UNCODED_OFFENCE_DESC VARCHAR2(240) NULL); 


ALTER TABLE XHB_VERDICT ADD (
DISPOSAL2_ID NUMBER(8) NULL,
DEFENDANT_ON_CASE_ID NUMBER(8) NULL);

ALTER TABLE XHB_VERDICT ADD (
CONSTRAINT VERDICT_DISPOSAL2_ID_FK FOREIGN KEY (DISPOSAL2_ID) REFERENCES XHB_DISPOSAL2,
CONSTRAINT VERDICT_DEF_ON_CASE_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE); 

ALTER TABLE XHB_TERMINAL 
ADD (COURT_ID NUMBER(8),
     ROAMING VARCHAR2(1) DEFAULT 'N' NOT NULL,
     CONSTRAINT TERMINAL_ROAMING_CHK CHECK (ROAMING IN ('Y','N')),
     CONSTRAINT TERMINAL_COURT_ID_FK FOREIGN KEY (COURT_ID) 
     REFERENCES XHB_COURT (COURT_ID));

CREATE INDEX TERMINAL_COURT_FK_IDX ON XHB_TERMINAL(COURT_ID)
TABLESPACE XHIBITX
STORAGE (INITIAL 256K
            NEXT 256K
     PCTINCREASE 0);

ALTER TABLE XHB_WLL_RECIPIENT 
ADD  (recipient_type VARCHAR2(1) DEFAULT 'S' NOT NULL, 
      CONSTRAINT wll_recipient_type_CHK CHECK (recipient_type IN ('S','O'))); 

ALTER TABLE XHB_CONNECTED_USER MODIFY (USER_NAME VARCHAR2(255));

ALTER TABLE XHB_FORMATTING DROP COLUMN PROCESS_TYPE;

DROP INDEX xhb_court_log_event_desc_type;
CREATE UNIQUE INDEX xhb_court_log_event_desc_type ON XHB_COURT_LOG_EVENT_DESC (event_type) 
TABLESPACE XHIBITX 
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0); 

CREATE INDEX xhb_connected_user_loc_idx ON XHB_CONNECTED_USER (location) 
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

CREATE INDEX xhb_connected_user_name_idx ON XHB_CONNECTED_USER (user_name) 
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0); 

/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

CREATE TABLE AUD_CONNECTED_USER TABLESPACE AUDITD AS
SELECT *
FROM   XHB_CONNECTED_USER
WHERE  1 = 0;

CREATE TABLE AUD_USER_PRINCIPAL TABLESPACE AUDITD AS
SELECT *
FROM   XHB_USER_PRINCIPAL
WHERE  1 = 0;

ALTER TABLE AUD_CONNECTED_USER ADD (INSERT_EVENT VARCHAR2(1) NOT NULL);
ALTER TABLE AUD_USER_PRINCIPAL ADD (INSERT_EVENT VARCHAR2(1) NOT NULL);

ALTER TABLE AUD_PLEA ADD (ALT_UNCODED_OFFENCE_DESC VARCHAR2(240) NULL); 
ALTER TABLE AUD_VERDICT ADD (ALT_UNCODED_OFFENCE_DESC VARCHAR2(240) NULL); 

ALTER TABLE AUD_VERDICT ADD (
DISPOSAL2_ID NUMBER(8) NULL,
DEFENDANT_ON_CASE_ID NUMBER(8) NULL);

ALTER TABLE AUD_TERMINAL ADD (COURT_ID NUMBER(8),
                              ROAMING VARCHAR2(1) DEFAULT 'N' NOT NULL);

ALTER TABLE AUD_WLL_RECIPIENT 
ADD  (recipient_type VARCHAR2(1));

ALTER TABLE AUD_CONNECTED_USER MODIFY (USER_NAME VARCHAR2(255));

ALTER TABLE AUD_FORMATTING DROP COLUMN PROCESS_TYPE;

/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE XHB_CONNECTED_USER_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;

CREATE SEQUENCE XHB_USER_PRINCIPAL_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER XHB_CONNECTED_USER_BIR_TR
  BEFORE INSERT
  ON XHB_CONNECTED_USER
  FOR EACH ROW
BEGIN

  IF :NEW.CONNECTED_USER_ID IS NULL THEN

    SELECT XHB_CONNECTED_USER_SEQ.NEXTVAL
    INTO   :NEW.CONNECTED_USER_ID
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
show errors

CREATE OR REPLACE TRIGGER XHB_USER_PRINCIPAL_BIR_TR
  BEFORE INSERT
  ON XHB_USER_PRINCIPAL
  FOR EACH ROW
BEGIN

  IF :NEW.USER_PRINCIPAL_ID IS NULL THEN

    SELECT XHB_USER_PRINCIPAL_SEQ.NEXTVAL
    INTO   :NEW.USER_PRINCIPAL_ID
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
show errors

CREATE OR REPLACE TRIGGER XHB_CONNECTED_USER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CONNECTED_USER
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

    INSERT INTO AUD_CONNECTED_USER
    VALUES (:OLD.CONNECTED_USER_ID,
            :OLD.COMPONENT,
            :OLD.USER_NAME,
            :OLD.TERMINAL_NAME,
            :OLD.TERMINAL_ID,
            :OLD.IP_ADDRESS,
            :OLD.LOCATION,
            :OLD.LAST_ACCESS_TIME,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/
show errors


CREATE OR REPLACE TRIGGER XHB_USER_PRINCIPAL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_USER_PRINCIPAL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_USER_PRINCIPAL') = 1) THEN

    INSERT INTO AUD_USER_PRINCIPAL
    VALUES (:OLD.USER_PRINCIPAL_ID,
            :OLD.CONNECTED_USER_ID,
            :OLD.PRINCIPAL,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/
show errors

CREATE OR REPLACE TRIGGER XHB_PLEA_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PLEA
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PLEA') = 1) THEN

    INSERT INTO AUD_PLEA 
            (plea_id, 
            ref_plea_id, 
            other_plea_text, 
            breach_admitted, 
            alt_ref_offence_id, 
            arraignment_date, 
            def_on_charge_or_offence, 
            obs_ind, 
            DEFENDANT_CHARGE_ID, 
            DEFENDANT_ON_OFFENCE_ID, 
            LAST_UPDATE_DATE, 
            CREATION_DATE, 
            CREATED_BY, 
            LAST_UPDATED_BY, 
            VERSION,
            ALT_UNCODED_OFFENCE_DESC,   
            insert_event) 
    VALUES (:old.plea_id, 
            :old.ref_plea_id, 
            :old.other_plea_text, 
            :old.breach_admitted, 
            :old.alt_ref_offence_id, 
            :old.arraignment_date, 
            :old.def_on_charge_or_offence, 
            :old.obs_ind, 
            :old.DEFENDANT_CHARGE_ID, 
            :old.DEFENDANT_ON_OFFENCE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            :old.ALT_UNCODED_OFFENCE_DESC,
            l_trig_event);

  END IF;

END;
/
show errors

ALTER TRIGGER XHB_PLEA_BIR_TR COMPILE;
show errors


CREATE OR REPLACE TRIGGER XHB_TERMINAL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_TERMINAL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_TERMINAL') = 1) THEN

    INSERT INTO AUD_TERMINAL 
           (TERMINAL_ID,            
            LOCATION,               
            DESCRIPTION,            
            TERMINAL_IP,            
            TERMINAL_NAME,          
            COURT_ROOM_ID,          
            VERSION,                
            LAST_UPDATED_BY,        
            CREATED_BY,             
            CREATION_DATE,          
            LAST_UPDATE_DATE,       
            COURTROOM_OR_SITE,      
            COURT_SITE_ID,
            COURT_ID,
            ROAMING,
            insert_event)          
    VALUES (:old.TERMINAL_ID, 
            :old.LOCATION, 
            :old.DESCRIPTION, 
            :old.TERMINAL_IP, 
            :old.TERMINAL_NAME, 
            :old.COURT_ROOM_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.courtroom_or_site, 
            :old.COURT_SITE_ID,
            :old.COURT_ID,
            :old.ROAMING,
            l_trig_event);

  END IF;

END;
/
show errors

ALTER TRIGGER XHB_TERMINAL_BIR_TR COMPILE;
show errors


CREATE OR REPLACE TRIGGER XHB_VERDICT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_VERDICT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_VERDICT') = 1) THEN

    INSERT INTO AUD_VERDICT 
           (VERDICT_ID, 
            OBS_IND, 
            DEF_ON_CHARGE_OR_OFFENCE, 
            JURORS_DISSENTING, 
            JURORS_ASSENTING, 
            ALT_REF_OFFENCE_ID, 
            VERDICT_DATE, 
            REF_VERDICT_ID, 
            VERSION, 
            LAST_UPDATED_BY, 
            other_verdict_text, 
            CREATED_BY, 
            CREATION_DATE, 
            LAST_UPDATE_DATE, 
            DEFENDANT_ON_OFFENCE_ID, 
            DEFENDANT_CHARGE_ID, 
            REF_APP_RESULT_ID,
            CASE_ID,
            APP_LESSER_OFF,
            ALT_UNCODED_OFFENCE_DESC, 
	    DISPOSAL2_ID,
            DEFENDANT_ON_CASE_ID,
            INSERT_EVENT) 
    VALUES (:old.VERDICT_ID, 
            :old.OBS_IND, 
            :old.DEF_ON_CHARGE_OR_OFFENCE, 
            :old.JURORS_DISSENTING, 
            :old.JURORS_ASSENTING, 
            :old.ALT_REF_OFFENCE_ID, 
            :old.VERDICT_DATE, 
            :old.REF_VERDICT_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.other_verdict_text, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.DEFENDANT_ON_OFFENCE_ID, 
            :old.DEFENDANT_CHARGE_ID, 
            :old.REF_APP_RESULT_ID,
            :old.CASE_ID,
            :old.APP_LESSER_OFF,
            :old.ALT_UNCODED_OFFENCE_DESC, 
	    :old.DISPOSAL2_ID,
            :old.DEFENDANT_ON_CASE_ID,
            l_trig_event);

  END IF;

END;
/
show errors

ALTER TRIGGER XHB_VERDICT_BIR_TR COMPILE;
show errors

CREATE OR REPLACE TRIGGER XHB_WLL_RECIPIENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_WLL_RECIPIENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WLL_RECIPIENT') = 1) THEN

    INSERT INTO AUD_WLL_RECIPIENT 
        (wll_recipient_id, 
        crest_solicitor_firm_id, 
        solicitor_firm_name, 
        solictior_firm_address, 
        solicitor_firm_fax, 
        solicitor_firm_email, 
        LAST_UPDATE_DATE, 
        CREATION_DATE, 
        CREATED_BY, 
        LAST_UPDATED_BY, 
        VERSION, 
        COURT_ID,
        recipient_type,
        insert_event) 
    VALUES (:old.wll_recipient_id, 
            :old.crest_solicitor_firm_id, 
            :old.solicitor_firm_name, 
            :old.solictior_firm_address, 
            :old.solicitor_firm_fax, 
            :old.solicitor_firm_email, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID,
            :old.recipient_type,
            l_trig_event);

  END IF;

END;
/
show errors

ALTER TRIGGER XHB_WLL_RECIPIENT_BIR_TR COMPILE;
show errors

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
            l_trig_event);

  END IF;

END;
/
show errors

ALTER TRIGGER  XHB_FORMATTING_BIR_TR  COMPILE;
ALTER TRIGGER  XHB_FORMATTING_AUR_TR  COMPILE;

CREATE TABLE XHB_REF_CALENDAR (
cal_date DATE NOT NULL,
court_id NUMBER(8) NOT NULL,
avail VARCHAR2(1) NOT NULL,
sys_ac_avail VARCHAR2(1) NOT NULL,
description VARCHAR2(35),
last_update_date DATE NOT NULL,
creation_date DATE NOT NULL,
created_by VARCHAR2(30) NOT NULL,
last_updated_by VARCHAR2(30) NOT NULL,
version NUMBER(5) NOT NULL,
CONSTRAINT REF_CALENDAR_AVAIL_CHK CHECK (avail IN ('Y', 'N')),
CONSTRAINT REF_CALENDAR_SYS_AC_AVAIL_CHK CHECK (avail IN ('Y', 'N')),
CONSTRAINT REF_CALENDAR_COURT_ID_FK FOREIGN KEY (court_id) REFERENCES XHB_COURT(court_id)
)
 TABLESPACE XHIBITD
         STORAGE (INITIAL 256K
                  NEXT 256K
                  PCTINCREASE 0);

ALTER TABLE XHB_REF_CALENDAR
       ADD (CONSTRAINT XHB_REF_CALENDAR_PK PRIMARY KEY (court_id, cal_date)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 256K
                NEXT 256K
                PCTINCREASE 0));

CREATE TABLE AUD_REF_CALENDAR TABLESPACE AUDITD AS
SELECT *
FROM   XHB_REF_CALENDAR
WHERE  1 = 0;

ALTER TABLE AUD_REF_CALENDAR ADD (INSERT_EVENT VARCHAR2(1) NOT NULL);

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_REF_CALENDAR_BIR_TR
BEFORE INSERT
ON XHB_REF_CALENDAR
FOR EACH ROW
BEGIN

IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
(:NEW.CREATED_BY IS NULL)) THEN

SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
SYS_CONTEXT('USERENV', 'SESSION_USER')
INTO :NEW.LAST_UPDATED_BY,
:NEW.CREATED_BY
FROM DUAL;

END IF;

SELECT SYSDATE,
SYSDATE,
1
INTO :NEW.LAST_UPDATE_DATE,
:NEW.CREATION_DATE,
:NEW.VERSION
FROM DUAL;

END XHB_REF_CALENDAR_BIR_TR;
/
show errors

CREATE OR REPLACE TRIGGER XHB_REF_CALENDAR_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_CALENDAR
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_CALENDAR') = 1) THEN

    INSERT INTO AUD_REF_CALENDAR (
	    cal_date,
	    court_id,
	    avail,
	    sys_ac_avail,
	    description,
	    last_update_date,
	    creation_date,
	    created_by,
	    last_updated_by,
	    version,
	    insert_event)
    VALUES (:OLD.cal_date,
	    :OLD.court_id,
	    :OLD.avail,
	    :OLD.sys_ac_avail,
	    :OLD.description,
	    :OLD.last_update_date,
	    :OLD.creation_date,
	    :OLD.created_by,
	    :OLD.last_updated_by,
	    :OLD.version,
	    l_trig_event);

  END IF;

END;
/
show errors




/*
 * Changes, additions or deletion of packages/procedures/functions
 */

-------------------------------------------------------------------------------
-- THE PACKAGE HEADER
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE xhb_search_pkg AS
    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE);


    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE);


    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE);


    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE);


    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE);


    PROCEDURE get_ref_hearing_type(p_results_out             OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in    IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
				   p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE);


    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE);


    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE);


    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE);


    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE,
	                      p_obs_ind_in      IN  XHB_REF_OFFENCE.obs_ind%TYPE);


    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE);


    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE);


    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE);
END xhb_search_pkg;
/
show errors
-------------------------------------------------------------------------------
-- THE PACKAGE BODY
--
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-- 
-- Couple of comments on the package structure:
--
-- The method log_entry (and the script to create the required table) has been
-- left in following investiations into the number of times each method was
-- called (and the resultant changes).
--
-- Each method that requires a call to the convert_values function (which is
-- now package private) does so in the declaration section of the procedure,
-- this is to prevent the call being performed for every row in the query.
-- (this resulted from the log_entry investigation).
--
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY xhb_search_pkg AS

--    CREATE TABLE search_pkg_calls(procedure_name VARCHAR2(100), access_time DATE DEFAULT SYSDATE);
--
--    PROCEDURE log_entry(procedure_name_in IN search_pkg_calls.procedure_name%TYPE)
--    AS
--        PRAGMA AUTONOMOUS_TRANSACTION;
--    BEGIN
--        INSERT INTO search_pkg_calls (procedure_name) VALUES (procedure_name_in);
--        COMMIT;
--    END log_entry;





    -- TBD: Would declaring this deterministic be of benefit?
    -- How common are the search strings?
    FUNCTION convert_value(p_value_in IN VARCHAR2)
        RETURN VARCHAR2 AS
    BEGIN
        --log_entry('convert_value()');
    
        -- Only convert to uppercase for now
        RETURN UPPER(p_value_in);
    END convert_value;





    --
    -- Query the XHB_COURT table, with some details from the XHB_COURT_SITE table
    --
    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE)
    AS
        l_circuit        CONSTANT XHB_COURT.circuit%TYPE        := convert_value(p_circuit_in);
        l_court_name     CONSTANT XHB_COURT.court_name%TYPE     := convert_value(p_court_name_in);
        l_court_prefix   CONSTANT XHB_COURT.court_prefix%TYPE   := convert_value(p_court_prefix_in);
        l_court_type     CONSTANT XHB_COURT.court_type%TYPE     := convert_value(p_court_type_in);
        l_crest_court_id CONSTANT XHB_COURT.crest_court_id%TYPE := convert_value(p_crest_court_id_in);
        l_short_name     CONSTANT XHB_COURT.short_name%TYPE     := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court');
    
        OPEN p_results_out FOR
            SELECT c.court_id AS "id",
                   cs.court_site_code AS "COURT_CODE",
                   c.*
            FROM   XHB_COURT c,
                   XHB_COURT_SITE cs
            WHERE  c.court_id                =    cs.court_id
            AND    ((c.obs_ind IS NULL) OR (c.obs_ind = 'N'))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id        =    p_court_site_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)        LIKE l_circuit))
            AND    ((l_court_name IS NULL)
                        OR (UPPER(c.court_name)     LIKE l_court_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(c.court_prefix)   LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(c.court_type)     LIKE l_court_type))
            AND    ((l_crest_court_id IS NULL)   
                        OR (UPPER(c.crest_court_id) LIKE l_crest_court_id))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)     LIKE l_short_name));
    END get_court;


    --
    -- Query the XHB_COURT_ROOM table, but also with search criteria from
    -- XHB_COURT_SITE and XHB_COURT
    --
    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE)
    AS
        l_court_room_name CONSTANT XHB_COURT_ROOM.court_room_name%TYPE := convert_value(p_court_room_name_in);
        l_court_site_code CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_short_name      CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court_room');
    
        OPEN p_results_out FOR
            SELECT DISTINCT cr.court_room_id AS "id",
                   t.location AS location,
                   cr.*
            FROM   XHB_COURT_ROOM cr,
                   XHB_COURT_SITE cs,
                   XHB_COURT c,
                   XHB_TERMINAL t
            WHERE  cr.court_site_id = cs.court_site_id
            AND    c.court_id       = cs.court_id
            AND    cr.court_room_id = t.court_room_id(+)
            AND    ((cr.obs_ind IS NULL) OR (cr.obs_ind = 'N'))
            AND    ((l_court_room_name IS NULL)
                        OR (UPPER(cr.court_room_name)  LIKE l_court_room_name))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code)  LIKE l_court_site_code))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id           =    p_court_site_id_in))
            AND    ((p_crest_court_room_no_in IS NULL)
                        OR (cr.crest_court_room_no     =    p_crest_court_room_no_in))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)        LIKE l_short_name));
    END get_court_room;


    --
    -- Query the XHB_COURT_SITE table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE)
    AS
        l_court_site_code  CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_court_short_name CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_court_short_name_in);
        l_court_site_name  CONSTANT XHB_COURT_SITE.court_site_name%TYPE := convert_value(p_court_site_name_in);
    BEGIN
        --log_entry('get_court_site');
    
        OPEN p_results_out FOR
            SELECT cs.court_site_id AS "id",
                   cs.*
            FROM   XHB_COURT_SITE cs,
                   XHB_COURT c
            WHERE  cs.court_id = c.court_id
            AND    ((cs.obs_ind IS NULL) OR (cs.obs_ind = 'N'))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code) LIKE l_court_site_code))
            AND    ((p_court_id_in IS NULL)
                        OR (c.court_id                =    p_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(c.short_name)       LIKE l_court_short_name))
            AND    ((l_court_site_name IS NULL)
                        OR (UPPER(cs.court_site_name) LIKE l_court_site_name));
    END get_court_site;


    --
    -- Query the XHB_REF_ADVOCATE table, with some details from
    -- XHB_REF_LEGAL_REPRESENTATIVE, XHB_REF_CHAMBER and XHB_ADDRESS
    --
    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE)
    AS
        l_adv_type_ind CONSTANT XHB_REF_ADVOCATE.adv_type_ind%TYPE            := convert_value(p_adv_type_ind_in);
        l_initials     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE    := convert_value(p_initials_in);
        l_first_name   CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name  CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname      CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE     := convert_value(p_surname_in);
        l_firm_name    CONSTANT XHB_REF_CHAMBER.firm_name%TYPE                := convert_value(p_firm_name_in);
    BEGIN
        --log_entry('get_ref_advocate_complex');

        OPEN p_results_out FOR
            SELECT ra.ref_advocate_id AS "id",
                   rlr.title,
                   rlr.first_name,
                   rlr.initials,
                   rlr.middle_name,
                   rlr.surname,
                   rlr.court_id,
                   rlr.legal_rep_type,
                   ra.ref_chamber_id AS chamber_id,
                   ra.ref_legal_rep_id AS legal_rep_id,
                   ra.bar_no,
                   ra.is_global,
                   ra.crest_Advocate_Id,
                   ra.version,
                   ra.year_Of_Call,
                   ra.vat_No,
                   ra.crest_Chamber_Id,
                   ra.honours,
                   ra.adv_Type_Ind,
                   ra.obs_ind,
                   rc.firm_name,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode
            FROM   XHB_REF_ADVOCATE ra,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_CHAMBER rc,
                   XHB_ADDRESS a
            WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ra.ref_chamber_id = rc.ref_chamber_id
            AND    rc.address_id = a.address_id(+)
            AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id           =    p_court_id_in))
            AND    ((l_adv_type_ind IS NULL)
                        OR (UPPER(ra.adv_type_ind) LIKE l_adv_type_ind))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (ra.ref_legal_rep_id    =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)     LIKE l_surname))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rc.firm_name)    LIKE l_firm_name))
            ORDER BY rlr.surname;
    END get_ref_advocate_complex;


    --
    -- Query the XHB_REF_APP_RESULT table
    --
    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE)
    AS
        l_app_result_code CONSTANT XHB_REF_APP_RESULT.app_result_code%TYPE := convert_value(p_app_result_code_in);
        l_vary_sentence   CONSTANT XHB_REF_APP_RESULT.vary_sentence%TYPE   := convert_value(p_vary_sentence_in);
        l_lesser_off_ind  CONSTANT XHB_REF_APP_RESULT.lesser_off_ind%TYPE  := convert_value(p_lesser_off_ind_in);
    BEGIN
        --log_entry('get_ref_app_result');
    
        OPEN p_results_out FOR
            SELECT rap.ref_app_result_id AS "id",
                   rap.ref_app_result_id AS ref_App_Res_Id,
                   rap.app_result_code AS code,
                   rap.app_result_descr1 AS description1,
                   rap.app_result_descr2 AS description2,
                   rap.court_id,
                   rap.vary_sentence,
                   rap.version,
                   rap.ho_code,
                   rap.lesser_off_ind,
                   rap.obs_ind
            FROM   XHB_REF_APP_RESULT rap
            WHERE  ((rap.obs_ind IS NULL) OR (rap.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rap.court_id               =    p_court_id_in))
            AND    ((l_app_result_code IS NULL)
                        OR (UPPER(rap.app_result_code) LIKE l_app_result_code))
            AND    ((p_ho_code_in IS NULL)
                        OR (rap.ho_code                =    p_ho_code_in))
            AND    ((l_vary_sentence IS NULL)
                        OR (UPPER(rap.vary_sentence)   LIKE l_vary_sentence))
            AND    ((l_lesser_off_ind IS NULL)
                        OR (UPPER(rap.lesser_off_ind)  LIKE l_lesser_off_ind));
    END get_ref_app_result;


    --
    -- Query the XHB_REF_COURT table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE)
    AS
        l_circuit          CONSTANT XHB_COURT.circuit%TYPE              := convert_value(p_circuit_in);
        l_court_full_name  CONSTANT XHB_REF_COURT.court_full_name%TYPE  := convert_value(p_court_full_name_in);
        l_court_prefix     CONSTANT XHB_REF_COURT.name_prefix%TYPE      := convert_value(p_court_prefix_in);
        l_court_type       CONSTANT XHB_REF_COURT.court_type%TYPE       := convert_value(p_court_type_in);
        l_court_short_name CONSTANT XHB_REF_COURT.court_short_name%TYPE := convert_value(p_court_short_name_in);
        l_is_psd           CONSTANT XHB_REF_COURT.is_psd%TYPE           := convert_value(p_is_psd_in);
    BEGIN
        --log_entry('get_ref_court');

        OPEN p_results_out FOR
            SELECT rc.ref_court_id AS "id",
                   rc.*
            FROM   XHB_REF_COURT rc,
                   XHB_COURT c
            WHERE  rc.court_id                =    c.court_id
            AND    ((rc.obs_ind IS NULL) OR (rc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rc.court_id                =    p_court_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)           LIKE l_circuit))
            AND    ((l_court_full_name IS NULL)
                        OR (UPPER(rc.court_full_name)  LIKE l_court_full_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(rc.name_prefix)      LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(rc.court_type)       LIKE l_court_type))
            AND    ((p_crest_court_id_in IS NULL)
                        OR (c.crest_court_id           =    p_crest_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(rc.court_short_name) LIKE l_court_short_name))
            AND    ((l_is_psd IS NULL)
                        OR (UPPER(rc.is_psd)           LIKE l_is_psd));
    END get_ref_court;


    --
    -- Query the XHB_REF_COURT_REPORTER table, but also with search criteria
    -- from XHB_REF_COURT_REPORTER_FIRM
    --
    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE)
    AS
        l_firm_name   CONSTANT XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE := convert_value(p_firm_name_in);
        l_initials    CONSTANT XHB_REF_COURT_REPORTER.initials%TYPE       := convert_value(p_initials_in);
        l_first_name  CONSTANT XHB_REF_COURT_REPORTER.first_name%TYPE     := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_COURT_REPORTER.middle_name%TYPE    := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_COURT_REPORTER.surname%TYPE        := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_court_reporter');

        OPEN p_results_out FOR
            SELECT rcr.ref_court_reporter_id AS "id",
                   rcr.*
            FROM   XHB_REF_COURT_REPORTER rcr,
                   XHB_REF_COURT_REPORTER_FIRM rcrf
            WHERE  rcr.ref_court_reporter_firm_id = rcrf.ref_court_reporter_firm_id
            AND    ((rcr.obs_ind IS NULL) OR (rcr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rcr.court_id           =    p_court_id_in))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rcrf.firm_name)  LIKE l_firm_name))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rcr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rcr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rcr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rcr.surname)     LIKE l_surname));
    END get_ref_court_reporter;


    --
    -- Query the XHB_REF_HEARING_TYPE table
    --
    PROCEDURE get_ref_hearing_type(p_results_out          OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                           p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE)
    AS
        l_hearing_type_code CONSTANT XHB_REF_HEARING_TYPE.hearing_type_code%TYPE := convert_value(p_hearing_type_code_in);
    BEGIN
        --log_entry('get_ref_hearing_type');

        OPEN p_results_out FOR
            SELECT rht.ref_hearing_type_id AS "id",
                   rht.*
            FROM   XHB_REF_HEARING_TYPE rht
            WHERE  ((rht.obs_ind IS NULL) OR (rht.obs_ind = 'N'))
            AND    ((l_hearing_type_code IS NULL)
                        OR (UPPER(rht.hearing_type_code) LIKE l_hearing_type_code))
	    AND    ((p_hearing_type_courtid_in IS NULL)
                        OR (rht.court_id             =    p_hearing_type_courtid_in ));
    END get_ref_hearing_type;


    --
    -- Query the XHB_REF_JUDGE table
    --
    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE)
    AS
        l_first_name  CONSTANT XHB_REF_JUDGE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_JUDGE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_JUDGE.surname%TYPE     := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_judge');

        OPEN p_results_out FOR
            SELECT rj.ref_judge_id AS "id",
                   rj.*
            FROM   XHB_REF_JUDGE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind  = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id =  p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rj.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rj.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rj.surname)     LIKE l_surname));
    END get_ref_judge;


    --
    -- Query the XHB_REF_JUSTICE table
    --
    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE)
    AS
        l_justice_name CONSTANT XHB_REF_JUSTICE.justice_name%TYPE := convert_value(p_justice_name_in);
    BEGIN
        --log_entry('get_ref_justice');

        OPEN p_results_out FOR
            SELECT rj.ref_justice_id AS "id",
                   rj.court_id AS court_i_d,
                   rj.*
            FROM   XHB_REF_JUSTICE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id = p_court_id_in))
            AND    ((l_justice_name IS NULL)
                        OR (UPPER(rj.justice_name) LIKE l_justice_name));
    END get_ref_justice;


    --
    -- Query the XHB_REF_LEGAL_REPRESENTATIVE table
    --
    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE)
    AS
        l_first_name     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE     := convert_value(p_first_name_in);
        l_surname        CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE        := convert_value(p_surname_in);
        l_legal_rep_type CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE := convert_value(p_legal_rep_type_in);
    BEGIN
        --log_entry('get_ref_legal_representative');

        OPEN p_results_out FOR
            SELECT rlr.ref_legal_rep_id AS "id",
                   rlr.*
            FROM   XHB_REF_LEGAL_REPRESENTATIVE rlr
            WHERE  ((rlr.obs_ind IS NULL) OR (rlr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id              =    p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)     LIKE l_first_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)        LIKE l_surname))
            AND    ((l_legal_rep_type IS NULL)
                        OR (UPPER(rlr.legal_rep_type) LIKE l_legal_rep_type));
    END get_ref_legal_representative;


    --
    -- Query the XHB_REF_OFFENCE table
    --
    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE,
	                      p_obs_ind_in      IN  XHB_REF_OFFENCE.obs_ind%TYPE)
    AS
        l_act_section  CONSTANT XHB_REF_OFFENCE.act_section%TYPE  := convert_value(p_act_section_in);
        l_offence_desc CONSTANT XHB_REF_OFFENCE.offence_desc%TYPE := convert_value(p_offence_desc_in);
        l_statute      CONSTANT XHB_REF_OFFENCE.statute%TYPE      := convert_value(p_statute_in);
        l_offence_code CONSTANT XHB_REF_OFFENCE.offence_code%TYPE := convert_value(p_offence_code_in);
    BEGIN
        --log_entry('get_ref_offence');

        OPEN p_results_out FOR
            SELECT ro.ref_offence_id AS "id",
                   ro.*,
                   -- Can't find what this IS
                   'OFFENCE TYPE' AS offence_type
            FROM   XHB_REF_OFFENCE ro
            WHERE  ((p_obs_ind_in = 'Y') OR ((ro.obs_ind IS NULL) OR (ro.obs_ind = 'N')))
            AND    ((l_act_section IS NULL)
                        OR (UPPER(ro.act_section)  LIKE l_act_section))
            AND    ((p_court_id_in IS NULL)
                        OR (ro.court_id            =    p_court_id_in))
            AND    ((l_offence_desc IS NULL)
                        OR (UPPER(ro.offence_desc) LIKE l_offence_desc))
            AND    ((l_statute IS NULL)
                        OR (UPPER(ro.statute)      LIKE l_statute))
            AND    ((l_offence_code IS NULL)
                        OR (UPPER(ro.offence_code) LIKE l_offence_code))
            ORDER BY ro.offence_code;
    END get_ref_offence;


    --
    -- Query the XHB_REF_SOLICITOR_FIRM table
    --
    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE)
    AS
        l_solicitor_firm_name CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_ref_solicitor_firm_complex');

        OPEN p_results_out FOR
            SELECT rsf.ref_solicitor_firm_id AS "id",
                   rsf.*,
                   a.address_id,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode,
                   a.country
            FROM   XHB_REF_SOLICITOR_FIRM rsf,
                   XHB_ADDRESS a
            WHERE  rsf.address_id = a.address_id
            AND    ((rsf.obs_ind IS NULL) OR (rsf.obs_ind = 'N'))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name))
            AND    ((p_crest_sof_id_in IS NULL)
                        OR (rsf.crest_sof_id               =    p_crest_sof_id_in))
            AND    ((p_court_id_in IS NULL)
                        OR (rsf.court_id                   =    p_court_id_in));
    END get_ref_solicitor_firm_complex;


    --
    -- Query the XHB_REF_SYSTEM_CODE table
    --
    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE)
    AS
        l_code_type  CONSTANT XHB_REF_SYSTEM_CODE.code_type%TYPE  := convert_value(p_code_type_in);
        l_de_code    CONSTANT XHB_REF_SYSTEM_CODE.de_code%TYPE    := convert_value(p_de_code_in);
        l_code       CONSTANT XHB_REF_SYSTEM_CODE.code%TYPE       := convert_value(p_code_in);
        l_code_title CONSTANT XHB_REF_SYSTEM_CODE.code_title%TYPE := convert_value(p_code_title_in);
    BEGIN
        --log_entry('get_ref_system_code');

        OPEN p_results_out FOR
            SELECT rsc.ref_system_code_id AS "id",
                   rsc.de_code AS DECODE,
                   rsc.code,
                   rsc.code_Type,
                   rsc.court_Id,
                   rsc.version,
                   rsc.code_Title,
                   rsc.ref_Code_Order,
                   rsc.obs_Ind
            FROM   XHB_REF_SYSTEM_CODE rsc
            WHERE  ((rsc.obs_ind IS NULL) OR (rsc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rsc.court_id          =    p_court_id_in))
            AND    ((l_code_type IS NULL)
                        OR (UPPER(rsc.code_type)  LIKE l_code_type))
            AND    ((l_de_code IS NULL)
                        OR (UPPER(rsc.de_code)    LIKE l_de_code))
            AND    ((l_code IS NULL)
                        OR (UPPER(rsc.code)       LIKE l_code))
            AND    ((l_code_title IS NULL)
                        OR (UPPER(rsc.code_title) LIKE l_code_title));
    END get_ref_system_code;


    --
    -- Query the XHB_REF_SOLICITOR table, but also with search criteria
    -- from XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_SOLICITOR_FIRM
    --
    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE)
    AS
        l_initials             CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE      := convert_value(p_initials_in);
        l_first_name           CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE    := convert_value(p_first_name_in);
        l_middle_name          CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE   := convert_value(p_middle_name_in);
        l_surname              CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE       := convert_value(p_surname_in);
        l_crest_solicitor_name CONSTANT XHB_REF_SOLICITOR.crest_solicitor_name%TYPE     := convert_value(p_crest_solicitor_name_in);
        l_solicitor_firm_name  CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_solicitor');

        OPEN p_results_out FOR
            SELECT rs.solicitor_id AS "id",
                   rs.*,
                   rlr.*,
                   rsf.ref_solicitor_firm_id AS "firm_id",
                   rs.ref_legal_rep_id AS "legal_rep_id",
                   rs.is_in_crest AS "in_crest"
            FROM   XHB_REF_SOLICITOR rs,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_SOLICITOR_FIRM rsf
            WHERE  rs.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ((p_court_id_in IS NULL) OR (rlr.court_id = p_court_id_in))
            AND    rs.ref_solicitor_firm_id = rsf.ref_solicitor_firm_id
            AND    ((rs.obs_ind IS NULL) OR (rs.obs_ind = 'N'))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (rs.ref_legal_rep_id            =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)            LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)          LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name)         LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)             LIKE l_surname))
            AND    ((l_crest_solicitor_name IS NULL)
                        OR (UPPER(rs.crest_solicitor_name) LIKE l_crest_solicitor_name))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name));
    END get_solicitor;
END xhb_search_pkg;
/
show errors
CREATE OR REPLACE PACKAGE xhb_terminal_pkg AS
    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE) RETURN SYS_REFCURSOR;
    FUNCTION get_courts RETURN SYS_REFCURSOR;
    FUNCTION get_court_sites RETURN SYS_REFCURSOR;
    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE) RETURN SYS_REFCURSOR;

    PROCEDURE maintain_terminal(p_terminal_name	IN xhb_terminal.terminal_name%TYPE,	p_location	IN xhb_terminal.location%TYPE);

END xhb_terminal_pkg;
/
show errors
   
CREATE OR REPLACE PACKAGE BODY xhb_terminal_pkg AS

    FUNCTION is_identical(p_var1_in IN VARCHAR2, p_var2_in IN VARCHAR2) RETURN boolean IS
    BEGIN
         RETURN ( (p_var1_in IS NULL AND p_var2_in IS NULL) OR (p_var1_in = p_var2_in) );
    END is_identical;
    
    FUNCTION is_identical(p_var1_in IN NUMBER, p_var2_in IN NUMBER) RETURN boolean IS
    BEGIN
         RETURN ( (p_var1_in IS NULL AND p_var2_in IS NULL) OR (p_var1_in = p_var2_in) );
    END is_identical;

	PROCEDURE update_terminal(l_terminal_name     IN XHB_TERMINAL.TERMINAL_NAME%TYPE,
			  				  l_location_string   IN XHB_TERMINAL.LOCATION%TYPE,
  			  				  l_court_id          IN XHB_TERMINAL.COURT_ID%TYPE,
			  				  l_court_site_id     IN XHB_TERMINAL.COURT_SITE_ID%TYPE,
			  				  l_court_room_id     IN XHB_TERMINAL.COURT_ROOM_ID%TYPE,
			  				  l_courtroom_or_site IN XHB_TERMINAL.COURTROOM_OR_SITE%TYPE,
			  				  l_roaming           IN XHB_TERMINAL.ROAMING%TYPE) 
	IS
		l_old_terminal_entry   xhb_terminal%ROWTYPE;
	BEGIN
    	 SELECT *
		 INTO   l_old_terminal_entry 
    	 FROM   XHB_TERMINAL
    	 WHERE  terminal_name = l_terminal_name;

    	 IF (NOT (    is_identical(l_old_terminal_entry.location,          l_location_string)
                  AND is_identical(l_old_terminal_entry.court_room_id,     l_court_room_id)
            	  AND is_identical(l_old_terminal_entry.court_site_id,     l_court_site_id)
            	  AND is_identical(l_old_terminal_entry.courtroom_or_site, l_courtroom_or_site) 
            	  AND is_identical(l_old_terminal_entry.court_id,          l_court_id) 
            	  AND is_identical(l_old_terminal_entry.roaming,           l_roaming) 
            	 )
            )
        THEN 
        	 -- Update terminal with new location details
        	 UPDATE  xhb_terminal
        	 SET     location = l_location_string,
                     court_room_id = l_court_room_id,
                	 courtroom_or_site = l_courtroom_or_site,
                	 court_site_id = l_court_site_id,
                	 court_id = l_court_id,
                	 roaming = l_roaming
             WHERE 	 terminal_name = l_terminal_name;
        END IF;

		EXCEPTION
    		WHEN NO_DATA_FOUND THEN
        	-- doesn't exist, so insert it...
        	   INSERT INTO xhb_terminal
               		  (location,
                	  terminal_ip,
                	  terminal_name,
                	  court_room_id, 
                	  courtroom_or_site,
                	  court_site_id,
                	  court_id,
                	  roaming)
               VALUES (l_location_string,
               		  'N/A',
               		  l_terminal_name,
               		  l_court_room_id,
               		  l_courtroom_or_site,
               		  l_court_site_id,
               		  l_court_id,
               		  l_roaming);
	END;
	

    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_SITE xcs
            WHERE  xt.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            AND    xt.COURT_ROOM_ID is NULL
            UNION
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   xcr.COURT_ROOM_NAME AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs
            WHERE  xt.COURT_ROOM_ID = xcr.COURT_ROOM_ID
            AND    xcr.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            ORDER BY TERMINAL_NAME;

        RETURN v_return_cursor;
    END get_terminals;


    FUNCTION get_courts RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT COURT_NAME,
                   COURT_ID,
                   CREST_COURT_ID
            FROM   XHB_COURT
            ORDER BY COURT_NAME;

        RETURN v_return_cursor;
    END get_courts;
    

    FUNCTION get_court_sites RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
        	 SELECT XHB_COURT.COURT_NAME,
                    XHB_COURT.COURT_ID,
                    XHB_COURT.CREST_COURT_ID,
                    XHB_COURT_SITE.COURT_SITE_NAME,
                    XHB_COURT_SITE.COURT_SITE_ID    
             FROM   XHB_COURT,
                    XHB_COURT_SITE
             WHERE  XHB_COURT.COURT_ID = XHB_COURT_SITE.COURT_ID 
             ORDER BY XHB_COURT.COURT_NAME,
                      XHB_COURT_SITE.COURT_SITE_NAME;

        RETURN v_return_cursor;
    END get_court_sites;
    

    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT TERMINAL_NAME,
                   TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   NULL AS COURT_SITE_NAME
            FROM   XHB_TERMINAL
            WHERE  TERMINAL_ID = p_terminal_id_in;

        RETURN v_return_cursor;
    END get_terminal_by_primary_key;



	-- This procedure will create an entry in the XHB_TERMINAL table
	-- If any part of the court short name, the court site code, or the court room number
	-- are incorrect, then a custom error -80000 will be thrown.
	-- There should only be one insert/update at the end as the Java code will
	-- continue to process updates and WILL commit at the end.
	PROCEDURE maintain_terminal(p_terminal_name	IN xhb_terminal.terminal_name%TYPE,
			  					p_location		IN xhb_terminal.location%TYPE) 
	IS
        l_terminal_name     xhb_terminal.terminal_name%TYPE     := LOWER(p_terminal_name);
        l_location          xhb_terminal.location%TYPE          := UPPER(p_location);
		l_court_short_name	xhb_court.short_name%TYPE;
		l_court_site_code	xhb_court_site.court_site_code%TYPE;
		l_room_or_site		xhb_terminal.location%TYPE;
		l_courtroom_or_site	xhb_terminal.courtroom_or_site%TYPE;
		l_court_id			xhb_court.court_id%TYPE;
		l_court_site_id		xhb_court_site.court_site_id%TYPE;
		l_court_room_id		xhb_court_room.court_room_id%TYPE;
		l_location_string	xhb_terminal.location%TYPE          := '/';  -- always start with a /
        l_roaming           xhb_terminal.roaming%type := 'N'; -- default to not roaming

        l_2nd_slash NUMBER := INSTR(l_location, '/', 1, 2);
        l_3rd_slash NUMBER := INSTR(l_location, '/', 1, 3);
	BEGIN
        -- Retrieve Court element
		IF (l_2nd_slash = 0) 
		THEN
			l_court_short_name := SUBSTR(l_location, 2);
		ELSE
			l_court_short_name := SUBSTR(l_location, 2, l_2nd_slash - 2);
		END IF;

		IF (l_court_short_name = 'ROAM') THEN
			l_roaming := 'Y';
			l_court_id := NULL;
			l_court_site_id := NULL;
			l_court_room_id := NULL;
			l_courtroom_or_site := 'A';
			l_location_string := 'roam';
		ELSE
			-- Get Court House ID and start building location string
			SELECT	court_id,
					l_location_string || REPLACE(LOWER(display_name), ' ', '_')
			INTO	l_court_id,
					l_location_string
			FROM	xhb_court
			WHERE	short_name = l_court_short_name;

			-- Retrieve Court Site Element
			-- Check if there is no third slash, so that layout is /ISLEW/I
			IF (l_3rd_slash = 0) THEN
				l_court_site_code := SUBSTR(l_location, l_2nd_slash + 1);
			ELSE
				l_court_site_code := SUBSTR(l_location, 
				                            l_2nd_slash + 1, 
                                            l_3rd_slash - l_2nd_slash - 1);
			END IF;

			IF (l_court_site_code = 'ROAM') THEN
				l_roaming := 'Y';
				l_court_site_id := NULL;
				l_court_room_id := NULL;
				l_courtroom_or_site := 'C';
				l_location_string := l_location_string || '/roam';
			ELSE
				-- Get Court Site ID and and continue building location string
				SELECT	court_site_id,
						l_location_string || '/' || REPLACE(LOWER(display_name), ' ', '_')
				INTO	l_court_site_id,
						l_location_string
				FROM	xhb_court_site
				WHERE	court_id = l_court_id
				AND		court_site_code = l_court_site_code;

				-- Retrieve court room or location element
				-- Check if there is a thrid slash before getting court room
				IF (l_3rd_slash = 0) 
				THEN
					l_room_or_site := NULL;
				ELSE
					l_room_or_site := LOWER(REPLACE(SUBSTR(l_location, l_3rd_slash + 1), ' ', '_'));
				END IF;
				
				IF (l_room_or_site IS NOT NULL 
				    AND UPPER(l_room_or_site) = 'ROAM')
				THEN
					l_roaming := 'Y';
					l_court_room_id := NULL;
					l_courtroom_or_site := 'S';
					l_location_string := l_location_string || '/roam';
				ELSE
					-- Determine whether this is a Court Room or Site
					IF ( (LENGTH(l_room_or_site) > 0)
					   AND (RTRIM(l_room_or_site, '0123456789') IS NULL) )
					THEN

						-- Get the Court Room ID, set Court Room or Site variable to 'R', and complete location string
						SELECT	court_room_id,
								'R',
								l_location_string || '/' || REPLACE(LOWER(display_name), ' ', '_')
						INTO	l_court_room_id,
								l_courtroom_or_site,
								l_location_string
						FROM	xhb_court_room
						WHERE	court_site_id = l_court_site_id
						AND		crest_court_room_no = l_room_or_site;

					ELSE
						-- Set the Court Room ID to NULL, set Court Room or Site variable to 'S', and complete location string
						l_court_room_id     := NULL;
						l_courtroom_or_site := 'S';
						l_location_string   := l_location_string || '/' || l_room_or_site;
					END IF;

				END IF;
				
			END IF;
			
		END IF;

		update_terminal(l_terminal_name,
					    l_location_string,
				        l_court_id,
				        l_court_site_id,
				        l_court_room_id,
				        l_courtroom_or_site,
				        l_roaming);

		EXCEPTION
    		WHEN NO_DATA_FOUND THEN
				RAISE_APPLICATION_ERROR(-20001, SQLCODE || ':' || SQLERRM); 

	END maintain_terminal;

END xhb_terminal_pkg;
/
show errors


CREATE OR REPLACE PACKAGE xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE);

	   PROCEDURE get_by_case_id_date(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

           PROCEDURE get_by_case_id_catdesc(results_out OUT SYS_REFCURSOR,
                                        case_id_in  IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        cat_desc_in IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

	   PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                cat_desc_in       IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

	   PROCEDURE get_by_case_eventdesc_date(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_desc_id_in  IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_eventtype_date_gt(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_id_eventtype(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE);

	   PROCEDURE get_by_case_id_date_pd(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

       PROCEDURE get_court_sched_hearing_value(results_out  OUT SYS_REFCURSOR,
                                               p_case_id_in IN  XHB_HEARING.case_id%TYPE);
END xhb_court_log_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out    OUT SYS_REFCURSOR,
                                    case_id_in IN  XHB_COURT_LOG_ENTRY.case_id%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in	
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id;


       PROCEDURE get_by_case_id_date(results_out    OUT SYS_REFCURSOR,
                                     case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                     start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                     end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date;


       PROCEDURE get_by_case_id_catdesc(results_out OUT SYS_REFCURSOR,
                                        case_id_in  IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        cat_desc_in IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE)
       IS
       BEGIN
            OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC, XHB_COURT_LOG_CATEGORY CATEGORY
                WHERE  CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
                AND    CATEGORY_DESC.category_description = cat_desc_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in  
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;          
       END get_by_case_id_catdesc;


       PROCEDURE get_by_case_id_date_catdesc(results_out   OUT SYS_REFCURSOR,
                                             case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                             start_date_in IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             end_date_in   IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             cat_desc_in   IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC, XHB_COURT_LOG_CATEGORY CATEGORY
                WHERE  CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
                AND    CATEGORY_DESC.category_description = cat_desc_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date_catdesc;


       PROCEDURE get_by_case_eventdesc_date(results_out      OUT SYS_REFCURSOR,
                                            case_id_in       IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                            event_desc_id_in IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                            start_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE  COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_ENTRY.EVENT_DESC_ID = event_desc_id_in
                AND    COURT_LOG_ENTRY.DATE_TIME >= start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventdesc_date;


       PROCEDURE get_by_case_eventtype_date_gt(results_out   OUT SYS_REFCURSOR,
                                               case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                               event_type_in IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                               start_date_in IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.* 
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                AND    COURT_LOG_ENTRY.DATE_TIME > start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventtype_date_gt;


       PROCEDURE get_by_case_id_eventtype(results_out   OUT SYS_REFCURSOR,
                                          case_id_in    IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                          event_type_in IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.*
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_eventtype;


       PROCEDURE get_by_case_id_date_pd(results_out       OUT SYS_REFCURSOR,
                                        case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                        start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE)
       IS
       BEGIN
	        OPEN results_out FOR
                SELECT COURT_LOG_ENTRY.*
                FROM   XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE  COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND    COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND    COURT_LOG_EVENT_DESC.PUBLIC_DISPLAY = public_display_in
                AND    COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME DESC, COURT_LOG_ENTRY.ENTRY_ID DESC;		   
       END get_by_case_id_date_pd;


       PROCEDURE get_court_sched_hearing_value(results_out  OUT SYS_REFCURSOR,
                                               p_case_id_in IN  XHB_HEARING.case_id%TYPE)
       IS
       BEGIN
            OPEN results_out FOR
                SELECT xsh.scheduled_hearing_id,
                       xrht.hearing_type_code,
                       xhl.start_date
                FROM   xhb_hearing xh,
                       xhb_scheduled_hearing xsh,
                       xhb_ref_hearing_type xrht,
                       xhb_sitting xs,
                       xhb_hearing_list xhl
                WHERE  xh.hearing_id = xsh.hearing_id
                AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
                AND    xsh.sitting_id = xs.sitting_id
                AND    xs.list_id = xhl.list_id
                AND    xh.case_id = p_case_id_in
                ORDER BY xhl.start_date, xsh.scheduled_hearing_id;
       END get_court_sched_hearing_value;
END xhb_court_log_pkg;
/
show errors
-------------------------------------------------------------------------------
-- Possible future enhancements:
-------------------------------------------------------------------------------
--   Change the types for the in parameters to be the column types instead;
--   Replace counsel_type type declaration to use SYS_REFCURSOR;
--   Investigate the views to see if they can be improved;
--   For search_counsel & search_defendants see if we can ignore the firstname
--       and surname fields on the database if they are null (similar to how
--       the checks for null on the past in parameters are done);
--   See if the passed in values (for VARCHAR2's) is 0 length or just spaces,
--       if so, would we want to convert to null, and therefore ignore it?;
--   Change name of package to be consistent with Oracle coding standards
--       e.g. counsel_facilities_pkg
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY counselfacilities AS
    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER) IS
    BEGIN
        -- Vastly improved this query by removing the two sub-queries with the
        -- minus operations
        OPEN p_counsel_cursor_out FOR
			 SELECT * FROM (
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END get_counsel_sign_in;


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2) IS
    BEGIN
    	OPEN p_counsel_cursor_out FOR
            SELECT * FROM
            (
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SH_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SHDID_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END search_counsel;


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2) IS
    BEGIN
        OPEN p_counsel_cursor_out FOR
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')));
    END search_defendants;


    --
    -- NEW FUNCTION FOR CR51...
    --
    FUNCTION get_court_room_list(p_court_id_in      IN NUMBER,
                                 p_start_date_in    IN DATE,
                                 p_court_room_id_in IN NUMBER) RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        -- Really do not like this query, will look into further for the 7 release...
        -- Do not like, however, it is still considerably more efficient than the previous
        -- version (see get_counsel_sign_in), and could be improved and integrated further
        -- with other areas with the use of more common views...

        OPEN v_return_cursor FOR
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- Court room details...
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.court_room_id,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    xs.ref_judge_id = xrj.ref_judge_id(+)
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    xshd.sched_hear_def_id = xslr.sched_hear_def_id(+)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
UNION
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- Court room details...
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.court_room_id,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    xs.ref_judge_id = xrj.ref_judge_id(+)
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    (xsh.scheduled_hearing_id = xslr.scheduled_hearing_id(+) AND xslr.sched_hear_def_id IS NULL)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
            ORDER BY court_site_code, floating, crest_court_room_no,
                   sitting_sequence_no, not_before_time, sequence_no;

        RETURN v_return_cursor;
    END get_court_room_list;
END counselfacilities;
/
show errors
--
--          Trigger Load Balancing Installation Script
--
-- DESCRIPTION:   Installs database objects required for Event Server database 
--                triggering on an Oracle 9 or above database.  The following
--                database objects are created and/or altered:
--
--                1. Tables, which track Event Server alive status.
--                2. Stored procedures, which interface to the tables.
--                3. Packages which implement the load balancing functionality.
--                4. Sequences that are used for load balancing.
--
-- REQUIREMENTS:  1. Oracle 9i Server
--         
--                2.
--                This script MUST be run by a user who can grant users of the
--                Event Server the following privileges:
--
--                  a. EXECUTE privilege on DBMS_SQL
--                  b. EXECUTE privilege on DBMS_TRANSACTION
--                  c. CREATE PUBLIC SYNONYM privilege
--                  d. DROP   PUBLIC SYNONYM privilege
--
--                The PUBLIC SYNONYM privileges are only needed at 
--                installation time.
--
--                The other privileges are necessary so that all Mercator DB 
--                triggering users will operate within a secure and pre-defined
--                Oracle environment.
--
-- INSTALL STEPS:
--                1. Before loading this script into SQL*Plus, issue 
--                   'SET SERVEROUTPUT ON' to see status messages.
--
--                2. If re-installing this script using a different user name,
--                   then follow the RE-INSTALL STEPS below.  Otherwise, just
--                   ignore this step.
--
--                3. Load and run the file from SQL*PLUS via the following
--                   command: 
--                   @TrigLbal.sql
--
-- RE-INSTALL STEPS:
--                Whenever re-installing under a different user name, all 
--                database objects associated with the old schema must be
--                properly deleted.  This can be done as follows:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'RE-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--
--                   c. Undo changes made in step a.
--
--                   d. Follow Step 3 above.
--
-- UN-INSTALL STEPS:
--                To remove this code and revert back to the original trigger
--                installation, do the following:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'UN-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--

DECLARE 

   nRc         INTEGER;
   nWarningCnt INTEGER;
   szMessage   VARCHAR2(500);
   szSchema    VARCHAR2(50);
   szSpfx      VARCHAR2(51);     -- Schema prefix: Schema + '.'

   PROCEDURE msp_exec_cmd (szDdlCmd  IN VARCHAR2, szMsg IN VARCHAR2 DEFAULT NULL) IS
      X_SEQ_NOTEXISTS     EXCEPTION;
      X_SYN_NOTEXISTS     EXCEPTION;
      X_PRO_NOTEXISTS     EXCEPTION;
      X_TBL_COLEXISTS     EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_SEQ_NOTEXISTS, -2289);
      PRAGMA EXCEPTION_INIT   (X_SYN_NOTEXISTS, -1432);
      PRAGMA EXCEPTION_INIT   (X_PRO_NOTEXISTS, -4043);
      PRAGMA EXCEPTION_INIT   (X_TBL_COLEXISTS, -1430);
      cid INTEGER;
   BEGIN

      cid := dbms_sql.open_cursor;

      dbms_sql.parse(cid, szDdlCmd, dbms_sql.native);

      IF szMsg IS NOT NULL THEN
         DBMS_OUTPUT.PUT_LINE(szMsg);
      END IF;

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_SEQ_NOTEXISTS OR X_SYN_NOTEXISTS OR X_PRO_NOTEXISTS OR X_TBL_COLEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

BEGIN

   dbms_output.enable(8000);

   szSchema := USER;
   szSchema := UPPER(RTRIM(LTRIM(szSchema)));
   dbms_output.put_line('Mercator objects will be located in the following schema: ' || szSchema );
   szSpfx := szSchema || '.';

   --
   -- Drop any objects created from a previous execution of this script.
   --
   msp_exec_cmd   ('DROP    SEQUENCE       ' || szSpfx || 'mseq_load_bal',
                   'Dropped SEQUENCE       ' || szSpfx || 'mseq_load_bal');
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_heartbeat',
                   'Dropped PUBLIC SYNONYM msp_heartbeat');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_heartbeat1',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_heartbeat1');
   msp_exec_cmd   ('DROP    PACKAGE        ' || szSpfx || 'mpkg_load_bal',
                   'Dropped PACKAGE        ' || szSpfx || 'mpkg_load_bal');


   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If un-installing or re-installing to a DIFFERENT schema, then comment
   -- out all the following CREATE commands by simply surrounding them with
   -- the comment characters.  See further below for the closing comment.
   -- /*

   --
   -- Privilege validation.
   --
   nWarningCnt := 0;

   -- Make sure the USER has EXECUTE privilege on the DBMS_SQL package.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_SQL'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_SQL package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_SQL package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_SQL package.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_SQL package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_SQL TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_TRANSACTION'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_TRANSACTION package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_TRANSACTION package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_TRANSACTION package.');
      END IF;

      EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_TRANSACTION package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_TRANSACTION TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   --
   -- Sequence Definitions
   --
   msp_exec_cmd   ('CREATE  SEQUENCE ' || szSpfx || 'mseq_load_bal ' ||
                            'INCREMENT BY 1 START WITH 1 MAXVALUE 1000000 MINVALUE 1 CYCLE CACHE 100 NOORDER',
                   'Created SEQUENCE       ' || szSpfx || 'mseq_load_bal');

   --
   -- Table Definitions.
   --
   msp_exec_cmd   ('ALTER TABLE ' || szSpfx || 'mtbl_trigger_server ' ||
                         'ADD (alive_ts TIMESTAMP WITH TIME ZONE, ' ||
                              'alive_interval INTERVAL DAY TO SECOND DEFAULT ''0 0:1:0'' NOT NULL)',
                   'Altered TABLE          ' || szSpfx || 'mtbl_trigger_server');

   --
   -- Stored Procedure
   --
   msp_exec_cmd   ('CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_heartbeat1 (' ||
                      'es_id         IN    mtbl_trigger_server.ID%TYPE, ' ||
                	    'es_name       IN    mtbl_trigger_server.Name%TYPE DEFAULT NULL ) AS ' ||
                   'BEGIN ' ||
                      'IF es_id IS NULL THEN ' ||
                          'UPDATE mtbl_trigger_server ' ||
                                 'SET   alive_ts = SYSTIMESTAMP ' ||
                                 'WHERE  UPPER(name) = UPPER(es_name); ' ||
                      'ELSE ' ||
                          'UPDATE mtbl_trigger_server ' ||
                                 'SET   alive_ts = SYSTIMESTAMP ' ||
                                 'WHERE  id = es_id; ' ||
                      'END IF; ' ||
                   'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_heartbeat1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_heartbeat1 TO PUBLIC');
   msp_exec_cmd   ('CREATE  PUBLIC SYNONYM msp_heartbeat FOR ' || szSpfx || 'msp_heartbeat1', 
                   'Created PUBLIC SYNONYM msp_heartbeat');

   --
   -- Package definition
   --
   msp_exec_cmd  (
       'CREATE OR REPLACE PACKAGE ' || szSpfx || 'mpkg_load_bal AS ' ||
          'PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN); ' ||
       'END mpkg_load_bal;',
                   'Created PACKAGE        ' || szSpfx || 'mpkg_load_bal');

   msp_exec_cmd  (
      'CREATE OR REPLACE PACKAGE BODY ' || szSpfx || 'mpkg_load_bal AS ' ||
         'lb_limit CONSTANT PLS_INTEGER := 256; ' ||
         'TYPE   VA_ES_LIST IS VARRAY(256) OF mtbl_trigger_server.ID%TYPE; ' ||
         'tx_id VARCHAR2(256);' ||
         'es_id mtbl_trigger_server.ID%TYPE; ' ||
      'PROCEDURE get_es_id (sid OUT mtbl_trigger_server.ID%TYPE, fNewTx OUT BOOLEAN) IS ' ||
         'nID    mtbl_trigger_server.ID%TYPE; ' ||
         'nSeq   PLS_INTEGER; ' ||
         'nMod   PLS_INTEGER; ' ||
         'cur_tx_id tx_id%TYPE; ' ||
         'CURSOR c_avail_es IS  ' ||
                'SELECT ID  ' ||
                'FROM   mtbl_trigger_server ' ||
                'WHERE  (NVL(alive_ts, SYSTIMESTAMP) + alive_interval >= SYSTIMESTAMP)  AND ' ||
                       '((startupdate > shutdowndate) OR (shutdowndate IS NULL) )' ||
                'ORDER BY ID; ' ||
         'CURSOR c_reg_es IS  ' ||
                'SELECT ID  ' ||
                'FROM   mtbl_trigger_server ' ||
                'WHERE  startupdate > shutdowndate OR shutdowndate IS NULL ' ||
                'ORDER BY ID; ' ||
         'es_list VA_ES_LIST; ' ||
      'BEGIN  ' ||
         'cur_tx_id := dbms_transaction.local_transaction_id(); ' ||
         'IF (tx_id != cur_tx_id)                          OR ' ||
            '(tx_id IS NULL     AND cur_tx_id IS NOT NULL) OR  ' ||
            '(tx_id IS NOT NULL AND cur_tx_id IS NULL) ' ||
         'THEN ' ||
            'tx_id := cur_tx_id; ' ||
            'SELECT mseq_load_bal.NEXTVAL INTO nSeq FROM DUAL; ' ||
            'OPEN c_avail_es; ' ||
            'FETCH c_avail_es BULK COLLECT INTO es_list LIMIT lb_limit; ' ||
            'CLOSE c_avail_es; ' ||
            'IF es_list.COUNT = 0 THEN ' ||
               'OPEN c_reg_es;                ' ||
               'FETCH c_reg_es BULK COLLECT INTO es_list LIMIT lb_limit; ' ||
               'CLOSE c_reg_es; ' ||
            'END IF; ' ||
            'IF es_list.COUNT = 0 THEN ' ||
               'sid := 0; ' ||
            'ELSE ' ||
               'nMod := (nSeq MOD es_list.COUNT) + 1; ' ||
               'sid := es_list(nMod); ' ||
            'END IF; ' ||
            'es_id  := sid; ' ||
            'fNewTx := TRUE; ' ||
         'ELSE ' ||
            'sid    := es_id; ' ||
            'fNewTx := FALSE; ' ||
         'END IF; ' ||
      'END; ' ||
      'BEGIN ' ||
         'tx_id := NULL; ' ||
         'es_id := 0; ' ||
      'END mpkg_load_bal; ',
                   'Created PACKAGE BODY   ' || szSpfx || 'mpkg_load_bal');

   --
   -- Event signaling routines.
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE  ' || szSpfx || 'msp_record_events1( '  ||
         'szTrigName Trigger_Registry.TriggerName%TYPE, '                  ||
         'cActionType CHAR, '                ||
         'nRowId ROWID ) AS '                ||
         'szSignalEvent VARCHAR2(50); '      ||
         'nID    NUMBER; '                   ||
         'nEsId  Trigger_Registry.ServerID%TYPE; '     ||
         'fNewTx BOOLEAN; '                            ||
         'CURSOR watch_events_cursor IS '    ||
                'SELECT ID '                 ||
                'FROM   Trigger_Registry '   ||
                'WHERE  TriggerName = szTrigName ' ||
                  'AND  ProcessDate IS NULL '      ||
                  'AND  ActionType = cActionType ' ||
                  'AND  ServerID   = nEsId '       ||
                'ORDER BY ID; '                    ||
       'BEGIN '                                    ||
         'mpkg_load_bal.get_es_id (nEsId, fNewTx); ' ||
         'IF cActionType = ''R'' AND fNewTx THEN ' ||
            'LOCK table Trigger_Registry IN ROW SHARE MODE; ' ||
         'END IF; ' ||
         'OPEN watch_events_cursor; '              ||
         'LOOP '                                   ||
             'FETCH watch_events_cursor INTO nID; '||
             'EXIT WHEN watch_events_cursor%NOTFOUND; '     ||
             'BEGIN '                                       ||
               'INSERT INTO Trigger_Events '                            ||
                           '(id,st_rowid) '                             ||
                      'VALUES '                                         ||
                           '(nID,nRowId); '                             ||
               'IF fNewTx THEN '                                        ||
                  'szSignalEvent := ''MERCTRIG_'' || nID; '                ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '      ||
               'END IF; '                                               ||
             'EXCEPTION '                                               ||
               'WHEN DUP_VAL_ON_INDEX THEN '                            ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '   ||
             'END; '                                                    ||
         'END LOOP; '                                                   ||
         'CLOSE watch_events_cursor; '                                  ||
       'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_record_events1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_record_events1 TO PUBLIC');


   IF nWarningCnt > 0 THEN
      IF nWarningCnt = 1 THEN
         szMessage   := 'A warning condition was detected that needs ';
      ELSE
         szMessage   := 'Multiple warning conditions were detected that need ';
      END IF;
      szMessage      := szMessage || 'to be resolved before running Mercator DB triggering.  ';
      szMessage      := szMessage || 'Warning messages can be viewed from SQL*PLUS by issuing the ''SET SERVEROUTPUT ON'' command.';
      raise_application_error(-20999, szMessage);
   END IF;

   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If uninstalling or re-installing to a DIFFERENT schema, then comment out
   -- all the previous CREATE commands by simply surrounding them with comment 
   -- characters.  See above for the opening comment.
   -- */

   -- *********                UN-INSTALL                      **********
   --                  
   -- If uninstalling, then just uncomment the follownig stored procedure.
   /*
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE  ' || szSpfx || 'msp_record_events1( '  ||
         'szTrigName Trigger_Registry.TriggerName%TYPE, '                  ||
         'cActionType CHAR, '                ||
         'nRowId ROWID ) AS '                ||
         'szSignalEvent VARCHAR2(50); '      ||
         'nID    NUMBER; '                   ||
         'CURSOR watch_events_cursor IS '    ||
                'SELECT ID '                 ||
                'FROM   Trigger_Registry '   ||
                'WHERE  TriggerName = szTrigName ' ||
                  'AND  ProcessDate IS NULL '      ||
                  'AND  ActionType = cActionType ' ||
                'ORDER BY ID; '                    ||
       'BEGIN '                                    ||
         'IF cActionType = ''R'' THEN ' ||
            'LOCK table Trigger_Registry IN ROW SHARE MODE; ' ||
         'END IF; ' ||
         'OPEN watch_events_cursor; '              ||
         'LOOP '                                   ||
             'FETCH watch_events_cursor INTO nID; '||
             'EXIT WHEN watch_events_cursor%NOTFOUND; '     ||
             'BEGIN '                                       ||
               'szSignalEvent := ''MERCTRIG_'' || nID; '    ||
               'INSERT INTO Trigger_Events '                ||
                           '(id,st_rowid) '   ||
                      'VALUES '                                         ||
                           '(nID,nRowId); '        ||
               'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '      ||
             'EXCEPTION '                                               ||
               'WHEN DUP_VAL_ON_INDEX THEN '                            ||
                  'DBMS_ALERT.signal(szSignalEvent, szSignalEvent); '   ||
             'END; '                                                    ||
         'END LOOP; '                                                   ||
         'CLOSE watch_events_cursor; '                                  ||
       'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_record_events1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_record_events1 TO PUBLIC');
   */

END;
/

show errors

--
--          Trigger Column Based Installation Script
--
-- DESCRIPTION:   Installs database objects required for Event Server database 
--                triggering on an Oracle 8 or above database.  The following
--                database objects are created and/or altered:
--
--                1. Stored procedures, which interface to the tables.
--                2. Public synonyms.
--
-- REQUIREMENTS:  1. Oracle 8i Server or above
--         
--                2.
--                This script MUST be run by a user who can grant users of the
--                Event Server the following privileges:
--
--                  a. EXECUTE privilege on DBMS_SQL
--                  b. CREATE PUBLIC SYNONYM privilege
--                  c. DROP   PUBLIC SYNONYM privilege
--
--                The PUBLIC SYNONYM privileges are only needed at 
--                installation time.
--
--                The other privileges are necessary so that all Mercator DB 
--                triggering users will operate within a secure and pre-defined
--                Oracle environment.
--
-- INSTALL STEPS:
--                1. Before loading this script into SQL*Plus, issue 
--                   'SET SERVEROUTPUT ON' to see status messages.
--
--                2. If re-installing this script using a different user name,
--                   then follow the RE-INSTALL STEPS below.  Otherwise, just
--                   ignore this step.
--
--                3. Load and run the file from SQL*PLUS via the following
--                   command: 
--                   @TrigCol.sql
--
-- RE-INSTALL STEPS:
--                Whenever re-installing under a different user name, all 
--                database objects associated with the old schema must be
--                properly deleted.  This can be done as follows:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'RE-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--
--                   c. Undo changes made in step a.
--
--                   d. Follow Step 3 above.
--
-- UN-INSTALL STEPS:
--                To remove this code and revert back to the original trigger
--                installation, do the following:
--                   a. Comment out the lines that "CREATE" all the 
--                      database objects.
--                      Look below for 'UN-INSTALL' to determine which 
--                      section to comment out.
--
--                   b. Follow Install Step 3 above.
--

DECLARE 

   nRc         INTEGER;
   nWarningCnt INTEGER;
   szMessage   VARCHAR2(500);
   szSchema    VARCHAR2(50);
   szSpfx      VARCHAR2(51);     -- Schema prefix: Schema + '.'

   PROCEDURE msp_exec_cmd (szDdlCmd  IN VARCHAR2, szMsg IN VARCHAR2 DEFAULT NULL) IS
      X_SEQ_NOTEXISTS     EXCEPTION;
      X_SYN_NOTEXISTS     EXCEPTION;
      X_PRO_NOTEXISTS     EXCEPTION;
      X_TBL_COLEXISTS     EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_SEQ_NOTEXISTS, -2289);
      PRAGMA EXCEPTION_INIT   (X_SYN_NOTEXISTS, -1432);
      PRAGMA EXCEPTION_INIT   (X_PRO_NOTEXISTS, -4043);
      PRAGMA EXCEPTION_INIT   (X_TBL_COLEXISTS, -1430);
      cid INTEGER;
   BEGIN

      cid := dbms_sql.open_cursor;

      dbms_sql.parse(cid, szDdlCmd, dbms_sql.native);

      IF szMsg IS NOT NULL THEN
         DBMS_OUTPUT.PUT_LINE(szMsg);
      END IF;

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_SEQ_NOTEXISTS OR X_SYN_NOTEXISTS OR X_PRO_NOTEXISTS OR X_TBL_COLEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

BEGIN

   dbms_output.enable(8000);

   szSchema := USER;
   szSchema := UPPER(RTRIM(LTRIM(szSchema)));
   dbms_output.put_line('Mercator objects will be located in the following schema: ' || szSchema );
   szSpfx := szSchema || '.';

   --
   -- Drop any objects created from a previous execution of this script.
   --
   msp_exec_cmd   ('DROP    PUBLIC SYNONYM msp_create_trigger_cc',
                   'Dropped PUBLIC SYNONYM msp_create_trigger_cc');
   msp_exec_cmd   ('DROP    PROCEDURE      ' || szSpfx || 'msp_create_trigger2',
                   'Dropped PROCEDURE      ' || szSpfx || 'msp_create_trigger2');


   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If un-installing or re-installing to a DIFFERENT schema, then comment
   -- out all the following CREATE commands by simply surrounding them with
   -- the comment characters.  See further below for the closing comment.
   -- /*

   --
   -- Privilege validation.
   --
   nWarningCnt := 0;

   -- Make sure the USER has EXECUTE privilege on the DBMS_SQL package.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_SQL'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_SQL package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_SQL package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_SQL package.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_SQL package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_SQL TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   --
   -- Stored Procedure for column based triggering
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_create_trigger2 ('  ||
                                       'szTrigName    IN   VARCHAR2, '     ||
                                       'szTblSchema   IN   VARCHAR2, '     ||
                                       'szTblName     IN   VARCHAR2, '     ||
                                       'szEvntParms   IN   VARCHAR2, '     ||
                                       'cEventType    IN   CHAR, '         ||
                                       'cActionType   IN   CHAR, '         ||
                                       'szWhenCondition IN VARCHAR2 '      ||
                                       ') AS '  ||
         'cid           INTEGER; '        ||
         'szTrigCmd     VARCHAR2(512); '  ||
         'szEventType   VARCHAR2(20); '   ||
         'szActionType  VARCHAR2(20); '   ||
         'szTrigBody    VARCHAR2(200); '  ||
         'szRowClause   VARCHAR2(20); '   ||
      'BEGIN '                            ||
         'cid := dbms_sql.open_cursor; ' ||
         'IF    cEventType    =  ''I'' THEN '      ||
               'szEventType   := '' INSERT ''; '   ||
         'ELSIF cEventType    =  ''U'' THEN '      ||
               'szEventType   := '' UPDATE ''; '   ||
         'ELSIF cEventType    =  ''D'' THEN '      ||
               'szEventType   := '' DELETE ''; '   ||
         'ELSE '                                   ||
            'raise_application_error (-20100, ''Invalid event type: '' || cEventType); ' ||
         'END IF; '                                ||
         'IF    cActionType   =  ''R'' THEN '      ||
               'szActionType  := '' AFTER ''; '    ||
               'szRowClause   := '' FOR EACH ROW ''; ' ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSIF cActionType   =  ''T'' THEN '                        ||
               'szActionType  := '' BEFORE ''; '                     ||
               'szRowClause   := '' ''; '          ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSE '                                                           ||
            'raise_application_error (-20101, ''Invalid action type: '' || cActionType); ' ||
         'END IF; '                             ||
         'szTrigCmd  := ''CREATE TRIGGER '' || ''' || szSpfx || ''' || szTrigName || szActionType || szEventType || ' ||
                              '''ON '' || szTblSchema || ''.'' || szTblName || szRowClause || szWhenCondition || szTrigBody; ' || 
         'dbms_sql.parse (cid, szTrigCmd, dbms_sql.native); ' ||
         'dbms_sql.close_cursor (cid); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN '  ||
            'dbms_sql.close_cursor(cid); ' ||
            'raise_application_error(-20102, ''Mercator Trigger Definition Failure. '' || szTrigCmd, TRUE); ' ||
            'RAISE; '                      ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_create_trigger2');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_create_trigger2 TO PUBLIC');
   msp_exec_cmd   ('CREATE PUBLIC SYNONYM msp_create_trigger_cc FOR ' || szSpfx || 'msp_create_trigger2',
                   'Created PUBLIC SYNONYM msp_create_trigger_cc');

   IF nWarningCnt > 0 THEN
      IF nWarningCnt = 1 THEN
         szMessage   := 'A warning condition was detected that needs ';
      ELSE
         szMessage   := 'Multiple warning conditions were detected that need ';
      END IF;
      szMessage      := szMessage || 'to be resolved before running Mercator DB triggering.  ';
      szMessage      := szMessage || 'Warning messages can be viewed from SQL*PLUS by issuing the ''SET SERVEROUTPUT ON'' command.';
      raise_application_error(-20999, szMessage);
   END IF;

   -- *********      RE-INSTALLATION TO A DIFFERENT SCHEMA     **********
   -- *********                    OR                          **********
   -- *********                UN-INSTALL                      **********
   --
   -- If uninstalling or re-installing to a DIFFERENT schema, then comment out
   -- all the previous CREATE commands by simply surrounding them with comment 
   -- characters.  See above for the opening comment.
   -- */

END;
/
show errors
--THIS CODE IS STILL IN PROGRESS, ALTHOUGH IT WORKS, THERE ARE A COUPLE OF
--MINOR ISSUES (COMMENTED ACCORDINGLY)...

CREATE OR REPLACE PACKAGE xhb_formatting_pkg AS

    FUNCTION get_next_document_id RETURN XHB_FORMATTING.formatting_id%TYPE;


    FUNCTION get_document_details(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE)
                                  RETURN SYS_REFCURSOR;


    -- 1 represents true, 0 represents false...
    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN NUMBER);

END xhb_formatting_pkg;
/
show errors
CREATE OR REPLACE PACKAGE BODY xhb_formatting_pkg AS

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- Constant definitions for the document status'
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------

    G_NEW_DOCUMENT        CONSTANT VARCHAR2(2) := 'ND';
    G_FORMATTING_DOCUMENT CONSTANT VARCHAR2(2) := 'FD';  -- Formatting currently in process...
    G_DOCUMENT_READY      CONSTANT VARCHAR2(2) := 'DR';  -- Formatting successful...
    G_NEW_FORMATTING      CONSTANT VARCHAR2(2) := 'NF';  -- Second formatting try, when first one errors (FE)
    G_DELETE              CONSTANT VARCHAR2(2) := 'XX';
    G_FORMATTING_ERROR    CONSTANT VARCHAR2(2) := 'FE';  -- First attempt at formatting failed, may retry later...
    G_FORMATTING_FAILED   CONSTANT VARCHAR2(2) := 'FF';  -- Both formatting attempts  failed


    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_next_document_id
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- This function is autonomous so as to allow the method to update the
    -- status of the formatting object irrespective of the parent transaction.
    -- The reasoning behind this is that this function MUST return a document
    -- id that requires processing only once, and as it may be called by
    -- several concurrent connections a lock must be acquired.  And as we
    -- want any type of lock to be as short-lived as possible, crossing tiers
    -- would add significant overhead and a wider point of contention...
    --
    -- Also, this function has several hard-coded format_status'.  This is
    -- required to help performance due to the skewdness of the data in the
    -- affected tables...
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_next_document_id RETURN XHB_FORMATTING.formatting_id%TYPE
    IS
        PRAGMA AUTONOMOUS_TRANSACTION;

        l_formatting_id     XHB_FORMATTING.formatting_id%TYPE;
        l_new_format_status XHB_FORMATTING.format_status%TYPE;
    BEGIN
        -- NOT TOTALLY CORRECT AS NOT SORTED BY THE ID...
        -- However, previous version did not sort by id either...
        --
        -- If this were to implemented correctly, then AQ would be the implementation
        -- of choice, however, it is believed that we do not have the licences for this.
        -- Therefore, having to do in a single select for update call without ordering
        -- and hoping that this will not cause too many issues.  Inline views cannot be
        -- used, as we need to guarantee that simultaneous calls do not return the same
        -- value...

        BEGIN

            SELECT formatting_id, 'FD'   -- Formatting document...
            INTO   l_formatting_id, l_new_format_status
            FROM   XHB_FORMATTING
            WHERE  format_status = 'ND'  -- New document...
            AND    ROWNUM <= 1
            FOR UPDATE;

        EXCEPTION

            WHEN NO_DATA_FOUND THEN

                BEGIN

                    SELECT formatting_id, 'NF'   -- Second formatting attempt...
                    INTO   l_formatting_id, l_new_format_status
                    FROM   XHB_FORMATTING
                    WHERE  format_status = 'FE'  -- Formatting error...
                    AND    ROWNUM <= 1
                    FOR UPDATE;

                EXCEPTION

                    WHEN NO_DATA_FOUND THEN

                        -- autonomous transaction, therefore, ensure transaction finished...
                        ROLLBACK;
                        RETURN NULL;

                END;

        END;

        UPDATE XHB_FORMATTING
        SET    format_status = l_new_format_status
        WHERE  formatting_id = l_formatting_id;

        -- autonomous transaction, therefore, ensure transaction committed...
        COMMIT;
        RETURN l_formatting_id;

    EXCEPTION

        WHEN OTHERS THEN

            -- autonomous transaction, therefore, ensure transaction rolled back...
            ROLLBACK;
            -- re-raise the exception...
            RAISE;

    END get_next_document_id;


    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_document_details
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- Acquire all of the document details for the xhb_formatting entry whose
    -- primary key is passed in.  This will guarantee that a BLOB entry is
    -- available for the formatted_document entry.
    --
    -- The returned resultset has been locked as "FOR UPDATE".
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_document_details(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE)
    RETURN SYS_REFCURSOR
    IS

        l_blob_id XHB_BLOB.blob_id%TYPE;
        l_return_cursor SYS_REFCURSOR;

    BEGIN

        -- Always force the blob to be new...
       INSERT INTO XHB_BLOB
              (blob_id, blob_data)
       VALUES (XHB_BLOB_SEQ.NEXTVAL, EMPTY_BLOB())
       RETURNING blob_id
       INTO      l_blob_id;

       UPDATE XHB_FORMATTING
       SET    formatted_document_blob_id = l_blob_id
       WHERE  formatting_id = p_formatting_id_in;

        OPEN l_return_cursor FOR
            SELECT xf.formatting_id,
                   xf.format_status,            
                   xf.distribution_type,
                   xf.mime_type,
                   xf.document_type,
                   xc.clob_data AS XML_DOCUMENT,
                   xb.blob_data AS FORMATTED_DOCUMENT
            FROM   XHB_FORMATTING xf,
                   XHB_CLOB xc,
                   XHB_BLOB xb
            WHERE  xf.formatted_document_blob_id = xb.blob_id
            AND    xf.xml_document_clob_id = xc.clob_id
            AND    xf.formatting_id = p_formatting_id_in
        FOR UPDATE;

        RETURN l_return_cursor;

    END get_document_details;


    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN BOOLEAN)
    IS

        l_old_format_status XHB_FORMATTING.format_status%TYPE;
        l_new_format_status XHB_FORMATTING.format_status%TYPE := NULL;

    BEGIN

        IF (p_success_in) THEN

            l_new_format_status := G_DOCUMENT_READY;

        ELSE

            SELECT format_status
            INTO   l_old_format_status
            FROM   XHB_FORMATTING
            WHERE  formatting_id = p_formatting_id_in;

            IF (l_old_format_status = G_FORMATTING_DOCUMENT) THEN

                l_new_format_status := G_FORMATTING_ERROR;

            ELSIF (l_old_format_status = G_NEW_FORMATTING) THEN

                l_new_format_status := G_FORMATTING_FAILED;

            ELSE

                DBMS_OUTPUT.put_line('Original status was ' || l_old_format_status);
                -- Invalid original status... Should potentially throw an exception...
                RETURN;

            END IF;

        END IF;

        UPDATE XHB_FORMATTING
        SET    format_status = l_new_format_status
        WHERE  formatting_id = p_formatting_id_in;

    END update_document_status;


    -- 0 represents false, anything else is true...
    PROCEDURE update_document_status(p_formatting_id_in IN XHB_FORMATTING.formatting_id%TYPE,
                                     p_success_in       IN NUMBER)
    IS
    BEGIN

        update_document_status(p_formatting_id_in, (p_success_in <> 0));

    END update_document_status;

END xhb_formatting_pkg;
/
show errors
CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- Constant definitions for the document status'
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------

    -- 'DA' and 'PE' are also defined later as constants to SQL queries to help
    -- in the CBO's calculations for optimal plan...

    --G_NEW_DOCUMENT          CONSTANT VARCHAR2(2) := 'ND';
    G_DOCUMENT_APPROVED     CONSTANT VARCHAR2(2) := 'DA';  -- The list has been approved for processing...
    G_PROCESSING_DOCUMENT   CONSTANT VARCHAR2(2) := 'PD';  -- Processing currently in progress, 1st attempt...
    G_PROCESSING_DOCUMENT_2 CONSTANT VARCHAR2(2) := 'SD';  -- Processing currently in progress, 2nd attempt...
    G_PROCESSING_COMPLETE   CONSTANT VARCHAR2(2) := 'PC';  -- Processing successful...
    G_PROCESSING_ERROR      CONSTANT VARCHAR2(2) := 'PE';  -- First attempt at processing failed, may retry later...
    G_PROCESSING_FAILED     CONSTANT VARCHAR2(2) := 'PF';  -- Both process attempts failed...

    G_DOCUMENT_READY        CONSTANT VARCHAR2(2) := 'DR';  -- Document (letter) ready for processing...
    --G_DELETE                CONSTANT VARCHAR2(2) := 'XX';





    ----
    -- TO BE REMOVED: get_wll_unsub_rec_by_court_id
    ----
    PROCEDURE get_wll_unsub_rec_by_court_id (
                                              p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                              p_court_id        IN     NUMBER
                                            )
    IS

    BEGIN

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */
      OPEN p_unsub_recip_cur FOR
        SELECT rsf.crest_sof_id,
               rsf.solicitor_firm_name,
               RTRIM(NVL2(a.address_1,a.address_1||',',NULL)
                     ||NVL2(a.address_2,a.address_2||',',NULL)
                     ||NVL2(a.address_3,a.address_3||',',NULL)
                     ||NVL2(a.address_4,a.address_4||',',NULL)
                     ||NVL2(a.town,a.town||',',NULL)
                     ||NVL2(a.county,a.county||',',NULL)
                     ||NVL2(a.postcode,a.postcode||',',NULL)
                     ||NVL(a.country,NULL),',') solictior_firm_address,
               NULL solicitor_firm_fax,
               NULL solicitor_firm_email,
               rsf.court_id, 
               NULL wll_recipient_id
        FROM   xhb_ref_solicitor_firm rsf,
               xhb_address a
        WHERE  rsf.court_id = p_court_id
        AND    rsf.crest_sof_id NOT IN (SELECT NVL(crest_solicitor_firm_id, -1)
                                         FROM   xhb_wll_recipient)
        AND    rsf.address_id = a.address_id(+)
        AND    rsf.obs_ind = 'N'
        UNION    
        SELECT wr.crest_solicitor_firm_id crest_sof_id,
               wr.solicitor_firm_name,
               wr.solictior_firm_address,
               wr.solicitor_firm_fax,
               wr.solicitor_firm_email,
               wr.court_id, 
               wr.wll_recipient_id
        FROM   xhb_wll_recipient wr
        WHERE  wr.court_id = p_court_id
        AND    NOT EXISTS (SELECT 1
                           FROM   xhb_document_distribution 
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id)
        ORDER BY solicitor_firm_name;

    END get_wll_unsub_rec_by_court_id;


    ----
    -- TO BE REMOVED: get_dist_stat_by_court_id
    ----
    PROCEDURE get_dist_stat_by_court_id (
                                          p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                          p_court_id      IN     NUMBER
                                        )
    IS

    BEGIN

      OPEN p_dist_stat_cur FOR
          SELECT dc.DOC_CONTROL_ID,
                 dc.STATUS,
                 dc.EXPIRY_DATE,
                 dc.DISTRIBUTION_TYPE,
                 dc.MIME_TYPE,
                 dc.DOCUMENT_TYPE,
                 dc.LAST_UPDATE_DATE,
                 dc.CREATION_DATE,
                 dc.CREATED_BY,
                 dc.LAST_UPDATED_BY,
                 dc.VERSION,
                 dc.FORMATTING_ID,
                 dc.COURT_ID,
                 dc.DISTRIBUTED_DATE,
                 dc.XML_DOCUMENT_ID,
                 dr.DOCUMENT_RECIPIENT_ID,
                 dr.DOC_RECIPIENT_NAME,
                 dr.DOC_RECIPIENT_FAX,
                 dr.DOC_RECIPIENT_EMAIL
          FROM   XHB_DOCUMENT_CONTROL dc,
                 XHB_DOCUMENT_RECIPIENT dr
          WHERE  dc.STATUS != 'XX'
          AND    dc.STATUS != 'XA'
          AND   (dc.DOCUMENT_TYPE != 'IWP' OR  (dc.DOCUMENT_TYPE = 'IWP'
                                           AND (dc.STATUS != 'SE' AND dc.STATUS != 'SF')))
          AND    dc.COURT_ID = p_court_id
          AND    dc.DOC_CONTROL_ID = dr.DOC_CONTROL_ID
          ORDER BY dc.CREATION_DATE DESC;

    END get_dist_stat_by_court_id;


    ----
    -- TO BE REMOVED: get_wll_dist_stat_by_court_id
    ----
    PROCEDURE get_wll_dist_stat_by_court_id (
                                              p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                              p_court_id          IN     NUMBER
                                            )
    IS

    BEGIN

      OPEN p_wll_dist_stat_cur FOR
          SELECT DISTINCT xd1.XML_DOCUMENT_ID,
                 xd1.DATE_CREATED,
                 xd1.DOCUMENT_TITLE,
                 xd2.STATUS,
                 xd1.EXPIRY_DATE,
                 xd1.DOCUMENT_TYPE,
                 xd1.LAST_UPDATE_DATE,
                 xd1.CREATION_DATE,
                 xd1.CREATED_BY,
                 xd1.LAST_UPDATED_BY,
                 xd1.VERSION,
                 xd1.COURT_ID
          FROM   XHB_XML_DOCUMENT xd1,
                 XHB_WLL_CONTROL wc,
                 XHB_WLL_DOCUMENT wd,
                 XHB_XML_DOCUMENT xd2
          WHERE  xd1.XML_DOCUMENT_ID = wc.XML_DOCUMENT_ID
          AND    wc.WLL_CONTROL_ID = wd.WLL_CONTROL_ID
          AND    wd.XML_DOCUMENT_ID = xd2.XML_DOCUMENT_ID
          AND    xd1.DOCUMENT_TYPE = 'WL'
          AND    (xd2.STATUS != 'XX' AND xd2.STATUS != 'XA')
          AND    xd1.COURT_ID = p_court_id;

    END get_wll_dist_stat_by_court_id;


    ----
    -- get_sub_wll_rec_by_court_id
    ----
    --      Returns all subscribed wll_recipients with their document_distribution data
    ----
    FUNCTION get_sub_wll_rec_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

            SELECT
               xwr.wll_recipient_id,
               xwr.crest_solicitor_firm_id,
               xwr.solicitor_firm_name,
               xwr.solictior_firm_address,
               xwr.solicitor_firm_fax,
               xwr.solicitor_firm_email,
               xwr.last_update_date AS recipient_last_update_date,
               xwr.creation_date AS recipient_creation_date,
               xwr.created_by AS recipient_created_by,
               xwr.last_updated_by AS recipient_last_updated_by,
               xwr.version AS recipient_version,
               xwr.court_id,
               xwr.recipient_type,
               xdd.doc_distribution_id,
               xdd.distribution_type,
               xdd.document_type AS document_type,
               xdd.mime_type AS mime_type,
               xdd.last_update_date AS distribution_last_update_date,
               xdd.creation_date AS distribution_creation_date,
               xdd.created_by AS distribution_created_by,
               xdd.last_updated_by AS distribution_last_updated_by,
               xdd.version AS distribution_version,
               xdd.recipient_id,
               xdd.wll_recipient_id,
               xdd.use_pref_dist_type
            FROM
               xhb_wll_recipient xwr,
               xhb_document_distribution xdd
            WHERE
               xwr.wll_recipient_id = xdd.wll_recipient_id
            AND
               xwr.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_sub_wll_rec_by_court_id;


    ----
    -- get_unsub_wll_rec_by_court_id
    ----
    --      Returns all unsubscribed wll_recipients 
    ----
    FUNCTION get_unsub_wll_rec_by_court_id(
                                           p_court_id    IN     NUMBER
                                          )
                                          RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

             SELECT
                xwr.wll_recipient_id,
                xwr.crest_solicitor_firm_id,
                xwr.solicitor_firm_name,
                xwr.solictior_firm_address,
                xwr.solicitor_firm_fax,
                xwr.solicitor_firm_email,
                xwr.last_update_date AS recipient_last_update_date,
                xwr.creation_date AS recipient_creation_date,
                xwr.created_by AS recipient_created_by,
                xwr.last_updated_by AS recipient_last_updated_by,
                xwr.version AS recipient_version,
                xwr.court_id,
                xwr.recipient_type
             FROM
                xhb_wll_recipient xwr
             WHERE
                NOT EXISTS (SELECT
                                1
                            FROM
                                xhb_document_distribution xdd
                            WHERE
                                xwr.wll_recipient_id = xdd.wll_recipient_id)
             AND
                xwr.court_id = p_court_id	   
         UNION ALL
             SELECT
                 NULL AS wll_recipient_id,
                 xrsf.crest_sof_id AS crest_solicitor_firm_id,
                 xrsf.solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrsf.court_id,
                 'S' AS recipient_type
             FROM
                 xhb_ref_solicitor_firm xrsf,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrsf.crest_sof_id
                             AND
                                xwr.court_id = xrsf.court_id
                             AND
                                xwr.recipient_type = 'S')
             AND
                 xrsf.address_id = xa.address_id(+)
             AND
                 (xrsf.obs_ind IS NULL or xrsf.obs_ind = 'N')
             AND
                 xrsf.court_id = p_court_id					
         UNION ALL        
             SELECT
                 NULL AS wll_recipient_id,
                 TO_NUMBER(xrpa.crest_opposer_id)  AS crest_solicitor_firm_id,
                 RTRIM(NVL2(xrpa.title, xrpa.title || ' ', NULL) ||
                       NVL2(xrpa.prosecutor_name_1, xrpa.prosecutor_name_1 || ' ', NULL) ||
                       NVL(xrpa.prosecutor_name_3, NULL), ' ') AS solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrpa.court_id,
                 'O' AS recipient_type
             FROM
                 xhb_ref_prosecutor_agency xrpa,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrpa.crest_opposer_id
                             AND
                                xwr.court_id = xrpa.court_id
                             AND
                                xwr.recipient_type = 'O')
             AND
                 xrpa.address_id = xa.address_id(+)
             AND
                 (xrpa.obs_ind IS NULL or xrpa.obs_ind = 'N')
             AND
                 xrpa.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_unsub_wll_rec_by_court_id;

    ---
    -- get_letter_xml
    ---
    --      Returns clobs containing the letters for the specified list
    ---
    FUNCTION get_letter_xml(
                             p_wll_control_id IN   NUMBER,
                             p_include_post   IN   NUMBER,
                             p_include_email  IN   NUMBER,
                             p_include_fax    IN   NUMBER
                           )
                           RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR
            SELECT 
	            xhb_clob.clob_data
            FROM
	            xhb_clob,
	            xhb_xml_document,
	            xhb_wll_document
            WHERE
	            xhb_clob.clob_id = xhb_xml_document.xml_document_clob_id
            AND
   	         xhb_wll_document.xml_document_id = xhb_xml_document.xml_document_id 
            AND
   	         xhb_wll_document.wll_control_id = p_wll_control_id   
            AND
   	         ((p_include_post <> 0 AND (xhb_xml_document.status = 'PR' OR xhb_xml_document.status = 'DP')) OR 
	             (p_include_email <> 0 AND xhb_xml_document.status = 'DF') OR
   	          (p_include_fax <> 0 AND xhb_xml_document.status = 'DE'));

         RETURN l_return_cursor;

    END get_letter_xml;

    ----
    -- get_wll_control_by_pk
    ----
    --      Returns wll_control and the status of its letters
    ----
    FUNCTION get_wll_control_by_pk(
                                   p_wll_control_id    IN     NUMBER
                                  )
                                  RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xwc.wll_control_id = p_wll_control_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_pk;

    ----
    -- get_wll_control_by_court_id 
    ----
    --      Returns details of the status of a distribution list and its letters
    ----
    FUNCTION get_wll_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xxd1.document_type IN ('DLD', 'FLD', 'WLD')
                  AND
                     xwc.status NOT IN ('XX', 'XA')
                  AND
                     xxd1.court_id = p_court_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_court_id;

    ----
    -- get_doc_control_by_court_id
    ----
    --      Returns all the docment_control records with their recipient data
    ----
    FUNCTION get_doc_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT 
                xdc.doc_control_id,
                xdc.status,
                xdc.expiry_date,
                xdc.distribution_type,
                xdc.mime_type,
                xdc.document_type,
                xdc.last_update_date,
                xdc.creation_date,
                xdc.created_by,
                xdc.last_updated_by,
                xdc.version,
                xdc.formatting_id,
                xdc.court_id,
                xdc.distributed_date,
                xdc.xml_document_id,
                xdc.formatted_document_blob_id,
                xdr.doc_recipient_name
            FROM
               xhb_document_control xdc,
               xhb_document_recipient xdr
            WHERE  
               xdc.status != 'XX'
            AND    
               xdc.status != 'XA'
            AND
               (xdc.document_type != 'IWP' OR  (xdc.document_type = 'IWP' AND xdc.status != 'SE' AND xdc.status != 'SF'))
            AND    
               xdc.doc_control_id = xdr.doc_control_id
            AND    
               xdc.court_id = p_court_id;

        RETURN l_return_cursor;

    END get_doc_control_by_court_id;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_next_control_id
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- This function is autonomous so as to allow the method to update the
    -- status of the wll control object irrespective of the parent transaction.
    -- The reasoning behind this is that this function MUST return a control
    -- id that requires processing only once, and as it may be called by
    -- several concurrent connections a lock must be acquired.  And as we
    -- want any type of lock to be as short-lived as possible, crossing tiers
    -- would add significant overhead and a wider point of contention...
    --
    -- Also, this function has several hard-coded status'.  This is
    -- required to help performance due to the skewdness of the data in the
    -- affected tables...
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_next_control_id RETURN XHB_WLL_CONTROL.wll_control_id%TYPE
    IS

        PRAGMA AUTONOMOUS_TRANSACTION;

        l_wll_control_id XHB_WLL_CONTROL.wll_control_id%TYPE;
        l_new_status     XHB_WLL_CONTROL.status%TYPE;

    BEGIN

        -- NOT TOTALLY CORRECT AS NOT SORTED BY THE ID...
        -- However, previous version did not sort by id either...
        --
        -- If this were to implemented correctly, then AQ would be the implementation
        -- of choice, however, it is believed that we do not have the licences for this.
        -- Therefore, having to do in a single select for update call without ordering
        -- and hoping that this will not cause too many issues.  Inline views cannot be
        -- used, as we need to guarantee that simultaneous calls do not return the same
        -- value...

        BEGIN

            SELECT wll_control_id, G_PROCESSING_DOCUMENT
            INTO   l_wll_control_id, l_new_status
            FROM   XHB_WLL_CONTROL
            WHERE  status = 'DA'
            AND    ROWNUM <= 1
            FOR UPDATE;

        EXCEPTION

            WHEN NO_DATA_FOUND THEN

                BEGIN

                    SELECT wll_control_id, G_PROCESSING_DOCUMENT_2
                    INTO   l_wll_control_id, l_new_status
                    FROM   XHB_WLL_CONTROL
                    WHERE  status = 'PE'         -- Processing error...
                    AND    ROWNUM <= 1
                    FOR UPDATE;

                EXCEPTION

                    WHEN NO_DATA_FOUND THEN

                        -- autonomous transaction, therefore, ensure transaction rolled back...
                        ROLLBACK;
                        RETURN NULL;

                END;

        END;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = l_wll_control_id;

        -- autonomous transaction, therefore, ensure transaction committed...
        COMMIT;
        RETURN l_wll_control_id;

    EXCEPTION

        WHEN OTHERS THEN

            -- autonomous transaction, therefore, ensure transaction rolled back...
            ROLLBACK;
            -- re-raise the exception...
            RAISE;

    END get_next_control_id;


    ----
    -- update_control_status
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to update
    --     p_success_in        - 0 represents false, anything else is true
    ----
    PROCEDURE update_control_status (
                                      p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                      p_success_in        IN NUMBER
                                    )
    IS

        l_old_status XHB_WLL_CONTROL.status%TYPE;
        l_new_status XHB_WLL_CONTROL.status%TYPE := NULL;

    BEGIN

        IF (p_success_in <> 0) THEN

            l_new_status := G_PROCESSING_COMPLETE;

        ELSE

            SELECT status
            INTO   l_old_status
            FROM   XHB_WLL_CONTROL
            WHERE  wll_control_id = p_wll_control_id_in;

            IF (l_old_status = G_PROCESSING_DOCUMENT) THEN

                l_new_status := G_PROCESSING_ERROR;

            ELSIF (l_old_status = G_PROCESSING_DOCUMENT_2) THEN

                l_new_status := G_PROCESSING_FAILED;

            ELSE

                -- Invalid original status... Should potentially throw an exception...
                RETURN;

            END IF;

        END IF;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = p_wll_control_id_in;

    END update_control_status;


    ----
    -- get_xml_document
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to get
    --                           the details for
    ----
    FUNCTION get_xml_document (
                                p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE
                              )
                              RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT xwc.wll_control_id,
                   xc.clob_data AS xml_document,
                   xxd.document_type,
                   xxd.court_id
            FROM   XHB_XML_DOCUMENT xxd,
                   XHB_WLL_CONTROL xwc,
                   XHB_CLOB xc
            WHERE  xxd.xml_document_id = xwc.xml_document_id
            AND    xwc.wll_control_id = p_wll_control_id_in
            AND    xxd.xml_document_clob_id = xc.clob_id;

        RETURN l_return_cursor;

    END get_xml_document;


    ----
    -- create_list_letter
    ----
    --     p_wll_control_id_in - The wll_control_id of the original list the letter was part of
    --     p_document_type_in  - The type of the letter to be inserted
    --     p_document_title_in - The title of the letter to be inserted
    --     p_court_id_in       - The court id the letter belongs to
    --     p_recipient_id_in   - The id of the letter recipient
    --     p_recipient_type_in - The type of the recipient
    --
    --     Return the clob that the letter can be written to externally
    ----
    FUNCTION create_list_letter (
                                  p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                  p_court_id_in       IN XHB_XML_DOCUMENT.court_id%TYPE,
                                  p_document_type_in  IN XHB_XML_DOCUMENT.document_type%TYPE,
                                  p_document_title_in IN XHB_XML_DOCUMENT.document_title%TYPE,
                                  p_recipient_id_in   IN XHB_WLL_RECIPIENT.crest_solicitor_firm_id%TYPE,
                                  p_recipient_type_in IN XHB_WLL_RECIPIENT.recipient_type%TYPE
                                )
                                RETURN SYS_REFCURSOR
    IS

        l_clob_id XHB_CLOB.clob_id%TYPE;
        l_return_cursor SYS_REFCURSOR;

        l_xml_document_id  XHB_XML_DOCUMENT.xml_document_id%TYPE;
        l_wll_recipient_id XHB_WLL_DOCUMENT.wll_recipient_id%TYPE;

    BEGIN

        -- Always force the clob to be new...
        INSERT INTO XHB_CLOB
        (clob_id, clob_data)
        VALUES
        (XHB_CLOB_SEQ.NEXTVAL, EMPTY_CLOB())
        RETURNING clob_id
        INTO      l_clob_id;

        --
        -- Insert a new entry into the xhb_xml_document table, and retrieve the
        -- primary key and the clob values that were inserted for use later...
        --
        INSERT INTO XHB_XML_DOCUMENT (
                                       date_created,
                                       document_title,
                                       status,
                                       document_type,
                                       court_id,
                                       xml_document_clob_id
                                     )
                              VALUES (
                                       TRUNC(SYSDATE),
                                       p_document_title_in,
                                       G_DOCUMENT_READY,
                                       p_document_type_in,
                                       p_court_id_in,
                                       l_clob_id
                                     )
        RETURNING xml_document_id
        INTO      l_xml_document_id;

        --
        -- If required, lookup the entry in xhb_wll_recipient...
        --
        IF (p_recipient_id_in IS NOT NULL) AND (p_recipient_type_in IS NOT NULL) THEN

            -- Define inside an anonymous PL/SQL block so that we can continue
            -- if no recipient is found
            BEGIN

                SELECT wll_recipient_id
                INTO   l_wll_recipient_id
                FROM   xhb_wll_recipient
                WHERE  recipient_type          = p_recipient_type_in
                AND    court_id                = p_court_id_in
                AND    crest_solicitor_firm_id = p_recipient_id_in;

            EXCEPTION

                WHEN NO_DATA_FOUND THEN
                    -- if not found, then there can be no recipient...
                    NULL;

            END;

        END IF;

        --
        -- Now insert the required entry into xhb_wll_document...
        --
        INSERT INTO XHB_WLL_DOCUMENT (
                                       wll_recipient_id,
                                       wll_control_id,
                                       xml_document_id
                                     )
                              VALUES (
                                       l_wll_recipient_id,
                                       p_wll_control_id_in,
                                       l_xml_document_id
                                     );

        -- Due to an issue with JDBC/Oracle/Weblogic we need to return a
        -- ResultSet instead of the preferable single CLOB value...

        OPEN l_return_cursor FOR
            SELECT clob_data
            FROM   xhb_clob
            WHERE  clob_id = l_clob_id;

        RETURN l_return_cursor;

    END create_list_letter;

END xhb_list_distribution_pkg;
/
show errors
CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- Constant definitions for the document status'
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------

    -- 'DA' and 'PE' are also defined later as constants to SQL queries to help
    -- in the CBO's calculations for optimal plan...

    --G_NEW_DOCUMENT          CONSTANT VARCHAR2(2) := 'ND';
    G_DOCUMENT_APPROVED     CONSTANT VARCHAR2(2) := 'DA';  -- The list has been approved for processing...
    G_PROCESSING_DOCUMENT   CONSTANT VARCHAR2(2) := 'PD';  -- Processing currently in progress, 1st attempt...
    G_PROCESSING_DOCUMENT_2 CONSTANT VARCHAR2(2) := 'SD';  -- Processing currently in progress, 2nd attempt...
    G_PROCESSING_COMPLETE   CONSTANT VARCHAR2(2) := 'PC';  -- Processing successful...
    G_PROCESSING_ERROR      CONSTANT VARCHAR2(2) := 'PE';  -- First attempt at processing failed, may retry later...
    G_PROCESSING_FAILED     CONSTANT VARCHAR2(2) := 'PF';  -- Both process attempts failed...

    G_DOCUMENT_READY        CONSTANT VARCHAR2(2) := 'DR';  -- Document (letter) ready for processing...
    --G_DELETE                CONSTANT VARCHAR2(2) := 'XX';





    ----
    -- TO BE REMOVED: get_wll_unsub_rec_by_court_id
    ----
    PROCEDURE get_wll_unsub_rec_by_court_id (
                                              p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                              p_court_id        IN     NUMBER
                                            )
    IS

    BEGIN

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */
      OPEN p_unsub_recip_cur FOR
        SELECT rsf.crest_sof_id,
               rsf.solicitor_firm_name,
               RTRIM(NVL2(a.address_1,a.address_1||',',NULL)
                     ||NVL2(a.address_2,a.address_2||',',NULL)
                     ||NVL2(a.address_3,a.address_3||',',NULL)
                     ||NVL2(a.address_4,a.address_4||',',NULL)
                     ||NVL2(a.town,a.town||',',NULL)
                     ||NVL2(a.county,a.county||',',NULL)
                     ||NVL2(a.postcode,a.postcode||',',NULL)
                     ||NVL(a.country,NULL),',') solictior_firm_address,
               NULL solicitor_firm_fax,
               NULL solicitor_firm_email,
               rsf.court_id, 
               NULL wll_recipient_id
        FROM   xhb_ref_solicitor_firm rsf,
               xhb_address a
        WHERE  rsf.court_id = p_court_id
        AND    rsf.crest_sof_id NOT IN (SELECT NVL(crest_solicitor_firm_id, -1)
                                         FROM   xhb_wll_recipient)
        AND    rsf.address_id = a.address_id(+)
        AND    rsf.obs_ind = 'N'
        UNION    
        SELECT wr.crest_solicitor_firm_id crest_sof_id,
               wr.solicitor_firm_name,
               wr.solictior_firm_address,
               wr.solicitor_firm_fax,
               wr.solicitor_firm_email,
               wr.court_id, 
               wr.wll_recipient_id
        FROM   xhb_wll_recipient wr
        WHERE  wr.court_id = p_court_id
        AND    NOT EXISTS (SELECT 1
                           FROM   xhb_document_distribution 
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id)
        ORDER BY solicitor_firm_name;

    END get_wll_unsub_rec_by_court_id;


    ----
    -- TO BE REMOVED: get_dist_stat_by_court_id
    ----
    PROCEDURE get_dist_stat_by_court_id (
                                          p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                          p_court_id      IN     NUMBER
                                        )
    IS

    BEGIN

      OPEN p_dist_stat_cur FOR
          SELECT dc.DOC_CONTROL_ID,
                 dc.STATUS,
                 dc.EXPIRY_DATE,
                 dc.DISTRIBUTION_TYPE,
                 dc.MIME_TYPE,
                 dc.DOCUMENT_TYPE,
                 dc.LAST_UPDATE_DATE,
                 dc.CREATION_DATE,
                 dc.CREATED_BY,
                 dc.LAST_UPDATED_BY,
                 dc.VERSION,
                 dc.FORMATTING_ID,
                 dc.COURT_ID,
                 dc.DISTRIBUTED_DATE,
                 dc.XML_DOCUMENT_ID,
                 dr.DOCUMENT_RECIPIENT_ID,
                 dr.DOC_RECIPIENT_NAME,
                 dr.DOC_RECIPIENT_FAX,
                 dr.DOC_RECIPIENT_EMAIL
          FROM   XHB_DOCUMENT_CONTROL dc,
                 XHB_DOCUMENT_RECIPIENT dr
          WHERE  dc.STATUS != 'XX'
          AND    dc.STATUS != 'XA'
          AND   (dc.DOCUMENT_TYPE != 'IWP' OR  (dc.DOCUMENT_TYPE = 'IWP'
                                           AND (dc.STATUS != 'SE' AND dc.STATUS != 'SF')))
          AND    dc.COURT_ID = p_court_id
          AND    dc.DOC_CONTROL_ID = dr.DOC_CONTROL_ID
          ORDER BY dc.CREATION_DATE DESC;

    END get_dist_stat_by_court_id;


    ----
    -- TO BE REMOVED: get_wll_dist_stat_by_court_id
    ----
    PROCEDURE get_wll_dist_stat_by_court_id (
                                              p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                              p_court_id          IN     NUMBER
                                            )
    IS

    BEGIN

      OPEN p_wll_dist_stat_cur FOR
          SELECT DISTINCT xd1.XML_DOCUMENT_ID,
                 xd1.DATE_CREATED,
                 xd1.DOCUMENT_TITLE,
                 xd2.STATUS,
                 xd1.EXPIRY_DATE,
                 xd1.DOCUMENT_TYPE,
                 xd1.LAST_UPDATE_DATE,
                 xd1.CREATION_DATE,
                 xd1.CREATED_BY,
                 xd1.LAST_UPDATED_BY,
                 xd1.VERSION,
                 xd1.COURT_ID
          FROM   XHB_XML_DOCUMENT xd1,
                 XHB_WLL_CONTROL wc,
                 XHB_WLL_DOCUMENT wd,
                 XHB_XML_DOCUMENT xd2
          WHERE  xd1.XML_DOCUMENT_ID = wc.XML_DOCUMENT_ID
          AND    wc.WLL_CONTROL_ID = wd.WLL_CONTROL_ID
          AND    wd.XML_DOCUMENT_ID = xd2.XML_DOCUMENT_ID
          AND    xd1.DOCUMENT_TYPE = 'WL'
          AND    (xd2.STATUS != 'XX' AND xd2.STATUS != 'XA')
          AND    xd1.COURT_ID = p_court_id;

    END get_wll_dist_stat_by_court_id;


    ----
    -- get_sub_wll_rec_by_court_id
    ----
    --      Returns all subscribed wll_recipients with their document_distribution data
    ----
    FUNCTION get_sub_wll_rec_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

            SELECT
               xwr.wll_recipient_id,
               xwr.crest_solicitor_firm_id,
               xwr.solicitor_firm_name,
               xwr.solictior_firm_address,
               xwr.solicitor_firm_fax,
               xwr.solicitor_firm_email,
               xwr.last_update_date AS recipient_last_update_date,
               xwr.creation_date AS recipient_creation_date,
               xwr.created_by AS recipient_created_by,
               xwr.last_updated_by AS recipient_last_updated_by,
               xwr.version AS recipient_version,
               xwr.court_id,
               xwr.recipient_type,
               xdd.doc_distribution_id,
               xdd.distribution_type,
               xdd.document_type AS document_type,
               xdd.mime_type AS mime_type,
               xdd.last_update_date AS distribution_last_update_date,
               xdd.creation_date AS distribution_creation_date,
               xdd.created_by AS distribution_created_by,
               xdd.last_updated_by AS distribution_last_updated_by,
               xdd.version AS distribution_version,
               xdd.recipient_id,
               xdd.wll_recipient_id,
               xdd.use_pref_dist_type
            FROM
               xhb_wll_recipient xwr,
               xhb_document_distribution xdd
            WHERE
               xwr.wll_recipient_id = xdd.wll_recipient_id
            AND
               xwr.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_sub_wll_rec_by_court_id;


    ----
    -- get_unsub_wll_rec_by_court_id
    ----
    --      Returns all unsubscribed wll_recipients 
    ----
    FUNCTION get_unsub_wll_rec_by_court_id(
                                           p_court_id    IN     NUMBER
                                          )
                                          RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR

             SELECT
                xwr.wll_recipient_id,
                xwr.crest_solicitor_firm_id,
                xwr.solicitor_firm_name,
                xwr.solictior_firm_address,
                xwr.solicitor_firm_fax,
                xwr.solicitor_firm_email,
                xwr.last_update_date AS recipient_last_update_date,
                xwr.creation_date AS recipient_creation_date,
                xwr.created_by AS recipient_created_by,
                xwr.last_updated_by AS recipient_last_updated_by,
                xwr.version AS recipient_version,
                xwr.court_id,
                xwr.recipient_type
             FROM
                xhb_wll_recipient xwr
             WHERE
                NOT EXISTS (SELECT
                                1
                            FROM
                                xhb_document_distribution xdd
                            WHERE
                                xwr.wll_recipient_id = xdd.wll_recipient_id)
             AND
                xwr.court_id = p_court_id	   
         UNION ALL
             SELECT
                 NULL AS wll_recipient_id,
                 xrsf.crest_sof_id AS crest_solicitor_firm_id,
                 xrsf.solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrsf.court_id,
                 'S' AS recipient_type
             FROM
                 xhb_ref_solicitor_firm xrsf,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrsf.crest_sof_id
                             AND
                                xwr.court_id = xrsf.court_id
                             AND
                                xwr.recipient_type = 'S')
             AND
                 xrsf.address_id = xa.address_id(+)
             AND
                 (xrsf.obs_ind IS NULL or xrsf.obs_ind = 'N')
             AND
                 xrsf.court_id = p_court_id					
         UNION ALL        
             SELECT
                 NULL AS wll_recipient_id,
                 TO_NUMBER(xrpa.crest_opposer_id)  AS crest_solicitor_firm_id,
                 RTRIM(NVL2(xrpa.title, xrpa.title || ' ', NULL) ||
                       NVL2(xrpa.prosecutor_name_1, xrpa.prosecutor_name_1 || ' ', NULL) ||
                       NVL(xrpa.prosecutor_name_3, NULL), ' ') AS solicitor_firm_name,
                 RTRIM(NVL2(xa.address_1, xa.address_1 || ',', NULL) ||
                       NVL2(xa.address_2, xa.address_2 || ',', NULL) ||
                       NVL2(xa.address_3, xa.address_3 || ',', NULL) ||
                       NVL2(xa.address_4, xa.address_4 || ',', NULL) ||
                       NVL2(xa.town, xa.town || ',', NULL) ||
                       NVL2(xa.county, xa.county || ',', NULL) ||
                       NVL2(xa.postcode, xa.postcode || ',', NULL) ||
                       NVL(xa.country, NULL), ',') AS solictior_firm_address,
                 NULL AS solicitor_firm_fax,
                 NULL AS solicitor_firm_email,
                 NULL AS recipient_last_update_date,
                 NULL AS recipient_creation_date,
                 NULL AS recipient_created_by,
                 NULL AS recipient_last_updated_by,
                 NULL AS recipient_version,
                 xrpa.court_id,
                 'O' AS recipient_type
             FROM
                 xhb_ref_prosecutor_agency xrpa,
                 xhb_address xa
             WHERE          
                 NOT EXISTS (SELECT
                                1
                             FROM
                                xhb_wll_recipient xwr
                             WHERE
                                xwr.crest_solicitor_firm_id = xrpa.crest_opposer_id
                             AND
                                xwr.court_id = xrpa.court_id
                             AND
                                xwr.recipient_type = 'O')
             AND
                 xrpa.address_id = xa.address_id(+)
             AND
                 (xrpa.obs_ind IS NULL or xrpa.obs_ind = 'N')
             AND
                 xrpa.court_id = p_court_id;

         RETURN l_return_cursor;

    END get_unsub_wll_rec_by_court_id;

    ---
    -- get_letter_xml
    ---
    --      Returns clobs containing the letters for the specified list
    ---
    FUNCTION get_letter_xml(
                             p_wll_control_id IN   NUMBER,
                             p_include_post   IN   NUMBER,
                             p_include_email  IN   NUMBER,
                             p_include_fax    IN   NUMBER
                           )
                           RETURN SYS_REFCURSOR
    IS

         l_return_cursor SYS_REFCURSOR;

    BEGIN

         OPEN l_return_cursor FOR
            SELECT 
	            xhb_clob.clob_data
            FROM
	            xhb_clob,
	            xhb_xml_document,
	            xhb_wll_document
            WHERE
	            xhb_clob.clob_id = xhb_xml_document.xml_document_clob_id
            AND
   	         xhb_wll_document.xml_document_id = xhb_xml_document.xml_document_id 
            AND
   	         xhb_wll_document.wll_control_id = p_wll_control_id   
            AND
   	         ((p_include_post <> 0 AND (xhb_xml_document.status = 'PR' OR xhb_xml_document.status = 'DP')) OR 
	             (p_include_email <> 0 AND xhb_xml_document.status = 'DF') OR
   	          (p_include_fax <> 0 AND xhb_xml_document.status = 'DE'));

         RETURN l_return_cursor;

    END get_letter_xml;

    ----
    -- get_wll_control_by_pk
    ----
    --      Returns wll_control and the status of its letters
    ----
    FUNCTION get_wll_control_by_pk(
                                   p_wll_control_id    IN     NUMBER
                                  )
                                  RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xwc.wll_control_id = p_wll_control_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_pk;

    ----
    -- get_wll_control_by_court_id 
    ----
    --      Returns details of the status of a distribution list and its letters
    ----
    FUNCTION get_wll_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT
               xwc.wll_control_id, 
               xwc.status, 
               xwc.expiry_date, 
               xwc.last_update_date, 
               xwc.creation_date, 
               xwc.created_by, 
               xwc.last_updated_by, 
               xwc.version, 
               xwc.xml_document_id,
               xxd.document_type,
               xxd.document_title,
               xxd.date_created,
               ids.letter_ready_count,
               ids.letter_faxed_count,
               ids.letter_emailed_count,
               ids.letter_print_required_count,
               ids.letter_printed_count,
               ids.letter_error_count,
               ids.letter_deleted_count,
               ids.letter_archived_count
            FROM
                 xhb_wll_control xwc,
                 xhb_xml_document xxd,
                 (SELECT
                      xxd1.xml_document_id,
                      SUM(decode(xxd2.status, 'DR', 1, 0)) letter_ready_count,
                      SUM(decode(xxd2.status, 'DF', 1, 0)) letter_faxed_count,
                      SUM(decode(xxd2.status, 'DE', 1, 0)) letter_emailed_count,
                      SUM(decode(xxd2.status, 'PR', 1, 0)) letter_print_required_count,
                      SUM(decode(xxd2.status, 'DP', 1, 0)) letter_printed_count,
                      SUM(decode(xxd2.status, 'PF', 1, 0)) letter_error_count,
                      SUM(decode(xxd2.status, 'XX', 1, 0)) letter_deleted_count,
                      SUM(decode(xxd2.status, 'XA', 1, 0)) letter_archived_count
                  FROM
                     xhb_wll_control xwc,
                     xhb_xml_document xxd1,
                     xhb_wll_document xwd,
                     xhb_xml_document xxd2
                  WHERE
                     xwc.xml_document_id = xxd1.xml_document_id
                  AND
                     xwd.wll_control_id(+) = xwc.wll_control_id
                  AND
                     xxd2.xml_document_id(+) = xwd.xml_document_id
                  AND
                     xxd1.document_type IN ('DLD', 'FLD', 'WLD')
                  AND
                     xwc.status NOT IN ('XX', 'XA')
                  AND
                     xxd1.court_id = p_court_id
                  GROUP BY
                     xxd1.xml_document_id) ids
            WHERE
               xwc.xml_document_id = xxd.xml_document_id
            AND
               ids.xml_document_id = xxd.xml_document_id;

        RETURN l_return_cursor;

    END get_wll_control_by_court_id;

    ----
    -- get_doc_control_by_court_id
    ----
    --      Returns all the docment_control records with their recipient data
    ----
    FUNCTION get_doc_control_by_court_id(
                                         p_court_id    IN     NUMBER
                                        )
                                        RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT 
                xdc.doc_control_id,
                xdc.status,
                xdc.expiry_date,
                xdc.distribution_type,
                xdc.mime_type,
                xdc.document_type,
                xdc.last_update_date,
                xdc.creation_date,
                xdc.created_by,
                xdc.last_updated_by,
                xdc.version,
                xdc.formatting_id,
                xdc.court_id,
                xdc.distributed_date,
                xdc.xml_document_id,
                xdc.formatted_document_blob_id,
                xdr.doc_recipient_name
            FROM
               xhb_document_control xdc,
               xhb_document_recipient xdr
            WHERE  
               xdc.status != 'XX'
            AND    
               xdc.status != 'XA'
            AND
               (xdc.document_type != 'IWP' OR  (xdc.document_type = 'IWP' AND xdc.status != 'SE' AND xdc.status != 'SF'))
            AND    
               xdc.doc_control_id = xdr.doc_control_id
            AND    
               xdc.court_id = p_court_id;

        RETURN l_return_cursor;

    END get_doc_control_by_court_id;

    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    -- get_next_control_id
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    -- This function is autonomous so as to allow the method to update the
    -- status of the wll control object irrespective of the parent transaction.
    -- The reasoning behind this is that this function MUST return a control
    -- id that requires processing only once, and as it may be called by
    -- several concurrent connections a lock must be acquired.  And as we
    -- want any type of lock to be as short-lived as possible, crossing tiers
    -- would add significant overhead and a wider point of contention...
    --
    -- Also, this function has several hard-coded status'.  This is
    -- required to help performance due to the skewdness of the data in the
    -- affected tables...
    --
    ---------------------------------------------------------------------------
    ---------------------------------------------------------------------------
    --
    FUNCTION get_next_control_id RETURN XHB_WLL_CONTROL.wll_control_id%TYPE
    IS

        PRAGMA AUTONOMOUS_TRANSACTION;

        l_wll_control_id XHB_WLL_CONTROL.wll_control_id%TYPE;
        l_new_status     XHB_WLL_CONTROL.status%TYPE;

    BEGIN

        -- NOT TOTALLY CORRECT AS NOT SORTED BY THE ID...
        -- However, previous version did not sort by id either...
        --
        -- If this were to implemented correctly, then AQ would be the implementation
        -- of choice, however, it is believed that we do not have the licences for this.
        -- Therefore, having to do in a single select for update call without ordering
        -- and hoping that this will not cause too many issues.  Inline views cannot be
        -- used, as we need to guarantee that simultaneous calls do not return the same
        -- value...

        BEGIN

            SELECT wll_control_id, G_PROCESSING_DOCUMENT
            INTO   l_wll_control_id, l_new_status
            FROM   XHB_WLL_CONTROL
            WHERE  status = 'DA'
            AND    ROWNUM <= 1
            FOR UPDATE;

        EXCEPTION

            WHEN NO_DATA_FOUND THEN

                BEGIN

                    SELECT wll_control_id, G_PROCESSING_DOCUMENT_2
                    INTO   l_wll_control_id, l_new_status
                    FROM   XHB_WLL_CONTROL
                    WHERE  status = 'PE'         -- Processing error...
                    AND    ROWNUM <= 1
                    FOR UPDATE;

                EXCEPTION

                    WHEN NO_DATA_FOUND THEN

                        -- autonomous transaction, therefore, ensure transaction rolled back...
                        ROLLBACK;
                        RETURN NULL;

                END;

        END;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = l_wll_control_id;

        -- autonomous transaction, therefore, ensure transaction committed...
        COMMIT;
        RETURN l_wll_control_id;

    EXCEPTION

        WHEN OTHERS THEN

            -- autonomous transaction, therefore, ensure transaction rolled back...
            ROLLBACK;
            -- re-raise the exception...
            RAISE;

    END get_next_control_id;


    ----
    -- update_control_status
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to update
    --     p_success_in        - 0 represents false, anything else is true
    ----
    PROCEDURE update_control_status (
                                      p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                      p_success_in        IN NUMBER
                                    )
    IS

        l_old_status XHB_WLL_CONTROL.status%TYPE;
        l_new_status XHB_WLL_CONTROL.status%TYPE := NULL;

    BEGIN

        IF (p_success_in <> 0) THEN

            l_new_status := G_PROCESSING_COMPLETE;

        ELSE

            SELECT status
            INTO   l_old_status
            FROM   XHB_WLL_CONTROL
            WHERE  wll_control_id = p_wll_control_id_in;

            IF (l_old_status = G_PROCESSING_DOCUMENT) THEN

                l_new_status := G_PROCESSING_ERROR;

            ELSIF (l_old_status = G_PROCESSING_DOCUMENT_2) THEN

                l_new_status := G_PROCESSING_FAILED;

            ELSE

                -- Invalid original status... Should potentially throw an exception...
                RETURN;

            END IF;

        END IF;

        UPDATE XHB_WLL_CONTROL
        SET    status = l_new_status
        WHERE  wll_control_id = p_wll_control_id_in;

    END update_control_status;


    ----
    -- get_xml_document
    ----
    --     p_wll_control_id_in - The wll_control_id of the wll_control to get
    --                           the details for
    ----
    FUNCTION get_xml_document (
                                p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE
                              )
                              RETURN SYS_REFCURSOR
    IS

        l_return_cursor SYS_REFCURSOR;

    BEGIN

        OPEN l_return_cursor FOR
            SELECT xwc.wll_control_id,
                   xc.clob_data AS xml_document,
                   xxd.document_type,
                   xxd.court_id
            FROM   XHB_XML_DOCUMENT xxd,
                   XHB_WLL_CONTROL xwc,
                   XHB_CLOB xc
            WHERE  xxd.xml_document_id = xwc.xml_document_id
            AND    xwc.wll_control_id = p_wll_control_id_in
            AND    xxd.xml_document_clob_id = xc.clob_id;

        RETURN l_return_cursor;

    END get_xml_document;


    ----
    -- create_list_letter
    ----
    --     p_wll_control_id_in - The wll_control_id of the original list the letter was part of
    --     p_document_type_in  - The type of the letter to be inserted
    --     p_document_title_in - The title of the letter to be inserted
    --     p_court_id_in       - The court id the letter belongs to
    --     p_recipient_id_in   - The id of the letter recipient
    --     p_recipient_type_in - The type of the recipient
    --
    --     Return the clob that the letter can be written to externally
    ----
    FUNCTION create_list_letter (
                                  p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE,
                                  p_court_id_in       IN XHB_XML_DOCUMENT.court_id%TYPE,
                                  p_document_type_in  IN XHB_XML_DOCUMENT.document_type%TYPE,
                                  p_document_title_in IN XHB_XML_DOCUMENT.document_title%TYPE,
                                  p_recipient_id_in   IN XHB_WLL_RECIPIENT.crest_solicitor_firm_id%TYPE,
                                  p_recipient_type_in IN XHB_WLL_RECIPIENT.recipient_type%TYPE
                                )
                                RETURN SYS_REFCURSOR
    IS

        l_clob_id XHB_CLOB.clob_id%TYPE;
        l_return_cursor SYS_REFCURSOR;

        l_xml_document_id  XHB_XML_DOCUMENT.xml_document_id%TYPE;
        l_wll_recipient_id XHB_WLL_DOCUMENT.wll_recipient_id%TYPE;

    BEGIN

        -- Always force the clob to be new...
        INSERT INTO XHB_CLOB
        (clob_id, clob_data)
        VALUES
        (XHB_CLOB_SEQ.NEXTVAL, EMPTY_CLOB())
        RETURNING clob_id
        INTO      l_clob_id;

        --
        -- Insert a new entry into the xhb_xml_document table, and retrieve the
        -- primary key and the clob values that were inserted for use later...
        --
        INSERT INTO XHB_XML_DOCUMENT (
                                       date_created,
                                       document_title,
                                       status,
                                       document_type,
                                       court_id,
                                       xml_document_clob_id
                                     )
                              VALUES (
                                       TRUNC(SYSDATE),
                                       p_document_title_in,
                                       G_DOCUMENT_READY,
                                       p_document_type_in,
                                       p_court_id_in,
                                       l_clob_id
                                     )
        RETURNING xml_document_id
        INTO      l_xml_document_id;

        --
        -- If required, lookup the entry in xhb_wll_recipient...
        --
        IF (p_recipient_id_in IS NOT NULL) AND (p_recipient_type_in IS NOT NULL) THEN

            -- Define inside an anonymous PL/SQL block so that we can continue
            -- if no recipient is found
            BEGIN

                SELECT wll_recipient_id
                INTO   l_wll_recipient_id
                FROM   xhb_wll_recipient
                WHERE  recipient_type          = p_recipient_type_in
                AND    court_id                = p_court_id_in
                AND    crest_solicitor_firm_id = p_recipient_id_in;

            EXCEPTION

                WHEN NO_DATA_FOUND THEN
                    -- if not found, then there can be no recipient...
                    NULL;

            END;

        END IF;

        --
        -- Now insert the required entry into xhb_wll_document...
        --
        INSERT INTO XHB_WLL_DOCUMENT (
                                       wll_recipient_id,
                                       wll_control_id,
                                       xml_document_id
                                     )
                              VALUES (
                                       l_wll_recipient_id,
                                       p_wll_control_id_in,
                                       l_xml_document_id
                                     );

        -- Due to an issue with JDBC/Oracle/Weblogic we need to return a
        -- ResultSet instead of the preferable single CLOB value...

        OPEN l_return_cursor FOR
            SELECT clob_data
            FROM   xhb_clob
            WHERE  clob_id = l_clob_id;

        RETURN l_return_cursor;

    END create_list_letter;

END xhb_list_distribution_pkg;
/
show errors
CREATE OR REPLACE PACKAGE xhb_connected_user_pkg AS
       PROCEDURE get_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);

       PROCEDURE get_connected_users(results_out      OUT SYS_REFCURSOR);
       
       PROCEDURE get_nonPD_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                       activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);
       
       PROCEDURE get_nonPD_connected_users(results_out      OUT SYS_REFCURSOR);
       
       PROCEDURE get_PD_by_later_than_date(results_out   OUT SYS_REFCURSOR,
                                       activity_date_in      IN  XHB_CONNECTED_USER.last_access_time%TYPE);
       
       PROCEDURE get_PD_connected_users(results_out      OUT SYS_REFCURSOR);

END xhb_connected_user_pkg;
/
show errors
CREATE OR REPLACE PACKAGE BODY xhb_connected_user_pkg AS
       PROCEDURE get_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
            OPEN results_out FOR
                SELECT connected_user.* 
                FROM   XHB_CONNECTED_USER connected_user
                WHERE  (activity_date_in IS NULL OR connected_user.LAST_ACCESS_TIME > activity_date_in OR connected_user.LAST_ACCESS_TIME IS NULL)
                ORDER BY  DECODE(connected_user.component,'PublicDisplay',1,0),connected_user.last_access_time DESC;
        END get_by_later_than_date;

        PROCEDURE get_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
            OPEN results_out FOR
                SELECT connected_user.*
                FROM     XHB_CONNECTED_USER connected_user
                ORDER BY  DECODE(connected_user.component,'PublicDisplay',1,0),connected_user.last_access_time DESC;
        END get_connected_users;

        PROCEDURE get_nonPD_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
            OPEN results_out FOR
               SELECT CONNECTED_USER.* 
               FROM   XHB_CONNECTED_USER CONNECTED_USER
               WHERE  (activity_date_in IS NULL OR CONNECTED_USER.LAST_ACCESS_TIME = activity_date_in)
               AND CONNECTED_USER.COMPONENT != 'PublicDisplay';         
        END get_nonPD_by_later_than_date;


        PROCEDURE get_nonPD_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
           OPEN results_out FOR
               SELECT CONNECTED_USER.* 
               FROM XHB_CONNECTED_USER CONNECTED_USER
               WHERE CONNECTED_USER.COMPONENT != 'PublicDisplay';
        END get_nonPD_connected_users;

        PROCEDURE get_PD_by_later_than_date(results_out    OUT SYS_REFCURSOR,
                                    activity_date_in IN  XHB_CONNECTED_USER.last_access_time%TYPE)
        IS
        BEGIN
           OPEN results_out FOR
              SELECT CONNECTED_USER.* 
              FROM   XHB_CONNECTED_USER CONNECTED_USER
              WHERE  (activity_date_in IS NULL OR CONNECTED_USER.LAST_ACCESS_TIME = activity_date_in)
              AND CONNECTED_USER.COMPONENT = 'PublicDisplay'
              ORDER BY LAST_ACCESS_TIME DESC;         
        END get_PD_by_later_than_date;


        PROCEDURE get_PD_connected_users(results_out  OUT SYS_REFCURSOR)
        IS
        BEGIN
          OPEN results_out FOR
              SELECT CONNECTED_USER.* 
              FROM XHB_CONNECTED_USER CONNECTED_USER
              WHERE CONNECTED_USER.COMPONENT = 'PublicDisplay'
              ORDER BY LAST_ACCESS_TIME DESC;
        END get_PD_connected_users;
END xhb_connected_user_pkg;
/
show errors


-- Abbas's script m4ora8.sql

--
--          Installation Script for Column Based Triggering.  This script is
--          run only when an existing release needs to be upgraded to support
--          column based triggering support.
--
-- DESCRIPTION:   Changes existing triggering objects in order to support column
--                based trigginer.  The following database objects are altered:
--
--                1. Stored procedure, msp_create_trigger1, is changed to handle
--                   column based triggering.
--                2. Trigger_catalog table is altered by adding a new column.
--
-- REQUIREMENTS:  1. Oracle 8i Server or above
--         
--                2.
--                This script MUST be run by a user who can grant users of the
--                Event Server the following privilege:
--
--                  a. EXECUTE privilege on DBMS_SQL
--
-- INSTALL STEPS:
--                1. Before loading this script into SQL*Plus, issue 
--                   'SET SERVEROUTPUT ON' to see status messages.
--
--                2. Load and run the file from SQL*PLUS via the following
--                   command: 
--                   @m4ora8_col.sql
--
-- RE-INSTALL STEPS:
--                Not necessary, since all re-installations can be done via the
--                original triggering installation script.
--
-- UN-INSTALL STEPS:
--                Not necessary, since all attempts to uninstall these changes
--                can be done via the original triggering installation script.
--

DECLARE 

   nRc         INTEGER;
   nWarningCnt INTEGER;
   szMessage   VARCHAR2(500);
   szSchema    VARCHAR2(50);
   szSpfx      VARCHAR2(51);     -- Schema prefix: Schema + '.'

   PROCEDURE msp_exec_cmd (szDdlCmd  IN VARCHAR2, szMsg IN VARCHAR2 DEFAULT NULL) IS
      X_SEQ_NOTEXISTS     EXCEPTION;
      X_SYN_NOTEXISTS     EXCEPTION;
      X_PRO_NOTEXISTS     EXCEPTION;
      X_TBL_COLEXISTS     EXCEPTION;
      PRAGMA EXCEPTION_INIT   (X_SEQ_NOTEXISTS, -2289);
      PRAGMA EXCEPTION_INIT   (X_SYN_NOTEXISTS, -1432);
      PRAGMA EXCEPTION_INIT   (X_PRO_NOTEXISTS, -4043);
      PRAGMA EXCEPTION_INIT   (X_TBL_COLEXISTS, -1430);
      cid INTEGER;
   BEGIN

      cid := dbms_sql.open_cursor;

      dbms_sql.parse(cid, szDdlCmd, dbms_sql.native);

      IF szMsg IS NOT NULL THEN
         DBMS_OUTPUT.PUT_LINE(szMsg);
      END IF;

      dbms_sql.close_cursor(cid);

   EXCEPTION
      WHEN X_SEQ_NOTEXISTS OR X_SYN_NOTEXISTS OR X_PRO_NOTEXISTS OR X_TBL_COLEXISTS THEN 
         dbms_sql.close_cursor(cid);
   END;

BEGIN

   dbms_output.enable(8000);

   szSchema := USER;
   szSchema := UPPER(RTRIM(LTRIM(szSchema)));
   dbms_output.put_line('Mercator objects will be located in the following schema: ' || szSchema );
   szSpfx := szSchema || '.';

   --
   -- Privilege validation.
   --
   nWarningCnt := 0;

   -- Make sure the USER has EXECUTE privilege on the DBMS_SQL package.
   BEGIN
      BEGIN
         SELECT   DISTINCT(1)
           INTO   nRc     
         FROM     ALL_TAB_PRIVS 
         WHERE    GRANTEE     IN (USER, 'PUBLIC')
           AND    TABLE_NAME  = 'DBMS_SQL'
           AND    PRIVILEGE   = 'EXECUTE';
      EXCEPTION
         WHEN OTHERS THEN
            nRc := 0;
      END;

      IF nRc = 1 THEN
         dbms_output.put_line('INFORMATION: The current user, ' || USER || ', has the required EXECUTE privilege on the DBMS_SQL package.');
      ELSE
         dbms_output.put_line('ERROR: The current user, ' || USER || ', requires EXECUTE privilege on the DBMS_SQL package.');
         raise_application_error(-20901, 'Insufficient privileges on the DBMS_SQL package.');
      END IF;

   EXCEPTION
      WHEN OTHERS THEN
         dbms_output.put_line('WARNING: User, ' || USER || ', must be granted EXECUTE on the DBMS_SQL package in order ');
         dbms_output.put_line('for Mercator database triggering to work.  ');
         dbms_output.put_line('This privilege can be granted by the SYSDBA via the command: GRANT EXECUTE on DBMS_SQL TO ' || USER);
         nWarningCnt := nWarningCnt + 1;
   END;

   msp_exec_cmd   (
      'ALTER TABLE mtbl_trigger_catalog ADD (ColumnCondition varchar2(1024))  ',
                   'Altered TABLE ' || szSpfx || 'mtbl_trigger_catalog');

   --
   -- Stored Procedure for column based triggering
   --
   msp_exec_cmd   (
      'CREATE OR REPLACE PROCEDURE ' || szSpfx || 'msp_create_trigger1 ('  ||
                                       'szTrigName    IN   VARCHAR2, '     ||
                                       'szTblSchema   IN   VARCHAR2, '     ||
                                       'szTblName     IN   VARCHAR2, '     ||
                                       'szEvntParms   IN   VARCHAR2, '     ||
                                       'cEventType    IN   CHAR, '         ||
                                       'cActionType   IN   CHAR, '         ||
                                       'szWhenCondition IN VARCHAR2 DEFAULT NULL ) AS '  ||
         'cid           INTEGER; '        ||
         'szTrigCmd     VARCHAR2(512); '  ||
         'szEventType   VARCHAR2(20); '   ||
         'szActionType  VARCHAR2(20); '   ||
         'szTrigBody    VARCHAR2(200); '  ||
         'szRowClause   VARCHAR2(20); '   ||
      'BEGIN '                            ||
         'cid := dbms_sql.open_cursor; ' ||
         'IF    cEventType    =  ''I'' THEN '      ||
               'szEventType   := '' INSERT ''; '   ||
         'ELSIF cEventType    =  ''U'' THEN '      ||
               'szEventType   := '' UPDATE ''; '   ||
         'ELSIF cEventType    =  ''D'' THEN '      ||
               'szEventType   := '' DELETE ''; '   ||
         'ELSE '                                   ||
            'raise_application_error (-20100, ''Invalid event type: '' || cEventType); ' ||
         'END IF; '                                ||
         'IF    cActionType   =  ''R'' THEN '      ||
               'szActionType  := '' AFTER ''; '    ||
               'szRowClause   := '' FOR EACH ROW ''; ' ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSIF cActionType   =  ''T'' THEN '                        ||
               'szActionType  := '' BEFORE ''; '                     ||
               'szRowClause   := '' ''; '          ||
               'szTrigBody    := '' BEGIN '' || ''' || szSpfx || ''' || ''msp_record_events1('' || szEvntParms || ''); END;'' ' || '; ' ||
         'ELSE '                                                           ||
            'raise_application_error (-20101, ''Invalid action type: '' || cActionType); ' ||
         'END IF; '                             ||
         'szTrigCmd  := ''CREATE TRIGGER '' || ''' || szSpfx || ''' || szTrigName || szActionType || szEventType || ' ||
                              '''ON '' || szTblSchema || ''.'' || szTblName || szRowClause || szWhenCondition || szTrigBody; ' || 
         'dbms_sql.parse (cid, szTrigCmd, dbms_sql.native); ' ||
         'dbms_sql.close_cursor (cid); ' ||
      'EXCEPTION ' ||
         'WHEN OTHERS THEN '  ||
            'dbms_sql.close_cursor(cid); ' ||
            'raise_application_error(-20102, ''Mercator Trigger Definition Failure. '' || szTrigCmd, TRUE); ' ||
            'RAISE; '                      ||
      'END;',
                   'Created PROCEDURE      ' || szSpfx || 'msp_create_trigger1');
   msp_exec_cmd   ('GRANT EXECUTE ON ' || szSpfx || 'msp_create_trigger1 TO PUBLIC');

   IF nWarningCnt > 0 THEN
      IF nWarningCnt = 1 THEN
         szMessage   := 'A warning condition was detected that needs ';
      ELSE
         szMessage   := 'Multiple warning conditions were detected that need ';
      END IF;
      szMessage      := szMessage || 'to be resolved before running Mercator DB triggering.  ';
      szMessage      := szMessage || 'Warning messages can be viewed from SQL*PLUS by issuing the ''SET SERVEROUTPUT ON'' command.';
      raise_application_error(-20999, szMessage);
   END IF;

END;
/
show errors

-- Alex Brown's 56805_DB_SCRIPT.SQL
/*
   These statments need to be run and exported in a delimited format so create a record of what will be changed by the update statements.
   Attach the files to PR 56805.
   The changes will also be documented in the audit tables in the database.
*/

SELECT XML_DOCUMENT_ID, STATUS FROM XHB_XML_DOCUMENT WHERE STATUS IN ('ND','PD','DR');

SELECT DOC_CONTROL_ID, STATUS FROM XHB_DOCUMENT_CONTROL WHERE STATUS IN ('ND','FD','DR','DA','FT');

SELECT FORMATTING_ID, FORMAT_STATUS FROM XHB_FORMATTING WHERE FORMAT_STATUS IN ('ND','FD','DR');

/*
   The following statments will update the database.
*/

UPDATE XHB_XML_DOCUMENT
SET STATUS = 'PF'
WHERE STATUS IN ('ND','PD','DR');

UPDATE XHB_DOCUMENT_CONTROL
SET STATUS = 'FF'
WHERE STATUS IN ('ND','FD','DR','DA','FT');

UPDATE XHB_FORMATTING
SET FORMAT_STATUS = 'FF'
WHERE FORMAT_STATUS IN ('ND','FD','DR');

COMMIT;

/*
 * Changes, additions or deletion of standing data
 */
delete from xhb_security_group_role where role_name like 'XHBOrders%';
delete from xhb_security_role where role_name like 'XHBOrders%';

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION)
VALUES ( 155, 40737, 'Capture Verdict', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 0, 0 , 'Magistrate_General_Disposal_Appeal_Result');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION)
VALUES ( 156, 40738, 'Capture Verdict', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 0, 0 , 'Delete_Magistrate_General_Disposal_Appeal_Result');


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.0', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.0', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.0', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.0', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
