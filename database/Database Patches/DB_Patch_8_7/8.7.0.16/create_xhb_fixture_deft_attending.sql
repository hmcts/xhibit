/*    ------------------------------------------------------------------
*     CREATE XHB_FIXTURE_DEFT_ATTENDING TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

CREATE TABLE XHB_FIXTURE_DEFT_ATTENDING 
( FIXTURE_DEFT_ATTENDING_ID NUMBER (8) NOT NULL
, DEFENDANT_ON_CASE_ID NUMBER (8) NOT NULL 
, CASE_DIARY_FIXTURE_ID NUMBER (8) NOT NULL 
, ATTENDING VARCHAR2(1) DEFAULT 'Y' NOT NULL
, OBS_IND VARCHAR2(1)  
, LAST_UPDATE_DATE DATE NOT NULL
, CREATION_DATE DATE NOT NULL
, LAST_UPDATED_BY VARCHAR2(30) NOT NULL
, CREATED_BY VARCHAR2(30) NOT NULL
, VERSION NUMBER (5) DEFAULT 1 NOT NULL
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
  
--Field Comments
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.FIXTURE_DEFT_ATTENDING_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.DEFENDANT_ON_CASE_ID IS 'FK. cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.CASE_DIARY_FIXTURE_ID IS 'FK cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.ATTENDING IS 'Y or N default to N';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.OBS_IND IS 'Used to record whether the data in this row is obsolete. This field can be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.LAST_UPDATE_DATE IS 'Date record was last updated on This field cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.CREATION_DATE IS 'Date record was created on. This field cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.LAST_UPDATED_BY IS 'User id of the user that updated the record.This field cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.CREATED_BY IS 'User if of the user that created the record. This field cannot be null';
COMMENT ON COLUMN XHB_FIXTURE_DEFT_ATTENDING.VERSION IS 'Default of 1. This field cannot be null';


-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_FIXTURE_DEFT_ATTENDING ADD (CONSTRAINT FIX_DEFT_ATT_DEF_CASE_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID));
ALTER TABLE XHB_FIXTURE_DEFT_ATTENDING ADD (CONSTRAINT FIX_DEFT_ATT_CASE_DY_FIX_ID_FK FOREIGN KEY (CASE_DIARY_FIXTURE_ID) REFERENCES XHB_CASE_DIARY_FIXTURE (CASE_DIARY_FIXTURE_ID));
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_FIXTURE_DEFT_ATTENDING
  add constraint XHB_FIXTURE_DEFT_ATTENDING_PK primary key (FIXTURE_DEFT_ATTENDING_ID) 
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
grant select, insert, update on XHB_FIXTURE_DEFT_ATTENDING to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_FIXTURE_DEFT_ATTENDING
*/	------------------------------------------------------------------

CREATE TABLE AUD_FIXTURE_DEFT_ATTENDING 
( FIXTURE_DEFT_ATTENDING_ID NUMBER (8) NOT NULL
, DEFENDANT_ON_CASE_ID NUMBER (8) NOT NULL 
, CASE_DIARY_FIXTURE_ID NUMBER (8) NOT NULL 
, ATTENDING VARCHAR2(1) DEFAULT 'Y' NOT NULL
, OBS_IND VARCHAR2(1)  
, LAST_UPDATE_DATE DATE NOT NULL
, CREATION_DATE DATE NOT NULL
, LAST_UPDATED_BY VARCHAR2(30) NOT NULL
, CREATED_BY VARCHAR2(30) NOT NULL
, VERSION NUMBER (5) DEFAULT 1 NOT NULL
, INSERT_EVENT VARCHAR2(1)
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
grant select, insert, update on AUD_FIXTURE_DEFT_ATTENDING to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE FROM XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_FIXTURE_DEFT_ATTENDING';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_FIXTURE_DEFT_ATTENDING', 'AUD_FIXTURE_DEFT_ATTENDING', 'Y');


-- Create sequence table for XHB_FIXTURE_DEFT_ATTENDING
create sequence XHB_FIXTURE_DEFT_ATTENDING_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_fixture_deft_attending_bir_tr.sql;
@@xhb_fixture_deft_attending_bur_tr.sql;

commit;
