/*    ------------------------------------------------------------------
*     CREATE XHB_JUDGE_USAGE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_JUDGE_USAGE
(
	JUDGE_USAGE_ID NUMBER(8) NOT NULL,
    	COURT_ROOM_ID NUMBER(8),
	COURT_CHAMBERS_IND VARCHAR2(3),
	REF_JUDGE_ID NUMBER(8),
	MAIN_WORK_TYPE VARCHAR2(2),
    	SITTING_DATE DATE,
	TYPE_OF_WORK VARCHAR2(2),
	LAST_UPDATE_DATE DATE NOT NULL,	
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER DEFAULT 1,
	OBS_IND VARCHAR2(1)
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
alter table XHB_JUDGE_USAGE
  add constraint XHB_JUDGE_USAGE_PK primary key (JUDGE_USAGE_ID) 
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


ALTER TABLE XHB_JUDGE_USAGE ADD (CONSTRAINT XHB_JUDGE_USAGE_COURT_ROOM_FK FOREIGN KEY (COURT_ROOM_ID) REFERENCES XHB_COURT_ROOM (COURT_ROOM_ID));
ALTER TABLE XHB_JUDGE_USAGE ADD (CONSTRAINT XHB_JUDGE_USAGE_REF_JUDGE_FK FOREIGN KEY (REF_JUDGE_ID) REFERENCES XHB_REF_JUDGE (REF_JUDGE_ID));


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_JUDGE_USAGE to PUBLIC;




/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_JUDGE_USAGE
*/	------------------------------------------------------------------

create table AUD_JUDGE_USAGE
(
	JUDGE_USAGE_ID NUMBER(8) NOT NULL,
    	COURT_ROOM_ID NUMBER(8),
	COURT_CHAMBERS_IND VARCHAR2(3),
	REF_JUDGE_ID NUMBER(8),
	MAIN_WORK_TYPE VARCHAR2(2),
    	SITTING_DATE DATE,
	TYPE_OF_WORK VARCHAR2(2),
	LAST_UPDATE_DATE DATE NOT NULL,	
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER DEFAULT 1,
	OBS_IND VARCHAR2(1),	
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
grant select, insert, update on AUD_JUDGE_USAGE to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_JUDGE_USAGE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_JUDGE_USAGE', 'AUD_JUDGE_USAGE', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_JUDGE_USAGE
*/	------------------------------------------------------------------

-- Create sequence table for XHB_JUDGE_USAGE
create sequence XHB_JUDGE_USAGE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_judge_usage_bir_tr.sql;
@@xhb_judge_usage_bur_tr.sql;

commit;
