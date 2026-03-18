CREATE OR REPLACE TRIGGER xhb_case_history_bur_tr
  BEFORE UPDATE OR DELETE
  ON xhb_case_history
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_HISTORY') = 1) THEN

    INSERT INTO aud_case_history
    (CASE_HISTORY_ID
    ,CASE_TYPE
    ,COURT_ID
    ,CASE_NUMBER
    ,PSD_CT_CODE
    ,COMMITTAL_DATE
    ,REASON_DELETED
    ,CASE_TITLE
    ,DATE_ARCHIVED
    ,SENT_FOR_TRIAL_DATE
    ,LAST_UPDATE_DATE
    ,CREATION_DATE
    ,LAST_UPDATED_BY
    ,CREATED_BY
    ,VERSION
    ,insert_event
    )
    VALUES (
    :OLD.CASE_HISTORY_ID
    ,:OLD.CASE_TYPE
    ,:OLD.COURT_ID
    ,:OLD.CASE_NUMBER
    ,:OLD.PSD_CT_CODE
    ,:OLD.COMMITTAL_DATE
    ,:OLD.REASON_DELETED
    ,:OLD.CASE_TITLE
    ,:OLD.DATE_ARCHIVED
    ,:OLD.SENT_FOR_TRIAL_DATE
    ,:OLD.LAST_UPDATE_DATE
    ,:OLD.CREATION_DATE
    ,:OLD.LAST_UPDATED_BY
    ,:OLD.CREATED_BY
    ,:OLD.VERSION
    ,l_trig_event
   );

  END IF;

END xhb_case_history_bur_tr;
/