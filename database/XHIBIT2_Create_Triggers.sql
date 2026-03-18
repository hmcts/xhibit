SET TERM OFF
/*
 * Filename:    XHIBIT2_Create_Triggers.sql
 *
 * Author:      Nick Sawyer
 *
 * Description: Creates triggers for INSERT and UPDATE/DELETE on most of the XHB_
 *              tables.
 *
 * Version Information:
 *
 * Revision    Author                 Notes
 *
 * 0.1         Nick Sawyer            Initial revision
 *
 */
SET TERM ON

CREATE OR REPLACE TRIGGER MTRG_MERC_REF_STOR_BIR_TR
  BEFORE INSERT
  ON MTBL_MERC_REFRESH_STORAGE
  FOR EACH ROW

BEGIN

  IF :NEW.MERC_REFRESH_STORAGE_ID IS NULL THEN

    SELECT MSEQ_MERC_REFRESH_STORAGE_SEQ.NEXTVAL
    INTO   :NEW.MERC_REFRESH_STORAGE_ID
    FROM   DUAL;

  END IF;

END;
/

CREATE OR REPLACE TRIGGER TMP_ERROR_LOG_BIR_TR
  BEFORE INSERT
  ON TMP_ERROR_LOG
  FOR EACH ROW

BEGIN

  SELECT TMP_ERROR_LOG_SEQ.NEXTVAL
  INTO   :NEW.SEQ_NO
  FROM   DUAL;

END;
/

CREATE OR REPLACE TRIGGER XHB_ADDRESS_BIR_TR
  BEFORE INSERT
  ON XHB_ADDRESS
  FOR EACH ROW

BEGIN

  IF :NEW.ADDRESS_ID IS NULL THEN

    SELECT XHB_ADDRESS_SEQ.NEXTVAL
    INTO   :NEW.ADDRESS_ID
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

CREATE OR REPLACE TRIGGER XHB_ADDRESS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ADDRESS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ADDRESS') = 1) THEN

    INSERT INTO AUD_ADDRESS 
    VALUES (:old.ADDRESS_ID, 
            :old.ADDRESS_1, 
            :old.ADDRESS_2, 
            :old.ADDRESS_3, 
            :old.ADDRESS_4, 
            :old.TOWN, 
            :old.COUNTY, 
            :old.POSTCODE, 
            :old.COUNTRY, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_BREACH_BIR_TR
  BEFORE INSERT
  ON XHB_BREACH
  FOR EACH ROW

BEGIN

  IF :NEW.BREACH_ID IS NULL THEN

    SELECT XHB_BREACH_SEQ.NEXTVAL
    INTO   :NEW.BREACH_ID
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

CREATE OR REPLACE TRIGGER XHB_BREACH_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_BREACH
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_BREACH') = 1) THEN

    INSERT INTO AUD_BREACH 
    VALUES (:old.BREACH_ID, 
            :old.ORIGINAL_SENTENCE, 
            :old.ORIGINAL_SENTENCE_DATE, 
            :old.ORIGINAL_COURT_TYPE, 
            :old.DATE_PUT, 
            :old.BREACH_TYPE, 
            :old.BRING_BACK, 
            :old.CHARGE_ID, 
            :old.REF_COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CASE_BIR_TR
  BEFORE INSERT
  ON XHB_CASE
  FOR EACH ROW

BEGIN

  IF :NEW.CASE_ID IS NULL THEN

    SELECT XHB_CASE_SEQ.NEXTVAL
    INTO   :NEW.CASE_ID
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

CREATE OR REPLACE TRIGGER XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE
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

    IF :NEW.CASE_ID = :OLD.CASE_ID AND
       :NEW.COURT_ID = :OLD.COURT_ID AND
      (:NEW.CASE_NUMBER = :OLD.CASE_NUMBER OR
       (:NEW.CASE_NUMBER IS NULL AND :OLD.CASE_NUMBER IS NULL)) AND
      (:NEW.CASE_TYPE = :OLD.CASE_TYPE OR
       (:NEW.CASE_TYPE IS NULL AND :OLD.CASE_TYPE IS NULL)) AND
      (:NEW.MAG_CONVICTION_DATE = :OLD.MAG_CONVICTION_DATE OR
       (:NEW.MAG_CONVICTION_DATE IS NULL AND :OLD.MAG_CONVICTION_DATE IS NULL)) AND
      (:NEW.CASE_SUB_TYPE = :OLD.CASE_SUB_TYPE OR
       (:NEW.CASE_SUB_TYPE IS NULL AND :OLD.CASE_SUB_TYPE IS NULL)) AND
      (:NEW.CASE_TITLE = :OLD.CASE_TITLE OR
       (:NEW.CASE_TITLE IS NULL AND :OLD.CASE_TITLE IS NULL)) AND
      (:NEW.CASE_DESCRIPTION = :OLD.CASE_DESCRIPTION OR
       (:NEW.CASE_DESCRIPTION IS NULL AND :OLD.CASE_DESCRIPTION IS NULL)) AND
      (:NEW.LINKED_CASE_ID = :OLD.LINKED_CASE_ID OR
       (:NEW.LINKED_CASE_ID IS NULL AND :OLD.LINKED_CASE_ID IS NULL)) AND
      (:NEW.BAIL_MAG_CODE = :OLD.BAIL_MAG_CODE OR
       (:NEW.BAIL_MAG_CODE IS NULL AND :OLD.BAIL_MAG_CODE IS NULL)) AND
      (:NEW.REF_COURT_ID = :OLD.REF_COURT_ID OR
       (:NEW.REF_COURT_ID IS NULL AND :OLD.REF_COURT_ID IS NULL)) AND
      (:NEW.SEVERED_IND = :OLD.SEVERED_IND OR
       (:NEW.SEVERED_IND IS NULL AND :OLD.SEVERED_IND IS NULL)) AND
      (:NEW.INDICT_RESP = :OLD.INDICT_RESP OR
       (:NEW.INDICT_RESP IS NULL AND :OLD.INDICT_RESP IS NULL)) AND
      (:NEW.DATE_IND_REC = :OLD.DATE_IND_REC OR
       (:NEW.DATE_IND_REC IS NULL AND :OLD.DATE_IND_REC IS NULL)) AND
      (:NEW.PROS_AGENCY_REFERENCE = :OLD.PROS_AGENCY_REFERENCE OR
       (:NEW.PROS_AGENCY_REFERENCE IS NULL AND :OLD.PROS_AGENCY_REFERENCE IS NULL)) AND
      (:NEW.CASE_CLASS = :OLD.CASE_CLASS OR
       (:NEW.CASE_CLASS IS NULL AND :OLD.CASE_CLASS IS NULL)) AND
      (:NEW.JUDGE_REASON_FOR_APPEAL = :OLD.JUDGE_REASON_FOR_APPEAL OR
       (:NEW.JUDGE_REASON_FOR_APPEAL IS NULL AND :OLD.JUDGE_REASON_FOR_APPEAL IS NULL)) AND
      (:NEW.RESULTS_VERIFIED = :OLD.RESULTS_VERIFIED OR
       (:NEW.RESULTS_VERIFIED IS NULL AND :OLD.RESULTS_VERIFIED IS NULL)) AND
      (:NEW.LENGTH_TAPE = :OLD.LENGTH_TAPE OR
       (:NEW.LENGTH_TAPE IS NULL AND :OLD.LENGTH_TAPE IS NULL)) AND
      (:NEW.NO_PAGE_PROS_EVIDENCE = :OLD.NO_PAGE_PROS_EVIDENCE OR
       (:NEW.NO_PAGE_PROS_EVIDENCE IS NULL AND :OLD.NO_PAGE_PROS_EVIDENCE IS NULL)) AND
      (:NEW.NO_PROS_WITNESS = :OLD.NO_PROS_WITNESS OR
       (:NEW.NO_PROS_WITNESS IS NULL AND :OLD.NO_PROS_WITNESS IS NULL)) AND
      (:NEW.EST_PDH_TRIAL_LENGTH = :OLD.EST_PDH_TRIAL_LENGTH OR
       (:NEW.EST_PDH_TRIAL_LENGTH IS NULL AND :OLD.EST_PDH_TRIAL_LENGTH IS NULL)) AND
      (:NEW.INDICTMENT_INFO_1 = :OLD.INDICTMENT_INFO_1 OR
       (:NEW.INDICTMENT_INFO_1 IS NULL AND :OLD.INDICTMENT_INFO_1 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_2 = :OLD.INDICTMENT_INFO_2 OR
       (:NEW.INDICTMENT_INFO_2 IS NULL AND :OLD.INDICTMENT_INFO_2 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_3 = :OLD.INDICTMENT_INFO_3 OR
       (:NEW.INDICTMENT_INFO_3 IS NULL AND :OLD.INDICTMENT_INFO_3 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_4 = :OLD.INDICTMENT_INFO_4 OR
       (:NEW.INDICTMENT_INFO_4 IS NULL AND :OLD.INDICTMENT_INFO_4 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_5 = :OLD.INDICTMENT_INFO_5 OR
       (:NEW.INDICTMENT_INFO_5 IS NULL AND :OLD.INDICTMENT_INFO_5 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_6 = :OLD.INDICTMENT_INFO_6 OR
       (:NEW.INDICTMENT_INFO_6 IS NULL AND :OLD.INDICTMENT_INFO_6 IS NULL)) AND
      (:NEW.POLICE_OFFICER_ATTENDING = :OLD.POLICE_OFFICER_ATTENDING OR
       (:NEW.POLICE_OFFICER_ATTENDING IS NULL AND :OLD.POLICE_OFFICER_ATTENDING IS NULL)) AND
      (:NEW.CPS_CASE_WORKER = :OLD.CPS_CASE_WORKER OR
       (:NEW.CPS_CASE_WORKER IS NULL AND :OLD.CPS_CASE_WORKER IS NULL)) AND
      (:NEW.EXPORT_CHARGES = :OLD.EXPORT_CHARGES OR
       (:NEW.EXPORT_CHARGES IS NULL AND :OLD.EXPORT_CHARGES IS NULL)) AND
      (:NEW.MAGISTRATES_CASE_REF = :OLD.MAGISTRATES_CASE_REF OR
       (:NEW.MAGISTRATES_CASE_REF IS NULL AND :OLD.MAGISTRATES_CASE_REF IS NULL)) AND
      (:NEW.CLASS_CODE = :OLD.CLASS_CODE OR
       (:NEW.CLASS_CODE IS NULL AND :OLD.CLASS_CODE IS NULL)) AND
      (:NEW.OFFENCE_GROUP_UPDATE = :OLD.OFFENCE_GROUP_UPDATE OR
       (:NEW.OFFENCE_GROUP_UPDATE IS NULL AND :OLD.OFFENCE_GROUP_UPDATE IS NULL)) AND
      (:NEW.CCC_TRANS_TO_REF_COURT_ID = :OLD.CCC_TRANS_TO_REF_COURT_ID OR
       (:NEW.CCC_TRANS_TO_REF_COURT_ID IS NULL AND :OLD.CCC_TRANS_TO_REF_COURT_ID IS NULL)) AND
      (:NEW.RECEIPT_TYPE = :OLD.RECEIPT_TYPE OR
       (:NEW.RECEIPT_TYPE IS NULL AND :OLD.RECEIPT_TYPE IS NULL)) THEN

      -- Only the CHARGE_IMPORT_INDICATOR has changed so do not increase VERSION
      -- This will even come into this section if the CHARGE_IMPORT_INDICATOR column is
      -- updated to the same value with all others staying the same

      SELECT SYSDATE
      INTO   :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    ELSE

      -- Other fields have changes so increase VERSION

      SELECT :OLD.VERSION + 1,
             SYSDATE
      INTO   :NEW.VERSION,
             :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    END IF;

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
            :old.CLASS_CODE,
            :old.OFFENCE_GROUP_UPDATE,
            :old.CCC_TRANS_TO_REF_COURT_ID,
            :old.RECEIPT_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CASE_APP_REASON_BIR_TR
  BEFORE INSERT
  ON XHB_CASE_APP_REASON
  FOR EACH ROW

BEGIN

  IF :NEW.CASE_APP_REASON_ID IS NULL THEN

    SELECT XHB_CASE_APP_REASON_SEQ.NEXTVAL
    INTO   :NEW.CASE_APP_REASON_ID
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

CREATE OR REPLACE TRIGGER XHB_CASE_APP_REASON_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE_APP_REASON
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_APP_REASON') = 1) THEN

    INSERT INTO AUD_CASE_APP_REASON
    VALUES (:old.CASE_APP_REASON_ID,
            :old.CAR_ID,
            :old.APP_REASON,
            :old.CASE_ID,
            :old.OBS_IND,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CASEPROSECUTORAGENC_BIR_TR
  BEFORE INSERT
  ON XHB_CASE_PROSECUTOR_AGENCY
  FOR EACH ROW

BEGIN

  IF :NEW.CASE_PROS_AGENCY_ID IS NULL THEN

    SELECT XHB_CASE_PROSECTOR_AGENCY_SEQ.NEXTVAL
    INTO   :NEW.CASE_PROS_AGENCY_ID
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

CREATE OR REPLACE TRIGGER XHB_CASEPROSECUTORAGENC_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE_PROSECUTOR_AGENCY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_PROSECUTOR_AGENCY') = 1) THEN

    INSERT INTO AUD_CASE_PROSECUTOR_AGENCY 
    VALUES (:old.CASE_PROS_AGENCY_ID, 
            :old.PROSECUTOR_TYPE, 
            :old.CASE_ID, 
            :old.REF_PROSECUTOR_AGENCY_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CASE_REFERENCE_BIR_TR
  BEFORE INSERT
  ON XHB_CASE_REFERENCE
  FOR EACH ROW

BEGIN

  IF :NEW.CASE_REFERENCE_ID IS NULL THEN

    SELECT XHB_CASE_REFERENCE_SEQ.NEXTVAL
    INTO   :NEW.CASE_REFERENCE_ID
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

CREATE OR REPLACE TRIGGER XHB_CASE_REFERENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE_REFERENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_REFERENCE') = 1) THEN

    INSERT INTO AUD_CASE_REFERENCE 
    VALUES (:old.CASE_REFERENCE_ID, 
            :old.REPORTING_RESTRICTIONS, 
            :old.CASE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CC_INFO_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CC_INFO
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CC_INFO') = 1) THEN

    INSERT INTO AUD_CC_INFO 
    VALUES (:old.CC_INFO_ID, 
            :old.CC_INFO_TEXT, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CC_INFO_BIR_TR
  BEFORE INSERT
  ON XHB_CC_INFO
  FOR EACH ROW

BEGIN

  IF :NEW.CC_INFO_ID IS NULL THEN

    SELECT XHB_CC_INFO_SEQ.NEXTVAL
    INTO  :NEW.CC_INFO_ID
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

CREATE OR REPLACE TRIGGER XHB_CHARGE_BIR_TR
  BEFORE INSERT
  ON XHB_CHARGE
  FOR EACH ROW

BEGIN

  IF :NEW.CHARGE_ID IS NULL THEN

    SELECT XHB_CHARGE_SEQ.NEXTVAL
    INTO   :NEW.CHARGE_ID
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

CREATE OR REPLACE TRIGGER XHB_CHARGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CHARGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CHARGE') = 1) THEN

    INSERT INTO AUD_CHARGE 
    VALUES (:old.CHARGE_ID, 
            :old.CHARGE_TYPE, 
            :old.PROS_PAPER_SERVED_DATE, 
            :old.CREST_CHARGE_ID, 
            :old.CREST_CHARGE_SEQ_NO, 
            :old.REF_SYSTEM_CODE_ID, 
            :old.CASE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.IND_SIGNED_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CHARGE_DIFFERENCES_BIR_TR
  BEFORE INSERT
  ON XHB_CHARGE_DIFFERENCES
  FOR EACH ROW

BEGIN

  IF :NEW.CHARGE_DIFF_ID IS NULL THEN

    SELECT XHB_CHARGE_DIFFERENCES_SEQ.NEXTVAL
    INTO  :NEW.CHARGE_DIFF_ID
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

CREATE OR REPLACE TRIGGER XHB_CHARGE_DIFFERENCES_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CHARGE_DIFFERENCES
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CHARGE_DIFFERENCES') = 1) THEN

    INSERT INTO AUD_CHARGE_DIFFERENCES 
    VALUES (:old.CHARGE_DIFF_ID, 
            :old.REPORT, 
            :old.DIFF_TIME, 
            :old.CASE_ID, 
            :old.COURT_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CONFIGUREDPUBLICNOT_BIR_TR
  BEFORE INSERT
  ON XHB_CONFIGURED_PUBLIC_NOTICE
  FOR EACH ROW

BEGIN

  IF :NEW.CONFIGURED_PUBLIC_NOTICE_ID IS NULL THEN

    SELECT XHB_CONFIGURED_PUBLIC_NOT_SEQ.NEXTVAL
    INTO   :NEW.CONFIGURED_PUBLIC_NOTICE_ID
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

CREATE OR REPLACE TRIGGER XHB_CONFIGUREDPUBLICNOT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CONFIGURED_PUBLIC_NOTICE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONFIGURED_PUBLIC_NOTICE') = 1) THEN

    INSERT INTO AUD_CONFIGURED_PUBLIC_NOTICE 
    VALUES (:old.CONFIGURED_PUBLIC_NOTICE_ID, 
            :old.IS_ACTIVE, 
            :old.COURT_ROOM_ID, 
            :old.PUBLIC_NOTICE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CONTACTDETAIL_BIR_TR
  BEFORE INSERT
  ON XHB_CONTACT_DETAIL
  FOR EACH ROW

BEGIN

  IF :NEW.CONTACT_ID IS NULL THEN

    SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL
    INTO   :NEW.CONTACT_ID
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

CREATE OR REPLACE TRIGGER XHB_CONTACTDETAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CONTACT_DETAIL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONTACT_DETAIL') = 1) THEN

    INSERT INTO AUD_CONTACT_DETAIL 
    VALUES (:old.CONTACT_ID, 
            :old.CONTACT_TYPE, 
            :old.CONTACT_VALUE, 
            :old.EMAIL_FORMAT, 
            :old.PAGER_NET, 
            :old.ADDRESS_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURT_BIR_TR
  BEFORE INSERT
  ON XHB_COURT
  FOR EACH ROW

BEGIN

  IF :NEW.COURT_ID IS NULL THEN

    SELECT XHB_COURT_SEQ.NEXTVAL
    INTO   :NEW.COURT_ID
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

CREATE OR REPLACE TRIGGER XHB_COURT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT
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

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_CATEGORY_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_LOG_CATEGORY
  FOR EACH ROW

BEGIN

  IF :NEW.CATEGORY_ID IS NULL THEN

    SELECT XHB_COURT_LOG_CATEGORY_SEQ.NEXTVAL
    INTO   :NEW.CATEGORY_ID
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

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_CATEGORY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_CATEGORY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_CATEGORY') = 1) THEN

    INSERT INTO AUD_COURT_LOG_CATEGORY 
    VALUES (:old.CATEGORY_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.CATEGORY_DESC_ID, 
            :old.EVENT_DESC_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_CAT_DESC_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_LOG_CATEGORY_DESC
  FOR EACH ROW

BEGIN

  IF :NEW.CATEGORY_DESC_ID IS NULL THEN

    SELECT XHB_COURT_LOG_CAT_DESC_SEQ.NEXTVAL
    INTO   :NEW.CATEGORY_DESC_ID
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

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_CAT_DESC_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_CATEGORY_DESC
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_CATEGORY_DESC') = 1) THEN

    INSERT INTO AUD_COURT_LOG_CATEGORY_DESC 
    VALUES (:old.CATEGORY_DESC_ID, 
            :old.CATEGORY_DESCRIPTION, 
            :old.CATEGORY_TYPE, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_ENTRY_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_LOG_ENTRY
  FOR EACH ROW

BEGIN

  IF :NEW.ENTRY_ID IS NULL THEN

    SELECT XHB_COURT_LOG_ENTRY_SEQ.NEXTVAL
    INTO   :NEW.ENTRY_ID
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

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_ENTRY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_ENTRY
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
            :old.DEFENDANT_ON_OFFENCE_ID,
            :old.SCHEDULED_HEARING_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_EVENT_DSC_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_LOG_EVENT_DESC
  FOR EACH ROW

BEGIN

  IF :NEW.EVENT_DESC_ID IS NULL THEN

    SELECT XHB_COURT_LOG_EVENT_DESC_SEQ.NEXTVAL
    INTO   :NEW.EVENT_DESC_ID
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

CREATE OR REPLACE TRIGGER XHB_COURT_LOG_EVENT_DSC_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_LOG_EVENT_DESC
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_LOG_EVENT_DESC') = 1) THEN

    INSERT INTO AUD_COURT_LOG_EVENT_DESC 
    VALUES (:old.EVENT_DESC_ID, 
            :old.FLAGGED_EVENT, 
            :old.EDITABLE, 
            :old.SEND_TO_MERCATOR, 
            :old.UPDATE_LINKED_CASES, 
            :old.PUBLISH_TO_SUBSCRIBERS, 
            :old.CLEAR_PUBLIC_DISPLAYS, 
            :old.E_INFORM, 
            :old.PUBLIC_DISPLAY, 
            :old.LINKED_CASE_TEXT, 
            :old.EVENT_DESCRIPTION, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.EVENT_TYPE, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.PUBLIC_NOTICE, 
            :old.SHORT_DESCRIPTION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURTROOM_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_ROOM
  FOR EACH ROW

BEGIN

  IF :NEW.COURT_ROOM_ID IS NULL THEN

    SELECT XHB_COURT_ROOM_SEQ.NEXTVAL
    INTO   :NEW.COURT_ROOM_ID
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

CREATE OR REPLACE TRIGGER XHB_COURTROOM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_ROOM
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_ROOM') = 1) THEN

    INSERT INTO AUD_COURT_ROOM 
    VALUES (:old.COURT_ROOM_ID, 
            :old.COURT_ROOM_NAME, 
            :old.DESCRIPTION, 
            :old.CREST_COURT_ROOM_NO, 
            :old.COURT_SITE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.obs_ind, 
            :old.DISPLAY_NAME,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_COURTSITE_BIR_TR
  BEFORE INSERT
  ON XHB_COURT_SITE
  FOR EACH ROW

BEGIN

  IF :NEW.COURT_SITE_ID IS NULL THEN

    SELECT XHB_COURT_SITE_SEQ.NEXTVAL
    INTO   :NEW.COURT_SITE_ID
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

CREATE OR REPLACE TRIGGER XHB_COURTSITE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_SITE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURTSITE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_SITE') = 1) THEN

    INSERT INTO AUD_COURT_SITE 
    VALUES (:old.COURT_SITE_ID,
            :old.COURT_SITE_NAME,
            :old.COURT_SITE_CODE,
            :old.COURT_ID,
            :old.ADDRESS_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.obs_ind,
            :old.DISPLAY_NAME,
            :old.CREST_COURT_ID,
            :old.SHORT_NAME,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BIR_TR
  BEFORE INSERT
  ON XHB_CREST_IMPORT
  FOR EACH ROW

BEGIN

  IF :NEW.CREST_IMPORT_ID IS NULL THEN

    SELECT XHB_CREST_IMPORT_SEQ.NEXTVAL
    INTO   :NEW.CREST_IMPORT_ID
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

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CREST_IMPORT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CREST_IMPORT') = 1) THEN

    INSERT INTO AUD_CREST_IMPORT (CREST_IMPORT_ID,
                                  STATUS,
                                  COURT_ID,
                                  IMPORT_TYPE,
                                  LAST_UPDATE_DATE,
                                  CREATION_DATE,
                                  CREATED_BY,
                                  LAST_UPDATED_BY,
                                  VERSION,
                                  TIME_TO_RUN,
                                  MAX_RETRY,
                                  CURRENT_RETRY,
                                  INSERT_EVENT)
    VALUES                       (:OLD.CREST_IMPORT_ID,
                                  :OLD.STATUS,
                                  :OLD.COURT_ID,
                                  :OLD.IMPORT_TYPE,
                                  :OLD.LAST_UPDATE_DATE,
                                  :OLD.CREATION_DATE,
                                  :OLD.CREATED_BY,
                                  :OLD.LAST_UPDATED_BY,
                                  :OLD.VERSION,
                                  :OLD.TIME_TO_RUN,
                                  :OLD.MAX_RETRY,
                                  :OLD.CURRENT_RETRY,
                                  l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CR_LIVE_STATUS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CR_LIVE_STATUS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CR_LIVE_STATUS') = 1) THEN

    INSERT INTO AUD_CR_LIVE_STATUS 
    VALUES (:old.cr_live_status_id, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ROOM_ID, 
            :old.SCHEDULED_HEARING_ID, 
            :old.TIME_STATUS_SET, 
            :old.INTERNET_HELP_CODE, 
            :old.INTERNET_STATUS, 
            :old.PUBLIC_DISPLAY_STATUS,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_CR_LIVE_STATUS_BIR_TR
  BEFORE INSERT
  ON XHB_CR_LIVE_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.CR_LIVE_STATUS_ID IS NULL THEN

    SELECT XHB_CR_LIVE_STATUS_SEQ.NEXTVAL
    INTO   :NEW.CR_LIVE_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_DEF_HEARING_RECORD_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEF_HEARING_RECORD
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEF_HEARING_RECORD') = 1) THEN

    INSERT INTO AUD_DEF_HEARING_RECORD 
    VALUES (:old.HEARING_RECORD_ID, 
            :old.REF_ADJOURNMENT_ID, 
            :old.ADJOURNED_DATE, 
            :old.IS_ADJOURNED, 
            :old.COLLECT_MAGISTRATE_COURT_ID, 
            :old.START_DATE_NEW_BAIL_STATUS, 
            :old.NEW_BAIL_STATUS, 
            :old.DATE_BAIL_APPLICATION, 
            :old.SUBST_BAIL_APPLICATION, 
            :old.ORAL_EVIDENCE, 
            :old.RESULT_BAIL_APPLICATION, 
            :old.IS_HRA_APPLICATION, 
            :old.REF_DEF_HEARING_TYPE_ID, 
            :old.END_BAIL_STATUS, 
            :old.START_BAIL_STATUS, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.HEARING_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.hearing_dates_freetext_1, 
            :old.hearing_dates_freetext_2, 
            :old.hearing_dates_freetext_3,
            :old.HEARING_START_DATE,
            :old.HEARING_END_DATE,
            :old.LAST_CALCULATED_DURATION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEF_HEARING_RECORD_BIR_TR
  BEFORE INSERT
  ON XHB_DEF_HEARING_RECORD
  FOR EACH ROW

BEGIN

  IF :NEW.HEARING_RECORD_ID IS NULL THEN

    SELECT XHB_DEF_HEARING_RECORD_SEQ.NEXTVAL
    INTO   :NEW.HEARING_RECORD_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANT_BIR_TR
  BEFORE INSERT
  ON XHB_DEFENDANT
  FOR EACH ROW

BEGIN

  IF :NEW.DEFENDANT_ID IS NULL THEN

    SELECT XHB_DEFENDANT_SEQ.NEXTVAL
    INTO   :NEW.DEFENDANT_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANT_CHARGE_BIR_TR
  BEFORE INSERT
  ON XHB_DEFENDANT_CHARGE
  FOR EACH ROW

BEGIN

  IF :NEW.DEFENDANT_CHARGE_ID IS NULL THEN

    SELECT XHB_DEFENDANT_CHARGE_SEQ.NEXTVAL
    INTO   :NEW.DEFENDANT_CHARGE_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANT_CHARGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_CHARGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_CHARGE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_CHARGE 
    VALUES (:old.DEFENDANT_CHARGE_ID, 
            :old.CHARGE_ID, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.VERSION, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONCASE_BIR_TR
  BEFORE INSERT
  ON XHB_DEFENDANT_ON_CASE
  FOR EACH ROW
BEGIN

  IF :NEW.DEFENDANT_ON_CASE_ID IS NULL THEN

    SELECT XHB_DEFENDANT_ON_CASE_SEQ.NEXTVAL
    INTO   :NEW.DEFENDANT_ON_CASE_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_CASE
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
            :old.COLLECT_MAGISTRATE_COURT_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONOFFENCE_BIR_TR
  BEFORE INSERT
  ON XHB_DEFENDANT_ON_OFFENCE
  FOR EACH ROW

BEGIN

  IF :NEW.DEFENDANT_ON_OFFENCE_ID IS NULL THEN

    SELECT XHB_DEFENDANT_ON_OFFENCE_SEQ.NEXTVAL
    INTO   :NEW.DEFENDANT_ON_OFFENCE_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_OFFENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_OFFENCE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_OFFENCE 
    VALUES (:old.DEFENDANT_ON_OFFENCE_ID, 
            :old.APPEAL_AGAINST_TYPE, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.OFFENCE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.IS_STAYED,
            :old.CRN_ID,
            :old.VCO_FLAG,
            :old.VCO_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFENDANTREFERENCE_BIR_TR
  BEFORE INSERT
  ON XHB_DEFENDANT_REFERENCE
  FOR EACH ROW

BEGIN

  IF :NEW.DEF_REF_ID IS NULL THEN

    SELECT XHB_DEFENDANT_REFERENCE_SEQ.NEXTVAL
    INTO   :NEW.DEF_REF_ID
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

CREATE OR REPLACE TRIGGER XHB_DEFENDANTREFERENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_REFERENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_REFERENCE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_REFERENCE 
    VALUES (:old.DEF_REF_ID, 
            :old.REFERENCE_VALUE, 
            :old.REFERENCE_NAME, 
            :old.CATEGORY, 
            :old.DEFENDANT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFINITIVE_PUBLIC_N_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFINITIVE_PUBLIC_NOTICE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFINITIVE_PUBLIC_NOTICE') = 1) THEN

    INSERT INTO AUD_DEFINITIVE_PUBLIC_NOTICE 
    VALUES (:old.definitive_pn_id, 
            :old.definitive_pn_desc, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.priority,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DEFINITIVE_PUBLIC_N_BIR_TR
  BEFORE INSERT
  ON XHB_DEFINITIVE_PUBLIC_NOTICE
  FOR EACH ROW

BEGIN

  IF :NEW.DEFINITIVE_PN_ID IS NULL THEN

    SELECT XHB_DEFINITIVE_PUB_NOTICE_SEQ.NEXTVAL
    INTO   :NEW.DEFINITIVE_PN_ID
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

CREATE OR REPLACE TRIGGER XHB_DIRECTION_ATTEND_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DIRECTION_ATTEND
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTION_ATTEND') = 1) THEN

    INSERT INTO AUD_DIRECTION_ATTEND 
    VALUES (:old.direction_attend_id, 
            :old.DIRECTIONS_FOR_CASE_ID, 
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

CREATE OR REPLACE TRIGGER XHB_DIRECTION_ATTEND_BIR_TR
  BEFORE INSERT
  ON XHB_DIRECTION_ATTEND
  FOR EACH ROW

BEGIN

  IF :NEW.DIRECTION_ATTEND_ID IS NULL THEN

    SELECT XHB_DIRECTION_ATTEND_SEQ.NEXTVAL
    INTO   :NEW.DIRECTION_ATTEND_ID
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

CREATE OR REPLACE TRIGGER XHB_DIRECTIONS_FOR_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DIRECTIONS_FOR_CASE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTIONS_FOR_CASE') = 1) THEN

    INSERT INTO AUD_DIRECTIONS_FOR_CASE 
    VALUES (:old.DIRECTIONS_FOR_CASE_ID, 
            :old.FREETEXT, 
            :old.DATE_TIME, 
            :old.LIST_DATE, 
            :old.LIST_TYPE, 
            :old.LISTED_AS, 
            :old.DIRECTIONS_TEXT, 
            :old.TRIAL_TIME_UNIT, 
            :old.TRIAL_TIME_ESTIMATE, 
            :old.HAS_PANDD_FORM, 
            :old.CASE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DIRECTIONS_FOR_CASE_BIR_TR
  BEFORE INSERT
  ON XHB_DIRECTIONS_FOR_CASE
  FOR EACH ROW
BEGIN

  IF :NEW.DIRECTIONS_FOR_CASE_ID IS NULL THEN

    SELECT XHB_DIR_FOR_CASE_SEQ.NEXTVAL
    INTO   :NEW.DIRECTIONS_FOR_CASE_ID
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

CREATE OR REPLACE TRIGGER XHB_DIRECTIONS_FOR_DEF_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DIRECTIONS_FOR_DEFENDANT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DIRECTIONS_FOR_DEFENDANT') = 1) THEN

    INSERT INTO AUD_DIRECTIONS_FOR_DEFENDANT 
    VALUES (:old.DIRECTIONS_FOR_DEFENDANT_ID, 
            :old.FREETEXT, 
            :old.DATE_TIME, 
            :old.is_identified, 
            :old.TO_BE_FILED_BY, 
            :old.FILED_FORM_B, 
            :old.CERT_ATTENDANCE, 
            :old.NEW_BAIL_CONDITIONS, 
            :old.BAIL_STATUS, 
            :old.ARRAIGNED, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DIRECTIONS_FOR_DEF_BIR_TR
  BEFORE INSERT
  ON XHB_DIRECTIONS_FOR_DEFENDANT
  FOR EACH ROW

BEGIN

  IF :NEW.DIRECTIONS_FOR_DEFENDANT_ID IS NULL THEN

    SELECT XHB_DIR_FOR_DEFENDANT_SEQ.NEXTVAL
    INTO   :NEW.DIRECTIONS_FOR_DEFENDANT_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPLAY_BIR_TR
  BEFORE INSERT
  ON XHB_DISPLAY
  FOR EACH ROW

BEGIN

  IF :NEW.DISPLAY_ID IS NULL THEN

    SELECT XHB_DISPLAY_SEQ.NEXTVAL
    INTO   :NEW.DISPLAY_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPLAY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPLAY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY') = 1) THEN

    INSERT INTO AUD_DISPLAY
    VALUES (:OLD.DISPLAY_ID,
            :OLD.DISPLAY_TYPE_ID,
            :OLD.DISPLAY_LOCATION_ID,
            :OLD.ROTATION_SET_ID,
            :OLD.DESCRIPTION_CODE,
            :OLD.LOCALE,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            :OLD.SHOW_UNASSIGNED_YN,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPLAY_COURT_ROOM_BIR_TR
  BEFORE INSERT
  ON XHB_DISPLAY_COURT_ROOM
  FOR EACH ROW

DECLARE

  l_trig_event     VARCHAR2(1) := 'I';
  l_modifying_user VARCHAR2(30);

BEGIN

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY_COURT_ROOM') = 1) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   l_modifying_user
    FROM   DUAL;

    INSERT INTO AUD_DISPLAY_COURT_ROOM
    VALUES (:NEW.DISPLAY_ID,
            :NEW.COURT_ROOM_ID,
            l_modifying_user,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPLAY_COURT_ROOM_BDR_TR
  BEFORE DELETE
  ON XHB_DISPLAY_COURT_ROOM
  FOR EACH ROW

DECLARE

  l_trig_event     VARCHAR2(1) := 'D';
  l_modifying_user VARCHAR2(30);

BEGIN

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY_COURT_ROOM') = 1) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   l_modifying_user
    FROM   DUAL;

    INSERT INTO AUD_DISPLAY_COURT_ROOM
    VALUES (:OLD.DISPLAY_ID,
            :OLD.COURT_ROOM_ID,
            l_modifying_user,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPLAY_DOCUMENT_BIR_TR
  BEFORE INSERT
  ON XHB_DISPLAY_DOCUMENT
  FOR EACH ROW

BEGIN

  IF :NEW.DISPLAY_DOCUMENT_ID IS NULL THEN

    SELECT XHB_DISPLAY_DOCUMENT_SEQ.NEXTVAL
    INTO   :NEW.DISPLAY_DOCUMENT_ID
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
    VALUES (:OLD.DISPLAY_DOCUMENT_ID,
            :OLD.DESCRIPTION_CODE,
            :OLD.DEFAULT_PAGE_DELAY,
            :OLD.MULTIPLE_COURT_YN,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPLAY_LOCATION_BIR_TR
  BEFORE INSERT
  ON XHB_DISPLAY_LOCATION
  FOR EACH ROW

BEGIN

  IF :NEW.DISPLAY_LOCATION_ID IS NULL THEN

    SELECT XHB_DISPLAY_LOCATION_SEQ.NEXTVAL
    INTO   :NEW.DISPLAY_LOCATION_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPLAY_LOCATION_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPLAY_LOCATION
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY_LOCATION') = 1) THEN

    INSERT INTO AUD_DISPLAY_LOCATION
    VALUES (:OLD.DISPLAY_LOCATION_ID,
            :OLD.DESCRIPTION_CODE,
            :OLD.COURT_SITE_ID,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPLAY_TYPE_BIR_TR
  BEFORE INSERT
  ON XHB_DISPLAY_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.DISPLAY_TYPE_ID IS NULL THEN

    SELECT XHB_DISPLAY_TYPE_SEQ.NEXTVAL
    INTO   :NEW.DISPLAY_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPLAY_TYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPLAY_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPLAY_TYPE') = 1) THEN

    INSERT INTO AUD_DISPLAY_TYPE
    VALUES (:OLD.DISPLAY_TYPE_ID,
            :OLD.DESCRIPTION_CODE,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPOSAL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL') = 1) THEN

    INSERT INTO AUD_DISPOSAL 
    VALUES (:old.DISPOSAL_ID, 
            :old.DEF_ON_CASE_OR_OFFENCE, 
            :old.FREETEXT, 
            :old.DATE_TIME, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.REF_DISPOSAL_ID, 
            :old.VERSION, 
            :old.DEFENDANT_ON_OFFENCE_ID, 
            :old.category, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_BIR_TR
  BEFORE INSERT
  ON XHB_DISPOSAL
  FOR EACH ROW

BEGIN

  IF :NEW.DISPOSAL_ID IS NULL THEN

    SELECT XHB_DISPOSAL_SEQ.NEXTVAL
    INTO   :NEW.DISPOSAL_ID
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

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL2_BIR_TR
  BEFORE INSERT
  ON XHB_DISPOSAL2
  FOR EACH ROW

BEGIN

  IF :NEW.DISPOSAL2_ID IS NULL THEN

    SELECT XHB_DISPOSAL2_SEQ.NEXTVAL
    INTO   :NEW.DISPOSAL2_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPOSAL2_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPOSAL2
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL2') = 1) THEN

    INSERT INTO AUD_DISPOSAL2
    VALUES (:old.DISPOSAL2_ID,
            :old.REF_DISPOSAL_TYPE_ID,
            :old.DEFENDANT_ON_OFFENCE_ID,
            :old.DEFENDANT_ON_CASE_ID,
            :old.DIS_ID,
            :old.COURT_TYPE,
            :old.PSD_DISPOSAL2_ID,
            :old.OBS_IND,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSALDETAIL_BIR_TR
  BEFORE INSERT
  ON XHB_DISPOSAL_DETAIL
  FOR EACH ROW

BEGIN

  IF :NEW.DISPOSAL_DETAIL_ID IS NULL THEN

    SELECT XHB_DISPOSAL_DETAIL_SEQ.NEXTVAL
    INTO   :NEW.DISPOSAL_DETAIL_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPOSALDETAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPOSAL_DETAIL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL_DETAIL') = 1) THEN

    INSERT INTO AUD_DISPOSAL_DETAIL 
    VALUES (:old.DISPOSAL_DETAIL_ID, 
            :old.UNIT, 
            :old.AMOUNT, 
            :old.FREE_TEXT_DESC, 
            :old.OBS_IND, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.ORIGINAL_RESULT_ID, 
            :old.CREST_VALUE_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_LINE_BIR_TR
  BEFORE INSERT
  ON XHB_DISPOSAL_LINE
  FOR EACH ROW

BEGIN

  IF :NEW.DISPOSAL_LINE_ID IS NULL THEN

    SELECT XHB_DISPOSAL_LINE_SEQ.NEXTVAL
    INTO   :NEW.DISPOSAL_LINE_ID
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

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_LINE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPOSAL_LINE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL_LINE') = 1) THEN

    INSERT INTO AUD_DISPOSAL_LINE
    VALUES (:old.DISPOSAL_LINE_ID,
            :old.REF_DISPOSAL_LINE_ID,
            :old.DISPOSAL2_ID,
            :old.LINE_NUMBER,
            :old.DATA,
            :old.DEL_DATA,
            :old.DEL_G1,
            :old.DEL_G2,
            :old.OBS_IND,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_REFERENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DISPOSAL_REFERENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DISPOSAL_REFERENCE') = 1) THEN

    INSERT INTO AUD_DISPOSAL_REFERENCE 
    VALUES (:old.DIS_REF_ID, 
            :old.DISPOSAL_ID, 
            :old.REFERENCE_NAME, 
            :old.REFERENCE_VALUE, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DISPOSAL_REFERENCE_BIR_TR
  BEFORE INSERT
  ON XHB_DISPOSAL_REFERENCE
  FOR EACH ROW

BEGIN

  IF :NEW.DIS_REF_ID IS NULL THEN

    SELECT XHB_DISPOSAL_REFERENCE_SEQ.NEXTVAL
    INTO   :NEW.DIS_REF_ID
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

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_CONTROL_BUR_TR
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

    INSERT INTO AUD_DOCUMENT_CONTROL 
    VALUES (:old.doc_control_id, 
            :old.formatted_document, 
            :old.status, 
            :old.expiry_date, 
            :old.distribution_type, 
            :old.mime_type, 
            :old.document_type, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.formatting_id, 
            :old.COURT_ID, 
            :old.DISTRIBUTED_DATE, 
            :old.xml_document_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_CONTROL_BIR_TR
  BEFORE INSERT
  ON XHB_DOCUMENT_CONTROL
  FOR EACH ROW

BEGIN

  IF :NEW.DOC_CONTROL_ID IS NULL THEN

    SELECT XHB_DOCUMENT_CONTROL_SEQ.NEXTVAL
    INTO   :NEW.DOC_CONTROL_ID
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

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_DIST_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_DISTRIBUTION
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_DISTRIBUTION') = 1) THEN

    INSERT INTO AUD_DOCUMENT_DISTRIBUTION 
    VALUES (:old.doc_distribution_id, 
            :old.distribution_type, 
            :old.document_type, 
            :old.mime_type, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.recipient_id, 
            :old.wll_recipient_id, 
            :old.COURT_ID,
            :old.USE_PREF_DIST_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_DIST_BIR_TR
  BEFORE INSERT
  ON XHB_DOCUMENT_DISTRIBUTION
  FOR EACH ROW

BEGIN

  IF :NEW.DOC_DISTRIBUTION_ID IS NULL THEN

    SELECT XHB_DOCUMENT_DISTRIBUTION_SEQ.NEXTVAL
    INTO   :NEW.DOC_DISTRIBUTION_ID
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

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_RECIPIENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_RECIPIENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_RECIPIENT') = 1) THEN

    INSERT INTO AUD_DOCUMENT_RECIPIENT 
    VALUES (:old.document_recipient_id, 
            :old.doc_recipient_name, 
            :old.doc_recipient_fax, 
            :old.doc_recipient_email, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.doc_control_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_RECIPIENT_BIR_TR
  BEFORE INSERT
  ON XHB_DOCUMENT_RECIPIENT
  FOR EACH ROW

BEGIN

  IF :NEW.DOCUMENT_RECIPIENT_ID IS NULL THEN

    SELECT XHB_DOCUMENT_RECIPIENT_SEQ.NEXTVAL
    INTO   :NEW.DOCUMENT_RECIPIENT_ID
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

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_REPLY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_REPLY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_REPLY') = 1) THEN

    INSERT INTO AUD_DOCUMENT_REPLY 
    VALUES (:old.doc_reply_id, 
            :old.reply_name, 
            :old.reply_fax, 
            :old.reply_email, 
            :old.reply_address, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.COURT_ID, 
            :old.DOCUMENT_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_REPLY_BIR_TR
  BEFORE INSERT
  ON XHB_DOCUMENT_REPLY
  FOR EACH ROW

BEGIN

  IF :NEW.DOC_REPLY_ID IS NULL THEN

    SELECT XHB_DOCUMENT_REPLY_SEQ.NEXTVAL
    INTO   :NEW.DOC_REPLY_ID
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

CREATE OR REPLACE TRIGGER XHB_EMAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL') = 1) THEN

    INSERT INTO AUD_EMAIL 
    VALUES (:old.MAIL_ID, 
            :old.RECIPIENTS, 
            :old.CCRECIPIENTS, 
            :old.BCCRECIPIENTS, 
            :old.SUBJECT, 
            :old.SENDER, 
            :old.MIME_BODY, 
            :old.STATUS, 
            :old.CREATION_TIME, 
            :old.MUSTRECEIVE, 
            :old.REJECTTIME, 
            :old.REASON, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.EMAIL_TO, 
            :old.COMPANY, 
            :old.ATTACHMENT,
            :old.MIME_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_EMAIL_BIR_TR
  BEFORE INSERT
  ON XHB_EMAIL
  FOR EACH ROW

BEGIN

  IF :NEW.MAIL_ID IS NULL THEN

    SELECT XHB_EMAIL_SEQ.NEXTVAL
    INTO   :NEW.MAIL_ID
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

CREATE OR REPLACE TRIGGER XHB_ERROR_DEF_BIR_TR
  BEFORE INSERT
  ON XHB_ERROR_DEF
  FOR EACH ROW

BEGIN

  IF :NEW.ERROR_DEF_ID IS NULL THEN

    SELECT XHB_ERROR_DEF_SEQ.NEXTVAL
    INTO   :NEW.ERROR_DEF_ID
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

CREATE OR REPLACE TRIGGER XHB_ERROR_DEF_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ERROR_DEF
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ERROR_DEF') = 1) THEN

    INSERT INTO AUD_ERROR_DEF 
    VALUES (:old.ERROR_DEF_ID, 
            :old.DESCRIPTION, 
            :old.SEVERITY, 
            :old.ERROR_TYPE, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_EXPORTA_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EXPORTA
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EXPORTA') = 1) THEN

    INSERT INTO AUD_EXPORTA 
    VALUES (:old.EXPORT_A_ID, 
            :old.COURT_CLERK_EXPORT, 
            :old.STATUS_FLAG, 
            :old.LINKED_HEARING_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.HEARING_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_EXPORTA_BIR_TR
  BEFORE INSERT
  ON XHB_EXPORTA
  FOR EACH ROW

BEGIN

  IF :NEW.EXPORT_A_ID IS NULL THEN

    SELECT XHB_EXPORTA_SEQ.NEXTVAL
    INTO   :NEW.EXPORT_A_ID
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

CREATE OR REPLACE TRIGGER XHB_FORMATTING_BIR_TR
  BEFORE INSERT
  ON XHB_FORMATTING
  FOR EACH ROW

BEGIN

  IF :NEW.FORMATTING_ID IS NULL THEN

    SELECT XHB_FORMATTING_SEQ.NEXTVAL
    INTO   :NEW.FORMATTING_ID
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

CREATE OR REPLACE TRIGGER XHB_FORMATTING_AUR_TR
  AFTER UPDATE
  ON XHB_FORMATTING
  FOR EACH ROW

DECLARE

BEGIN

  IF :NEW.FORMAT_STATUS = 'FE' OR
     :NEW.FORMAT_STATUS = 'FF' THEN

    UPDATE XHB_DOCUMENT_CONTROL
    SET    STATUS = :NEW.FORMAT_STATUS
    WHERE  FORMATTING_ID = :NEW.FORMATTING_ID;

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_FORMB_RESULT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_FORMB_RESULT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMB_RESULT') = 1) THEN

    INSERT INTO AUD_FORMB_RESULT 
    VALUES (:old.formb_result_id, 
            :old.formb_result_type,
            :old.ref_plea_id, 
            :old.ref_verdict_id, 
            :old.result_description,
            :old.obs_ind, 
            :old.court_id,
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_FORMB_RESULT_BIR_TR
  BEFORE INSERT
  ON XHB_FORMB_RESULT
  FOR EACH ROW
BEGIN

  IF :NEW.FORMB_RESULT_ID IS NULL THEN

    SELECT XHB_FORMB_RESULT_SEQ.NEXTVAL
    INTO   :NEW.FORMB_RESULT_ID
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

CREATE OR REPLACE TRIGGER XHB_HEARING_BIR_TR
  BEFORE INSERT
  ON XHB_HEARING
  FOR EACH ROW

BEGIN

  IF :NEW.HEARING_ID IS NULL THEN

    SELECT XHB_HEARING_SEQ.NEXTVAL
    INTO   :NEW.HEARING_ID
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

CREATE OR REPLACE TRIGGER XHB_HEARING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_HEARING
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_HEARING') = 1) THEN

    INSERT INTO AUD_HEARING 
    VALUES (:old.HEARING_ID, 
            :old.CASE_ID, 
            :old.REF_HEARING_TYPE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.MP_HEARING_TYPE, 
            :old.last_calculated_duration, 
            :old.hearing_start_date, 
            :old.hearing_end_date, 
            :old.linked_hearing_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_HEARING_LEG_REP_BIR_TR
  BEFORE INSERT
  ON XHB_HEARING_LEG_REP
  FOR EACH ROW

BEGIN

  IF :NEW.HEARING_LEG_REP_ID IS NULL THEN

    SELECT XHB_HEARING_LEG_REP_SEQ.NEXTVAL
    INTO   :NEW.HEARING_LEG_REP_ID
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

CREATE OR REPLACE TRIGGER XHB_HEARING_LEG_REP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_HEARING_LEG_REP
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
    IF (Xhb_Custom_Pkg.IS_CONNECTION_POOL_USER = 1) THEN

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
    IF (Xhb_Custom_Pkg.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (Xhb_Custom_Pkg.IS_AUDIT_REQUIRED('XHB_HEARING_LEG_REP') = 1) THEN

    INSERT INTO AUD_HEARING_LEG_REP 
    VALUES (:OLD.HEARING_LEG_REP_ID, 
            :OLD.HEARING_ID, 
            :OLD.REF_LEGAL_REP_ID, 
            :OLD.START_DATE, 
            :OLD.END_DATE, 
            :OLD.VERSION, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATE_DATE, 
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_HEARINGLIST_BIR_TR
  BEFORE INSERT
  ON XHB_HEARING_LIST
  FOR EACH ROW

BEGIN

  IF :NEW.LIST_ID IS NULL THEN

    SELECT XHB_HEARING_LIST_SEQ.NEXTVAL
    INTO   :NEW.LIST_ID
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

CREATE OR REPLACE TRIGGER XHB_HEARINGLIST_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_HEARING_LIST
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_HEARING_LIST') = 1) THEN

    INSERT INTO AUD_HEARING_LIST 
    VALUES (:old.LIST_ID, 
            :old.LIST_TYPE, 
            :old.START_DATE, 
            :old.END_DATE, 
            :old.STATUS, 
            :old.EDITION_NO, 
            :old.PUBLISHED_TIME, 
            :old.PRINT_REFERENCE, 
            :old.CREST_LIST_ID, 
            :old.COURT_ID, 
            :old.LIST_COURT_TYPE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

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

CREATE OR REPLACE TRIGGER XHB_INTERNET_HTML_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_INTERNET_HTML
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_INTERNET_HTML') = 1) THEN

    INSERT INTO AUD_INTERNET_HTML 
    VALUES (:old.internet_html_id, 
            :old.html, 
            :old.status, 
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

CREATE OR REPLACE TRIGGER XHB_INTERNET_HTML_BIR_TR
  BEFORE INSERT
  ON XHB_INTERNET_HTML
  FOR EACH ROW

BEGIN

  IF :NEW.INTERNET_HTML_ID IS NULL THEN

    SELECT XHB_INTERNET_HTML_SEQ.NEXTVAL
    INTO   :NEW.INTERNET_HTML_ID
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

CREATE OR REPLACE TRIGGER XHB_JOINDER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_JOINDER
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_JOINDER') = 1) THEN

    INSERT INTO AUD_JOINDER 
    VALUES (:old.JOINDER_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.REF_JUDGE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_JOINDER_BIR_TR
  BEFORE INSERT
  ON XHB_JOINDER
  FOR EACH ROW

BEGIN

  IF :NEW.JOINDER_ID IS NULL THEN

    SELECT XHB_JOINDER_SEQ.NEXTVAL
    INTO   :NEW.JOINDER_ID
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

CREATE OR REPLACE TRIGGER XHB_JOINDER_CHARGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_JOINDER_CHARGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_JOINDER_CHARGE') = 1) THEN

    INSERT INTO AUD_JOINDER_CHARGE 
    VALUES (:old.joinder_charge_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.JOINDER_ID, 
            :old.CHARGE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_JOINDER_CHARGE_BIR_TR
  BEFORE INSERT
  ON XHB_JOINDER_CHARGE
  FOR EACH ROW

BEGIN

  IF :NEW.JOINDER_CHARGE_ID IS NULL THEN

    SELECT XHB_JOINDER_CHARGE_SEQ.NEXTVAL
    INTO   :NEW.JOINDER_CHARGE_ID
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

CREATE OR REPLACE TRIGGER XHB_JOINDER_DEF_ON_CASE_BIR_TR
  BEFORE INSERT
  ON XHB_JOINDER_DEFENDANT_ON_CASE
  FOR EACH ROW

BEGIN

  IF :NEW.JOINDER_DEFENDANT_ON_CASE_ID IS NULL THEN

    SELECT XHB_JOINDER_DEF_ON_CASE_SEQ.NEXTVAL
    INTO   :NEW.JOINDER_DEFENDANT_ON_CASE_ID
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

CREATE OR REPLACE TRIGGER XHB_JOINDER_DEF_ON_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_JOINDER_DEFENDANT_ON_CASE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_JOINDER_DEFENDANT_ON_CASE') = 1) THEN

    INSERT INTO AUD_JOINDER_DEFENDANT_ON_CASE 
    VALUES (:old.joinder_defendant_on_case_id, 
            :old.defendant_on_case_id_1, 
            :old.defendant_on_case_id_2, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.JOINDER_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_JOINDER_XML_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_JOINDER_XML
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_JOINDER_XML') = 1) THEN

    INSERT INTO AUD_JOINDER_XML 
    VALUES (:old.JOINDER_XML_ID, 
            :old.XML_CLOB, 
            :old.STATUS, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.JOINDER_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_JOINDER_XML_BIR_TR
  BEFORE INSERT
  ON XHB_JOINDER_XML
  FOR EACH ROW

BEGIN

  IF :NEW.JOINDER_XML_ID IS NULL THEN

    SELECT XHB_JOINDER_XML_SEQ.NEXTVAL
    INTO   :NEW.JOINDER_XML_ID
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

CREATE OR REPLACE TRIGGER XHB_LINKEDCASE_BIR_TR
  BEFORE INSERT
  ON XHB_LINKED_CASE
  FOR EACH ROW

BEGIN

  IF :NEW.LINKED_CASE_ID IS NULL THEN

    SELECT XHB_LINKED_CASE_SEQ.NEXTVAL
    INTO   :NEW.LINKED_CASE_ID
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

CREATE OR REPLACE TRIGGER XHB_LINKEDCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LINKED_CASE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_CASE') = 1) THEN

    INSERT INTO AUD_LINKED_CASE 
    VALUES (:old.LINKED_CASE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_LINKED_HEARING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LINKED_HEARING
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_HEARING') = 1) THEN

    INSERT INTO AUD_LINKED_HEARING 
    VALUES (:old.LINKED_HEARING_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_LINKED_HEARING_BIR_TR
  BEFORE INSERT
  ON XHB_LINKED_HEARING
  FOR EACH ROW

BEGIN

  IF :NEW.LINKED_HEARING_ID IS NULL THEN

    SELECT XHB_LINKED_HEARING_SEQ.NEXTVAL
    INTO   :NEW.LINKED_HEARING_ID
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

CREATE OR REPLACE TRIGGER XHB_LINKED_SH_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LINKED_SH
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LINKED_SH') = 1) THEN

    INSERT INTO AUD_LINKED_SH 
    VALUES (:old.LINKED_SH_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_LINKED_SH_BIR_TR
  BEFORE INSERT
  ON XHB_LINKED_SH
  FOR EACH ROW

BEGIN

  IF :NEW.LINKED_SH_ID IS NULL THEN

    SELECT XHB_LINKED_SH_SEQ.NEXTVAL
    INTO   :NEW.LINKED_SH_ID
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

CREATE OR REPLACE TRIGGER XHB_MESSAGE_BIR_TR
  BEFORE INSERT
  ON XHB_MESSAGE
  FOR EACH ROW

BEGIN

  IF :NEW.MESSAGE_ID IS NULL THEN

    SELECT XHB_MESSAGE_SEQ.NEXTVAL
    INTO   :NEW.MESSAGE_ID
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

CREATE OR REPLACE TRIGGER XHB_MESSAGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_MESSAGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_MESSAGE') = 1) THEN

    INSERT INTO AUD_MESSAGE 
    VALUES (:old.MESSAGE_ID, 
            :old.MESSAGE_FROM, 
            :old.MESSAGE_TO, 
            :old.MESSAGE, 
            :old.TIME_SENT, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_OBJECTSTATUS_BIR_TR
  BEFORE INSERT
  ON XHB_OBJECT_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.OBJECT_STATUS_ID  IS NULL THEN

    SELECT XHB_OBJECT_STATUS_SEQ.NEXTVAL
    INTO   :NEW.OBJECT_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_OBJECTSTATUS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_OBJECT_STATUS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OBJECT_STATUS') = 1) THEN

    INSERT INTO AUD_OBJECT_STATUS 
    VALUES (:old.OBJECT_STATUS_ID, 
            :old.OBJECT_NAME, 
            :old.OBJECT_ID, 
            :old.CREST_OBJECT_ID, 
            :old.COURT_ID, 
            :old.ACTION, 
            :old.STATUS, 
            :old.TIMESTAMP, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_OFFENCE_BIR_TR
  BEFORE INSERT
  ON XHB_OFFENCE
  FOR EACH ROW

BEGIN

  IF :NEW.OFFENCE_ID IS NULL THEN

    SELECT XHB_OFFENCE_SEQ.NEXTVAL
    INTO   :NEW.OFFENCE_ID
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

CREATE OR REPLACE TRIGGER XHB_OFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_OFFENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OFFENCE') = 1) THEN

    INSERT INTO AUD_OFFENCE 
    VALUES (:old.OFFENCE_ID, 
            :old.CREST_OFFENCE_ID, 
            :old.CREST_OFFENCE_SEQ_NO, 
            :old.CREST_OFFENCE_FREETEXT, 
            :old.MULTIPLE, 
            :old.CREST_HOO_CLASS_FREETEXT, 
            :old.CREST_HOO_SUBCLASS_FREETEXT, 
            :old.REF_OFFENCE_ID, 
            :old.CHARGE_ID, 
            :old.OBS_IND, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.REF_SYSTEM_CODE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER') = 1) THEN

    INSERT INTO AUD_ORDER 
    VALUES (:old.ORDER_ID, 
            :old.DELIVERY_DATE, 
            :old.SIGNED_BY, 
            :old.SIGNING_DATE, 
            :old.DATA_XML, 
            :old.ORDER_DELIVERY_STATUS_ID, 
            :old.ORDER_STATUS_ID, 
            :old.ORDER_TEMPLATE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATE_DATE, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.DESCRIPTION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER
  FOR EACH ROW

BEGIN

  IF :NEW.ORDER_ID IS NULL THEN

    SELECT XHB_ORDER_SEQ.NEXTVAL
    INTO   :NEW.ORDER_ID
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

CREATE OR REPLACE TRIGGER XHB_ORDER_DELIVERY_STAT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_DELIVERY_STATUS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_DELIVERY_STATUS') = 1) THEN

    INSERT INTO AUD_ORDER_DELIVERY_STATUS 
    VALUES (:old.ORDER_DELIVERY_STATUS_ID, 
            :old.CODE, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_DELIVERY_STAT_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_DELIVERY_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.ORDER_DELIVERY_STATUS_ID IS NULL THEN

    SELECT XHB_ORD_DELIVERY_STATUS_SEQ.NEXTVAL
    INTO   :NEW.ORDER_DELIVERY_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_ORDER_DISPOSAL_XREF_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_DISPOSAL_XREF
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_DISPOSAL_XREF') = 1) THEN

    INSERT INTO AUD_ORDER_DISPOSAL_XREF 
    VALUES (:old.order_disposal_xref_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.COURT_ID,
            :old.rs_ref_disposal_type_id,
            :old.co_ref_disposal_type_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_DISPOSAL_XREF_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_DISPOSAL_XREF
  FOR EACH ROW

BEGIN
  IF :NEW.ORDER_DISPOSAL_XREF_ID IS NULL THEN

    SELECT XHB_ORDER_DISPOSAL_XREF_SEQ.NEXTVAL
    INTO   :NEW.ORDER_DISPOSAL_XREF_ID
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

CREATE OR REPLACE TRIGGER XHB_ORDER_STATUS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_STATUS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_STATUS') = 1) THEN

    INSERT INTO AUD_ORDER_STATUS 
    VALUES (:old.ORDER_STATUS_ID, 
            :old.CODE, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_STATUS_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.ORDER_STATUS_ID IS NULL THEN

    SELECT XHB_ORD_STATUS_SEQ.NEXTVAL
    INTO   :NEW.ORDER_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_ORDER_TEMPLATE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_TEMPLATE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_TEMPLATE') = 1) THEN

    INSERT INTO AUD_ORDER_TEMPLATE 
    VALUES (:old.ORDER_TEMPLATE_ID, 
            :old.DISPLAY_TRANSFORM_NAME, 
            :old.NARRATIVE_TEMPLATE_NAME, 
            :old.EDITOR_TEMPLATE_NAME, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.ORDER_TYPE_ID, 
            :old.obs_ind,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_TEMPLATE_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_TEMPLATE
  FOR EACH ROW

BEGIN

  IF :NEW.ORDER_TEMPLATE_ID IS NULL THEN

    SELECT XHB_ORD_TEMPLATE_SEQ.NEXTVAL
    INTO   :NEW.ORDER_TEMPLATE_ID
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

CREATE OR REPLACE TRIGGER XHB_ORDER_TYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_TYPE') = 1) THEN

    INSERT INTO AUD_ORDER_TYPE 
    VALUES (:old.ORDER_TYPE_ID, 
            :old.CODE, 
            :old.DESCRIPTION, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.REF_DISPOSAL_TYPE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_TYPE_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.ORDER_TYPE_ID IS NULL THEN

    SELECT XHB_ORD_TYPE_SEQ.NEXTVAL
    INTO   :NEW.ORDER_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_ORIGINAL_RESULT_BIR_TR
  BEFORE INSERT
  ON XHB_ORIGINAL_RESULT
  FOR EACH ROW

BEGIN

  IF :NEW.ORIGINAL_RESULT_ID IS NULL THEN

    SELECT XHB_ORIGINAL_RESULT_SEQ.NEXTVAL
    INTO   :NEW.ORIGINAL_RESULT_ID
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

CREATE OR REPLACE TRIGGER XHB_ORIGINAL_RESULT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORIGINAL_RESULT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORIGINAL_RESULT') = 1) THEN

    INSERT INTO AUD_ORIGINAL_RESULT 
    VALUES (:old.ORIGINAL_RESULT_ID, 
            :old.COURT_TYPE, 
            :old.CREST_DIS_ID, 
            :old.DEF_ON_CHARGE_OR_OFFENCE, 
            :old.REF_COURT_ID, 
            :old.DEF_ON_CHARGE_OR_OFFENCE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.REF_DISPOSAL_ID,
            l_trig_event);

  END IF;

END;
/

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
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_PLEA_BIR_TR
  BEFORE INSERT
  ON XHB_PLEA
  FOR EACH ROW

BEGIN

  IF :NEW.PLEA_ID IS NULL THEN

    SELECT XHB_PLEA_SEQ.NEXTVAL
    INTO   :NEW.PLEA_ID
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

CREATE OR REPLACE TRIGGER XHB_PSR_RECIPIENT_BIR_TR
  BEFORE INSERT
  ON XHB_PSR_RECIPIENT
  FOR EACH ROW

BEGIN

  IF :NEW.RECIPIENT_ID IS NULL THEN

    SELECT XHB_PSR_RECIPIENT_SEQ.NEXTVAL
    INTO   :NEW.RECIPIENT_ID
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

CREATE OR REPLACE TRIGGER XHB_PSR_RECIPIENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PSR_RECIPIENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PSR_RECIPIENT') = 1) THEN

    INSERT INTO AUD_PSR_RECIPIENT 
    VALUES (:old.recipient_id, 
            :old.recipient_name, 
            :old.recipient_method_of_contact, 
            :old.recipient_address_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_PSR_REQUEST_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PSR_REQUEST
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PSR_REQUEST') = 1) THEN

    INSERT INTO AUD_PSR_REQUEST 
    VALUES (:old.psr_request_id, 
            :old.arrive_no_later_than_date, 
            :old.hearing_date, 
            :old.defendant_remand_location, 
            :old.solicitors_telephone, 
            :old.probation_contact, 
            :old.antecendants, 
            :old.cps_office_for_antecendants, 
            :old.circumstances_for_offences, 
            :old.comments_by_court, 
            :old.available_for_interview, 
            :old.probation_office_name, 
            :old.probation_office_address, 
            :old.probation_office_telephone, 
            :old.probation_office_fax, 
            :old.probation_office_email, 
            :old.recipient_name, 
            :old.recipient_address, 
            :old.recipient_telephone, 
            :old.recipient_fax, 
            :old.recipient_email, 
            :old.defendant_age, 
            :old.defendant_address, 
            :old.defendant_surname, 
            :old.defendant_forenames, 
            :old.defendant_date_of_birth, 
            :old.judge_title, 
            :old.court_name, 
            :old.solicitor_firm_name, 
            :old.psr_status, 
            :old.psr_recipient_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.DEFENDANT_ON_CASE_ID,
            :old.PSR_COURT_ROOM,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_PSR_REQUEST_BIR_TR
  BEFORE INSERT
  ON XHB_PSR_REQUEST
  FOR EACH ROW

BEGIN

  IF :NEW.PSR_REQUEST_ID IS NULL THEN

    SELECT XHB_PSR_REQUEST_SEQ.NEXTVAL
    INTO   :NEW.PSR_REQUEST_ID
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

CREATE OR REPLACE TRIGGER XHB_PUBLICNOTICE_BIR_TR
  BEFORE INSERT
  ON XHB_PUBLIC_NOTICE
  FOR EACH ROW

BEGIN

  IF :NEW.PUBLIC_NOTICE_ID IS NULL THEN

    SELECT XHB_PUBLIC_NOTICE_SEQ.NEXTVAL
    INTO   :NEW.PUBLIC_NOTICE_ID
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

CREATE OR REPLACE TRIGGER XHB_PUBLICNOTICE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PUBLIC_NOTICE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PUBLIC_NOTICE') = 1) THEN

    INSERT INTO AUD_PUBLIC_NOTICE 
    VALUES (:old.PUBLIC_NOTICE_ID, 
            :old.PUBLIC_NOTICE_DESC, 
            :old.COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.definitive_pn_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_RECIPIENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_RECIPIENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_RECIPIENT') = 1) THEN

    INSERT INTO AUD_RECIPIENT 
    VALUES (:old.recipient_id, 
            :old.recipient_name, 
            :old.fax_number, 
            :old.email_address, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID,
            :old.PREF_DISTRIBUTION_TYPE,
            :old.PREF_MIME_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_RECIPIENT_BIR_TR
  BEFORE INSERT
  ON XHB_RECIPIENT
  FOR EACH ROW

BEGIN

  IF :NEW.RECIPIENT_ID IS NULL THEN

    SELECT XHB_RECIPIENT_SEQ.NEXTVAL
    INTO   :NEW.RECIPIENT_ID
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

CREATE OR REPLACE TRIGGER XHB_REFADVOCATE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_ADVOCATE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_ADVOCATE_ID IS NULL THEN

    SELECT XHB_REF_ADVOCATE_SEQ.NEXTVAL
    INTO   :NEW.REF_ADVOCATE_ID
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

CREATE OR REPLACE TRIGGER XHB_REFADVOCATE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_ADVOCATE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_ADVOCATE') = 1) THEN

    INSERT INTO AUD_REF_ADVOCATE 
    VALUES (:old.REF_ADVOCATE_ID, 
            :old.IS_GLOBAL, 
            :old.CREST_ADVOCATE_ID, 
            :old.CREST_CHAMBER_ID, 
            :old.OBS_IND, 
            :old.YEAR_OF_CALL, 
            :old.VAT_NO, 
            :old.BAR_NO, 
            :old.HONOURS, 
            :old.ADV_TYPE_IND, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.REF_LEGAL_REP_ID, 
            :old.REF_CHAMBER_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_APP_RESULT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_APP_RESULT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_APP_RESULT') = 1) THEN

    INSERT INTO AUD_REF_APP_RESULT 
    VALUES (:old.REF_APP_RESULT_ID, 
            :old.APP_RESULT_CODE, 
            :old.APP_RESULT_DESCR1, 
            :old.HO_CODE, 
            :old.VARY_SENTENCE, 
            :old.APP_RESULT_DESCR2, 
            :old.LESSER_OFF_IND, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_APP_RESULT_BIR_TR
  BEFORE INSERT
  ON XHB_REF_APP_RESULT
  FOR EACH ROW

BEGIN

  IF :NEW.REF_APP_RESULT_ID IS NULL THEN

    SELECT XHB_REF_APP_RESULT_SEQ.NEXTVAL
    INTO   :NEW.REF_APP_RESULT_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_CHAMBER_BIR_TR
  BEFORE INSERT
  ON XHB_REF_CHAMBER
  FOR EACH ROW

BEGIN

  IF :NEW.REF_CHAMBER_ID IS NULL THEN

    SELECT XHB_REF_CHAMBER_SEQ.NEXTVAL
    INTO   :NEW.REF_CHAMBER_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_CHAMBER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_CHAMBER
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_CHAMBER') = 1) THEN

    INSERT INTO AUD_REF_CHAMBER 
    VALUES (:old.REF_CHAMBER_ID, 
            :old.OBS_IND, 
            :old.IS_GLOBAL, 
            :old.DX_REF, 
            :old.LOCATION_CODE, 
            :old.CREST_CHAMBER_ID, 
            :old.FIRM_NAME, 
            :old.ADDRESS_ID, 
            :old.COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFCOURT_BIR_TR
  BEFORE INSERT
  ON XHB_REF_COURT
  FOR EACH ROW

BEGIN

  IF :NEW.REF_COURT_ID IS NULL THEN

    SELECT XHB_REF_COURT_SEQ.NEXTVAL
    INTO   :NEW.REF_COURT_ID
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

CREATE OR REPLACE TRIGGER XHB_REFCOURT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_COURT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT') = 1) THEN

    INSERT INTO AUD_REF_COURT 
    VALUES (:old.REF_COURT_ID, 
            :old.COURT_FULL_NAME, 
            :old.COURT_SHORT_NAME, 
            :old.NAME_PREFIX, 
            :old.COURT_TYPE, 
            :old.CREST_CODE, 
            :old.OBS_IND, 
            :old.IS_PSD, 
            :old.DX_REF, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.ADDRESS_ID, 
            :old.COURT_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFCOURTREPORTER_BIR_TR
  BEFORE INSERT
  ON XHB_REF_COURT_REPORTER
  FOR EACH ROW

BEGIN

  IF :NEW.REF_COURT_REPORTER_ID IS NULL THEN

    SELECT XHB_REF_COURT_REPORTER_SEQ.NEXTVAL
    INTO   :NEW.REF_COURT_REPORTER_ID
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

CREATE OR REPLACE TRIGGER XHB_REFCOURTREPORTER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_COURT_REPORTER
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT_REPORTER') = 1) THEN

    INSERT INTO AUD_REF_COURT_REPORTER 
    VALUES (:old.REF_COURT_REPORTER_ID, 
            :old.FIRST_NAME, 
            :old.MIDDLE_NAME, 
            :old.SURNAME, 
            :old.CREST_COURT_REPORTER_ID, 
            :old.INITIALS, 
            :old.REPORT_METHOD, 
            :old.OBS_IND, 
            :old.REF_COURT_REPORTER_FIRM_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_COURT_REPORT_F_BIR_TR
  BEFORE INSERT
  ON XHB_REF_COURT_REPORTER_FIRM
  FOR EACH ROW

BEGIN

  IF :NEW.REF_COURT_REPORTER_FIRM_ID IS NULL THEN

    SELECT XHB_REF_COURT_REPORT_F_SEQ.NEXTVAL
    INTO   :NEW.REF_COURT_REPORTER_FIRM_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_COURT_REPORT_F_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_COURT_REPORTER_FIRM
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_COURT_REPORTER_FIRM') = 1) THEN

    INSERT INTO AUD_REF_COURT_REPORTER_FIRM 
    VALUES (:old.REF_COURT_REPORTER_FIRM_ID, 
            :old.OBS_IND, 
            :old.DISPLAY_FIRST, 
            :old.DX_REF, 
            :old.VAT_NO, 
            :old.FIRM_NAME, 
            :old.ADDRESS_ID, 
            :old.COURT_ID, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.VERSION, 
            :old.CREST_COURT_REPORTER_FIRM_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFDISPOSAL_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISPOSAL
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_ID IS NULL THEN

    SELECT XHB_REF_DISPOSAL_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_ID
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

CREATE OR REPLACE TRIGGER XHB_REFDISPOSAL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISPOSAL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL') = 1) THEN

    INSERT INTO AUD_REF_DISPOSAL 
    VALUES (:old.REF_DISPOSAL_ID, 
            :old.DISPOSAL_CODE, 
            :old.DISPOSAL_TITLE, 
            :old.CREST_MENU_GROUP, 
            :old.CREST_TEMPLATE_VERSION, 
            :old.DISP_TITLE1, 
            :old.DISP_TITLE2, 
            :old.DVLC_CODE, 
            :old.OBS_IND, 
            :old.COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_LINE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISPOSAL_LINE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_LINE_ID IS NULL THEN

    SELECT XHB_REF_DISPOSAL_LINE_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_LINE_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_LINE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISPOSAL_LINE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL_LINE') = 1) THEN

    INSERT INTO AUD_REF_DISPOSAL_LINE
    VALUES (:old.REF_DISPOSAL_LINE_ID,
            :old.COURT_ID,
            :old.DISPOSAL_CODE,
            :old.TEMPLATE_VERSION,
            :old.DIL_SEQ_NO,
            :old.DATA,
            :old.INPUT_FLAG,
            :old.SCREEN_PRINT,
            :old.FORM_PRINT,
            :old.DBDESTIN,
            :old.PROMPT,
            :old.FORMAT,
            :old.MANDATORY,
            :old.DBSOURCE,
            :old.VALIDATION,
            :old.MULTIPLE_CHOICE,
            :old.MCGROUP1,       
            :old.MCGROUP2,
            :old.CHAR_MAX,
            :old.CONC_FLAG,
            :old.LINE_INSERT,
            :old.OBS_IND,
            :old.LAST_UPDATED_BY,
            :old.CREATED_BY,
            :old.CREATION_DATE,
            :old.LAST_UPDATE_DATE,
            :old.VERSION,
            l_trig_event);


  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_MENU_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISPOSAL_MENU
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_MENU_ID IS NULL THEN

    SELECT XHB_REF_DISPOSAL_MENU_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_MENU_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_MENU_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISPOSAL_MENU
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL_MENU') = 1) THEN

    INSERT INTO AUD_REF_DISPOSAL_MENU 
    VALUES (:old.REF_DISPOSAL_MENU_ID, 
            :old.TITLE, 
            :old.DISPOSAL_CODE, 
            :old.PARENT, 
            :old.ABBREV, 
            :old.MENU_GROUP, 
            :old.SEQ_NO, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.OBS_IND,
            :old.MENU_ITEM_ID, 
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_DISP_MENU_CTYPE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISP_MENU_CASE_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_MENU_CASE_TYPE_ID IS NULL THEN

    SELECT XHB_R_DISP_MENU_CASE_TYPE_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_MENU_CASE_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_DISP_MENU_CTYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISP_MENU_CASE_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISP_MENU_CASE_TYPE') = 1) THEN

    INSERT INTO AUD_REF_DISP_MENU_CASE_TYPE 
    VALUES (:old.REF_DISPOSAL_MENU_CASE_TYPE_ID,
            :old.REF_DISPOSAL_MENU_ID,
            :old.CASE_TYPE,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION, 
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_TYPE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISPOSAL_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_TYPE_ID IS NULL THEN

    SELECT XHB_REF_DISPOSAL_TYPE_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_DISPOSAL_TYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISPOSAL_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISPOSAL_TYPE') = 1) THEN

    INSERT INTO AUD_REF_DISPOSAL_TYPE
    VALUES (:old.REF_DISPOSAL_TYPE_ID,
            :old.COURT_ID,
            :old.TEMPLATE_VERSION,
            :old.DISPOSAL_CODE,
            :old.MENU_GROUP,
            :old.TITLE,
            :old.DISP_TITLE1,
            :old.DISP_TITLE2,
            :old.LINE_AVAIL,
            :old.CATEGORY,
            :old.OBS_IND,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFHEARINGTYPE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_HEARING_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_HEARING_TYPE_ID IS NULL THEN

    SELECT XHB_REF_HEARING_TYPE_SEQ.NEXTVAL
    INTO   :NEW.REF_HEARING_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_REFHEARINGTYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_HEARING_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_HEARING_TYPE') = 1) THEN

    INSERT INTO AUD_REF_HEARING_TYPE 
    VALUES (:old.REF_HEARING_TYPE_ID, 
            :old.HEARING_TYPE_CODE, 
            :old.HEARING_TYPE_DESC, 
            :old.CATEGORY, 
            :old.SEQ_NO, 
            :old.LIST_SEQUENCE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFJUDGE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_JUDGE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_JUDGE_ID IS NULL THEN

    SELECT XHB_REF_JUDGE_SEQ.NEXTVAL
    INTO   :NEW.REF_JUDGE_ID
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

CREATE OR REPLACE TRIGGER XHB_REFJUDGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_JUDGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_JUDGE') = 1) THEN

    INSERT INTO AUD_REF_JUDGE 
    VALUES (:old.REF_JUDGE_ID, 
            :old.JUDGE_TYPE, 
            :old.CREST_JUDGE_ID, 
            :old.TITLE, 
            :old.FIRST_NAME, 
            :old.MIDDLE_NAME, 
            :old.SURNAME, 
            :old.FULL_LIST_TITLE1, 
            :old.FULL_LIST_TITLE2, 
            :old.FULL_LIST_TITLE3, 
            :old.STATS_CODE, 
            :old.INITIALS, 
            :old.HONOURS, 
            :old.JUD_VERS, 
            :old.OBS_IND, 
            :old.SOURCE_TABLE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFJUSTICE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_JUSTICE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_JUSTICE_ID IS NULL THEN

    SELECT XHB_REF_JUSTICE_SEQ.NEXTVAL
    INTO   :NEW.REF_JUSTICE_ID
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

CREATE OR REPLACE TRIGGER XHB_REFJUSTICE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_JUSTICE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_JUSTICE') = 1) THEN

    INSERT INTO AUD_REF_JUSTICE 
    VALUES (:old.REF_JUSTICE_ID, 
            :old.JUSTICE_NAME, 
            :old.CREST_JUSTICE_ID, 
            :old.COURT_ID, 
            :old.PSD_COURT_CODE, 
            :old.TITLE, 
            :old.INITIALS, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFLEGALREPRESENTAT_BIR_TR
  BEFORE INSERT
  ON XHB_REF_LEGAL_REPRESENTATIVE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_LEGAL_REP_ID IS NULL THEN

    SELECT XHB_REF_LEGAL_REP_SEQ.NEXTVAL
    INTO   :NEW.REF_LEGAL_REP_ID
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

CREATE OR REPLACE TRIGGER XHB_REFLEGALREPRESENTAT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_LEGAL_REPRESENTATIVE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_LEGAL_REPRESENTATIVE') = 1) THEN

    INSERT INTO AUD_REF_LEGAL_REPRESENTATIVE 
    VALUES (:old.REF_LEGAL_REP_ID, 
            :old.FIRST_NAME, 
            :old.MIDDLE_NAME, 
            :old.SURNAME, 
            :old.TITLE, 
            :old.INITIALS, 
            :old.LEGAL_REP_TYPE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFOFFENCE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_OFFENCE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_OFFENCE_ID IS NULL THEN

    SELECT XHB_REF_OFFENCE_SEQ.NEXTVAL
    INTO   :NEW.REF_OFFENCE_ID
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

CREATE OR REPLACE TRIGGER XHB_REFOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_OFFENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_OFFENCE') = 1) THEN

    INSERT INTO AUD_REF_OFFENCE 
    VALUES (:old.REF_OFFENCE_ID, 
            :old.OFFENCE_CODE, 
            :old.OFFENCE_DESC, 
            :old.HO_PROC_TYPE, 
            :old.HO_CLASS, 
            :old.HO_SUB_CLASS, 
            :old.DVLC_CODE, 
            :old.STATUTE, 
            :old.OFFENCE_CLASS, 
            :old.ACT_SECTION, 
            :old.OBS_IND, 
            :old.OFFENCE_DESC2, 
            :old.OFFENCE_GROUP, 
            :old.COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFPROSECUTORAGENCY_BIR_TR
  BEFORE INSERT
  ON XHB_REF_PROSECUTOR_AGENCY
  FOR EACH ROW

BEGIN

  IF :NEW.REF_PROSECUTOR_AGENCY_ID IS NULL THEN

    SELECT XHB_REF_PROSECUTOR_AGENCY_SEQ.NEXTVAL
    INTO   :NEW.REF_PROSECUTOR_AGENCY_ID
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

CREATE OR REPLACE TRIGGER XHB_REFPROSECUTORAGENCY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_PROSECUTOR_AGENCY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_PROSECUTOR_AGENCY') = 1) THEN

    INSERT INTO AUD_REF_PROSECUTOR_AGENCY 
    VALUES (:old.REF_PROSECUTOR_AGENCY_ID, 
            :old.TITLE, 
            :old.PROSECUTOR_NAME_1, 
            :old.PROSECUTOR_NAME_2, 
            :old.PROSECUTOR_NAME_3, 
            :old.INITIALS, 
            :old.ADDRESS_ID, 
            :old.CREST_OPPOSER_ID, 
            :old.COURT_ID, 
            :old.CPS_CODE, 
            :old.DX_REF, 
            :old.OBS_IND, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SOLICITOR_BIR_TR
  BEFORE INSERT
  ON XHB_REF_SOLICITOR
  FOR EACH ROW

BEGIN

  IF :NEW.SOLICITOR_ID IS NULL THEN

    SELECT XHB_SOLICITOR_SEQ.NEXTVAL
    INTO   :NEW.SOLICITOR_ID
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

CREATE OR REPLACE TRIGGER XHB_SOLICITOR_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_SOLICITOR
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SOLICITOR') = 1) THEN

    INSERT INTO AUD_REF_SOLICITOR 
    VALUES (:old.SOLICITOR_ID, 
            :old.CREST_SOLICITOR_NAME, 
            :old.IS_IN_CREST, 
            :old.REF_LEGAL_REP_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.OBS_IND, 
            :old.REF_SOLICITOR_FIRM_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFSOLICITORFIRM_BIR_TR
  BEFORE INSERT
  ON XHB_REF_SOLICITOR_FIRM
  FOR EACH ROW

BEGIN

  IF :NEW.REF_SOLICITOR_FIRM_ID IS NULL THEN

    SELECT XHB_REF_SOLICITOR_FIRM_SEQ.NEXTVAL
    INTO   :NEW.REF_SOLICITOR_FIRM_ID
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REFSOLICITORFIRM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_SOLICITOR_FIRM
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SOLICITOR_FIRM') = 1) THEN

    INSERT INTO AUD_REF_SOLICITOR_FIRM 
    VALUES (:old.REF_SOLICITOR_FIRM_ID, 
            :old.SOLICITOR_FIRM_NAME, 
            :old.CREST_SOF_ID, 
            :old.COURT_ID, 
            :old.OBS_IND, 
            :old.SHORT_NAME, 
            :old.DX_REF, 
            :old.VAT_NO, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.ADDRESS_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_REF_SYSTEM_CODE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_SYSTEM_CODE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_SYSTEM_CODE_ID IS NULL THEN

    SELECT XHB_REF_SYSTEM_CODE_SEQ.NEXTVAL
    INTO   :NEW.REF_SYSTEM_CODE_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_SYSTEM_CODE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_SYSTEM_CODE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_SYSTEM_CODE') = 1) THEN

    INSERT INTO AUD_REF_SYSTEM_CODE 
    VALUES (:old.REF_SYSTEM_CODE_ID, 
            :old.CODE, 
            :old.CODE_TYPE, 
            :old.CODE_TITLE, 
            :old.DE_CODE, 
            :old.REF_CODE_ORDER, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.OBS_IND,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ROTATION_SET_DD_BIR_TR
  BEFORE INSERT
  ON XHB_ROTATION_SET_DD
  FOR EACH ROW

BEGIN

  IF :NEW.ROTATION_SET_DD_ID IS NULL THEN

    SELECT XHB_ROTATION_SET_DD_SEQ.NEXTVAL
    INTO   :NEW.ROTATION_SET_DD_ID
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

CREATE OR REPLACE TRIGGER XHB_ROTATION_SET_DD_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ROTATION_SET_DD
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ROTATION_SET_DD') = 1) THEN

    INSERT INTO AUD_ROTATION_SET_DD
    VALUES (:OLD.ROTATION_SET_DD_ID,
            :OLD.ROTATION_SET_ID,
            :OLD.DISPLAY_DOCUMENT_ID,
            :OLD.PAGE_DELAY,
            :OLD.ORDERING,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ROTATION_SETS_BIR_TR
  BEFORE INSERT
  ON XHB_ROTATION_SETS
  FOR EACH ROW

BEGIN

  IF :NEW.ROTATION_SET_ID IS NULL THEN

    SELECT XHB_ROTATION_SETS_SEQ.NEXTVAL
    INTO   :NEW.ROTATION_SET_ID
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

CREATE OR REPLACE TRIGGER XHB_ROTATION_SETS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ROTATION_SETS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ROTATION_SETS') = 1) THEN

    INSERT INTO AUD_ROTATION_SETS
    VALUES (:OLD.ROTATION_SET_ID,
            :OLD.COURT_ID,
            :OLD.DESCRIPTION,
            :OLD.DEFAULT_YN,
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.LAST_UPDATE_DATE, 
            :OLD.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SCHED_HEARING_ATT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SCHED_HEARING_ATTENDEE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHED_HEARING_ATTENDEE') = 1) THEN

    INSERT INTO AUD_SCHED_HEARING_ATTENDEE 
    VALUES (:old.SH_ATTENDEE_ID, 
            :old.ATTENDEE_TYPE, 
            :old.SCHEDULED_HEARING_ID, 
            :old.VERSION, 
            :old.SH_STAFF_ID, 
            :old.SH_JUSTICE_ID, 
            :old.REF_JUDGE_ID, 
            :old.REF_COURT_REPORTER_ID, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            :old.REF_JUSTICE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SCHED_HEARING_ATT_BIR_TR
  BEFORE INSERT
  ON XHB_SCHED_HEARING_ATTENDEE
  FOR EACH ROW

BEGIN

  IF :NEW.SH_ATTENDEE_ID IS NULL THEN

    SELECT XHB_SCHED_HEARING_ATTEND_SEQ.NEXTVAL
    INTO   :NEW.SH_ATTENDEE_ID
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

CREATE OR REPLACE TRIGGER XHB_SCHEDHEARDEFEND_BIR_TR
  BEFORE INSERT
  ON XHB_SCHED_HEARING_DEFENDANT
  FOR EACH ROW

BEGIN

  IF :NEW.SCHED_HEAR_DEF_ID IS NULL THEN

    SELECT XHB_SCHEDULED_HEARING_DEF_SEQ.NEXTVAL
    INTO   :NEW.SCHED_HEAR_DEF_ID
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

CREATE OR REPLACE TRIGGER XHB_SCHEDHEARDEFEND_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SCHED_HEARING_DEFENDANT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHED_HEARING_DEFENDANT') = 1) THEN

    INSERT INTO AUD_SCHED_HEARING_DEFENDANT 
    VALUES (:old.SCHED_HEAR_DEF_ID, 
            :old.SCHEDULED_HEARING_ID, 
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

CREATE OR REPLACE TRIGGER XHB_SCHEDULEDHEARING_BIR_TR
  BEFORE INSERT
  ON XHB_SCHEDULED_HEARING
  FOR EACH ROW

BEGIN

  IF :NEW.SCHEDULED_HEARING_ID IS NULL THEN

    SELECT XHB_SCHEDULED_HEARING_SEQ.NEXTVAL
    INTO   :NEW.SCHEDULED_HEARING_ID
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

CREATE OR REPLACE TRIGGER XHB_SCHEDULEDHEARING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SCHEDULED_HEARING
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHEDULED_HEARING') = 1) THEN

    INSERT INTO AUD_SCHEDULED_HEARING 
    VALUES (:old.SCHEDULED_HEARING_ID, 
            :old.SEQUENCE_NO, 
            :old.NOT_BEFORE_TIME, 
            :old.ORIGINAL_TIME, 
            :old.LISTING_NOTE, 
            :old.HEARING_PROGRESS, 
            :old.SITTING_ID, 
            :old.HEARING_ID, 
            :old.MOVED_FROM, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.LINKED_SH_ID, 
            :old.END_TIME, 
            :old.START_TIME, 
            :old.DATE_OF_HEARING, 
            :old.is_case_active,
            :old.MOVED_FROM_COURT_ROOM_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SECURITY_GROUP_ROLE_BIR_TR
  BEFORE INSERT
  ON XHB_SECURITY_GROUP_ROLE
  FOR EACH ROW

BEGIN

  IF :NEW.SECURITY_GROUP_ROLE_ID IS NULL THEN

    SELECT XHB_SECURITY_GROUP_ROLE_SEQ.NEXTVAL
    INTO   :NEW.SECURITY_GROUP_ROLE_ID
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

CREATE OR REPLACE TRIGGER XHB_SECURITY_GROUP_ROLE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SECURITY_GROUP_ROLE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SECURITY_GROUP_ROLE') = 1) THEN

    INSERT INTO AUD_SECURITY_GROUP_ROLE 
    VALUES (:old.SECURITY_GROUP_ROLE_ID,
            :old.GROUP_NAME,
            :old.ROLE_NAME,
            :old.IS_ENABLED,
            :old.IS_ENABLED_BY_DEFAULT,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SH_JUDGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_JUDGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_JUDGE') = 1) THEN

    INSERT INTO AUD_SH_JUDGE 
    VALUES (:old.SH_JUDGE_ID, 
            :old.DEPUTY_HCJ, 
            :old.REF_JUDGE_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.SH_ATTENDEE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SH_JUDGE_BIR_TR
  BEFORE INSERT
  ON XHB_SH_JUDGE
  FOR EACH ROW

BEGIN

  IF :NEW.SH_JUDGE_ID IS NULL THEN

    SELECT XHB_SH_JUDGE_SEQ.NEXTVAL
    INTO   :NEW.SH_JUDGE_ID
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

CREATE OR REPLACE TRIGGER XHB_SH_JUSTICE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_JUSTICE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_JUSTICE') = 1) THEN

    INSERT INTO AUD_SH_JUSTICE 
    VALUES (:old.SH_JUSTICE_ID, 
            :old.JUSTICE_NAME, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.HEARING_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SH_JUSTICE_BIR_TR
  BEFORE INSERT
  ON XHB_SH_JUSTICE
  FOR EACH ROW

BEGIN

  IF :NEW.SH_JUSTICE_ID IS NULL THEN

    SELECT XHB_SH_JUSTICE_SEQ.NEXTVAL
    INTO   :NEW.SH_JUSTICE_ID
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

CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_LEG_REP
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_LEG_REP') = 1) THEN

    INSERT INTO AUD_SH_LEG_REP 
    VALUES (:old.SH_LEG_REP_ID, 
            :old.CREST_SEQUENCE_NO, 
            :old.LEGAL_ROLE, 
            :old.IS_SIGNED_IN, 
            :old.SOL_FIRM_OR_REF_LEGAL_REP, 
            :old.SCHED_HEAR_DEF_ID, 
            :old.REF_LEGAL_REP_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.CC_INFO_ID, 
            :old.REF_SOLICITOR_FIRM_ID, 
            :old.REF_DEFENCE_CATEGORY_ID, 
            :old.SCHEDULED_HEARING_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BIR_TR
  BEFORE INSERT
  ON XHB_SH_LEG_REP
  FOR EACH ROW

BEGIN

  IF :NEW.SH_LEG_REP_ID IS NULL THEN

    SELECT XHB_SH_LEG_REP_SEQ.NEXTVAL
    INTO   :NEW.SH_LEG_REP_ID
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

CREATE OR REPLACE TRIGGER XHB_SH_STAFF_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_STAFF
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_STAFF') = 1) THEN

    INSERT INTO AUD_SH_STAFF 
    VALUES (:old.SH_STAFF_ID, 
            :old.STAFF_ROLE, 
            :old.STAFF_NAME, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SH_STAFF_BIR_TR
  BEFORE INSERT
  ON XHB_SH_STAFF
  FOR EACH ROW

BEGIN

  IF :NEW.SH_STAFF_ID IS NULL THEN

    SELECT XHB_SH_STAFF_SEQ.NEXTVAL
    INTO   :NEW.SH_STAFF_ID
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

CREATE OR REPLACE TRIGGER XHB_SITTING_BIR_TR
  BEFORE INSERT
  ON XHB_SITTING
  FOR EACH ROW

BEGIN

  IF :NEW.SITTING_ID IS NULL THEN

    SELECT XHB_SITTING_SEQ.NEXTVAL
    INTO   :NEW.SITTING_ID
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

CREATE OR REPLACE TRIGGER XHB_SITTING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SITTING
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SITTING') = 1) THEN

    INSERT INTO AUD_SITTING 
    VALUES (:old.SITTING_ID, 
            :old.SITTING_SEQUENCE_NO, 
            :old.IS_SITTING_JUDGE, 
            :old.SITTING_TIME, 
            :old.SITTING_NOTE, 
            :old.REF_JUSTICE1_ID, 
            :old.REF_JUSTICE2_ID, 
            :old.REF_JUSTICE3_ID, 
            :old.REF_JUSTICE4_ID, 
            :old.IS_FLOATING, 
            :old.LIST_ID, 
            :old.REF_JUDGE_ID, 
            :old.COURT_ROOM_ID, 
            :old.COURT_SITE_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.JUSTICENAME4, 
            :old.JUSTICENAME3, 
            :old.JUSTICENAME2, 
            :old.JUSTICENAME1,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SKELETON_DAY_BIR_TR
  BEFORE INSERT
  ON XHB_SKELETON_DAY
  FOR EACH ROW

BEGIN

  IF :NEW.SKELETON_DAY_ID IS NULL THEN

    SELECT XHB_SKELETON_DAY_SEQ.NEXTVAL
    INTO   :NEW.SKELETON_DAY_ID
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

CREATE OR REPLACE TRIGGER XHB_SKELETON_DAY_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SKELETON_DAY
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SKELETON_DAY') = 1) THEN

    INSERT INTO AUD_SKELETON_DAY 
    VALUES (:old.skeleton_day_id, 
            :old.day_number, 
            :old.skeleton_date, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.WEEK_NUMBER, 
            :old.skeleton_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SKELETON_DEL_STATUS_BIR_TR
  BEFORE INSERT
  ON XHB_SKELETON_DELIVERY_STATUS
  FOR EACH ROW

BEGIN

  IF :NEW.SKELETON_DELIVERY_STATUS_ID IS NULL THEN

    SELECT XHB_SKELETON_DEL_STATUS_SEQ.NEXTVAL
    INTO   :NEW.SKELETON_DELIVERY_STATUS_ID
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

CREATE OR REPLACE TRIGGER XHB_SKELETON_DEL_STATUS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SKELETON_DELIVERY_STATUS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SKELETON_DELIVERY_STATUS') = 1) THEN

    INSERT INTO AUD_SKELETON_DELIVERY_STATUS 
    VALUES (:old.skeleton_delivery_status_id, 
            :old.code, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SKELETON_SCHEDULE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SKELETON_SCHEDULE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SKELETON_SCHEDULE') = 1) THEN

    INSERT INTO AUD_SKELETON_SCHEDULE 
    VALUES (:old.skeleton_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.skeleton_delivery_status_id, 
            :old.DELIVERABLE, 
            :old.CASE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SKELETON_SCHEDULE_BIR_TR
  BEFORE INSERT
  ON XHB_SKELETON_SCHEDULE
  FOR EACH ROW

BEGIN

  IF :NEW.SKELETON_ID IS NULL THEN

    SELECT XHB_SKELETON_SCHEDULE_SEQ.NEXTVAL
    INTO   :NEW.SKELETON_ID
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

CREATE OR REPLACE TRIGGER XHB_SKELETON_SESSION_BUR_TR
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
    VALUES (:old.skeleton_session_id, 
            :old.morning_or_afternoon, 
            :old.notes, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.skeleton_day_id, 
            :old.skeleton_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SKELETON_SESSION_BIR_TR
  BEFORE INSERT
  ON XHB_SKELETON_SESSION
  FOR EACH ROW

BEGIN

  IF :NEW.SKELETON_SESSION_ID IS NULL THEN

    SELECT XHB_SKELETON_SESSION_SEQ.NEXTVAL
    INTO   :NEW.SKELETON_SESSION_ID
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

CREATE OR REPLACE TRIGGER XHB_SUB_EVENT_CTRL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SUBSCR_EVENT_CONTROL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SUBSCR_EVENT_CONTROL') = 1) THEN

    INSERT INTO AUD_SUBSCR_EVENT_CONTROL 
    VALUES (:old.EVENT_CONTROL_ID, 
            :old.EVENT_TYPE, 
            :old.EVENT_DATA, 
            :old.EVENT_IDENTIFIER, 
            :old.EVENT_LEVEL, 
            :old.EVENT_TIME, 
            :old.CREST_COURT_ID, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_SUB_EVENT_CTRL_BIR_TR
  BEFORE INSERT
  ON XHB_SUBSCR_EVENT_CONTROL
  FOR EACH ROW

BEGIN

  IF :NEW.EVENT_CONTROL_ID IS NULL THEN

    SELECT XHB_SUBSCR_EVENT_CONTROL_SEQ.NEXTVAL
    INTO   :NEW.EVENT_CONTROL_ID
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

CREATE OR REPLACE TRIGGER XHB_SYS_AUDIT_BIR_TR
  BEFORE INSERT
  ON XHB_SYS_AUDIT
  FOR EACH ROW

DECLARE

BEGIN

  IF :NEW.SYS_AUDIT_ID IS NULL THEN

    SELECT XHB_SYS_AUDIT_SEQ.NEXTVAL
    INTO   :NEW.SYS_AUDIT_ID
    FROM   DUAL;

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_TERMINAL_BIR_TR
  BEFORE INSERT
  ON XHB_TERMINAL
  FOR EACH ROW

BEGIN

  IF :NEW.TERMINAL_ID IS NULL THEN

    SELECT XHB_TERMINAL_SEQ.NEXTVAL
    INTO   :NEW.TERMINAL_ID
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
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_TERMINAL_REFERENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_TERMINAL_REFERENCE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_TERMINAL_REFERENCE') = 1) THEN

    INSERT INTO AUD_TERMINAL_REFERENCE 
    VALUES (:old.ter_ref_id, 
            :old.reference_value, 
            :old.reference_name, 
            :old.category, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.TERMINAL_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_TERMINAL_REFERENCE_BIR_TR
  BEFORE INSERT
  ON XHB_TERMINAL_REFERENCE
  FOR EACH ROW

BEGIN

  IF :NEW.ter_ref_ID IS NULL THEN

    SELECT XHB_TERMINAL_REFERENCE_SEQ.NEXTVAL
    INTO   :NEW.ter_ref_ID
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

CREATE OR REPLACE TRIGGER XHB_TIME_BIR_TR
  BEFORE INSERT
  ON XHB_TIME
  FOR EACH ROW

BEGIN

  IF :NEW.TIME_ID IS NULL THEN

    SELECT XHB_TIME_SEQ.NEXTVAL
    INTO   :NEW.TIME_ID
    FROM   DUAL;

  END IF;

END;
/

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
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_VERDICT_BIR_TR
  BEFORE INSERT
  ON XHB_VERDICT
  FOR EACH ROW

BEGIN

  IF :NEW.VERDICT_ID IS NULL THEN

    SELECT XHB_VERDICT_SEQ.NEXTVAL
    INTO   :NEW.VERDICT_ID
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

CREATE OR REPLACE TRIGGER XHB_WITNESS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_WITNESS
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WITNESS') = 1) THEN

    INSERT INTO AUD_WITNESS 
    VALUES (:old.witness_id, 
            :old.name, 
            :old.status, 
            :old.age, 
            :old.witness_type, 
            :old.actual_arrival_date_time, 
            :old.EXPECTED_ARRIVAL_TIME, 
            :old.released_date_time, 
            :old.mobileNumber, 
            :old.pagerNumber, 
            :old.pagerNet, 
            :old.notes, 
            :old.calculated_witness_time, 
            :old.session_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.CASE_ID,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_WITNESS_BIR_TR
  BEFORE INSERT
  ON XHB_WITNESS
  FOR EACH ROW

BEGIN

  IF :NEW.WITNESS_ID IS NULL THEN

    SELECT XHB_WITNESS_SEQ.NEXTVAL
    INTO  :NEW.WITNESS_ID
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

CREATE OR REPLACE TRIGGER XHB_WLL_CONTROL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_WLL_CONTROL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WLL_CONTROL') = 1) THEN

    INSERT INTO AUD_WLL_CONTROL 
    VALUES (:old.wll_control_id, 
            :old.status, 
            :old.expiry_date, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.xml_document_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_WLL_CONTROL_BIR_TR
  BEFORE INSERT
  ON XHB_WLL_CONTROL
  FOR EACH ROW

BEGIN

  IF :NEW.WLL_CONTROL_ID IS NULL THEN

    SELECT XHB_WLL_CONTROL_SEQ.NEXTVAL
    INTO   :NEW.WLL_CONTROL_ID
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

CREATE OR REPLACE TRIGGER XHB_WLL_DOCUMENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_WLL_DOCUMENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_WLL_DOCUMENT') = 1) THEN

    INSERT INTO AUD_WLL_DOCUMENT 
    VALUES (:old.wll_document_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.wll_recipient_id, 
            :old.wll_control_id, 
            :old.xml_document_id,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_WLL_DOCUMENT_BIR_TR
  BEFORE INSERT
  ON XHB_WLL_DOCUMENT
  FOR EACH ROW

BEGIN

  IF :NEW.WLL_DOCUMENT_ID IS NULL THEN

    SELECT XHB_WLL_DOCUMENT_SEQ.NEXTVAL
    INTO   :NEW.WLL_DOCUMENT_ID
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

CREATE OR REPLACE TRIGGER XHB_WLL_RECIPIENT_BIR_TR
  BEFORE INSERT
  ON XHB_WLL_RECIPIENT
  FOR EACH ROW

BEGIN

  IF :NEW.WLL_RECIPIENT_ID IS NULL THEN

    SELECT XHB_WLL_RECIPIENT_SEQ.NEXTVAL
    INTO   :NEW.WLL_RECIPIENT_ID
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
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_XGROUP_BIR_TR
  BEFORE INSERT
  ON XHB_XGROUP
  FOR EACH ROW

BEGIN

  IF :NEW.XGROUP_ID IS NULL THEN

    SELECT XHB_XGROUP_SEQ.NEXTVAL
    INTO   :NEW.XGROUP_ID
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

CREATE OR REPLACE TRIGGER XHB_XGROUP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_XGROUP
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XGROUP') = 1) THEN

    INSERT INTO AUD_XGROUP 
    VALUES (:old.XGROUP_ID, 
            :old.ROLES, 
            :old.TYPE, 
            :old.DESCRIPTION, 
            :old.NAME, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_XML_DOCUMENT_BUR_TR
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

    INSERT INTO AUD_XML_DOCUMENT 
    VALUES (:old.xml_document_id, 
            :old.date_created, 
            :old.document_title, 
            :old.xml_document, 
            :old.status, 
            :old.expiry_date, 
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

CREATE OR REPLACE TRIGGER XHB_XML_DOCUMENT_BIR_TR
  BEFORE INSERT
  ON XHB_XML_DOCUMENT
  FOR EACH ROW

BEGIN

  IF :NEW.XML_DOCUMENT_ID IS NULL THEN

    SELECT XHB_XML_DOCUMENT_SEQ.NEXTVAL
    INTO   :NEW.XML_DOCUMENT_ID
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

CREATE OR REPLACE TRIGGER XHB_XROLE_BIR_TR
  BEFORE INSERT
  ON XHB_XROLE
  FOR EACH ROW

BEGIN

  IF :NEW.XROLE_ID IS NULL THEN

    SELECT XHB_XROLE_SEQ.NEXTVAL
    INTO   :NEW.XROLE_ID
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

CREATE OR REPLACE TRIGGER XHB_XROLE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_XROLE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XROLE') = 1) THEN

    INSERT INTO AUD_XROLE 
    VALUES (:old.XROLE_ID, 
            :old.GROUPS, 
            :old.TYPE, 
            :old.DESCRIPTION, 
            :old.NAME, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

create or replace trigger XHB_CASE_I_IMP_EXP
  AFTER UPDATE ON XHB_CASE
  FOR EACH ROW
  
BEGIN

  IF (:NEW.EXPORT_CHARGES = 'R') THEN
    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('CI','R', :OLD.CASE_ID, :OLD.COURT_ID);
  END IF;

  IF (:NEW.RESULTS_VERIFIED = 'R') THEN
    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('R','R', :OLD.CASE_ID, :OLD.COURT_ID);
  END IF;

  IF (:NEW.IND_CHANGE_STATUS= 'R') THEN
    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID)
    VALUES ('IC','R', :OLD.CASE_ID, :OLD.COURT_ID);
  END IF;

END;
/

create or replace trigger XHB_JOINDER_IMP_EXP
  AFTER UPDATE ON XHB_JOINDER_XML
  FOR EACH ROW

DECLARE

  l_caseId NUMBER(8) := NULL;
  l_courtId NUMBER(8) := NULL;
  
BEGIN

  IF (:NEW.STATUS = 'R') THEN

    SELECT CASE_ID,
           COURT_ID
    INTO   l_caseId,
           l_courtId
    FROM   XHB_CASE
    WHERE  CASE_ID IN (SELECT CASE_ID
                       FROM   XHB_CHARGE
                       WHERE  CHARGE_ID IN (SELECT DISTINCT FIRST_VALUE(charge_id) OVER () 
                                            FROM   XHB_JOINDER_CHARGE
                                            WHERE  JOINDER_ID = :NEW.JOINDER_ID));

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE,
                                         STATUS_CODE,
                                         CASE_ID,
                                         COURT_ID)
                                 VALUES ('CI',
                                         'R',
                                         l_caseId,
                                         l_courtId);

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



CREATE OR REPLACE TRIGGER XHB_LEGAL_AID_ORDER_BIR_TR
  BEFORE INSERT
  ON XHB_LEGAL_AID_ORDER
  FOR EACH ROW

BEGIN

  IF :NEW.LEGAL_AID_ORDER_ID IS NULL THEN

    SELECT XHB_LEGAL_AID_ORDER_SEQ.NEXTVAL
    INTO   :NEW.LEGAL_AID_ORDER_ID
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


CREATE OR REPLACE TRIGGER XHB_LEGAL_AID_ORDER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEGAL_AID_ORDER
  FOR EACH ROW

/* default body for XHB_LEGAL_AID_ORDER_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEGAL_AID_ORDER') = 1) THEN

    INSERT INTO AUD_LEGAL_AID_ORDER (
      LEGAL_AID_ORDER_ID,
      CREST_LEO_ID,
      DEFENDANT_ON_CASE_ID,
      LAST_UPDATE_DATE,
      CREATION_DATE,
      CREATED_BY,
      LAST_UPDATED_BY,
      VERSION,
      OBS_IND,
      INSERT_EVENT
      )
    VALUES (
      :old.LEGAL_AID_ORDER_ID,
      :old.CREST_LEO_ID,
      :old.DEFENDANT_ON_CASE_ID,
      :old.LAST_UPDATE_DATE,
      :old.CREATION_DATE,
      :old.CREATED_BY,
      :old.LAST_UPDATED_BY,
      :old.VERSION,
      :old.OBS_IND,
      l_trig_event);

  END IF;

END;
/


CREATE OR REPLACE TRIGGER XHB_LEO_ADV_LINK_BIR_TR
  BEFORE INSERT
  ON XHB_LEO_ADV_LINK
  FOR EACH ROW

BEGIN

  IF :NEW.LEO_ADV_LINK_ID IS NULL THEN

    SELECT XHB_LEO_ADV_LINK_SEQ.NEXTVAL
    INTO   :NEW.LEO_ADV_LINK_ID
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


CREATE OR REPLACE TRIGGER XHB_LEO_ADV_LINK_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEO_ADV_LINK
  FOR EACH ROW

/* default body for XHB_LEO_ADV_LINK_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEO_ADV_LINK') = 1) THEN

    INSERT INTO AUD_LEO_ADV_LINK (
           LEO_ADV_LINK_ID,
           LEGAL_AID_ORDER_ID,
           DEFENDANT_ON_CASE_ID,
           REF_ADVOCATE_ID,
           CREST_ADV_CATEGORY,
           AVAILABLE,
           NEW_ROW_FLAG,
           LAST_UPDATE_DATE,
           CREATION_DATE,
           CREATED_BY,
           LAST_UPDATED_BY,
           VERSION,
           OBS_IND,
           INSERT_EVENT,
           CREST_POST_NUMBER)
    VALUES (
           :old.LEO_ADV_LINK_ID,
           :old.LEGAL_AID_ORDER_ID,
           :old.DEFENDANT_ON_CASE_ID,
           :old.REF_ADVOCATE_ID,
           :old.CREST_ADV_CATEGORY,
           :old.AVAILABLE,
	   :old.NEW_ROW_FLAG,
           :old.LAST_UPDATE_DATE,
           :old.CREATION_DATE,
           :old.CREATED_BY,
           :old.LAST_UPDATED_BY,
           :old.VERSION,
           :old.OBS_IND,
           l_trig_event,
           :old.CREST_POST_NUMBER
           );

  END IF;

END;
/


CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BIR_TR
  BEFORE INSERT
  ON XHB_SH_LEG_REP
  FOR EACH ROW

BEGIN

  IF :NEW.SH_LEG_REP_ID IS NULL THEN

    SELECT XHB_SH_LEG_REP_SEQ.NEXTVAL
    INTO   :NEW.SH_LEG_REP_ID
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


CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_LEG_REP
  FOR EACH ROW

/* default body for XHB_SH_LEG_REP_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_LEG_REP') = 1) THEN

    INSERT INTO AUD_SH_LEG_REP
    VALUES (:old.SH_LEG_REP_ID,
            :old.CREST_SEQUENCE_NO,
            :old.LEGAL_ROLE,
            :old.IS_SIGNED_IN,
            :old.SOL_FIRM_OR_REF_LEGAL_REP,
            :old.SCHED_HEAR_DEF_ID,
            :old.REF_LEGAL_REP_ID,
            :old.VERSION,
            :old.LAST_UPDATED_BY,
            :old.CREATED_BY,
            :old.CREATION_DATE,
            :old.LAST_UPDATE_DATE,
            :old.CC_INFO_ID,
            :old.REF_SOLICITOR_FIRM_ID,
            :old.REF_DEFENCE_CATEGORY_ID,
            :old.SCHEDULED_HEARING_ID,
            :old.SUB_INST,
            :old.SUBSTITUTED_REF_LEGAL_REP_ID,
            l_trig_event);

  END IF;

END;
/

