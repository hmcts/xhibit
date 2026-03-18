/*    ------------------------------------------------------------------
*     CREATE XHB_LEGAL_AID_AMENDMENT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_LEGAL_AID_AMENDMENT (
	LEGAL_AID_AMENDMENT_ID NUMBER(8) NOT NULL,
	LEGAL_AID_ORDER_ID NUMBER(8) NOT NULL,
	CHANGE_TYPE_ID NUMBER(8) NOT NULL,
	AMENDMENT_DATE DATE,
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1
)
tablespace XHIBITD
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
ALTER TABLE XHB_LEGAL_AID_AMENDMENT ADD (CONSTRAINT XHB_LEGAL_AID_ORDER_ID_FK FOREIGN KEY (LEGAL_AID_ORDER_ID) REFERENCES XHB_LEGAL_AID_ORDER(LEGAL_AID_ORDER_ID));
ALTER TABLE XHB_LEGAL_AID_AMENDMENT ADD (CONSTRAINT XHB_CHANGE_TYPE_ID_FK FOREIGN KEY (CHANGE_TYPE_ID) REFERENCES XHB_REF_SYSTEM_CODE(REF_SYSTEM_CODE_ID));
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_LEGAL_AID_AMENDMENT
  add constraint XHB_LEGAL_AID_AMENDMENT_PK primary key (LEGAL_AID_AMENDMENT_ID) 
  using index 
  tablespace XHIBITD
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_LEGAL_AID_AMENDMENT to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR LEGAL_AID_AMENDMENT
*/	------------------------------------------------------------------

create table AUD_LEGAL_AID_AMENDMENT
(
	LEGAL_AID_AMENDMENT_ID NUMBER(8) NOT NULL,
	LEGAL_AID_ORDER_ID NUMBER(8) NOT NULL,
	CHANGE_TYPE_ID NUMBER(8) NOT NULL,
	AMENDMENT_DATE DATE,
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1,
 	insert_event VARCHAR2(1)
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
grant select, insert, update on AUD_LEGAL_AID_AMENDMENT to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_LEGAL_AID_AMENDMENT';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_LEGAL_AID_AMENDMENT', 'AUD_LEGAL_AID_AMENDMENT', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_EMAIL_RECIPIENTS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_LEGAL_AID_AMENDMENT
create sequence XHB_LEGAL_AID_AMENDMENT_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_LEGAL_AID_AMENDMENT_bir_tr.sql;
@@XHB_LEGAL_AID_AMENDMENT_bur_tr.sql;

commit;
