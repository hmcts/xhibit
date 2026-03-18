/*    ------------------------------------------------------------------
*     CREATE AUD_CPP_FORMATTING_MERGE TABLE
*     Update TRIGGER
*/    ------------------------------------------------------------------

create table AUD_CPP_FORMATTING_MERGE (
	CPP_FORMATTING_MERGE_ID NUMBER(8) NOT NULL,
	CPP_FORMATTING_ID NUMBER(8) NOT NULL,
	FORMATTING_ID NUMBER(8) NOT NULL,
	XHIBIT_CLOB_ID NUMBER(10),
	COURT_ID NUMBER(8) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LANGUAGE VARCHAR2(2),
	OBS_IND vARCHAR2(1),
	VERSION NUMBER(5) DEFAULT 1,
	LAST_UPDATE_DATE DATE NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
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
grant select, insert, update on AUD_CPP_FORMATTING_MERGE to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CPP_FORMATTING_MERGE ';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CPP_FORMATTING_MERGE', 'AUD_CPP_FORMATTING_MERGE', 'Y');



/*    ------------------------------------------------------------------
*     UPDATE DATABASE TRIGGER
*/    ------------------------------------------------------------------

@@xhb_cppformattingmerge_bur_tr.sql;

commit;
