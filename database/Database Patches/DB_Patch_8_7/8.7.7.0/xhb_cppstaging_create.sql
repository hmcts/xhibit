/*    ------------------------------------------------------------------
*     CREATE XHB_CPP_STAGING_INBOUND TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CPP_STAGING_INBOUND (
	CPP_STAGING_INBOUND_ID NUMBER(8) NOT NULL,
	DOCUMENT_NAME VARCHAR2(50) NOT NULL,
	COURT_CODE VARCHAR2(3) NOT NULL,
	DOCUMENT_TYPE VARCHAR2(2) NOT NULL,
	TIME_LOADED DATE NOT NULL,
	CLOB_ID NUMBER(10) NOT NULL,
	VALIDATION_STATUS VARCHAR2(2) NOT NULL,
	ACKNOWLEDGMENT_STATUS VARCHAR2(2),
	PROCESSING_STATUS VARCHAR2(2),
	VALIDATION_ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND vARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1	
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
ALTER TABLE XHB_CPP_STAGING_INBOUND ADD (CONSTRAINT CPP_STAGING_CLOB_ID_FK FOREIGN KEY (CLOB_ID) REFERENCES XHB_CLOB(CLOB_ID));
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_CPP_STAGING_INBOUND
  add constraint XHB_CPP_STAGING_INBOUND_PK primary key (CPP_STAGING_INBOUND_ID) 
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
grant select, insert, update on XHB_CPP_STAGING_INBOUND to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CPP_FORMATTING 
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CPP_FORMATTING
create sequence XHB_CPP_STAGING_INBOUND_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_cppstaging_bir_tr.sql;
@@xhb_cppstaging_bur_tr.sql;

commit;
