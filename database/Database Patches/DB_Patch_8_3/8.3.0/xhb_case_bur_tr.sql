CREATE OR REPLACE TRIGGER XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_CASE   FOR EACH ROW
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

    IF :NEW.CASE_ID = :OLD.CASE_ID AND
       :NEW.COURT_ID = :OLD.COURT_ID AND
      (:NEW.CASE_NUMBER = :OLD.CASE_NUMBER OR
       (:NEW.CASE_NUMBER IS NULL AND :OLD.CASE_NUMBER IS NULL)) AND
      (:NEW.CASE_TYPE = :OLD.CASE_TYPE OR
       (:NEW.CASE_TYPE IS NULL AND :OLD.CASE_TYPE IS NULL)) AND
      (:NEW.MAG_CONVICTION_DATE = :OLD.MAG_CONVICTION_DATE OR
       (:NEW.MAG_CONVICTION_DATE IS NULL AND :OLD.MAG_CONVICTION_DATE IS NULL)) AND
      (:NEW.CASE_SUB_TYPE = :OLD.CASE_SUB_TYPE OR
       (:NEW.CASE_SUB_TYPE IS NULL AND :OLD.CASE_SUB_TYPE IS NULL)) AND
      (:NEW.CASE_TITLE = :OLD.CASE_TITLE OR
       (:NEW.CASE_TITLE IS NULL AND :OLD.CASE_TITLE IS NULL)) AND
      (:NEW.CASE_DESCRIPTION = :OLD.CASE_DESCRIPTION OR
       (:NEW.CASE_DESCRIPTION IS NULL AND :OLD.CASE_DESCRIPTION IS NULL)) AND
      (:NEW.LINKED_CASE_ID = :OLD.LINKED_CASE_ID OR
       (:NEW.LINKED_CASE_ID IS NULL AND :OLD.LINKED_CASE_ID IS NULL)) AND
      (:NEW.BAIL_MAG_CODE = :OLD.BAIL_MAG_CODE OR
       (:NEW.BAIL_MAG_CODE IS NULL AND :OLD.BAIL_MAG_CODE IS NULL)) AND
      (:NEW.REF_COURT_ID = :OLD.REF_COURT_ID OR
       (:NEW.REF_COURT_ID IS NULL AND :OLD.REF_COURT_ID IS NULL)) AND
      (:NEW.SEVERED_IND = :OLD.SEVERED_IND OR
       (:NEW.SEVERED_IND IS NULL AND :OLD.SEVERED_IND IS NULL)) AND
      (:NEW.INDICT_RESP = :OLD.INDICT_RESP OR
       (:NEW.INDICT_RESP IS NULL AND :OLD.INDICT_RESP IS NULL)) AND
      (:NEW.DATE_IND_REC = :OLD.DATE_IND_REC OR
       (:NEW.DATE_IND_REC IS NULL AND :OLD.DATE_IND_REC IS NULL)) AND
      (:NEW.PROS_AGENCY_REFERENCE = :OLD.PROS_AGENCY_REFERENCE OR
       (:NEW.PROS_AGENCY_REFERENCE IS NULL AND :OLD.PROS_AGENCY_REFERENCE IS NULL)) AND
      (:NEW.CASE_CLASS = :OLD.CASE_CLASS OR
       (:NEW.CASE_CLASS IS NULL AND :OLD.CASE_CLASS IS NULL)) AND
      (:NEW.JUDGE_REASON_FOR_APPEAL = :OLD.JUDGE_REASON_FOR_APPEAL OR
       (:NEW.JUDGE_REASON_FOR_APPEAL IS NULL AND :OLD.JUDGE_REASON_FOR_APPEAL IS NULL)) AND
      (:NEW.RESULTS_VERIFIED = :OLD.RESULTS_VERIFIED OR
       (:NEW.RESULTS_VERIFIED IS NULL AND :OLD.RESULTS_VERIFIED IS NULL)) AND
      (:NEW.LENGTH_TAPE = :OLD.LENGTH_TAPE OR
       (:NEW.LENGTH_TAPE IS NULL AND :OLD.LENGTH_TAPE IS NULL)) AND
      (:NEW.NO_PAGE_PROS_EVIDENCE = :OLD.NO_PAGE_PROS_EVIDENCE OR
       (:NEW.NO_PAGE_PROS_EVIDENCE IS NULL AND :OLD.NO_PAGE_PROS_EVIDENCE IS NULL)) AND
      (:NEW.NO_PROS_WITNESS = :OLD.NO_PROS_WITNESS OR
       (:NEW.NO_PROS_WITNESS IS NULL AND :OLD.NO_PROS_WITNESS IS NULL)) AND
      (:NEW.EST_PDH_TRIAL_LENGTH = :OLD.EST_PDH_TRIAL_LENGTH OR
       (:NEW.EST_PDH_TRIAL_LENGTH IS NULL AND :OLD.EST_PDH_TRIAL_LENGTH IS NULL)) AND
      (:NEW.INDICTMENT_INFO_1 = :OLD.INDICTMENT_INFO_1 OR
       (:NEW.INDICTMENT_INFO_1 IS NULL AND :OLD.INDICTMENT_INFO_1 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_2 = :OLD.INDICTMENT_INFO_2 OR
       (:NEW.INDICTMENT_INFO_2 IS NULL AND :OLD.INDICTMENT_INFO_2 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_3 = :OLD.INDICTMENT_INFO_3 OR
       (:NEW.INDICTMENT_INFO_3 IS NULL AND :OLD.INDICTMENT_INFO_3 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_4 = :OLD.INDICTMENT_INFO_4 OR
       (:NEW.INDICTMENT_INFO_4 IS NULL AND :OLD.INDICTMENT_INFO_4 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_5 = :OLD.INDICTMENT_INFO_5 OR
       (:NEW.INDICTMENT_INFO_5 IS NULL AND :OLD.INDICTMENT_INFO_5 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_6 = :OLD.INDICTMENT_INFO_6 OR
       (:NEW.INDICTMENT_INFO_6 IS NULL AND :OLD.INDICTMENT_INFO_6 IS NULL)) AND
      (:NEW.POLICE_OFFICER_ATTENDING = :OLD.POLICE_OFFICER_ATTENDING OR
       (:NEW.POLICE_OFFICER_ATTENDING IS NULL AND :OLD.POLICE_OFFICER_ATTENDING IS NULL)) AND
      (:NEW.CPS_CASE_WORKER = :OLD.CPS_CASE_WORKER OR
       (:NEW.CPS_CASE_WORKER IS NULL AND :OLD.CPS_CASE_WORKER IS NULL)) AND
      (:NEW.EXPORT_CHARGES = :OLD.EXPORT_CHARGES OR
       (:NEW.EXPORT_CHARGES IS NULL AND :OLD.EXPORT_CHARGES IS NULL)) AND
      (:NEW.MAGISTRATES_CASE_REF = :OLD.MAGISTRATES_CASE_REF OR
       (:NEW.MAGISTRATES_CASE_REF IS NULL AND :OLD.MAGISTRATES_CASE_REF IS NULL)) AND
      (:NEW.CLASS_CODE = :OLD.CLASS_CODE OR
       (:NEW.CLASS_CODE IS NULL AND :OLD.CLASS_CODE IS NULL)) AND
      (:NEW.OFFENCE_GROUP_UPDATE = :OLD.OFFENCE_GROUP_UPDATE OR
       (:NEW.OFFENCE_GROUP_UPDATE IS NULL AND :OLD.OFFENCE_GROUP_UPDATE IS NULL)) AND
      (:NEW.CCC_TRANS_TO_REF_COURT_ID = :OLD.CCC_TRANS_TO_REF_COURT_ID OR
       (:NEW.CCC_TRANS_TO_REF_COURT_ID IS NULL AND :OLD.CCC_TRANS_TO_REF_COURT_ID IS NULL)) AND
      (:NEW.RECEIPT_TYPE = :OLD.RECEIPT_TYPE OR
       (:NEW.RECEIPT_TYPE IS NULL AND :OLD.RECEIPT_TYPE IS NULL)) AND
      (:NEW.ccc_trans_from_ref_court_id = :OLD.ccc_trans_from_ref_court_id OR
       (:NEW.ccc_trans_from_ref_court_id IS NULL AND :OLD.ccc_trans_from_ref_court_id IS NULL)) AND
      (:NEW.date_trans_from = :OLD.date_trans_from OR
       (:NEW.date_trans_from IS NULL AND :OLD.date_trans_from IS NULL)) AND
      (:NEW.retrial = :OLD.retrial OR
       (:NEW.retrial IS NULL AND :OLD.retrial IS NULL)) AND
      (:NEW.original_case_number = :OLD.original_case_number OR
       (:NEW.original_case_number IS NULL AND :OLD.original_case_number IS NULL)) AND
      (:NEW.lc_sent_date = :OLD.lc_sent_date OR
       (:NEW.lc_sent_date IS NULL AND :OLD.lc_sent_date IS NULL)) AND
      (:NEW.no_cb_pros_witness = :OLD.no_cb_pros_witness OR
       (:NEW.no_cb_pros_witness IS NULL AND :OLD.no_cb_pros_witness IS NULL)) AND
      (:NEW.no_other_pros_witness = :OLD.no_other_pros_witness OR
       (:NEW.no_other_pros_witness IS NULL AND :OLD.no_other_pros_witness IS NULL)) AND
      (:NEW.Vulnerable_victim_indicator = :OLD.Vulnerable_victim_indicator OR
       (:NEW.Vulnerable_victim_indicator IS NULL AND :OLD.Vulnerable_victim_indicator IS NULL)) THEN 
       



      -- Only the CHARGE_IMPORT_INDICATOR has changed so do not increase VERSION
      -- This will even come into this section if the CHARGE_IMPORT_INDICATOR column is
      -- updated to the same value with all others staying the same

      SELECT SYSDATE
      INTO   :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    ELSE

      -- Other fields have changes so increase VERSION

      SELECT :OLD.VERSION + 1,
             SYSDATE
      INTO   :NEW.VERSION,
             :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    END IF;

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN

    INSERT INTO AUD_CASE
    VALUES (:OLD.CASE_ID,
            :OLD.CASE_NUMBER,
            :OLD.CASE_TYPE,
            :OLD.MAG_CONVICTION_DATE,
            :OLD.CASE_SUB_TYPE,
            :OLD.CASE_TITLE,
            :OLD.CASE_DESCRIPTION,
            :OLD.LINKED_CASE_ID,
            :OLD.BAIL_MAG_CODE,
            :OLD.REF_COURT_ID,
            :OLD.COURT_ID,
            :OLD.CHARGE_IMPORT_INDICATOR,
            :OLD.SEVERED_IND,
            :OLD.INDICT_RESP,
            :OLD.DATE_IND_REC,
            :OLD.PROS_AGENCY_REFERENCE,
            :OLD.LAST_UPDATE_DATE,
            :OLD.CREATION_DATE,
            :OLD.CREATED_BY,
            :OLD.LAST_UPDATED_BY,
            :OLD.VERSION,
            :OLD.CASE_CLASS,
            :OLD.JUDGE_REASON_FOR_APPEAL,
            :OLD.RESULTS_VERIFIED,
            :OLD.LENGTH_TAPE,
            :OLD.NO_PAGE_PROS_EVIDENCE,
            :OLD.NO_PROS_WITNESS,
            :OLD.EST_PDH_TRIAL_LENGTH,
            :OLD.indictment_info_1,
            :OLD.indictment_info_2,
            :OLD.indictment_info_3,
            :OLD.indictment_info_4,
            :OLD.indictment_info_5,
            :OLD.indictment_info_6,
            :OLD.POLICE_OFFICER_ATTENDING,
            :OLD.CPS_CASE_WORKER,
            :OLD.EXPORT_CHARGES,
            :OLD.IND_CHANGE_STATUS,
            :OLD.MAGISTRATES_CASE_REF,
            :OLD.CLASS_CODE,
            :OLD.OFFENCE_GROUP_UPDATE,
            :OLD.CCC_TRANS_TO_REF_COURT_ID,
            :OLD.RECEIPT_TYPE,
	    l_trig_event,
	    :OLD.ccc_trans_from_ref_court_id,
	    :OLD.date_trans_from,
	    :OLD.retrial,
	    :OLD.original_case_number,
	    :OLD.lc_sent_date,
	    :OLD.no_cb_pros_witness,
	    :OLD.no_other_pros_witness,
	    :OLD.Vulnerable_victim_indicator);

  END IF;

END;
/
show errors;
