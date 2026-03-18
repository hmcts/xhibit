create or replace TRIGGER XHB_LEGAL_AID_AMEND_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEGAL_AID_AMENDMENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEGAL_AID_AMENDMENT') = 1) THEN
	INSERT INTO AUD_LEGAL_AID_AMENDMENT
     		(LEGAL_AID_AMENDMENT_ID,
		LEGAL_AID_ORDER_ID,
		CHANGE_TYPE_ID,
		AMENDMENT_DATE,
		CREATED_BY,
		LAST_UPDATED_BY,
		CREATION_DATE,
		LAST_UPDATE_DATE,
		VERSION,
		OBS_IND,
		INSERT_EVENT)
	VALUES (:old.LEGAL_AID_AMENDMENT_ID,
		:old.LEGAL_AID_ORDER_ID,
		:old.CHANGE_TYPE_ID,
		:old.AMENDMENT_DATE,
		:old.CREATED_BY,
		:old.LAST_UPDATED_BY,
		:old.CREATION_DATE,
		:old.LAST_UPDATE_DATE,
		:old.VERSION,
		:old.OBS_IND,
		l_trig_event);
  END IF;


END;
/