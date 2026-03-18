/*
*   --------------------------------------------------------------------------------------
*   Script created by KD on 04/02/2009, for CCN0400
*   The aim of the script is to create the new Xhibit reference table, that will hold both
*   the country codes, and descriptions
*   --------------------------------------------------------------------------------------
*/

/*

/*    ------------------------------------------------------------------
*     CREATE NATIONALITY TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_NATIONALITY
(
  REF_NATIONALITY_ID          NUMBER not null,
  REF_NATIONALITY_CODE        VARCHAR2(3) not null,
  REF_COUNTRY VARCHAR2(60),
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1
)
tablespace USERS
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
alter table XHB_REF_NATIONALITY
  add constraint REF_NATIONALITY_PK primary key (REF_NATIONALITY_ID)
  using index 
  tablespace USERS
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
grant select, insert, update on XHB_REF_NATIONALITY to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR NATIONALITY
*/	------------------------------------------------------------------

create table AUD_REF_NATIONALITY (
  REF_NATIONALITY_ID          NUMBER not null,
  REF_NATIONALITY_CODE        VARCHAR2(3) not null,
  REF_COUNTRY VARCHAR2(255),
  CREATED_BY              VARCHAR2(30) not null,
  LAST_UPDATED_BY         VARCHAR2(30) not null,
  CREATION_DATE           DATE not null,
  LAST_UPDATE_DATE        DATE not null,
  OBS_IND                 VARCHAR2(1),
  VERSION                 NUMBER default 1,
  insert_event            VARCHAR2(1)
)
tablespace USERS
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
grant select, insert, update on AUD_REF_NATIONALITY to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR NATIONALITY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_NATIONALITY
create sequence XHB_REF_NATIONALITY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 816317
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@xhb_refnationality_bir_tr.sql
@xhb_refnationality_bur_tr.sql




/*    ------------------------------------------------------------------
*     UPDATE DEFENDANT ON CASE AND AUDIT TABLE
*     UPDATE TRIGGERS
*/    ------------------------------------------------------------------



--alter XHIBIT defendant_On_Case table to include new Nationality field
ALTER TABLE XHB_DEFENDANT_ON_CASE
  ADD custodial VARCHAR2(1)
  ADD suspended VARCHAR2(1)
  ADD serious_drug_offence VARCHAR2(1)
  ADD recommended_deportation VARCHAR2(1)
  ADD NATIONALITY VARCHAR2(3);

--alter  AUDIT defendant_On_Case table to include new Nationality field  
ALTER TABLE AUD_DEFENDANT_ON_CASE
  ADD custodial VARCHAR2(1)
  ADD suspended VARCHAR2(1) 
  ADD serious_drug_offence VARCHAR2(1)
  ADD recommended_deportation VARCHAR2(1)
  ADD NATIONALITY VARCHAR2(3);
  
-- the defendant on case trigger will be updated in a separate file


/*    ------------------------------------------------------------------
*     THIS SECTION IS CONCERNED WITH THE INSERTATION OF DATA
*     INSERT NEW EVENT TYPE, EVENT DESCRIPTION
*/    ------------------------------------------------------------------


INSERT INTO XHB_COURT_LOG_EVENT_DESC
values(999,
	0,
	1,
	0,
	1,
	1,
	0,
	0,
	0,
	'LC_TEXT_',	
	'Highlight Deportation Reasons',
	1,
	'Xhibit',
	40791,
	'Xhibit',
	SYSDATE,
	SYSDATE,
	0,
	'Authorise_results');

commit;





