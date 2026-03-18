CREATE OR REPLACE TRIGGER xhb_cpp_list_bur_tr
  BEFORE UPDATE OR DELETE
  ON xhb_cpp_list
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CPP_LIST') = 1) THEN

    INSERT INTO AUD_CPP_LIST
    VALUES (:old.CPP_LIST_ID,
	:old.COURT_CODE,
	:old.LIST_TYPE,
	:old.TIME_LOADED,
	:old.LIST_START_DATE,
	:old.LIST_END_DATE,
	:old.LIST_CLOB_ID,
	:old.MERGED_CLOB_ID,
	:old.STATUS,
	:old.ERROR_MESSAGE,
	:old.OBS_IND,
	:old.LAST_UPDATED_BY,
	:old.CREATION_DATE,
	:old.LAST_UPDATE_DATE,
	:old.CREATED_BY,
	:old.VERSION,
            l_trig_event);

  END IF;


END;
/