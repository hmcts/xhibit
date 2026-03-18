/*
 * Filename:    DB_Patch_6.1_Interim.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        15th June 2004
 *
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

ALTER TABLE XHB_COURT_SITE ADD (SHORT_NAME VARCHAR2(6));

ALTER TABLE XHB_COURT_SITE
      ADD (CONSTRAINT COURT_SITE_CTID_SHORTNAME_UC UNIQUE (COURT_ID, SHORT_NAME));


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

DROP TABLE AUD_COURT_SITE;

CREATE TABLE AUD_COURT_SITE TABLESPACE AUDITD AS SELECT * FROM XHB_COURT_SITE WHERE 1 = 0;
ALTER TABLE AUD_COURT_SITE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);


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

ALTER TRIGGER XHB_COURTSITE_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_COURTSITE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_COURT_SITE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_COURTSITE_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_COURT_SITE') = 1) THEN

    INSERT INTO AUD_COURT_SITE 
    VALUES (:old.COURT_SITE_ID,
            :old.COURT_SITE_NAME,
            :old.COURT_SITE_CODE,
            :old.COURT_ID,
            :old.ADDRESS_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.obs_ind,
            :old.DISPLAY_NAME,
            :old.CREST_COURT_ID,
            :old.SHORT_NAME,
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


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '6.1 Int', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
