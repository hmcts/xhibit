/*ctx-1539*/
CREATE TABLE XHB_DMI_CAD_RUN_LOG
(DMI_CAD_RUN_LOG_ID NUMBER NOT NULL
,DMI_CAD_RUN_HISTORY_ID NUMBER NOT NULL
,DMI_CAD_REF_CODE_ID NUMBER NOT NULL
,COURT_ID NUMBER 
,MODULE_NAME VARCHAR2(100)
,LOG_MESSAGE VARCHAR2(250)
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
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.DMI_CAD_RUN_LOG_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.DMI_CAD_RUN_HISTORY_ID IS 'Foreign key reference to XHB_DMI_CAD_RUN_HISTORY.c to establish master-detail relationship';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.DMI_CAD_REF_CODE_ID IS 'Foreign key relationship to xhb_dmi_cad_ref_code.xhb_dmi_ref_code_id';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.COURT_ID IS 'Foreign key relationship to xhb_court.court id.';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.MODULE_NAME IS 'Name of the PLSQL block that the code is currently processing';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_LOG.LOG_MESSAGE IS 'Log message';

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_DMI_CAD_RUN_LOG ADD (CONSTRAINT DMI_CAD_RUN_LOG_HISTORY_ID_FK FOREIGN KEY (DMI_CAD_RUN_HISTORY_ID) REFERENCES XHB_DMI_CAD_RUN_HISTORY (DMI_CAD_RUN_HISTORY_ID));
ALTER TABLE XHB_DMI_CAD_RUN_LOG ADD (CONSTRAINT DMI_CAD_RUN_LOG_REF_CODE_ID_FK FOREIGN KEY (DMI_CAD_REF_CODE_ID) REFERENCES xhb_dmi_cad_ref_code (DMI_CAD_REF_CODE_ID));
ALTER TABLE XHB_DMI_CAD_RUN_LOG ADD (CONSTRAINT DMI_CAD_RUN_LOG_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES xhb_court (COURT_ID));
  
alter table XHB_DMI_CAD_RUN_LOG
  add constraint XHB_DMI_CAD_RUN_LOG_PK primary key (DMI_CAD_RUN_LOG_ID) 
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
grant select, insert, update on XHB_DMI_CAD_RUN_LOG to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_DMI_CAD_RUN_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


commit;
  
  
 
