/*
 * Filename:    DB_Patch_7_3.sql
 *
 * System:      Pre-Production and Production
 *
 * Date:        9th March 2005
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 * 09/03/05	SS			Database change to rename column HTML_CLOB_ID to HTML_BLOB_ID in the XHB_INTERNET_HTML table. 
 *
 */ 

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */
ALTER TABLE XHB_INTERNET_HTML
	  DROP CONSTRAINT XHB_INT_HTML_HTML_CLOB_ID_FK;

ALTER TABLE XHB_INTERNET_HTML 
	  RENAME COLUMN HTML_CLOB_ID TO HTML_BLOB_ID;	   
	  
ALTER TABLE XHB_INTERNET_HTML 
	  ADD CONSTRAINT XHB_INT_HTML_HTML_BLOB_ID_FK FOREIGN KEY (HTML_BLOB_ID) REFERENCES XHB_BLOB(BLOB_ID);

/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

ALTER TABLE AUD_INTERNET_HTML
	  RENAME COLUMN HTML_CLOB_ID TO HTML_BLOB_ID;

/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */
CREATE OR REPLACE TRIGGER XHB_INTERNET_HTML_BUR_TR
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

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORM
ATION */

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

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_IN
FORMATION */

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
                                   HTML_BLOB_ID,
                                   INSERT_EVENT)
                           VALUES (:OLD.INTERNET_HTML_ID,
                                   :OLD.STATUS,
                                   :OLD.LAST_UPDATE_DATE,
                                   :OLD.CREATION_DATE,
                                   :OLD.CREATED_BY,
                                   :OLD.LAST_UPDATED_BY,
                                   :OLD.VERSION,
                                   :OLD.COURT_ID,
                                   :OLD.HTML_BLOB_ID,
                                   l_trig_event);

  END IF;

END XHB_INTERNET_HTML_BUR_TR;
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

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.3', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.3', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.3', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.3', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
