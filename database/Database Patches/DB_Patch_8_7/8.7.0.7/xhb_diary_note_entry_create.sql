/*    ------------------------------------------------------------------
*     CREATE XHB_DIARY_NOTE_ENTRY TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_DIARY_NOTE_ENTRY
(
	DIARY_NOTE_ENTRY_ID NUMBER(8) NOT NULL,
    CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
    NOTE_TYPE_ID NUMBER(8),
	NOTE_CLASSIFICATION_ID NUMBER(8),
    DIARY_NOTE_TEXT VARCHAR2(200), 
	DIARY_NOTE_PRE_DEFINED_ID NUMBER(8),
    DIARY_START_DATE DATE NOT NULL,
	DIARY_END_DATE DATE NOT NULL,
	COURT_ID NUMBER(8) NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,	
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER DEFAULT 1,
	CONSTRAINT DNE_CASE_LISTING_ENTRY_ID_FK
	FOREIGN KEY(CASE_LISTING_ENTRY_ID)
	REFERENCES XHB_CASE_LISTING_ENTRY(CASE_LISTING_ENTRY_ID),	
	CONSTRAINT DNE_NOTE_TYPE_ID_FK
	FOREIGN KEY(NOTE_TYPE_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT DNE_NOTE_CLASSIFICATION_ID_FK
	FOREIGN KEY(NOTE_CLASSIFICATION_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT DNE_COURT_ID_FK
	FOREIGN KEY(COURT_ID)
	REFERENCES XHB_COURT(COURT_ID)
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
alter table XHB_DIARY_NOTE_ENTRY
  add constraint XHB_DIARY_NOTE_ENTRY_PK primary key (DIARY_NOTE_ENTRY_ID) 
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
grant select, insert, update on XHB_DIARY_NOTE_ENTRY to PUBLIC;




/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_DIARY_NOTE_ENTRY
*/	------------------------------------------------------------------

create table AUD_DIARY_NOTE_ENTRY
(
	DIARY_NOTE_ENTRY_ID NUMBER(8) NOT NULL,
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	NOTE_TYPE_ID NUMBER(8),
	NOTE_CLASSIFICATION_ID NUMBER(8),
    DIARY_NOTE_TEXT VARCHAR2(200),
	DIARY_NOTE_PRE_DEFINED_ID NUMBER(8),
	DIARY_START_DATE DATE NOT NULL,
	DIARY_END_DATE DATE NOT NULL,
	COURT_ID NUMBER(8) NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,	
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER DEFAULT 1, 	
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
grant select, insert, update on AUD_DIARY_NOTE_ENTRY to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_DIARY_NOTE_ENTRY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_DIARY_NOTE_ENTRY', 'AUD_DIARY_NOTE_ENTRY', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR AUD_DIARY_NOTE_ENTRY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_DIARY_NOTE_ENTRY
create sequence XHB_DIARY_NOTE_ENTRY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_diary_note_entry_bir_tr.sql;
@@xhb_diary_note_entry_bur_tr.sql;

commit;
