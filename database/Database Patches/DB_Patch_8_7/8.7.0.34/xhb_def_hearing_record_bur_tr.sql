CREATE OR REPLACE TRIGGER "XHIBIT".XHB_DEF_HEARING_RECORD_BUR_TR
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

    INSERT INTO AUD_DEF_HEARING_RECORD (
              HEARING_RECORD_ID,
              REF_ADJOURNMENT_ID,
              ADJOURNED_DATE,
              IS_ADJOURNED,
              COLLECT_MAGISTRATE_COURT_ID,
              START_DATE_NEW_BAIL_STATUS,
              NEW_BAIL_STATUS,
              DATE_BAIL_APPLICATION,
              SUBST_BAIL_APPLICATION,
              ORAL_EVIDENCE,
              RESULT_BAIL_APPLICATION,
              IS_HRA_APPLICATION,
              REF_DEF_HEARING_TYPE_ID,
              END_BAIL_STATUS,
              START_BAIL_STATUS,
              DEFENDANT_ON_CASE_ID,
              HEARING_ID,
              VERSION,
              LAST_UPDATED_BY,
              CREATED_BY,
              CREATION_DATE,
              LAST_UPDATE_DATE,
              HEARING_DATES_FREETEXT_1,
              HEARING_DATES_FREETEXT_2,
              HEARING_DATES_FREETEXT_3,
              HEARING_START_DATE,
              HEARING_END_DATE,
              LAST_CALCULATED_DURATION,
              MP_HEARING_TYPE,
              INSERT_EVENT,
              TRIAL_IN_DEF_ABSENCE,
              SENTENCE_IN_DEF_ABSENCE,
              ADJOURNED_FOR_POCA_HRG,
              FORMA_STATUS,
              FORMA_COURT_CLERK
    ) VALUES (
            :OLD.HEARING_RECORD_ID,
            :OLD.REF_ADJOURNMENT_ID,
            :OLD.ADJOURNED_DATE,
            :OLD.IS_ADJOURNED,
            :OLD.COLLECT_MAGISTRATE_COURT_ID,
            :OLD.START_DATE_NEW_BAIL_STATUS,
            :OLD.NEW_BAIL_STATUS,
            :OLD.DATE_BAIL_APPLICATION,
            :OLD.SUBST_BAIL_APPLICATION,
            :OLD.ORAL_EVIDENCE,
            :OLD.RESULT_BAIL_APPLICATION,
            :OLD.IS_HRA_APPLICATION,
            :OLD.REF_DEF_HEARING_TYPE_ID,
            :OLD.END_BAIL_STATUS,
            :OLD.START_BAIL_STATUS,
            :OLD.DEFENDANT_ON_CASE_ID,
            :OLD.HEARING_ID,
            :OLD.VERSION,
            :OLD.LAST_UPDATED_BY,
            :OLD.CREATED_BY,
            :OLD.CREATION_DATE,
            :OLD.LAST_UPDATE_DATE,
            :OLD.HEARING_DATES_FREETEXT_1,
            :OLD.HEARING_DATES_FREETEXT_2,
            :OLD.HEARING_DATES_FREETEXT_3,
            :OLD.HEARING_START_DATE,
            :OLD.HEARING_END_DATE,
            :OLD.LAST_CALCULATED_DURATION,
            :OLD.MP_HEARING_TYPE,
            l_trig_event,
            :OLD.TRIAL_IN_DEF_ABSENCE,
            :OLD.SENTENCE_IN_DEF_ABSENCE,
            :OLD.ADJOURNED_FOR_POCA_HRG,
            :OLD.FORMA_STATUS,
            :OLD.FORMA_COURT_CLERK);

  END IF;

END xhb_def_hearing_record_bur_tr;
/