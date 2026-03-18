/*
 * Filename:    DB_Patch_7_5_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 *
 * Date:       6th June 2005
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

CREATE TABLE XHB_ORDER_TYPE_MAPPING (
       ORDER_TYPE_MAPPING_ID NUMBER(8) NOT NULL,
       ORDER_TYPE_ID NUMBER(8) NOT NULL,
       REPLACED_BY NUMBER(8) NULL,
       VERSION NUMBER(5) NOT NULL,
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL,
       CREATION_DATE DATE NOT NULL,
       LAST_UPDATE_DATE DATE NOT NULL
)
TABLESPACE XHIBITD 
STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0); 



ALTER TABLE XHB_ORDER_TYPE_MAPPING ADD (CONSTRAINT ORDER_TYPE_MAPPING_PK PRIMARY KEY (ORDER_TYPE_MAPPING_ID)
USING INDEX TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0));


ALTER TABLE XHB_IMPORT_EXPORT_STATUS MODIFY (TYPE_CODE VARCHAR2(7));

ALTER TABLE XHB_ORDER_TYPE_MAPPING ADD (CONSTRAINT ORDER_TEMP_TYPE_MAP_ID_FK FOREIGN KEY (ORDER_TYPE_ID) REFERENCES XHB_ORDER_TYPE (ORDER_TYPE_ID));

DELETE FROM xhb_order_disposal_xref;

ALTER TABLE xhb_order_disposal_xref RENAME COLUMN CO_REF_DISPOSAL_TYPE_ID TO ORDER_TYPE_ID;

ALTER TABLE xhb_order_disposal_xref DROP CONSTRAINT O_DISP_XREF_CO_REF_DISP_ID_FK;

ALTER TABLE xhb_order_disposal_xref ADD (CONSTRAINT O_ORDER_TYPE_ID_FK FOREIGN KEY (ORDER_TYPE_ID) REFERENCES XHB_ORDER_TYPE);

ALTER TABLE xhb_court MODIFY (PROBATION_OFFICE_NAME VARCHAR2(50));
 
/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */
CREATE TABLE AUD_ORDER_TYPE_MAPPING (
       ORDER_TYPE_MAPPING_ID NUMBER(8) NOT NULL,
       ORDER_TYPE_ID NUMBER(8) NOT NULL,
       REPLACED_BY NUMBER(8) NULL,
       VERSION NUMBER(5) NOT NULL,
       LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
       CREATED_BY VARCHAR2(30) NOT NULL,
       CREATION_DATE DATE NOT NULL,
       LAST_UPDATE_DATE DATE NOT NULL,
       INSERT_EVENT VARCHAR2(1) NOT NULL
)
TABLESPACE AUDITD;


ALTER TABLE AUD_IMPORT_EXPORT_STATUS MODIFY (TYPE_CODE VARCHAR2(7));

/*
 * Changes, additions or deletion of sequences
 */
CREATE SEQUENCE XHB_ORD_TYPE_MAP_SEQ NOMAXVALUE NOMINVALUE NOCACHE NOCYCLE NOORDER;

/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */


CREATE OR REPLACE TRIGGER XHIBIT.XHB_ORDER_IMP_EXP
BEFORE UPDATE
ON XHIBIT.XHB_ORDER 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
DECLARE

  l_orderType VARCHAR2(7) := NULL;
  l_caseId NUMBER(8) := NULL;
  l_courtId NUMBER(8) := NULL;

BEGIN

  IF (:NEW.ORDER_DELIVERY_STATUS_ID = 2) THEN

    SELECT OTYPE.CODE
    INTO   l_orderType
    FROM   XHB_ORDER_TYPE OTYPE,
           XHB_ORDER_TEMPLATE OTEMP
    WHERE  :NEW.ORDER_TEMPLATE_ID = OTEMP.ORDER_TEMPLATE_ID
    AND    OTEMP.ORDER_TYPE_ID = OTYPE.ORDER_TYPE_ID;

    SELECT CASE_ID
    INTO   l_caseId
    FROM   XHB_DEFENDANT_ON_CASE DC
    WHERE  DC.DEFENDANT_ON_CASE_ID = :NEW.DEFENDANT_ON_CASE_ID;

    SELECT COURT_ID
    INTO   l_courtId
    FROM   XHB_CASE C,
           XHB_DEFENDANT_ON_CASE DC
    WHERE  DC.DEFENDANT_ON_CASE_ID = :NEW.DEFENDANT_ON_CASE_ID
    AND    DC.CASE_ID = C.CASE_ID;

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE, STATUS_CODE, CASE_ID, COURT_ID, DEFENDANT_ON_CASE_ID)
    VALUES (l_orderType,'R', l_caseId, l_courtId, :OLD.DEFENDANT_ON_CASE_ID);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_ORDER_TYPE_MAPPING_BIR_TR
  BEFORE INSERT
  ON XHB_ORDER_TYPE_MAPPING
  FOR EACH ROW
BEGIN

  IF :NEW.ORDER_TYPE_MAPPING_ID IS NULL THEN

    SELECT XHB_ORD_TYPE_MAP_SEQ.NEXTVAL
    INTO   :NEW.ORDER_TYPE_MAPPING_ID
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



CREATE OR REPLACE TRIGGER XHB_ORDER_TYPE_MAPPING_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_TYPE_MAPPING
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  IF UPDATING THEN

    l_trig_event := 'U';

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

    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CONNECTED_USER') = 1) THEN

    INSERT INTO AUD_ORDER_TYPE_MAPPING
    VALUES (:OLD.ORDER_TYPE_MAPPING_ID,
            :OLD.ORDER_TYPE_ID,
            :OLD.REPLACED_BY,
            :OLD.VERSION,
            :OLD.LAST_UPDATED_BY,
            :OLD.CREATED_BY,
            :OLD.CREATION_DATE,
            :OLD.LAST_UPDATE_DATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_ORDER_DISPOSAL_XREF_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_ORDER_DISPOSAL_XREF
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_ORDER_DISPOSAL_XREF') = 1) THEN

    INSERT INTO AUD_ORDER_DISPOSAL_XREF
    VALUES (:OLD.order_disposal_xref_id,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            :OLD.COURT_ID,
            :OLD.rs_ref_disposal_type_id,
            :OLD.order_type_id,
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */
@code_release_control.sql

/*
 * Changes, additions or deletion of standing data
 */
-- New Community Order
INSERT INTO XHB_ORDER_TYPE (ORDER_TYPE_ID, CODE, REF_DISPOSAL_TYPE_ID, DESCRIPTION)
	VALUES (9, 'COMSENT', NULL, 'Community Order');


-- Bail Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (1, 1, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Bench Warrant - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (2, 2, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Community Punishment Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (3, 3, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Community Punishment and Rehabilitation Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (4, 4, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Young Offender Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (5, 5, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Community Rehabilitation Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (6, 6, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Imprisonment Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (7, 7, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- Remand Order - not replaced
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (8, 8, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

-- New Community Order replaced by New Community Order
INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (9, 9, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));


-- New Community Order
INSERT INTO XHB_ORDER_TEMPLATE ( ORDER_TEMPLATE_ID, DISPLAY_TRANSFORM_NAME, NARRATIVE_TEMPLATE_NAME, EDITOR_TEMPLATE_NAME, ORDER_TYPE_ID, OBS_IND ) VALUES ( 
9, '/metadata/OrderFOPTransform.xslt', '/metadata/COMSENT_Narrative.xml', '/metadata/COMSENTOrderTemplate.xml'
, 9, NULL);

commit;


/* FILE_NAME: XHIBIT2_Standing_Orders_Types_and_Templates.sql*/
/* ENVIRONMENT: Live System, Pre-production, System test, Integration, Development */
/* DESCRIPTION:The order types that are provided in XHIBIT2, and the templates relating to them */
/* ALso populate the order type mapping */
/* STATUS DESCRIPTION: required for live system */
/* DEPENDENCIES: Dependency on XHB_REF_DISPOSAL data existing */
/* OWNER: Doug Climie */

ALTER TABLE XHB_ORDER DISABLE CONSTRAINT ORDER_ORDER_TEMPLATE_ID_FK;

ALTER TABLE XHB_ORDER_TEMPLATE DISABLE CONSTRAINT ORDER_TEMP_ORDER_TYPE_ID_FK;

DELETE FROM XHB_ORDER_TEMPLATE;

DELETE FROM XHB_ORDER_TYPE_MAPPING;

DELETE FROM XHB_ORDER_TYPE;


COMMIT;



INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (1, 'BC', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Bail Conditions');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (2, 'BW', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Bench Warrant');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (3, 'CMPO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Punishment Order (5042)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (4, 'CMPRO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Punishment Rehabilitation Order (5042a)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (5, 'COMY', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Commitment of Young Offender (5044)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (6, 'CRO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Rehabilitation Order (5037)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (7, 'IMPO', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Imprisonment (5035)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (8, 'RC', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Remand in Custody (5038)');

INSERT INTO XHB_ORDER_TYPE 
(ORDER_TYPE_ID, CODE, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, ref_disposal_type_id, DESCRIPTION)
VALUES (9, 'COMSENT', 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate), NULL, 'Community Order');

INSERT INTO XHB_ORDER_TEMPLATE VALUES (1, '/metadata/OrderFOPTransform.xslt', '/metadata/BailOrder_Narrative.xml', '/metadata/BailOrderTemplate.xml', 1, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 1, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (2, '/metadata/OrderFOPTransform.xslt', '/metadata/BenchWarrantOrder_Narrative.xml', '/metadata/BenchWarrantOrderTemplate.xml', 2, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 2, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (3, '/metadata/OrderFOPTransform.xslt', '/metadata/CPO_Narrative.xml', '/metadata/CPOrderTemplate.xml', 3, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 3, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (4, '/metadata/OrderFOPTransform.xslt', '/metadata/CPRO_Narrative.xml', '/metadata/CPROrderTemplate.xml', 4, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 4, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (5, '/metadata/OrderFOPTransform.xslt', '/metadata/YoungOffendersOrder_Narrative.xml', '/metadata/YOIOrderTemplate.xml', 5, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 5, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (6, '/metadata/OrderFOPTransform.xslt', '/metadata/CRO_Narrative.xml', '/metadata/CROrderTemplate.xml', 6, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 6, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (7, '/metadata/OrderFOPTransform.xslt', '/metadata/ImprisonmentOrder_Narrative.xml', '/metadata/ImprisonmentOrderTemplate.xml', 7, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 7, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (8, '/metadata/OrderFOPTransform.xslt', '/metadata/RemandOrder_Narrative.xml', '/metadata/RemandOrderTemplate.xml', 8, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 8, null);

INSERT INTO XHB_ORDER_TEMPLATE VALUES (9, '/metadata/OrderFOPTransform.xslt', '/metadata/COMSENT_Narrative.xml', '/metadata/COMSENTOrderTemplate.xml', 9, 'JUNIT', 'JUNIT', trunc(sysdate), trunc(sysdate), 9, null);

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE)
VALUES (1, 1, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (2, 2, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (3, 3, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (4, 4, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (5, 5, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (6, 6, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (7, 7, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE)
VALUES (8, 8, NULL, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

INSERT INTO XHB_ORDER_TYPE_MAPPING (ORDER_TYPE_MAPPING_ID, ORDER_TYPE_ID, REPLACED_BY, VERSION,  LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) 
VALUES (9, 9, 9, 0, 'TEST', 'TEST',trunc(sysdate), trunc(sysdate));

COMMIT;

PROMPT Set ref_disposal_type_id foreign key value where 

UPDATE XHB_ORDER_TYPE xot
SET xot.REF_DISPOSAL_TYPE_ID = (SELECT xrdt.ref_disposal_type_id
                                       FROM
                                           (SELECT
                                               ref_disposal_type_id,
                                               disposal_code
                                            FROM
                                               xhb_ref_disposal_type
                                            WHERE
                                               court_id = p_court_id
                                            AND
                                               obs_ind = 'N'
                                            ORDER BY
                                               template_version DESC) xrdt
                                       WHERE
                                           xrdt.disposal_code = xot.code
                                       AND
                                           ROWNUM = 1);


PROMPT Now set the description via the ref_disposal_type_id link. this is set initially above
PROMPT but is updated here in case it has changed

    UPDATE XHB_ORDER_TYPE xot
    SET    xot.description = (SELECT title
                              FROM   xhb_ref_disposal_type xrdt
                              WHERE  xrdt.ref_disposal_type_id = xot.ref_disposal_type_id
                              AND    court_id = p_court_id)
    WHERE  xot.ref_disposal_type_id IS NOT NULL;

    COMMIT;





ALTER TABLE XHB_ORDER ENABLE CONSTRAINT ORDER_ORDER_TEMPLATE_ID_FK;

ALTER TABLE XHB_ORDER_TEMPLATE ENABLE CONSTRAINT ORDER_TEMP_ORDER_TYPE_ID_FK;

INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('BITS_Poll.mmc', sysdate, 'csa00100', 10, 'C');
INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('BITS_Submit_Poll.mmc', sysdate, 'csa00100', 10, 'C');
INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('CJI_EVENT_POLL.mmc', sysdate, 'csa00100', 10, 'C');
INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('CJIP_Poll.mmc', sysdate, 'csa00100', 10, 'C');
INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('CJIT_Poll.mmc', sysdate, 'csa00100', 10, 'C');
INSERT INTO MTBL_TIME_TRIGGER_STATUS VALUES ('CSU_BITS_REGISTER_POLL.mmc', sysdate, 'csa00100', 10, 'C');

commit;

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '7.5', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '7.5', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '7.5', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '7.5', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

