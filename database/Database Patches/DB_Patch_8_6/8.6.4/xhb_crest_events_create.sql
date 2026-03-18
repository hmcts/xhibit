/*
*   -------------------------------------------------------------------------------------------
*   Script created by BH on 27/01/2016, for storing the event details for court log events which need to be written back to CREST
*   -------------------------------------------------------------------------------------------
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_CREST_EVENTS TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CREST_EVENTS
(
  CREST_EVENT_ID     	  NUMBER not null,
  EVENT_TYPE        	  NUMBER not null,
  CREST_VALUE             VARCHAR2(120) not Null,
  CREST_COURT_ID       	  NUMBER not null,
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
alter table XHB_CREST_EVENTS
  add constraint CREST_EVENTS_PK primary key (CREST_EVENT_ID)
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
grant select, insert, update on XHB_CREST_EVENTS to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR CREST_EVENTS
*/	------------------------------------------------------------------

create table AUD_CREST_EVENTS
(
  CREST_EVENT_ID     	  NUMBER not null,
  EVENT_TYPE        	  NUMBER not null,
  CREST_VALUE             VARCHAR2(120) not Null,
  CREST_COURT_ID       	  NUMBER not null,
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
grant select, insert, update on AUD_CREST_EVENTS to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CREST_EVENTS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CREST_EVENTS', 'AUD_CREST_EVENTS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CREST_EVENTS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CREST_EVENTS
create sequence XHB_CREST_EVENTS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_CREST_EVENTS_bir_tr.sql;
@@XHB_CREST_EVENTS_bur_tr.sql;

commit;
