/*Ctx-1913*/
CREATE TABLE XHB_PUB_RUNNING_LIST
(PUB_RUNNING_LIST_ID NUMBER(8) NOT NULL
,COURT_ID NUMBER(8) NOT NULL 
,PUBLISHED_DATE DATE
,OBS_IND VARCHAR2(1)
,LAST_UPDATE_DATE DATE NOT NULL 
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATED_BY VARCHAR2(30) NOT NULL 
,VERSION NUMBER(5)DEFAULT 1 NOT NULL 
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
  
--Field Comments
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.PUB_RUNNING_LIST_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.COURT_ID IS 'The court this entry is linked to. cannot be null';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.PUBLISHED_DATE IS 'The date the running list was published';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN XHB_PUB_RUNNING_LIST.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_PUB_RUNNING_LIST ADD (CONSTRAINT XHB_PUB_RUN_LIST_court_id_fk FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));

alter table XHB_PUB_RUNNING_LIST
  add constraint XHB_PUB_RUNNING_LIST_PK primary key (PUB_RUNNING_LIST_ID) 
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
grant select, insert, update on XHB_PUB_RUNNING_LIST to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_PUB_RUNNING_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE AUD_PUB_RUNNING_LIST
(PUB_RUNNING_LIST_ID NUMBER(8) NOT NULL
,COURT_ID NUMBER(8) NOT NULL 
,PUBLISHED_DATE DATE
,OBS_IND VARCHAR2(1)
,LAST_UPDATE_DATE DATE NOT NULL 
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATED_BY VARCHAR2(30) NOT NULL 
,VERSION NUMBER(5)DEFAULT 1 NOT NULL 
,INSERT_EVENT VARCHAR2(1)
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
  
--Field Comments
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.PUB_RUNNING_LIST_ID IS 'PK. cannot be null';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.COURT_ID IS 'The court this entry is linked to. cannot be null';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.PUBLISHED_DATE IS 'The date the running list was published';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN AUD_PUB_RUNNING_LIST.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on AUD_PUB_RUNNING_LIST to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'AUD_PUB_RUNNING_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_PUB_RUNNING_LIST', 'AUD_PUB_RUNNING_LIST', 'Y');

@@xhb_pub_running_list_bur_tr;
@@xhb_pub_running_list_bir_tr;

commit;
 
