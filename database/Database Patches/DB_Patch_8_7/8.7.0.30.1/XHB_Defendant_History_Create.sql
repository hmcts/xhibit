/*
*   -------------------------------------------------------------------------------------------
*   Script created by BH on 28/06/2018, for the database changes required for the new XHB_DEFENDANT_HISTORY table
*   -------------------------------------------------------------------------------------------
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_DEFENDANT_HISTORY TABLE 
*/    ------------------------------------------------------------------

create table XHB_DEFENDANT_HISTORY
(
  DEFENDANT_HISTORY_ID   		NUMBER(8) not null,
  DEFENDANT_ID				NUMBER(8) not null,
  CREST_DEFENDANT_ID			NUMBER(8) not null,
  COURT_ID				NUMBER(8) not null,
  SURNAME		  		VARCHAR2(255),
  FIRST_NAME				VARCHAR2(35),
  MIDDLE_NAME				VARCHAR2(35),
  DATE_OF_BIRTH				DATE,
  GENDER				NUMBER(1),
  REASON_DELETED			VARCHAR2(255),
  DATE_ARCHIVED				DATE not null,
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1
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
alter table XHB_DEFENDANT_HISTORY
  add constraint DEFENDANT_HISTORY_PK primary key (DEFENDANT_HISTORY_ID)
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


ALTER TABLE XHB_DEFENDANT_HISTORY ADD (CONSTRAINT XHB_DEFENDANT_HISTORY_COURT_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_DEFENDANT_HISTORY to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_DEFENDANT_HISTORY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_DEFENDANT_HISTORY
create sequence XHB_DEFENDANT_HISTORY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;




commit;


