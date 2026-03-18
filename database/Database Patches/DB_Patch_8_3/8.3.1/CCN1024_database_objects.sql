-- Create sequence 
create sequence AUD_USER_LOGINS_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
nocache;


-- Create table
create table AUD_USER_LOGINS
(
  ID             NUMBER not null,
  USER_ID        VARCHAR2(20) not null,
  DATE_LOGGED_IN DATE not null,
  TERMINAL_ID    VARCHAR2(20) not null,
  LOGGED_IN      CHAR(1)
)
tablespace USERS
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
alter table AUD_USER_LOGINS
  add primary key (ID)
  using index 
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );


--Create before update trigger
@AUD_USER_LOGINS_BIR_TR

--Create index
CREATE INDEX AUD_USER_LOGINS_USERID ON AUD_USER_LOGINS(USER_ID);
