/*
*   -------------------------------------------------------------------------------------------
*   Script created by SA on 04/12/2015, for storing the D20 offence codes used in the D20 order
*   -------------------------------------------------------------------------------------------
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_D20_OFFENCE_CODES TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_D20_OFFENCE_CODES
(
  OFFENCE_CODE_ID     	  NUMBER not null,
  OFFENCE_CODE        	  VARCHAR2(4) not null,
  REASON_TYPE             VARCHAR2(50),
  REASON              	  VARCHAR2(250),
  PENALTY_POINTS          VARCHAR2(50),
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
alter table XHB_D20_OFFENCE_CODES
  add constraint D20_OFFENCE_CODES_PK primary key (OFFENCE_CODE_ID)
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
grant select, insert, update on XHB_D20_OFFENCE_CODES to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

create table AUD_D20_OFFENCE_CODES
(
  OFFENCE_CODE_ID     	  NUMBER not null,
  OFFENCE_CODE        	  VARCHAR2(4) not null,
  REASON_TYPE             VARCHAR2(50),
  REASON              	  VARCHAR2(250),
  PENALTY_POINTS          VARCHAR2(50),
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
grant select, insert, update on AUD_D20_OFFENCE_CODES to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_D20_OFFENCE_CODES';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_D20_OFFENCE_CODES', 'AUD_D20_OFFENCE_CODES', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_D20_OFFENCE_CODES
*/	------------------------------------------------------------------

-- Create sequence table for XHB_D20_OFFENCE_CODES
create sequence XHB_D20_OFFENCE_CODES_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_D20_OFFENCE_CODES_bir_tr.sql;
@@XHB_D20_OFFENCE_CODES_bur_tr.sql;

commit;
