/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_LISTING_ENTRY TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_LISTING_ENTRY
(
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	REF_JUDGE_TYPE_ID VARCHAR2(2),
	TICKET_TYPE_ID NUMBER(8), 
	COURT_SITE_ID NUMBER(8),
	INTERPRETER VARCHAR2(50),
	JUDGE_ID NUMBER(8),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1,
	CONSTRAINT CASE_LISTING_COURT_SITE_ID_FK
	FOREIGN KEY(COURT_SITE_ID)
	REFERENCES XHB_COURT_SITE(COURT_SITE_ID),
	CONSTRAINT CASE_LISTING_JUDGE_ID_FK
	FOREIGN KEY(JUDGE_ID)
	REFERENCES XHB_REF_JUDGE(REF_JUDGE_ID)
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
alter table XHB_CASE_LISTING_ENTRY
  add constraint XHB_CASE_LISTING_ENTRY_PK primary key (CASE_LISTING_ENTRY_ID) 
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
grant select, insert, update on XHB_CASE_LISTING_ENTRY to PUBLIC;




/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_CASE_LISTING_ENTRY
*/	------------------------------------------------------------------

create table AUD_CASE_LISTING_ENTRY
(
	CASE_LISTING_ENTRY_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	REF_JUDGE_TYPE_ID VARCHAR2(2),
	TICKET_TYPE_ID NUMBER(8), 
	COURT_SITE_ID NUMBER(8),
	INTERPRETER VARCHAR2(50),
	JUDGE_ID NUMBER(8),
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
grant select, insert, update on AUD_CASE_LISTING_ENTRY to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_LISTING_ENTRY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_LISTING_ENTRY', 'AUD_CASE_LISTING_ENTRY', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_CASE_LISTING_ENTRY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CASE_LISTING_ENTRY
create sequence XHB_CASE_LISTING_ENTRY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_listing_entry_bir_tr.sql;
@@xhb_case_listing_entry_bur_tr.sql;

commit;
