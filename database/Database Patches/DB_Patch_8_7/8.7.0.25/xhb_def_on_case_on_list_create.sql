/*Ctx-1971*/ 
CREATE TABLE xhb_def_on_case_on_list
(DEF_ON_CASE_ON_LIST_ID NUMBER(8) NOT NULL
,DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL
,CASE_ON_LIST_ID NUMBER (8) NOT NULL
,CASE_ID NUMBER (8) NOT NULL
,OBS_IND VARCHAR2(1) 
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
COMMENT ON COLUMN xhb_def_on_case_on_list.DEF_ON_CASE_ON_LIST_ID IS 'PK. cannot be null';
COMMENT ON COLUMN xhb_def_on_case_on_list.DEFENDANT_ON_CASE_ID IS 'Displayed Sitting Number. FK to XHB_DEFENDANT_ON_CASE. cannot be null';
COMMENT ON COLUMN xhb_def_on_case_on_list.CASE_ON_LIST_ID IS 'FK to case_on_list. cannot be null';
COMMENT ON COLUMN xhb_def_on_case_on_list.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN xhb_def_on_case_on_list.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN xhb_def_on_case_on_list.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN xhb_def_on_case_on_list.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN xhb_def_on_case_on_list.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN xhb_def_on_case_on_list.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 

alter table xhb_def_on_case_on_list
  add constraint xhb_def_on_case_on_list_pk primary key (DEF_ON_CASE_ON_LIST_ID) 
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

ALTER TABLE xhb_def_on_case_on_list ADD (CONSTRAINT xhb_def_on_case_on_lst_doc_id FOREIGN KEY (defendant_on_case_id) REFERENCES xhb_defendant_on_case (defendant_on_case_id));
ALTER TABLE xhb_def_on_case_on_list ADD (CONSTRAINT xhb_def_on_case_on_lst_col_id FOREIGN KEY (case_on_list_id) REFERENCES xhb_case_on_list (case_on_list_id));
ALTER TABLE xhb_def_on_case_on_list ADD (CONSTRAINT xhb_def_on_case_on_lst_case_id FOREIGN KEY (case_id) REFERENCES xhb_case (case_id));
-- Grant/Revoke object privileges 
grant select, insert, update on xhb_def_on_case_on_list to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_DEF_ON_CASE_ON_LIST_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE aud_def_on_case_on_list
(DEF_ON_CASE_ON_LIST_ID NUMBER(8) NOT NULL
,DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL
,CASE_ON_LIST_ID NUMBER (8) NOT NULL
,CASE_ID NUMBER (8) NOT NULL
,OBS_IND VARCHAR2(1) 
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
COMMENT ON COLUMN aud_def_on_case_on_list.DEF_ON_CASE_ON_LIST_ID IS 'PK. cannot be null';
COMMENT ON COLUMN aud_def_on_case_on_list.DEFENDANT_ON_CASE_ID IS 'Displayed Sitting Number. FK to XHB_DEFENDANT_ON_CASE. cannot be null';
COMMENT ON COLUMN aud_def_on_case_on_list.CASE_ON_LIST_ID IS 'FK to case_on_list. cannot be null';
COMMENT ON COLUMN aud_def_on_case_on_list.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN aud_def_on_case_on_list.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN aud_def_on_case_on_list.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN aud_def_on_case_on_list.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN aud_def_on_case_on_list.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN aud_def_on_case_on_list.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 
-- Grant/Revoke object privileges 
grant select, insert, update on aud_def_on_case_on_list to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING 
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_DEF_ON_CASE_ON_LIST';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_DEF_ON_CASE_ON_LIST', 'AUD_DEF_ON_CASE_ON_LIST', 'Y');

@@xhb_def_on_case_on_list_bur_tr;
@@xhb_def_on_case_on_list_bir_tr;

commit;
 
