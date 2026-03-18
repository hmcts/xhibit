create or replace TRIGGER XHB_PROSECUTORREFSOLFRM_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM FOR EACH ROW
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PROSECUTOR_REF_SOL_FIRM') = 1) THEN

    INSERT INTO AUD_PROSECUTOR_REF_SOL_FIRM (
	        prosecutor_ref_sol_firm_id,
            crest_cpf_id,
            rep_type,
            rep_st_date,
            rep_end_date,
            ref_solicitor_firm_id,
            case_pros_agency_id,
            last_update_date,
            creation_date,
            created_by,
            last_updated_by,
	        version,
	        solicitor_ref,
            insert_event, 
            OBS_IND,
			LEGAL_AID_ORDER_ID)
    VALUES (:OLD.prosecutor_ref_sol_firm_id,
            :OLD.crest_cpf_id,
            :OLD.rep_type,
            :OLD.rep_st_date,
            :OLD.rep_end_date,
            :OLD.ref_solicitor_firm_id,
            :OLD.case_pros_agency_id,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
		    :OLD.solicitor_ref,
            l_trig_event,
            :old.OBS_IND,
			:OLD.LEGAL_AID_ORDER_ID);

  END IF;

END;
/
