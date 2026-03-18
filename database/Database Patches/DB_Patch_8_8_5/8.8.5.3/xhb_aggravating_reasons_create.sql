/*    ------------------------------------------------------------------
*     CREATE XHB_AGGRAVATING_REASONS TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

drop table XHB_AGGRAVATING_REASONS;
create table XHB_AGGRAVATING_REASONS (
	AGGRAVATING_REASONS_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	REF_AGGRAVATING_REASONS_ID NUMBER(8) NOT NULL,
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
alter table XHB_AGGRAVATING_REASONS
  add constraint XHB_AGGRAVATING_REASONS_PK primary key (AGGRAVATING_REASONS_ID) 
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

-- Foreign key constraints   
ALTER TABLE XHB_AGGRAVATING_REASONS ADD (CONSTRAINT AGGRAVATING_REASONS_DOC_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID));
ALTER TABLE XHB_AGGRAVATING_REASONS ADD (CONSTRAINT AGGRAVATING_REASONS_REF_FK FOREIGN KEY (REF_AGGRAVATING_REASONS_ID) REFERENCES XHB_REF_AGGRAVATING_REASONS(REF_AGGRAVATING_REASONS_ID));


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_AGGRAVATING_REASONS to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_AGGRAVATING_REASONS
*/	------------------------------------------------------------------
drop table AUD_AGGRAVATING_REASONS;
create table AUD_AGGRAVATING_REASONS
(
	AGGRAVATING_REASONS_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	REF_AGGRAVATING_REASONS_ID NUMBER(8) NOT NULL,
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
grant select, insert, update on AUD_AGGRAVATING_REASONS to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_AGGRAVATING_REASONS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_AGGRAVATING_REASONS', 'AUD_AGGRAVATING_REASONS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_AGGRAVATING_REASONS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_AGGRAVATING_REASONS
create sequence XHB_AGGRAVATING_REASONS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_aggravating_reasons_bir_tr.sql;
@xhb_aggravating_reasons_bur_tr.sql;

commit;