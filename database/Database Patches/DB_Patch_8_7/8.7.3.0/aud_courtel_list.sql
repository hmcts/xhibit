DROP TABLE AUD_XHB_COURTEL_LIST;
DROP TABLE AUD_COURTEL_LIST; 

/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR AUD_COURTEL_LIST
*/	------------------------------------------------------------------

CREATE TABLE AUD_COURTEL_LIST 
(
  COURTEL_LIST_ID NUMBER NOT NULL 
, XML_DOCUMENT_ID NUMBER NOT NULL 
, BLOB_ID NUMBER(10)
, FILENAME VARCHAR2(50) 
, SENT_TO_COURTEL VARCHAR2(1) 
, NUM_SERVERS_UPLOADED_TO NUMBER(8) DEFAULT 0 
, NUM_SEND_ATTEMPTS NUMBER(8) DEFAULT 0 
, LAST_ATTEMPT_DATETIME DATE DEFAULT NULL
, COURTEL_RESPONSE_SERVER_1 VARCHAR2(2000) 
, COURTEL_RESPONSE_SERVER_2 VARCHAR2(2000) 
, MESSAGE_TEXT VARCHAR2(256) 
, LAST_UPDATE_DATE DATE 
, CREATION_DATE DATE 
, CREATED_BY VARCHAR2(50) 
, LAST_UPDATED_BY VARCHAR2(50) 
, VERSION NUMBER DEFAULT 1
, insert_event VARCHAR2(1)
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
grant select, insert, update on AUD_COURTEL_LIST to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_COURTEL_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_COURTEL_LIST', 'AUD_COURTEL_LIST', 'Y');

@@xhb_courtel_list_bur_tr.sql;

COMMIT;