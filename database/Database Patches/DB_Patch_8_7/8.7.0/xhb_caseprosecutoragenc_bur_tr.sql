CREATE OR REPLACE TRIGGER XHB_CASEPROSECUTORAGENC_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE_PROSECUTOR_AGENCY
  FOR EACH ROW

/* default body for XHB_CASEPROSECUTORAGENC_BUR_TR */

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

    INSERT INTO AUD_CASE_PROSECUTOR_AGENCY (
            CASE_PROS_AGENCY_ID,
            PROSECUTOR_TYPE,
            CASE_ID,
            REF_PROSECUTOR_AGENCY_ID,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
	    RESPONDENT_STATUS,
            INSERT_EVENT)
    VALUES (:old.CASE_PROS_AGENCY_ID,
            :old.PROSECUTOR_TYPE,
            :old.CASE_ID,
            :old.REF_PROSECUTOR_AGENCY_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
	    :old.RESPONDENT_STATUS,
            l_trig_event);

  END IF;

END;
/