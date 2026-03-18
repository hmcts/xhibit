/*    ------------------------------------------------------------------
*     CREATE XHB_REF_APP_RES_D20_MAP TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

drop table XHB_REF_APP_RES_D20_MAP;
create table XHB_REF_APP_RES_D20_MAP (
	REF_APP_RES_D20_MAP_ID NUMBER(8) NOT NULL,
	APP_RESULT_CODE VARCHAR2(8) NOT NULL,
	D20_RESULT VARCHAR2(12) NOT NULL,
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
alter table XHB_REF_APP_RES_D20_MAP
  add constraint XHB_REF_APP_RES_D20_MAP_PK primary key (REF_APP_RES_D20_MAP_ID) 
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
grant select, insert, update on XHB_REF_APP_RES_D20_MAP to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_REF_APP_RES_D20_MAP
*/	------------------------------------------------------------------
drop table AUD_REF_APP_RES_D20_MAP;
create table AUD_REF_APP_RES_D20_MAP
(
	REF_APP_RES_D20_MAP_ID NUMBER(8) NOT NULL,
	APP_RESULT_CODE VARCHAR2(8) NOT NULL,
	D20_RESULT VARCHAR2(12) NOT NULL,
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
grant select, insert, update on AUD_REF_APP_RES_D20_MAP to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_APP_RES_D20_MAP';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_APP_RES_D20_MAP', 'AUD_REF_APP_RES_D20_MAP', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_APP_RES_D20_MAP
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_APP_RES_D20_MAP
create sequence XHB_REF_APP_RES_D20_MAP_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@XHB_REF_APP_RES_D20_MAP_bir_tr.sql;
@XHB_REF_APP_RES_D20_MAP_bur_tr.sql;

commit;