/*
Author      : G Nagarajan
Date        : 11/11/2008
Description : This script will create tablespaces, schema and grants for DARTS schema
Usage       : This script should be run as DARTS

Version  Date       Author           Description
==============================================================================
1.0      11/11/08   G Nagarajan      Initial version
1.1      12/11/08   Paul Milner      create DARTS configuration table
1.2      18/11/08   Paul Milner      initial DARTS configuration setup
1.3      25/11/08   Paul Milner      Add darts.dump property to support DARTS stub test
1.4      28/11/08   Paul Milner      Add DAR_NEW_MESSAGES table (initial version)
1.5      22/12/08   G Nagarajan      Script was modified to remove the TSpace creation in CreateDARTS_dba.sql
1.6      16/02/09   G Nagarajan      Change the order or the create commands and the insert statements
==============================================================================
*/


set echo on
set term off
column filename new_value spool_filename
  select 'DARTSDB_CREATION_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename




/****************************************
 *
 *  Create DARTS config table
 *
 ***************************************/
create table DAR_DARTS_CONFIG
(
  DARTS_CONFIG_ID 	NUMBER not null,
  DARTS_PROPERTY_NAME 	VARCHAR2(255) not null,
  DARTS_PROPERTY_VALUE 	VARCHAR2(255) not null,
  LAST_UPDATE_DATE      DATE not null,
  CREATION_DATE         DATE not null,
  CREATED_BY            VARCHAR2(30) not null,
  LAST_UPDATED_BY       VARCHAR2(30) not null,
  VERSION               NUMBER(5) not null
)
tablespace DARTSD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 5M
    next 5M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
/
-- Grant/Revoke object privileges 
--grant select, insert, update, delete on DAR_DARTS_CONFIG to DARTS;

COMMIT;
/

/*
 * Add Primary Key constraint
 */
ALTER TABLE DAR_DARTS_CONFIG
       ADD (CONSTRAINT DAR_CONFIG_PK PRIMARY KEY (DARTS_PROPERTY_NAME ) 
       USING INDEX TABLESPACE DARTSX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));
/
CREATE TABLE AUD_DAR_DARTS_CONFIG TABLESPACE AUDIDARTS AS SELECT * FROM DAR_DARTS_CONFIG;
ALTER TABLE AUD_DAR_DARTS_CONFIG ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);
/

/*
 * Add Sequence
 */
CREATE SEQUENCE DAR_DARTS_CONFIG_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;
/

/*
 * INSERT to DAR_ table triggers 
 */
CREATE OR REPLACE TRIGGER DAR_DARTS_CONFIG_BIR_TR
  BEFORE INSERT
  ON DAR_DARTS_CONFIG
  FOR EACH ROW

BEGIN

  IF :NEW.DARTS_CONFIG_ID  IS NULL THEN

    SELECT DAR_DARTS_CONFIG_SEQ.NEXTVAL
    INTO   :NEW.DARTS_CONFIG_ID
    FROM   DUAL;

  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/



/*
 * UPDATES-DELETES to DAR_ table triggers 
 */
CREATE OR REPLACE TRIGGER DAR_DARTS_CONFIG_BUR_TR
  BEFORE UPDATE OR DELETE
  ON DAR_DARTS_CONFIG
  FOR EACH ROW

/* default body for DAR_DARTS_CONFIG_BUR_TR */
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY
    FROM   DUAL;


  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  INSERT INTO AUD_DAR_DARTS_CONFIG (
   	DARTS_CONFIG_ID,
  	DARTS_PROPERTY_NAME,
  	DARTS_PROPERTY_VALUE,
  	LAST_UPDATE_DATE,
  	CREATION_DATE,
  	CREATED_BY,
  	LAST_UPDATED_BY,
  	VERSION,
	INSERT_EVENT  
      )
    VALUES (
      	:old.DARTS_CONFIG_ID,
  	:old.DARTS_PROPERTY_NAME,
  	:old.DARTS_PROPERTY_VALUE,
  	:old.LAST_UPDATE_DATE,
  	:old.CREATION_DATE,
  	:old.CREATED_BY,
  	:old.LAST_UPDATED_BY,
 	:old.VERSION,
 	l_trig_event);


END;
/







/********************************************************************************
 *
 *  Create DARTS DAR_MESSAGE_STORE table
 *
 ********************************************************************************/
create table DAR_MESSAGE_STORE
(
  MESSAGE_ID	 	NUMBER not null,
  XHIBIT_MESSAGE_CODE	VARCHAR2(50) not null,
  EXISS_MESSAGE_CODE	VARCHAR2(50) not null,
  PAYLOAD		CLOB,
  STATUS_CODE	        VARCHAR2(20) not null,
  STATUS_DETAIL		VARCHAR2(500),	
  LAST_UPDATE_DATE      DATE not null,
  CREATION_DATE         DATE not null,
  CREATED_BY            VARCHAR2(30) not null,
  LAST_UPDATED_BY       VARCHAR2(30) not null,
  VERSION               NUMBER(5)
)
tablespace DARTSD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 50M
    next 50M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
/
-- Grant/Revoke object privileges 
--grant select, insert, update, delete on DAR_MESSAGE_STORE to DARTS;

COMMIT;
/

/*
 * Add Primary Key constraint
 */
ALTER TABLE DAR_MESSAGE_STORE
       ADD (CONSTRAINT DAR_MESSAGE_STORE_PK PRIMARY KEY (MESSAGE_ID) 
       USING INDEX TABLESPACE DARTSX
       STORAGE (INITIAL 2M
                NEXT 5M
                PCTINCREASE 0));
/

/*
 * Create Index
 *
 *  Does this need adding if the PK is indexed above?
 *
create index DAR_MSG_STORE_INDEX on DAR_MESSAGE_STORE (MESSAGE_Id)
  tablespace DARTSX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 50M
    next 50M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
*/


/*
 * Add Sequence
 */
CREATE SEQUENCE DAR_MESSAGE_STORE_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;
/

/*
 * INSERT to DAR_ table triggers 
 */
CREATE OR REPLACE TRIGGER DAR_MESSAGE_STORE_BIR_TR
  BEFORE INSERT
  ON DAR_MESSAGE_STORE
  FOR EACH ROW

BEGIN

  IF :NEW.MESSAGE_ID   IS NULL THEN

    SELECT DAR_MESSAGE_STORE_SEQ.NEXTVAL
    INTO   :NEW.MESSAGE_ID   
    FROM   DUAL;

  END IF;

  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

  SELECT SYSDATE,
         SYSDATE,
         1
  INTO   :NEW.LAST_UPDATE_DATE,
         :NEW.CREATION_DATE,
         :NEW.VERSION
  FROM   DUAL;

END;
/

/*********************************************************************/










/********************************************************************************
 *
 *  Create DARTS DAR_NEW_MESSAGES table
 *
 ********************************************************************************/
create table DAR_NEW_MESSAGES 
(
  MESSAGE_ID	 	NUMBER not null,
  XHIBIT_MESSAGE_CODE	VARCHAR2(50) not null,
  EXISS_MESSAGE_CODE	VARCHAR2(50) not null,
  PAYLOAD		CLOB,
  RETRY_COUNT           NUMBER not null,
  NEXT_RETRY_TIME       DATE not null,
  LAST_UPDATE_DATE      DATE not null,
  CREATION_DATE         DATE not null,
  CREATED_BY            VARCHAR2(30) not null,
  LAST_UPDATED_BY       VARCHAR2(30) not null,
  VERSION               NUMBER(5) 
)
tablespace DARTSD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 10M
    next 10M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
/
-- Grant/Revoke object privileges 
--grant select, insert, update, delete on DAR_NEW_MESSAGES to DARTS;

COMMIT;
/

/*
 * Add Primary Key constraint
 */
ALTER TABLE DAR_NEW_MESSAGES 
       ADD (CONSTRAINT DAR_NEW_MESSAGES_PK PRIMARY KEY (MESSAGE_ID) 
       USING INDEX TABLESPACE DARTSX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));
/
/*
 * Create Index
 *
 *  Does this need adding if the PK is indexed above?
 *
create index DAR_NEW_MESSAGES_INDEX on DAR_NEW_MESSAGES (MESSAGE_Id)
  tablespace DARTSX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 2M
    next 5M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
*/



/*
 * INSERT to DAR_ table triggers 
 */
CREATE OR REPLACE TRIGGER DAR_MESSAGE_STORE_AIR_TR
  AFTER INSERT
  ON DAR_MESSAGE_STORE
  FOR EACH ROW

BEGIN

 INSERT INTO DAR_NEW_MESSAGES(MESSAGE_ID,
                              XHIBIT_MESSAGE_CODE,
                              EXISS_MESSAGE_CODE,
                              PAYLOAD, 
                              RETRY_COUNT, 
                              NEXT_RETRY_TIME)
                              
                   VALUES(    :NEW.MESSAGE_ID,
                              :NEW.XHIBIT_MESSAGE_CODE,
                              :NEW.EXISS_MESSAGE_CODE,
                              :NEW.PAYLOAD,
                              0,
                              SYSDATE);
END;
/

CREATE OR REPLACE TRIGGER DAR_NEW_MESSAGES_BIR_TR
  BEFORE INSERT
  ON DAR_NEW_MESSAGES
  FOR EACH ROW

BEGIN


  IF ((:NEW.LAST_UPDATED_BY IS NULL) OR
      (:NEW.CREATED_BY IS NULL)) THEN

    SELECT SYS_CONTEXT('USERENV', 'SESSION_USER'),
           SYS_CONTEXT('USERENV', 'SESSION_USER')
    INTO   :NEW.LAST_UPDATED_BY,
           :NEW.CREATED_BY
    FROM   DUAL;

  END IF;

  IF(:NEW.CREATION_DATE is null)
  THEN
    SELECT SYSDATE,
           SYSDATE,
           1
    INTO   :NEW.LAST_UPDATE_DATE,
           :NEW.CREATION_DATE,
           :NEW.VERSION
    FROM   DUAL;
  END IF;

END;
/

/********************************************************************************
 *
 *  Set up initial DARTS configuration
 *
 ********************************************************************************/
insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'MAX_LOCK_ATTEMPTS', '3', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'BULK_COUNT', '10', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.doctypes', 'DL,NEWCASE,UPDCASE', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'cache.time', '60000', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.active', 'true', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.dump', 'true', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.repository', 'moj_darts', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.user', 'dmadmin', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.password', 'dmadmin', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.moduleName', 'darts', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.contextRoot', 'http://192.168.2.11:9080/service', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.retry.max.times', '3', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.retry.interval', '2000000', sysdate, sysdate, 'DARTS', 'DARTS', null);

insert into dar_darts_config (DARTS_CONFIG_ID, DARTS_PROPERTY_NAME, DARTS_PROPERTY_VALUE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION)
values (null, 'darts.conn_retry_wait', '450000', sysdate, sysdate, 'DARTS', 'DARTS', null);


COMMIT;

spool off
