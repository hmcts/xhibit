/*    ------------------------------------------------------------------
*     CREATE XHB_REF_DAR_RETENTION_POLICIES TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_REF_DAR_RETENTION_POLICIES';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE XHB_REF_DAR_RETENTION_POLICIES (
	REF_DAR_RETENTION_POLICY_ID NUMBER(8) NOT NULL,
	POLICY_NO                   NUMBER(2),
	POLICY_DESCRIPTION          VARCHAR2(244),
	OBS_IND                     VARCHAR2(1),
	CREATED_BY                  VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY             VARCHAR2(30) NOT NULL,
	CREATION_DATE               DATE NOT NULL,
	LAST_UPDATE_DATE            DATE NOT NULL,
	VERSION                     NUMBER(5) DEFAULT 1
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
ALTER TABLE XHB_REF_DAR_RETENTION_POLICIES
  ADD (CONSTRAINT XHB_REF_RETENTION_POLICIES_PK PRIMARY KEY (REF_DAR_RETENTION_POLICY_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_REF_DAR_RETENTION_POLICIES TO PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_REF_DAR_RETENTION_POLICIES
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_REF_DAR_RETENTION_POLICIES';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_REF_DAR_RETENTION_POLICIES
(
	REF_DAR_RETENTION_POLICY_ID NUMBER(8) NOT NULL,
	POLICY_NO                   NUMBER(2),
	POLICY_DESCRIPTION          VARCHAR2(244),
	OBS_IND                     VARCHAR2(1),
	CREATED_BY                  VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY             VARCHAR2(30) NOT NULL,
	CREATION_DATE               DATE NOT NULL,
	LAST_UPDATE_DATE            DATE NOT NULL,
	VERSION                     NUMBER(5) DEFAULT 1,
	insert_event                VARCHAR2(1)
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
GRANT SELECT, INSERT, UPDATE ON AUD_REF_DAR_RETENTION_POLICIES TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_DAR_RETENTION_POLICIES';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_DAR_RETENTION_POLICIES', 'AUD_REF_DAR_RETENTION_POLICIES', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_DAR_RETENTION_POLICIES
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_DAR_RETENTION_POLICIES
CREATE SEQUENCE XHB_REF_RETENTION_POLICY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_ref_dar_retention_policies_bir_tr.sql;
@xhb_ref_dar_retention_policies_bur_tr.sql;

COMMIT;