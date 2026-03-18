/*ctx-1539*/
CREATE TABLE XHB_DMI_CAD_REF_CODE
(DMI_CAD_REF_CODE_ID NUMBER NOT NULL
,DMI_CAD_REF_CODE_DESCRIPTION VARCHAR2(50 BYTE) NOT NULL
,CREATION_DATE DATE 
,CREATED_BY VARCHAR2(20 BYTE)
,OBS_IND VARCHAR2(1 BYTE) DEFAULT 'N' 
,DMI_CAD_REF_CODE_SHORT_NAME VARCHAR2(2 BYTE) NOT NULL
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
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.DMI_CAD_REF_CODE_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.DMI_CAD_REF_CODE_DESCRIPTION IS 'Description of the PK entry. cannot be null';
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.DMI_CAD_REF_CODE_SHORT_NAME IS 'Short name of the PK entry. cannot be null';
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.CREATION_DATE IS 'Creation date of the record';
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.CREATED_BY IS 'Creator the record';
COMMENT ON COLUMN XHB_DMI_CAD_REF_CODE.OBS_IND IS 'Obsolete indicator of the row.  Default to N';
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_DMI_CAD_REF_CODE
  add constraint XHB_DMI_CAD_REF_CODE_PK primary key (DMI_CAD_REF_CODE_ID) 
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
grant select, insert, update on XHB_DMI_CAD_REF_CODE to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_REF_CODE
create sequence XHB_DMI_CAD_REF_CODE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


commit;
  
  
 
