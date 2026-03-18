CREATE OR REPLACE TRIGGER XHB_SCHEDULEDHEARING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SCHEDULED_HEARING
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_SCHEDULEDHEARING_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SCHEDULED_HEARING') = 1) THEN

    INSERT INTO AUD_SCHEDULED_HEARING (
            SCHEDULED_HEARING_ID,
            SEQUENCE_NO,
            NOT_BEFORE_TIME,
            ORIGINAL_TIME,
            LISTING_NOTE,
            HEARING_PROGRESS,
            SITTING_ID,
            HEARING_ID,
            MOVED_FROM,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            LINKED_SH_ID,
            END_TIME,
            START_TIME,
            DATE_OF_HEARING,
            IS_CASE_ACTIVE,
            MOVED_FROM_COURT_ROOM_ID,
            ADD_HEARING_USED,
            INSERT_EVENT
    ) VALUES (
            :old.SCHEDULED_HEARING_ID,
            :old.SEQUENCE_NO,
            :old.NOT_BEFORE_TIME,
            :old.ORIGINAL_TIME,
            :old.LISTING_NOTE,
            :old.HEARING_PROGRESS,
            :old.SITTING_ID,
            :old.HEARING_ID,
            :old.MOVED_FROM,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.LINKED_SH_ID,
            :old.END_TIME,
            :old.START_TIME,
            :old.DATE_OF_HEARING,
            :old.is_case_active,
            :old.MOVED_FROM_COURT_ROOM_ID,
            :old.ADD_HEARING_USED,
            l_trig_event);

  END IF;

END;
/
show errors;
