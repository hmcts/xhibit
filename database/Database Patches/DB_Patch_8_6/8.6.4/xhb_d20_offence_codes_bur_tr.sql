CREATE OR REPLACE TRIGGER XHB_D20_OFFENCE_CODES_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_D20_OFFENCE_CODES
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_D20_OFFENCE_CODES') = 1) THEN
	INSERT INTO AUD_D20_OFFENCE_CODES
     		(OFFENCE_CODE_ID,
		OFFENCE_CODE,
		REASON_TYPE,
		REASON,
		PENALTY_POINTS,
		CREATED_BY,
		LAST_UPDATED_BY,
		CREATION_DATE,
		LAST_UPDATE_DATE,
		OBS_IND,
		VERSION,
		INSERT_EVENT)
	VALUES (:old.OFFENCE_CODE_ID,
		:old.OFFENCE_CODE,
		:old.REASON_TYPE,
		:old.REASON,
		:old.PENALTY_POINTS,
		:old.CREATED_BY,
		:old.LAST_UPDATED_BY,
		:old.CREATION_DATE,
		:old.LAST_UPDATE_DATE,
		:old.OBS_IND,
		:old.VERSION,
		l_trig_event);
  END IF;


END;
/
