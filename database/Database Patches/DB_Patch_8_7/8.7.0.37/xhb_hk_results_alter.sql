/*ctx-2704*/
alter table xhb_hk_results
  add constraint xhb_hk_results_pk primary key (HK_RUN_ID) 
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

ALTER TABLE xhb_hk_results MODIFY HK_RUN_ID NOT NULL NOVALIDATE;

--Alter xhb_hk2_results
--Create the new PK which will implicitly create the constraint and index
alter table xhb_hk2_results
  add constraint xhb_hk2_results_pk primary key (HK2_RUN_ID) 
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

ALTER TABLE xhb_hk2_results MODIFY HK2_RUN_ID NOT NULL NOVALIDATE;

COMMIT;