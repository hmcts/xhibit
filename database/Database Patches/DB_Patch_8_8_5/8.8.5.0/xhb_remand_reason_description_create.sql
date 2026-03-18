/*    ------------------------------------------------------------------
*     CREATE XHB_REMAND_REASON_DESCRIPTION TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

drop table XHB_REMAND_REASON_DESCRIPTION;
create table XHB_REMAND_REASON_DESCRIPTION (
	REMAND_REASON_DESCRIPTION_ID NUMBER(8) NOT NULL,
	REASON_CATEGORY VARCHAR2(100) NOT NULL,
	REASON_DESCRIPTION VARCHAR2(500) NOT NULL,
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
alter table XHB_REMAND_REASON_DESCRIPTION
  add constraint XHB_REMAND_REASON_DESC_PK primary key (REMAND_REASON_DESCRIPTION_ID) 
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
grant select, insert, update on XHB_REMAND_REASON_DESCRIPTION to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_REMAND_REASON_DESCRIPTION
*/	------------------------------------------------------------------
drop table AUD_REMAND_REASON_DESCRIPTION;
create table AUD_REMAND_REASON_DESCRIPTION
(
	REMAND_REASON_DESCRIPTION_ID NUMBER(8) NOT NULL,
	REASON_CATEGORY VARCHAR2(100) NOT NULL,
	REASON_DESCRIPTION VARCHAR2(500) NOT NULL,
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
grant select, insert, update on AUD_REMAND_REASON_DESCRIPTION to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REMAND_REASON_DESCRIPTION';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REMAND_REASON_DESCRIPTION', 'AUD_REMAND_REASON_DESCRIPTION', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REMAND_REASON_DESCRIPTION
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REMAND_REASON_DESCRIPTION
create sequence XHB_REMAND_REASON_DESC_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_remand_reason_desc_bir_tr.sql;
@xhb_remand_reason_desc_bur_tr.sql;

commit;