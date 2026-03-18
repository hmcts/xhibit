/*
 * Patch for DB Release 5.11.1 (Production & Pre-Production)
 *
 * 23rd February 2004
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

CREATE TABLE XHB_EMAIL_VAR TABLESPACE XHIBITD AS SELECT * FROM XHB_EMAIL;

DROP TABLE XHB_EMAIL;

CREATE TABLE XHB_EMAIL (
       MAIL_ID           NUMBER(8)      NOT NULL,
       RECIPIENTS        CLOB           NULL,
       CCRECIPIENTS      CLOB           NULL,
       BCCRECIPIENTS     CLOB           NULL,
       SUBJECT           VARCHAR2(255)  NOT NULL,
       SENDER            VARCHAR2(255)  NOT NULL,
       MIME_BODY         BLOB           NOT NULL,
       STATUS            VARCHAR2(1)    NOT NULL,
       CREATION_TIME     DATE           NOT NULL,
       MUSTRECEIVE       VARCHAR2(2)    NOT NULL,
       REJECTTIME        DATE           NULL,
       REASON            VARCHAR2(50)   NULL,
       LAST_UPDATE_DATE  DATE           NOT NULL,
       CREATION_DATE     DATE           NOT NULL,
       CREATED_BY        VARCHAR2(30)   NOT NULL,
       LAST_UPDATED_BY   VARCHAR2(30)   NOT NULL,
       VERSION           NUMBER(5)      NOT NULL,
       COURT_ID          NUMBER(8)      NULL,
       EMAIL_TO          CLOB           NULL,
       COMPANY           VARCHAR2(255)  NULL,
       ATTACHMENT        VARCHAR2(1)    DEFAULT 'Y' NULL,
       MIME_TYPE         VARCHAR2(3)    DEFAULT 'PDF' NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);

ALTER TABLE XHB_EMAIL
       ADD (PRIMARY KEY (MAIL_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

ALTER TABLE XHB_EMAIL
      ADD (FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT);

CREATE INDEX xhb_email_court_fk ON XHB_EMAIL
  (COURT_ID ASC)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);

INSERT INTO XHB_EMAIL (SELECT * FROM XHB_EMAIL_VAR);

DROP TABLE XHB_EMAIL_VAR;


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *
 * Need to create temporary table in a process on the live system:
 *
 *     1. Create a temporary audit table as a copy of the current audit table
 *     2. Drop the original audit table
 *     3. Create new audit table as select * from XHB_ table with no rows
 *     4. Add the INSERT_EVENT column to the end of the audit table
 *     5. Insert the data from the temporary audit table into the new audit table
 */

CREATE TABLE AUD_EMAIL_VAR TABLESPACE AUDITD AS SELECT * FROM AUD_EMAIL;

DROP TABLE AUD_EMAIL;

CREATE TABLE AUD_EMAIL TABLESPACE AUDITD AS SELECT * FROM XHB_EMAIL WHERE 1 = 0;
ALTER TABLE AUD_EMAIL ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO AUD_EMAIL (SELECT * FROM AUD_EMAIL_VAR);

DROP TABLE AUD_EMAIL_VAR;


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table.  However, always a good idea to recompile the BIR trigger.
 */

CREATE OR REPLACE TRIGGER XHB_EMAIL_BIR_TR
  BEFORE INSERT
  ON XHB_EMAIL
  FOR EACH ROW

BEGIN

  IF :NEW.MAIL_ID IS NULL THEN

    SELECT XHB_EMAIL_SEQ.NEXTVAL
    INTO   :NEW.MAIL_ID
    FROM   DUAL;

  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/

CREATE OR REPLACE TRIGGER XHB_EMAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_EMAIL_BUR_TR */

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

    INSERT INTO AUD_EMAIL 
    VALUES (:old.MAIL_ID, 
            :old.RECIPIENTS, 
            :old.CCRECIPIENTS, 
            :old.BCCRECIPIENTS, 
            :old.SUBJECT, 
            :old.SENDER, 
            :old.MIME_BODY, 
            :old.STATUS, 
            :old.CREATION_TIME, 
            :old.MUSTRECEIVE, 
            :old.REJECTTIME, 
            :old.REASON, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.EMAIL_TO, 
            :old.COMPANY, 
            :old.ATTACHMENT,
            :old.MIME_TYPE,
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of standing data
 */

-- Updates of Data timed event maps for Mercator.

UPDATE MTBL_TIME_TRIGGER_STATUS
SET    mapname = 'CJIP_Doc_DeReg_Trigger.mmc'
WHERE  mapname = 'CJIP_Doc_DoReg_Trigger.mmc';

UPDATE MTBL_TIME_TRIGGER_STATUS
SET    mapname = 'CSU_CJIT_Trigger.mmc'
WHERE  mapname = 'CSI_CJIT_Trigger.mmc';

COMMIT;


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '5.11.1', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
