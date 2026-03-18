/*
 * Filename:    DB_Patch_6_4_2.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * Date:        25th October 2004
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

CREATE TABLE MTBL_HTTP_RETRY (MAPNAME VARCHAR2(64) NOT NULL,
                              ATTEMPTS NUMBER NOT NULL,
                              FREQUENCY NUMBER NOT NULL)
TABLESPACE MERCATORD
STORAGE (INITIAL 256K
         NEXT 256K
         PCTINCREASE 0);

ALTER TABLE MTBL_HTTP_RETRY
       ADD (CONSTRAINT MTBL_HTTP_RETRY_PK PRIMARY KEY (MAPNAME)
       USING INDEX TABLESPACE MERCATORX
       STORAGE (INITIAL 256K
                NEXT 256K
                PCTINCREASE 0));

ALTER TABLE XHB_CREST_IMPORT ADD (TIME_TO_RUN DATE);

/*
 * Changes, additions or deletion of views
 */

/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 *     1. Rename existing audit table to _old
 *     2. Create new audit table as SELECT * FROM XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *     4. Insert the data from the old audit table into the new audit table
 *     5. If successful, drop old audit table
 */

ALTER TABLE AUD_CREST_IMPORT ADD (TIME_TO_RUN DATE);


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

ALTER TRIGGER XHB_CREST_IMPORT_BIR_TR COMPILE;
ALTER TRIGGER XHB_REF_DATA_IMP_EXP COMPILE;

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
                                  l_trig_event);

  END IF;

END;
/
show errors

/*
 * Changes, additions or deletion of packages/procedures/functions
 */

/*
 * Changes, additions or deletion of standing data
 */

INSERT INTO MTBL_HTTP_RETRY ( MAPNAME, ATTEMPTS, FREQUENCY )
VALUES ( 'Resynch_Refresh_Caller.mmc', 5, 2); 

INSERT INTO MTBL_HTTP_RETRY ( MAPNAME, ATTEMPTS, FREQUENCY )
VALUES ( 'Pre_Hearing_Initial_Upload_Split.mmc', 5, 2); 

INSERT INTO MTBL_HTTP_RETRY ( MAPNAME, ATTEMPTS, FREQUENCY )
VALUES ( 'Exp_Res_CRN.mmc', 5, 2); 

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '6.4.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '6.4.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '6.4.2', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '6.4.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
