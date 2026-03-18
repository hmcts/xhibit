/*Ctx-1906*/ 
CREATE TABLE xhb_ref_event_description
(REF_EVENT_DESCRIPTION_ID NUMBER(8) NOT NULL
,EXTERNAL_EVENT_CODE NUMBER(8) NOT NULL 
,EVENT_DESCRIPTION VARCHAR2(240) NOT NULL 
,EVENT_SUB_DESCRIPTION VARCHAR2(240)  
,SEND_TO_MIS VARCHAR2(1) DEFAULT 'Y'
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
COMMENT ON COLUMN xhb_ref_event_description.REF_EVENT_DESCRIPTION_ID IS 'PK. cannot be null';
COMMENT ON COLUMN xhb_ref_event_description.EXTERNAL_EVENT_CODE IS 'The entry code for the cracked / ineffective entry.  Cannot be null';
COMMENT ON COLUMN xhb_ref_event_description.event_description IS 'High level description of the event.  Cannot be null';
COMMENT ON COLUMN xhb_ref_event_description.event_sub_description IS 'Optional additional information description of the event.  Cannot be null';
COMMENT ON COLUMN xhb_ref_event_description.send_to_mis IS 'Default to Y. Can be null';
COMMENT ON COLUMN xhb_ref_event_description.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN xhb_ref_event_description.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN xhb_ref_event_description.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN xhb_ref_event_description.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN xhb_ref_event_description.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN xhb_ref_event_description.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 

alter table xhb_ref_event_description
  add constraint ref_event_description primary key (REF_EVENT_DESCRIPTION_ID) 
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
grant select, insert, update on xhb_ref_event_description to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_REF_EVENT_DESCRIPTION_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE AUD_REF_EVENT_DESCRIPTION
(REF_EVENT_DESCRIPTION_ID NUMBER(8) NOT NULL
,EXTERNAL_EVENT_CODE NUMBER(8) NOT NULL 
,EVENT_DESCRIPTION VARCHAR2(240) NOT NULL 
,EVENT_SUB_DESCRIPTION VARCHAR2(240)  
,SEND_TO_MIS VARCHAR2(1) DEFAULT 'Y'
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
COMMENT ON COLUMN aud_ref_event_description.REF_EVENT_DESCRIPTION_ID IS 'PK. cannot be null';
COMMENT ON COLUMN aud_ref_event_description.EXTERNAL_EVENT_CODE IS 'The entry code for the cracked / ineffective entry.  Cannot be null';
COMMENT ON COLUMN aud_ref_event_description.event_description IS 'High level description of the event.  Cannot be null';
COMMENT ON COLUMN aud_ref_event_description.event_sub_description IS 'Optional additional information description of the event.  Cannot be null';
COMMENT ON COLUMN aud_ref_event_description.send_to_mis IS 'Default to Y. Can be null';
COMMENT ON COLUMN aud_ref_event_description.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN aud_ref_event_description.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN aud_ref_event_description.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN aud_ref_event_description.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN aud_ref_event_description.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN aud_ref_event_description.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on aud_ref_event_description to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING 
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_EVENT_DESCRIPTION';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_EVENT_DESCRIPTION', 'AUD_REF_EVENT_DESCRIPTION', 'Y');

@@xhb_ref_event_desc_bur_tr;
@@xhb_ref_event_desc_bir_tr;

commit;
 
