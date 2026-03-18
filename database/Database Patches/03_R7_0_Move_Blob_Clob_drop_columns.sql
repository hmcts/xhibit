-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                          Drop the old columns                             --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

ALTER TABLE XHB_FORMATTING       DROP (XML_DOCUMENT);
ALTER TABLE XHB_FORMATTING       DROP (FORMATTED_DOCUMENT);
ALTER TABLE XHB_XML_DOCUMENT     DROP (XML_DOCUMENT);
ALTER TABLE XHB_DOCUMENT_CONTROL DROP (FORMATTED_DOCUMENT);
ALTER TABLE XHB_EMAIL            DROP (MIME_BODY);
ALTER TABLE XHB_INTERNET_HTML    DROP (HTML);


-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                         Recompile affected packages                       --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

ALTER PACKAGE xhb_list_distribution_pkg COMPILE;





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                   Update the "before insert" triggers                     --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

-- The bodies of these would not have changed...
ALTER TRIGGER XHB_DOCUMENT_CONTROL_BIR_TR COMPILE;
ALTER TRIGGER XHB_EMAIL_BIR_TR COMPILE;
ALTER TRIGGER XHB_FORMATTING_BIR_TR COMPILE;
ALTER TRIGGER XHB_INTERNET_HTML_BIR_TR COMPILE;
ALTER TRIGGER XHB_XML_DOCUMENT_BIR_TR COMPILE;
-- and re-compile the after update row trigger...
ALTER TRIGGER XHB_FORMATTING_AUR_TR COMPILE;





-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
--                  Update the "before update" triggers                      --
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_FORMATTING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_FORMATTING
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMATTING') = 1) THEN

    INSERT INTO AUD_FORMATTING (FORMATTING_ID,
                                DATE_IN,
                                FORMAT_STATUS,
                                DISTRIBUTION_TYPE,
                                MIME_TYPE,
                                DOCUMENT_TYPE,
                                PROCESS_TYPE,
                                LAST_UPDATE_DATE,
                                CREATION_DATE,
                                CREATED_BY,
                                LAST_UPDATED_BY,
                                VERSION,
                                COURT_ID,
                                FORMATTED_DOCUMENT_BLOB_ID,
                                XML_DOCUMENT_CLOB_ID,
                                INSERT_EVENT)
                        VALUES (:OLD.FORMATTING_ID,
                                :OLD.DATE_IN,
                                :OLD.FORMAT_STATUS,
                                :OLD.DISTRIBUTION_TYPE,
                                :OLD.MIME_TYPE,
                                :OLD.DOCUMENT_TYPE,
                                :OLD.PROCESS_TYPE,
                                :OLD.LAST_UPDATE_DATE,
                                :OLD.CREATION_DATE,
                                :OLD.CREATED_BY,
                                :OLD.LAST_UPDATED_BY,
                                :OLD.VERSION,
                                :OLD.COURT_ID,
                                :OLD.FORMATTED_DOCUMENT_BLOB_ID,
                                :OLD.XML_DOCUMENT_CLOB_ID,
                                l_trig_event);

  END IF;

END XHB_FORMATTING_BUR_TR;
/
show err


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_XML_DOCUMENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_XML_DOCUMENT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_XML_DOCUMENT') = 1) THEN

    INSERT INTO AUD_XML_DOCUMENT (XML_DOCUMENT_ID,
                                  DATE_CREATED,
                                  DOCUMENT_TITLE,
                                  STATUS,
                                  EXPIRY_DATE,
                                  DOCUMENT_TYPE,
                                  LAST_UPDATE_DATE,
                                  CREATION_DATE,
                                  CREATED_BY,
                                  LAST_UPDATED_BY,
                                  VERSION,
                                  COURT_ID,
                                  XML_DOCUMENT_CLOB_ID,
                                  INSERT_EVENT)
                          VALUES (:OLD.XML_DOCUMENT_ID,
                                  :OLD.DATE_CREATED,
                                  :OLD.DOCUMENT_TITLE,
                                  :OLD.STATUS,
                                  :OLD.EXPIRY_DATE,
                                  :OLD.DOCUMENT_TYPE,
                                  :OLD.LAST_UPDATE_DATE,
                                  :OLD.CREATION_DATE,
                                  :OLD.CREATED_BY,
                                  :OLD.LAST_UPDATED_BY,
                                  :OLD.VERSION,
                                  :OLD.COURT_ID,
                                  :OLD.XML_DOCUMENT_CLOB_ID,
                                  l_trig_event);

  END IF;

END XHB_XML_DOCUMENT_BUR_TR;
/
show err


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_DOCUMENT_CONTROL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_CONTROL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_CONTROL') = 1) THEN

    INSERT INTO AUD_DOCUMENT_CONTROL (DOC_CONTROL_ID,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DISTRIBUTION_TYPE,
                                      MIME_TYPE,
                                      DOCUMENT_TYPE,
                                      LAST_UPDATE_DATE,
                                      CREATION_DATE,
                                      CREATED_BY,
                                      LAST_UPDATED_BY,
                                      VERSION,
                                      FORMATTING_ID,
                                      COURT_ID,
                                      DISTRIBUTED_DATE,
                                      XML_DOCUMENT_ID,
                                      FORMATTED_DOCUMENT_BLOB_ID,
                                      INSERT_EVENT)
                              VALUES (:OLD.DOC_CONTROL_ID,
                                      :OLD.STATUS,
                                      :OLD.EXPIRY_DATE,
                                      :OLD.DISTRIBUTION_TYPE,
                                      :OLD.MIME_TYPE,
                                      :OLD.DOCUMENT_TYPE,
                                      :OLD.LAST_UPDATE_DATE,
                                      :OLD.CREATION_DATE,
                                      :OLD.CREATED_BY,
                                      :OLD.LAST_UPDATED_BY,
                                      :OLD.VERSION,
                                      :OLD.FORMATTING_ID,
                                      :OLD.COURT_ID,
                                      :OLD.DISTRIBUTED_DATE,
                                      :OLD.XML_DOCUMENT_ID,
                                      :OLD.FORMATTED_DOCUMENT_BLOB_ID,
                                      l_trig_event);

  END IF;

END XHB_DOCUMENT_CONTROL_BUR_TR;
/
show err


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL') = 1) THEN

    INSERT INTO AUD_EMAIL (MAIL_ID,
                           RECIPIENTS,
                           CCRECIPIENTS,
                           BCCRECIPIENTS,
                           SUBJECT,
                           SENDER,
                           STATUS,
                           CREATION_TIME,
                           MUSTRECEIVE,
                           REJECTTIME,
                           REASON,
                           LAST_UPDATE_DATE,
                           CREATION_DATE,
                           CREATED_BY,
                           LAST_UPDATED_BY,
                           VERSION,
                           COURT_ID,
                           EMAIL_TO,
                           COMPANY,
                           ATTACHMENT,
                           MIME_TYPE,
                           MIME_BODY_BLOB_ID,
                           INSERT_EVENT)
                   VALUES (:OLD.MAIL_ID,
                           :OLD.RECIPIENTS,
                           :OLD.CCRECIPIENTS,
                           :OLD.BCCRECIPIENTS,
                           :OLD.SUBJECT,
                           :OLD.SENDER,
                           :OLD.STATUS,
                           :OLD.CREATION_TIME,
                           :OLD.MUSTRECEIVE,
                           :OLD.REJECTTIME,
                           :OLD.REASON,
                           :OLD.LAST_UPDATE_DATE,
                           :OLD.CREATION_DATE,
                           :OLD.CREATED_BY,
                           :OLD.LAST_UPDATED_BY,
                           :OLD.VERSION,
                           :OLD.COURT_ID,
                           :OLD.EMAIL_TO,
                           :OLD.COMPANY,
                           :OLD.ATTACHMENT,
                           :OLD.MIME_TYPE,
                           :OLD.MIME_BODY_BLOB_ID,
                           l_trig_event);

  END IF;

END XHB_EMAIL_BUR_TR;
/
show err


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_INTERNET_HTML_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_INTERNET_HTML
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_INTERNET_HTML') = 1) THEN

    INSERT INTO AUD_INTERNET_HTML (INTERNET_HTML_ID,
                                   STATUS,
                                   LAST_UPDATE_DATE,
                                   CREATION_DATE,
                                   CREATED_BY,
                                   LAST_UPDATED_BY,
                                   VERSION,
                                   COURT_ID,
                                   HTML_CLOB_ID,
                                   INSERT_EVENT)
                           VALUES (:OLD.INTERNET_HTML_ID,
                                   :OLD.STATUS,
                                   :OLD.LAST_UPDATE_DATE,
                                   :OLD.CREATION_DATE,
                                   :OLD.CREATED_BY,
                                   :OLD.LAST_UPDATED_BY,
                                   :OLD.VERSION,
                                   :OLD.COURT_ID,
                                   :OLD.HTML_CLOB_ID,
                                   l_trig_event);

  END IF;

END XHB_INTERNET_HTML_BUR_TR;
/
show err
