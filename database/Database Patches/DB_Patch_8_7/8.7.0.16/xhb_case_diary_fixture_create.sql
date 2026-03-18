/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_DIARY_FIXTURE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_DIARY_FIXTURE
(
	CASE_DIARY_FIXTURE_ID NUMBER(8) NOT NULL,
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	LISTING_DATE DATE NOT NULL,
	FIXTURE_NOTICE_REQUIRED VARCHAR2(1) DEFAULT 'N' NOT NULL,
	HEARING_TYPE_ID NUMBER(8),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(100),
	PRE_DEF_NOTE_CLASS_ID NUMBER(8),
	FREE_TEXT_NOTE_CLASS_ID NUMBER(8),
	VACATION_PRE_DEFINED_RSON_ID NUMBER(8),
	VACATION_FREETEXT_REASON VARCHAR2(35),
	STATUS VARCHAR2(1) NOT NULL,
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL,
	CONSTRAINT CDF_CASE_LISTING_ENTRY_ID_FK
	FOREIGN KEY(CASE_LISTING_ENTRY_ID)
	REFERENCES XHB_CASE_LISTING_ENTRY(CASE_LISTING_ENTRY_ID),
	CONSTRAINT CDF_HEARING_TYPE_ID_FK
	FOREIGN KEY(HEARING_TYPE_ID)
	REFERENCES XHB_REF_HEARING_TYPE(REF_HEARING_TYPE_ID),
	CONSTRAINT CDF_LISTNOTEPREDEFINEDID_FK
	FOREIGN KEY(LIST_NOTE_PRE_DEFINED_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT CDF_PRE_DEF_NOTE_CLASS_ID_FK
	FOREIGN KEY(PRE_DEF_NOTE_CLASS_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT CDF_FREE_TEXT_NOTE_CLASS_ID_FK
	FOREIGN KEY(FREE_TEXT_NOTE_CLASS_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT CDF_VACATION_PREDEF_RSON_ID_FK
	FOREIGN KEY(VACATION_PRE_DEFINED_RSON_ID)
	REFERENCES XHB_REF_SYSTEM_CODE(REF_SYSTEM_CODE_ID)
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
alter table XHB_CASE_DIARY_FIXTURE
  add constraint XHB_CASE_DIARY_FIXTURE_PK primary key (CASE_DIARY_FIXTURE_ID) 
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
grant select, insert, update on XHB_CASE_DIARY_FIXTURE to PUBLIC;

/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_CASE_DIARY_FIXTURE
*/	------------------------------------------------------------------

create table AUD_CASE_DIARY_FIXTURE
(
	CASE_DIARY_FIXTURE_ID NUMBER(8) NOT NULL,
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	LISTING_DATE DATE NOT NULL,
	FIXTURE_NOTICE_REQUIRED VARCHAR2(1) NOT NULL,
	HEARING_TYPE_ID NUMBER(8),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(100),
	PRE_DEF_NOTE_CLASS_ID NUMBER(8),
	FREE_TEXT_NOTE_CLASS_ID NUMBER(8),
	VACATION_PRE_DEFINED_RSON_ID NUMBER(8),
	VACATION_FREETEXT_REASON VARCHAR2(35),
	STATUS VARCHAR2(1) NOT NULL,
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL,
	INSERT_EVENT VARCHAR2(1)
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


-- Grant/Revoke object privileges 
grant select, insert, update on AUD_CASE_DIARY_FIXTURE to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_DIARY_FIXTURE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_DIARY_FIXTURE', 'AUD_CASE_DIARY_FIXTURE', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_CASE_DIARY_FIXTURE
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CASE_DIARY_FIXTURE
create sequence XHB_CASE_DIARY_FIXTURE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_diary_fixture_bir_tr.sql;
@@xhb_case_diary_fixture_bur_tr.sql;

commit;
