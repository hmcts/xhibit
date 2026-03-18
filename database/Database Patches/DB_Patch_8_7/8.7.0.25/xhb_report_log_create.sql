/*Ctx-1913*/
CREATE TABLE XHB_REPORT_LOG
(REPORT_LOG_ID NUMBER(8) NOT NULL
,COURT_ID NUMBER(8) NOT NULL 
,REPORT_NAME VARCHAR2(80)
,CREST_REPORT_CODE VARCHAR2(10)  
,DATE_LAST_RUN DATE
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
COMMENT ON COLUMN XHB_REPORT_LOG.REPORT_LOG_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_REPORT_LOG.COURT_ID IS 'The court this entry is linked to. cannot be null';
COMMENT ON COLUMN XHB_REPORT_LOG.REPORT_NAME IS 'The descriptive name of the report';
COMMENT ON COLUMN XHB_REPORT_LOG.DATE_LAST_RUN IS 'The date that the report was last run';
COMMENT ON COLUMN XHB_REPORT_LOG.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN XHB_REPORT_LOG.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN XHB_REPORT_LOG.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN XHB_REPORT_LOG.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN XHB_REPORT_LOG.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN XHB_REPORT_LOG.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_REPORT_LOG ADD (CONSTRAINT XHB_REPORT_LOG_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));

alter table XHB_REPORT_LOG
  add constraint XHB_REPORT_LOG_PK primary key (REPORT_LOG_ID) 
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
grant select, insert, update on XHB_REPORT_LOG to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_REPORT_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE AUD_REPORT_LOG
(REPORT_LOG_ID NUMBER(8) NOT NULL
,COURT_ID NUMBER(8) NOT NULL 
,REPORT_NAME VARCHAR2(80)
,CREST_REPORT_CODE VARCHAR2(10)  
,DATE_LAST_RUN DATE
,OBS_IND VARCHAR2(1)
,LAST_UPDATE_DATE DATE NOT NULL 
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATED_BY VARCHAR2(30) NOT NULL 
,VERSION NUMBER(5)DEFAULT 1 NOT NULL
,insert_event VARCHAR2(1)
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
COMMENT ON COLUMN AUD_REPORT_LOG.REPORT_LOG_ID IS 'PK. cannot be null';
COMMENT ON COLUMN AUD_REPORT_LOG.COURT_ID IS 'The court this entry is linked to. cannot be null';
COMMENT ON COLUMN AUD_REPORT_LOG.REPORT_NAME IS 'The descriptive name of the report';
COMMENT ON COLUMN AUD_REPORT_LOG.DATE_LAST_RUN IS 'The date that the report was last run';
COMMENT ON COLUMN AUD_REPORT_LOG.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN AUD_REPORT_LOG.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN AUD_REPORT_LOG.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN AUD_REPORT_LOG.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN AUD_REPORT_LOG.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN AUD_REPORT_LOG.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on AUD_REPORT_LOG to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REPORT_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REPORT_LOG', 'AUD_REPORT_LOG', 'Y');

@@xhb_report_log_bur_tr;
@@xhb_report_log_bir_tr;

commit;
 
