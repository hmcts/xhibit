/*    ------------------------------------------------------------------
*     XHB_DMI_CAD_ERROR_LOG
*     
*/    ------------------------------------------------------------------

create table XHB_DMI_CAD_ERROR_LOG 
   (DMI_CAD_RUN_ID NUMBER NOT NULL ENABLE 
	,ERROR_MESSAGE VARCHAR2(500 BYTE)
	,RUN_DATE DATE
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
alter table XHB_DMI_CAD_ERROR_LOG
  add constraint XHB_DMI_CAD_ERROR_LOG_PK primary key (DMI_CAD_RUN_ID) 
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
grant select, insert, update on XHB_DMI_CAD_ERROR_LOG to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_EMAIL_RECIPIENTS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_LEGAL_AID_AMENDMENT
create sequence XHB_DMI_CAD_ERROR_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


commit;
