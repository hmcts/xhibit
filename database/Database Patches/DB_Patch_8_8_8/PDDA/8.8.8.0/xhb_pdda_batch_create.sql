/*    ------------------------------------------------------------------
*     CREATE XHB_PDDA_BATCH TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_PDDA_BATCH CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XHB_PDDA_BATCH (
	PDDA_BATCH_ID			NUMBER(8) NOT NULL,
	NO_OF_RECORDS_IN_BATCH	NUMBER(4) NOT NULL,
	BATCH_OPENED_DATETIME	DATE NOT NULL,
	BATCH_CLOSED_DATETIME	DATE,
	BATCH_STATUS_ID			NUMBER(8) NOT NULL,
	BATCH_MESSAGE			VARCHAR2(300),
	BATCH_NO_RESENDS		NUMBER(2),
	BATCH_SENT_TIME			DATE,
	OBS_IND					VARCHAR2(1),
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(30) NOT NULL,
	VERSION					NUMBER(5) DEFAULT 1,
	CONSTRAINT PDDABATCH_BATCH_STATUS_FK
	FOREIGN KEY(BATCH_STATUS_ID)
	REFERENCES XHB_REF_STATUS_CODES(REF_STATUS_CODE_ID)
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
ALTER TABLE XHB_PDDA_BATCH
  ADD (CONSTRAINT XHB_PDDA_BATCH_PK PRIMARY KEY (PDDA_BATCH_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));
				
-- Add indexes
CREATE INDEX XHB_PDDABATCH_BATCH_STATUS_IDX ON XHB_PDDA_BATCH(BATCH_STATUS_ID)
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

CREATE INDEX XHB_PDDABATCH_SENT_TIME_IDX ON XHB_PDDA_BATCH(BATCH_SENT_TIME)
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_PDDA_BATCH TO PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_PDDA_BATCH
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_PDDA_BATCH';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_PDDA_BATCH
(
	PDDA_BATCH_ID			NUMBER(8) NOT NULL,
	NO_OF_RECORDS_IN_BATCH	NUMBER(4) NOT NULL,
	BATCH_OPENED_DATETIME	DATE NOT NULL,
	BATCH_CLOSED_DATETIME	DATE,
	BATCH_STATUS_ID			NUMBER(8) NOT NULL,
	BATCH_MESSAGE			VARCHAR2(300),
	BATCH_NO_RESENDS		NUMBER(2),
	BATCH_SENT_TIME			DATE,
	OBS_IND					VARCHAR2(1),
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(30) NOT NULL,
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
GRANT SELECT, INSERT, UPDATE ON AUD_PDDA_BATCH TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_PDDA_BATCH';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_PDDA_BATCH', 'AUD_PDDA_BATCH', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_PDDA_BATCH
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE XHB_PDDA_BATCH_SEQ';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Create sequence table for XHB_PDDA_BATCH
CREATE SEQUENCE XHB_PDDA_BATCH_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_pdda_batch_bir_tr.sql;
@xhb_pdda_batch_bur_tr.sql;

COMMIT;