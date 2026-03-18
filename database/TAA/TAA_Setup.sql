DROP TABLE XHB_TRAINING_REF_DATA;

CREATE TABLE XHB_TRAINING_REF_DATA (
       PARAMETER  VARCHAR(64),
       COURT_ID   NUMBER,
       VALUE1     VARCHAR(256),
       VALUE2     VARCHAR(256))
         TABLESPACE XHIBITD
         STORAGE (INITIAL 64K
                  NEXT 64K
                  PCTINCREASE 0);

INSERT INTO XHB_TRAINING_REF_DATA VALUES ('TOMORROWS_LIST',1,'/export/home/oracle','Daily_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('DAILY_LIST_DIST',1,'/export/home/oracle','Daily_List_Dist.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST',1,'/export/home/oracle','Warned_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST_DIST',1,'/export/home/oracle','Warned_List_Dist.xml');

INSERT INTO XHB_TRAINING_REF_DATA VALUES ('TOMORROWS_LIST',2,'/export/home/oracle','Daily_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('DAILY_LIST_DIST',2,'/export/home/oracle','Daily_List_Dist.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST',2,'/export/home/oracle','Warned_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST_DIST',2,'/export/home/oracle','Warned_List_Dist.xml');

INSERT INTO XHB_TRAINING_REF_DATA VALUES ('TOMORROWS_LIST',3,'/export/home/oracle','Daily_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('DAILY_LIST_DIST',3,'/export/home/oracle','Daily_List_Dist.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST',3,'/export/home/oracle','Warned_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST_DIST',3,'/export/home/oracle','Warned_List_Dist.xml');

INSERT INTO XHB_TRAINING_REF_DATA VALUES ('TOMORROWS_LIST',4,'/export/home/oracle','Daily_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('DAILY_LIST_DIST',4,'/export/home/oracle','Daily_List_Dist.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST',4,'/export/home/oracle','Warned_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST_DIST',4,'/export/home/oracle','Warned_List_Dist.xml');

INSERT INTO XHB_TRAINING_REF_DATA VALUES ('TOMORROWS_LIST',5,'/export/home/oracle','Daily_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('DAILY_LIST_DIST',5,'/export/home/oracle','Daily_List_Dist.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST',5,'/export/home/oracle','Warned_List.xml');
INSERT INTO XHB_TRAINING_REF_DATA VALUES ('WARNED_LIST_DIST',5,'/export/home/oracle','Warned_List_Dist.xml');

COMMIT;



DROP TABLE TMP_SKELETON_DAY_DELETE;

CREATE TABLE TMP_SKELETON_DAY_DELETE (del_row_id ROWID);

DROP TABLE XHB_TRAINING_STATUS;

CREATE TABLE XHB_TRAINING_STATUS (
       COURT_ID        NUMBER,
       SCHEMA_NAME     VARCHAR2(32),
       OPERATION_CODE  VARCHAR2(1),
       STATUS_CODE     VARCHAR2(1),
       START_TIME      DATE,
       END_TIME        DATE)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 256K
                  NEXT 256K
                  PCTINCREASE 0);

DROP TABLE XHB_TAA_COURT_INFO;

CREATE TABLE XHB_TAA_COURT_INFO (
       COURT_ID        NUMBER,
       NAME            VARCHAR2(60),
       VALUE           VARCHAR2(255))
         TABLESPACE XHIBITD
         STORAGE (INITIAL 256K
                  NEXT 256K
                  PCTINCREASE 0);

DROP TABLE TMP_ORDER_IDS;

CREATE TABLE TMP_ORDER_IDS (ORDER_DELIVERY_STATUS_ID  NUMBER(8)    NOT NULL,
                            ORDER_STATUS_ID           NUMBER(8)    NOT NULL);

CREATE OR REPLACE TRIGGER TRAINING_STATUS_TR
  AFTER INSERT
  ON XHB_TRAINING_STATUS
  FOR EACH ROW

DECLARE

  l_jobno       NUMBER;
  l_results_out SYS_REFCURSOR;
  l_job_string  VARCHAR2(2000);

BEGIN

  IF :NEW.OPERATION_CODE = 'P' AND
     :NEW.STATUS_CODE = 'R' AND
     :NEW.SCHEMA_NAME = 'XHIBIT' THEN

    l_job_string := 'training_utils_pkg.restore_xhibit('||:NEW.COURT_ID||');';

    DBMS_JOB.SUBMIT(l_jobno,
                    'training_utils_pkg.restore_xhibit('||:NEW.COURT_ID||');',
                    trunc(sysdate),
                    NULL);
--    DBMS_JOB.SUBMIT(l_jobno,
--                    'training_utils_pkg.restore_xhibit(l_results_out, :NEW.COURT_ID);',
--                    trunc(sysdate),
--                    NULL);

  ELSIF :NEW.OPERATION_CODE = 'P' AND
        :NEW.STATUS_CODE = 'R' AND
        :NEW.SCHEMA_NAME = 'MLD' THEN

    DBMS_JOB.SUBMIT(l_jobno,
                    'training_utils_pkg.restore_mld_data('||:NEW.COURT_ID||');',
                    trunc(sysdate),
                    NULL);

--    DBMS_JOB.SUBMIT(l_jobno,
--                    'training_utils_pkg.restore_mld_data(l_results_out, :NEW.COURT_ID);',
--                    trunc(sysdate),
--                    NULL);

  ELSE

    NULL;

  END IF;

END;
/