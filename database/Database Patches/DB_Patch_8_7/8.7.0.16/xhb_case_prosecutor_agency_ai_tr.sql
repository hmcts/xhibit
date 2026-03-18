create or replace TRIGGER XHB_CASEPROSECUTORAGENC_AI_TR
  AFTER INSERT
  ON XHIBIT.XHB_CASE_PROSECUTOR_AGENCY
  FOR EACH ROW

BEGIN

   /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE_PROSECUTOR_AGENCY') = 1) THEN

    INSERT INTO AUD_CASE_PROSECUTOR_AGENCY (
            CASE_PROS_AGENCY_ID,
            PROSECUTOR_TYPE,
            CASE_ID,
            REF_PROSECUTOR_AGENCY_ID,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
			RESPONDENT_STATUS,
            INSERT_EVENT,
			OBS_IND)
    VALUES (:new.CASE_PROS_AGENCY_ID,
            :new.PROSECUTOR_TYPE,
            :new.CASE_ID,
            :new.REF_PROSECUTOR_AGENCY_ID,
            :new.LAST_UPDATE_DATE,
            :new.CREATION_DATE,
            :new.CREATED_BY,
            :new.LAST_UPDATED_BY,
            :new.VERSION,
			:new.RESPONDENT_STATUS,
            'I',
			:new.OBS_IND);
 END IF;
END;
/