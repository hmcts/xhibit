create or replace TRIGGER "XHIBIT".XHB_DISPOSAL2_BUR_TR
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
  
  /* check to see if id is null, if so set it to curr id */
  IF(:NEW.DIS_ID IS NULL) THEN
    :NEW.DIS_ID  := :NEW.DISPOSAL2_ID;
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