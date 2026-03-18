/*    ------------------------------------------------------------------
*     CREATE XHB_MIGRATE_CASE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE XHB_MIGRATE_CASE CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE TABLE XHB_MIGRATE_CASE (
	MIGRATE_CASE_ID			NUMBER(8) NOT NULL,
	CASE_ID					NUMBER(8) NOT NULL,
	MIGRATED				VARCHAR2(1) NOT NULL,
	MIGRATION_TO			VARCHAR2(8) NOT NULL,
	MIGRATION_TO_URN		VARCHAR2(11),
	MIGRATION_DATE			DATE NOT NULL,
	LAST_UPDATE_DATE		DATE NOT NULL,
	CREATION_DATE			DATE NOT NULL,
	CREATED_BY				VARCHAR2(35) NOT NULL,
	LAST_UPDATED_BY			VARCHAR2(35) NOT NULL,
	VERSION					NUMBER(5) DEFAULT 1,
	CONSTRAINT MIGRATECASE_CASE_ID_FK
	FOREIGN KEY(CASE_ID)
	REFERENCES XHB_CASE(CASE_ID)
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
ALTER TABLE XHB_MIGRATE_CASE
  ADD (CONSTRAINT XHB_MIGRATE_CASE_PK PRIMARY KEY (MIGRATE_CASE_ID) 
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_MIGRATE_CASE TO PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_MIGRATE_CASE
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP TABLE AUD_MIGRATE_CASE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
CREATE TABLE AUD_MIGRATE_CASE
(
	MIGRATE_CASE_ID			NUMBER(8) NOT NULL,
	CASE_ID					NUMBER(8) NOT NULL,
	MIGRATED				VARCHAR2(1) NOT NULL,
	MIGRATION_TO			VARCHAR2(8) NOT NULL,
	MIGRATION_TO_URN		VARCHAR2(11),
	MIGRATION_DATE			DATE NOT NULL,
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
GRANT SELECT, INSERT, UPDATE ON AUD_MIGRATE_CASE TO PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_MIGRATE_CASE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_MIGRATE_CASE', 'AUD_MIGRATE_CASE', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_MIGRATE_CASE
*/	------------------------------------------------------------------
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE XHB_MIGRATE_CASE_SEQ';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- Create sequence table for XHB_MIGRATE_CASE
CREATE SEQUENCE XHB_MIGRATE_CASE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_migrate_case_bir_tr.sql;
@xhb_migrate_case_bur_tr.sql;

COMMIT;