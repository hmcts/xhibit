/*    ------------------------------------------------------------------
*     CREATE XHB_D20_OFFENCE_LINK TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------
drop table XHB_D20_OFFENCE_LINK;
create table XHB_D20_OFFENCE_LINK (
	D20_OFFENCE_LINK_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	SEQ_NO NUMBER(4) NOT NULL,
	DVLA_OFFENCE_CODE VARCHAR2(4) NOT NULL,
	REF_OFFENCE_ID NUMBER(8) NOT NULL,
	INT_D20 VARCHAR2(1),
	INT_D20_DATE DATE,
	FINAL_D20 VARCHAR2(1),
	FINAL_D20_DATE DATE,
	OBS_IND VARCHAR2(1),
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
ALTER TABLE XHB_D20_OFFENCE_LINK ADD (CONSTRAINT D20_LINK_DEF_ON_CASE_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID));
ALTER TABLE XHB_D20_OFFENCE_LINK ADD (CONSTRAINT D20_LINK_REF_OFFENCE_ID_FK FOREIGN KEY (REF_OFFENCE_ID) REFERENCES XHB_REF_OFFENCE(REF_OFFENCE_ID));
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_D20_OFFENCE_LINK
  add constraint XHB_D20_OFFENCE_LINK_PK primary key (D20_OFFENCE_LINK_ID) 
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
grant select, insert, update on XHB_D20_OFFENCE_LINK to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_D20_OFFENCE_LINK
*/	------------------------------------------------------------------
drop table AUD_D20_OFFENCE_LINK;
create table AUD_D20_OFFENCE_LINK
(
	D20_OFFENCE_LINK_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	SEQ_NO NUMBER(4) NOT NULL,
	DVLA_OFFENCE_CODE VARCHAR2(4) NOT NULL,
	REF_OFFENCE_ID NUMBER(8) NOT NULL,
	INT_D20 VARCHAR2(1),
	INT_D20_DATE DATE,
	FINAL_D20 VARCHAR2(1),
	FINAL_D20_DATE DATE,
	OBS_IND VARCHAR2(1),
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
grant select, insert, update on AUD_D20_OFFENCE_LINK to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_D20_OFFENCE_LINK';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_D20_OFFENCE_LINK', 'AUD_D20_OFFENCE_LINK', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_D20_OFFENCE_LINK
*/	------------------------------------------------------------------

-- Create sequence table for XHB_D20_OFFENCE_LINK
create sequence XHB_D20_OFFENCE_LINK_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@XHB_D20_OFFENCE_LINK_bir_tr.sql;
@XHB_D20_OFFENCE_LINK_bur_tr.sql;

commit;