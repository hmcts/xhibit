/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_ON_LIST
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_ON_LIST(
	CASE_ON_LIST_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	LIST_ID NUMBER(8) NOT NULL,
	SITTING_ID NUMBER(8),
	COURT_SITE_ID NUMBER(8),
	COURT_ROOM_ID NUMBER(8) NOT NULL,
	RESERVED VARCHAR2(1),
	FLOATING_CASE VARCHAR2(1),
	TIME_MARKING_ID NUMBER(8),
	TIME_LISTED DATE,
	IS_COURT_ROOM_LIST_ENTRY VARCHAR2(1),
	HEARING_TYPE_ID NUMBER(8) NOT NULL,
	REASON_FOR_REMOVAL VARCHAR2(100),
	CRACKED_INEFFECTIVE_ID NUMBER(8),
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1 NOT NULL
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
alter table XHB_CASE_ON_LIST
  add constraint XHB_CASE_ON_LIST_PK primary key (CASE_ON_LIST_ID)
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
    
-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_CASE_ID_FK FOREIGN KEY (CASE_ID) REFERENCES XHB_CASE(CASE_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_LIST_ID_FK FOREIGN KEY (LIST_ID) REFERENCES XHB_LIST(LIST_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_SITTING_ID_FK FOREIGN KEY (SITTING_ID) REFERENCES XHB_SITTING(SITTING_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_CS_ID_FK FOREIGN KEY (COURT_SITE_ID) REFERENCES XHB_COURT_SITE(COURT_SITE_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_CR_ID_FK FOREIGN KEY (COURT_ROOM_ID) REFERENCES XHB_COURT_ROOM(COURT_ROOM_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_RHT_ID_FK FOREIGN KEY (HEARING_TYPE_ID) REFERENCES XHB_REF_HEARING_TYPE(REF_HEARING_TYPE_ID));
ALTER TABLE XHB_CASE_ON_LIST ADD (CONSTRAINT XHB_CASE_ON_LIST_TM_ID_FK FOREIGN KEY (TIME_MARKING_ID) REFERENCES XHB_REF_SYSTEM_CODE(REF_SYSTEM_CODE_ID));

alter table XHB_CASE_ON_LIST
ADD CONSTRAINT IS_COURT_ROOM_LIST_ENTRY_CHK CHECK (IS_COURT_ROOM_LIST_ENTRY IN ('Y','N'));
  
 -- Grant/Revoke object privileges 
grant select, insert, update on XHB_CASE_ON_LIST to PUBLIC;

/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR CASE_ON_LIST
*/	------------------------------------------------------------------

create table AUD_CASE_ON_LIST
(
	CASE_ON_LIST_ID NUMBER(8) NOT NULL,
	CASE_ID NUMBER(8) NOT NULL,
	LIST_ID NUMBER(8) NOT NULL,
	SITTING_ID NUMBER(8),
	COURT_SITE_ID NUMBER(8),
	COURT_ROOM_ID NUMBER(8) NOT NULL,
	RESERVED VARCHAR2(1),
	FLOATING_CASE VARCHAR2(1),
	TIME_MARKING_ID NUMBER(8),
	TIME_LISTED DATE,
	IS_COURT_ROOM_LIST_ENTRY VARCHAR2(1),
	HEARING_TYPE_ID NUMBER(8) NOT NULL,
	REASON_FOR_REMOVAL VARCHAR2(100),
	CRACKED_INEFFECTIVE_ID NUMBER(8),
	OBS_IND VARCHAR2(1),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1 NOT NULL,
  INSERT_EVENT VARCHAR2(1) NOT NULL
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
grant select, insert, update on AUD_CASE_ON_LIST to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_ON_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_ON_LIST', 'AUD_CASE_ON_LIST', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CASE_ON_LIST
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CASE_ON_LIST
create sequence XHB_CASE_ON_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_case_on_list_bir_tr.sql;
@@xhb_case_on_list_bur_tr.sql;

commit;

