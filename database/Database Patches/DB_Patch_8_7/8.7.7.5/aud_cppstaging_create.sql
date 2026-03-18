/*    ------------------------------------------------------------------
*     CREATE AUD_CPP_STAGING_INBOUND TABLE
*     Update TRIGGER
*/    ------------------------------------------------------------------

create table AUD_CPP_STAGING_INBOUND (
	CPP_STAGING_INBOUND_ID NUMBER(8) NOT NULL,
	DOCUMENT_NAME VARCHAR2(50) NOT NULL,
	COURT_CODE VARCHAR2(3) NOT NULL,
	DOCUMENT_TYPE VARCHAR2(2) NOT NULL,
	TIME_LOADED DATE NOT NULL,
	CLOB_ID NUMBER(10) NOT NULL,
	VALIDATION_STATUS VARCHAR2(2) NOT NULL,
	ACKNOWLEDGMENT_STATUS VARCHAR2(2),
	PROCESSING_STATUS VARCHAR2(2),
	VALIDATION_ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND vARCHAR2(1),
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1,
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
grant select, insert, update on AUD_CPP_STAGING_INBOUND to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CPP_STAGING_INBOUND ';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CPP_STAGING_INBOUND', 'AUD_CPP_STAGING_INBOUND', 'Y');


/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGER
*/    ------------------------------------------------------------------

@@xhb_cppstaging_bur_tr.sql;

commit;
