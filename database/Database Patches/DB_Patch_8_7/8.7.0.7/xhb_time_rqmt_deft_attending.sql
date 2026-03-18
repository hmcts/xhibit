/*    ------------------------------------------------------------------
*     CREATE XHB_TIME_RQMT_DEFT_ATTENDING TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_TIME_RQMT_DEFT_ATTENDING
(
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	CASE_DIARY_TIME_RQMT_ID NUMBER(8) NOT NULL,
	ATTENDING VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1
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
alter table XHB_TIME_RQMT_DEFT_ATTENDING
  add constraint XHB_TIMERQMTDEFTATTENDING_PK primary key (DEFENDANT_ON_CASE_ID, CASE_DIARY_TIME_RQMT_ID)
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
grant select, insert, update on XHB_TIME_RQMT_DEFT_ATTENDING to PUBLIC;




/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_TIME_RQMT_DEFT_ATTENDING
*/	------------------------------------------------------------------

create table AUD_TIME_RQMT_DEFT_ATTENDING
(
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	CASE_DIARY_TIME_RQMT_ID NUMBER(8) NOT NULL,
	ATTENDING VARCHAR2(1),
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
grant select, insert, update on AUD_TIME_RQMT_DEFT_ATTENDING to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_TIME_RQMT_DEFT_ATTENDING';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_TIME_RQMT_DEFT_ATTENDING', 'AUD_TIME_RQMT_DEFT_ATTENDING', 'Y');

/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_time_rqmt_deft_attending_bir_tr.sql;
@@xhb_time_rqmt_deft_attending_bur_tr.sql;

commit;
