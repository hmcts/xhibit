/*
 * Filename:    Xhibit_DB_Patch_8_1_1.sql
 *
 *
 * PLEASE CHANGE :  Insert relevant release number in X_X - in Filename above, for generating logfile name
 *                  and in xhb_version updates.
 */
/*
 * HISTORY
 * =======
 * DATE		WHO	CHANGE ID	COMMENT
 * ----         ---     ---------       -------
 *  14/07/2006	K SHAH			Changes for release 8.1
 *  19/07/2006  K SHAH			Added changes from Wiki  
 *  31/07/2006  C RANAWEERA		Added changes from Wiki
 *  01/08/2006  C RANAWEERA		Removed drop statements
 *  18/10/2006  K SHAH			Several changes including alter tables etc.
 *  19/10/2006  K SHAH			Added audit table for XHB_PROGRESS_TRIGGER
 *  19/10/2006  K Shah			Altered the calling order so that pkgs are created before triggers
 *  30/11/2006  K Shah			Added trigger XHB_PSR_REQUEST_BUR_TR as it was missed in the baseline script
 *  30/11/2006  K Shah			Altered xhb_progress_trigger table and corresponding audit table
 *					and added the update trigger Also updated sys_audit table to enable auditing
 *					for the xhb_progress_trigger table -  Wiki request 40
 *  05/12/2006  K Shah			Added default values for version and last_updated_by
 *					columns of xhb_progress_trigger and audit table
 *  15/12/2006  K Shah			Added xhb_progress_trig_bir_tr change Wiki 1 -  Rel 8.1.1
 *
 */ 

set echo on
set term off
column filename new_value spool_filename
  select 'Xhibit_DB_Patch_8_1_1_'||
    to_char(sysdate,'YYYYMMDDHH24MI')||'.log' filename
  from dual
/
set term on
spool &&spool_filename


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 */

 alter table XHB_DEFENDANT_ON_CASE add (Current_BC_Status varchar2(1) );
 alter table AUD_DEFENDANT_ON_CASE add (Current_BC_Status varchar2(1) );

ALTER TABLE xhb_psr_request ADD (LONG_ADJOURNMENT_DATE   date );
ALTER TABLE xhb_psr_request ADD (psr_request_trigger NUMBER(1) DEFAULT 0);

ALTER TABLE aud_psr_request ADD (LONG_ADJOURNMENT_DATE   date );
ALTER TABLE aud_psr_request ADD (psr_request_trigger NUMBER(1) DEFAULT 0);

 /*

 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */


CREATE TABLE xhb_selectors (
selector_id          NUMBER(8) NOT NULL,
selector             VARCHAR2(444),
description          VARCHAR2(1333),
enabled              VARCHAR2(1) default 'Y',
precedence           NUMBER(3))
TABLESPACE xhibitd;

ALTER TABLE xhb_selectors
 ADD ( CONSTRAINT selectors_enabled_chk CHECK                                      
 (enabled IN ('Y','N') AND enabled IS NOT NULL));    

ALTER TABLE xhb_selectors ADD (
  CONSTRAINT selector_id_pk PRIMARY KEY ( selector_id )
    USING INDEX 
    TABLESPACE xhibitx);

create public synonym xhb_selectors for xhibit.xhb_selectors;

GRANT DELETE, INSERT, SELECT, UPDATE ON  xhibit.xhb_selectors TO PUBLIC;


CREATE TABLE xhb_queues (
queue_id             NUMBER(8) NOT NULL,
jndi_name            VARCHAR2(444) NOT NULL,
description          VARCHAR2(1333))
TABLESPACE xhibitd;

ALTER TABLE xhb_queues ADD (
  CONSTRAINT queue_id_pk PRIMARY KEY ( queue_id )
    USING INDEX 
    TABLESPACE xhibitx);


create public synonym xhb_queues for xhibit.xhb_queues;

GRANT DELETE, INSERT, SELECT, UPDATE ON  xhibit.xhb_queues TO PUBLIC;


CREATE TABLE xhb_selector_queues (
selector_queue_id    NUMBER(8) NOT NULL,
selector_id          NUMBER(8) NOT NULL,
queue_id             NUMBER(8))
TABLESPACE xhibitd;

ALTER TABLE xhb_selector_queues ADD (
  CONSTRAINT selector_queue_id_pk PRIMARY KEY ( selector_queue_id )
    USING INDEX 
    TABLESPACE xhibitx);

CREATE INDEX select_que_select_id_fk
  ON xhb_selector_queues  (selector_id)
TABLESPACE xhibitx;

ALTER TABLE xhb_selector_queues ADD (
  CONSTRAINT selector_queues_selector_id_fk FOREIGN KEY (selector_id) 
    REFERENCES xhb_selectors (selector_id));

CREATE INDEX select_ques_que_id_fk
  ON xhb_selector_queues  (queue_id)
TABLESPACE xhibitx;

ALTER TABLE xhb_selector_queues ADD (
  CONSTRAINT selector_queues_queue_id_fk FOREIGN KEY (queue_id) 
    REFERENCES xhb_queues (queue_id));

create public synonym xhb_selector_queues for xhibit.xhb_selector_queues;

GRANT DELETE, INSERT, SELECT, UPDATE ON  xhibit.xhb_selector_queues TO PUBLIC;


create table XHB_PROGRESS_TRIGGER_STATUS (
status_id       NUMBER(8)                     NOT NULL,
status_text     VARCHAR2(50)                  NOT NULL)
TABLESPACE xhibitd;


ALTER TABLE xhb_progress_trigger_status add (
  CONSTRAINT progress_status_id_pk PRIMARY KEY ( status_id )
  USING INDEX
  TABLESPACE xhibitx) ;

create public synonym XHB_PROGRESS_TRIGGER_STATUS for xhibit.XHB_PROGRESS_TRIGGER_STATUS;

GRANT DELETE, INSERT, SELECT, UPDATE ON  xhibit.XHB_PROGRESS_TRIGGER_STATUS TO PUBLIC;


CREATE TABLE XHIBIT.XHB_PROGRESS_TRIGGER
(
  PROG_TRIGGER_ID   NUMBER(8)                   NOT NULL,
  CASE_ID           NUMBER(8)                   NOT NULL,
  STATUS_ID         NUMBER(8)                   NOT NULL,
  UPDATED_TIME      DATE                        NOT NULL,
  LAST_SENT_TIME    DATE,
  NEW_HEARING_FLAG  VARCHAR2(1 BYTE)            DEFAULT 'N'     NOT NULL,
  RETRY             NUMBER(2)                   DEFAULT 0,
  LAST_UPDATE_DATE  DATE,
  LAST_UPDATED_BY   VARCHAR2(30) 		DEFAULT ' '	NOT NULL,
  VERSION	    NUMBER(5)			DEFAULT 0	NOT NULL
)
TABLESPACE XHIBITD;

ALTER TABLE xhb_progress_trigger ADD (
  CONSTRAINT progress_trigger_id_pk PRIMARY KEY ( prog_trigger_id )
    USING INDEX 
    TABLESPACE xhibitx);

CREATE UNIQUE INDEX progress_trigger_case_fk
  ON xhb_progress_trigger  (case_id)
TABLESPACE xhibitx;

ALTER TABLE xhb_progress_trigger ADD (
  CONSTRAINT progress_trigger_case_id_fk FOREIGN KEY (case_id) 
    REFERENCES xhb_case (case_id));

CREATE INDEX progress_trigger_status_fk
  ON xhb_progress_trigger  (status_id)
TABLESPACE xhibitx;


ALTER TABLE xhb_progress_trigger ADD (
  CONSTRAINT progress_trigger_status_id_fk FOREIGN KEY (status_id) 
    REFERENCES xhb_progress_trigger_status (status_id));

ALTER TABLE xhb_progress_trigger                                                           
 ADD ( CONSTRAINT new_hearing_flag_chk CHECK                                       
 (new_hearing_flag IN ('Y','N') ) );  

create public synonym XHB_PROGRESS_TRIGGER for xhibit.XHB_PROGRESS_TRIGGER;

GRANT DELETE, INSERT, SELECT, UPDATE ON  xhibit.XHB_PROGRESS_TRIGGER TO PUBLIC;


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 */


CREATE TABLE XHIBIT.AUD_PROGRESS_TRIGGER
(
  PROG_TRIGGER_ID   NUMBER(8)                   NOT NULL,
  CASE_ID           NUMBER(8)                   NOT NULL,
  STATUS_ID         NUMBER(8)                   NOT NULL,
  UPDATED_TIME      DATE                        NOT NULL,
  LAST_SENT_TIME    DATE,
  NEW_HEARING_FLAG  VARCHAR2(1 BYTE) DEFAULT 'N' NOT NULL,
  RETRY		    NUMBER(2)   DEFAULT 0,
  LAST_UPDATE_DATE  DATE,
  LAST_UPDATED_BY   VARCHAR2(30) DEFAULT ' '	NOT NULL,
  VERSION	    NUMBER(5)	DEFAULT 0		NOT NULL,
  INSERT_EVENT	    VARCHAR2(1)			NOT NULL
)
TABLESPACE AUDITD;


CREATE PUBLIC SYNONYM AUD_PROGRESS_TRIGGER FOR XHIBIT.AUD_PROGRESS_TRIGGER;


GRANT DELETE, INSERT, SELECT, UPDATE ON  XHIBIT.AUD_PROGRESS_TRIGGER TO PUBLIC;




/*
 * Changes, additions or deletion of sequences
 */

create sequence xhb_selectors_seq       start with 1000 increment by 1 nocache;
create sequence xhb_queues_seq          start with 1000 increment by 1 nocache;
create sequence xhb_selector_queues_seq start with 1000 increment by 1 nocache;
create sequence xhb_progress_status_seq start with 1 increment by 1 nocache;
create sequence xhb_progress_trigger_seq start with 1 increment by 1 nocache;


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

@@xhb_code_release_control_811.sql


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

CREATE OR REPLACE TRIGGER XHB_DEFENDANTONCASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEFENDANT_ON_CASE
  FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEFENDANTONCASE_BUR_TR */
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEFENDANT_ON_CASE') = 1) THEN

    INSERT INTO AUD_DEFENDANT_ON_CASE
	   (DEFENDANT_ON_CASE_ID,
            NO_OF_TICS,
            FINAL_DRIVING_LICENCE_STATUS,
            PTIURN,
            IS_JUVENILE,
            IS_MASKED,
            MASKED_NAME,
            CASE_ID,
            DEFENDANT_ID,
            LAST_UPDATE_DATE,
            CREATION_DATE,
            CREATED_BY,
            LAST_UPDATED_BY,
            VERSION,
            OBS_IND,
            results_verified,
            defendant_number,
            date_of_committal,
            PNC_ID,
            COLLECT_MAGISTRATE_COURT_ID,
            INSERT_EVENT,
	    current_BC_status)
    VALUES (:old.DEFENDANT_ON_CASE_ID,
            :old.NO_OF_TICS,
            :old.FINAL_DRIVING_LICENCE_STATUS,
            :old.PTIURN,
            :old.IS_JUVENILE,
            :old.IS_MASKED,
            :old.MASKED_NAME,
            :old.CASE_ID,
            :old.DEFENDANT_ID,
            :old.LAST_UPDATE_DATE,
            :old.CREATION_DATE,
            :old.CREATED_BY,
            :old.LAST_UPDATED_BY,
            :old.VERSION,
            :old.OBS_IND,
            :old.results_verified,
            :old.defendant_number,
            :old.date_of_committal,
            :old.PNC_ID,
            :old.COLLECT_MAGISTRATE_COURT_ID,
            l_trig_event,
	    :old.current_BC_status);

  END IF;

END;
/

show errors;


create or replace trigger xhb_selectors_bir_tr
    before insert on xhb_selectors
    for each row
begin
    if :new.selector_id is null then
        select xhb_selectors_seq.nextval
        into  :new.selector_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger xhb_queues_bir_tr
    before insert on xhb_queues
    for each row
begin
    if :new.queue_id is null then
        select xhb_queues_seq.nextval
        into  :new.queue_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger xhb_selector_queues_bir_tr
    before insert on xhb_selector_queues
    for each row
begin
    if :new.selector_queue_id is null then
        select xhb_selector_queues_seq.nextval
        into  :new.selector_queue_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger xhb_progress_trig_stat_bir_tr
    before insert on xhb_progress_trigger_status
    for each row
begin
    if :new.status_id is null then
        select xhb_progress_status_seq.nextval
        into  :new.status_id
        from   dual;
    end if;
end;
/

show errors;

create or replace trigger xhb_progress_trig_bir_tr
    before insert on xhb_progress_trigger
    for each row
begin
    if :new.prog_trigger_id is null then
        select xhb_progress_trigger_seq.nextval
        into  :new.prog_trigger_id
        from   dual;
    end if;
    
    IF (:NEW.LAST_UPDATED_BY IS NULL) THEN
        SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
        INTO   :NEW.LAST_UPDATED_BY
        FROM   DUAL;
    END IF;

    SELECT SYSDATE,
        1
    INTO   :NEW.LAST_UPDATE_DATE,
        :NEW.VERSION
    FROM   DUAL;

end;
/

show errors;


CREATE OR REPLACE TRIGGER XHB_PROGRESS_TRIG_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PROGRESS_TRIGGER
  FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_PROGRESS_TRIGGER_BUR_TR */
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PROGRESS_TRIGGER') = 1) THEN

    INSERT INTO AUD_PROGRESS_TRIGGER
		(PROG_TRIGGER_ID,
		CASE_ID,
		STATUS_ID,
		UPDATED_TIME,
		LAST_SENT_TIME,
		NEW_HEARING_FLAG,
		RETRY,
		LAST_UPDATE_DATE,
		LAST_UPDATED_BY,
		VERSION,
		INSERT_EVENT
		)
    VALUES (:old.PROG_TRIGGER_ID,
            :old.CASE_ID,
            :old.STATUS_ID,
            :old.UPDATED_TIME,
 	    :old.LAST_SENT_TIME,
	    :old.NEW_HEARING_FLAG,
            :old.RETRY,
	    :old.LAST_UPDATE_DATE,
	    :old.LAST_UPDATED_BY,
	    :old.VERSION,
            l_trig_event);

  END IF;

END;

/

SHOW ERRORS;

CREATE OR REPLACE TRIGGER XHB_PSR_REQUEST_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_PSR_REQUEST
  FOR EACH ROW
/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_PSR_REQUEST_BUR_TR */
DECLARE

  l_trig_event VARCHAR2(1) := NULL;

  OPTIMISTIC_LOCK_PROB EXCEPTION;
  PRAGMA EXCEPTION_INIT(OPTIMISTIC_LOCK_PROB, -20101);

BEGIN

  /* Determine whether UPDATING or DELETING */
  IF UPDATING THEN

    l_trig_event := 'U';

    /* If the user is the connection pool user as defined in XHB_SYS_USER_INFORMATION */
    IF (Xhb_Custom_Pkg.IS_CONNECTION_POOL_USER = 1) THEN

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
    IF (Xhb_Custom_Pkg.IS_CONNECTION_POOL_USER = 0) THEN

      SELECT SYS_CONTEXT('USERENV', 'SESSION_USER')
      INTO   :NEW.LAST_UPDATED_BY
      FROM   DUAL;

    END IF;

  ELSE -- Must be DELETING

    l_trig_event := 'D';

  END IF;

  /* Is Auditing on this table required */
  IF (Xhb_Custom_Pkg.IS_AUDIT_REQUIRED('XHB_PSR_REQUEST') = 1) THEN

    INSERT INTO AUD_PSR_REQUEST
    VALUES (:OLD.psr_request_id,
            :OLD.arrive_no_later_than_date,
            :OLD.hearing_date,
            :OLD.defendant_remand_location,
            :OLD.solicitors_telephone,
            :OLD.probation_contact,
            :OLD.antecendants,
            :OLD.cps_office_for_antecendants,
            :OLD.circumstances_for_offences,
            :OLD.comments_by_court,
            :OLD.available_for_interview,
            :OLD.probation_office_name,
            :OLD.probation_office_address,
            :OLD.probation_office_telephone,
            :OLD.probation_office_fax,
            :OLD.probation_office_email,
            :OLD.recipient_name,
            :OLD.recipient_address,
            :OLD.recipient_telephone,
            :OLD.recipient_fax,
            :OLD.recipient_email,
            :OLD.defendant_age,
            :OLD.defendant_address,
            :OLD.defendant_surname,
            :OLD.defendant_forenames,
            :OLD.defendant_date_of_birth,
            :OLD.judge_title,
            :OLD.court_name,
            :OLD.solicitor_firm_name,
            :OLD.psr_status,
            :OLD.psr_recipient_id,
            :OLD.last_update_date,
            :OLD.creation_date,
            :OLD.created_by,
            :OLD.last_updated_by,
            :OLD.version,
            :OLD.DEFENDANT_ON_CASE_ID,
            :OLD.PSR_COURT_ROOM,
             l_trig_event,
	    :OLD.LONG_ADJOURNMENT_DATE,
            :OLD.PSR_REQUEST_TRIGGER);

  END IF;

END;
/

SHOW ERRORS;


/*
 * Changes, additions or deletion of standing data
 */

@@xhb_data_load.sql


/*
 * Updating of table XHB_SYS_AUDIT
 */


INSERT INTO XHB_SYS_AUDIT 
   (TABLE_TO_AUDIT, AUDIT_TABLE, AUDITABLE) 
 VALUES 
   ('XHB_PROGRESS_TRIGGER', 'AUD_PROGRESS_TRIGGER', 'Y');
   

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '8.1.1', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '8.1.1', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '8.1.1', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '8.1.1', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;

spool off
