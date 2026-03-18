/*    ------------------------------------------------------------------
*     CREATE XHB_REFUSED_BROADCAST_CASE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REFUSED_BROADCAST_CASE (
	REFUSED_BROADCAST_CASE_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	TELE_APP_REFUSED_REASON_ID NUMBER(8),
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1
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
ALTER TABLE XHB_REFUSED_BROADCAST_CASE ADD (CONSTRAINT XHB_REF_BROAD_CASE_FK FOREIGN KEY (CASE_ID) REFERENCES XHB_CASE(CASE_ID));
ALTER TABLE XHB_REFUSED_BROADCAST_CASE ADD (CONSTRAINT XHB_REF_BROAD_REF_SYSTEM_FK FOREIGN KEY (TELE_APP_REFUSED_REASON_ID) REFERENCES XHB_REF_SYSTEM_CODE(REF_SYSTEM_CODE_ID));

-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_REFUSED_BROADCAST_CASE
  add constraint XHB_REFUSED_BROADCAST_CASE_PK primary key (REFUSED_BROADCAST_CASE_ID) 
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
grant select, insert, update on XHB_REFUSED_BROADCAST_CASE to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_REFUSED_BROADCAST_CASE
*/	------------------------------------------------------------------

create table AUD_REFUSED_BROADCAST_CASE(
	REFUSED_BROADCAST_CASE_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	TELE_APP_REFUSED_REASON_ID NUMBER(8),
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1,
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
grant select, insert, update on AUD_REFUSED_BROADCAST_CASE to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REFUSED_BROADCAST_CASE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REFUSED_BROADCAST_CASE', 'AUD_REFUSED_BROADCAST_CASE', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REFUSED_BROADCAST_CASE
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REFUSED_BROADCAST_CASE
create sequence XHB_REFUSED_BROADCAST_CASE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_ref_broadcast_case_bir_tr.sql;
@@xhb_ref_broadcast_case_bur_tr.sql;

commit;
