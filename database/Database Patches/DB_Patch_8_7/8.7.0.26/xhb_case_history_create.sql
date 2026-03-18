/*Ctx-1405*/ 
CREATE TABLE xhb_case_history
(CASE_HISTORY_ID NUMBER(8) NOT NULL
,CASE_TYPE VARCHAR2(1) NOT NULL
,COURT_ID NUMBER (8) NOT NULL
,CASE_NUMBER NUMBER (8) NOT NULL
,PSD_CT_CODE VARCHAR2(4) 
,COMMITTAL_DATE DATE
,REASON_DELETED VARCHAR2(255) NOT NULL 
,CASE_TITLE VARCHAR2(72)
,DATE_ARCHIVED DATE NOT NULL 
,SENT_FOR_TRIAL_DATE DATE
,LAST_UPDATE_DATE DATE NOT NULL 
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATED_BY VARCHAR2(30) NOT NULL 
,VERSION NUMBER(5)DEFAULT 1 NOT NULL 
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

alter table xhb_case_history
  add constraint xhb_case_history_pk primary key (CASE_HISTORY_ID) 
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
grant select, insert, update on xhb_case_history to PUBLIC;

-- Create sequence table
create sequence XHB_CASE_HISTORY_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE aud_case_history
(CASE_HISTORY_ID NUMBER(8) NOT NULL
,CASE_TYPE VARCHAR2(1) NOT NULL
,COURT_ID NUMBER (8) NOT NULL
,CASE_NUMBER NUMBER (8) NOT NULL
,PSD_CT_CODE VARCHAR2(4) 
,COMMITTAL_DATE DATE
,REASON_DELETED VARCHAR2(255) NOT NULL 
,CASE_TITLE VARCHAR2(72)
,DATE_ARCHIVED DATE NOT NULL 
,SENT_FOR_TRIAL_DATE DATE
,LAST_UPDATE_DATE DATE NOT NULL 
,CREATION_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATED_BY VARCHAR2(30) NOT NULL 
,VERSION NUMBER(5)DEFAULT 1 NOT NULL 
,insert_event VARCHAR2(1)
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
grant select, insert, update on aud_case_history to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING 
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_CASE_HISTORY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_CASE_HISTORY', 'AUD_CASE_HISTORY', 'Y');

@@xhb_case_history_bur_tr;
@@xhb_case_history_bir_tr;

commit;
 
