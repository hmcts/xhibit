/*
*   -------------------------------------------------------------------------------------------
*   Script created by SA on 24/12/2015, for storing the disposals applicable to Monetary Orders
*   -------------------------------------------------------------------------------------------
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_REF_MON_ORD_DISPOSALS TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_MON_ORD_DISPOSALS
(
  MO_DIS_ID     	  NUMBER not null,
  DISPOSAL_CODE        	  VARCHAR2(7) not null,
  MO_TYPE             	  VARCHAR2(50),
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
alter table XHB_REF_MON_ORD_DISPOSALS
  add constraint REF_MON_ORD_DISPOSALS_PK primary key (MO_DIS_ID)
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
grant select, insert, update on XHB_REF_MON_ORD_DISPOSALS to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

create table AUD_REF_MON_ORD_DISPOSALS
(
  MO_DIS_ID     	  NUMBER not null,
  DISPOSAL_CODE        	  VARCHAR2(7) not null,
  MO_TYPE             	  VARCHAR2(50),
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
grant select, insert, update on AUD_REF_MON_ORD_DISPOSALS to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_MON_ORD_DISPOSALS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_MON_ORD_DISPOSALS', 'AUD_REF_MON_ORD_DISPOSALS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_MON_ORD_DISPOSALS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_MON_ORD_DISPOSALS
create sequence XHB_REF_MON_ORD_DISPOSALS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_REF_MON_ORD_DISP_bir_tr.sql;
@@XHB_REF_MON_ORD_DISP_bur_tr.sql;

commit;
