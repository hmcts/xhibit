CREATE OR REPLACE TRIGGER XHB_D20_OFFENCE_LINK_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_D20_OFFENCE_LINK
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_D20_OFFENCE_LINK') = 1) THEN
	INSERT INTO AUD_D20_OFFENCE_LINK
     		(D20_OFFENCE_LINK_ID,
		DEFENDANT_ON_CASE_ID,
		SEQ_NO,
		DVLA_OFFENCE_CODE,
		REF_OFFENCE_ID,
		CONVICTION_DATE,
		INT_D20,
		INT_D20_DATE,
		FINAL_D20,
		FINAL_D20_DATE,
		OBS_IND,
		CREATED_BY,
		LAST_UPDATED_BY,
		CREATION_DATE,
		LAST_UPDATE_DATE,
		VERSION,
		INSERT_EVENT)
	VALUES (:old.D20_OFFENCE_LINK_ID,
		:old.DEFENDANT_ON_CASE_ID,
		:old.SEQ_NO,
		:old.DVLA_OFFENCE_CODE,
		:old.REF_OFFENCE_ID,
		:old.CONVICTION_DATE,
		:old.INT_D20,
		:old.INT_D20_DATE,
		:old.FINAL_D20,
		:old.FINAL_D20_DATE,
		:old.OBS_IND,
		:old.CREATED_BY,
		:old.LAST_UPDATED_BY,
		:old.CREATION_DATE,
		:old.LAST_UPDATE_DATE,
		:old.VERSION,
		l_trig_event);
  END IF;


END;
/
