CREATE OR REPLACE TRIGGER XHB_COURT_BUR_TR
  BEFORE UPDATE OR DELETE  ON XHB_COURT
  REFERENCING NEW AS NEW OLD AS OLD
  FOR EACH ROW

/* default body for XHB_COURT_BUR_TR */
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT') = 1) THEN

    INSERT INTO AUD_COURT
           (COURT_ID,
            COURT_TYPE,
            CIRCUIT,
            COURT_NAME,
            CREST_COURT_ID,
            COURT_PREFIX,
            SHORT_NAME,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            ADDRESS_ID,
            CREST_IP_ADDRESS,
            IN_SERVICE_FLAG,
            OBS_IND,
            PROBATION_OFFICE_NAME,
            INTERNET_COURT_NAME,
            DISPLAY_NAME,
            COURT_CODE,
            COUNTRY,
            LANGUAGE,
	          POLICE_FORCE_CODE,
            INSERT_EVENT,
            fl_rep_sort,
            court_start_time,
            wl_rep_sort,
            wl_rep_period,
            wl_rep_time,
            wl_free_text)
    VALUES (:old.COURT_ID,
            :old.COURT_TYPE,
            :old.CIRCUIT,
            :old.COURT_NAME,
            :old.CREST_COURT_ID,
            :old.COURT_PREFIX,
            :old.SHORT_NAME,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.ADDRESS_ID,
            :old.CREST_IP_ADDRESS,
            :old.IN_SERVICE_FLAG,
            :old.OBS_IND,
            :old.PROBATION_OFFICE_NAME,
            :old.INTERNET_COURT_NAME,
            :old.DISPLAY_NAME,
            :old.COURT_CODE,
            :old.country,
            :old.LANGUAGE,
            :old.POLICE_FORCE_CODE,
            l_trig_event,
            :old.fl_rep_sort,
            :old.court_start_time,
            :old.wl_rep_sort,
            :old.wl_rep_period,
            :old.wl_rep_time,
            :old.wl_free_text);

  END IF;

END XHB_COURT_BUR_TR;
/