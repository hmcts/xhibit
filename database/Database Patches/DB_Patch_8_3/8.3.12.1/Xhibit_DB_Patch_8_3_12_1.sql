/*
 * Filename:    Xhibit_DB_Patch_8_3_12_1.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE        WHO        COMMENT
 * ----         ---         -------
 *  22/02/2010    J Powell    Changes for DB release 8.3.12.1
 *  24/02/2010    W Fardell   Added validation 	queues
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_3_12_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename

/*
 * Add the xhibit validation queues.
 */

DECLARE
BEGIN
    dbms_aqadm.CREATE_queue_table (
        queue_table => 'xhb_validation_queue_t',
        queue_payload_type => 'SYS.AQ$_JMS_TEXT_MESSAGE',
        compatible => '10.0',
        comment => 'Creating Xhibit validation request queue table.');
END;
/
SHOW ERRORS;

DECLARE
BEGIN
    dbms_aqadm.CREATE_queue (
        queue_name => 'xhb_validation_queue',
        queue_table => 'xhb_validation_queue_t',
        max_retries => 10,
        retry_delay => 60,
        comment => 'Creating Xhibit validation request queue.');
END;
/
SHOW ERRORS;

DECLARE
BEGIN
        dbms_aqadm.start_queue (
                queue_name => 'xhb_validation_queue');
END;
/
SHOW ERRORS;

/*
 * Add the validation tables.
 */




/*
 *CREATE XHB_VALIDATION TABLE
 */
create table XHB_VALIDATION
(
	VALIDATION_ID 	NUMBER(16),
	CODE		VARCHAR2(10),
	CLOB_DATA	CLOB,
	SCHEMA_NAME	VARCHAR2(255),
	STATUS		VARCHAR2(1),
	DETAILS		VARCHAR2(4000)
);


/*
 * Create primary keys and indexes for xhb_validation
 */
alter table XHB_VALIDATION
  add primary key (VALIDATION_ID);
CREATE INDEX XHB_VALIDATION_STATUS_IDX ON XHB_VALIDATION(STATUS);

/*
 * CREATE XHB_VALIDATION_CJI TABLE
 */
create table XHB_VALIDATION_CJI
(
	VALIDATION_ID			NUMBER(16),
  	EXT_DOCUMENT_ID           	VARCHAR2(21),
  	DOCUMENT_TYPE_ID          	NUMBER(8) not null,
  	STATUS_ID                 	NUMBER(8) not null,
  	STYLESHEET_NAME           	VARCHAR2(255),
  	CREATION_DATE             	DATE,
  	BITS_REGISTERING_LOCATION 	VARCHAR2(50),
  	BITS_DOCUMENT_NAME        	VARCHAR2(50),
  	BITS_DOCUMENT_DESCRIPTION 	VARCHAR2(200),
  	BITS_DOCUMENT_COMMENT     	VARCHAR2(512),
  	BITS_REGISTRATION_DATE    	DATE,
  	SRC_DOCUMENT_KEY          	VARCHAR2(255)
);

/*
 * Create Foreign Key
 */
alter table XHB_VALIDATION_CJI
  add constraint XHB_VALIDATION_CJI foreign key (VALIDATION_ID)
  references XHB_VALIDATION (VALIDATION_ID);

/*
 * CREATE XHB_VALIDATION_EXISS TABLE
 */
create table XHB_VALIDATION_EXISS
(
	VALIDATION_ID		NUMBER(16),
  	TYPE_ID        		NUMBER(8) not null,
  	IDENTIFIER     		VARCHAR2(255),
  	CREST_COURT_ID 		VARCHAR2(3),
  	DESCRIPTION    		VARCHAR2(255),
  	ITEM_CREATED   		DATE not null,
  	ITEM_EXPIRES   		DATE
);

/*
 * Add foreign key
 */
alter table XHB_VALIDATION_EXISS
  add constraint XHB_VALIDATION_EXISS foreign key (VALIDATION_ID)
  references XHB_VALIDATION (VALIDATION_ID);


/*
 * Create Sequence
 */
create sequence XHB_VALIDATION_SEQ
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;


/*
 * Add the validation triggers.
 */

CREATE OR REPLACE TRIGGER xhb_validation_status_u_rv
AFTER UPDATE OF status ON xhb_validation FOR EACH ROW
DECLARE
    l_message_id RAW(16);
BEGIN
    IF :new.status = 'N' THEN
        l_message_id := xhb_validation_pkg.request_validation(:new.validation_id, :new.schema_name, :new.clob_data);
    END IF;
END;
/
SHOW ERRORS;

CREATE OR REPLACE TRIGGER xhb_validation_status_i_rv
AFTER INSERT ON xhb_validation FOR EACH ROW
DECLARE
    l_message_id RAW(16);
BEGIN
    IF :new.status = 'N' THEN
        l_message_id := xhb_validation_pkg.request_validation(:new.validation_id, :new.schema_name, :new.clob_data);
    END IF;
END;
/
SHOW ERRORS;

/*
 * Create Triggers
 */
create or replace trigger XHB_VALIDATION_BIR_TR
    before insert on XHB_VALIDATION
    for each row
begin
    if :new.validation_id is null then
        select xhb_validation_seq.nextval
        into  :new.validation_id
        from   dual;
    end if;
end;
/
CREATE OR REPLACE TRIGGER xhb_validation_status_i_wsm
AFTER INSERT ON xhb_validation FOR EACH ROW
DECLARE
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_VALIDATION',
                                     'STATUS',
                                     :new.status,
                                     'INSERT',
                                     :new.validation_id);
END;
/
CREATE OR REPLACE TRIGGER xhb_validation_status_u_wsm
AFTER UPDATE OF status ON xhb_validation FOR EACH ROW
DECLARE
BEGIN
    wmb_message_pkg.send_wmb_message('XHIBIT',
                                     'XHB_VALIDATION',
                                     'STATUS',
                                     :new.status,
                                     'UPDATE',
                                     :new.validation_id);
END;

/*
 * Insert Standing data for routing the new database triggers
 */
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_VALUE,OPERATION,QUEUE_NAME)
	VALUES ('XHIBIT','XHB_VALIDATION','STATUS','V','INSERT','XMLROUTING.IN');
INSERT INTO WMB_MESSAGE_ROUTE (SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,COLUMN_VALUE,OPERATION,QUEUE_NAME)
	VALUES ('XHIBIT','XHB_VALIDATION','STATUS','V','UPDATE','XMLROUTING.IN');
COMMIT;

/*
 * Create Audit Tables
 */

create table AUD_VALIDATION
(
	VALIDATION_ID 	NUMBER(16),
	CODE		VARCHAR2(10),
	CLOB_DATA	CLOB,
	SCHEMA_NAME	VARCHAR2(255),
	STATUS		VARCHAR2(1),
	DETAILS		VARCHAR2(4000),
	INSERT_EVENT 	VARCHAR2(1) NOT NULL
);
/
create table AUD_VALIDATION_CJI
(
	VALIDATION_ID			NUMBER(16),
  	EXT_DOCUMENT_ID           	VARCHAR2(21),
  	DOCUMENT_TYPE_ID          	NUMBER(8) not null,
  	STATUS_ID                 	NUMBER(8) not null,
  	STYLESHEET_NAME           	VARCHAR2(255),
  	CREATION_DATE             	DATE,
  	BITS_REGISTERING_LOCATION 	VARCHAR2(50),
  	BITS_DOCUMENT_NAME        	VARCHAR2(50),
  	BITS_DOCUMENT_DESCRIPTION 	VARCHAR2(200),
  	BITS_DOCUMENT_COMMENT     	VARCHAR2(512),
  	BITS_REGISTRATION_DATE    	DATE,
  	SRC_DOCUMENT_KEY          	VARCHAR2(255),
	INSERT_EVENT			VARCHAR2(1) NOT NULL
);


create table AUD_VALIDATION_EXISS
(
	VALIDATION_ID		NUMBER(16),
  	TYPE_ID        		NUMBER(8) not null,
  	IDENTIFIER     		VARCHAR2(255),
  	CREST_COURT_ID 		VARCHAR2(3),
  	DESCRIPTION    		VARCHAR2(255),
  	ITEM_CREATED   		DATE not null,
  	ITEM_EXPIRES   		DATE,
	INSERT_EVENT		VARCHAR2(1) NOT NULL	
);


/*
 * Create Audit Triggers
 */

CREATE OR REPLACE TRIGGER XHB_VALIDATION_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_VALIDATION   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_VALIDATION') = 1) THEN

    INSERT INTO AUD_VALIDATION
    (
        VALIDATION_ID, 	
	CODE,		
	CLOB_DATA,	
	SCHEMA_NAME,	
	STATUS,		
	DETAILS,		
	INSERT_EVENT
    )
    VALUES 
    (
	:OLD.VALIDATION_ID,
	:OLD.CODE,		
	:OLD.CLOB_DATA,	
	:OLD.SCHEMA_NAME,	
	:OLD.STATUS,		
	:OLD.DETAILS,		
	l_trig_event
    );
  END IF;

END;


CREATE OR REPLACE TRIGGER XHB_VALIDATION_CJI_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_VALIDATION_CJI   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_VALIDATION_CJI') = 1) THEN

    INSERT INTO AUD_VALIDATION_CJI    
    VALUES 
    (
	:OLD.VALIDATION_ID,			
  	:OLD.EXT_DOCUMENT_ID,           	
  	:OLD.DOCUMENT_TYPE_ID,          	
  	:OLD.STATUS_ID,                 	
  	:OLD.STYLESHEET_NAME,           	
  	:OLD.CREATION_DATE,             	
  	:OLD.BITS_REGISTERING_LOCATION, 	
  	:OLD.BITS_DOCUMENT_NAME,        	
  	:OLD.BITS_DOCUMENT_DESCRIPTION, 	
  	:OLD.BITS_DOCUMENT_COMMENT,     	
  	:OLD.BITS_REGISTRATION_DATE,    	
  	:OLD.SRC_DOCUMENT_KEY,          	
	l_trig_event
    );
  END IF;

END;

CREATE OR REPLACE TRIGGER XHB_VALIDATION_EXISS_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHIBIT.XHB_VALIDATION_EXISS   FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;
BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_VALIDATION_EXISS') = 1) THEN

    INSERT INTO AUD_VALIDATION_EXISS   
    VALUES 
    (
	:OLD.VALIDATION_ID,		
  	:OLD.TYPE_ID,        		
  	:OLD.IDENTIFIER,     		
  	:OLD.CREST_COURT_ID, 		
  	:OLD.DESCRIPTION,    		
  	:OLD.ITEM_CREATED,   		
  	:OLD.ITEM_EXPIRES,   		
	l_trig_event
    );
  END IF;
END;	

/*
 * Turn on Auditing
 */
INSERT INTO XHB_SYS_AUDIT (TABLE_TO_aUDIT,AUDIT_TABLE,AUDITABLE) VALUES ('XHB_VALIDATION','AUD_VALIDATION','Y');
INSERT INTO XHB_SYS_AUDIT (TABLE_TO_aUDIT,AUDIT_TABLE,AUDITABLE) VALUES ('XHB_VALIDATION_CJI','AUD_VALIDATION_CJI','Y');
INSERT INTO XHB_SYS_AUDIT (TABLE_TO_aUDIT,AUDIT_TABLE,AUDITABLE) VALUES ('XHB_VALIDATION_EXISS','AUD_VALIDATION_EXISS','Y');
COMMIT

/
@Polling_Xhb_Config_Props.sql



/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.3.12.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.3.12.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.3.12.1', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.3.12.1', sysdate, 'RELEASE', 'Mercator', 4); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'BROKER', '8.3.12.1', sysdate, 'CSH Broker Components', 'Mercator', 5); 

/

COMMIT;

spool off


