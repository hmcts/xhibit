create or replace TRIGGER XHB_DEFENDANT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT
  FOR EACH ROW


/* default body for XHB_DEFENDANT_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT') = 1) THEN

    INSERT INTO AUD_DEFENDANT (
	  DEFENDANT_ID,
	  CREST_DEFENDANT_ID,
	  FIRST_NAME,
	  MIDDLE_NAME,
	  SURNAME,
	  INITIALS,
	  DATE_OF_BIRTH,
	  GENDER,
	  LAST_CONVICTION_DATE,
	  IS_COMPANY,
	  LAST_UPDATE_DATE,
	  CREATION_DATE,
	  CREATED_BY,
	  LAST_UPDATED_BY,
	  VERSION,
	  ADDRESS_ID,
	  COURT_ID,
	  PRISON_ID,
	  INSERT_EVENT,
	  PUBLIC_DISPLAY_HIDE,
	  PARENT_GUARDIAN_NAME,
	  ETHNIC_APPEARANCE_CODE,
	  ETHNICITY_SELF_DEFINED,
	  CURRENT_PRISON_STATUS
    ) VALUES (:old.DEFENDANT_ID,
            :old.CREST_DEFENDANT_ID,
            :old.FIRST_NAME,
            :old.MIDDLE_NAME,
            :old.SURNAME,
            :old.INITIALS,
            :old.DATE_OF_BIRTH,
            :old.GENDER,
            :old.LAST_CONVICTION_DATE,
            :old.IS_COMPANY,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.ADDRESS_ID,
            :old.COURT_ID,
            :old.PRISON_ID,
            l_trig_event,
	    :old.PUBLIC_DISPLAY_HIDE,
	    :old.PARENT_GUARDIAN_NAME,
	    :old.ETHNIC_APPEARANCE_CODE,
	    :old.ETHNICITY_SELF_DEFINED,
   	    :old.CURRENT_PRISON_STATUS);

  END IF;

END;
/