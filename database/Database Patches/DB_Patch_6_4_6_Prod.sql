/*
 * Filename:    DB_Patch_6_4_6_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 *
 * Date:        18th Novemeber 2004
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

ALTER TABLE XHB_CREST_IMPORT ADD (MAX_RETRY NUMBER(3));
ALTER TABLE XHB_CREST_IMPORT ADD (CURRENT_RETRY NUMBER(3));


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

ALTER TABLE XHIBIT.AUD_CREST_IMPORT ADD (MAX_RETRY NUMBER(3));
ALTER TABLE XHIBIT.AUD_CREST_IMPORT ADD (CURRENT_RETRY NUMBER(3));

/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

ALTER TRIGGER XHB_CREST_IMPORT_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CREST_IMPORT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CREST_IMPORT') = 1) THEN

    INSERT INTO AUD_CREST_IMPORT (CREST_IMPORT_ID,
                                  STATUS,
                                  COURT_ID,
                                  IMPORT_TYPE,
                                  LAST_UPDATE_DATE,
                                  CREATION_DATE,
                                  CREATED_BY,
                                  LAST_UPDATED_BY,
                                  VERSION,
                                  TIME_TO_RUN,
                                  MAX_RETRY,
                                  CURRENT_RETRY,
                                  INSERT_EVENT)
    VALUES                       (:OLD.CREST_IMPORT_ID,
                                  :OLD.STATUS,
                                  :OLD.COURT_ID,
                                  :OLD.IMPORT_TYPE,
                                  :OLD.LAST_UPDATE_DATE,
                                  :OLD.CREATION_DATE,
                                  :OLD.CREATED_BY,
                                  :OLD.LAST_UPDATED_BY,
                                  :OLD.VERSION,
                                  :OLD.TIME_TO_RUN,
                                  :OLD.MAX_RETRY,
                                  :OLD.CURRENT_RETRY,
                                  l_trig_event);

  END IF;

END;
/
show errors


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_ref_local_pkg AS
    PROCEDURE end_hearings(p_court_id_in         IN XHB_HEARING.court_id%TYPE,
                           p_hearing_end_date_in IN VARCHAR2);
END xhb_ref_local_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_ref_local_pkg AS
    PROCEDURE end_hearings(p_court_id_in         IN XHB_HEARING.court_id%TYPE,
                           p_hearing_end_date_in IN VARCHAR2)
    IS
        l_hearing_end_date       DATE := to_date(p_hearing_end_date_in, 'YYYY-MM-DD HH24:MI:SS');
        l_trunc_hearing_end_date DATE := TRUNC(l_hearing_end_date);
    BEGIN
        UPDATE XHB_HEARING xh
        SET    xh.hearing_end_date = l_hearing_end_date
        WHERE  xh.court_id = p_court_id_in
        AND    EXISTS (SELECT 1
                       FROM   XHB_SCHEDULED_HEARING xsh 
                       WHERE  xsh.start_time IS NULL
                       AND    TRUNC(xsh.not_before_time) = l_trunc_hearing_end_date
                       AND    xh.hearing_id = xsh.hearing_id);

        -- Sets the START DATE FOR the scheduled hearing so that this will NOT be 
        -- processed again NEXT TIME the script IS RUN
        UPDATE XHB_SCHEDULED_HEARING xsh
        SET    START_TIME = l_hearing_end_date
        WHERE  START_TIME IS NULL
        AND    TRUNC(not_before_time) = l_trunc_hearing_end_date
        AND    EXISTS (SELECT 1
                       FROM   XHB_HEARING xh
                       WHERE  xh.hearing_id = xsh.hearing_id
                       AND    xh.court_id   = p_court_id_in);
    END end_hearings;
END xhb_ref_local_pkg;
/
show errors

/*
 * Changes, additions or deletion of standing data
 */

UPDATE XHB_CREST_IMPORT SET MAX_RETRY = 20, CURRENT_RETRY = 0;

/*
 * Updating of table XHB_VERSION
 */
DELETE FROM XHB_VERSION WHERE DISPLAY_NAME IN ('Mercator', 'Database');

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '6.4.6', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '6.4.6', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
