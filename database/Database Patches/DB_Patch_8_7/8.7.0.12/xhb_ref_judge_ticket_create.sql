-- Create table. --
  CREATE TABLE XHB_REF_JUDGE_TICKET 
   (	REF_JUDGE_TICKET_ID NUMBER(8,0) NOT NULL, 
	JUDGE_ID NUMBER(8,0) NOT NULL, 
	TICKET_TYPE VARCHAR2(10) NOT NULL, 
	LAST_UPDATE_DATE DATE, 
	CREATION_DATE DATE NOT NULL, 
	CREATED_BY VARCHAR2(30) NOT NULL, 
	LAST_UPDATED_BY VARCHAR2(30), 
	VERSION NUMBER DEFAULT 1
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
  
-- Add primary key constraint --  
ALTER TABLE XHB_REF_JUDGE_TICKET 
	ADD CONSTRAINT XHB_REF_JUDGE_TICKET_PK PRIMARY KEY (REF_JUDGE_TICKET_ID)
USING INDEX
TABLESPACE XHIBITD
pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );

-- Add foreign key constraint --
ALTER TABLE XHB_REF_JUDGE_TICKET ADD (CONSTRAINT REF_JUDGE_TICKET_JUDGE_ID_FK FOREIGN KEY (JUDGE_ID)
REFERENCES XHB_REF_JUDGE (REF_JUDGE_ID));

-- Create sequence --
CREATE SEQUENCE  XHB_REF_JUDGE_TICKET_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE;

-- Grant/Revoke object privileges 
GRANT SELECT, INSERT, UPDATE ON XHB_REF_JUDGE_TICKET TO PUBLIC;

-- Create audit table
  CREATE TABLE AUD_REF_JUDGE_TICKET 
   (	REF_JUDGE_TICKET_ID NUMBER(8,0) NOT NULL, 
	JUDGE_ID NUMBER(8,0) NOT NULL, 
	TICKET_TYPE VARCHAR2(10) NOT NULL, 
	LAST_UPDATE_DATE DATE, 
	CREATION_DATE DATE NOT NULL, 
	CREATED_BY VARCHAR2(30) NOT NULL, 
	LAST_UPDATED_BY VARCHAR2(30), 
	VERSION NUMBER DEFAULT 1,
	INSERT_EVENT VARCHAR2(1)
   ) 
tablespace AUDITD
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
GRANT SELECT, INSERT, UPDATE ON AUD_REF_JUDGE_TICKET TO PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_JUDGE_TICKET';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_JUDGE_TICKET', 'AUD_REF_JUDGE_TICKET', 'Y');

/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------
@@xhb_ref_judge_ticket_bir_tr.sql;
/
@@xhb_ref_judge_ticket_bur_tr.sql;
/


