/*
 * Filename:    Xhibit_DB_Patch_8_2_4.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  30/11/2008	M HEWITT		Changes for release 8.2.4
 
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_2_4_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

ALTER TABLE XHB_LEO_ADV_LINK ADD
(
  CREST_POST_NUMBER  NUMBER(8) null
);



/*
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */





/*
 * Changes, additions or deletion of views
 */




/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

ALTER TABLE AUD_LEO_ADV_LINK ADD
(
  CREST_POST_NUMBER  NUMBER(8) null
);



/*
 * Changes, additions or deletion of sequences
 */




/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@xhb_search_pkg_h.sql
@xhb_search_pkg_b.sql



/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */




CREATE OR REPLACE TRIGGER XHB_LEO_ADV_LINK_BIR_TR
  BEFORE INSERT
  ON XHB_LEO_ADV_LINK
  FOR EACH ROW

BEGIN

  IF :NEW.LEO_ADV_LINK_ID IS NULL THEN

    SELECT XHB_LEO_ADV_LINK_SEQ.NEXTVAL
    INTO   :NEW.LEO_ADV_LINK_ID
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


CREATE OR REPLACE TRIGGER XHB_LEO_ADV_LINK_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEO_ADV_LINK
  FOR EACH ROW

/* default body for XHB_LEO_ADV_LINK_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEO_ADV_LINK') = 1) THEN

    INSERT INTO AUD_LEO_ADV_LINK (
           LEO_ADV_LINK_ID,
           LEGAL_AID_ORDER_ID,
           DEFENDANT_ON_CASE_ID,
           REF_ADVOCATE_ID,
           CREST_ADV_CATEGORY,
           AVAILABLE,
	   NEW_ROW_FLAG,
           LAST_UPDATE_DATE,
           CREATION_DATE,
           CREATED_BY,
           LAST_UPDATED_BY,
           VERSION,
           OBS_IND,
           INSERT_EVENT,
           CREST_POST_NUMBER)
    VALUES (
           :old.LEO_ADV_LINK_ID,
           :old.LEGAL_AID_ORDER_ID,
           :old.DEFENDANT_ON_CASE_ID,
           :old.REF_ADVOCATE_ID,
           :old.CREST_ADV_CATEGORY,
           :old.AVAILABLE,
	   :old.NEW_ROW_FLAG,
           :old.LAST_UPDATE_DATE,
           :old.CREATION_DATE,
           :old.CREATED_BY,
           :old.LAST_UPDATED_BY,
           :old.VERSION,
           :old.OBS_IND,
           l_trig_event,
           :old.CREST_POST_NUMBER
           );

  END IF;

END;
/









/*
 * Changes, additions or deletion of standing data
 */




/*
 * Updating of table XHB_SYS_AUDIT
 */





/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.2.4', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off


