/*
*   -------------------------------------------------------------------------------------------
*   Script created by BH on 03/05/2017, for the database changes required for the Case Maintenance part of CREST to XHIBIT functionality project
*   -------------------------------------------------------------------------------------------
* 20/07/2018	S.Atwell	Amend call to add new PARENT_GUARDIAN_NAME column so that it is the correct width - 35 chars. This has been done to remove the need for a 2nd script to fix it in 8.7.0.22
* 06/09/2018	S.Atwell	Remove call to update XHB_CASE.CASE_LISTED at this time
*/


/*    ------------------------------------------------------------------
*     CREATE XHB_REF_MONITORING_CATEGORY TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_MONITORING_CATEGORY
(
  REF_MONITORING_CATEGORY_ID   		NUMBER not null,
  MONITORING_CATEGORY_CODE  		VARCHAR2(5) not null,
  MONITORING_CATEGORY_NAME		VARCHAR2(255) not null,
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1
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
alter table XHB_REF_MONITORING_CATEGORY
  add constraint REF_MONITORING_CATEGORY_PK primary key (REF_MONITORING_CATEGORY_ID)
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
grant select, insert, update on XHB_REF_MONITORING_CATEGORY to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR REF_MONITORING_CATEGORY
*/	------------------------------------------------------------------

create table AUD_REF_MONITORING_CATEGORY
(
  REF_MONITORING_CATEGORY_ID   		NUMBER not null,
  MONITORING_CATEGORY_CODE  		VARCHAR2(5) not null,
  MONITORING_CATEGORY_NAME		VARCHAR2(255) not null,
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1,
  insert_event            		VARCHAR2(1)
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
grant select, insert, update on AUD_REF_MONITORING_CATEGORY to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_MONITORING_CATEGORY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_MONITORING_CATEGORY', 'AUD_REF_MONIROTING_CATEGORY', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_REF_MONITORING_CATEGORY
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_MONIROTING_CATEGORY
create sequence XHB_REF_MONITORING_CAT_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_REF_MON_CATEGORY_bir_tr.sql;
@@XHB_REF_MON_CATEGORY_bur_tr.sql;

commit;


/*    ------------------------------------------------------------------
*     UPDATE XHB_COURT TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_COURT ADD (POLICE_FORCE_CODE 	NUMBER);



-- Create foreign key constraints 

ALTER TABLE XHB_COURT ADD (CONSTRAINT XHB_POLICE_FORCE_CODE_FK FOREIGN KEY (POLICE_FORCE_CODE) REFERENCES XHB_REF_SYSTEM_CODE (REF_SYSTEM_CODE_ID));




/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR COURT
*/	------------------------------------------------------------------

ALTER TABLE AUD_COURT ADD (POLICE_FORCE_CODE	NUMBER);




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_COURT_bur_tr.sql;

commit;




/*    ------------------------------------------------------------------
*     UPDATE XHB_CASE TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_CASE ADD (TRANSFERRED_CASE 			VARCHAR2(1), 
			TRANSFER_DEFERRED_SENTENCE 		VARCHAR2(1), 
			MONITORING_CATEGORY_ID  		NUMBER,
  			APPEAL_LODGED_DATE      		DATE,
  			RECEIVED_DATE           		DATE,
  			EITHER_WAY_TYPE      	  		VARCHAR2(2),
  			TICKET_REQUIRED         		VARCHAR2(1),
  			TICKET_TYPE_CODE    	  		NUMBER,
  			COURT_ID_RECEIVING_SITE 		NUMBER,
  			COMMITTAL_DATE          		DATE,
  			SENT_FOR_TRIAL_DATE     		DATE,
  			NO_DEFENDANTS_FOR_CASE  		NUMBER,
  			BENCH_WARRANT           		VARCHAR2(1),
  			BENCH_WARRANT_DEF_SENTENCE		VARCHAR2(1),
  			SECURE_COURT				VARCHAR2(1),
  			PRELIMINARY_DATE_OF_HEARING		DATE,
  			ORIGINAL_JPS_1				VARCHAR2(255),
  			ORIGINAL_JPS_2				VARCHAR2(255),
  			ORIGINAL_JPS_3				VARCHAR2(255),
  			ORIGINAL_JPS_4				VARCHAR2(255),
  			POLICE_FORCE_CODE			NUMBER,
 			MAGCOURT_HEARINGTYPE_REF_ID		NUMBER,
			CASE_LISTED 				VARCHAR2(1));



-- Create foreign key constraints 


ALTER TABLE XHB_CASE ADD (CONSTRAINT XHB_MONITORINGCATEGORY_ID_FK FOREIGN KEY (MONITORING_CATEGORY_ID) REFERENCES XHB_REF_MONITORING_CATEGORY (REF_MONITORING_CATEGORY_ID));

ALTER TABLE XHB_CASE ADD (CONSTRAINT XHB_TICKETTYPE_CODE_FK FOREIGN KEY (TICKET_TYPE_CODE) REFERENCES XHB_REF_SYSTEM_CODE (REF_SYSTEM_CODE_ID));

ALTER TABLE XHB_CASE ADD (CONSTRAINT XHB_COURT_RECEIVING_CODE_FK FOREIGN KEY (COURT_ID_RECEIVING_SITE) REFERENCES XHB_COURT_SITE (COURT_SITE_ID));

ALTER TABLE XHB_CASE ADD (CONSTRAINT XHB_POLICEFORCE_CODE_FK FOREIGN KEY (POLICE_FORCE_CODE) REFERENCES XHB_REF_SYSTEM_CODE (REF_SYSTEM_CODE_ID));

ALTER TABLE XHB_CASE ADD (CONSTRAINT XHB_MAGCOURT_HEARING_FK FOREIGN KEY (MAGCOURT_HEARINGTYPE_REF_ID) REFERENCES XHB_REF_SYSTEM_CODE (REF_SYSTEM_CODE_ID));




/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR CASE
*/	------------------------------------------------------------------

ALTER TABLE AUD_CASE ADD (TRANSFERRED_CASE 			VARCHAR2(1), 
			TRANSFER_DEFERRED_SENTENCE 		VARCHAR2(1), 
			MONITORING_CATEGORY_ID  		NUMBER,
  			APPEAL_LODGED_DATE      		DATE,
  			RECEIVED_DATE           		DATE,
  			EITHER_WAY_TYPE      	  		VARCHAR2(2),
  			TICKET_REQUIRED         		VARCHAR2(1),
  			TICKET_TYPE_CODE    	  		NUMBER,
  			COURT_ID_RECEIVING_SITE 		NUMBER,
  			COMMITTAL_DATE          		DATE,
  			SENT_FOR_TRIAL_DATE     		DATE,
  			NO_DEFENDANTS_FOR_CASE  		NUMBER,
  			BENCH_WARRANT           		VARCHAR2(1),
  			BENCH_WARRANT_DEF_SENTENCE	VARCHAR2(1),
  			SECURE_COURT				VARCHAR2(1),
  			PRELIMINARY_DATE_OF_HEARING		DATE,
  			ORIGINAL_JPS_1				VARCHAR2(255),
  			ORIGINAL_JPS_2				VARCHAR2(255),
  			ORIGINAL_JPS_3				VARCHAR2(255),
  			ORIGINAL_JPS_4				VARCHAR2(255),
  			POLICE_FORCE_CODE			NUMBER,
 			MAGCOURT_HEARINGTYPE_REF_ID		NUMBER,
			CASE_LISTED 				VARCHAR2(1));




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_CASE_bur_tr.sql;

commit;

/*    ---------------------------------------------------------------------
*     UPDATE XHB_CASE - set default value for CASE_LISTED of existing cases
*/    ---------------------------------------------------------------------

--UPDATE XHB_CASE SET CASE_LISTED = 'Y';

--commit;



/*    ------------------------------------------------------------------
*     CREATE XHB_CHARGES_LOG TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CHARGES_LOG
(
  CHARGES_LOG_ID  	 		NUMBER not null,
  CASE_ID				NUMBER not null,
  SEQUENCE_NO				NUMBER not null,
  CHARGES_INFO				VARCHAR2(80),
  OBS_IND				VARCHAR2(1),
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1
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
alter table XHB_CHARGES_LOG
  add constraint CHARGES_LOG_PK primary key (CHARGES_LOG_ID)
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
grant select, insert, update on XHB_CHARGES_LOG to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR CHARGES_LOG
*/	------------------------------------------------------------------

create table AUD_CHARGES_LOG
(
  CHARGES_LOG_ID  	 		NUMBER not null,
  CASE_ID				NUMBER not null,
  SEQUENCE_NO				NUMBER not null,
  CHARGES_INFO				VARCHAR2(80),
  OBS_IND				VARCHAR2(1),
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1,
  insert_event            		VARCHAR2(1)
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
grant select, insert, update on AUD_CHARGES_LOG to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CHARGES_LOG';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CHARGES_LOG', 'AUD_CHARGES_LOG', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_CHARGES_LOG
*/	------------------------------------------------------------------

-- Create sequence table for XHB_CHARGES_LOG
create sequence XHB_CHARGES_LOG_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_CHARGES_LOG_bir_tr.sql;
@@XHB_CHARGES_LOG_bur_tr.sql;

commit;


/*    ------------------------------------------------------------------
*     CREATE XHB_CASE_NUMBER_SEQ_GENERATOR TABLE AND AUDIT TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_CASE_NUMBER_SEQ_GENERATOR
(
  CASE_TYPE  	 			VARCHAR2(50) not null,
  COURT_ID				NUMBER not null,
  CURRENT_SEQUENCE			NUMBER not null,
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1
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
ALTER TABLE XHB_CASE_NUMBER_SEQ_GENERATOR ADD (CONSTRAINT XHB_COURT_ID_FK FOREIGN KEY (COURT_ID) REFERENCES XHB_COURT (COURT_ID));



-- Grant/Revoke object privileges 
grant select, insert, update on XHB_CASE_NUMBER_SEQ_GENERATOR to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR CASE_NUMBER_SEQ_GENERATOR
*/	------------------------------------------------------------------

create table AUD_CASE_NUMBER_SEQ_GENERATOR
(
  CASE_TYPE  	 			VARCHAR2(50) not null,
  COURT_ID				NUMBER not null,
  CURRENT_SEQUENCE			NUMBER not null,
  CREATED_BY              		VARCHAR2(30) not null,
  LAST_UPDATED_BY         		VARCHAR2(30) not null,
  CREATION_DATE           		DATE not null,
  LAST_UPDATE_DATE        		DATE not null,
  VERSION                 		NUMBER default 1,
  insert_event            		VARCHAR2(1)
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
grant select, insert, update on AUD_CASE_NUMBER_SEQ_GENERATOR to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_NUMBER_SEQ_GENERATOR';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_NUMBER_SEQ_GENERATOR', 'AUD_CASE_NUMBER_SEQ_GENERATOR', 'Y');



/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_CASENUMBERSEQGEN_bir_tr.sql;
@@XHB_CASENUMBERSEQGEN_bur_tr.sql;

commit;


/*    ------------------------------------------------------------------
*     UPDATE XHB_PROSECUTOR_REF_SOL_FIRM TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_PROSECUTOR_REF_SOL_FIRM ADD (SOLICITOR_REF		VARCHAR2(30), 
					     LIST_DATE_SENT 		DATE, 
					     LIST_DATE_RECEIVED		DATE);



/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR PROSECUTOR_REF_SOL_FIRM
*/	------------------------------------------------------------------

ALTER TABLE AUD_PROSECUTOR_REF_SOL_FIRM ADD (SOLICITOR_REF		VARCHAR2(30), 
					     LIST_DATE_SENT 		DATE, 
					     LIST_DATE_RECEIVED		DATE);




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_PROSECUTORREFSOLFRM_bur_tr.sql;

commit;


/*    ------------------------------------------------------------------
*     UPDATE XHB_DEFENDANT_ON_CASE TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_DEFENDANT_ON_CASE ADD (DRIVING_DISQ_SUSPENDED_DATE	DATE, 
				       MAG_COURT_FIRST_HEARING_DATE		DATE, 
				       MAG_COURT_FINAL_HEARING_DATE		DATE);



/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR DEFENDANT_ON_CASE
*/	------------------------------------------------------------------

ALTER TABLE AUD_DEFENDANT_ON_CASE ADD (DRIVING_DISQ_SUSPENDED_DATE	DATE, 
				       MAG_COURT_FIRST_HEARING_DATE		DATE, 
				       MAG_COURT_FINAL_HEARING_DATE		DATE);




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_DEFENDANTONCASE_bur_tr.sql;

commit;



/*    ------------------------------------------------------------------
*     UPDATE XHB_DEFENDANT TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_DEFENDANT ADD (PARENT_GUARDIAN_NAME	VARCHAR2(35), 
			       ETHNIC_APPEARANCE_CODE   VARCHAR2(10), 
			       ETHNICITY_SELF_DEFINED	VARCHAR2(10));



/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR DEFENDANT
*/	------------------------------------------------------------------

ALTER TABLE AUD_DEFENDANT ADD (PARENT_GUARDIAN_NAME	VARCHAR2(35), 
			       ETHNIC_APPEARANCE_CODE   VARCHAR2(10), 
			       ETHNICITY_SELF_DEFINED	VARCHAR2(10));




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_DEFENDANT_bur_tr.sql;


/*    ------------------------------------------------------------------
*     UPDATE XHB_DEF_ON_CASE_REF_SOL_FIRM TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_DEF_ON_CASE_REF_SOL_FIRM ADD (SOLICITOR_REF		VARCHAR2(30), 
			       		      LIST_DATE_SENT    	DATE, 
			       		      LIST_DATE_RECEIVED	DATE);



/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR DEF_ON_CASE_REF_SOL_FIRM
*/	------------------------------------------------------------------

ALTER TABLE AUD_DEF_ON_CASE_REF_SOL_FIRM ADD (SOLICITOR_REF		VARCHAR2(30), 
			       		      LIST_DATE_SENT    	DATE, 
			       		      LIST_DATE_RECEIVED	DATE);




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_DEF_CASE_SOL_FIRM_bur_tr.sql;

commit;



/*    ------------------------------------------------------------------
*     UPDATE XHB_CASE_PROSECUTOR_AGENCY TABLE AND AUDIT TABLE
*/    ------------------------------------------------------------------


ALTER TABLE XHB_CASE_PROSECUTOR_AGENCY ADD (RESPONDENT_STATUS	VARCHAR2(2));



/*	------------------------------------------------------------------
/*	UPDATE AUDIT TABLE FOR CASE_PROSECUTOR_AGENCY
*/	------------------------------------------------------------------

ALTER TABLE AUD_CASE_PROSECUTOR_AGENCY ADD (RESPONDENT_STATUS	VARCHAR2(2));




/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------


@@XHB_CASEPROSECUTORAGENC_bur_tr.sql;

commit;