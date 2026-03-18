/*
 * Filename:    Xhibit_DB_Patch_8_2_3.sql
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
 *  15/10/2008	M HEWITT		Changes for release 8.2.3
 
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_2_3_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename



/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

ALTER TABLE XHB_SH_LEG_REP ADD
(
  SUB_INST                     VARCHAR2(1) null,
  SUBSTITUTED_REF_LEGAL_REP_ID  NUMBER(8) null
);


ALTER TABLE XHB_SH_LEG_REP
  ADD CONSTRAINT SH_LEG_REP_SUB_REF_LEG_REP_FK FOREIGN KEY (SUBSTITUTED_REF_LEGAL_REP_ID)
  REFERENCES XHB_REF_LEGAL_REPRESENTATIVE (REF_LEGAL_REP_ID);



/*
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


CREATE TABLE XHB_LEGAL_AID_ORDER
(
  LEGAL_AID_ORDER_ID           NUMBER(8) not null,
  CREST_LEO_ID                 NUMBER(8) not null,
  DEFENDANT_ON_CASE_ID         NUMBER(8) not null,
  LAST_UPDATE_DATE             DATE not null,
  CREATION_DATE                DATE not null,
  CREATED_BY                   VARCHAR2(30) not null,
  LAST_UPDATED_BY              VARCHAR2(30) not null,
  VERSION                      NUMBER(5) not null,
  OBS_IND                      VARCHAR2(1)
)
TABLESPACE XHIBITD
  STORAGE
  (
    INITIAL 1M
    NEXT 1M
    PCTINCREASE 0
  );


ALTER TABLE XHB_LEGAL_AID_ORDER
       ADD (CONSTRAINT LEGAL_AID_ORDER_PK PRIMARY KEY (LEGAL_AID_ORDER_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

ALTER TABLE XHB_LEGAL_AID_ORDER
  ADD CONSTRAINT LEGAL_AID_ORDER_DEF_ON_CASE_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID)
  REFERENCES XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID);

create index XHB_LEGAL_AID_ORDER_DOCID on XHB_LEGAL_AID_ORDER (DEFENDANT_ON_CASE_ID)
  tablespace XHIBITX
  storage
  (
    initial 1M
    next 5M
    pctincrease 0
  );

CREATE TABLE AUD_LEGAL_AID_ORDER TABLESPACE AUDITD AS SELECT * FROM XHB_LEGAL_AID_ORDER;
ALTER TABLE AUD_LEGAL_AID_ORDER ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

---

CREATE TABLE XHB_LEO_ADV_LINK
(
  LEO_ADV_LINK_ID              NUMBER(8) not null,
  LEGAL_AID_ORDER_ID           NUMBER(8) not null,
  DEFENDANT_ON_CASE_ID         NUMBER(8) not null,
  REF_ADVOCATE_ID              NUMBER(8) not null,
  CREST_ADV_CATEGORY           VARCHAR2(1) null,
  AVAILABLE                    VARCHAR2(1) null,
  NEW_ROW_FLAG                 VARCHAR2(1) null,
  LAST_UPDATE_DATE             DATE not null,
  CREATION_DATE                DATE not null,
  CREATED_BY                   VARCHAR2(30) not null,
  LAST_UPDATED_BY              VARCHAR2(30) not null,
  VERSION                      NUMBER(5) not null,
  OBS_IND                      VARCHAR2(1)
)
TABLESPACE XHIBITD
  STORAGE
  (
    INITIAL 1M
    NEXT 1M
    PCTINCREASE 0
  );


ALTER TABLE XHB_LEO_ADV_LINK
       ADD (CONSTRAINT LEO_ADV_LINK_PK PRIMARY KEY (LEO_ADV_LINK_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

ALTER TABLE XHB_LEO_ADV_LINK
  ADD CONSTRAINT LEO_ADV_LINK_LEG_AID_ORDER_FK FOREIGN KEY (LEGAL_AID_ORDER_ID)
  REFERENCES XHB_LEGAL_AID_ORDER (LEGAL_AID_ORDER_ID);

ALTER TABLE XHB_LEO_ADV_LINK
  ADD CONSTRAINT LEO_ADV_LINK_DEF_ON_CASE_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID)
  REFERENCES XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID);

ALTER TABLE XHB_LEO_ADV_LINK
  ADD CONSTRAINT LEO_ADV_LINK_REF_ADVOCATE_FK FOREIGN KEY (REF_ADVOCATE_ID)
  REFERENCES XHB_REF_ADVOCATE (REF_ADVOCATE_ID);

create index XHB_LEO_ADV_LINK_NR on XHB_LEO_ADV_LINK (NEW_ROW_FLAG)
  tablespace XHIBITX
  storage
  (
    initial 1M
    next 5M
    pctincrease 0
  );

create index XHB_LEO_ADV_LINK_DOCID on XHB_LEO_ADV_LINK (DEFENDANT_ON_CASE_ID)
  tablespace XHIBITX
  storage
  (
    initial 1M
    next 5M
    pctincrease 0
  );

CREATE TABLE AUD_LEO_ADV_LINK TABLESPACE AUDITD AS SELECT * FROM XHB_LEO_ADV_LINK;
ALTER TABLE AUD_LEO_ADV_LINK ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);




/*
 * Changes, additions or deletion of views
 */




/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


ALTER TABLE AUD_SH_LEG_REP ADD
(
  SUB_INST                     VARCHAR2(1) null,
  SUBSTITUTED_REF_LEGAL_REP_ID  NUMBER(8) null
);



/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE XHB_LEGAL_AID_ORDER_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_LEO_ADV_LINK_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@xhb_search_pkg_h.sql
@xhb_search_pkg_b.sql

/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */


CREATE OR REPLACE TRIGGER XHB_LEGAL_AID_ORDER_BIR_TR
  BEFORE INSERT
  ON XHB_LEGAL_AID_ORDER
  FOR EACH ROW

BEGIN

  IF :NEW.LEGAL_AID_ORDER_ID IS NULL THEN

    SELECT XHB_LEGAL_AID_ORDER_SEQ.NEXTVAL
    INTO   :NEW.LEGAL_AID_ORDER_ID
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


CREATE OR REPLACE TRIGGER XHB_LEGAL_AID_ORDER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_LEGAL_AID_ORDER
  FOR EACH ROW

/* default body for XHB_LEGAL_AID_ORDER_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_LEGAL_AID_ORDER') = 1) THEN

    INSERT INTO AUD_LEGAL_AID_ORDER (
      LEGAL_AID_ORDER_ID,
      CREST_LEO_ID,
      DEFENDANT_ON_CASE_ID,
      LAST_UPDATE_DATE,
      CREATION_DATE,
      CREATED_BY,
      LAST_UPDATED_BY,
      VERSION,
      OBS_IND,
      INSERT_EVENT
      )
    VALUES (
      :old.LEGAL_AID_ORDER_ID,
      :old.CREST_LEO_ID,
      :old.DEFENDANT_ON_CASE_ID,
      :old.LAST_UPDATE_DATE,
      :old.CREATION_DATE,
      :old.CREATED_BY,
      :old.LAST_UPDATED_BY,
      :old.VERSION,
      :old.OBS_IND,
      l_trig_event);

  END IF;

END;
/


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
           INSERT_EVENT)
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
           l_trig_event
           );

  END IF;

END;
/


CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BIR_TR
  BEFORE INSERT
  ON XHB_SH_LEG_REP
  FOR EACH ROW

BEGIN

  IF :NEW.SH_LEG_REP_ID IS NULL THEN

    SELECT XHB_SH_LEG_REP_SEQ.NEXTVAL
    INTO   :NEW.SH_LEG_REP_ID
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


CREATE OR REPLACE TRIGGER XHB_SH_LEG_REP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_SH_LEG_REP
  FOR EACH ROW

/* default body for XHB_SH_LEG_REP_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_SH_LEG_REP') = 1) THEN

    INSERT INTO AUD_SH_LEG_REP
    VALUES (:old.SH_LEG_REP_ID,
            :old.CREST_SEQUENCE_NO,
            :old.LEGAL_ROLE,
            :old.IS_SIGNED_IN,
            :old.SOL_FIRM_OR_REF_LEGAL_REP,
            :old.SCHED_HEAR_DEF_ID,
            :old.REF_LEGAL_REP_ID,
            :old.VERSION,
            :old.LAST_UPDATED_BY,
            :old.CREATED_BY,
            :old.CREATION_DATE,
            :old.LAST_UPDATE_DATE,
            :old.CC_INFO_ID,
            :old.REF_SOLICITOR_FIRM_ID,
            :old.REF_DEFENCE_CATEGORY_ID,
            :old.SCHEDULED_HEARING_ID,
            l_trig_event,
            :old.SUB_INST,
            :old.SUBSTITUTED_REF_LEGAL_REP_ID
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

INSERT INTO XHB_SYS_AUDIT ( SYS_AUDIT_ID, TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE )
VALUES (NULL, 'XHB_LEGAL_AID_ORDER', 'AUD_LEGAL_AID_ORDER', 'Y');

INSERT INTO XHB_SYS_AUDIT ( SYS_AUDIT_ID, TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE )
VALUES (NULL, 'XHB_LEO_ADV_LINK', 'AUD_LEO_ADV_LINK', 'Y');

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.2', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.2', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.2.3', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.2', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off
