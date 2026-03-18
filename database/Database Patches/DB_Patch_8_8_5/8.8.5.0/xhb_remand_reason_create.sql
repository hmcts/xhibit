/*    ------------------------------------------------------------------
*     CREATE XHB_REMAND_REASONS TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

drop table XHB_REMAND_REASONS;
create table XHB_REMAND_REASONS (
	REMAND_REASONS_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	ORDER_ID NUMBER(8) NOT NULL,
	REMAND_REASON_DESCRIPTION_ID NUMBER(8) NOT NULL,
	ADDITIONAL_INFORMATION VARCHAR2(500),
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

-- No foreign key constraints 
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_REMAND_REASONS
  add constraint XHB_REMAND_REASONS_PK primary key (REMAND_REASONS_ID) 
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

ALTER TABLE XHB_REMAND_REASONS ADD (CONSTRAINT REMAND_REASONS_DEF_ON_CASE_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID));
ALTER TABLE XHB_REMAND_REASONS ADD (CONSTRAINT REMAND_REASONS_ORDER_FK FOREIGN KEY (ORDER_ID) REFERENCES XHB_ORDER(ORDER_ID));
ALTER TABLE XHB_REMAND_REASONS ADD (CONSTRAINT REMAND_REASONS_DESC_FK FOREIGN KEY (REMAND_REASON_DESCRIPTION_ID) REFERENCES XHB_REMAND_REASON_DESCRIPTION(REMAND_REASON_DESCRIPTION_ID));

-- Grant/Revoke object privileges 
grant select, insert, update on XHB_REMAND_REASONS to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_REMAND_REASONS
*/	------------------------------------------------------------------
drop table AUD_REMAND_REASONS;
create table AUD_REMAND_REASONS
(
	REMAND_REASONS_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	ORDER_ID NUMBER(8) NOT NULL,
	REMAND_REASON_DESCRIPTION_ID NUMBER(8) NOT NULL,
	ADDITIONAL_INFORMATION VARCHAR2(500),
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
grant select, insert, update on AUD_REMAND_REASONS to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REMAND_REASONS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REMAND_REASONS', 'AUD_REMAND_REASONS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REMAND_REASONS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REMAND_REASONS
create sequence XHB_REMAND_REASONS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_remand_reasons_bir_tr.sql;
@xhb_remand_reasons_bur_tr.sql;

commit;