CREATE OR REPLACE TRIGGER "XHIBIT".XHB_COURTROOM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_ROOM
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURTROOM_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_ROOM') = 1) THEN

    INSERT INTO AUD_COURT_ROOM (COURT_ROOM_ID,
                                COURT_ROOM_NAME,
                                DESCRIPTION,
                                CREST_COURT_ROOM_NO,
                                COURT_SITE_ID,
                                LAST_UPDATE_DATE,
                                CREATION_DATE,
                                CREATED_BY,
                                LAST_UPDATED_BY,
                                VERSION,
                                obs_ind,
                                DISPLAY_NAME,
                                INSERT_EVENT,
                                security_ind,
                                video_ind 
                                )
    VALUES (:old.COURT_ROOM_ID,
            :old.COURT_ROOM_NAME,
            :old.DESCRIPTION,
            :old.CREST_COURT_ROOM_NO,
            :old.COURT_SITE_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.obs_ind,
            :old.DISPLAY_NAME,
            l_trig_event,
            :old.security_ind,
            :old.video_ind
            );

  END IF;

END XHB_COURTROOM_BUR_TR;
/