/*    ------------------------------------------------------------------
*     CREATE XHB_SITTING_ON_LIST TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_SITTING_ON_LIST
(
	SITTING_ON_LIST_ID NUMBER(8) NOT NULL,
	SITTING_NUMBER NUMBER(3) NOT NULL,
	LIST_ID NUMBER(8) NOT NULL,
	TIME_MARKING_ID NUMBER(8),
	TIME_LISTED DATE,
	JUDGE_REF_ID NUMBER(8),
	JP1 VARCHAR2(100),
	JP2 VARCHAR2(100),
	JP3 VARCHAR2(100),
	JP4 VARCHAR2(100),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(200),
	PRE_DEF_NOTE_CLASSIFICATION_ID NUMBER(8), 
	FREE_TEXT_NOTE_CLASS_ID NUMBER(8), 
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL,
	CONSTRAINT SOL_LIST_ID_FK
	FOREIGN KEY(LIST_ID)
	REFERENCES XHB_LIST(LIST_ID),
	CONSTRAINT SOL_TIME_MARKING_ID_FK
	FOREIGN KEY(TIME_MARKING_ID)
	REFERENCES XHB_REF_SYSTEM_CODE(REF_SYSTEM_CODE_ID),
	CONSTRAINT SOL_JUDGE_REF_ID_FK
	FOREIGN KEY(JUDGE_REF_ID)
	REFERENCES XHB_REF_JUDGE(REF_JUDGE_ID),
	CONSTRAINT SOL_LISTNOTEPREDEFINEDID_FK
	FOREIGN KEY(LIST_NOTE_PRE_DEFINED_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT SOL_PRE_DEF_NOTE_CLASS_ID_FK
	FOREIGN KEY(PRE_DEF_NOTE_CLASSIFICATION_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID),
	CONSTRAINT SOL_FREE_TEXT_NOTE_CLASS_ID_FK
	FOREIGN KEY(FREE_TEXT_NOTE_CLASS_ID)
	REFERENCES XHB_REF_LISTING_DATA(REF_LISTING_DATA_ID)
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
alter table XHB_SITTING_ON_LIST
  add constraint XHB_SITTING_ON_LIST_PK primary key (SITTING_ON_LIST_ID) 
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
grant select, insert, update on XHB_SITTING_ON_LIST to PUBLIC;

/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_SITTING_ON_LIST
*/	------------------------------------------------------------------

create table AUD_SITTING_ON_LIST
(
	SITTING_ON_LIST_ID NUMBER(8) NOT NULL,
	SITTING_NUMBER NUMBER(3) NOT NULL,
	LIST_ID NUMBER(8) NOT NULL,
	TIME_MARKING_ID NUMBER(8),
	TIME_LISTED DATE,
	JUDGE_REF_ID NUMBER(8),
	JP1 VARCHAR2(100),
	JP2 VARCHAR2(100),
	JP3 VARCHAR2(100),
	JP4 VARCHAR2(100),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(200),
	PRE_DEF_NOTE_CLASSIFICATION_ID NUMBER(8), 
	FREE_TEXT_NOTE_CLASS_ID NUMBER(8), 
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
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
grant select, insert, update on AUD_SITTING_ON_LIST to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_SITTING_ON_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_SITTING_ON_LIST', 'AUD_SITTING_ON_LIST', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_SITTING_ON_LIST
*/	------------------------------------------------------------------

-- Create sequence table for XHB_SITTING_ON_LIST
create sequence XHB_SITTING_ON_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_sitting_on_list_bir_tr.sql;
@@xhb_sitting_on_list_bur_tr.sql;

commit;
