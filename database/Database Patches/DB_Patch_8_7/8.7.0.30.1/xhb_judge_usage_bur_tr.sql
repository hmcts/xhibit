CREATE OR REPLACE TRIGGER XHB_JUDGE_USAGE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_JUDGE_USAGE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_JUDGE_USAGE') = 1) THEN
	INSERT INTO AUD_JUDGE_USAGE
     		(JUDGE_USAGE_ID,
    		COURT_ROOM_ID,
		COURT_CHAMBERS_IND,
		REF_JUDGE_ID,
		MAIN_WORK_TYPE,
    		SITTING_DATE,
		TYPE_OF_WORK,
		LAST_UPDATE_DATE,	
		CREATION_DATE,
		LAST_UPDATED_BY,
		CREATED_BY,
		VERSION,
		OBS_IND,
		INSERT_EVENT)
	VALUES (:old.JUDGE_USAGE_ID,
    		:old.COURT_ROOM_ID,
		:old.COURT_CHAMBERS_IND,
		:old.REF_JUDGE_ID,
		:old.MAIN_WORK_TYPE,
    		:old.SITTING_DATE,
		:old.TYPE_OF_WORK,
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
