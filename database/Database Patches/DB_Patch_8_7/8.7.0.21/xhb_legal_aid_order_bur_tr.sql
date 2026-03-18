/*
 *    -------------------------------------------------------------------------------
 *     CTX-1474
 *    -------------------------------------------------------------------------------
 */

create or replace TRIGGER XHB_LEGAL_AID_ORDER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEGAL_AID_ORDER
  FOR EACH ROW

/* default body for XHB_LEGAL_AID_ORDER_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEGAL_AID_ORDER') = 1) THEN

    INSERT INTO AUD_LEGAL_AID_ORDER (
      LEGAL_AID_ORDER_ID,
      CREST_LEO_ID,
      DEFENDANT_ON_CASE_ID,
      LAST_UPDATE_DATE,
      CREATION_DATE,
      CREATED_BY,
      LAST_UPDATED_BY,
      VERSION,
      OBS_IND,
      INSERT_EVENT,
	  CASE_PROS_AGENCY_ID,
	  ORDER_DATE,
	  GRANTED_BY,
	  PSD_RO_REF,
	  NUMBER_OF_ADVOCATES,
	  NUMBER_OF_QCS,
	  DATE_OF_REVOCATION,
	  REASON_FOR_REVOCATION_ID
      )
    VALUES (
      :old.LEGAL_AID_ORDER_ID,
      :old.CREST_LEO_ID,
      :old.DEFENDANT_ON_CASE_ID,
      :old.LAST_UPDATE_DATE,
      :old.CREATION_DATE,
      :old.CREATED_BY,
      :old.LAST_UPDATED_BY,
      :old.VERSION,
      :old.OBS_IND,
      l_trig_event,
	  :old.CASE_PROS_AGENCY_ID,
	  :old.ORDER_DATE,
	  :old.GRANTED_BY,
	  :old.PSD_RO_REF,
	  :old.NUMBER_OF_ADVOCATES,
	  :old.NUMBER_OF_QCS,
	  :old.DATE_OF_REVOCATION,
	  :old.REASON_FOR_REVOCATION_ID);

  END IF;

END;
/
