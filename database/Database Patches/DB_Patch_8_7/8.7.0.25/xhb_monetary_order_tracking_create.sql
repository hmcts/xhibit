/*Ctx-1912*/
CREATE TABLE XHB_MONETARY_ORDER_TRACKING
(MONETARY_ORDER_TRACKING_ID NUMBER(8) NOT NULL
,CASE_ID NUMBER(8) NOT NULL 
,DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL
,ORDER_DATE DATE
,FINED NUMBER(10) 
,COMPENSATION NUMBER(10)
,COSTS NUMBER(10)
,COLLECT_MAGISTRATES_COURT_ID NUMBER(8)
,ACKNOWLEDGEMENT_DATE DATE
,OBS_IND VARCHAR2(1)
,LAST_UPDATE_DATE DATE NOT NULL 
,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
,CREATION_DATE DATE NOT NULL 
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
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.MONETARY_ORDER_TRACKING_ID IS 'PK. cannot be null';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.CASE_ID IS 'The case that this entry is linked to.  Cannot be null';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.DEFENDANT_ON_CASE_ID IS 'The defendant on case that this entry is linked to.  Cannot be null';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.ORDER_DATE IS 'The date the monetary order was created.  Cannot be null';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.FINED IS 'The total amount of fines in the order';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.COMPENSATION IS 'The total amount of compensation in the order';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.COSTS IS 'The total amount of costs in the order';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.COLLECT_MAGISTRATES_COURT_ID IS 'The primary of the xhb_ref_court entry for the collecting court';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.ACKNOWLEDGEMENT_DATE IS 'The date the monetary order was acknowledged';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN XHB_MONETARY_ORDER_TRACKING.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Create/Recreate primary, unique and foreign key constraints 
ALTER TABLE XHB_MONETARY_ORDER_TRACKING ADD (CONSTRAINT XHB_MOT_CASE_ID_FK FOREIGN KEY (CASE_ID) REFERENCES xhb_case (CASE_ID));
ALTER TABLE XHB_MONETARY_ORDER_TRACKING ADD (CONSTRAINT XHB_MOT_DEF_ON_CASE_ID_FK FOREIGN KEY (DEFENDANT_ON_CASE_ID) REFERENCES XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID));
ALTER TABLE XHB_MONETARY_ORDER_TRACKING ADD (CONSTRAINT XHB_MOT_COLL_MAG_COURT_ID_FK FOREIGN KEY (COLLECT_MAGISTRATES_COURT_ID) REFERENCES XHB_REF_COURT (REF_COURT_ID));

alter table XHB_MONETARY_ORDER_TRACKING
  add constraint XHB_MONETARY_ORDER_TRACKING_PK primary key (MONETARY_ORDER_TRACKING_ID) 
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
grant select, insert, update on XHB_MONETARY_ORDER_TRACKING to PUBLIC;

-- Create sequence table for XHB_DMI_CAD_RUN_LOG
create sequence XHB_MONETARY_ORDER_TRACK_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;

/******Create audit table***************/
CREATE TABLE AUD_MONETARY_ORDER_TRACKING
 (MONETARY_ORDER_TRACKING_ID NUMBER(8) NOT NULL
 ,CASE_ID NUMBER(8) NOT NULL 
 ,DEFENDANT_ON_CASE_ID NUMBER(8) NOT NULL
 ,ORDER_DATE DATE 
 ,FINED NUMBER(10)
 ,COMPENSATION NUMBER(10)
 ,COSTS NUMBER(10)
 ,COLLECT_MAGISTRATES_COURT_ID NUMBER(8)
 ,ACKNOWLEDGEMENT_DATE DATE
 ,OBS_IND VARCHAR2(1)
 ,LAST_UPDATE_DATE DATE NOT NULL 
 ,LAST_UPDATED_BY VARCHAR2(30) NOT NULL 
 ,CREATION_DATE DATE NOT NULL 
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
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.MONETARY_ORDER_TRACKING_ID IS 'PK. cannot be null';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.CASE_ID IS 'The case that this entry is linked to.  Cannot be null';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.DEFENDANT_ON_CASE_ID IS 'The defendant on case that this entry is linked to.  Cannot be null';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.ORDER_DATE IS 'The date the monetary order was created.  Cannot be null';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.FINED IS 'The total amount of fines in the order';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.COMPENSATION IS 'The total amount of compensation in the order';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.COSTS IS 'The total amount of costs in the order';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.COLLECT_MAGISTRATES_COURT_ID IS 'The primary of the xhb_ref_court entry for the collecting court';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.OBS_IND IS 'Obsolete indicator';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.ACKNOWLEDGEMENT_DATE IS 'The date the monetary order was acknowledged';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.LAST_UPDATE_DATE IS 'Standard field. The date the last time this row was updated.';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.CREATION_DATE IS 'Standard field. The date this row was created.';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.CREATED_BY IS 'Standard field. Which user created this row.';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.LAST_UPDATED_BY IS 'Standard field. Which user last updated this row.';
COMMENT ON COLUMN AUD_MONETARY_ORDER_TRACKING.VERSION IS 'Standard field. Every time this row is changed the version will increment.'; 

-- Grant/Revoke object privileges 
grant select, insert, update on AUD_MONETARY_ORDER_TRACKING to PUBLIC;

/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_MONETARY_ORDER_TRACKING';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_MONETARY_ORDER_TRACKING', 'AUD_MONETARY_ORDER_TRACKING', 'Y');

@@xhb_monetary_order_trac_bur_tr;
@@xhb_monetary_order_trac_bir_tr;

commit;
 
