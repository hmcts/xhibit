/*    ------------------------------------------------------------------
*     CREATE XHB_COURT_ROOM_USAGE TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_COURT_ROOM_USAGE
(
	COURT_ROOM_USAGE_ID NUMBER(8) NOT NULL,
    	COURT_ROOM_ID NUMBER(8),
	AM_TIME_CIV_HOURS NUMBER(22),
	AM_TIME_CIV_MINS NUMBER(22),
	AM_TIME_HOURS NUMBER(22),
	AM_TIME_MINS NUMBER(22),
	PM_TIME_CIV_HOURS NUMBER(22),
	PM_TIME_CIV_MINS NUMBER(22),
	PM_TIME_HOURS NUMBER(22),
	PM_TIME_MINS NUMBER(22),
    	SITTING_DATE DATE,
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
alter table XHB_COURT_ROOM_USAGE
  add constraint XHB_COURT_ROOM_USAGE_PK primary key (COURT_ROOM_USAGE_ID) 
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


ALTER TABLE XHB_COURT_ROOM_USAGE ADD (CONSTRAINT XHB_COURTROOM_USAGE_CRTROOM_FK FOREIGN KEY (COURT_ROOM_ID) REFERENCES XHB_COURT_ROOM (COURT_ROOM_ID));




-- Grant/Revoke object privileges 
grant select, insert, update on XHB_COURT_ROOM_USAGE to PUBLIC;




/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_COURT_ROOM_USAGE
*/	------------------------------------------------------------------

create table AUD_COURT_ROOM_USAGE
(
	COURT_ROOM_USAGE_ID NUMBER(8) NOT NULL,
    	COURT_ROOM_ID NUMBER(8),
    	AM_TIME_CIV_HOURS NUMBER(22),
	AM_TIME_CIV_MINS NUMBER(22),
	AM_TIME_HOURS NUMBER(22),
	AM_TIME_MINS NUMBER(22),
	PM_TIME_CIV_HOURS NUMBER(22),
	PM_TIME_CIV_MINS NUMBER(22),
	PM_TIME_HOURS NUMBER(22),
	PM_TIME_MINS NUMBER(22),
    	SITTING_DATE DATE,
	LAST_UPDATE_DATE DATE NOT NULL,	
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER DEFAULT 1,
	OBS_IND VARCHAR2(1) ,	
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
grant select, insert, update on AUD_COURT_ROOM_USAGE to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_COURT_ROOM_USAGE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_COURT_ROOM_USAGE', 'AUD_COURT_ROOM_USAGE', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_COURT_ROOM_USAGE
*/	------------------------------------------------------------------

-- Create sequence table for XHB_COURT_ROOM_USAGE
create sequence XHB_COURT_ROOM_USAGE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_court_room_usage_bir_tr.sql;
@@xhb_court_room_usage_bur_tr.sql;

commit;
