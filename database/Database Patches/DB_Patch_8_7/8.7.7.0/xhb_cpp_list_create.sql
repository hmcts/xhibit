/*feature/cpp-7*/

/*    ------------------------------------------------------------------
*     CREATE XHB_CPP_LIST_TABLE
*/    ------------------------------------------------------------------

CREATE TABLE XHB_CPP_LIST (
	CPP_LIST_ID NUMBER(8) NOT NULL,
	COURT_CODE NUMBER(3) NOT NULL,
	LIST_TYPE VARCHAR2(1) NOT NULL,
	TIME_LOADED DATE NOT NULL,
	LIST_START_DATE DATE NOT NULL,
	LIST_END_DATE DATE NOT NULL,
	LIST_CLOB_ID NUMBER(8),
	MERGED_CLOB_ID NUMBER(8),
	STATUS VARCHAR2(2),
	ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND VARCHAR2 (1),
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL
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


ALTER TABLE XHB_CPP_LIST ADD (CONSTRAINT LIST_CLOB_ID_FK FOREIGN KEY (LIST_CLOB_ID) REFERENCES XHB_CLOB(CLOB_ID));
ALTER TABLE XHB_CPP_LIST ADD (CONSTRAINT MERGED_CLOB_ID_FK FOREIGN KEY (MERGED_CLOB_ID) REFERENCES XHB_CLOB(CLOB_ID));

  
-- Create/Recreate primary, unique and foreign key constraints 
  alter table  XHB_CPP_LIST
  add constraint XHB_CPP_LIST_PK primary key (CPP_LIST_ID) 
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
grant select, insert, update on XHB_CPP_LIST to PUBLIC;

/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CPP_LIST
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CPP_LIST
create sequence XHB_CPP_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_CPP_LIST_bir_tr.sql;
@@XHB_CPP_LIST_bur_tr.sql;

commit;
