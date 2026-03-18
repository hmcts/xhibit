/*
*   --------------------------------------------------------------------------------------
*   Script created by SA on 23/09/2015, for NLE Enhancements Data Reset
*   The aim of the script is to create the new Xhibit tables (for NLE only) that will handle
*   the management of cases where data is being reset.
*   --------------------------------------------------------------------------------------
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_NLE_MANAGED_CASES TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_NLE_MANAGED_CASES
(
  MANAGED_CASE_ID          	NUMBER not null,
  CASE_ID        		NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1
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
alter table XHB_NLE_MANAGED_CASES
  add constraint NLE_MANAGED_CASE_PK primary key (MANAGED_CASE_ID)
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

ALTER TABLE XHB_NLE_MANAGED_CASES ADD (CONSTRAINT XHB_NLE_MANAGED_CASE_ID_FK FOREIGN KEY (CASE_ID) REFERENCES XHB_CASE (CASE_ID));



-- Create index on defendant_on_case_id
CREATE INDEX XHB_NLE_MANAGED_CASE_FK ON XHB_NLE_MANAGED_CASES (CASE_ID ASC)
	TABLESPACE XHIBITD
	STORAGE (INITIAL 1M
		 NEXT 1M
		 PCTINCREASE 0); 

-- Grant/Revoke object privileges 
grant select, insert, update on XHB_NLE_MANAGED_CASES to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

create table AUD_NLE_MANAGED_CASES (
  MANAGED_CASE_ID		NUMBER not null,
  CASE_ID        		NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1,
  insert_event            VARCHAR2(1)
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
grant select, insert, update on AUD_NLE_MANAGED_CASES to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_NLE_MANAGED_CASES';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_NLE_MANAGED_CASES', 'AUD_NLE_MANAGED_CASES', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_NLE_MANAGED_CASES
*/	------------------------------------------------------------------

-- Create sequence table for XHB_NLE_MANAGED_CASES
create sequence XHB_NLE_MANAGED_CASES_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_nle_managed_cases_bir_tr.sql;
@@xhb_nle_managed_cases_bur_tr.sql;

commit;




