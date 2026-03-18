/*feature/cpp-109*/

/*    ------------------------------------------------------------------
*     CREATE XHB_CPP_LIST_TABLE
*/    ------------------------------------------------------------------

CREATE TABLE AUD_CPP_LIST (
	CPP_LIST_ID NUMBER(8) NOT NULL,
	COURT_CODE NUMBER(3) NOT NULL,
	LIST_TYPE VARCHAR2(1) NOT NULL,
	TIME_LOADED DATE NOT NULL,
	LIST_START_DATE DATE NOT NULL,
	LIST_END_DATE DATE NOT NULL,
	LIST_CLOB_ID NUMBER(8),
	MERGED_CLOB_ID NUMBER(8),
	STATUS VARCHAR2(2),
	ERROR_MESSAGE VARCHAR2(4000),
	OBS_IND VARCHAR2 (1),
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	CREATED_BY VARCHAR2(30) NOT NULL,
	VERSION NUMBER(5) DEFAULT 1 NOT NULL,
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
grant select, insert, update on AUD_CPP_LIST to PUBLIC;


/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CPP_LIST ';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CPP_LIST ', 'AUD_CPP_LIST', 'Y');



/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@XHB_CPP_LIST_bur_tr.sql;

commit;
