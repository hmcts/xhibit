/*Ctx-1914*/
CREATE TABLE XHB_CASE_GROUP_SEQ_GENERATOR
(CASE_GROUP_SEQ_GENERATOR_ID NUMBER(8) NOT NULL
,COURT_ID NUMBER(8) NOT NULL 
,CURRENT_SEQUENCE NUMBER(7) NOT NULL
,CREATED_BY VARCHAR2(30) NOT NULL
,LAST_UPDATE_BY VARCHAR2(30) NOT NULL
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATE_DATE DATE NOT NULL 
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
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.CASE_GROUP_SEQ_GENERATOR_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.COURT_ID IS 'The court ID of the crown court';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.CURRENT_SEQUENCE IS 'The current sequence number.  It will start the number after the highest group number in that court at cutover';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.CURRENT_SEQUENCE IS 'The current sequence number.  It will start the number after the highest group number in that court at cutover';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.LAST_UPDATE_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN XHB_CASE_GROUP_SEQ_GENERATOR.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_CASE_GROUP_SEQ_GENERATOR ADD (CONSTRAINT XHB_CASE_GRP_SEQ_GEN_CRT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES xhb_court (COURT_ID));

alter table XHB_CASE_GROUP_SEQ_GENERATOR
  add constraint XHB_CASE_GROUP_SEQ_GENERATOR primary key (CASE_GROUP_SEQ_GENERATOR_ID) 
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
grant select, insert, update on XHB_CASE_GROUP_SEQ_GENERATOR to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_CASE_GRP_SEQ_GENERATOR_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
--No audit table required

@@xhb_case_grp_seq_gentr_bir_tr;

commit;
 
