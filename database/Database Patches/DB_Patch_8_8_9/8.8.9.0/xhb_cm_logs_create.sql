/*    ------------------------------------------------------------------
*     CREATE XHB_CM_LOGS TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_CM_LOGS CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XHB_CM_LOGS (
	CM_LOGS_ID			NUMBER(8) NOT NULL,
	ORIGINAL_UPLOADED_FILENAME VARCHAR2(30) NOT NULL,
	ORIGINAL_UPLOADED_FILE_CLOB_ID	NUMBER(8),
	PROCESSING_STATUS		VARCHAR2(8),
	PROCESSING_DATETIME		DATE,
	LOGS_CLOB_ID			NUMBER(8),
	FILE_CHECKSUM			VARCHAR2(100),
	UPLOAD_SIZE				NUMBER(3),
	NO_OF_CASES_IN_FILE		NUMBER(7),
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(35) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(35) NOT NULL,
	VERSION					NUMBER(5) DEFAULT 1,
	CONSTRAINT CMLOGS_ORIGFILECLOB_ID_FK
	FOREIGN KEY(ORIGINAL_UPLOADED_FILE_CLOB_ID)
	REFERENCES XHB_CLOB(CLOB_ID),
	CONSTRAINT CMLOGS_LOGSCLOB_ID_FK
	FOREIGN KEY(LOGS_CLOB_ID)
	REFERENCES XHB_CLOB(CLOB_ID)
)
TABLESPACE XHIBITD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_CM_LOGS
  ADD (CONSTRAINT XHB_CM_LOGS_PK PRIMARY KEY (CM_LOGS_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_CM_LOGS TO PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_CM_LOGS
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_CM_LOGS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_CM_LOGS
(
	CM_LOGS_ID				NUMBER(8) NOT NULL,
	ORIGINAL_UPLOADED_FILENAME		VARCHAR2(30),
	ORIGINAL_UPLOADED_FILE_CLOB_ID	NUMBER(8),
	PROCESSING_STATUS		VARCHAR2(8),
	PROCESSING_DATETIME		DATE,
	LOGS_CLOB_ID			NUMBER(8),
	FILE_CHECKSUM			VARCHAR2(100),
	UPLOAD_SIZE				NUMBER(3),
	NO_OF_CASES_IN_FILE		NUMBER(7),
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(35) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(35) NOT NULL,
	VERSION					NUMBER(5) DEFAULT 1,
	INSERT_EVENT			VARCHAR2(1)
)
TABLESPACE AUDITD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Grant/Revoke object privileges
GRANT SELECT, INSERT, UPDATE ON AUD_CM_LOGS TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CM_LOGS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CM_LOGS', 'AUD_CM_LOGS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CM_LOGS
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE XHB_CM_LOGS_SEQ';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Create sequence table for XHB_CM_LOGS
CREATE SEQUENCE XHB_CM_LOGS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_cm_logs_bir_tr.sql;
@xhb_cm_logs_bur_tr.sql;

COMMIT;