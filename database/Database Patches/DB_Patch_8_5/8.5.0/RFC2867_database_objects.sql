/*
*   --------------------------------------------------------------------------------------
*   Script created by BH on 30/03/2011, for RFC2867
*   The aim of the script is to create the new Xhibit table, that will hold the
*   expanded Indictment Log
*   --------------------------------------------------------------------------------------
*/

/*

/*    ------------------------------------------------------------------
*     CREATE XHB_INDICTMENT_LOG TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_INDICTMENT_LOG
(
  INDICTMENT_LOG_ID          NUMBER not null,
  CASE_ID        NUMBER not null,
  SEQUENCE_NO NUMBER not null,
  INDICTMENT_INFO 	  VARCHAR(78) not null,
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
alter table XHB_INDICTMENT_LOG
  add constraint INDICTMENT_LOGY_PK primary key (INDICTMENT_LOG_ID)
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

ALTER TABLE XHB_INDICTMENT_LOG ADD (CONSTRAINT XHB_INDICTMENT_LOG_CASE_ID_FK FOREIGN KEY (CASE_ID) REFERENCES XHB_CASE (CASE_ID));

-- Create index on case_id
CREATE INDEX XHB_INDICTMENT_LOG_CASE_FK ON XHB_INDICTMENT_LOG (CASE_ID ASC)
	TABLESPACE XHIBITD
	STORAGE (INITIAL 1M
		 NEXT 1M
		 PCTINCREASE 0); 

-- Grant/Revoke object privileges 
grant select, insert, update on XHB_INDICTMENT_LOG to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR INDICTMENT_LOG
*/	------------------------------------------------------------------

create table AUD_INDICTMENT_LOG (
  INDICTMENT_LOG_ID          NUMBER not null,
  CASE_ID        NUMBER not null,
  SEQUENCE_NO NUMBER not null,
  INDICTMENT_INFO 	  VARCHAR(78) not null,
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
grant select, insert, update on AUD_INDICTMENT_LOG to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_INDICTMENT_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_INDICTMENT_LOG', 'AUD_INDICTMENT_LOG', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR INDICTMENT_LOG
*/	------------------------------------------------------------------

-- Create sequence table for XHB_INDICTMENT_LOG
create sequence XHB_INDICTMENT_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_indictmentlog_bir_tr.sql
@xhb_indictmentlog_bur_tr.sql

@xhb_housekeeping_pkg_b.sql


commit;





