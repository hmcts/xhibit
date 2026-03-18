/*    ------------------------------------------------------------------
*     CREATE XHB_REF_LISTING_DATA TABLE
*     CREATE TRIGGERS
*/    ------------------------------------------------------------------

create table XHB_REF_LISTING_DATA
(
	REF_LISTING_DATA_ID NUMBER(8) NOT NULL,
	REF_DATA_TYPE VARCHAR2(50),
	REF_DATA_VALUE VARCHAR2(100),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1
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
alter table XHB_REF_LISTING_DATA
  add constraint XHB_REF_LISTING_DATA_PK primary key (REF_LISTING_DATA_ID) 
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
grant select, insert, update on XHB_REF_LISTING_DATA to PUBLIC;




/*	------------------------------------------------------------------
* 	CREATE AUDIT TABLE FOR XHB_REF_LISTING_DATA
*/	------------------------------------------------------------------

create table AUD_REF_LISTING_DATA
(
	REF_LISTING_DATA_ID NUMBER(8) NOT NULL,
	REF_DATA_TYPE VARCHAR2(50),
	REF_DATA_VALUE VARCHAR2(100),
	CREATED_BY VARCHAR2(30) NOT NULL,
	LAST_UPDATED_BY VARCHAR2(30) NOT NULL,
	CREATION_DATE DATE NOT NULL,
	LAST_UPDATE_DATE DATE NOT NULL,
	VERSION NUMBER DEFAULT 1,
 	INSERT_EVENT VARCHAR2(1)
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
grant select, insert, update on AUD_REF_LISTING_DATA to PUBLIC;



/*    ------------------------------------------------------------------
*     CREATE ENTRY TO ENABLE AUDITING
*/    ------------------------------------------------------------------

DELETE XHB_SYS_AUDIT WHERE TABLE_TO_AUDIT = 'XHB_REF_LISTING_DATA';

INSERT INTO XHB_SYS_AUDIT VALUES(NULL, 'XHB_REF_LISTING_DATA', 'AUD_REF_LISTING_DATA', 'Y');


/*	------------------------------------------------------------------
* 	CREATE sequence TABLE FOR XHB_REF_LISTING_DATA
*/	------------------------------------------------------------------

-- Create sequence table for XHB_REF_LISTING_DATA
create sequence XHB_REF_LISTING_DATA_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


/*    ------------------------------------------------------------------
*     CREATE DATABASE TRIGGERS
*/    ------------------------------------------------------------------

@@xhb_ref_listing_data_bir_tr.sql;
@@xhb_ref_listing_data_bur_tr.sql;

commit;
