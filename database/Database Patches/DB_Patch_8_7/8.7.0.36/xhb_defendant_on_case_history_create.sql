/*Ctx-2576*/ 
CREATE TABLE xhb_defendant_on_case_history
(DEFENDANT_ON_CASE_HISTORY_ID NUMBER(8) NOT NULL
,DEFENDANT_HISTORY_ID NUMBER(8) NOT NULL
,CASE_HISTORY_ID NUMBER (8) NOT NULL
,DEFENDANT_NUMBER NUMBER (8)
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
  
--Field Comments
COMMENT ON COLUMN xhb_defendant_on_case_history.DEFENDANT_ON_CASE_HISTORY_ID IS 'PK. cannot be null';
COMMENT ON COLUMN xhb_defendant_on_case_history.DEFENDANT_HISTORY_ID IS 'The PK of the related xhb_defendant_history_record for the defendant that has been deleted';
COMMENT ON COLUMN xhb_defendant_on_case_history.CASE_HISTORY_ID IS 'The PK of the related xhb_case_history record';
COMMENT ON COLUMN xhb_defendant_on_case_history.defendant_number IS 'The defendant_number from the deleted xhb_defendant_on_case record';

-- Create/Recreate primary, unique and foreign key constraints 

ALTER TABLE xhb_defendant_on_case_history ADD CONSTRAINT CASE_HISTORY_ID_FK FOREIGN KEY (CASE_HISTORY_ID) REFERENCES xhb_case_history(CASE_HISTORY_ID);
ALTER TABLE xhb_defendant_on_case_history ADD CONSTRAINT DEFENDANT_HISTORY_ID_FK FOREIGN KEY (DEFENDANT_HISTORY_ID) REFERENCES xhb_defendant_history(DEFENDANT_HISTORY_ID);

alter table xhb_defendant_on_case_history
  add constraint xhb_def_on_case_hist_pk primary key (DEFENDANT_ON_CASE_HISTORY_ID) 
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
grant select, insert, update on xhb_defendant_on_case_history to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence xhb_defendant_on_case_hist_seq
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE aud_defendant_on_case_history
(DEFENDANT_ON_CASE_HISTORY_ID NUMBER(8) NOT NULL
,DEFENDANT_HISTORY_ID NUMBER(8) NOT NULL
,CASE_HISTORY_ID NUMBER (8) NOT NULL
,DEFENDANT_NUMBER NUMBER (8)
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
  
--Field Comments
COMMENT ON COLUMN aud_defendant_on_case_history.DEFENDANT_ON_CASE_HISTORY_ID IS 'PK. cannot be null';
COMMENT ON COLUMN aud_defendant_on_case_history.DEFENDANT_HISTORY_ID IS 'The PK of the related xhb_defendant_history_record for the defendant that has been deleted';
COMMENT ON COLUMN aud_defendant_on_case_history.CASE_HISTORY_ID IS 'The PK of the related xhb_case_history record';
COMMENT ON COLUMN aud_defendant_on_case_history.defendant_number IS 'The defendant_number from the deleted xhb_defendant_on_case record';

-- Grant/Revoke object privileges 
grant select, insert, update on aud_defendant_on_case_history to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING 
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_DEFENDANT_ON_CASE_HISTORY';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_DEFENDANT_ON_CASE_HISTORY', 'AUD_DEFENDANT_ON_CASE_HISTORY', 'Y');

@@xhb_def_on_case_hist_bur_tr;
@@xhb_def_on_case_hist_bir_tr;

commit;
 
