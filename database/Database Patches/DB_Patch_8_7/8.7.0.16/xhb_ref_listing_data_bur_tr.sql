CREATE OR REPLACE TRIGGER XHB_REF_LISTING_DATA_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_LISTING_DATA
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_LISTING_DATA') = 1) THEN
	INSERT INTO AUD_REF_LISTING_DATA
     	(REF_LISTING_DATA_ID,
	 	REF_DATA_TYPE,
	 	REF_DATA_VALUE,
		CREATED_BY,
		LAST_UPDATED_BY,
		CREATION_DATE,
		LAST_UPDATE_DATE,
		VERSION,
		INSERT_EVENT,
                OBS_IND)
	VALUES (:old.REF_LISTING_DATA_ID,
	 	:old.REF_DATA_TYPE,
	 	:old.REF_DATA_VALUE,
		:old.CREATED_BY,
		:old.LAST_UPDATED_BY,
		:old.CREATION_DATE,
		:old.LAST_UPDATE_DATE,
		:old.VERSION,
		l_trig_event,
                :old.OBS_IND);
  END IF;


END;
/