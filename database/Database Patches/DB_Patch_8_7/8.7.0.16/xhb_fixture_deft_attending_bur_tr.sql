CREATE OR REPLACE TRIGGER "XHIBIT".XHB_FIXTURE_DEFT_ATTEND_BUR_TR
  BEFORE UPDATE OR DELETE ON XHB_FIXTURE_DEFT_ATTENDING
  REFERENCING NEW AS NEW OLD AS OLD
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FIXTURE_DEFT_ATTENDING') = 1) THEN

    INSERT INTO AUD_FIXTURE_DEFT_ATTENDING ( FIXTURE_DEFT_ATTENDING_ID
                                            , DEFENDANT_ON_CASE_ID
										    , CASE_DIARY_FIXTURE_ID
											, ATTENDING
											, OBS_IND
											, LAST_UPDATE_DATE
											, CREATION_DATE
											, LAST_UPDATED_BY
											, CREATED_BY
											, VERSION 
											, INSERT_EVENT
											)
                          VALUES (:OLD.FIXTURE_DEFT_ATTENDING_ID
                                 ,:OLD.DEFENDANT_ON_CASE_ID
                                 ,:OLD.CASE_DIARY_FIXTURE_ID
                                 ,:OLD.ATTENDING
                                 ,:OLD.OBS_IND
                                 ,:OLD.LAST_UPDATE_DATE
								 ,:OLD.CREATION_DATE
								 ,:OLD.LAST_UPDATED_BY
								 ,:OLD.CREATED_BY
                                 ,:OLD.VERSION
                                 ,l_trig_event);

  END IF;

END XHB_FIXTURE_DEFT_ATTEND_BUR_TR;
/