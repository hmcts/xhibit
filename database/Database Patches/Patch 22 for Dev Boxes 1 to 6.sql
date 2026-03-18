/*
 * Patch(22) to upgrade DB release 21 to DB release 22.
 */

ALTER TABLE XHB_DOCUMENT_DISTRIBUTION ADD (USE_PREF_DIST_TYPE VARCHAR2(1) DEFAULT 'X' NOT NULL);

ALTER TABLE XHB_EMAIL ADD (MIME_TYPE VARCHAR2(3) DEFAULT 'PDF' NOT NULL);

ALTER TABLE XHB_RECIPIENT ADD (PREF_DISTRIBUTION_TYPE VARCHAR2(5) DEFAULT 'XXXXX' NOT NULL,
                               PREF_MIME_TYPE         VARCHAR2(3) DEFAULT 'XXX' NOT NULL);


DROP TABLE AUD_RECIPIENT;
CREATE TABLE AUD_RECIPIENT TABLESPACE AUDITD AS SELECT * FROM XHB_RECIPIENT;
TRUNCATE TABLE AUD_RECIPIENT;

DROP TABLE AUD_DOCUMENT_DISTRIBUTION;
CREATE TABLE AUD_DOCUMENT_DISTRIBUTION TABLESPACE AUDITD AS SELECT * FROM XHB_DOCUMENT_DISTRIBUTION;
TRUNCATE TABLE AUD_DOCUMENT_DISTRIBUTION;

DROP TABLE AUD_EMAIL;
CREATE TABLE AUD_EMAIL TABLESPACE AUDITD AS SELECT * FROM XHB_EMAIL;
TRUNCATE TABLE AUD_EMAIL;

set serveroutput on
execute dbms_output.enable(100000)

prompt Adding columns to autit tables to indicate UPDATE/DELETED on main table

DECLARE

  /*
   * Cursor to select the audit tables that require the additional column.
   */

  CURSOR c_audit_tables is SELECT table_name
                           FROM   user_tables
                           WHERE  table_name like 'AUD_%';

  l_audit_table VARCHAR2(128);
  l_cmd_start   VARCHAR2(128) := ' ALTER TABLE ';
  l_cmd_end     VARCHAR2(128) := ' ADD (INSERT_EVENT VARCHAR2(1) DEFAULT ''X'' NOT NULL)';
  l_cmd_full    VARCHAR2(512);

BEGIN

  OPEN c_audit_tables;

  LOOP

    FETCH c_audit_tables
    INTO  l_audit_table;

    EXIT WHEN c_audit_tables%NOTFOUND;


    l_cmd_full := l_cmd_start||l_audit_table||l_cmd_end;


    EXECUTE IMMEDIATE l_cmd_full;

  END LOOP;

  CLOSE c_audit_tables;

END;
/

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_DIST_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DOCUMENT_DISTRIBUTION
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DOCUMENT_DIST_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_DISTRIBUTION') = 1) THEN

    INSERT INTO AUD_DOCUMENT_DISTRIBUTION 
    VALUES (:old.doc_distribution_id, 
            :old.distribution_type, 
            :old.document_type, 
            :old.mime_type, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.recipient_id, 
            :old.wll_recipient_id, 
            :old.COURT_ID,
            :old.USE_PREF_DIST_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_EMAIL_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_EMAIL
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_EMAIL_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL') = 1) THEN

    INSERT INTO AUD_EMAIL 
    VALUES (:old.MAIL_ID, 
            :old.RECIPIENTS, 
            :old.CCRECIPIENTS, 
            :old.BCCRECIPIENTS, 
            :old.SUBJECT, 
            :old.SENDER, 
            :old.MIME_BODY, 
            :old.STATUS, 
            :old.CREATION_TIME, 
            :old.MUSTRECEIVE, 
            :old.REJECTTIME, 
            :old.REASON, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID, 
            :old.EMAIL_TO, 
            :old.COMPANY, 
            :old.ATTACHMENT,
            :old.MIME_TYPE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_RECIPIENT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_RECIPIENT
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_RECIPIENT_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_RECIPIENT') = 1) THEN

    INSERT INTO AUD_RECIPIENT 
    VALUES (:old.recipient_id, 
            :old.recipient_name, 
            :old.fax_number, 
            :old.email_address, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.COURT_ID,
            :old.PREF_DISTRIBUTION_TYPE,
            :old.PREF_MIME_TYPE,       
            l_trig_event);

  END IF;

END;
/

create  or
replace view
XHB_COUNSEL_FACILITIES_VIEW  as
    	select distinct
		sitting.court_room_id                         	COURT_ROOM_ID,    
		sitting.is_floating                           	IS_FLOATING,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,    
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME, 
		court_room.court_room_name                    	COURT_ROOM_NAME,   
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,    
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME, 
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID, 
		scheduled_hearing.original_time               	SH_ORIGINAL_TIME, 
		scheduled_hearing.not_before_time             	SH_NOT_BEFORE_TIME,
		sched_hearing_defendant.sched_hear_def_id     	SCHED_HEAR_DEF_ID,
		defendant_on_case.defendant_on_case_id        	DEFENDANT_ON_CASE_ID, 
		defendant_on_case.is_masked                   	IS_MASKED, 
		defendant_on_case.masked_name 			MASKED_NAME,
		defendant.defendant_id				DEFENDANT_ID, 
		defendant.first_name 				DEF_FIRST_NAME, 
		defendant.middle_name 				DEF_MIDDLE_NAME, 
		defendant.surname 				DEF_SURNAME, 
		defendant.initials 				DEF_INITIALS,
		xcase.case_id					CASE_ID, 
		xcase.case_number 				CASE_NUMBER, 
		xcase.case_type					CASE_TYPE, 
		xcase.case_sub_type				CASE_SUB_TYPE,
		sh_leg_rep.sh_leg_rep_id			SH_LEG_REP_ID, 
		sh_leg_rep.legal_role				LEGAL_ROLE,
		legal_rep.ref_legal_rep_id			REF_LEGAL_REP_ID, 
		legal_rep.first_name 				LEG_REP_FIRST_NAME, 
		legal_rep.middle_name 				LEG_REP_MIDDLE_NAME, 
		legal_rep.surname 				LEG_REP_SURNAME, 
		legal_rep.title 				LEG_REP_TITLE, 
		legal_rep.initials 				LEG_REP_INITIALS, 
		legal_rep.legal_rep_type			LEGAL_REP_TYPE,
		advocate.ref_advocate_id			REF_ADVOCATE_ID,
		chamber.ref_chamber_id				REF_CHAMBER_ID, 
		chamber.firm_name 				CHAMBER_FIRM_NAME,
		adv_address.address_id 				ADV_ADDRESS_ID, 
		adv_address.address_1 				ADV_ADDRESS_1,
		adv_address.address_2 				ADV_ADDRESS_2,
		adv_address.address_3 				ADV_ADDRESS_3,
		adv_address.address_4  				ADV_ADDRESS_4,
		adv_address.town  				ADV_TOWN,
		adv_address.county 				ADV_COUNTY,
		adv_address.country 				ADV_COUNTRY,
		adv_address.postcode 				ADV_POSTCODE,
		solicitor.solicitor_id				SOLICITOR_ID,
		solfirm.ref_solicitor_firm_id			REF_SOLICITOR_FIRM_ID, 
		solfirm.solicitor_firm_name			SOLICITOR_FIRM_NAME,
		sol_address.address_id 				SOL_ADDRESS_ID, 
		sol_address.address_1  				SOL_ADDRESS_1,
		sol_address.address_2  				SOL_ADDRESS_2,
		sol_address.address_3  				SOL_ADDRESS_3,
		sol_address.address_4  				SOL_ADDRESS_4,
		sol_address.town  				SOL_TOWN,
		sol_address.county  				SOL_COUNTY,
		sol_address.country  				SOL_COUNTRY,
		sol_address.postcode 				SOL_POSTCODE
             from 
		xhb_hearing_list             HEARING_LIST,
		xhb_sitting                  SITTING,
		xhb_court_room               COURT_ROOM,
		xhb_scheduled_hearing        SCHEDULED_HEARING,
		xhb_hearing                  HEARING,
		xhb_ref_hearing_type         REF_HEARING_TYPE,
		xhb_sched_hearing_attendee   SH_ATTENDEE,
		xhb_sh_staff                 SH_STAFF,
		xhb_sched_hearing_defendant  SCHED_HEARING_DEFENDANT,
		xhb_defendant_on_case        DEFENDANT_ON_CASE,
		xhb_defendant                DEFENDANT,
		xhb_case                     XCASE,
		xhb_sh_leg_rep               SH_LEG_REP,
		xhb_ref_legal_representative LEGAL_REP,
		xhb_ref_advocate             ADVOCATE,
		xhb_ref_chamber              CHAMBER,
		xhb_ref_solicitor            SOLICITOR,
		xhb_ref_solicitor_firm       SOLFIRM,
		xhb_address                  ADV_ADDRESS,
		xhb_address                  SOL_ADDRESS		
             where
		( hearing_list.list_id = sitting.list_id ) and
		( sitting.court_room_id = court_room.court_room_id ) and
		( scheduled_hearing.sitting_id = sitting.sitting_id ) and
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) and
		( scheduled_hearing.hearing_id = hearing.hearing_id ) and
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) and	
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) and						   
		( hearing.case_id = xcase.case_id ) and
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) and
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) and	
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) and
		( scheduled_hearing.scheduled_hearing_id = sh_leg_rep.scheduled_hearing_id(+)) and
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) and	
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) and
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) and
		( chamber.address_id = adv_address.address_id(+) ) and
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) and
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) and
		( solfirm.address_id = sol_address.address_id(+) ) ;

create  or
replace package
counselfacilities  as

    type counsel_type is ref cursor;
    
    procedure get_counsel_sign_in(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_court_room_id   in     number
        );
    
    procedure search_counsel(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        );
        
    procedure search_defendants(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        );
     
end counselfacilities;
/
show errors

create  or
replace package body
counselfacilities  as
    procedure get_counsel_sign_in(
	p_counsel_cursor  in out counsel_type,        
	p_court_id        in     number,
        p_startdate       in     date,
        p_court_room_id   in     number
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_VIEW
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate )
		and court_room_id = any 
			(select court_room_id from xhb_sitting minus select court_room_id from xhb_sitting where court_room_id != p_court_room_id) ;
            
    end get_counsel_sign_in;
    
    
    procedure search_counsel(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_VIEW
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( sh_leg_rep_id IS NOT NULL ) and 
		( UPPER( NVL( LEG_REP_FIRST_NAME, '%') ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( LEG_REP_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) ) ;
	     
            
    end search_counsel;
    
    
    procedure search_defendants(
	p_counsel_cursor  in out counsel_type,       
	p_court_id        in     number,
        p_startdate       in     date,
        p_first_name      in     varchar2,
	p_surname         in     varchar2
        ) is

    begin
    
        open p_counsel_cursor
        for  
             select * 
                from XHB_COUNSEL_FACILITIES_VIEW
		where 
		( court_id = p_court_id ) and
		( start_date = p_startdate ) and 
		( UPPER( NVL( DEF_FIRST_NAME, '%' ) ) LIKE UPPER ( p_first_name||'%' ) ) and
		( UPPER( NVL( DEF_SURNAME, '%' ) ) LIKE UPPER ( p_surname||'%' ) ) ;
	     
            
    end search_defendants;
    
    
    
end counselfacilities;
/
show errors