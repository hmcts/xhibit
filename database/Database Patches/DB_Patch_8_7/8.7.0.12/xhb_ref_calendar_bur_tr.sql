create or replace TRIGGER "XHIBIT".XHB_REF_CALENDAR_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_CALENDAR
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_CALENDAR') = 1) THEN

    INSERT INTO AUD_REF_CALENDAR (
	    cal_date,
	    court_id,
	    avail,
	    sys_ac_avail,
	    description,
		secure_court,
		video_link,
	    last_update_date,
	    creation_date,
	    created_by,
	    last_updated_by,
	    version,
	    insert_event)
    VALUES (:OLD.cal_date,
	    :OLD.court_id,
	    :OLD.avail,
	    :OLD.sys_ac_avail,
	    :OLD.description,
		:OLD.secure_court,
		:OLD.video_link,
	    :OLD.last_update_date,
	    :OLD.creation_date,
	    :OLD.created_by,
	    :OLD.last_updated_by,
	    :OLD.version,
	    l_trig_event);

  END IF;

END;