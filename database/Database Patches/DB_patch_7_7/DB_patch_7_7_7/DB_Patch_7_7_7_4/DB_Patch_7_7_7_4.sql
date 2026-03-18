/*
 * Filename:    DB_Patch_7_7_7_4.sql
 *
 * System:      System Test, Development Systems
 *              Integration 1 & 2, Merc Dev development
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
   07/06/2006   K Shah			Changes for release 7.7.7.4
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'DBPATCH_7_7_7_4_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

/*
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

CREATE TABLE XHB_EMAIL2
(
  MAIL_ID            NUMBER(8)			        NOT NULL,
  RECIPIENT          VARCHAR2(255),
  SUBJECT            VARCHAR2(255)         	NOT NULL,
  SENDER             VARCHAR2(255)         	NOT NULL,
  STATUS             VARCHAR2(3)           	NOT NULL,
  ATTEMPTS           NUMBER(2)             	DEFAULT 0,
  CREATION_TIME      DATE                  	NOT NULL,
  MUSTRECEIVE        VARCHAR2(2)           	NOT NULL,
  REJECTTIME         DATE,
  REASON             VARCHAR2(50),
  COURT_ID           NUMBER(8),
  EMAIL_TO           VARCHAR2(255),
  COMPANY            VARCHAR2(255),
  ATTACHMENT         VARCHAR2(1)           	DEFAULT 'Y',
  MIME_TYPE          VARCHAR2(3)           	DEFAULT 'PDF'	NOT NULL,
  MIME_BODY_BLOB_ID  NUMBER(10)            	NOT NULL,
  EMAIL_PROTOCOL     VARCHAR2(1)           	NOT NULL,
  SERVER_ID          NUMBER(2),
  CREATION_DATE      DATE                  	NOT NULL,
  CREATED_BY         VARCHAR2(30)          	NOT NULL,
  LAST_UPDATE_DATE   DATE                  	NOT NULL,
  LAST_UPDATED_BY    VARCHAR2(30)          	NOT NULL,
  VERSION            NUMBER(5)
)
TABLESPACE XHIBITD;


ALTER TABLE XHB_EMAIL2 
  ADD CONSTRAINT EMAIL2_PK 
  PRIMARY KEY (MAIL_ID)
    USING INDEX 
    TABLESPACE XHIBITX;


ALTER TABLE XHB_EMAIL2 ADD (
  CONSTRAINT EMAIL2_COURT_ID_FK FOREIGN KEY (COURT_ID) 
    REFERENCES XHB_COURT (COURT_ID));

ALTER TABLE XHB_EMAIL2 ADD (
  CONSTRAINT XHB_EMAIL2_MIMEBODY_BLOB_ID_FK FOREIGN KEY (MIME_BODY_BLOB_ID) 
    REFERENCES XHB_BLOB (BLOB_ID));


CREATE INDEX XHIBIT.XHB_EMAIL2_REC_SUB_IDX
 ON XHIBIT.XHB_EMAIL2(RECIPIENT, SUBJECT)
    TABLESPACE XHIBITX;

create public synonym XHB_EMAIL2 for XHIBIT.XHB_EMAIL2;

GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.XHB_EMAIL2 TO PUBLIC;



CREATE TABLE XHB_EMAIL_DOMAIN
(
  DOMAIN_ID          NUMBER(8)			NOT NULL,
  EMAIL_DOMAIN       VARCHAR2(255),
  CREATION_DATE      DATE                       NOT NULL,
  CREATED_BY         VARCHAR2(30)          	NOT NULL,
  LAST_UPDATE_DATE   DATE                       NOT NULL,
  LAST_UPDATED_BY    VARCHAR2(30)          	NOT NULL,
  VERSION            NUMBER(5)                  NOT NULL
)
TABLESPACE XHIBITD;



ALTER TABLE XHB_EMAIL_DOMAIN 
  ADD CONSTRAINT EMAIL_DOMAIN_PK
  PRIMARY KEY (DOMAIN_ID)
    USING INDEX 
    TABLESPACE XHIBITX;

create public synonym XHB_EMAIL_DOMAIN for XHIBIT.XHB_EMAIL_DOMAIN;

GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.XHB_EMAIL_DOMAIN TO PUBLIC;



CREATE TABLE XHB_EMAIL_SERVER
(
  SERVER_ID         NUMBER(2)                   NOT NULL,
  SERVER_ADDRESS    VARCHAR2(64),
  SMTP_USERNAME     VARCHAR2(64),
  SMTP_PASSWD	    	BLOB,
  SMTP_PARAMETERS   VARCHAR2(255),
  MAPI_USERNAME     VARCHAR2(64),
  MAPI_PARAMETERS   VARCHAR2(255),
  POP3_USERNAME     VARCHAR2(64),
  POP3_PASSWD	    	BLOB,
  POP3_PARAMETERS   VARCHAR2(255),
  DOCUMENT_NAME			VARCHAR2(64)                NOT NULL,
  GET_RECORDS       NUMBER(6),
  ARCHIVE_EMAIL     VARCHAR2(255),
  SERVER_STATUS     VARCHAR2(1),
  FAX_PROCESS				VARCHAR2(1),
  LAST_FAXRUN_DATE  DATE,
  CREATION_DATE     DATE                        NOT NULL,
  CREATED_BY        VARCHAR2(30)           	NOT NULL,
  LAST_UPDATE_DATE  DATE                        NOT NULL,
  LAST_UPDATED_BY   VARCHAR2(30)           	NOT NULL,
  VERSION            NUMBER(5)                  NOT NULL,
  PROTOCOL	    VARCHAR2(1)			NOT NULL,
  SERVER_GROUP	    NUMBER(2)
)
TABLESPACE XHIBITD;


ALTER TABLE XHB_EMAIL_SERVER 
  ADD CONSTRAINT EMAIL_SERVER_PK
  PRIMARY KEY (SERVER_ID)
    USING INDEX 
    TABLESPACE XHIBITX;


create public synonym XHB_EMAIL_SERVER for XHIBIT.XHB_EMAIL_SERVER;

GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.XHB_EMAIL_SERVER TO PUBLIC;


ALTER TABLE XHB_EMAIL2 ADD (
  CONSTRAINT XHB_EMAIL2_SERVER_ID_FK FOREIGN KEY (SERVER_ID) 
    REFERENCES XHB_EMAIL_SERVER (SERVER_ID));



/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */

CREATE TABLE AUD_EMAIL2
(
  MAIL_ID            NUMBER(8)			NOT NULL,
  RECIPIENT          VARCHAR2(255),
  SUBJECT            VARCHAR2(255)         	NOT NULL,
  SENDER             VARCHAR2(255)         	NOT NULL,
  STATUS             VARCHAR2(3)           	NOT NULL,
  ATTEMPTS           NUMBER(2)             	DEFAULT 0,
  CREATION_TIME      DATE                  	NOT NULL,
  MUSTRECEIVE        VARCHAR2(2)           	NOT NULL,
  REJECTTIME         DATE,
  REASON             VARCHAR2(50),
  COURT_ID           NUMBER(8),
  EMAIL_TO           VARCHAR2(255),
  COMPANY            VARCHAR2(255),
  ATTACHMENT         VARCHAR2(1)           	DEFAULT 'Y',
  MIME_TYPE          VARCHAR2(3)           	DEFAULT 'PDF'	NOT NULL,
  MIME_BODY_BLOB_ID  NUMBER(10)            	NOT NULL,
  EMAIL_PROTOCOL     VARCHAR2(1)           	NOT NULL,
  SERVER_ID          NUMBER(2),
  CREATION_DATE      DATE                  	NOT NULL,
  CREATED_BY         VARCHAR2(30)          	NOT NULL,
  LAST_UPDATE_DATE   DATE                  	NOT NULL,
  LAST_UPDATED_BY    VARCHAR2(30)          	NOT NULL,
  VERSION            NUMBER(5),
  INSERT_EVENT       VARCHAR2(1)           	DEFAULT 'X'     NOT NULL
)
TABLESPACE AUDITD;

CREATE PUBLIC SYNONYM AUD_EMAIL2 FOR AUD_EMAIL2;

GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.AUD_EMAIL2 TO PUBLIC;





CREATE TABLE AUD_EMAIL_DOMAIN
(
  DOMAIN_ID          NUMBER(8)			NOT NULL,
  EMAIL_DOMAIN       VARCHAR2(255),
  CREATION_DATE      DATE                       NOT NULL,
  CREATED_BY         VARCHAR2(30)          	NOT NULL,
  LAST_UPDATE_DATE   DATE                       NOT NULL,
  LAST_UPDATED_BY    VARCHAR2(30)          	NOT NULL,
  VERSION            NUMBER(5)                  NOT NULL,
  INSERT_EVENT       VARCHAR2(1)           	DEFAULT 'X'          NOT NULL
)
TABLESPACE AUDITD;



CREATE PUBLIC SYNONYM AUD_EMAIL_DOMAIN FOR AUD_EMAIL_DOMAIN;


GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.AUD_EMAIL_DOMAIN TO PUBLIC;



/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE XHIBIT.XHB_EMAIL2_SEQ
  START WITH 1
  NOMAXVALUE
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE SEQUENCE XHIBIT.XHB_EMAIL_DOMAIN_SEQ
  START WITH 1
  NOMAXVALUE
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


CREATE SEQUENCE XHIBIT.XHB_EMAIL_SERVER_SEQ
  START WITH 1
  NOMAXVALUE
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL2_BIR_TR
  BEFORE INSERT
  ON XHB_EMAIL2
  FOR EACH ROW
BEGIN

  IF :NEW.MAIL_ID IS NULL THEN

    SELECT XHB_EMAIL2_SEQ.NEXTVAL
    INTO   :NEW.MAIL_ID
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

END XHB_EMAIL2_BIR_TR;
/

SHOW ERRORS;


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL2_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL2
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL2') = 1) THEN

    INSERT INTO AUD_EMAIL2 (MAIL_ID,
                           RECIPIENT,
                           SUBJECT,
                           SENDER,
                           STATUS,
                           ATTEMPTS,
                           CREATION_TIME,
                           MUSTRECEIVE,
                           REJECTTIME,
                           REASON,
                           LAST_UPDATE_DATE,
                           CREATION_DATE,
                           CREATED_BY,
                           LAST_UPDATED_BY,
                           VERSION,
                           COURT_ID,
                           EMAIL_TO,
                           COMPANY,
                           ATTACHMENT,
                           MIME_TYPE,
                           MIME_BODY_BLOB_ID,
                           INSERT_EVENT,
			   EMAIL_PROTOCOL,
			   SERVER_ID)
                   VALUES (:OLD.MAIL_ID,
                           :OLD.RECIPIENT,
                           :OLD.SUBJECT,
                           :OLD.SENDER,
                           :OLD.STATUS,
                           :OLD.ATTEMPTS,
                           :OLD.CREATION_TIME,
                           :OLD.MUSTRECEIVE,
                           :OLD.REJECTTIME,
                           :OLD.REASON,
                           :OLD.LAST_UPDATE_DATE,
                           :OLD.CREATION_DATE,
                           :OLD.CREATED_BY,
                           :OLD.LAST_UPDATED_BY,
                           :OLD.VERSION,
                           :OLD.COURT_ID,
                           :OLD.EMAIL_TO,
                           :OLD.COMPANY,
                           :OLD.ATTACHMENT,
                           :OLD.MIME_TYPE,
                           :OLD.MIME_BODY_BLOB_ID,
                           l_trig_event,
			   :OLD.EMAIL_PROTOCOL,
			   :OLD.SERVER_ID);

  END IF;

END XHB_EMAIL2_BUR_TR;
/

SHOW ERRORS;




CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL_DOMAIN_BIR_TR
  BEFORE INSERT
  ON XHB_EMAIL_DOMAIN
  FOR EACH ROW
BEGIN

  IF :NEW.DOMAIN_ID IS NULL THEN

    SELECT XHB_EMAIL_DOMAIN_SEQ.NEXTVAL
    INTO   :NEW.DOMAIN_ID
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

END XHB_EMAIL_DOMAIN_BIR_TR;
/

show errors;



CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL_DOMAIN_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL_DOMAIN
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION + 1,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL_DOMAIN') = 1) THEN

    INSERT INTO AUD_EMAIL_DOMAIN	 (DOMAIN_ID,
			   		EMAIL_DOMAIN,
                           		LAST_UPDATE_DATE,
                           		CREATION_DATE,
                           		CREATED_BY,
                           		LAST_UPDATED_BY,
                           		VERSION,
                           		INSERT_EVENT)
                 VALUES 		(:OLD.DOMAIN_ID,
                           		:OLD.EMAIL_DOMAIN,
                           		:OLD.LAST_UPDATE_DATE,
                           		:OLD.CREATION_DATE,
                           		:OLD.CREATED_BY,
                           		:OLD.LAST_UPDATED_BY,
                           		:OLD.VERSION,
                           		l_trig_event);

  END IF;

END XHB_EMAIL_DOMAIN_BUR_TR;
/

show errors;


CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL_SERVER_BIR_TR
  BEFORE INSERT
  ON XHB_EMAIL_SERVER
  FOR EACH ROW
BEGIN

  IF :NEW.SERVER_ID IS NULL THEN

    SELECT XHB_EMAIL_SERVER_SEQ.NEXTVAL
    INTO   :NEW.SERVER_ID
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

END XHB_EMAIL_SERVER_BIR_TR;
/

show errors;

CREATE OR REPLACE TRIGGER "XHIBIT".XHB_EMAIL_SERVER_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL_SERVER
  FOR EACH ROW
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 1) THEN

      IF (:OLD.VERSION != :NEW.VERSION) THEN
      /* Someone has pulled the rug out from below! */

        RAISE OPTIMISTIC_LOCK_PROB;

      END IF;

    END IF;

    SELECT :OLD.VERSION,
           SYSDATE
    INTO   :NEW.VERSION,
           :NEW.LAST_UPDATE_DATE
    FROM   DUAL;

    /* If the user is not the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (XHB_CUSTOM_PKG.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;


END XHB_EMAIL_SERVER_BUR_TR;
/


Show errors;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */


/*
 * Changes, additions or deletion of data
 */

INSERT INTO XHB_CONFIG_PROP ( CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE ) VALUES ( 1, 'ALL_SMTP', 'N'); 
INSERT INTO XHB_CONFIG_PROP ( CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE ) VALUES ( 2, 'MAX_SMTP_SEND_ATTEMPTS', '5'); 
INSERT INTO XHB_CONFIG_PROP ( CONFIG_PROP_ID, PROPERTY_NAME, PROPERTY_VALUE ) VALUES ( 3, 'MAX_MAPI_SEND_ATTEMPTS', '5'); 
INSERT INTO MTBL_TIME_TRIGGER_STATUS ( MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS ) VALUES ( 'EmailFax_Poll.mmc',  TO_Date( '05/10/2006 12:27:05 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'MCMAIL2_2', 1, 'C'); 
INSERT INTO MTBL_TIME_TRIGGER_STATUS ( MAPNAME, LASTRUNTIMEDATE, HOSTNAME, FREQUENCY, PROGRESS ) VALUES ( 'EmailFax_Poll_M.mmc',  TO_Date( '05/10/2006 12:27:05 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'MCMAIL2_1', 1, 'C'); 
COMMIT;


insert into  xhb_sys_audit(TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) 
values 
( 'XHB_EMAIL2','AUD_EMAIL2','Y');

insert into  xhb_sys_audit(TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) 
values 
('XHB_EMAIL_DOMAIN','AUD_EMAIL_DOMAIN','Y');



/*
 * Updating of table XHB_VERSION
 */

update xhb_version
set schema_version = '7.7.7.4',
last_UPDATE_DATE = sysdate
where display_name = 'Database';

update xhb_version
set schema_version = '7.7.7.4',
last_UPDATE_DATE = sysdate
where display_name = 'Mercator';


COMMIT;

spool off
