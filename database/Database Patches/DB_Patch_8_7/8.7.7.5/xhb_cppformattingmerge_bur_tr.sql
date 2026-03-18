CREATE OR REPLACE TRIGGER XHB_CPPFORMATTINGMERGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CPP_FORMATTING_MERGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CPP_FORMATTING_MERGE') = 1) THEN

    INSERT INTO AUD_CPP_FORMATTING_MERGE
    VALUES (:old.CPP_FORMATTING_MERGE_ID,
	:old.CPP_FORMATTING_ID,
	:old.FORMATTING_ID,
	:old.XHIBIT_CLOB_ID,
	:old.COURT_ID,
	:old.CREATION_DATE,
	:old.LANGUAGE,
	:old.OBS_IND,
	:old.VERSION,
	:old.LAST_UPDATE_DATE,
	:old.LAST_UPDATED_BY,
	:old.CREATED_BY,
            l_trig_event);

  END IF;

END;
/
