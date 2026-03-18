/*
 * Filename:    DB_Patch_6.1_Prod.sql
 *
 * System:      Pre-Production & Production
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

ALTER TABLE XHB_COURT_SITE
      ADD (SHORT_NAME VARCHAR2(6));

ALTER TABLE XHB_COURT_SITE
      ADD (CONSTRAINT COURT_SITE_CTID_SHORTNAME_UC UNIQUE (COURT_ID, SHORT_NAME));

CREATE TABLE XHB_REF_DISP_MENU_CASE_TYPE (
       REF_DISPOSAL_MENU_CASE_TYPE_ID  NUMBER(8)    NOT NULL,
       REF_DISPOSAL_MENU_ID            NUMBER(8)    NOT NULL,
       CASE_TYPE                       VARCHAR2(1)  NULL,
       LAST_UPDATE_DATE                DATE         NOT NULL,
       CREATION_DATE                   DATE         NOT NULL,
       CREATED_BY                      VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY                 VARCHAR2(30) NOT NULL,
       VERSION                         NUMBER(5)    NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);

ALTER TABLE XHB_REF_DISP_MENU_CASE_TYPE
       ADD (CONSTRAINT REF_DISP_MENU_CASE_TYPE_PK PRIMARY KEY (REF_DISPOSAL_MENU_CASE_TYPE_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

ALTER TABLE XHB_REF_DISP_MENU_CASE_TYPE
      ADD (CONSTRAINT REF_DISP_MENU_CASE_TYPE_MEN_FK FOREIGN KEY (REF_DISPOSAL_MENU_ID) REFERENCES XHB_REF_DISPOSAL_MENU);

ALTER TABLE XHB_CASE
      ADD (RECEIPT_TYPE  VARCHAR2(2) NULL);

ALTER TABLE XHB_VERSION
      ADD (DISPLAY_NAME  VARCHAR2(30),
      DISPLAY_SEQ        NUMBER(2));


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

DROP TABLE TEMP_AUD_COURT_SITE;

CREATE TABLE TEMP_AUD_COURT_SITE TABLESPACE AUDITD AS SELECT * FROM AUD_COURT_SITE;

DROP TABLE AUD_COURT_SITE;

CREATE TABLE AUD_COURT_SITE TABLESPACE AUDITD AS SELECT * FROM XHB_COURT_SITE WHERE 1 = 0;
ALTER TABLE AUD_COURT_SITE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO AUD_COURT_SITE (SELECT COURT_SITE_ID,
                                   COURT_SITE_NAME,
                                   COURT_SITE_CODE,
                                   COURT_ID,
                                   ADDRESS_ID,
                                   LAST_UPDATE_DATE,
                                   CREATION_DATE,
                                   CREATED_BY,
                                   LAST_UPDATED_BY,
                                   VERSION,
                                   OBS_IND,
                                   DISPLAY_NAME,
                                   CREST_COURT_ID,
                                   NULL,
                                   INSERT_EVENT
                            FROM   TEMP_AUD_COURT_SITE);

DROP TABLE TEMP_AUD_COURT_SITE;

DROP TABLE AUD_REF_DISP_MENU_CASE_TYPE;

CREATE TABLE AUD_REF_DISP_MENU_CASE_TYPE TABLESPACE AUDITD AS SELECT * FROM XHB_REF_DISP_MENU_CASE_TYPE WHERE 1 = 0;
ALTER TABLE AUD_REF_DISP_MENU_CASE_TYPE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE TEMP_AUD_CASE;

CREATE TABLE TEMP_AUD_CASE TABLESPACE AUDITD AS SELECT * FROM AUD_CASE;

DROP TABLE AUD_CASE;

CREATE TABLE AUD_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_CASE WHERE 1 = 0;
ALTER TABLE AUD_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO AUD_CASE (SELECT CASE_ID,
                      CASE_NUMBER,
                      CASE_TYPE,
                      MAG_CONVICTION_DATE,
                      CASE_SUB_TYPE,
                      CASE_TITLE,
                      CASE_DESCRIPTION,
                      LINKED_CASE_ID,
                      BAIL_MAG_CODE,
                      REF_COURT_ID,
                      COURT_ID,
                      CHARGE_IMPORT_INDICATOR,
                      SEVERED_IND,
                      INDICT_RESP,
                      DATE_IND_REC,
                      PROS_AGENCY_REFERENCE,
                      LAST_UPDATE_DATE,
                      CREATION_DATE,
                      CREATED_BY,
                      LAST_UPDATED_BY,
                      VERSION,
                      CASE_CLASS,
                      JUDGE_REASON_FOR_APPEAL,
                      RESULTS_VERIFIED,
                      LENGTH_TAPE,
                      NO_PAGE_PROS_EVIDENCE,
                      NO_PROS_WITNESS,
                      EST_PDH_TRIAL_LENGTH,
                      INDICTMENT_INFO_1,
                      INDICTMENT_INFO_2,
                      INDICTMENT_INFO_3,
                      INDICTMENT_INFO_4,
                      INDICTMENT_INFO_5,
                      INDICTMENT_INFO_6,
                      POLICE_OFFICER_ATTENDING,
                      CPS_CASE_WORKER,
                      EXPORT_CHARGES,
                      IND_CHANGE_STATUS,
                      MAGISTRATES_CASE_REF,
                      CLASS_CODE,
                      OFFENCE_GROUP_UPDATE,
                      CCC_TRANS_TO_REF_COURT_ID,
                      NULL,
                      INSERT_EVENT
                      FROM   TEMP_AUD_CASE);

DROP TABLE TEMP_AUD_CASE;


/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE XHB_R_DISP_MENU_CASE_TYPE_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;


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

CREATE OR REPLACE TRIGGER XHB_REF_DISP_MENU_CTYPE_BIR_TR
  BEFORE INSERT
  ON XHB_REF_DISP_MENU_CASE_TYPE
  FOR EACH ROW

BEGIN

  IF :NEW.REF_DISPOSAL_MENU_CASE_TYPE_ID IS NULL THEN

    SELECT XHB_R_DISP_MENU_CASE_TYPE_SEQ.NEXTVAL
    INTO   :NEW.REF_DISPOSAL_MENU_CASE_TYPE_ID
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

CREATE OR REPLACE TRIGGER XHB_REF_DISP_MENU_CTYPE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_REF_DISP_MENU_CASE_TYPE
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_REF_DISP_MENU_CASE_TYPE') = 1) THEN

    INSERT INTO AUD_REF_DISP_MENU_CASE_TYPE 
    VALUES (:old.REF_DISPOSAL_MENU_CASE_TYPE_ID,
            :old.REF_DISPOSAL_MENU_ID,
            :old.CASE_TYPE,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION, 
            l_trig_event);

  END IF;

END;
/

ALTER TRIGGER XHB_CASE_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE
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

    IF :NEW.CASE_ID = :OLD.CASE_ID AND
       :NEW.COURT_ID = :OLD.COURT_ID AND
      (:NEW.CASE_NUMBER = :OLD.CASE_NUMBER OR
       (:NEW.CASE_NUMBER IS NULL AND :OLD.CASE_NUMBER IS NULL)) AND
      (:NEW.CASE_TYPE = :OLD.CASE_TYPE OR
       (:NEW.CASE_TYPE IS NULL AND :OLD.CASE_TYPE IS NULL)) AND
      (:NEW.MAG_CONVICTION_DATE = :OLD.MAG_CONVICTION_DATE OR
       (:NEW.MAG_CONVICTION_DATE IS NULL AND :OLD.MAG_CONVICTION_DATE IS NULL)) AND
      (:NEW.CASE_SUB_TYPE = :OLD.CASE_SUB_TYPE OR
       (:NEW.CASE_SUB_TYPE IS NULL AND :OLD.CASE_SUB_TYPE IS NULL)) AND
      (:NEW.CASE_TITLE = :OLD.CASE_TITLE OR
       (:NEW.CASE_TITLE IS NULL AND :OLD.CASE_TITLE IS NULL)) AND
      (:NEW.CASE_DESCRIPTION = :OLD.CASE_DESCRIPTION OR
       (:NEW.CASE_DESCRIPTION IS NULL AND :OLD.CASE_DESCRIPTION IS NULL)) AND
      (:NEW.LINKED_CASE_ID = :OLD.LINKED_CASE_ID OR
       (:NEW.LINKED_CASE_ID IS NULL AND :OLD.LINKED_CASE_ID IS NULL)) AND
      (:NEW.BAIL_MAG_CODE = :OLD.BAIL_MAG_CODE OR
       (:NEW.BAIL_MAG_CODE IS NULL AND :OLD.BAIL_MAG_CODE IS NULL)) AND
      (:NEW.REF_COURT_ID = :OLD.REF_COURT_ID OR
       (:NEW.REF_COURT_ID IS NULL AND :OLD.REF_COURT_ID IS NULL)) AND
      (:NEW.SEVERED_IND = :OLD.SEVERED_IND OR
       (:NEW.SEVERED_IND IS NULL AND :OLD.SEVERED_IND IS NULL)) AND
      (:NEW.INDICT_RESP = :OLD.INDICT_RESP OR
       (:NEW.INDICT_RESP IS NULL AND :OLD.INDICT_RESP IS NULL)) AND
      (:NEW.DATE_IND_REC = :OLD.DATE_IND_REC OR
       (:NEW.DATE_IND_REC IS NULL AND :OLD.DATE_IND_REC IS NULL)) AND
      (:NEW.PROS_AGENCY_REFERENCE = :OLD.PROS_AGENCY_REFERENCE OR
       (:NEW.PROS_AGENCY_REFERENCE IS NULL AND :OLD.PROS_AGENCY_REFERENCE IS NULL)) AND
      (:NEW.CASE_CLASS = :OLD.CASE_CLASS OR
       (:NEW.CASE_CLASS IS NULL AND :OLD.CASE_CLASS IS NULL)) AND
      (:NEW.JUDGE_REASON_FOR_APPEAL = :OLD.JUDGE_REASON_FOR_APPEAL OR
       (:NEW.JUDGE_REASON_FOR_APPEAL IS NULL AND :OLD.JUDGE_REASON_FOR_APPEAL IS NULL)) AND
      (:NEW.RESULTS_VERIFIED = :OLD.RESULTS_VERIFIED OR
       (:NEW.RESULTS_VERIFIED IS NULL AND :OLD.RESULTS_VERIFIED IS NULL)) AND
      (:NEW.LENGTH_TAPE = :OLD.LENGTH_TAPE OR
       (:NEW.LENGTH_TAPE IS NULL AND :OLD.LENGTH_TAPE IS NULL)) AND
      (:NEW.NO_PAGE_PROS_EVIDENCE = :OLD.NO_PAGE_PROS_EVIDENCE OR
       (:NEW.NO_PAGE_PROS_EVIDENCE IS NULL AND :OLD.NO_PAGE_PROS_EVIDENCE IS NULL)) AND
      (:NEW.NO_PROS_WITNESS = :OLD.NO_PROS_WITNESS OR
       (:NEW.NO_PROS_WITNESS IS NULL AND :OLD.NO_PROS_WITNESS IS NULL)) AND
      (:NEW.EST_PDH_TRIAL_LENGTH = :OLD.EST_PDH_TRIAL_LENGTH OR
       (:NEW.EST_PDH_TRIAL_LENGTH IS NULL AND :OLD.EST_PDH_TRIAL_LENGTH IS NULL)) AND
      (:NEW.INDICTMENT_INFO_1 = :OLD.INDICTMENT_INFO_1 OR
       (:NEW.INDICTMENT_INFO_1 IS NULL AND :OLD.INDICTMENT_INFO_1 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_2 = :OLD.INDICTMENT_INFO_2 OR
       (:NEW.INDICTMENT_INFO_2 IS NULL AND :OLD.INDICTMENT_INFO_2 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_3 = :OLD.INDICTMENT_INFO_3 OR
       (:NEW.INDICTMENT_INFO_3 IS NULL AND :OLD.INDICTMENT_INFO_3 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_4 = :OLD.INDICTMENT_INFO_4 OR
       (:NEW.INDICTMENT_INFO_4 IS NULL AND :OLD.INDICTMENT_INFO_4 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_5 = :OLD.INDICTMENT_INFO_5 OR
       (:NEW.INDICTMENT_INFO_5 IS NULL AND :OLD.INDICTMENT_INFO_5 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_6 = :OLD.INDICTMENT_INFO_6 OR
       (:NEW.INDICTMENT_INFO_6 IS NULL AND :OLD.INDICTMENT_INFO_6 IS NULL)) AND
      (:NEW.POLICE_OFFICER_ATTENDING = :OLD.POLICE_OFFICER_ATTENDING OR
       (:NEW.POLICE_OFFICER_ATTENDING IS NULL AND :OLD.POLICE_OFFICER_ATTENDING IS NULL)) AND
      (:NEW.CPS_CASE_WORKER = :OLD.CPS_CASE_WORKER OR
       (:NEW.CPS_CASE_WORKER IS NULL AND :OLD.CPS_CASE_WORKER IS NULL)) AND
      (:NEW.EXPORT_CHARGES = :OLD.EXPORT_CHARGES OR
       (:NEW.EXPORT_CHARGES IS NULL AND :OLD.EXPORT_CHARGES IS NULL)) AND
      (:NEW.MAGISTRATES_CASE_REF = :OLD.MAGISTRATES_CASE_REF OR
       (:NEW.MAGISTRATES_CASE_REF IS NULL AND :OLD.MAGISTRATES_CASE_REF IS NULL)) AND
      (:NEW.CLASS_CODE = :OLD.CLASS_CODE OR
       (:NEW.CLASS_CODE IS NULL AND :OLD.CLASS_CODE IS NULL)) AND
      (:NEW.OFFENCE_GROUP_UPDATE = :OLD.OFFENCE_GROUP_UPDATE OR
       (:NEW.OFFENCE_GROUP_UPDATE IS NULL AND :OLD.OFFENCE_GROUP_UPDATE IS NULL)) AND
      (:NEW.CCC_TRANS_TO_REF_COURT_ID = :OLD.CCC_TRANS_TO_REF_COURT_ID OR
       (:NEW.CCC_TRANS_TO_REF_COURT_ID IS NULL AND :OLD.CCC_TRANS_TO_REF_COURT_ID IS NULL)) AND
      (:NEW.RECEIPT_TYPE = :OLD.RECEIPT_TYPE OR
       (:NEW.RECEIPT_TYPE IS NULL AND :OLD.RECEIPT_TYPE IS NULL)) THEN

      -- Only the CHARGE_IMPORT_INDICATOR has changed so do not increase VERSION
      -- This will even come into this section if the CHARGE_IMPORT_INDICATOR column is
      -- updated to the same value with all others staying the same

      SELECT SYSDATE
      INTO   :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    ELSE

      -- Other fields have changes so increase VERSION

      SELECT :OLD.VERSION + 1,
             SYSDATE
      INTO   :NEW.VERSION,
             :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    END IF;

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN

    INSERT INTO AUD_CASE 
    VALUES (:old.CASE_ID, 
            :old.CASE_NUMBER, 
            :old.CASE_TYPE, 
            :old.MAG_CONVICTION_DATE, 
            :old.CASE_SUB_TYPE, 
            :old.CASE_TITLE, 
            :old.CASE_DESCRIPTION, 
            :old.LINKED_CASE_ID, 
            :old.BAIL_MAG_CODE, 
            :old.REF_COURT_ID, 
            :old.COURT_ID, 
            :old.CHARGE_IMPORT_INDICATOR, 
            :old.SEVERED_IND, 
            :old.INDICT_RESP, 
            :old.DATE_IND_REC, 
            :old.PROS_AGENCY_REFERENCE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.CASE_CLASS, 
            :old.JUDGE_REASON_FOR_APPEAL, 
            :old.RESULTS_VERIFIED, 
            :old.LENGTH_TAPE, 
            :old.NO_PAGE_PROS_EVIDENCE, 
            :old.NO_PROS_WITNESS, 
            :old.EST_PDH_TRIAL_LENGTH, 
            :old.indictment_info_1, 
            :old.indictment_info_2, 
            :old.indictment_info_3, 
            :old.indictment_info_4, 
            :old.indictment_info_5, 
            :old.indictment_info_6, 
            :old.POLICE_OFFICER_ATTENDING, 
            :old.CPS_CASE_WORKER, 
            :old.EXPORT_CHARGES, 
            :old.IND_CHANGE_STATUS,
            :old.MAGISTRATES_CASE_REF,
            :old.CLASS_CODE,
            :old.OFFENCE_GROUP_UPDATE,
            :old.CCC_TRANS_TO_REF_COURT_ID,
            :old.RECEIPT_TYPE,
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_post_merc_ref_data_pkg AS

  PROCEDURE standing_post_merc(p_main_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_cr_live_status;

  PROCEDURE standing_formb_result(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_order_disposal_xref(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_orders_types_temps(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE standing_ref_disp_menu_ctype;

END xhb_post_merc_ref_data_pkg;
/
show errors

/*
 * These procedures can be run after a Mercator Crest ref data load.
 * All are run after a new build, with standing_formb_result and
 * standing_order_disposal_xref being run for each court loaded.
 * Each one (4) are run once a new court is also added.
 */

CREATE OR REPLACE PACKAGE BODY xhb_post_merc_ref_data_pkg AS

  PROCEDURE standing_post_merc(p_main_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    xhb_post_merc_ref_data_pkg.standing_formb_result(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_order_disposal_xref(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_orders_types_temps(p_main_court_id);

    xhb_post_merc_ref_data_pkg.standing_ref_disp_menu_ctype;

  END standing_post_merc;

  PROCEDURE standing_cr_live_status IS

  v_max_court_room_id NUMBER;

  BEGIN

    SELECT MAX(court_room_id)
    INTO   v_max_court_room_id
    FROM   xhb_cr_live_status;

    IF v_max_court_room_id IS NOT NULL THEN

      -- entries already exists so only insert for court rooms with higher court_room_id

      v_max_court_room_id := v_max_court_room_id + 1;

      INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
      SELECT court_room_id,
             SYSDATE,
             'No Information to display'
      FROM   xhb_court_room
      WHERE  court_room_id >= v_max_court_room_id;

    ELSE

      -- table is empty so insert for ALL court rooms

      INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
      SELECT court_room_id,
             SYSDATE,
             'No Information to display'
      FROM   xhb_court_room;

    END IF;

  END standing_cr_live_status;

  PROCEDURE standing_formb_result (p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    DELETE FROM XHB_FORMB_RESULT
    WHERE  COURT_ID = p_court_id;

    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'G' AND COURT_ID = p_court_id ), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NG' AND COURT_ID = p_court_id ), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AA' AND COURT_ID = p_court_id), 'Verdict', p_court_id); 
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'AC' AND COURT_ID = p_court_id), 'Verdict', p_court_id); 
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'DUD' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GA' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GAJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GJJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GL' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'GLJ' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'JUTA' AND COURT_ID = p_court_id), 'Other', p_court_id); 
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGIS' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJJ' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NGJU' AND COURT_ID = p_court_id), 'Verdict', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'NV' AND COURT_ID = p_court_id), 'Other', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'O' AND COURT_ID = p_court_id), 'Other', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTG' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_VERDICT_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('V', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'VERDICT' AND CODE = 'RTNG' AND COURT_ID = p_court_id), 'Verdict', p_court_id);

    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NPT' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AA' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'G' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'NG' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'AC' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPG' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPGJ' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'CPNG' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GAO' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'GLO' AND COURT_ID = p_court_id), 'Conviction', p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'O' AND COURT_ID = p_court_id), NULL, p_court_id);
    INSERT INTO XHB_FORMB_RESULT(FORMB_RESULT_TYPE,REF_PLEA_ID,RESULT_DESCRIPTION,COURT_ID) VALUES ('P', (SELECT REF_SYSTEM_CODE_ID FROM XHB_REF_SYSTEM_CODE WHERE CODE_TYPE = 'PLEA' AND CODE = 'P' AND COURT_ID = p_court_id), NULL, p_court_id);

    COMMIT;

  END standing_formb_result;

  PROCEDURE standing_order_disposal_xref(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  /*
   * DESCRIPTION:  This script loops through each court in XHB_COURT. Then it selects the CREST CO disposals
   *               that Xhibit is producing (second cursor). It then loops through the matching RC disposal types
   *               (the mapping is defined in the linked_rs_data cursor) and inserts a row into the
   *               XHB_ORDER_DISPOSAL_XREF table.
   *
   * DEPENDENCIES: This must be run AFTER the CREST XHB_REF_DISPOSAL_TYPE data has been created, otherwsie it will do nothing.
   */

  CURSOR xrd_co_data IS
    SELECT ref_disposal_type_id,
           disposal_code,
           court_id,
           menu_group
    FROM   XHB_REF_DISPOSAL_TYPE
    WHERE  menu_group in ('CO')
    AND    disposal_code in ('RC','IMPO','COMY','CMPO','CMPRO','CRO')      -- the order CO codes we are interested in
    AND    NVL(obs_ind,'N') = 'N'
    AND    court_id = p_court_id;  

  /* This cursor holds the actual details of the mapping */

  CURSOR linked_rs_data (v_co_disposal_code VARCHAR2) IS
    SELECT ref_disposal_type_id,
           disposal_code,
           menu_group
    FROM   XHB_REF_DISPOSAL_TYPE
    WHERE  menu_group in ('RS')
    AND    NVL(obs_ind,'N') = 'N'
    AND    court_id = p_court_id 
    AND    (v_co_disposal_code = 'RC' AND disposal_code IN ('REMMENT','REMTREA')) OR
           (v_co_disposal_code = 'IMPO' AND disposal_code IN ('IMPMIN','LIFE','LIFESEC','LIMM','CSSP','CTFL','CUSTEXT','IMP','ODIMP')) OR
           (v_co_disposal_code = 'COMY' AND disposal_code IN ('DET','DWLT','IMPMYO','LIFESYO','YOI','CSSP','CTFL','CUSTEXT','IMP','ODIMP')) OR
           (v_co_disposal_code = 'CMPO' AND disposal_code IN ('CPO','CPODAR')) OR
           (v_co_disposal_code = 'CMPRO' AND disposal_code IN ('CPDRCUR','CPDRDAR','CPDREXC','CPDRHR','CPDRNH','CPDROA',
                                                               'CPDROC','CPDROD','CPDROG','CPDROR','CPRCUR','CPRDAR',
                                                               'CPREXC','CPRHR','CPRNH','CPROA','CPROC','CPROD',
                                                               'CPROG','CPROH','CPROR')) OR
           (v_co_disposal_code = 'CRO' AND disposal_code IN ('CRCR','CRDA','CRDC','CRHR','CRNH','CROCUR','CRODAR','CROEXC','CROG','CRRA'));

  -- temp variables, for sqlplus check of data

  v_co_ref_disposal_type_id  varchar2(20):= NULL;
  v_co_data_disposal_code    varchar2(20):= NULL;
  v_co_data_court_id         varchar2(20):= NULL;
  v_rs_ref_disposal_type_id  varchar2(20):= NULL;
  v_rs_data_disposal_code    varchar2(20):= NULL;

  BEGIN

    DELETE FROM XHB_ORDER_DISPOSAL_XREF
    WHERE  COURT_ID = p_court_id;

    /* Loop through each CO type we are interested in, get the matching RS values and create a row in the target table for each one.  */

    FOR r_xrd_co_data IN xrd_co_data LOOP

      FOR r_linked_rs_data IN linked_rs_data(r_xrd_co_data.disposal_code) LOOP  -- multiple rows for each CO code

        v_co_ref_disposal_type_id := to_char(r_xrd_co_data.ref_disposal_type_id);
        v_co_data_disposal_code := r_xrd_co_data.disposal_code;
        v_co_data_court_id := to_char(p_court_id);
        v_rs_ref_disposal_type_id := to_char(r_linked_rs_data.ref_disposal_type_id);
        v_rs_data_disposal_code := r_linked_rs_data.disposal_code;

        -- Create a record in the target table

        INSERT INTO XHB_ORDER_DISPOSAL_XREF (order_disposal_xref_id,
                                             court_id,
                                             rs_ref_disposal_type_id,
                                             co_ref_disposal_type_id,
                                             version,
                                             creation_date,
                                             last_update_date,
                                             created_by,
                                             last_updated_by)
                                     VALUES (NULL,
                                             v_co_data_court_id,
                                             v_rs_ref_disposal_type_id,
                                             v_co_ref_disposal_type_id,
                                             1,
                                             SYSDATE,
                                             SYSDATE,
                                             USER,
                                             USER);

        -- output details for visual confirmation (if serveroutput is set to on)

        -- dbms_output.put_line('Inserting row: '||'ID '||to_char(v_id)||':'||v_co_ref_disposal_type_id||':'||v_co_data_disposal_code);

      END LOOP;   -- RS code loop

    END LOOP;   -- CO code loop

    COMMIT;

  END standing_order_disposal_xref;

  PROCEDURE standing_orders_types_temps(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  /* DESCRIPTION:  The order types that are provided in XHIBIT2, and the templates relating to them.
   *
   * DEPENDENCIES: Dependency on XHB_REF_DISPOSAL data existing.
   */

  v_order_type_id_count     NUMBER;
  v_order_template_id_count NUMBER;

  BEGIN
    
    SELECT count(*)
    INTO   v_order_type_id_count
    FROM   XHB_ORDER_TYPE;

    IF v_order_type_id_count = 0 THEN

      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (1, 'BC', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Bail Conditions');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (2, 'BW', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Bench Warrant');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (3, 'CMPO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Punishment Order (5042)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (4, 'CMPRO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Punishment Rehabilitation Order (5042a)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (5, 'COMY', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Commitment of Young Offender (5044)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (6, 'CRO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Community Rehabilitation Order (5037)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (7, 'IMPO', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Imprisonment (5035)');
      INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
                          VALUES (8, 'RC', 0, 'TEST', 'TEST',TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'Remand in Custody (5038)');

      COMMIT;

    END IF;

    SELECT count(*)
    INTO   v_order_template_id_count
    FROM   XHB_ORDER_TEMPLATE;

    IF v_order_template_id_count = 0 THEN

      INSERT INTO XHB_ORDER_TEMPLATE VALUES (1, '/metadata/OrderFOPTransform.xslt', '/metadata/BailOrder_Narrative.xml', '/metadata/BailOrderTemplate.xml', 1, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 1, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (2, '/metadata/OrderFOPTransform.xslt', '/metadata/BenchWarrantOrder_Narrative.xml', '/metadata/BenchWarrantOrderTemplate.xml', 2, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 2, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (3, '/metadata/OrderFOPTransform.xslt', '/metadata/CPO_Narrative.xml', '/metadata/CPOrderTemplate.xml', 3, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 3, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (4, '/metadata/OrderFOPTransform.xslt', '/metadata/CPRO_Narrative.xml', '/metadata/CPROrderTemplate.xml', 4, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 4, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (5, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder_Narrative.xml', '/metadata/YOIOrderTemplate.xml', 5, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 5, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (6, '/metadata/OrderFOPTransform.xslt', '/metadata/CRO_Narrative.xml', '/metadata/CROrderTemplate.xml', 6, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 6, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (7, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder_Narrative.xml', '/metadata/ImprisonmentOrderTemplate.xml', 7, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 7, NULL);
      INSERT INTO XHB_ORDER_TEMPLATE VALUES (8, '/metadata/OrderFOPTransform.xslt', '/metadata/RemandOrder_Narrative.xml', '/metadata/RemandOrderTemplate.xml', 8, 'JUNIT', 'JUNIT', TRUNC(SYSDATE), TRUNC(SYSDATE), 8, NULL);

      COMMIT;

    END IF;

    UPDATE XHB_ORDER_TYPE xot
    SET    xot.REF_DISPOSAL_TYPE_ID = (SELECT ref_disposal_type_id
                                       FROM   xhb_ref_disposal_type xrdt
                                       WHERE  xrdt.disposal_code = xot.code
                                       AND    court_id = p_court_id);

    UPDATE XHB_ORDER_TYPE xot
    SET    xot.description = (SELECT title
                              FROM   xhb_ref_disposal_type xrdt
                              WHERE  xrdt.ref_disposal_type_id = xot.ref_disposal_type_id
                              AND    court_id = p_court_id)
    WHERE  xot.ref_disposal_type_id IS NOT NULL;

    COMMIT;

  END standing_orders_types_temps;

  PROCEDURE standing_ref_disp_menu_ctype IS


  BEGIN

    DELETE FROM XHB_REF_DISP_MENU_CASE_TYPE;

    INSERT INTO XHB_REF_DISP_MENU_CASE_TYPE (REF_DISPOSAL_MENU_CASE_TYPE_ID,
                                             REF_DISPOSAL_MENU_ID,
                                             CASE_TYPE)
                                     SELECT  NULL,
                                             rdmm.REF_DISPOSAL_MENU_ID,
                                             'S'
                                     FROM    XHB_REF_DISPOSAL_MENU rdmm,
                                             XHB_REF_DISPOSAL_MENU rdmg
                                     WHERE  (rdmm.parent = rdmg.menu_item_id OR
                                             rdmm.menu_item_id = rdmg.menu_item_id)
                                     AND    rdmg.abbrev = 'STNOIND';

  END standing_ref_disp_menu_ctype;

END xhb_post_merc_ref_data_pkg;
/
show errors


/*
 * Changes, additions or deletion of standing data
 */

INSERT INTO XHB_SYS_AUDIT VALUES (NULL, 'XHB_REF_DISP_MENU_CASE_TYPE', 'AUD_REF_DISP_MENU_CASE_TYPE', 'Y');


/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('JAVA',
                         '6.1',
                         SYSDATE,
                         'RELEASE',
                         'Java Application',
                         1);

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('XHIBIT',
                         '6.1',
                         SYSDATE,
                         'RELEASE',
                         'Database',
                         2);

INSERT INTO XHB_VERSION (SCHEMA_NAME,
                         SCHEMA_VERSION,
                         LAST_UPDATE_DATE,
                         UPDATED_BY,
                         DISPLAY_NAME,
                         DISPLAY_SEQ)
                 VALUES ('MERCATOR',
                         '6.1',
                         SYSDATE,
                         'RELEASE',
                         'Mercator',
                          3); 

COMMIT;

DECLARE

  l_syn_name  VARCHAR2(30);
  l_str1      VARCHAR2(50) := 'DROP PUBLIC SYNONYM ';
  l_stmt      VARCHAR2(500);

  CURSOR c_synonyms IS
    SELECT synonym_name
    FROM   dba_synonyms
    WHERE  table_owner = 'XHIBIT'
    AND    table_name like 'XHB_%'
    AND    owner = 'PUBLIC';

BEGIN

  /*
   * Remove any PUBLIC synonymns
   */

  OPEN c_synonyms;

  LOOP

    FETCH c_synonyms
    INTO  l_syn_name;

    EXIT WHEN c_synonyms%NOTFOUND;

    l_stmt := l_str1||l_syn_name;

    EXECUTE IMMEDIATE l_stmt;

  END LOOP;

  CLOSE c_synonyms;

END;
/
