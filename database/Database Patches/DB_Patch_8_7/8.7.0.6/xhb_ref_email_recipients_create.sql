/*    ------------------------------------------------------------------
*     CREATE XHB_REF_EMAIL_RECIPIENTS TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_EMAIL_RECIPIENTS
(
	EMAIL_RECIPIENTS_ID NUMBER(8) NOT NULL,
	EMAIL_ADDRESS VARCHAR2(255),
	ROLE_TYPE VARCHAR2(50),
	COURT_ID NUMBER(8) NOT NULL,
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
  

ALTER TABLE XHB_REF_EMAIL_RECIPIENTS ADD (CONSTRAINT XHB_LEGAL_AID_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));

-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_REF_EMAIL_RECIPIENTS
  add constraint XHB_REF_EMAIL_RECIPIENTS_PK primary key (EMAIL_RECIPIENTS_ID) 
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
grant select, insert, update on XHB_REF_EMAIL_RECIPIENTS to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_REF_EMAIL_RECIPIENTS
*/	------------------------------------------------------------------

create table AUD_REF_EMAIL_RECIPIENTS
(
	EMAIL_RECIPIENTS_ID NUMBER(8) NOT NULL,
	EMAIL_ADDRESS VARCHAR2(255),
	ROLE_TYPE VARCHAR2(50),
	COURT_ID NUMBER(8) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1,
 	insert_event VARCHAR2(1)
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
grant select, insert, update on AUD_REF_EMAIL_RECIPIENTS to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_EMAIL_RECIPIENTS';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_EMAIL_RECIPIENTS', 'AUD_REF_EMAIL_RECIPIENTS', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_EMAIL_RECIPIENTS
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_EMAIL_RECIPIENTS
create sequence XHB_REF_EMAIL_RECIPIENTS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_REF_EMAIL_RECIPIENTS_bir_tr.sql;
@@XHB_REF_EMAIL_RECIPIENTS_bur_tr.sql;

commit;
