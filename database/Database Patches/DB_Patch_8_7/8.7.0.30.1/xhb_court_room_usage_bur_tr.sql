CREATE OR REPLACE TRIGGER XHB_COURT_ROOM_USAGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_ROOM_USAGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_ROOM_USAGE') = 1) THEN
	INSERT INTO AUD_COURT_ROOM_USAGE
     		(COURT_ROOM_USAGE_ID,
    		COURT_ROOM_ID,
		AM_TIME_CIV_HOURS,
		AM_TIME_CIV_MINS,
		AM_TIME_HOURS,
		AM_TIME_MINS,
		PM_TIME_CIV_HOURS,
		PM_TIME_CIV_MINS,
		PM_TIME_HOURS,
		PM_TIME_MINS,
    		SITTING_DATE,
		LAST_UPDATE_DATE,	
		CREATION_DATE,
		LAST_UPDATED_BY,
		CREATED_BY,
		VERSION,
		OBS_IND,
		INSERT_EVENT)
	VALUES (:old.COURT_ROOM_USAGE_ID,
    		:old.COURT_ROOM_ID,
		:old.AM_TIME_CIV_HOURS,
		:old.AM_TIME_CIV_MINS,
		:old.AM_TIME_HOURS,
		:old.AM_TIME_MINS,
		:old.PM_TIME_CIV_HOURS,
		:old.PM_TIME_CIV_MINS,
		:old.PM_TIME_HOURS,
		:old.PM_TIME_MINS,
    		:old.SITTING_DATE,
		:old.LAST_UPDATE_DATE,	
		:old.CREATION_DATE,
		:old.LAST_UPDATED_BY,
		:old.CREATED_BY,
		:old.VERSION,
		:old.OBS_IND,
		l_trig_event);
  END IF;


END;
/
