ALTER TABLE XHIBIT.XHB_DEFENDANT_ON_CASE ADD
(	FORM_NG_SENT_DATE DATE, 
	CACD_APPEAL_RESULT_DATE DATE
 );
 
ALTER TABLE XHIBIT.AUD_DEFENDANT_ON_CASE ADD
(	FORM_NG_SENT_DATE DATE, 
	CACD_APPEAL_RESULT_DATE DATE
 );
 

 create or replace TRIGGER "XHIBIT".XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_DEFENDANT_ON_CASE   FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANTONCASE_BUR_TR */
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_CASE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_CASE
     (DEFENDANT_ON_CASE_ID,
            NO_OF_TICS,
            FINAL_DRIVING_LICENCE_STATUS,
            PTIURN,
            IS_JUVENILE,
            IS_MASKED,
            MASKED_NAME,
            CASE_ID,
            DEFENDANT_ID,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            OBS_IND,
            results_verified,
            defendant_number,
            date_of_committal,
            PNC_ID,
            COLLECT_MAGISTRATE_COURT_ID,
            INSERT_EVENT,
            current_BC_status,
            asn,
            bench_warrant_exec_date,
            comm_bc_status,
            bc_status_bw_executed,
            special_cir_found,
            custodial,
            suspended,
            serious_drug_offence,
            recommended_deportation,
            date_exported,
            first_fixed_trial,
            first_hearing_type,
            public_display_hide,
	    AMENDED_DATE_EXPORTED,
	    AMENDED_REASON,
	    HATE_IND,
	    HATE_TYPE,
	    HATE_SENT_IND,
		CUSTODY_TIME_LIMIT,
		FORM_NG_SENT_DATE, 
		CACD_APPEAL_RESULT_DATE)
    VALUES (:old.DEFENDANT_ON_CASE_ID,
            :old.NO_OF_TICS,
            :old.FINAL_DRIVING_LICENCE_STATUS,
            :old.PTIURN,
            :old.IS_JUVENILE,
            :old.IS_MASKED,
            :old.MASKED_NAME,
            :old.CASE_ID,
            :old.DEFENDANT_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.OBS_IND,
            :old.results_verified,
            :old.defendant_number,
            :old.date_of_committal,
            :old.PNC_ID,
            :old.COLLECT_MAGISTRATE_COURT_ID,
            l_trig_event,
            :old.current_BC_status,
            :old.asn,
            :old.bench_warrant_exec_date,
            :old.comm_bc_status,
            :old.bc_status_bw_executed,
            :old.special_cir_found,
            :old.custodial,
            :old.suspended,
            :old.serious_drug_offence,
            :old.recommended_deportation,
            :old.date_exported,
            :old.first_fixed_trial,
            :old.first_hearing_type,
            :old.public_display_hide,
	    :old.AMENDED_DATE_EXPORTED,
	    :old.AMENDED_REASON,
	    :old.HATE_IND,
	    :old.HATE_TYPE,
	    :old.HATE_SENT_IND,
		:old.CUSTODY_TIME_LIMIT,
		:old.FORM_NG_SENT_DATE, 
		:old.CACD_APPEAL_RESULT_DATE);

  END IF;

END;

/
commit;
