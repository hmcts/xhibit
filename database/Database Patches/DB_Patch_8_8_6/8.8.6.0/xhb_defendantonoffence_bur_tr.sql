create or replace TRIGGER XHB_DEFENDANTONOFFENCE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_OFFENCE   FOR EACH ROW
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_OFFENCE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_OFFENCE
    (DEFENDANT_ON_OFFENCE_ID,
     APPEAL_AGAINST_TYPE,
    DEFENDANT_ON_CASE_ID,
    OFFENCE_ID,
    LAST_UPDATE_DATE,
    CREATION_DATE,
    CREATED_BY,
    LAST_UPDATED_BY,
    VERSION,
    OBS_IND,
    IS_STAYED,
    CRN_ID,
    VCO_FLAG,
    VCO_DATE,
    INSERT_EVENT,
    SEQ_NO,
    ARREST_DATE,
    CHARGE_DATE,
    IS_COMMITTED_ON_BAIL,
    INTERIM_D20,
    DAR_RETENTION_POLICY_ID
    )
    VALUES (:OLD.DEFENDANT_ON_OFFENCE_ID,
            :OLD.APPEAL_AGAINST_TYPE,
            :OLD.DEFENDANT_ON_CASE_ID,
            :OLD.OFFENCE_ID,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.OBS_IND,
            :OLD.IS_STAYED,
            :OLD.CRN_ID,
            :OLD.VCO_FLAG,
            :OLD.VCO_DATE,
            l_trig_event,
            :OLD.seq_no,
            :OLD.arrest_date,
            :OLD.charge_date,
            :OLD.is_committed_on_bail,
            :OLD.INTERIM_D20,
            :OLD.DAR_RETENTION_POLICY_ID
            );

  END IF;

END;
/