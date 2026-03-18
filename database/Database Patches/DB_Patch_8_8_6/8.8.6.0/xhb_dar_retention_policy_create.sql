/*    ------------------------------------------------------------------
*     CREATE XHB_DAR_RETENTION_POLICY TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_DAR_RETENTION_POLICY';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE XHB_DAR_RETENTION_POLICY (
	DAR_RETENTION_POLICY_ID      NUMBER(8) NOT NULL,
	DISPOSAL2_ID                 NUMBER(8),
	CASE_ID                      NUMBER(8),
	DEFENDANT_ON_CASE_ID         NUMBER(8),
	DEFENDANT_ON_OFFENCE_ID      NUMBER(8),
	REF_DISP_RETENTION_POLICY_ID NUMBER(8),
	REF_DAR_RETENTION_POLICY_ID  NUMBER(8),
	DURATION_DAYS                NUMBER(6),
	DURATION_MONTHS              NUMBER(6),
	DURATION_YEARS               NUMBER(6),
	HAS_LIFE                     VARCHAR2(1),
	IS_CONSECUTIVE               VARCHAR2(1),
	IS_UPDATED                   VARCHAR2(1) DEFAULT 'N' NOT NULL,
	OBS_IND                      VARCHAR2(1),
	CREATED_BY                   VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY              VARCHAR2(30) NOT NULL,
	CREATION_DATE                DATE NOT NULL,
	LAST_UPDATE_DATE             DATE NOT NULL,
	VERSION                      NUMBER(5) DEFAULT 1,
	CONSTRAINT XDRP_DISP_RETENTION_POLICY_FK
	FOREIGN KEY(REF_DISP_RETENTION_POLICY_ID)
	REFERENCES XHB_REF_DISP_RETENTION_POLICY(REF_DISP_RETENTION_POLICY_ID),
	CONSTRAINT XDRP_DISPOSAL2_FK
	FOREIGN KEY(DISPOSAL2_ID)
	REFERENCES XHB_DISPOSAL2(DISPOSAL2_ID),
	CONSTRAINT XDRP_CASE_FK
	FOREIGN KEY(CASE_ID)
	REFERENCES XHB_CASE(CASE_ID),
	CONSTRAINT XDRP_DEF_ON_CASE_FK
	FOREIGN KEY(DEFENDANT_ON_CASE_ID)
	REFERENCES XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID),
	CONSTRAINT XDRP_DEF_ON_OFFENCE_FK
	FOREIGN KEY(DEFENDANT_ON_OFFENCE_ID)
	REFERENCES XHB_DEFENDANT_ON_OFFENCE(DEFENDANT_ON_OFFENCE_ID),
	CONSTRAINT XDRP_REF_DARRETENTIONPOLICY_FK
	FOREIGN KEY (REF_DAR_RETENTION_POLICY_ID)
	REFERENCES XHB_REF_DAR_RETENTION_POLICIES(REF_DAR_RETENTION_POLICY_ID)
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
ALTER TABLE XHB_DAR_RETENTION_POLICY
  ADD (CONSTRAINT XHB_DAR_RETENTION_POLICY_PK PRIMARY KEY (DAR_RETENTION_POLICY_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Add indexes
CREATE INDEX XHB_DRP_CASE_IDX ON XHB_DAR_RETENTION_POLICY(CASE_ID)
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

CREATE INDEX XHB_DRP_DISPOSAL2_IDX ON XHB_DAR_RETENTION_POLICY(DISPOSAL2_ID)
TABLESPACE XHIBITX STORAGE (INITIAL 256K NEXT 256K PCTINCREASE 0);

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_DAR_RETENTION_POLICY TO PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_DAR_RETENTION_POLICY
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_DAR_RETENTION_POLICY';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_DAR_RETENTION_POLICY
(
	DAR_RETENTION_POLICY_ID      NUMBER(8) NOT NULL,
	DISPOSAL2_ID                 NUMBER(8),
	CASE_ID                      NUMBER(8),
	DEFENDANT_ON_CASE_ID         NUMBER(8),
	DEFENDANT_ON_OFFENCE_ID      NUMBER(8),
	REF_DISP_RETENTION_POLICY_ID NUMBER(8),
	REF_DAR_RETENTION_POLICY_ID  NUMBER(8),
	DURATION_DAYS                NUMBER(6),
	DURATION_MONTHS              NUMBER(6),
	DURATION_YEARS               NUMBER(6),
	HAS_LIFE                     VARCHAR2(1),
	IS_CONSECUTIVE               VARCHAR2(1),
	IS_UPDATED                   VARCHAR2(1) DEFAULT 'N' NOT NULL,
	OBS_IND                      VARCHAR2(1),
	CREATED_BY                   VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY              VARCHAR2(30) NOT NULL,
	CREATION_DATE                DATE NOT NULL,
	LAST_UPDATE_DATE             DATE NOT NULL,
	VERSION                      NUMBER(5) DEFAULT 1,
	insert_event                 VARCHAR2(1)
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
GRANT SELECT, INSERT, UPDATE ON AUD_DAR_RETENTION_POLICY TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_DAR_RETENTION_POLICY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_DAR_RETENTION_POLICY', 'AUD_DAR_RETENTION_POLICY', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_DAR_RETENTION_POLICY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_DAR_RETENTION_POLICY
CREATE SEQUENCE XHB_DAR_RETENTION_POLICY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_dar_retention_policy_bir_tr.sql;
@xhb_dar_retention_policy_bur_tr.sql;

COMMIT;