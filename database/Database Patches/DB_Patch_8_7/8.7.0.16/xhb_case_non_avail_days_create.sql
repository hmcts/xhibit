/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_NON_AVAIL_DAYS TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_NON_AVAIL_DAYS
(
	NAD_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	START_DATE DATE NOT NULL,
	END_DATE DATE NOT NULL,
	REASON VARCHAR2(200) NOT NULL,
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL,
	CONSTRAINT NAD_CASE_ID_FK
	FOREIGN KEY(CASE_ID)
	REFERENCES XHB_CASE(CASE_ID)
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
alter table XHB_CASE_NON_AVAIL_DAYS
  add constraint XHB_CASE_NON_AVAIL_DAYS_PK primary key (NAD_ID) 
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
grant select, insert, update on XHB_CASE_NON_AVAIL_DAYS to PUBLIC;

/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_CASE_NON_AVAIL_DAYS
*/	------------------------------------------------------------------

create table AUD_CASE_NON_AVAIL_DAYS
(
	NAD_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	START_DATE DATE NOT NULL,
	END_DATE DATE NOT NULL,
	REASON VARCHAR2(200) NOT NULL,
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
grant select, insert, update on AUD_CASE_NON_AVAIL_DAYS to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_NON_AVAIL_DAYS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_NON_AVAIL_DAYS', 'AUD_CASE_NON_AVAIL_DAYS', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_CASE_NON_AVAIL_DAYS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CASE_NON_AVAIL_DAYS
create sequence XHB_CASE_NON_AVAIL_DAYS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_non_avail_days_bir_tr.sql;
@@xhb_case_non_avail_days_bur_tr.sql;

commit;
