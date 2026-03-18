CREATE OR REPLACE TRIGGER "XHIBIT".XHB_DEF_HEARING_RECORD_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEF_HEARING_RECORD
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEF_HEARING_RECORD_BUR_TR */

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
              trial_in_def_absence,
              sentence_in_def_absence,
			  adjourned_for_poca_hrg,
			  S41_APPLICATION,
			  S41_GRANTED,
			  S41_APPLICATION_MADE		  
    ) VALUES (
            :old.HEARING_RECORD_ID,
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
            :old.hearing_start_date,
            :old.hearing_end_date,
            :old.last_calculated_duration,
            :old.mp_hearing_type,
            l_trig_event,
            :old.trial_in_def_absence,
            :old.sentence_in_def_absence,
			:old.adjourned_for_poca_hrg,
			:old.S41_APPLICATION,
			:old.S41_GRANTED,
			:old.S41_APPLICATION_MADE	);

  END IF;

END xhb_def_hearing_record_bur_tr;
/