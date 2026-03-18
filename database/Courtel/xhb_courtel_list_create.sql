



/*    ------------------------------------------------------------------
*     CREATE XHB_COURTEL_LIST TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_COURTEL_LIST
(
	COURTEL_LIST_ID NUMBER(8) NOT NULL,
	XML_DOCUMENT_ID NUMBER(8) NOT NULL,
	BLOB_ID NUMBER(10),
	FILENAME VARCHAR2(50),
	SENT_TO_COURTEL VARCHAR(1) DEFAULT 'N',
	NUM_SERVERS_UPLOADED_TO NUMBER(8) DEFAULT 0,
	NUM_SEND_ATTEMPTS NUMBER(8) DEFAULT 0,
	LAST_ATTEMPT_DATETIME DATE DEFAULT NULL,
	COURTEL_RESPONSE_SERVER_1 VARCHAR2(2000),
	COURTEL_RESPONSE_SERVER_2 VARCHAR2(2000),
	MESSAGE_TEXT VARCHAR2(256),
	LAST_UPDATE_DATE DATE,
	CREATION_DATE DATE,
	CREATED_BY VARCHAR2(50),
	LAST_UPDATED_BY VARCHAR2(50),
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
  

ALTER TABLE XHB_COURTEL_LIST ADD (CONSTRAINT XML_DOCUMENT_ID_FK FOREIGN KEY (XML_DOCUMENT_ID) REFERENCES XHB_XML_DOCUMENT (XML_DOCUMENT_ID));

-- Create/Recreate primary, unique and foreign key constraints 
alter table XHB_COURTEL_LIST
  add constraint XHB_COURTEL_LIST_PK primary key (COURTEL_LIST_ID) 
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


  ALTER TABLE XHB_COURTEL_LIST ADD (CONSTRAINT BLOB_ID_FK FOREIGN KEY (BLOB_ID) REFERENCES XHB_BLOB (BLOB_ID));


-- Grant/Revoke object privileges 
grant select, insert, update on XHB_COURTEL_LIST to PUBLIC;


/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR XHB_COURTEL_LIST
*/	------------------------------------------------------------------

create table AUD_XHB_COURTEL_LIST
(
	COURTEL_LIST_ID NUMBER(8) NOT NULL,
	XML_DOCUMENT_ID NUMBER(8) NOT NULL,
	BLOB_ID NUMBER(10),
	FILENAME VARCHAR2(50),
	SENT_TO_COURTEL VARCHAR(1) DEFAULT 'N',
	NUM_SERVERS_UPLOADED_TO NUMBER(8) DEFAULT 0,
	NUM_SEND_ATTEMPTS NUMBER(8) DEFAULT 0,
	LAST_ATTEMPT_DATETIME DATE DEFAULT NULL,
	COURTEL_RESPONSE_SERVER_1 VARCHAR2(256),
	COURTEL_RESPONSE_SERVER_2 VARCHAR2(256),
	MESSAGE_TEXT VARCHAR2(256),
	LAST_UPDATE_DATE DATE,
	CREATION_DATE DATE,
	CREATED_BY VARCHAR2(50),
	LAST_UPDATED_BY VARCHAR2(50),
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
grant select, insert, update on AUD_XHB_COURTEL_LIST to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_COURTEL_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_COURTEL_LIST', 'AUD_XHB_COURTEL_LIST', 'Y');


/*	------------------------------------------------------------------
/*	CREATE sequence TABLE FOR XHB_COURTEL_LIST
*/	------------------------------------------------------------------

-- Create sequence table for XHB_COURTEL_LIST
create sequence XHIBIT.XHB_COURTEL_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_COURTEL_LIST_bir_tr.sql;
@@XHB_COURTEL_LIST_bur_tr.sql;

commit;
