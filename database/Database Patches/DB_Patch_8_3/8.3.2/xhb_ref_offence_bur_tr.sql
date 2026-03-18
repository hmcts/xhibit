CREATE OR REPLACE TRIGGER XHB_REFOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_OFFENCE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_REFOFFENCE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_OFFENCE') = 1) THEN

    INSERT INTO AUD_REF_OFFENCE
    VALUES (:old.REF_OFFENCE_ID,
            :old.OFFENCE_CODE,
            :old.OFFENCE_DESC,
            :old.HO_PROC_TYPE,
            :old.HO_CLASS,
            :old.HO_SUB_CLASS,
            :old.DVLC_CODE,
            :old.STATUTE,
            :old.OFFENCE_CLASS,
            :old.ACT_SECTION,
            :old.OBS_IND,
            :old.OFFENCE_DESC2,
            :old.OFFENCE_GROUP,
            :old.COURT_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            l_trig_event,
            :old.BAIL_ACT);

  END IF;

END;
/
show errors;

