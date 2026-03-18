/*    ------------------------------------------------------------------
*     CREATE AUD_CPP_FORMATTING TABLE
*     Update TRIGGER
*/    ------------------------------------------------------------------

create table AUD_CPP_FORMATTING (
	CPP_FORMATTING_ID NUMBER(8) NOT NULL,
	STAGING_TABLE_ID NUMBER(8) NOT NULL,
	DATE_IN DATE NOT NULL,
	FORMAT_STATUS VARCHAR2(2) DEFAULT 'ND' NOT NULL,
	DOCUMENT_TYPE VARCHAR2(3) NOT NULL,
	COURT_ID NUMBER(8) NOT NULL,
	XML_DOCUMENT_CLOB_ID NUMBER(8) NOT NULL,
	ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND VARCHAR2(1),
	VERSION NUMBER(5) DEFAULT 1,
	LAST_UPDATE_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	insert_event VARCHAR2(1)
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

-- Grant/Revoke object privileges
grant select, insert, update on AUD_CPP_FORMATTING to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CPP_FORMATTING ';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CPP_FORMATTING', 'AUD_CPP_FORMATTING', 'Y');


/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGER
*/    ------------------------------------------------------------------

@@xhb_cppformatting_bur_tr.sql;

commit;
