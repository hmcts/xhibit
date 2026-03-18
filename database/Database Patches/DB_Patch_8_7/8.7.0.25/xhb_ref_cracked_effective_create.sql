/*Ctx-1906*/ 
CREATE TABLE xhb_ref_cracked_effective
(REF_CRACKED_EFFECTIVE_ID NUMBER(8) NOT NULL
,CODE VARCHAR2(2) NOT NULL 
,DESCRIPTION VARCHAR2(150)
,PARTY_RESPONSIBLE VARCHAR2(1)  
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
COMMENT ON COLUMN xhb_ref_cracked_effective.REF_CRACKED_EFFECTIVE_ID IS 'PK. cannot be null';
COMMENT ON COLUMN xhb_ref_cracked_effective.CODE IS 'The entry code for the cracked / ineffective entry.  Cannot be null';
COMMENT ON COLUMN xhb_ref_cracked_effective.DESCRIPTION IS 'The description of the cracked / ineffective entry.';
COMMENT ON COLUMN xhb_ref_cracked_effective.PARTY_RESPONSIBLE IS 'The party responsible for the cracked codes.';
COMMENT ON COLUMN xhb_ref_cracked_effective.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN xhb_ref_cracked_effective.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN xhb_ref_cracked_effective.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN xhb_ref_cracked_effective.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN xhb_ref_cracked_effective.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN xhb_ref_cracked_effective.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 

alter table xhb_ref_cracked_effective
  add constraint ref_cracked_effective_pk primary key (REF_CRACKED_EFFECTIVE_ID) 
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
grant select, insert, update on xhb_ref_cracked_effective to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_REF_CRACKED_EFFECTIVE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE AUD_REF_CRACKED_EFFECTIVE
(REF_CRACKED_EFFECTIVE_ID NUMBER(8) NOT NULL
,CODE VARCHAR2(2) NOT NULL 
,DESCRIPTION VARCHAR2(150)
,PARTY_RESPONSIBLE VARCHAR2(1)  
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
COMMENT ON COLUMN aud_ref_cracked_effective.REF_CRACKED_EFFECTIVE_ID IS 'PK. cannot be null';
COMMENT ON COLUMN aud_ref_cracked_effective.CODE IS 'The entry code for the cracked / ineffective entry.  Cannot be null';
COMMENT ON COLUMN aud_ref_cracked_effective.DESCRIPTION IS 'The description of the cracked / ineffective entry.';
COMMENT ON COLUMN aud_ref_cracked_effective.PARTY_RESPONSIBLE IS 'The party responsible for the cracked codes.';
COMMENT ON COLUMN aud_ref_cracked_effective.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN aud_ref_cracked_effective.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN aud_ref_cracked_effective.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN aud_ref_cracked_effective.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN aud_ref_cracked_effective.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN aud_ref_cracked_effective.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on aud_ref_cracked_effective to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REPORT_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_CRACKED_EFFECTIVE', 'AUD_REF_CRACKED_EFFECTIVE', 'Y');

@@xhb_ref_cracked_effect_bur_tr;
@@xhb_ref_cracked_effect_bir_tr;

commit;
 
