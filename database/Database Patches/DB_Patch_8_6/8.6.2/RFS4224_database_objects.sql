/*
*   --------------------------------------------------------------------------------------
*   Script created by BH on 10/07/2014, for RFS4224
*   The aim of the script is to update XHB_DEFENDANT_ON_CASE and
*   create the new Xhibit tables that will handle the new Hate Crime information.
*   --------------------------------------------------------------------------------------
*/

/*

/*    ------------------------------------------------------------------
*     UPDATE XHB_DEFENDANT_ON_CASE TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_DEFENDANT_ON_CASE ADD (HATE_IND VARCHAR2(1), HATE_TYPE VARCHAR2(2), HATE_SENT_IND VARCHAR2(1));
ALTER TABLE AUD_DEFENDANT_ON_CASE ADD (HATE_IND VARCHAR2(1), HATE_TYPE VARCHAR2(2), HATE_SENT_IND VARCHAR2(1));

@@xhb_defendantoncase_bur_tr.sql;


commit;



/*    ------------------------------------------------------------------
*     CREATE XHB_HATE_SENTENCING TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_HATE_SENTENCING
(
  HATE_SENTENCING_ID          NUMBER not null,
  DEFENDANT_ON_CASE_ID        NUMBER not null,
  REF_HATE_SENT_TYPE_ID 	  NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1
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
alter table XHB_HATE_SENTENCING
  add constraint HATE_SENTENCE_PK primary key (HATE_SENTENCING_ID)
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

ALTER TABLE XHB_HATE_SENTENCING ADD (CONSTRAINT XHB_HATE_SENT_DOC_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID));



-- Create index on defendant_on_case_id
CREATE INDEX XHB_HATE_SENT_DEF_ON_CASE_FK ON XHB_HATE_SENTENCING (DEFENDANT_ON_CASE_ID ASC)
	TABLESPACE XHIBITD
	STORAGE (INITIAL 1M
		 NEXT 1M
		 PCTINCREASE 0); 

-- Grant/Revoke object privileges 
grant select, insert, update on XHB_HATE_SENTENCING to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

create table AUD_HATE_SENTENCING (
  HATE_SENTENCING_ID          NUMBER not null,
  DEFENDANT_ON_CASE_ID        NUMBER not null,
  REF_HATE_SENT_TYPE_ID 	  NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1,
  insert_event            VARCHAR2(1)
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
grant select, insert, update on AUD_HATE_SENTENCING to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_HATE_SENTENCING';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_HATE_SENTENCING', 'AUD_HATE_SENTENCING', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

-- Create sequence table for XHB_HATE_SENTENCING
create sequence XHB_HATE_SENTENCING_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_hatesentencing_bir_tr.sql;
@@xhb_hatesentencing_bur_tr.sql;

@@xhb_housekeeping_pkg_b.sql;





commit;


/*    ------------------------------------------------------------------
*     CREATE XHB_REF_HATE_SENTENCING_TYPE TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_HATE_SENTENCING_TYPE
(
  REF_HATE_SENTENCING_TYPE_ID          NUMBER not null,
  HATE_SENT_TYPE 	  VARCHAR2(10) not null,
  TITLE			  VARCHAR2(60) not null,
  DESCRIPTION		  VARCHAR2(150) not null,
  CJS_QUALIFIER		  VARCHAR2(4),
  COURT_ID        	  NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1
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
alter table XHB_REF_HATE_SENTENCING_TYPE
  add constraint REF_HATE_SENTENCE_PK primary key (REF_HATE_SENTENCING_TYPE_ID)
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

ALTER TABLE XHB_REF_HATE_SENTENCING_TYPE ADD (CONSTRAINT XHB_REF_HATE_SENT_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));

-- Create index on court_id
CREATE INDEX XHB_REF_HATE_SENT_COURT_FK ON XHB_REF_HATE_SENTENCING_TYPE (COURT_ID ASC)
	TABLESPACE XHIBITD
	STORAGE (INITIAL 1M
		 NEXT 1M
		 PCTINCREASE 0); 

-- Grant/Revoke object privileges 
grant select, insert, update on XHB_REF_HATE_SENTENCING_TYPE to PUBLIC;

--Create link from XHB_HATE_SENTENCING TO REF TABLE
ALTER TABLE XHB_HATE_SENTENCING ADD (CONSTRAINT XHB_HATE_SENT_REF_HATE_TYPE_FK FOREIGN KEY (REF_HATE_SENT_TYPE_ID) REFERENCES XHB_REF_HATE_SENTENCING_TYPE (REF_HATE_SENTENCING_TYPE_ID));


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR REF_HATE_SENTENCING_TYPE
*/	------------------------------------------------------------------

create table AUD_REF_HATE_SENTENCING_TYPE (
  REF_HATE_SENTENCING_TYPE_ID          NUMBER not null,
  HATE_SENT_TYPE 	  VARCHAR2(10) not null,
  TITLE			  VARCHAR2(60) not null,
  DESCRIPTION		  VARCHAR2(150) not null,
  CJS_QUALIFIER		  VARCHAR2(4),
  COURT_ID        	  NUMBER not null,
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1,
  insert_event            VARCHAR2(1)
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
grant select, insert, update on AUD_REF_HATE_SENTENCING_TYPE to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_HATE_SENTENCING_TYPE';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_HATE_SENTENCING_TYPE', 'AUD_REF_HATE_SENTENCING_TYPE', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR HATE_SENTENCING
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_HATE_SENTENCING_TYPE
create sequence XHB_REF_HATE_SENT_TYPE_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_refhatesentencingtype_bir_tr.sql;
@@xhb_refhatesentencingtype_bur_tr.sql;

commit;




