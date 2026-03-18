/*    ------------------------------------------------------------------
*     CREATE XHB_BW_HISTORY TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_BW_HISTORY ( 
	BW_HISTORY_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	BW_ISSUE_DATE DATE,
	BW_END_DATE   DATE,
	BC_STATUS_BW_ISSUED VARCHAR2(2),
	BC_STATUS_BW_ENDED  VARCHAR2(2),
	WITHDRAWN VARCHAR2(1),
	ABSCONDING VARCHAR2(30),
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE,
	CREATION_DATE    DATE,
	LAST_UPDATED_BY VARCHAR2(30),
	CREATED_BY 	VARCHAR2(30),
	VERSION NUMBER(5) DEFAULT 1
)
TABLESPACE XHIBITD
	pctfree 10
	initrans 1
	maxtrans 255
	storage
	(
		initial 64k
		minextents 1
		maxextents unlimited
	);

-- cREATE PRIMARY KEY CONSTRAINTS
ALTER TABLE XHB_BW_HISTORY ADD CONSTRAINT XHB_BW_HISTORY_PK PRIMARY KEY (BW_HISTORY_ID)
USING INDEX
TABLESPACE XHIBITD 
	pctfree 10
	initrans 2
	maxtrans 255
	storage
	(
		initial 64k
		minextents 1
		maxextents unlimited
	);

-- CREATE FOREIGN KEY CONSTRAINTS
ALTER TABLE XHB_BW_HISTORY ADD (CONSTRAINT BW_DEFENDANT_ON_CASE_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID));

-- CREATE SEQUENCE TABLE FOR XHB_BW_HISTORY
CREATE SEQUENCE  XHB_BW_HISTORY_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999999 START WITH 1 INCREMENT BY 1 NOCACHE;

-- GRANT/REVOKE OBJECT PRIVILEGES
grant select, insert, update on XHB_BW_HISTORY to PUBLIC;



/*	------------------------------------------------------------------
/*	CREATE AUDIT TABLE FOR BW_HISTORY
*/	------------------------------------------------------------------

CREATE TABLE AUD_BW_HISTORY 
(
	BW_HISTORY_ID NUMBER(8) NOT NULL,
	DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL,
	BW_ISSUE_DATE DATE,
	BW_END_DATE   DATE,
	BC_STATUS_BW_ISSUED VARCHAR2(2),
	BC_STATUS_BW_ENDED  VARCHAR2(2),
	WITHDRAWN VARCHAR2(1),
	ABSCONDING VARCHAR2(30),
	OBS_IND VARCHAR2(1),
	LAST_UPDATE_DATE DATE,
	CREATION_DATE    DATE,
	LAST_UPDATED_BY VARCHAR2(30),
	CREATED_BY 	VARCHAR2(30),
	VERSION NUMBER(5) DEFAULT 1,
	insert_event VARCHAR2(1)
)
TABLESPACE XHIBITD
	pctfree 10
	initrans 1
	maxtrans 255
	storage
	(
		initial 64k
		minextents 1
		maxextents unlimited
	);

-- GRANT/REVOKE OBJECT PRIVILEGES
grant select, insert, update on AUD_BW_HISTORY to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_BW_HISTORY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_BW_HISTORY', 'AUD_BW_HISTORY', 'Y');


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------
@@xhb_bw_history_bir_tr.sql;
/
@@xhb_bw_history_bur_tr.sql;
/

