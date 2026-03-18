CREATE OR REPLACE TRIGGER xhb_mis_event_log_bur_tr
  BEFORE UPDATE OR DELETE
  ON xhb_mis_event_log
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_MIS_EVENT_LOG') = 1) THEN

    INSERT INTO AUD_MIS_EVENT_LOG (
     MIS_EVENT_LOG_ID
    ,REF_EVENT_DESCRIPTION_ID
    ,EVENT_TIME
    ,COURT_ID
    ,CASE_ID
    ,DEFENDANT_ON_CASE_ID
    ,EVENT_TEXT
    ,OBS_IND
    ,LAST_UPDATE_DATE
    ,CREATION_DATE
    ,LAST_UPDATED_BY
    ,CREATED_BY
    ,VERSION
    ,insert_event
    )
    VALUES (
    :OLD.MIS_EVENT_LOG_ID
    ,:OLD.REF_EVENT_DESCRIPTION_ID
    ,:OLD.EVENT_TIME
    ,:OLD.COURT_ID
    ,:OLD.CASE_ID
    ,:OLD.DEFENDANT_ON_CASE_ID
    ,:OLD.EVENT_TEXT
    ,:OLD.OBS_IND
    ,:OLD.LAST_UPDATE_DATE
    ,:OLD.CREATION_DATE
    ,:OLD.LAST_UPDATED_BY
    ,:OLD.CREATED_BY
    ,:OLD.VERSION
    ,l_trig_event
   );

  END IF;

END xhb_mis_event_log_bur_tr;
/