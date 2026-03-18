/*
 * Patch for DB release 27
 *
 * 15th October 2003
 */

/*
 * Modify index on column case_id on XHB_TIME to be unique
 */
drop index XHB_TIME_XHB_CASE_FK;

CREATE unique INDEX XHB_TIME_XHB_CASE_FK ON XHB_TIME
(CASE_ID)
LOGGING
TABLESPACE XHIBITX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          1M
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

/*
 * Constraint on COURT_ID, CASE_TYPE, CASE_NUMBER in XHB_CASE
 */
CREATE UNIQUE INDEX xhb_case_court_type_number on XHB_CASE
(COURT_ID ASC, 
 CASE_TYPE ASC,
 CASE_NUMBER ASC)
LOGGING
TABLESPACE XHIBITX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          1M
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            FREELISTS        1
            FREELIST GROUPS  1
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

/*
 * Add trigger to autogenerate identifer for primary key on XHB_CREST_IMPORT
 */
CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BIR_TR
  BEFORE INSERT
  ON XHB_CREST_IMPORT
  FOR EACH ROW

BEGIN

  IF :NEW.CREST_IMPORT_ID IS NULL THEN

    SELECT XHB_CREST_IMPORT_SEQ.NEXTVAL
    INTO   :NEW.CREST_IMPORT_ID
    FROM   DUAL;

  END IF;

END;
/


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = 27, last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;