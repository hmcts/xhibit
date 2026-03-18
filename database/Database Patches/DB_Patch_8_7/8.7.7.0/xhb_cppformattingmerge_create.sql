/*    ------------------------------------------------------------------
*     CREATE XHB_CPP_FORMATTING_MERGE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CPP_FORMATTING_MERGE (
	CPP_FORMATTING_MERGE_ID NUMBER(8) NOT NULL,
	CPP_FORMATTING_ID NUMBER(8) NOT NULL,
	FORMATTING_ID NUMBER(8) NOT NULL,
	XHIBIT_CLOB_ID NUMBER(10),
	COURT_ID NUMBER(8) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LANGUAGE VARCHAR2(2),
	OBS_IND vARCHAR2(1),
	VERSION NUMBER(5) DEFAULT 1,
	LAST_UPDATE_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
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
ALTER TABLE XHB_CPP_FORMATTING_MERGE ADD (CONSTRAINT CPP_MERGE_CPP_FORMATTING_ID_FK FOREIGN KEY (CPP_FORMATTING_ID) REFERENCES XHB_CPP_FORMATTING(CPP_FORMATTING_ID));
ALTER TABLE XHB_CPP_FORMATTING_MERGE ADD (CONSTRAINT CPP_MERGE_FORMATTING_ID_FK FOREIGN KEY (FORMATTING_ID) REFERENCES XHB_FORMATTING(FORMATTING_ID));
ALTER TABLE XHB_CPP_FORMATTING_MERGE ADD (CONSTRAINT CPP_MERGE_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT(COURT_ID));
ALTER TABLE XHB_CPP_FORMATTING_MERGE ADD (CONSTRAINT CPP_MERGE_CLOB_ID_FK FOREIGN KEY (XHIBIT_CLOB_ID) REFERENCES XHB_CLOB(CLOB_ID));
 
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_CPP_FORMATTING_MERGE
  add constraint XHB_CPP_FORMATTING_MERGE_PK primary key (CPP_FORMATTING_MERGE_ID) 
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
grant select, insert, update on XHB_CPP_FORMATTING_MERGE to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CPP_FORMATTING_MERGE 
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CPP_FORMATTING_MERGE
create sequence XHB_CPP_FORMATTING_MERGE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_cppformattingmerge_bir_tr.sql;
@@xhb_cppformattingmerge_bur_tr.sql;

commit;
