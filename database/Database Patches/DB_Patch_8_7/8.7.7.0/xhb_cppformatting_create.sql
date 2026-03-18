/*    ------------------------------------------------------------------
*     CREATE XHB_CPP_FORMATTING TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CPP_FORMATTING (
	CPP_FORMATTING_ID NUMBER(8) NOT NULL,
	STAGING_TABLE_ID NUMBER(8) NOT NULL,
	DATE_IN DATE NOT NULL,
	FORMAT_STATUS VARCHAR2(2) DEFAULT 'ND' NOT NULL,
	DOCUMENT_TYPE VARCHAR2(3) NOT NULL,
	COURT_ID NUMBER(8) NOT NULL,
	XML_DOCUMENT_CLOB_ID NUMBER(8) NOT NULL,
	ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND VARCHAR2(1),
	VERSION NUMBER(5) DEFAULT 1,
	LAST_UPDATE_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL
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
ALTER TABLE XHB_CPP_FORMATTING ADD (CONSTRAINT CPP_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT(COURT_ID));
ALTER TABLE XHB_CPP_FORMATTING ADD (CONSTRAINT CPP_CLOB_ID_FK FOREIGN KEY (XML_DOCUMENT_CLOB_ID) REFERENCES XHB_CLOB(CLOB_ID));
ALTER TABLE XHB_CPP_FORMATTING ADD (CONSTRAINT CPP_STAGING_ID_FK FOREIGN KEY (STAGING_TABLE_ID) REFERENCES XHB_CPP_STAGING_INBOUND(CPP_STAGING_INBOUND_ID));
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_CPP_FORMATTING
  add constraint XHB_CPP_FORMATTING_PK primary key (CPP_FORMATTING_ID) 
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
grant select, insert, update on XHB_CPP_FORMATTING to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CPP_FORMATTING 
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CPP_FORMATTING
create sequence XHB_CPP_FORMATTING_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_cppformatting_bir_tr.sql;
@@xhb_cppformatting_bur_tr.sql;

commit;
