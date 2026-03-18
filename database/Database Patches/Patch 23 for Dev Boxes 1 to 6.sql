/*
 * Patch(23) to upgrade DB release 22 to DB release 23.
 */

-- SEQ For CRN autogeneration

CREATE SEQUENCE XHB_CRN_SEQ MAXVALUE 99999999999 CYCLE NOORDER;


-- SEQ for XHB_IMPORT_EXPORT_STATUS

CREATE SEQUENCE XHB_IMPORT_EXPORT_STATUS_SEQ
NOMAXVALUE
NOMINVALUE
NOCACHE
NOCYCLE
NOORDER;

CREATE TABLE XHB_IMPORT_EXPORT_STATUS (
       IMPORT_EXPORT_STATUS_ID  NUMBER(8)      NOT NULL,
       TYPE_CODE                VARCHAR2(5)    NOT NULL,
       STATUS_CODE              VARCHAR2(1)    NOT NULL,
       MESSAGE                  VARCHAR2(2000) NULL,
       CASE_ID                  NUMBER(8)      NULL,
       COURT_ID                 NUMBER(8)      NOT NULL,
       DEFENDANT_ON_CASE_ID     NUMBER(8)      NULL,
       LAST_UPDATE_DATE         DATE           NOT NULL,
       CREATION_DATE            DATE           NOT NULL,
       CREATED_BY               VARCHAR2(30)   NOT NULL,
       LAST_UPDATED_BY          VARCHAR2(30)   NOT NULL,
       VERSION                  NUMBER(5)      NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);

ALTER TABLE XHB_IMPORT_EXPORT_STATUS
       ADD (PRIMARY KEY (IMPORT_EXPORT_STATUS_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Foreign Keys

ALTER TABLE XHB_IMPORT_EXPORT_STATUS
      ADD (FOREIGN KEY (CASE_ID)
           REFERENCES XHB_CASE);

ALTER TABLE XHB_IMPORT_EXPORT_STATUS
      ADD (FOREIGN KEY (COURT_ID)
           REFERENCES XHB_COURT);

ALTER TABLE XHB_IMPORT_EXPORT_STATUS
      ADD (FOREIGN KEY (DEFENDANT_ON_CASE_ID)
           REFERENCES XHB_DEFENDANT_ON_CASE);

-- Audit table

CREATE TABLE AUD_IMPORT_EXPORT_STATUS TABLESPACE AUDITD AS SELECT * FROM XHB_IMPORT_EXPORT_STATUS;
ALTER TABLE AUD_IMPORT_EXPORT_STATUS ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

CREATE OR REPLACE TRIGGER XHB_IMP_EXP_STATUS_BIR_TR
  BEFORE INSERT
  ON XHB_IMPORT_EXPORT_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.IMPORT_EXPORT_STATUS_ID IS NULL THEN

    SELECT XHB_IMPORT_EXPORT_STATUS_SEQ.NEXTVAL
    INTO   :NEW.IMPORT_EXPORT_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_IMP_EXP_STATUS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_IMPORT_EXPORT_STATUS
  FOR EACH ROW

DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in 
XHB_SYS_USER_INFORMATION */
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

    /* If the user is not the connection pool user as defined in 
XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_IMPORT_EXPORT_STATUS') = 1) THEN

    INSERT INTO AUD_IMPORT_EXPORT_STATUS
    VALUES (:old.IMPORT_EXPORT_STATUS_ID,
            :old.TYPE_CODE,
            :old.STATUS_CODE,
            :old.MESSAGE,
            :old.CASE_ID,
            :old.COURT_ID,
            :old.DEFENDANT_ON_CASE_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

ALTER TABLE XHB_DEFENDANT ADD (PRISON_ID VARCHAR2(4) NULL);

ALTER TABLE XHB_DEFENDANT_ON_CASE ADD (PNC_ID VARCHAR2(10) NULL);

ALTER TABLE XHB_DEFENDANT_ON_OFFENCE ADD (CRN_ID VARCHAR2(23) NULL);

ALTER TABLE XHB_CASE ADD (MAGISTRATES_CASE_REF VARCHAR2(30) NULL);

ALTER TABLE XHB_COURT ADD (COURT_CODE CHAR(4) NULL);


DROP TABLE AUD_DEFENDANT;
CREATE TABLE AUD_DEFENDANT TABLESPACE AUDITD AS SELECT * FROM XHB_DEFENDANT;
TRUNCATE TABLE AUD_DEFENDANT;
ALTER TABLE AUD_DEFENDANT ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE AUD_DEFENDANT_ON_CASE;
CREATE TABLE AUD_DEFENDANT_ON_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_DEFENDANT_ON_CASE;
TRUNCATE TABLE AUD_DEFENDANT_ON_CASE;
ALTER TABLE AUD_DEFENDANT_ON_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE AUD_DEFENDANT_ON_OFFENCE;
CREATE TABLE AUD_DEFENDANT_ON_OFFENCE TABLESPACE AUDITD AS SELECT * FROM XHB_DEFENDANT_ON_OFFENCE;
TRUNCATE TABLE AUD_DEFENDANT_ON_OFFENCE;
ALTER TABLE AUD_DEFENDANT_ON_OFFENCE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE AUD_CASE;
CREATE TABLE AUD_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_CASE;
TRUNCATE TABLE AUD_CASE;
ALTER TABLE AUD_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE AUD_COURT;
CREATE TABLE AUD_COURT TABLESPACE AUDITD AS SELECT * FROM XHB_COURT;
TRUNCATE TABLE AUD_COURT;
ALTER TABLE AUD_COURT ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

CREATE OR REPLACE TRIGGER XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_CASE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN

    INSERT INTO AUD_CASE 
    VALUES (:old.CASE_ID, 
            :old.CASE_NUMBER, 
            :old.CASE_TYPE, 
            :old.MAG_CONVICTION_DATE, 
            :old.CASE_SUB_TYPE, 
            :old.CASE_TITLE, 
            :old.CASE_DESCRIPTION, 
            :old.LINKED_CASE_ID, 
            :old.BAIL_MAG_CODE, 
            :old.REF_COURT_ID, 
            :old.COURT_ID, 
            :old.CHARGE_IMPORT_INDICATOR, 
            :old.SEVERED_IND, 
            :old.INDICT_RESP, 
            :old.DATE_IND_REC, 
            :old.PROS_AGENCY_REFERENCE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.CASE_CLASS, 
            :old.JUDGE_REASON_FOR_APPEAL, 
            :old.RESULTS_VERIFIED, 
            :old.LENGTH_TAPE, 
            :old.NO_PAGE_PROS_EVIDENCE, 
            :old.NO_PROS_WITNESS, 
            :old.EST_PDH_TRIAL_LENGTH, 
            :old.indictment_info_1, 
            :old.indictment_info_2, 
            :old.indictment_info_3, 
            :old.indictment_info_4, 
            :old.indictment_info_5, 
            :old.indictment_info_6, 
            :old.POLICE_OFFICER_ATTENDING, 
            :old.CPS_CASE_WORKER, 
            :old.EXPORT_CHARGES, 
            :old.IND_CHANGE_STATUS,
            :old.MAGISTRATES_CASE_REF,
            l_trig_event);

  END IF;

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
            :old.in_service_flag, 
            :old.obs_ind, 
            :old.PROBATION_OFFICE_NAME, 
            :old.INTERNET_COURT_NAME, 
            :old.DISPLAY_NAME,
            :old.COURT_CODE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANT_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT') = 1) THEN

    INSERT INTO AUD_DEFENDANT 
    VALUES (:old.DEFENDANT_ID, 
            :old.CREST_DEFENDANT_ID, 
            :old.FIRST_NAME, 
            :old.MIDDLE_NAME, 
            :old.SURNAME, 
            :old.INITIALS, 
            :old.DATE_OF_BIRTH, 
            :old.GENDER, 
            :old.LAST_CONVICTION_DATE, 
            :old.IS_COMPANY, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.ADDRESS_ID, 
            :old.COURT_ID,
            :old.PRISON_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_CASE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANTONCASE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_CASE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_CASE 
    VALUES (:old.DEFENDANT_ON_CASE_ID, 
            :old.NO_OF_TICS, 
            :old.FINAL_DRIVING_LICENCE_STATUS, 
            :old.PTIURN, 
            :old.IS_JUVENILE, 
            :old.IS_MASKED, 
            :old.MASKED_NAME, 
            :old.CASE_ID, 
            :old.DEFENDANT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.results_verified, 
            :old.defendant_number, 
            :old.date_of_committal,
            :old.PNC_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_OFFENCE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANTONOFFENCE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_OFFENCE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_OFFENCE 
    VALUES (:old.DEFENDANT_ON_OFFENCE_ID, 
            :old.APPEAL_AGAINST_TYPE, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.OFFENCE_ID, 
            :old.ORIGINAL_RESULT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.IS_SENTENCE_APPEAL, 
            :old.IS_CONVICTION_APPEAL, 
            :old.ALT_REF_OFFENCE_ID, 
            :old.HAS_OTHER, 
            :old.is_stayed, 
            :old.HAS_CONVICTION, 
            :old.HAS_VERDICT,
            :old.CRN_ID,
            l_trig_event);

  END IF;

END;
/

alter trigger xhb_COURT_LOG_ENTRY_BUR_TR disable;

ALTER TABLE xhb_court_log_entry RENAME COLUMN log_entry_xml TO log_entry_xml_varchar;

ALTER TABLE xhb_court_log_entry ADD (log_entry_xml CLOB NULL);

UPDATE xhb_court_log_entry SET log_entry_xml = log_entry_xml_varchar;

ALTER TABLE xhb_court_log_entry modify (log_entry_xml NOT NULL);

ALTER TABLE xhb_court_log_entry DROP COLUMN log_entry_xml_varchar;

alter trigger xhb_COURT_LOG_ENTRY_BUR_TR enable;

-- AUDIT

DROP TABLE AUD_COURT_LOG_ENTRY;
CREATE TABLE AUD_COURT_LOG_ENTRY TABLESPACE AUDITD AS SELECT * FROM XHB_COURT_LOG_ENTRY;
TRUNCATE TABLE AUD_COURT_LOG_ENTRY;
ALTER TABLE AUD_COURT_LOG_ENTRY ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

ALTER TABLE XHB_EMAIL MODIFY (RECIPIENTS VARCHAR2(255), CCRECIPIENTS VARCHAR2(255), BCCRECIPIENTS VARCHAR2(255));

DROP TABLE AUD_EMAIL;
CREATE TABLE AUD_EMAIL TABLESPACE AUDITD AS SELECT * FROM XHB_EMAIL;
TRUNCATE TABLE AUD_EMAIL;
ALTER TABLE AUD_EMAIL ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

ALTER TABLE XHB_COURT_LOG_ENTRY ADD (DEFENDANT_ON_CASE_ID NUMBER(8) NULL,
                                     DEFENDANT_ID         NUMBER(8) NULL);

DROP TABLE AUD_COURT_LOG_ENTRY;
CREATE TABLE AUD_COURT_LOG_ENTRY TABLESPACE AUDITD AS SELECT * FROM XHB_COURT_LOG_ENTRY;
TRUNCATE TABLE AUD_COURT_LOG_ENTRY;
ALTER TABLE AUD_COURT_LOG_ENTRY ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (DEFENDANT_ON_CASE_ID)
           REFERENCES XHB_DEFENDANT_ON_CASE);

ALTER TABLE XHB_COURT_LOG_ENTRY
      ADD (FOREIGN KEY (DEFENDANT_ID)
           REFERENCES XHB_DEFENDANT);

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_ENTRY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_ENTRY
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURT_LOG_ENTRY_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_ENTRY') = 1) THEN

    INSERT INTO AUD_COURT_LOG_ENTRY 
    VALUES (:old.ENTRY_ID, 
            :old.CASE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.DATE_TIME, 
            :old.EVENT_DESC_ID,
            :old.LOG_ENTRY_XML,
            :old.DEFENDANT_ON_CASE_ID,
            :old.DEFENDANT_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE PACKAGE xhb_list_distribution_pkg AS

  TYPE cur_unsub_recips IS REF CURSOR;

  PROCEDURE  get_wll_unsub_rec_by_court_id(p_unsub_recip_cur IN OUT cur_unsub_recips,
                                           p_court_id        IN     NUMBER);

END xhb_list_distribution_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

  PROCEDURE get_wll_unsub_rec_by_court_id (p_unsub_recip_cur IN OUT cur_unsub_recips,
                                           p_court_id        IN     NUMBER) IS

    BEGIN

      OPEN p_unsub_recip_cur FOR

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */

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

END xhb_list_distribution_pkg;
/
show errors

CREATE OR REPLACE VIEW
XHB_COUNSEL_FACILITIES_SH_V AS
SELECT DISTINCT
		sitting.court_room_id                         	COURT_ROOM_ID,
		sitting.is_floating                           	IS_FLOATING,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME,
		court_room.court_room_name                    	COURT_ROOM_NAME,
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME,
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID,
		scheduled_hearing.original_time               	SH_ORIGINAL_TIME,
		scheduled_hearing.not_before_time             	SH_NOT_BEFORE_TIME,
		sched_hearing_defendant.sched_hear_def_id     	SCHED_HEAR_DEF_ID,
		defendant_on_case.defendant_on_case_id        	DEFENDANT_ON_CASE_ID,
		defendant_on_case.is_masked                   	IS_MASKED,
		defendant_on_case.masked_name 			MASKED_NAME,
		defendant.defendant_id				DEFENDANT_ID,
		defendant.first_name 				DEF_FIRST_NAME,
		defendant.middle_name 				DEF_MIDDLE_NAME,
		defendant.surname 				DEF_SURNAME,
		defendant.initials 				DEF_INITIALS,
		xcase.case_id					CASE_ID,
		xcase.case_number 				CASE_NUMBER,
		xcase.case_type					CASE_TYPE,
		xcase.case_sub_type				CASE_SUB_TYPE,
		sh_leg_rep.sh_leg_rep_id			SH_LEG_REP_ID,
		sh_leg_rep.legal_role				LEGAL_ROLE,
		legal_rep.ref_legal_rep_id			REF_LEGAL_REP_ID,
		legal_rep.first_name 				LEG_REP_FIRST_NAME,
		legal_rep.middle_name 				LEG_REP_MIDDLE_NAME,
		legal_rep.surname 				LEG_REP_SURNAME,
		legal_rep.title 				LEG_REP_TITLE,
		legal_rep.initials 				LEG_REP_INITIALS,
		legal_rep.legal_rep_type			LEGAL_REP_TYPE,
		advocate.ref_advocate_id			REF_ADVOCATE_ID,
		chamber.ref_chamber_id				REF_CHAMBER_ID,
		chamber.firm_name 				CHAMBER_FIRM_NAME,
		adv_address.address_id 				ADV_ADDRESS_ID,
		adv_address.address_1 				ADV_ADDRESS_1,
		adv_address.address_2 				ADV_ADDRESS_2,
		adv_address.address_3 				ADV_ADDRESS_3,
		adv_address.address_4  				ADV_ADDRESS_4,
		adv_address.town  				ADV_TOWN,
		adv_address.county 				ADV_COUNTY,
		adv_address.country 				ADV_COUNTRY,
		adv_address.postcode 				ADV_POSTCODE,
		solicitor.solicitor_id				SOLICITOR_ID,
		solfirm.ref_solicitor_firm_id			REF_SOLICITOR_FIRM_ID,
		solfirm.solicitor_firm_name			SOLICITOR_FIRM_NAME,
		sol_address.address_id 				SOL_ADDRESS_ID,
		sol_address.address_1  				SOL_ADDRESS_1,
		sol_address.address_2  				SOL_ADDRESS_2,
		sol_address.address_3  				SOL_ADDRESS_3,
		sol_address.address_4  				SOL_ADDRESS_4,
		sol_address.town  				SOL_TOWN,
		sol_address.county  				SOL_COUNTY,
		sol_address.country  				SOL_COUNTRY,
		sol_address.postcode 				SOL_POSTCODE
             FROM
		XHB_HEARING_LIST             HEARING_LIST,
		XHB_SITTING                  SITTING,
		XHB_COURT_ROOM               COURT_ROOM,
		XHB_SCHEDULED_HEARING        SCHEDULED_HEARING,
		XHB_HEARING                  HEARING,
		XHB_REF_HEARING_TYPE         REF_HEARING_TYPE,
		XHB_SCHED_HEARING_ATTENDEE   SH_ATTENDEE,
		XHB_SH_STAFF                 SH_STAFF,
		XHB_SCHED_HEARING_DEFENDANT  SCHED_HEARING_DEFENDANT,
		XHB_DEFENDANT_ON_CASE        DEFENDANT_ON_CASE,
		XHB_DEFENDANT                DEFENDANT,
		XHB_CASE                     XCASE,
		XHB_SH_LEG_REP               SH_LEG_REP,
		XHB_REF_LEGAL_REPRESENTATIVE LEGAL_REP,
		XHB_REF_ADVOCATE             ADVOCATE,
		XHB_REF_CHAMBER              CHAMBER,
		XHB_REF_SOLICITOR            SOLICITOR,
		XHB_REF_SOLICITOR_FIRM       SOLFIRM,
		XHB_ADDRESS                  ADV_ADDRESS,
		XHB_ADDRESS                  SOL_ADDRESS
             WHERE
		( hearing_list.list_id = sitting.list_id ) AND
		( sitting.court_room_id = court_room.court_room_id ) AND
		( scheduled_hearing.sitting_id = sitting.sitting_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) AND
		( scheduled_hearing.hearing_id = hearing.hearing_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) AND
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) AND
		( hearing.case_id = xcase.case_id ) AND
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) AND
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) AND
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_leg_rep.scheduled_hearing_id(+) AND sh_leg_rep.sched_hear_def_id IS NULL) AND
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) AND
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) AND
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) AND
		( chamber.address_id = adv_address.address_id(+) ) AND
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) AND
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) AND
		( solfirm.address_id = sol_address.address_id(+) );

CREATE OR REPLACE VIEW XHB_COUNSEL_FACILITIES_SHDID_V
AS
SELECT DISTINCT
		sitting.court_room_id                         	COURT_ROOM_ID,
		sitting.is_floating                           	IS_FLOATING,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME,
		court_room.court_room_name                    	COURT_ROOM_NAME,
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME,
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID,
		scheduled_hearing.original_time               	SH_ORIGINAL_TIME,
		scheduled_hearing.not_before_time             	SH_NOT_BEFORE_TIME,
		sched_hearing_defendant.sched_hear_def_id     	SCHED_HEAR_DEF_ID,
		defendant_on_case.defendant_on_case_id        	DEFENDANT_ON_CASE_ID,
		defendant_on_case.is_masked                   	IS_MASKED,
		defendant_on_case.masked_name 			MASKED_NAME,
		defendant.defendant_id				DEFENDANT_ID,
		defendant.first_name 				DEF_FIRST_NAME,
		defendant.middle_name 				DEF_MIDDLE_NAME,
		defendant.surname 				DEF_SURNAME,
		defendant.initials 				DEF_INITIALS,
		xcase.case_id					CASE_ID,
		xcase.case_number 				CASE_NUMBER,
		xcase.case_type					CASE_TYPE,
		xcase.case_sub_type				CASE_SUB_TYPE,
		sh_leg_rep.sh_leg_rep_id			SH_LEG_REP_ID,
		sh_leg_rep.legal_role				LEGAL_ROLE,
		legal_rep.ref_legal_rep_id			REF_LEGAL_REP_ID,
		legal_rep.first_name 				LEG_REP_FIRST_NAME,
		legal_rep.middle_name 				LEG_REP_MIDDLE_NAME,
		legal_rep.surname 				LEG_REP_SURNAME,
		legal_rep.title 				LEG_REP_TITLE,
		legal_rep.initials 				LEG_REP_INITIALS,
		legal_rep.legal_rep_type			LEGAL_REP_TYPE,
		advocate.ref_advocate_id			REF_ADVOCATE_ID,
		chamber.ref_chamber_id				REF_CHAMBER_ID,
		chamber.firm_name 				CHAMBER_FIRM_NAME,
		adv_address.address_id 				ADV_ADDRESS_ID,
		adv_address.address_1 				ADV_ADDRESS_1,
		adv_address.address_2 				ADV_ADDRESS_2,
		adv_address.address_3 				ADV_ADDRESS_3,
		adv_address.address_4  				ADV_ADDRESS_4,
		adv_address.town  				ADV_TOWN,
		adv_address.county 				ADV_COUNTY,
		adv_address.country 				ADV_COUNTRY,
		adv_address.postcode 				ADV_POSTCODE,
		solicitor.solicitor_id				SOLICITOR_ID,
		solfirm.ref_solicitor_firm_id			REF_SOLICITOR_FIRM_ID,
		solfirm.solicitor_firm_name			SOLICITOR_FIRM_NAME,
		sol_address.address_id 				SOL_ADDRESS_ID,
		sol_address.address_1  				SOL_ADDRESS_1,
		sol_address.address_2  				SOL_ADDRESS_2,
		sol_address.address_3  				SOL_ADDRESS_3,
		sol_address.address_4  				SOL_ADDRESS_4,
		sol_address.town  				SOL_TOWN,
		sol_address.county  				SOL_COUNTY,
		sol_address.country  				SOL_COUNTRY,
		sol_address.postcode 				SOL_POSTCODE
             FROM
		XHB_HEARING_LIST             HEARING_LIST,
		XHB_SITTING                  SITTING,
		XHB_COURT_ROOM               COURT_ROOM,
		XHB_SCHEDULED_HEARING        SCHEDULED_HEARING,
		XHB_HEARING                  HEARING,
		XHB_REF_HEARING_TYPE         REF_HEARING_TYPE,
		XHB_SCHED_HEARING_ATTENDEE   SH_ATTENDEE,
		XHB_SH_STAFF                 SH_STAFF,
		XHB_SCHED_HEARING_DEFENDANT  SCHED_HEARING_DEFENDANT,
		XHB_DEFENDANT_ON_CASE        DEFENDANT_ON_CASE,
		XHB_DEFENDANT                DEFENDANT,
		XHB_CASE                     XCASE,
		XHB_SH_LEG_REP               SH_LEG_REP,
		XHB_REF_LEGAL_REPRESENTATIVE LEGAL_REP,
		XHB_REF_ADVOCATE             ADVOCATE,
		XHB_REF_CHAMBER              CHAMBER,
		XHB_REF_SOLICITOR            SOLICITOR,
		XHB_REF_SOLICITOR_FIRM       SOLFIRM,
		XHB_ADDRESS                  ADV_ADDRESS,
		XHB_ADDRESS                  SOL_ADDRESS
             WHERE
		( hearing_list.list_id = sitting.list_id ) AND
		( sitting.court_room_id = court_room.court_room_id ) AND
		( scheduled_hearing.sitting_id = sitting.sitting_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) AND
		( scheduled_hearing.hearing_id = hearing.hearing_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) AND
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) AND
		( hearing.case_id = xcase.case_id ) AND
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) AND
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) AND
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) AND
		( SCHED_HEARING_DEFENDANT.sched_hear_def_id = sh_leg_rep.sched_hear_def_id(+)) AND
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) AND
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) AND
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) AND
		( chamber.address_id = adv_address.address_id(+) ) AND
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) AND
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) AND
		( solfirm.address_id = sol_address.address_id(+) );

create  or
replace package body
counselfacilities  as
    procedure get_counsel_sign_in(
	p_counsel_cursor  in out counsel_type,        
	p_court_id        in     number,
        p_startdate       in     date,
        p_court_room_id   in     number
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_SH_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate )
		and court_room_id = any 
			(select court_room_id from xhb_sitting minus select court_room_id from xhb_sitting where court_room_id != p_court_room_id)
	     union
	     select * 
                from XHB_COUNSEL_FACILITIES_SHDID_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate )
		and court_room_id = any 
			(select court_room_id from xhb_sitting minus select court_room_id from xhb_sitting where court_room_id != p_court_room_id) ;
            
    end get_counsel_sign_in;
    
    
    procedure search_counsel(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_SH_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( sh_leg_rep_id IS NOT NULL ) and 
		( UPPER( NVL( LEG_REP_FIRST_NAME, '%') ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( LEG_REP_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) )
             union
             select * 
                from XHB_COUNSEL_FACILITIES_SHDID_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( sh_leg_rep_id IS NOT NULL ) and 
		( UPPER( NVL( LEG_REP_FIRST_NAME, '%') ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( LEG_REP_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) ) ;
	     
            
    end search_counsel;
    
    
    procedure search_defendants(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_SH_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( UPPER( NVL( DEF_FIRST_NAME, '%' ) ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( DEF_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) )
             union
             select * 
                from XHB_COUNSEL_FACILITIES_SHDID_V
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( UPPER( NVL( DEF_FIRST_NAME, '%' ) ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( DEF_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) ) ;
	     
            
    end search_defendants;
    
    
    
end counselfacilities;
/
show errors

create or replace trigger XHB_CASE_I_IMP_EXP
  AFTER UPDATE ON XHB_CASE
  FOR EACH ROW
  
BEGIN

  IF (:NEW.EXPORT_CHARGES = 'R') THEN

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE,
                                         STATUS_CODE,
                                         CASE_ID,
                                         COURT_ID)
    VALUES ('CI',
            'R',
            :OLD.CASE_ID,
            :OLD.COURT_ID);

  END IF;

  IF (:NEW.RESULTS_VERIFIED = 'R') THEN

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE,
                                         STATUS_CODE,
                                         CASE_ID,
                                         COURT_ID)
    VALUES ('R',
            'R',
            :OLD.CASE_ID,
            :OLD.COURT_ID);

  END IF;

  IF (:NEW.IND_CHANGE_STATUS= 'R') THEN

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE,
                                         STATUS_CODE,
                                         CASE_ID,
                                         COURT_ID)
    VALUES ('IC',
            'R',
            :OLD.CASE_ID,
            :OLD.COURT_ID);

  END IF;

END;
/

create or replace trigger XHB_JOINDER_IMP_EXP
  AFTER UPDATE ON XHB_JOINDER_XML
  FOR EACH ROW

DECLARE

  l_defCaseId NUMBER(8) := NULL;
  l_caseId NUMBER(8) := NULL;
  l_courtId NUMBER(8) := NULL;
  
BEGIN


  IF (:NEW.STATUS = 'R') THEN

    SELECT CASE_ID
    INTO   l_caseId
    FROM  XHB_DEFENDANT_ON_CASE DC,
          XHB_JOINDER_DEFENDANT_ON_CASE JD
    WHERE JD.JOINDER_ID = :NEW.JOINDER_ID 
    AND   DC.DEFENDANT_ON_CASE_ID = JD.DEFENDANT_ON_CASE_ID_1;

    SELECT COURT_ID
    INTO   l_courtId 
    FROM   XHB_CASE C,
           XHB_DEFENDANT_ON_CASE DC,
           XHB_JOINDER_DEFENDANT_ON_CASE JD
    WHERE  JD.JOINDER_ID = :NEW.JOINDER_ID
    AND    DC.DEFENDANT_ON_CASE_ID = JD.DEFENDANT_ON_CASE_ID_1
    AND    DC.CASE_ID = C.CASE_ID;

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('CI','R', l_caseId, l_courtId);
  END IF;

END;
/

create or replace trigger XHB_SKELETON_IMP_EXP
  AFTER UPDATE ON XHB_SKELETON_SCHEDULE
  FOR EACH ROW

DECLARE  
  l_courtId NUMBER(8) := NULL;
  l_codeid  NUMBER(8) := NULL;

BEGIN
/*  SELECT skeleton_delivery_status_id INTO l_codeid FROM XHB_SKELETON_DELIVERY_STATUS
   WHERE CODE = 'READY';
*/  
  IF (:NEW.skeleton_delivery_status_id = 2) THEN

    SELECT COURT_ID
    INTO   l_courtId
    FROM   XHB_CASE C
    WHERE  C.CASE_ID = :NEW.CASE_ID;

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('SS','R', :OLD.CASE_ID, l_courtId);
  END IF;

END;
/

create or replace trigger XHB_REF_DATA_IMP_EXP
  AFTER UPDATE ON XHB_CREST_IMPORT
  FOR EACH ROW
  
BEGIN

  IF (:NEW.STATUS = 'R') THEN
    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, COURT_ID)
    VALUES (:OLD.IMPORT_TYPE,'R',:OLD.COURT_ID);
  END IF;

END;
/

create or replace trigger XHB_ORDER_IMP_EXP
  AFTER UPDATE ON XHB_ORDER
  FOR EACH ROW

DECLARE
 
  l_orderType VARCHAR2(5) := NULL;
  l_caseId NUMBER(8) := NULL;
  l_courtId NUMBER(8) := NULL;
  
BEGIN

  IF (:NEW.ORDER_DELIVERY_STATUS_ID = 2) THEN

    SELECT OTYPE.CODE
    INTO   l_orderType
    FROM   XHB_ORDER_TYPE OTYPE,
           XHB_ORDER_TEMPLATE OTEMP
    WHERE  :NEW.ORDER_TEMPLATE_ID = OTEMP.ORDER_TEMPLATE_ID
    AND    OTEMP.ORDER_TYPE_ID = OTYPE.ORDER_TYPE_ID;    

    SELECT CASE_ID
    INTO   l_caseId
    FROM   XHB_DEFENDANT_ON_CASE DC
    WHERE  DC.DEFENDANT_ON_CASE_ID = :NEW.DEFENDANT_ON_CASE_ID;

    SELECT COURT_ID
    INTO   l_courtId
    FROM   XHB_CASE C,
           XHB_DEFENDANT_ON_CASE DC
    WHERE  DC.DEFENDANT_ON_CASE_ID = :NEW.DEFENDANT_ON_CASE_ID
    AND    DC.CASE_ID = C.CASE_ID;

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID, DEFENDANT_ON_CASE_ID)
    VALUES (l_orderType,'R', l_caseId, l_courtId, :OLD.DEFENDANT_ON_CASE_ID);

  END IF;

END;
/
