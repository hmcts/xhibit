/*    ------------------------------------------------------------------
*     CREATE XHB_LIST
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_LIST(
	LIST_ID NUMBER(8) NOT NULL,
	LIST_TYPE_ID NUMBER(8) NOT NULL,
	LIST_PARENT_ID NUMBER(8),
	COURT_ID NUMBER(8) NOT NULL,
	DRAFT_OR_FINAL VARCHAR2(1) NOT NULL,
	LIST_NUMBER NUMBER(3) NOT NULL,
	LIST_START_DATE DATE NOT NULL,
	LIST_END_DATE DATE NOT NULL,
	PUBLISH_DATE DATE,
	PUBLISH_STATUS VARCHAR2(8),
	PUBLISH_ERROR_REASON VARCHAR2(200),
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1 NOT NULL
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
alter table XHB_LIST
  add constraint XHB_LIST_PK primary key (LIST_ID) 
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
    
-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_LIST ADD (CONSTRAINT XHB_LIST_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT(COURT_ID));
ALTER TABLE XHB_LIST ADD (CONSTRAINT XHB_LIST_PARENT_FK FOREIGN KEY (LIST_PARENT_ID) REFERENCES XHB_LIST(LIST_ID));
ALTER TABLE XHB_LIST ADD (CONSTRAINT XHB_LIST_LIST_TYPE_ID_FK FOREIGN KEY (LIST_TYPE_ID) REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID));

   alter table XHB_LIST
ADD CONSTRAINT DRAFT_OR_FINAL_CHK CHECK (DRAFT_OR_FINAL IN ('D','F'));

alter table XHB_LIST
ADD CONSTRAINT PUBLISH_STATUS_CHK CHECK (PUBLISH_STATUS IN ('SUCCESS','FAILURE'));
  
 -- Grant/Revoke object privileges 
grant select, insert, update on XHB_LIST to PUBLIC;

/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR LIST
*/	------------------------------------------------------------------

create table AUD_LIST
(
	LIST_ID NUMBER(8) NOT NULL,
	LIST_TYPE_ID NUMBER(8) NOT NULL,
	LIST_PARENT_ID NUMBER(8),
	COURT_ID NUMBER(8) NOT NULL,
	DRAFT_OR_FINAL VARCHAR2(1) NOT NULL,
	LIST_NUMBER NUMBER(3) NOT NULL,
	LIST_START_DATE DATE NOT NULL,
	LIST_END_DATE DATE NOT NULL,
	PUBLISH_DATE DATE,
	PUBLISH_STATUS VARCHAR2(8),
	PUBLISH_ERROR_REASON VARCHAR2(200),
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1 NOT NULL,
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
grant select, insert, update on AUD_LIST to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_LIST', 'AUD_LIST', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_LIST
*/	------------------------------------------------------------------

-- Create sequence table for XHB_LIST
create sequence XHB_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_list_bir_tr.sql;
@@xhb_list_bur_tr.sql;

commit;

