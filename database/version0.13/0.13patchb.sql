-- Remove Test Data from previous patch
DELETE TABLE XHB_COURT_LOG_EVENT_DESC 

/*
	List Distribution Addtions
*/
-- Drop Sequence
DROP SEQUENCE XHB_WLL_RECIPIENT_SEQ ;

DROP SEQUENCE XHB_RECIPIENT_SEQ ;

-- Drop Table
DROP TABLE XHB_WLL_RECIPIENT CASCADE CONSTRAINTS;

DROP TABLE XHB_RECIPIENT CASCADE CONSTRAINTS;


-- Rose Script BEGIN

CREATE TABLE XHB_DOCUMENT_REPLY (
	doc_reply_id NUMBER ( 8 ) NOT NULL,
	reply_name VARCHAR2 ( 255 ) NOT NULL,
	reply_fax VARCHAR2 ( 30 ),
	reply_email VARCHAR2 ( 50 ),
	reply_address VARCHAR2 ( 255 ),
	court_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_DocumentReply19 PRIMARY KEY (doc_reply_id),
	CONSTRAINT TC_XHB_DOCUMENT_REPLY174 UNIQUE (court_id)
	)
/
CREATE TABLE XHB_RECIPIENT (
	recipient_id NUMBER ( 8 ) NOT NULL,
	recipient_name VARCHAR2 ( 255 ) NOT NULL,
	fax_number VARCHAR2 ( 30 ),
	email_address VARCHAR2 ( 50 ),
	court_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_Recipient10 PRIMARY KEY (recipient_id)
	)
/
CREATE TABLE XHB_DOCUMENT_RECIPIENT (
	document_recipient_id NUMBER ( 8 ) NOT NULL,
	doc_recipient_name VARCHAR2 ( 255 ),
	doc_recipient_fax VARCHAR2 ( 30 ),
	doc_recipient_email VARCHAR2 ( 30 ),
	doc_address VARCHAR2 ( 255 ),
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_DocumentRecipient13 PRIMARY KEY (document_recipient_id)
	)
/
CREATE TABLE XHB_FORMATTING (
	formatting_id NUMBER ( 8 ) NOT NULL,
	date_in DATE NOT NULL,
	xml_document CLOB NOT NULL,
	formatted_document CLOB,
	format_status VARCHAR2 ( 2 ),
	distribution_type VARCHAR2 ( 5 ) NOT NULL,
	mime_type VARCHAR2 ( 3 ) NOT NULL,
	document_type VARCHAR2 ( 3 ) NOT NULL,
	process_type VARCHAR2 ( 3 ),
	court_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_Formatting7 PRIMARY KEY (formatting_id)
	)
/
CREATE TABLE XHB_XML_DOCUMENT (
	xml_document_id NUMBER ( 8 ) NOT NULL,
	date_created DATE NOT NULL,
	document_title VARCHAR2 ( 255 ) NOT NULL,
	xml_document CLOB NOT NULL,
	status VARCHAR2 ( 2 ),
	expiry_date DATE,
	document_type VARCHAR2 ( 3 ) NOT NULL,
	court_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_XMLDocument18 PRIMARY KEY (xml_document_id)
	)
/
CREATE TABLE XHB_DOCUMENT_CONTROL (
	doc_control_id NUMBER ( 8 ) NOT NULL,
	formatted_document CLOB,
	status VARCHAR2 ( 2 ),
	expiry_date DATE,
	distribution_type VARCHAR2 ( 5 ) NOT NULL,
	mime_type VARCHAR2 ( 3 ) NOT NULL,
	document_type VARCHAR2 ( 3 ) NOT NULL,
	court_id NUMBER ( 8 ) NOT NULL,
	formatting_id NUMBER ( 8 ),
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT TC_XHB_DOCUMENT_CONTROL170 UNIQUE (formatting_id),
	CONSTRAINT PK_XHB_DocumentControl12 PRIMARY KEY (doc_control_id)
	)
/
CREATE TABLE XHB_DOCUMENT_DISTRIBUTION (
	doc_distribution_id NUMBER ( 8 ) NOT NULL,
	distribution_type VARCHAR2 ( 5 ) NOT NULL,
	document_type VARCHAR2 ( 3 ) NOT NULL,
	mime_type VARCHAR2 ( 3 ) NOT NULL,
	recipient_id NUMBER ( 8 ),
	wll_recipient_id NUMBER ( 8 ),
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_DocumentDistribution10 PRIMARY KEY (doc_distribution_id)
	)
/
CREATE TABLE XHB_WLL_CONTROL (
	wll_control_id NUMBER ( 8 ) NOT NULL,
	status VARCHAR2 ( 2 ),
	expiry_date DATE,
	xml_document_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_WLLControl17 PRIMARY KEY (wll_control_id),
	CONSTRAINT TC_XHB_WLL_CONTROL140 UNIQUE (xml_document_id)
	)
/
CREATE TABLE XHB_WLL_RECIPIENT (
	wll_recipient_id NUMBER ( 8 ) NOT NULL,
	crest_solicitor_firm_id NUMBER ( 8 ),
	solicitor_firm_name VARCHAR2 ( 255 ),
	solictior_firm_address VARCHAR2 ( 255 ),
	solicitor_firm_fax VARCHAR2 ( 30 ),
	solicitor_firm_email VARCHAR2 ( 50 ),
	distribution_type VARCHAR2 ( 5 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_WLLRecipient15 PRIMARY KEY (wll_recipient_id)
	)
/
CREATE TABLE XHB_WLL_DOCUMENT (
	wll_document_id NUMBER ( 8 ) NOT NULL,
	wll_recipient_id NUMBER ( 8 ),
	xml_document_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_WLL_DOCUMENT10 PRIMARY KEY (wll_document_id),
	CONSTRAINT TC_XHB_WLL_DOCUMENT242 UNIQUE (xml_document_id)
	)
/
ALTER TABLE XHB_DOCUMENT_CONTROL ADD ( CONSTRAINT FK_XHB_DOCUMENT_CONTROL16 FOREIGN KEY (formatting_id) REFERENCES XHB_FORMATTING (formatting_id))
/
ALTER TABLE XHB_DOCUMENT_DISTRIBUTION ADD ( CONSTRAINT FK_XHB_DOCUMENT_DISTRIBUTIO15 FOREIGN KEY (wll_recipient_id) REFERENCES XHB_WLL_RECIPIENT (wll_recipient_id))
/
ALTER TABLE XHB_DOCUMENT_DISTRIBUTION ADD ( CONSTRAINT FK_XHB_DOCUMENT_DISTRIBUTIO14 FOREIGN KEY (recipient_id) REFERENCES XHB_RECIPIENT (recipient_id))
/
ALTER TABLE XHB_WLL_CONTROL ADD ( CONSTRAINT FK_XHB_WLL_CONTROL18 FOREIGN KEY (xml_document_id) REFERENCES XHB_XML_DOCUMENT (xml_document_id))
/
ALTER TABLE XHB_WLL_DOCUMENT ADD ( CONSTRAINT FK_XHB_WLL_DOCUMENT17 FOREIGN KEY (xml_document_id) REFERENCES XHB_XML_DOCUMENT (xml_document_id))
/
ALTER TABLE XHB_WLL_DOCUMENT ADD ( CONSTRAINT FK_XHB_WLL_DOCUMENT16 FOREIGN KEY (wll_recipient_id) REFERENCES XHB_WLL_RECIPIENT (wll_recipient_id))
/

-- Rose Script END

-- Add Sequences

CREATE SEQUENCE XHB_DOCUMENT_REPLY_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_RECIPIENT_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_DOCUMENT_RECIPIENT_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_FORMATTING_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_XML_DOCUMENT_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_DOCUMENT_CONTROL_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_DOCUMENT_DISTRIBUTION_SEQ
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_WLL_CONTROL_SEQ
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_WLL_RECIPIENT_SEQ
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

CREATE SEQUENCE XHB_WLL_DOCUMENT_SEQ
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

-- Triggers for new tables

create or replace trigger XHB_DOCUMENT_REPLY_BIR_TR
  BEFORE INSERT
  on XHB_DOCUMENT_REPLY
  
  for each row
BEGIN
SELECT XHB_DOCUMENT_REPLY_SEQ.NEXTVAL INTO               :NEW.doc_reply_id  FROM DUAL;


end;
/

create or replace trigger XHB_RECIPIENT_BIR_TR
  BEFORE INSERT
  on XHB_RECIPIENT
  
  for each row
BEGIN
SELECT XHB_RECIPIENT_SEQ.NEXTVAL INTO               :NEW.recipient_id  FROM DUAL;


end;
/


create or replace trigger XHB_DOCUMENT_RECIPIENT_BIR_TR
  BEFORE INSERT
  on XHB_DOCUMENT_RECIPIENT
  
  for each row
BEGIN
SELECT XHB_DOCUMENT_RECIPIENT_SEQ.NEXTVAL INTO               :NEW.document_recipient_id  FROM DUAL;


end;
/


create or replace trigger XHB_FORMATTING_BIR_TR
  BEFORE INSERT
  on XHB_FORMATTING
  
  for each row
BEGIN
SELECT XHB_FORMATTING_SEQ.NEXTVAL INTO               :NEW.formatting_id  FROM DUAL;


end;
/

create or replace trigger XHB_XML_DOCUMENT_BIR_TR
  BEFORE INSERT
  on XHB_XML_DOCUMENT
  
  for each row
BEGIN
SELECT XHB_XML_DOCUMENT_SEQ.NEXTVAL INTO               :NEW.xml_document_id  FROM DUAL;


end;
/

create or replace trigger XHB_DOCUMENT_CONTROL_BIR_TR
  BEFORE INSERT
  on XHB_DOCUMENT_CONTROL
  
  for each row
BEGIN
SELECT XHB_DOCUMENT_CONTROL_SEQ.NEXTVAL INTO               :NEW.doc_control_id  FROM DUAL;


end;
/

create or replace trigger XHB_DOCUMENT_DISTRIB_BIR_TR
  BEFORE INSERT
  on XHB_DOCUMENT_DISTRIBUTION
  
  for each row
BEGIN
SELECT XHB_DOCUMENT_DISTRIBUTION_SEQ.NEXTVAL INTO               :NEW.doc_distribution_id  FROM DUAL;


end;
/

create or replace trigger XHB_WLL_CONTROL_BIR_TR
  BEFORE INSERT
  on XHB_WLL_CONTROL
  
  for each row
BEGIN
SELECT XHB_WLL_CONTROL_SEQ.NEXTVAL INTO               :NEW.wll_control_id  FROM DUAL;


end;
/

create or replace trigger XHB_WLL_RECIPIENT_BIR_TR
  BEFORE INSERT
  on XHB_WLL_RECIPIENT
  
  for each row
BEGIN
SELECT XHB_WLL_RECIPIENT_SEQ.NEXTVAL INTO               :NEW.wll_recipient_id  FROM DUAL;


end;
/

create or replace trigger XHB_WLL_DOCUMENT_BIR_TR
  BEFORE INSERT
  on XHB_WLL_DOCUMENT
  
  for each row
BEGIN
SELECT XHB_WLL_DOCUMENT_SEQ.NEXTVAL INTO               :NEW.wll_document_id  FROM DUAL;


end;
/


/*
	CR Live Status Update
*/

-- Drop Sequence
DROP SEQUENCE XHB_CR_LIVE_STATUS_DESC_SEQ ;

DROP SEQUENCE XHB_CR_LIVE_STATUS_SEQ ;

-- Drop Table
DROP TABLE XHB_CR_LIVE_STATUS CASCADE CONSTRAINTS;

DROP TABLE XHB_CR_LIVE_STATUS_DESC CASCADE CONSTRAINTS;

-- Rose Script BEGIN
-- Add Bean Audit Attributes

CREATE TABLE XHB_CR_LIVE_STATUS (
	cr_live_status_id NUMBER ( 8 ) NOT NULL,
	court_room_id NUMBER ( 8 ) NOT NULL,
	scheduled_hearing_id NUMBER ( 8 ) NOT NULL,
	time_status_set DATE NOT NULL,
	internet_help_code NUMBER ( 8 ) ,
	internet_status VARCHAR2 ( 255 ) ,
	public_display_status VARCHAR2 ( 255 ) ,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,
	CONSTRAINT PK_XHB_CourtRoomLiveStatus24 PRIMARY KEY (cr_live_status_id)
	)
/
-- Rose Script END

-- Add Constraints
ALTER TABLE XHB_CR_LIVE_STATUS ADD ( CONSTRAINT FK_XHB_CR_LIVE_STATUS34 FOREIGN KEY (scheduled_hearing_id) REFERENCES XHB_SCHEDULED_HEARING (scheduled_hearing_id))
/
ALTER TABLE XHB_CR_LIVE_STATUS ADD ( CONSTRAINT FK_XHB_CR_LIVE_STATUS33 FOREIGN KEY (court_room_id) REFERENCES XHB_COURT_ROOM (court_room_id))
/

-- Add Sequences

CREATE SEQUENCE XHB_CR_LIVE_STATUS_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

-- Triggers for new tables

create or replace trigger XHB_CR_LIVE_STATUS_BIR_TR
  BEFORE INSERT
  on XHB_CR_LIVE_STATUS
  
  for each row
BEGIN
SELECT XHB_CR_LIVE_STATUS_SEQ.NEXTVAL INTO               :NEW.cr_live_status_id  FROM DUAL;


end;
/

/*
	Remove AUDIT tables
*/


declare
-- Dynamic SQL to drop audit tables as below
--drop table jnk

cursor c1 is select table_name from user_tables
  where table_name like 'AUD%';

l_org_table varchar2(255);
l_str1  varchar2(255) := ' ; ';
l_str2  varchar2(255) := ' DROP TABLE ';
l_stmt   varchar2(500);
begin
for record in c1 loop
if c1%FOUND then
  l_org_table := record.table_name;

  l_stmt := l_str2||l_org_table;
  DBMS_OUTPUT.PUT_LINE(l_stmt);
  EXECUTE IMMEDIATE l_stmt;
end if;
end loop;
end;
/


DROP TABLE XXX_DUMMY_TABLE_FOR_AUDITING CASCADE CONSTRAINTS;

/*
	SH_LEG_REP Changes

	1. Make XHB_SH_LEG_REP.ref_solicitor_firm_id NULLABLE
	2. XHB_SH_LEG_REP.SCHED_HEAR_DEF_ID NULLABLE
	3. Remove ShLegRepID from ScheduledHearingAttendee
	4. Add scheduledHearingId to SHLegRep 

*/
-- Drop Sequence
DROP SEQUENCE XHB_SCHED_HEARING_ATTEND_SEQ ;

DROP SEQUENCE XHB_SH_LEG_REP_SEQ ;

-- Drop Table
DROP TABLE XHB_SCHED_HEARING_ATTENDEE CASCADE CONSTRAINTS;

DROP TABLE XHB_SH_LEG_REP CASCADE CONSTRAINTS;

-- Remove ShLegRepID from ScheduledHearingAttendee
CREATE TABLE XHB_SCHED_HEARING_ATTENDEE (
       SH_ATTENDEE_ID       NUMBER(8) NOT NULL,
       ATTENDEE_TYPE        VARCHAR2(2) NOT NULL,
       SCHEDULED_HEARING_ID NUMBER(8) NOT NULL,
       VERSION              NUMBER NOT NULL,
       SH_STAFF_ID          NUMBER(8) NULL,
       SH_JUSTICE_ID        NUMBER(8) NULL,
       REF_JUDGE_ID         NUMBER(8) NULL,
       REF_COURT_REPORTER_ID NUMBER(8) NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL
);

CREATE INDEX xhb_sched_hear_att_sched_hr_fk ON XHB_SCHED_HEARING_ATTENDEE
(
       SCHEDULED_HEARING_ID           ASC
);

CREATE INDEX xhb_sch_hr_sh_staff_fk ON XHB_SCHED_HEARING_ATTENDEE
(
       SH_STAFF_ID                    ASC
);

CREATE INDEX xhb_sch_hr_sh_just_fk ON XHB_SCHED_HEARING_ATTENDEE
(
       SH_JUSTICE_ID                  ASC
);

CREATE INDEX xhb_sch_hr_ref_judge_fk ON XHB_SCHED_HEARING_ATTENDEE
(
       REF_JUDGE_ID                   ASC
);

CREATE INDEX xhb_sch_hr_ref_crt_rep_fk ON XHB_SCHED_HEARING_ATTENDEE
(
       REF_COURT_REPORTER_ID          ASC
);


ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( PRIMARY KEY (SH_ATTENDEE_ID) ) ;

ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( FOREIGN KEY (REF_COURT_REPORTER_ID)
                             REFERENCES XHB_REF_COURT_REPORTER ) ;


ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( FOREIGN KEY (REF_JUDGE_ID)
                             REFERENCES XHB_REF_JUDGE ) ;


ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( FOREIGN KEY (SH_JUSTICE_ID)
                             REFERENCES XHB_SH_JUSTICE ) ;


ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( FOREIGN KEY (SH_STAFF_ID)
                             REFERENCES XHB_SH_STAFF ) ;


ALTER TABLE XHB_SCHED_HEARING_ATTENDEE
       ADD  ( FOREIGN KEY (SCHEDULED_HEARING_ID)
                             REFERENCES XHB_SCHEDULED_HEARING ) ;
	   
-- Rose Script BEGIN
-- Add Bean Audit Attributes
CREATE TABLE XHB_SH_LEG_REP (
	sh_leg_rep_id NUMBER ( 8 ) NOT NULL,
	sol_firm_or_ref_legal_rep VARCHAR2 ( 1 ),
	is_signed_in VARCHAR2 ( 1 ),
	legal_role VARCHAR2 ( 1 ),
	crest_sequence_no NUMBER ( 5 ),
	sched_hear_def_id NUMBER ( 8 ),
	cc_info_id NUMBER ( 8 ),
	ref_defence_category_id NUMBER ( 8 ),
	ref_solicitor_firm_id NUMBER ( 8 ),
	ref_legal_rep_id NUMBER ( 8 ),
	scheduled_hearing_id NUMBER ( 8 ),
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,	
	CONSTRAINT sh_leg_rep_pk PRIMARY KEY (sh_leg_rep_id)
	)
/
-- Rose Script END

-- Add Constraints
ALTER TABLE XHB_SH_LEG_REP ADD ( CONSTRAINT FK_XHB_SH_LEG_REP10 FOREIGN KEY (ref_legal_rep_id) REFERENCES XHB_REF_LEGAL_REPRESENTATIVE (ref_legal_rep_id))
/
ALTER TABLE XHB_SH_LEG_REP ADD ( CONSTRAINT sh_leg_rep_cc_info_fk FOREIGN KEY (cc_info_id) REFERENCES XHB_CC_INFO (cc_info_id))
/
ALTER TABLE XHB_SH_LEG_REP ADD ( CONSTRAINT sh_leg_rep_sh_def_fk FOREIGN KEY (sched_hear_def_id) REFERENCES XHB_SCHED_HEARING_DEFENDANT (sched_hear_def_id))
/
ALTER TABLE XHB_SH_LEG_REP ADD ( CONSTRAINT FK_XHB_SH_LEG_REP0 FOREIGN KEY (scheduled_hearing_id) REFERENCES XHB_SCHEDULED_HEARING (scheduled_hearing_id))
/
ALTER TABLE XHB_SH_LEG_REP ADD ( CONSTRAINT FK_XHB_SH_LEG_REP9 FOREIGN KEY (ref_solicitor_firm_id) REFERENCES XHB_REF_SOLICITOR_FIRM (ref_solicitor_firm_id))
/


/*
	COURT.internet_name
*/

ALTER TABLE XHB_COURT ADD  internet_court_name VARCHAR2 ( 255 ) NOT NULL

/*
	INTERNET_HTML
*/
-- Drop Sequence
DROP SEQUENCE XHB_INTERNET_XML_HTML_SEQ ;

-- Drop Table
DROP TABLE XHB_INTERNET_XML_HTML CASCADE CONSTRAINTS;

-- Rose Script BEGIN
CREATE TABLE XHB_INTERNET_HTML (
	internet_html_id NUMBER ( 8 ) NOT NULL,
	html CLOB,
	status VARCHAR2 ( 1 ) NOT NULL,
	court_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT PK_XHB_InternetXMLHTML26 PRIMARY KEY (internet_html_id)
	)
/
-- Rose Script END

-- Add Constraints
ALTER TABLE XHB_INTERNET_HTML
       ADD  ( FOREIGN KEY (COURT_ID)
                             REFERENCES XHB_COURT ) ;

-- Add Sequences

CREATE SEQUENCE XHB_INTERNET_HTML_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

-- Triggers for new tables

create or replace trigger XHB_INTERNET_HTML_BIR_TR
  BEFORE INSERT
  on XHB_INTERNET_HTML
  
  for each row
BEGIN
SELECT XHB_INTERNET_HTML_SEQ.NEXTVAL INTO               :NEW.internet_html_id   FROM DUAL;


end;
/


/*
	CHARGE_DIFF
*/

-- Drop Sequence
DROP SEQUENCE XHB_CHARGE_DIFFERENCES_SEQ ;

-- Drop Table
DROP TABLE XHB_CHARGE_DIFFERENCES CASCADE CONSTRAINTS;

-- Rose Script BEGIN
CREATE TABLE XHB_CHARGE_DIFFERENCES (
	charge_diff_id NUMBER ( 8 ) NOT NULL,
	diff_time DATE NOT NULL,
	report CLOB,
	court_id NUMBER ( 8 ) NOT NULL,
	case_id NUMBER ( 8 ) NOT NULL,
       LAST_UPDATE_DATE     DATE NOT NULL,
       CREATION_DATE        DATE NOT NULL,
       CREATED_BY           VARCHAR2(30) NOT NULL,
       LAST_UPDATED_BY      VARCHAR2(30) NOT NULL,
       VERSION              NUMBER NOT NULL,		
	CONSTRAINT charge_diff_id_pk PRIMARY KEY (charge_diff_id)
	)
/
-- Rose Script END

-- Add Constraints
ALTER TABLE XHB_CHARGE_DIFFERENCES ADD ( CONSTRAINT charge_differences_court_fk FOREIGN KEY (court_id) REFERENCES XHB_COURT (court_id))
/
ALTER TABLE XHB_CHARGE_DIFFERENCES ADD ( CONSTRAINT charge_differences_case_fk FOREIGN KEY (case_id) REFERENCES XHB_CASE (case_id))
/
-- Add Sequences

CREATE SEQUENCE XHB_CHARGE_DIFFERENCES_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;

-- Triggers for new tables

create or replace trigger XHB_CHARGE_DIFFERENCES_BIR_TR
  BEFORE INSERT
  on XHB_CHARGE_DIFFERENCES
  
  for each row
BEGIN
SELECT XHB_CHARGE_DIFFERENCES_SEQ.NEXTVAL INTO               :NEW.charge_diff_id  FROM DUAL;


end;
/

