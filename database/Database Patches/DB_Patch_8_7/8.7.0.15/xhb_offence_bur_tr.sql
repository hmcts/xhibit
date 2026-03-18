create or replace TRIGGER XHIBIT.XHB_OFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_OFFENCE   FOR EACH ROW
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_OFFENCE') = 1) THEN

    INSERT INTO AUD_OFFENCE
    VALUES (:OLD.OFFENCE_ID,
            :OLD.CREST_OFFENCE_ID,
            :OLD.CREST_OFFENCE_SEQ_NO,
            :OLD.CREST_OFFENCE_FREETEXT,
            :OLD.MULTIPLE,
            :OLD.CREST_HOO_CLASS_FREETEXT,
            :OLD.CREST_HOO_SUBCLASS_FREETEXT,
            :OLD.REF_OFFENCE_ID,
            :OLD.CHARGE_ID,
            :OLD.OBS_IND,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.REF_SYSTEM_CODE_ID,
            l_trig_event,
            :OLD.start_date,
            :OLD.end_date,
            :OLD.force_location_code,
            :OLD.location_address_id,
	    :OLD.appeal_type);

  END IF;

END;
/