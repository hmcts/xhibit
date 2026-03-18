/*Ctx-1906*/ 
CREATE TABLE xhb_mis_event_log
(MIS_EVENT_LOG_ID NUMBER(8) NOT NULL
,REF_EVENT_DESCRIPTION_ID NUMBER(8) NOT NULL 
,EVENT_TIME DATE
,COURT_ID NUMBER(8) NOT NULL
,CASE_ID NUMBER(8) NOT NULL
,DEFENDANT_ON_CASE_ID NUMBER(8)
,EVENT_TEXT VARCHAR2(240)
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
COMMENT ON COLUMN xhb_mis_event_log.MIS_EVENT_LOG_ID IS 'PK. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.REF_EVENT_DESCRIPTION_ID IS 'FK to xhb_ref_event_description. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.EVENT_TIME IS 'Record time of event. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.CASE_ID IS 'FK to XHB_CASE. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.COURT_ID IS 'FK to XHB_COURT. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.DEFENDANT_ON_CASE_ID IS 'FK to XHB_DEFENDANT_ON_CASE. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.EVENT_TEXT IS 'Record sub option of the event. cannot be null';
COMMENT ON COLUMN xhb_mis_event_log.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN xhb_mis_event_log.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN xhb_mis_event_log.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN xhb_mis_event_log.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN xhb_mis_event_log.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN xhb_mis_event_log.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 

alter table xhb_mis_event_log
  add constraint xhb_mis_event_log primary key (MIS_EVENT_LOG_ID) 
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

ALTER TABLE xhb_mis_event_log ADD (CONSTRAINT xhb_mis_event_log_event_id FOREIGN KEY (REF_EVENT_DESCRIPTION_ID) REFERENCES xhb_REF_EVENT_DESCRIPTION (REF_EVENT_DESCRIPTION_ID));
ALTER TABLE xhb_mis_event_log ADD (CONSTRAINT xhb_mis_event_log_case_id FOREIGN KEY (case_id) REFERENCES xhb_case (case_id));
ALTER TABLE xhb_mis_event_log ADD (CONSTRAINT xhb_mis_event_log_court_id FOREIGN KEY (court_id) REFERENCES xhb_court (court_id));
ALTER TABLE xhb_mis_event_log ADD (CONSTRAINT xhb_mis_event_log_doc_id FOREIGN KEY (defendant_on_case_id) REFERENCES xhb_defendant_on_case (defendant_on_case_id));

-- Grant/Revoke object privileges 
grant select, insert, update on xhb_mis_event_log to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_MIS_EVENT_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE aud_mis_event_log
(MIS_EVENT_LOG_ID NUMBER(8) NOT NULL
,REF_EVENT_DESCRIPTION_ID NUMBER(8) NOT NULL 
,EVENT_TIME DATE
,COURT_ID NUMBER(8) NOT NULL
,CASE_ID NUMBER(8) NOT NULL
,DEFENDANT_ON_CASE_ID NUMBER(8)
,EVENT_TEXT VARCHAR2(240)
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
COMMENT ON COLUMN aud_mis_event_log.MIS_EVENT_LOG_ID IS 'PK. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.REF_EVENT_DESCRIPTION_ID IS 'FK to xhb_ref_event_description. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.EVENT_TIME IS 'Record time of event. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.CASE_ID IS 'FK to XHB_CASE. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.COURT_ID IS 'FK to XHB_COURT. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.DEFENDANT_ON_CASE_ID IS 'FK to XHB_DEFENDANT_ON_CASE. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.EVENT_TEXT IS 'Record sub option of the event. cannot be null';
COMMENT ON COLUMN aud_mis_event_log.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN aud_mis_event_log.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN aud_mis_event_log.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN aud_mis_event_log.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN aud_mis_event_log.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN aud_mis_event_log.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on aud_mis_event_log to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING 
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_MIS_EVENT_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_MIS_EVENT_LOG', 'AUD_MIS_EVENT_LOG', 'Y');

@@xhb_mis_event_log_bur_tr;
@@xhb_mis_event_log_bir_tr;

commit;
 
