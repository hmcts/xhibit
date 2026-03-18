/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_DIARY_TIME_RQMT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_DIARY_TIME_RQMT 
(
	CASE_DIARY_TIME_RQMT_ID NUMBER(8) NOT NULL,
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	FIRST_LISTING_DATE DATE,
	FINAL_LISTING_DATE DATE,
	TIME_ESTIMATE_HOURS VARCHAR2(4),
	TIME_ESTIMATE_DAYS VARCHAR2(4),
	TIME_ESTIMATE_WEEKS VARCHAR2(4),
	FIXTURE_NOTICE_REQUIRED VARCHAR2(1),
	HEARING_TYPE_ID NUMBER(8),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(200),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1,
	CONSTRAINT CDTR_CASE_LISTING_ENTRY_ID_FK
	FOREIGN KEY(CASE_LISTING_ENTRY_ID)
	REFERENCES XHB_CASE_LISTING_ENTRY(CASE_LISTING_ENTRY_ID),
	CONSTRAINT CDTR_HEARING_TYPE_ID_FK
	FOREIGN KEY(HEARING_TYPE_ID)
	REFERENCES XHB_REF_HEARING_TYPE(REF_HEARING_TYPE_ID)
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
alter table XHB_CASE_DIARY_TIME_RQMT
  add constraint XHB_CASE_DIARY_TIME_RQMT_PK primary key (CASE_DIARY_TIME_RQMT_ID) 
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
grant select, insert, update on XHB_CASE_DIARY_TIME_RQMT to PUBLIC;




/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_CASE_DIARY_TIME_RQMT
*/	------------------------------------------------------------------

create table AUD_CASE_DIARY_TIME_RQMT
(
	CASE_DIARY_TIME_RQMT_ID NUMBER(8) NOT NULL,
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	FIRST_LISTING_DATE DATE,
	FINAL_LISTING_DATE DATE,
	TIME_ESTIMATE_HOURS VARCHAR2(4),
	TIME_ESTIMATE_DAYS VARCHAR2(4),
	TIME_ESTIMATE_WEEKS VARCHAR2(4),
	FIXTURE_NOTICE_REQUIRED VARCHAR2(1),
	HEARING_TYPE_ID NUMBER(8),
	LIST_NOTE_PRE_DEFINED_ID NUMBER(8),
	LIST_NOTE_TEXT VARCHAR2(200),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
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
grant select, insert, update on AUD_CASE_DIARY_TIME_RQMT to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_DIARY_TIME_RQMT';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_DIARY_TIME_RQMT', 'AUD_CASE_DIARY_TIME_RQMT', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_CASE_DIARY_TIME_RQMT
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CASE_DIARY_TIME_RQMT
create sequence XHB_CASE_DIARY_TIME_RQMT_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_diary_time_rqmt_bir_tr.sql;
@@xhb_case_diary_time_rqmt_bur_tr.sql;

commit;
