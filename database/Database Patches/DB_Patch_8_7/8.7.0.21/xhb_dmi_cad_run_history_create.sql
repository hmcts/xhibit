/*ctx-1539*/
CREATE TABLE XHB_DMI_CAD_RUN_HISTORY
(DMI_CAD_RUN_HISTORY_ID NUMBER NOT NULL
,CREATION_DATE DATE 
,CREATED_BY VARCHAR2(20 BYTE)
,START_RUN_TIME DATE 
,END_RUN_TIME DATE
,DMI_CAD_REF_CODE_ID NUMBER
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
COMMENT ON COLUMN XHB_DMI_CAD_RUN_HISTORY.DMI_CAD_RUN_HISTORY_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_HISTORY.CREATION_DATE IS 'Creation date of the record';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_HISTORY.CREATED_BY IS 'Creator the record';
COMMENT ON COLUMN XHB_DMI_CAD_RUN_HISTORY.DMI_CAD_REF_CODE_ID IS 'Foreign key reference to xbh_dmi_cad_ref_code.dmi_cad_ref_code_id';

  
-- Create/Recreate primary, unique and foreign key constraints 

ALTER TABLE XHB_DMI_CAD_RUN_HISTORY ADD (CONSTRAINT DMI_CAD_REF_CODE_ID_FK FOREIGN KEY (DMI_CAD_REF_CODE_ID) REFERENCES xhb_dmi_cad_ref_code (DMI_CAD_REF_CODE_ID));

alter table XHB_DMI_CAD_RUN_HISTORY
  add constraint XHB_DMI_CAD_RUN_HISTORY_PK primary key (DMI_CAD_RUN_HISTORY_ID) 
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
grant select, insert, update on XHB_DMI_CAD_RUN_HISTORY to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_HISTORY
create sequence XHB_DMI_CAD_RUN_HISTORY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


commit;
  
  
 
