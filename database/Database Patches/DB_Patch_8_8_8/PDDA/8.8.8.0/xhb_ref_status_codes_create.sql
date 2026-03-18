/*    ------------------------------------------------------------------
*     CREATE XHB_REF_STATUS_CODES TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_REF_STATUS_CODES CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XHB_REF_STATUS_CODES (
	REF_STATUS_CODE_ID		NUMBER(8) NOT NULL,
	STATUS_CODE_TYPE		VARCHAR2(20) NOT NULL,
	STATUS_CODE				VARCHAR2(4),
	STATUS_CODE_DESCRIPTION	VARCHAR2(255),
	OBS_IND					VARCHAR2(1),
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(30) NOT NULL,
	VERSION					NUMBER(5) DEFAULT 1
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
ALTER TABLE XHB_REF_STATUS_CODES
  ADD (CONSTRAINT XHB_REF_STATUS_CODES_PK PRIMARY KEY (REF_STATUS_CODE_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_REF_STATUS_CODES TO PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_REF_STATUS_CODES
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_REF_STATUS_CODES';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_REF_STATUS_CODES
(
	REF_STATUS_CODE_ID		NUMBER(8) NOT NULL,
	STATUS_CODE_TYPE		VARCHAR2(20) NOT NULL,
	STATUS_CODE				VARCHAR2(4),
	STATUS_CODE_DESCRIPTION	VARCHAR2(255),
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
GRANT SELECT, INSERT, UPDATE ON AUD_REF_STATUS_CODES TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_STATUS_CODES';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_STATUS_CODES', 'AUD_REF_STATUS_CODES', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_STATUS_CODES
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE XHB_REF_STATUS_CODES_SEQ';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Create sequence table for XHB_REF_STATUS_CODES
CREATE SEQUENCE XHB_REF_STATUS_CODES_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_ref_status_codes_bir_tr.sql;
@xhb_ref_status_codes_bur_tr.sql;

COMMIT;