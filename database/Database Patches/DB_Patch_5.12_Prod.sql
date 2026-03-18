/*
 * Patch for DB Release 5.12
 *
 * 13th February 2004
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

-- Add an additional column to the XHB_PSR_REQUEST table to store the COURT_ROOM_NAME
-- when the PSR is created via the Court Log events

ALTER TABLE XHB_PSR_REQUEST ADD (PSR_COURT_ROOM  VARCHAR2(255) NULL);

ALTER TABLE XHB_DEF_HEARING_RECORD ADD (HEARING_START_DATE        DATE       NULL,
                                        HEARING_END_DATE          DATE       NULL,
                                        LAST_CALCULATED_DURATION  NUMBER(20) NULL);

CREATE TABLE XHB_HEARING_LEG_REP (
       HEARING_LEG_REP_ID  NUMBER(8)    NOT NULL,
       HEARING_ID          NUMBER(8)    NOT NULL,
       REF_LEGAL_REP_ID    NUMBER(8)    NOT NULL,
       START_DATE          DATE         NOT NULL,
       END_DATE            DATE         NOT NULL,
       VERSION             NUMBER(5)    NOT NULL,
       LAST_UPDATED_BY     VARCHAR2(30) NOT NULL,
       CREATED_BY          VARCHAR2(30) NOT NULL,
       CREATION_DATE       DATE         NOT NULL,
       LAST_UPDATE_DATE    DATE         NOT NULL)
         TABLESPACE XHIBITD
         STORAGE (INITIAL 1M
                  NEXT 1M
                  PCTINCREASE 0);

ALTER TABLE XHB_HEARING_LEG_REP
       ADD (PRIMARY KEY (HEARING_LEG_REP_ID)
       USING INDEX TABLESPACE XHIBITX
       STORAGE (INITIAL 1M
                NEXT 1M
                PCTINCREASE 0));

ALTER TABLE XHB_HEARING_LEG_REP
      ADD (FOREIGN KEY (HEARING_ID) REFERENCES XHB_HEARING,
           FOREIGN KEY (REF_LEGAL_REP_ID) REFERENCES XHB_REF_LEGAL_REPRESENTATIVE);


/*
 * Changes, additions or deletion of views
 */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *
 * Need to create temporary table in a process on the live system:
 *
 *     1. Create a temporary audit table as a copy of the current audit table
 *     2. Drop the original audit table
 *     3. Create new audit table as select * from XHB_ table with no rows
 *     4. Add the INSERT_EVENT column to the end of the audit table
 *     5. Insert the data from the temporary audit table into the new audit table
 */

CREATE TABLE TEMP_AUD_PSR_REQUEST TABLESPACE AUDITD AS SELECT * FROM AUD_PSR_REQUEST;

DROP TABLE AUD_PSR_REQUEST;

CREATE TABLE AUD_PSR_REQUEST TABLESPACE AUDITD AS SELECT * FROM XHB_PSR_REQUEST WHERE 1 = 0;
ALTER TABLE AUD_PSR_REQUEST ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO AUD_PSR_REQUEST (SELECT PSR_REQUEST_ID,
                                    ARRIVE_NO_LATER_THAN_DATE,
                                    HEARING_DATE,
                                    DEFENDANT_REMAND_LOCATION,
                                    SOLICITORS_TELEPHONE,
                                    PROBATION_CONTACT,
                                    ANTECENDANTS,
                                    CPS_OFFICE_FOR_ANTECENDANTS,
                                    CIRCUMSTANCES_FOR_OFFENCES,
                                    COMMENTS_BY_COURT,
                                    AVAILABLE_FOR_INTERVIEW,
                                    PROBATION_OFFICE_NAME,
                                    PROBATION_OFFICE_ADDRESS,
                                    PROBATION_OFFICE_TELEPHONE,
                                    PROBATION_OFFICE_FAX,
                                    PROBATION_OFFICE_EMAIL,
                                    RECIPIENT_NAME,
                                    RECIPIENT_ADDRESS,
                                    RECIPIENT_TELEPHONE,
                                    RECIPIENT_FAX,
                                    RECIPIENT_EMAIL,
                                    DEFENDANT_AGE,
                                    DEFENDANT_ADDRESS,
                                    DEFENDANT_SURNAME,
                                    DEFENDANT_FORENAMES,
                                    DEFENDANT_DATE_OF_BIRTH,
                                    JUDGE_TITLE,
                                    COURT_NAME,
                                    SOLICITOR_FIRM_NAME,
                                    PSR_STATUS,
                                    PSR_RECIPIENT_ID,
                                    LAST_UPDATE_DATE,
                                    CREATION_DATE,
                                    CREATED_BY,
                                    LAST_UPDATED_BY,
                                    VERSION,
                                    DEFENDANT_ON_CASE_ID,
                                    NULL,
                                    INSERT_EVENT
                             FROM   TEMP_AUD_PSR_REQUEST);

DROP TABLE TEMP_AUD_PSR_REQUEST;

CREATE TABLE TEMP_AUD_DEF_HEARING_RECORD TABLESPACE AUDITD AS SELECT * FROM AUD_DEF_HEARING_RECORD;

DROP TABLE AUD_DEF_HEARING_RECORD;

CREATE TABLE AUD_DEF_HEARING_RECORD TABLESPACE AUDITD AS SELECT * FROM XHB_DEF_HEARING_RECORD WHERE 1 = 0;
ALTER TABLE AUD_DEF_HEARING_RECORD ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

INSERT INTO AUD_DEF_HEARING_RECORD (SELECT HEARING_RECORD_ID,
                                           REF_ADJOURNMENT_ID,
                                           ADJOURNED_DATE,
                                           IS_ADJOURNED,
                                           COLLECT_MAGISTRATE_COURT_ID,
                                           START_DATE_NEW_BAIL_STATUS,
                                           NEW_BAIL_STATUS,
                                           DATE_BAIL_APPLICATION,
                                           SUBST_BAIL_APPLICATION,
                                           ORAL_EVIDENCE,
                                           RESULT_BAIL_APPLICATION,
                                           IS_HRA_APPLICATION,
                                           REF_DEF_HEARING_TYPE_ID,
                                           END_BAIL_STATUS,
                                           START_BAIL_STATUS,
                                           DEFENDANT_ON_CASE_ID,
                                           HEARING_ID,
                                           VERSION,
                                           LAST_UPDATED_BY,
                                           CREATED_BY,
                                           CREATION_DATE,
                                           LAST_UPDATE_DATE,
                                           HEARING_DATES_FREETEXT_1,
                                           HEARING_DATES_FREETEXT_2,
                                           HEARING_DATES_FREETEXT_3,
                                           NULL,
                                           NULL,
                                           NULL,
                                           INSERT_EVENT
                                    FROM   TEMP_AUD_DEF_HEARING_RECORD);

DROP TABLE TEMP_AUD_DEF_HEARING_RECORD;

CREATE TABLE AUD_HEARING_LEG_REP TABLESPACE AUDITD AS SELECT * FROM XHB_HEARING_LEG_REP;
ALTER TABLE AUD_HEARING_LEG_REP ADD (INSERT_EVENT VARCHAR2(1) NOT NULL);


/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE XHB_HEARING_LEG_REP_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER
;				


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table.  However, always a good idea to recompile the BIR trigger.
 */

ALTER TRIGGER XHB_PSR_REQUEST_BIR_TR COMPILE;

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_PSR_REQUEST') = 1) THEN

    INSERT INTO AUD_PSR_REQUEST 
    VALUES (:old.psr_request_id, 
            :old.arrive_no_later_than_date, 
            :old.hearing_date, 
            :old.defendant_remand_location, 
            :old.solicitors_telephone, 
            :old.probation_contact, 
            :old.antecendants, 
            :old.cps_office_for_antecendants, 
            :old.circumstances_for_offences, 
            :old.comments_by_court, 
            :old.available_for_interview, 
            :old.probation_office_name, 
            :old.probation_office_address, 
            :old.probation_office_telephone, 
            :old.probation_office_fax, 
            :old.probation_office_email, 
            :old.recipient_name, 
            :old.recipient_address, 
            :old.recipient_telephone, 
            :old.recipient_fax, 
            :old.recipient_email, 
            :old.defendant_age, 
            :old.defendant_address, 
            :old.defendant_surname, 
            :old.defendant_forenames, 
            :old.defendant_date_of_birth, 
            :old.judge_title, 
            :old.court_name, 
            :old.solicitor_firm_name, 
            :old.psr_status, 
            :old.psr_recipient_id, 
            :old.last_update_date, 
            :old.creation_date, 
            :old.created_by, 
            :old.last_updated_by, 
            :old.version, 
            :old.DEFENDANT_ON_CASE_ID,
            :old.PSR_COURT_ROOM,
            l_trig_event);

  END IF;

END;
/

ALTER TRIGGER XHB_DEF_HEARING_RECORD_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_DEF_HEARING_RECORD_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_DEF_HEARING_RECORD
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_DEF_HEARING_RECORD_BUR_TR */

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DEF_HEARING_RECORD') = 1) THEN

    INSERT INTO AUD_DEF_HEARING_RECORD 
    VALUES (:old.HEARING_RECORD_ID, 
            :old.REF_ADJOURNMENT_ID, 
            :old.ADJOURNED_DATE, 
            :old.IS_ADJOURNED, 
            :old.COLLECT_MAGISTRATE_COURT_ID, 
            :old.START_DATE_NEW_BAIL_STATUS, 
            :old.NEW_BAIL_STATUS, 
            :old.DATE_BAIL_APPLICATION, 
            :old.SUBST_BAIL_APPLICATION, 
            :old.ORAL_EVIDENCE, 
            :old.RESULT_BAIL_APPLICATION, 
            :old.IS_HRA_APPLICATION, 
            :old.REF_DEF_HEARING_TYPE_ID, 
            :old.END_BAIL_STATUS, 
            :old.START_BAIL_STATUS, 
            :old.DEFENDANT_ON_CASE_ID, 
            :old.HEARING_ID, 
            :old.VERSION, 
            :old.LAST_UPDATED_BY, 
            :old.CREATED_BY, 
            :old.CREATION_DATE, 
            :old.LAST_UPDATE_DATE, 
            :old.hearing_dates_freetext_1, 
            :old.hearing_dates_freetext_2, 
            :old.hearing_dates_freetext_3,
            :old.hearing_start_date,
            :old.hearing_end_date,
            :old.last_calculated_duration,
            l_trig_event);

  END IF;

END;
/

create or replace trigger XHB_JOINDER_IMP_EXP
  AFTER UPDATE ON XHB_JOINDER_XML
  FOR EACH ROW

DECLARE

  l_caseId NUMBER(8) := NULL;
  l_courtId NUMBER(8) := NULL;
  
BEGIN

  IF (:NEW.STATUS = 'R') THEN

    SELECT CASE_ID,
           COURT_ID
    INTO   l_caseId,
           l_courtId
    FROM   XHB_CASE
    WHERE  CASE_ID IN (SELECT CASE_ID
                       FROM   XHB_CHARGE
                       WHERE  CHARGE_ID IN (SELECT DISTINCT FIRST_VALUE(charge_id) OVER () 
                                            FROM   XHB_JOINDER_CHARGE
                                            WHERE  JOINDER_ID = :NEW.JOINDER_ID));

    INSERT INTO XHB_IMPORT_EXPORT_STATUS(TYPE_CODE,
                                         STATUS_CODE,
                                         CASE_ID,
                                         COURT_ID)
                                 VALUES ('CI',
                                         'R',
                                         l_caseId,
                                         l_courtId);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER XHB_HEARING_LEG_REP_BIR_TR
  BEFORE INSERT
  ON XHB_HEARING_LEG_REP
  FOR EACH ROW

BEGIN

  IF :NEW.HEARING_LEG_REP_ID IS NULL THEN

    SELECT XHB_HEARING_LEG_REP_SEQ.NEXTVAL
    INTO   :NEW.HEARING_LEG_REP_ID
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

CREATE OR REPLACE TRIGGER XHB_HEARING_LEG_REP_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_HEARING_LEG_REP
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
  IF (Xhb_Custom_Pkg.IS_AUDIT_REQUIRED('XHB_HEARING_LEG_REP') = 1) THEN

    INSERT INTO AUD_HEARING_LEG_REP 
    VALUES (:OLD.HEARING_LEG_REP_ID, 
            :OLD.HEARING_ID, 
            :OLD.REF_LEGAL_REP_ID, 
            :OLD.START_DATE, 
            :OLD.END_DATE, 
            :OLD.VERSION, 
            :OLD.LAST_UPDATED_BY, 
            :OLD.CREATED_BY, 
            :OLD.CREATION_DATE, 
            :OLD.LAST_UPDATE_DATE, 
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_psr_request_pkg AS
    PROCEDURE get_issued_psrs(results_out      OUT SYS_REFCURSOR,
                              court_id_in      IN  XHB_COURT.court_id%TYPE);

    PROCEDURE get_unissued_psrs(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_COURT.court_id%TYPE);										 
END xhb_psr_request_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_psr_request_pkg AS
    PROCEDURE get_issued_psrs(results_out      OUT SYS_REFCURSOR,
                              court_id_in      IN  XHB_COURT.court_id%TYPE) AS
       BEGIN
	        OPEN results_out FOR
				SELECT psr.psr_request_id PSR_REQUEST_ID,
					   case.case_type ||case.case_number CASENUMBER,
					   def.first_name||' '||def.middle_name||' '||def.surname DEFENDANT,
					   psr.psr_court_room PSR_COURT_ROOM,
					   psr.psr_status PSR_STATUS
				FROM   XHB_PSR_REQUEST psr,
					   XHB_CASE case,
					   XHB_DEFENDANT_ON_CASE doc,
					   XHB_DEFENDANT def
				WHERE  psr.defendant_on_case_id = doc.defendant_on_case_id
				AND	   doc.defendant_id = def.defendant_id
				AND	   doc.case_id = case.case_id
				AND    case.court_id = court_id_in
				AND	   psr.psr_status IS NOT NULL
				AND    psr.psr_status IN ('ISSUED')
				ORDER BY psr.last_update_date,
						 defendant;
       END get_issued_psrs;

    PROCEDURE get_unissued_psrs(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_COURT.court_id%TYPE) AS
       BEGIN
	        OPEN results_out FOR
				SELECT psr.psr_request_id PSR_REQUEST_ID,
					   case.case_type ||case.case_number CASENUMBER,
					   def.first_name||' '||def.middle_name||' '||def.surname DEFENDANT,
					   psr.psr_court_room PSR_COURT_ROOM,
					   psr.psr_status PSR_STATUS
				FROM   XHB_PSR_REQUEST psr,
					   XHB_CASE case,
					   XHB_DEFENDANT_ON_CASE doc,
					   XHB_DEFENDANT def
				WHERE  psr.defendant_on_case_id = doc.defendant_on_case_id
				AND	   doc.defendant_id = def.defendant_id
				AND	   doc.case_id = case.case_id
				AND    case.court_id = court_id_in
				AND	   (psr.psr_status IS NULL
					   OR psr.psr_status NOT IN ('ISSUED'))
				ORDER BY psr.last_update_date,
						 defendant;
       END get_unissued_psrs;

END xhb_psr_request_pkg;
/
show errors

-------------------------------------------------------------------------------
-- THE PACKAGE HEADER
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE xhb_search_pkg AS
    PROCEDURE get_contact_detail(p_results_out      OUT SYS_REFCURSOR,
                                 p_address_id_in    IN  XHB_CONTACT_DETAIL.address_id%TYPE,
                                 p_contact_type_in  IN  XHB_CONTACT_DETAIL.contact_type%TYPE,
                                 p_contact_value_in IN  XHB_CONTACT_DETAIL.contact_value%TYPE);


    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE);


    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE);


    PROCEDURE get_terminal(p_results_out      OUT SYS_REFCURSOR,
                           p_terminal_name_in IN  XHB_TERMINAL.terminal_name%TYPE);


    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE);


    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE);


    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE);


    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE);


    PROCEDURE get_ref_court_reporter_firm(p_results_out  OUT SYS_REFCURSOR,
                                          p_firm_name_in IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                          p_court_id_in  IN  XHB_REF_COURT_REPORTER_FIRM.court_id%TYPE);


    PROCEDURE get_ref_disposal(p_results_out       OUT SYS_REFCURSOR,
                               p_disposal_code_in  IN  XHB_REF_DISPOSAL.disposal_code%TYPE,
                               p_disposal_title_in IN  XHB_REF_DISPOSAL.disposal_title%TYPE,
                               p_court_id_in       IN  XHB_REF_DISPOSAL.court_id%TYPE);


    PROCEDURE get_ref_disposal_menu(p_results_out           OUT SYS_REFCURSOR,
                                    p_abbrev_in             IN  XHB_REF_DISPOSAL_MENU.abbrev%TYPE,
                                    p_court_id_in           IN  XHB_REF_DISPOSAL_MENU.court_id%TYPE,
                                    p_crest_menu_item_id_in IN  XHB_REF_DISPOSAL_MENU.crest_menu_item_id%TYPE,
                                    p_disposal_code_in      IN  XHB_REF_DISPOSAL_MENU.disposal_code%TYPE,
                                    p_menu_group_in         IN  XHB_REF_DISPOSAL_MENU.menu_group%TYPE,
                                    p_parent_in             IN  XHB_REF_DISPOSAL_MENU.parent%TYPE,
                                    p_title_in              IN  XHB_REF_DISPOSAL_MENU.title%TYPE);


    PROCEDURE get_ref_hearing_type(p_results_out             OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in    IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
				   p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE);


    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE);


    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE);


    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE);


    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE);


    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE);


    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE);


    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE);


    /*
    -- All of these ones have a criteria object but am unable to complete the
    -- stored procedures for them, and therefore they are being excluded from
    -- the fast lane reader process for now...
    PROCEDURE get_plea(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_chamber(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_home_office_Proceeding(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_prosecutor_agency(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_verdict(p_results_out OUT SYS_REFCURSOR);
    */
END xhb_search_pkg;
/
show errors

-------------------------------------------------------------------------------
-- THE PACKAGE BODY
--
-- The xhb_search_pkg contains all of the procedures used by the fast
-- lane readers.
-- 
-- Couple of comments on the package structure:
--
-- The method log_entry (and the script to create the required table) has been
-- left in following investiations into the number of times each method was
-- called (and the resultant changes).
--
-- Each method that requires a call to the convert_values function (which is
-- now package private) does so in the declaration section of the procedure,
-- this is to prevent the call being performed for every row in the query.
-- (this resulted from the log_entry investigation).
--
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY xhb_search_pkg AS

--    CREATE TABLE search_pkg_calls(procedure_name VARCHAR2(100), access_time DATE DEFAULT SYSDATE);
--
--    PROCEDURE log_entry(procedure_name_in IN search_pkg_calls.procedure_name%TYPE)
--    AS
--        PRAGMA AUTONOMOUS_TRANSACTION;
--    BEGIN
--        INSERT INTO search_pkg_calls (procedure_name) VALUES (procedure_name_in);
--        COMMIT;
--    END log_entry;





    -- TBD: Would declaring this deterministic be of benefit?
    -- How common are the search strings?
    FUNCTION convert_value(p_value_in IN VARCHAR2)
        RETURN VARCHAR2 AS
    BEGIN
        --log_entry('convert_value()');
    
        -- Only convert to uppercase for now
        RETURN UPPER(p_value_in);
    END convert_value;





    --
    -- Query the XHB_CONTACT_DETAIL table
    --
    PROCEDURE get_contact_detail(p_results_out      OUT SYS_REFCURSOR,
                                 p_address_id_in    IN  XHB_CONTACT_DETAIL.address_id%TYPE,
                                 p_contact_type_in  IN  XHB_CONTACT_DETAIL.contact_type%TYPE,
                                 p_contact_value_in IN  XHB_CONTACT_DETAIL.contact_value%TYPE)
    AS
        l_contact_type  CONSTANT XHB_CONTACT_DETAIL.contact_type%TYPE  := convert_value(p_contact_type_in);
        l_contact_value CONSTANT XHB_CONTACT_DETAIL.contact_value%TYPE := convert_value(p_contact_value_in);
    BEGIN
        --log_entry('get_contact_detail');

        OPEN p_results_out FOR
            SELECT cd.contact_id AS "id",
                   cd.*
            FROM   XHB_CONTACT_DETAIL cd
            WHERE  ((p_address_id_in IS NULL)
                        OR (cd.address_id = p_address_id_in))
            AND    ((l_contact_type IS NULL)
                        OR (UPPER(cd.contact_type)  LIKE l_contact_type))
            AND    ((l_contact_value IS NULL)
                        OR (UPPER(cd.contact_value) LIKE l_contact_value));
    END get_contact_detail;


    --
    -- Query the XHB_COURT table, with some details from the XHB_COURT_SITE table
    --
    PROCEDURE get_court(p_results_out       OUT SYS_REFCURSOR,
                        p_circuit_in        IN  XHB_COURT.circuit%TYPE,
                        p_court_site_id_in  IN  XHB_COURT_SITE.court_site_id%TYPE,
                        p_court_name_in     IN  XHB_COURT.court_name%TYPE,
                        p_court_prefix_in   IN  XHB_COURT.court_prefix%TYPE,
                        p_court_type_in     IN  XHB_COURT.court_type%TYPE,
                        p_crest_court_id_in IN  XHB_COURT.crest_court_id%TYPE,
                        p_short_name_in     IN  XHB_COURT.short_name%TYPE)
    AS
        l_circuit        CONSTANT XHB_COURT.circuit%TYPE        := convert_value(p_circuit_in);
        l_court_name     CONSTANT XHB_COURT.court_name%TYPE     := convert_value(p_court_name_in);
        l_court_prefix   CONSTANT XHB_COURT.court_prefix%TYPE   := convert_value(p_court_prefix_in);
        l_court_type     CONSTANT XHB_COURT.court_type%TYPE     := convert_value(p_court_type_in);
        l_crest_court_id CONSTANT XHB_COURT.crest_court_id%TYPE := convert_value(p_crest_court_id_in);
        l_short_name     CONSTANT XHB_COURT.short_name%TYPE     := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court');
    
        OPEN p_results_out FOR
            SELECT c.court_id AS "id",
                   cs.court_site_code AS "COURT_CODE",
                   c.*
            FROM   XHB_COURT c,
                   XHB_COURT_SITE cs
            WHERE  c.court_id                =    cs.court_id
            AND    ((c.obs_ind IS NULL) OR (c.obs_ind = 'N'))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id        =    p_court_site_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)        LIKE l_circuit))
            AND    ((l_court_name IS NULL)
                        OR (UPPER(c.court_name)     LIKE l_court_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(c.court_prefix)   LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(c.court_type)     LIKE l_court_type))
            AND    ((l_crest_court_id IS NULL)   
                        OR (UPPER(c.crest_court_id) LIKE l_crest_court_id))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)     LIKE l_short_name));
    END get_court;


    --
    -- Query the XHB_COURT_ROOM table, but also with search criteria from
    -- XHB_COURT_SITE and XHB_COURT
    --
    PROCEDURE get_court_room(p_results_out            OUT SYS_REFCURSOR,
                             p_court_room_name_in     IN  XHB_COURT_ROOM.court_room_name%TYPE,
                             p_court_site_code_in     IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_site_id_in       IN  XHB_COURT_SITE.court_site_id%TYPE,
                             p_crest_court_room_no_in IN  XHB_COURT_ROOM.crest_court_room_no%TYPE,
                             p_short_name_in          IN  XHB_COURT.short_name%TYPE)
    AS
        l_court_room_name CONSTANT XHB_COURT_ROOM.court_room_name%TYPE := convert_value(p_court_room_name_in);
        l_court_site_code CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_short_name      CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_short_name_in);
    BEGIN
        --log_entry('get_court_room');
    
        OPEN p_results_out FOR
            SELECT DISTINCT cr.court_room_id AS "id",
                   t.location AS location,
                   cr.*
            FROM   XHB_COURT_ROOM cr,
                   XHB_COURT_SITE cs,
                   XHB_COURT c,
                   XHB_TERMINAL t
            WHERE  cr.court_site_id = cs.court_site_id
            AND    c.court_id       = cs.court_id
            AND    cr.court_room_id = t.court_room_id(+)
            AND    ((cr.obs_ind IS NULL) OR (cr.obs_ind = 'N'))
            AND    ((l_court_room_name IS NULL)
                        OR (UPPER(cr.court_room_name)  LIKE l_court_room_name))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code)  LIKE l_court_site_code))
            AND    ((p_court_site_id_in IS NULL)
                        OR (cs.court_site_id           =    p_court_site_id_in))
            AND    ((p_crest_court_room_no_in IS NULL)
                        OR (cr.crest_court_room_no     =    p_crest_court_room_no_in))
            AND    ((l_short_name IS NULL)
                        OR (UPPER(c.short_name)        LIKE l_short_name));
    END get_court_room;


    --
    -- Query the XHB_COURT_SITE table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_court_site(p_results_out         OUT SYS_REFCURSOR,
                             p_court_site_code_in  IN  XHB_COURT_SITE.court_site_code%TYPE,
                             p_court_id_in         IN  XHB_COURT.court_id%TYPE,
                             p_court_short_name_in IN  XHB_COURT.short_name%TYPE,
                             p_court_site_name_in  IN  XHB_COURT_SITE.court_site_name%TYPE)
    AS
        l_court_site_code  CONSTANT XHB_COURT_SITE.court_site_code%TYPE := convert_value(p_court_site_code_in);
        l_court_short_name CONSTANT XHB_COURT.short_name%TYPE           := convert_value(p_court_short_name_in);
        l_court_site_name  CONSTANT XHB_COURT_SITE.court_site_name%TYPE := convert_value(p_court_site_name_in);
    BEGIN
        --log_entry('get_court_site');
    
        OPEN p_results_out FOR
            SELECT cs.court_site_id AS "id",
                   cs.*
            FROM   XHB_COURT_SITE cs,
                   XHB_COURT c
            WHERE  cs.court_id = c.court_id
            AND    ((cs.obs_ind IS NULL) OR (cs.obs_ind = 'N'))
            AND    ((l_court_site_code IS NULL)
                        OR (UPPER(cs.court_site_code) LIKE l_court_site_code))
            AND    ((p_court_id_in IS NULL)
                        OR (c.court_id                =    p_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(c.short_name)       LIKE l_court_short_name))
            AND    ((l_court_site_name IS NULL)
                        OR (UPPER(cs.court_site_name) LIKE l_court_site_name));
    END get_court_site;


    --
    -- Query the XHB_TERMINAL table
    --
    PROCEDURE get_terminal(p_results_out      OUT SYS_REFCURSOR,
                           p_terminal_name_in IN  XHB_TERMINAL.terminal_name%TYPE)
    AS
        l_terminal_name CONSTANT XHB_TERMINAL.terminal_name%TYPE := convert_value(p_terminal_name_in);
    BEGIN
        --log_entry('get_terminal');
    
        OPEN p_results_out FOR
            SELECT t.terminal_id AS "id",
                   t.*
            FROM   XHB_TERMINAL t
            WHERE  ((l_terminal_name IS NULL)
                        OR (UPPER(t.terminal_name) LIKE l_terminal_name));
    END get_terminal;


    --
    -- Query the XHB_REF_ADVOCATE table, with some details from
    -- XHB_REF_LEGAL_REPRESENTATIVE, XHB_REF_CHAMBER and XHB_ADDRESS
    --
    PROCEDURE get_ref_advocate_complex(p_results_out         OUT SYS_REFCURSOR,
                                       p_adv_type_ind_in     IN  XHB_REF_ADVOCATE.adv_type_ind%TYPE,
                                       p_initials_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                                       p_first_name_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                       p_middle_name_in      IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                                       p_surname_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                       p_ref_legal_rep_id_in IN  XHB_REF_ADVOCATE.ref_legal_rep_id%TYPE,
                                       p_firm_name_in        IN  XHB_REF_CHAMBER.firm_name%TYPE,
                                       p_court_id_in         IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE)
    AS
        l_adv_type_ind CONSTANT XHB_REF_ADVOCATE.adv_type_ind%TYPE            := convert_value(p_adv_type_ind_in);
        l_initials     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE    := convert_value(p_initials_in);
        l_first_name   CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name  CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname      CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE     := convert_value(p_surname_in);
        l_firm_name    CONSTANT XHB_REF_CHAMBER.firm_name%TYPE                := convert_value(p_firm_name_in);
    BEGIN
        --log_entry('get_ref_advocate_complex');

        OPEN p_results_out FOR
            SELECT ra.ref_advocate_id AS "id",
                   rlr.title,
                   rlr.first_name,
                   rlr.initials,
                   rlr.middle_name,
                   rlr.surname,
                   rlr.court_id,
                   rlr.legal_rep_type,
                   ra.ref_chamber_id AS chamber_id,
                   ra.ref_legal_rep_id AS legal_rep_id,
                   ra.bar_no,
                   ra.is_global,
                   ra.crest_Advocate_Id,
                   ra.version,
                   ra.year_Of_Call,
                   ra.vat_No,
                   ra.crest_Chamber_Id,
                   ra.honours,
                   ra.adv_Type_Ind,
                   ra.obs_ind,
                   rc.firm_name,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode
            FROM   XHB_REF_ADVOCATE ra,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_CHAMBER rc,
                   XHB_ADDRESS a
            WHERE  ra.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ra.ref_chamber_id = rc.ref_chamber_id
            AND    rc.address_id = a.address_id(+)
            AND    ((ra.obs_ind IS NULL) OR (ra.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id           =    p_court_id_in))
            AND    ((l_adv_type_ind IS NULL)
                        OR (UPPER(ra.adv_type_ind) LIKE l_adv_type_ind))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (ra.ref_legal_rep_id    =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)     LIKE l_surname))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rc.firm_name)    LIKE l_firm_name))
            ORDER BY rlr.surname;
    END get_ref_advocate_complex;


    --
    -- Query the XHB_REF_APP_RESULT table
    --
    PROCEDURE get_ref_app_result(p_results_out        OUT SYS_REFCURSOR,
                                 p_court_id_in        IN  XHB_REF_APP_RESULT.court_id%TYPE,
                                 p_app_result_code_in IN  XHB_REF_APP_RESULT.app_result_code%TYPE,
                                 p_ho_code_in         IN  XHB_REF_APP_RESULT.ho_code%TYPE,
                                 p_vary_sentence_in   IN  XHB_REF_APP_RESULT.vary_sentence%TYPE,
                                 p_lesser_off_ind_in  IN  XHB_REF_APP_RESULT.lesser_off_ind%TYPE)
    AS
        l_app_result_code CONSTANT XHB_REF_APP_RESULT.app_result_code%TYPE := convert_value(p_app_result_code_in);
        l_vary_sentence   CONSTANT XHB_REF_APP_RESULT.vary_sentence%TYPE   := convert_value(p_vary_sentence_in);
        l_lesser_off_ind  CONSTANT XHB_REF_APP_RESULT.lesser_off_ind%TYPE  := convert_value(p_lesser_off_ind_in);
    BEGIN
        --log_entry('get_ref_app_result');
    
        OPEN p_results_out FOR
            SELECT rap.ref_app_result_id AS "id",
                   rap.ref_app_result_id AS ref_App_Res_Id,
                   rap.app_result_code AS code,
                   rap.app_result_descr1 AS description1,
                   rap.app_result_descr2 AS description2,
                   rap.court_id,
                   rap.vary_sentence,
                   rap.version,
                   rap.ho_code,
                   rap.lesser_off_ind,
                   rap.obs_ind
            FROM   XHB_REF_APP_RESULT rap
            WHERE  ((rap.obs_ind IS NULL) OR (rap.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rap.court_id               =    p_court_id_in))
            AND    ((l_app_result_code IS NULL)
                        OR (UPPER(rap.app_result_code) LIKE l_app_result_code))
            AND    ((p_ho_code_in IS NULL)
                        OR (rap.ho_code                =    p_ho_code_in))
            AND    ((l_vary_sentence IS NULL)
                        OR (UPPER(rap.vary_sentence)   LIKE l_vary_sentence))
            AND    ((l_lesser_off_ind IS NULL)
                        OR (UPPER(rap.lesser_off_ind)  LIKE l_lesser_off_ind));
    END get_ref_app_result;


    --
    -- Query the XHB_REF_COURT table, but also with search criteria
    -- from XHB_COURT
    --
    PROCEDURE get_ref_court(p_results_out         OUT SYS_REFCURSOR,
                            p_court_id_in         IN  XHB_REF_COURT.court_id%TYPE,
                            p_circuit_in          IN  XHB_COURT.circuit%TYPE,
                            p_court_full_name_in  IN  XHB_REF_COURT.court_full_name%TYPE,
                            p_court_prefix_in     IN  XHB_REF_COURT.name_prefix%TYPE,
                            p_court_type_in       IN  XHB_REF_COURT.court_type%TYPE,
                            p_crest_court_id_in   IN  XHB_COURT.crest_court_id%TYPE,
                            p_court_short_name_in IN  XHB_REF_COURT.court_short_name%TYPE,
                            p_is_psd_in           IN  XHB_REF_COURT.is_psd%TYPE)
    AS
        l_circuit          CONSTANT XHB_COURT.circuit%TYPE              := convert_value(p_circuit_in);
        l_court_full_name  CONSTANT XHB_REF_COURT.court_full_name%TYPE  := convert_value(p_court_full_name_in);
        l_court_prefix     CONSTANT XHB_REF_COURT.name_prefix%TYPE      := convert_value(p_court_prefix_in);
        l_court_type       CONSTANT XHB_REF_COURT.court_type%TYPE       := convert_value(p_court_type_in);
        l_court_short_name CONSTANT XHB_REF_COURT.court_short_name%TYPE := convert_value(p_court_short_name_in);
        l_is_psd           CONSTANT XHB_REF_COURT.is_psd%TYPE           := convert_value(p_is_psd_in);
    BEGIN
        --log_entry('get_ref_court');

        OPEN p_results_out FOR
            SELECT rc.ref_court_id AS "id",
                   rc.*
            FROM   XHB_REF_COURT rc,
                   XHB_COURT c
            WHERE  rc.court_id                =    c.court_id
            AND    ((rc.obs_ind IS NULL) OR (rc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rc.court_id                =    p_court_id_in))
            AND    ((l_circuit IS NULL)
                        OR (UPPER(c.circuit)           LIKE l_circuit))
            AND    ((l_court_full_name IS NULL)
                        OR (UPPER(rc.court_full_name)  LIKE l_court_full_name))
            AND    ((l_court_prefix IS NULL)
                        OR (UPPER(rc.name_prefix)      LIKE l_court_prefix))
            AND    ((l_court_type IS NULL)
                        OR (UPPER(rc.court_type)       LIKE l_court_type))
            AND    ((p_crest_court_id_in IS NULL)
                        OR (c.crest_court_id           =    p_crest_court_id_in))
            AND    ((l_court_short_name IS NULL)
                        OR (UPPER(rc.court_short_name) LIKE l_court_short_name))
            AND    ((l_is_psd IS NULL)
                        OR (UPPER(rc.is_psd)           LIKE l_is_psd));
    END get_ref_court;


    --
    -- Query the XHB_REF_COURT_REPORTER table, but also with search criteria
    -- from XHB_REF_COURT_REPORTER_FIRM
    --
    PROCEDURE get_ref_court_reporter(p_results_out    OUT SYS_REFCURSOR,
                                     p_court_id_in    IN  XHB_REF_COURT_REPORTER.court_id%TYPE,
                                     p_firm_name_in   IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                     p_initials_in    IN  XHB_REF_COURT_REPORTER.initials%TYPE,
                                     p_first_name_in  IN  XHB_REF_COURT_REPORTER.first_name%TYPE,
                                     p_middle_name_in IN  XHB_REF_COURT_REPORTER.middle_name%TYPE,
                                     p_surname_in     IN  XHB_REF_COURT_REPORTER.surname%TYPE)
    AS
        l_firm_name   CONSTANT XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE := convert_value(p_firm_name_in);
        l_initials    CONSTANT XHB_REF_COURT_REPORTER.initials%TYPE       := convert_value(p_initials_in);
        l_first_name  CONSTANT XHB_REF_COURT_REPORTER.first_name%TYPE     := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_COURT_REPORTER.middle_name%TYPE    := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_COURT_REPORTER.surname%TYPE        := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_court_reporter');

        OPEN p_results_out FOR
            SELECT rcr.ref_court_reporter_id AS "id",
                   rcr.*
            FROM   XHB_REF_COURT_REPORTER rcr,
                   XHB_REF_COURT_REPORTER_FIRM rcrf
            WHERE  rcr.ref_court_reporter_firm_id = rcrf.ref_court_reporter_firm_id
            AND    ((rcr.obs_ind IS NULL) OR (rcr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rcr.court_id           =    p_court_id_in))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rcrf.firm_name)  LIKE l_firm_name))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rcr.initials)    LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rcr.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rcr.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rcr.surname)     LIKE l_surname));
    END get_ref_court_reporter;


    --
    -- Query the XHB_REF_COURT_REPORTER_FIRM table
    --
    PROCEDURE get_ref_court_reporter_firm(p_results_out  OUT SYS_REFCURSOR,
                                          p_firm_name_in IN  XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE,
                                          p_court_id_in  IN  XHB_REF_COURT_REPORTER_FIRM.court_id%TYPE)
    AS
        l_firm_name CONSTANT XHB_REF_COURT_REPORTER_FIRM.firm_name%TYPE := convert_value(p_firm_name_in);
    BEGIN
        --log_entry('get_ref_court_reporter_firm');

        OPEN p_results_out FOR
            SELECT rcrf.ref_court_reporter_firm_id AS "id",
                   rcrf.*
            FROM   XHB_REF_COURT_REPORTER_FIRM rcrf
            WHERE  ((rcrf.obs_ind IS NULL) OR (rcrf.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rcrf.court_id = p_court_id_in))
            AND    ((l_firm_name IS NULL)
                        OR (UPPER(rcrf.firm_name) LIKE l_firm_name));
    END get_ref_court_reporter_firm;


    --
    -- Query the XHB_REF_DISPOSAL table
    --
    PROCEDURE get_ref_disposal(p_results_out       OUT SYS_REFCURSOR,
                               p_disposal_code_in  IN  XHB_REF_DISPOSAL.disposal_code%TYPE,
                               p_disposal_title_in IN  XHB_REF_DISPOSAL.disposal_title%TYPE,
                               p_court_id_in       IN  XHB_REF_DISPOSAL.court_id%TYPE)
    AS
        l_disposal_code  CONSTANT XHB_REF_DISPOSAL.disposal_code%TYPE  := convert_value(p_disposal_code_in);
        l_disposal_title CONSTANT XHB_REF_DISPOSAL.disposal_title%TYPE := convert_value(p_disposal_title_in);
    BEGIN
        --log_entry('get_ref_disposal');

        OPEN p_results_out FOR
            SELECT rd.ref_disposal_id AS "id",
                   rd.*
            FROM   XHB_REF_DISPOSAL rd
            WHERE  ((rd.obs_ind IS NULL) OR (rd.obs_ind = 'N'))
            AND    ((l_disposal_code IS NULL)
                        OR (UPPER(rd.disposal_code)  LIKE l_disposal_code))
            AND    ((l_disposal_title IS NULL)
                        OR (UPPER(rd.disposal_title) LIKE l_disposal_title))
            AND    ((p_court_id_in IS NULL)
                        OR (rd.court_id              =    p_court_id_in));
    END get_ref_disposal;


    --
    -- Query the XHB_REF_DISPOSAL_MENU table
    --
    PROCEDURE get_ref_disposal_menu(p_results_out           OUT SYS_REFCURSOR,
                                    p_abbrev_in             IN  XHB_REF_DISPOSAL_MENU.abbrev%TYPE,
                                    p_court_id_in           IN  XHB_REF_DISPOSAL_MENU.court_id%TYPE,
                                    p_crest_menu_item_id_in IN  XHB_REF_DISPOSAL_MENU.crest_menu_item_id%TYPE,
                                    p_disposal_code_in      IN  XHB_REF_DISPOSAL_MENU.disposal_code%TYPE,
                                    p_menu_group_in         IN  XHB_REF_DISPOSAL_MENU.menu_group%TYPE,
                                    p_parent_in             IN  XHB_REF_DISPOSAL_MENU.parent%TYPE,
                                    p_title_in              IN  XHB_REF_DISPOSAL_MENU.title%TYPE)
    AS
        l_abbrev        CONSTANT XHB_REF_DISPOSAL_MENU.abbrev%TYPE        := convert_value(p_abbrev_in);
        l_disposal_code CONSTANT XHB_REF_DISPOSAL_MENU.disposal_code%TYPE := convert_value(p_disposal_code_in);
        l_menu_group    CONSTANT XHB_REF_DISPOSAL_MENU.menu_group%TYPE    := convert_value(p_menu_group_in);
        l_title         CONSTANT XHB_REF_DISPOSAL_MENU.title%TYPE         := convert_value(p_title_in);
    BEGIN
        --log_entry('get_ref_disposal_menu');

        OPEN p_results_out FOR
            SELECT rdm.ref_disposal_menu_id AS "id",
                   rdm.title AS viewable_title,
                   rdm.*
            FROM   XHB_REF_DISPOSAL_MENU rdm
            WHERE  ((rdm.obs_ind IS NULL) OR (rdm.obs_ind = 'N'))
            AND    ((l_abbrev IS NULL)
                        OR (UPPER(rdm.abbrev)        LIKE l_abbrev))
            AND    ((p_court_id_in IS NULL)
                        OR (rdm.court_id             =    p_court_id_in))
            AND    ((p_crest_menu_item_id_in IS NULL)
                        OR (rdm.crest_menu_item_id   =    p_crest_menu_item_id_in))
            AND    ((l_disposal_code IS NULL)
                        OR (UPPER(rdm.disposal_code) LIKE l_disposal_code))
            AND    ((l_menu_group IS NULL)
                        OR (UPPER(rdm.menu_group)    LIKE l_menu_group))
            AND    ((p_parent_in IS NULL)
                        OR (rdm.parent               =    p_parent_in))
            AND    ((l_title IS NULL)
                        OR (UPPER(rdm.title)         LIKE l_title));
    END get_ref_disposal_menu;


    --
    -- Query the XHB_REF_HEARING_TYPE table
    --
    PROCEDURE get_ref_hearing_type(p_results_out          OUT SYS_REFCURSOR,
                                   p_hearing_type_code_in IN  XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                           p_hearing_type_courtid_in IN  XHB_REF_HEARING_TYPE.court_id%TYPE)
    AS
        l_hearing_type_code CONSTANT XHB_REF_HEARING_TYPE.hearing_type_code%TYPE := convert_value(p_hearing_type_code_in);
    BEGIN
        --log_entry('get_ref_hearing_type');

        OPEN p_results_out FOR
            SELECT rht.ref_hearing_type_id AS "id",
                   rht.*
            FROM   XHB_REF_HEARING_TYPE rht
            WHERE  ((rht.obs_ind IS NULL) OR (rht.obs_ind = 'N'))
            AND    ((l_hearing_type_code IS NULL)
                        OR (UPPER(rht.hearing_type_code) LIKE l_hearing_type_code))
	    AND    ((p_hearing_type_courtid_in IS NULL)
                        OR (rht.court_id             =    p_hearing_type_courtid_in ));
    END get_ref_hearing_type;


    --
    -- Query the XHB_REF_JUDGE table
    --
    PROCEDURE get_ref_judge(p_results_out    OUT SYS_REFCURSOR,
                            p_first_name_in  IN  XHB_REF_JUDGE.first_name%TYPE,
                            p_middle_name_in IN  XHB_REF_JUDGE.middle_name%TYPE,
                            p_surname_in     IN  XHB_REF_JUDGE.surname%TYPE,
                            p_court_id_in    IN  XHB_REF_JUDGE.court_id%TYPE)
    AS
        l_first_name  CONSTANT XHB_REF_JUDGE.first_name%TYPE  := convert_value(p_first_name_in);
        l_middle_name CONSTANT XHB_REF_JUDGE.middle_name%TYPE := convert_value(p_middle_name_in);
        l_surname     CONSTANT XHB_REF_JUDGE.surname%TYPE     := convert_value(p_surname_in);
    BEGIN
        --log_entry('get_ref_judge');

        OPEN p_results_out FOR
            SELECT rj.ref_judge_id AS "id",
                   rj.*
            FROM   XHB_REF_JUDGE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind  = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id =  p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rj.first_name)  LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rj.middle_name) LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rj.surname)     LIKE l_surname));
    END get_ref_judge;


    --
    -- Query the XHB_REF_JUSTICE table
    --
    PROCEDURE get_ref_justice(p_results_out     OUT SYS_REFCURSOR,
                              p_justice_name_in IN  XHB_REF_JUSTICE.justice_name%TYPE,
                              p_court_id_in     IN  XHB_REF_JUSTICE.court_id%TYPE)
    AS
        l_justice_name CONSTANT XHB_REF_JUSTICE.justice_name%TYPE := convert_value(p_justice_name_in);
    BEGIN
        --log_entry('get_ref_justice');

        OPEN p_results_out FOR
            SELECT rj.ref_justice_id AS "id",
                   rj.court_id AS court_i_d,
                   rj.*
            FROM   XHB_REF_JUSTICE rj
            WHERE  ((rj.obs_ind IS NULL) OR (rj.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL) OR (rj.court_id = p_court_id_in))
            AND    ((l_justice_name IS NULL)
                        OR (UPPER(rj.justice_name) LIKE l_justice_name));
    END get_ref_justice;


    --
    -- Query the XHB_REF_LEGAL_REPRESENTATIVE table
    --
    PROCEDURE get_ref_legal_representative(p_results_out       OUT SYS_REFCURSOR,
                                           p_court_id_in       IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE,
                                           p_first_name_in     IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                                           p_surname_in        IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                                           p_legal_rep_type_in IN  XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE)
    AS
        l_first_name     CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE     := convert_value(p_first_name_in);
        l_surname        CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE        := convert_value(p_surname_in);
        l_legal_rep_type CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.legal_rep_type%TYPE := convert_value(p_legal_rep_type_in);
    BEGIN
        --log_entry('get_ref_legal_representative');

        OPEN p_results_out FOR
            SELECT rlr.ref_legal_rep_id AS "id",
                   rlr.*
            FROM   XHB_REF_LEGAL_REPRESENTATIVE rlr
            WHERE  ((rlr.obs_ind IS NULL) OR (rlr.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rlr.court_id              =    p_court_id_in))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)     LIKE l_first_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)        LIKE l_surname))
            AND    ((l_legal_rep_type IS NULL)
                        OR (UPPER(rlr.legal_rep_type) LIKE l_legal_rep_type));
    END get_ref_legal_representative;


    --
    -- Query the XHB_REF_OFFENCE table
    --
    PROCEDURE get_ref_offence(p_results_out     OUT SYS_REFCURSOR,
                              p_act_section_in  IN  XHB_REF_OFFENCE.act_section%TYPE,
                              p_court_id_in     IN  XHB_REF_OFFENCE.court_id%TYPE,
                              p_offence_desc_in IN  XHB_REF_OFFENCE.offence_desc%TYPE,
                              p_statute_in      IN  XHB_REF_OFFENCE.statute%TYPE,
                              p_offence_code_in IN  XHB_REF_OFFENCE.offence_code%TYPE)
    AS
        l_act_section  CONSTANT XHB_REF_OFFENCE.act_section%TYPE  := convert_value(p_act_section_in);
        l_offence_desc CONSTANT XHB_REF_OFFENCE.offence_desc%TYPE := convert_value(p_offence_desc_in);
        l_statute      CONSTANT XHB_REF_OFFENCE.statute%TYPE      := convert_value(p_statute_in);
        l_offence_code CONSTANT XHB_REF_OFFENCE.offence_code%TYPE := convert_value(p_offence_code_in);
    BEGIN
        --log_entry('get_ref_offence');

        OPEN p_results_out FOR
            SELECT ro.ref_offence_id AS "id",
                   ro.*,
                   -- Can't find what this IS
                   'OFFENCE TYPE' AS offence_type
            FROM   XHB_REF_OFFENCE ro
            WHERE  ((ro.obs_ind IS NULL) OR (ro.obs_ind = 'N'))
            AND    ((l_act_section IS NULL)
                        OR (UPPER(ro.act_section)  LIKE l_act_section))
            AND    ((p_court_id_in IS NULL)
                        OR (ro.court_id            =    p_court_id_in))
            AND    ((l_offence_desc IS NULL)
                        OR (UPPER(ro.offence_desc) LIKE l_offence_desc))
            AND    ((l_statute IS NULL)
                        OR (UPPER(ro.statute)      LIKE l_statute))
            AND    ((l_offence_code IS NULL)
                        OR (UPPER(ro.offence_code) LIKE l_offence_code))
            ORDER BY ro.offence_code;
    END get_ref_offence;


    --
    -- Query the XHB_REF_SOLICITOR_FIRM table
    --
    PROCEDURE get_ref_solicitor_firm_complex(p_results_out            OUT SYS_REFCURSOR,
                                             p_solicitor_firm_name_in IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                                             p_crest_sof_id_in        IN  XHB_REF_SOLICITOR_FIRM.crest_sof_id%TYPE,
                                             p_court_id_in            IN  XHB_REF_SOLICITOR_FIRM.court_id%TYPE)
    AS
        l_solicitor_firm_name CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_ref_solicitor_firm_complex');

        OPEN p_results_out FOR
            SELECT rsf.ref_solicitor_firm_id AS "id",
                   rsf.*,
                   a.address_id,
                   a.address_1 AS address1,
                   a.address_2 AS address2,
                   a.address_3 AS address3,
                   a.address_4 AS address4,
                   a.town,
                   a.county,
                   a.postcode,
                   a.country
            FROM   XHB_REF_SOLICITOR_FIRM rsf,
                   XHB_ADDRESS a
            WHERE  rsf.address_id = a.address_id
            AND    ((rsf.obs_ind IS NULL) OR (rsf.obs_ind = 'N'))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name))
            AND    ((p_crest_sof_id_in IS NULL)
                        OR (rsf.crest_sof_id               =    p_crest_sof_id_in))
            AND    ((p_court_id_in IS NULL)
                        OR (rsf.court_id                   =    p_court_id_in));
    END get_ref_solicitor_firm_complex;


    --
    -- Query the XHB_REF_SYSTEM_CODE table
    --
    PROCEDURE get_ref_system_code(p_results_out   OUT SYS_REFCURSOR,
                                  p_court_id_in   IN  XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                  p_code_type_in  IN  XHB_REF_SYSTEM_CODE.code_type%TYPE,
                                  p_de_code_in    IN  XHB_REF_SYSTEM_CODE.de_code%TYPE,
                                  p_code_in       IN  XHB_REF_SYSTEM_CODE.code%TYPE,
                                  p_code_title_in IN  XHB_REF_SYSTEM_CODE.code_title%TYPE)
    AS
        l_code_type  CONSTANT XHB_REF_SYSTEM_CODE.code_type%TYPE  := convert_value(p_code_type_in);
        l_de_code    CONSTANT XHB_REF_SYSTEM_CODE.de_code%TYPE    := convert_value(p_de_code_in);
        l_code       CONSTANT XHB_REF_SYSTEM_CODE.code%TYPE       := convert_value(p_code_in);
        l_code_title CONSTANT XHB_REF_SYSTEM_CODE.code_title%TYPE := convert_value(p_code_title_in);
    BEGIN
        --log_entry('get_ref_system_code');

        OPEN p_results_out FOR
            SELECT rsc.ref_system_code_id AS "id",
                   rsc.de_code AS DECODE,
                   rsc.code,
                   rsc.code_Type,
                   rsc.court_Id,
                   rsc.version,
                   rsc.code_Title,
                   rsc.ref_Code_Order,
                   rsc.obs_Ind
            FROM   XHB_REF_SYSTEM_CODE rsc
            WHERE  ((rsc.obs_ind IS NULL) OR (rsc.obs_ind = 'N'))
            AND    ((p_court_id_in IS NULL)
                        OR (rsc.court_id          =    p_court_id_in))
            AND    ((l_code_type IS NULL)
                        OR (UPPER(rsc.code_type)  LIKE l_code_type))
            AND    ((l_de_code IS NULL)
                        OR (UPPER(rsc.de_code)    LIKE l_de_code))
            AND    ((l_code IS NULL)
                        OR (UPPER(rsc.code)       LIKE l_code))
            AND    ((l_code_title IS NULL)
                        OR (UPPER(rsc.code_title) LIKE l_code_title));
    END get_ref_system_code;


    --
    -- Query the XHB_REF_SOLICITOR table, but also with search criteria
    -- from XHB_REF_LEGAL_REPRESENTATIVE and XHB_REF_SOLICITOR_FIRM
    --
    PROCEDURE get_solicitor(p_results_out             OUT SYS_REFCURSOR,
                            p_ref_legal_rep_id_in     IN  XHB_REF_SOLICITOR.ref_legal_rep_id%TYPE,
                            p_initials_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE,
                            p_first_name_in           IN  XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE,
                            p_middle_name_in          IN  XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE,
                            p_surname_in              IN  XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE,
                            p_crest_solicitor_name_in IN  XHB_REF_SOLICITOR.crest_solicitor_name%TYPE,
                            p_solicitor_firm_name_in  IN  XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE,
                            p_court_id_in             IN  XHB_REF_LEGAL_REPRESENTATIVE.court_id%TYPE)
    AS
        l_initials             CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.initials%TYPE      := convert_value(p_initials_in);
        l_first_name           CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.first_name%TYPE    := convert_value(p_first_name_in);
        l_middle_name          CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.middle_name%TYPE   := convert_value(p_middle_name_in);
        l_surname              CONSTANT XHB_REF_LEGAL_REPRESENTATIVE.surname%TYPE       := convert_value(p_surname_in);
        l_crest_solicitor_name CONSTANT XHB_REF_SOLICITOR.crest_solicitor_name%TYPE     := convert_value(p_crest_solicitor_name_in);
        l_solicitor_firm_name  CONSTANT XHB_REF_SOLICITOR_FIRM.solicitor_firm_name%TYPE := convert_value(p_solicitor_firm_name_in);
    BEGIN
        --log_entry('get_solicitor');

        OPEN p_results_out FOR
            SELECT rs.solicitor_id AS "id",
                   rs.*,
                   rlr.*,
                   rsf.ref_solicitor_firm_id AS "firm_id",
                   rs.ref_legal_rep_id AS "legal_rep_id",
                   rs.is_in_crest AS "in_crest"
            FROM   XHB_REF_SOLICITOR rs,
                   XHB_REF_LEGAL_REPRESENTATIVE rlr,
                   XHB_REF_SOLICITOR_FIRM rsf
            WHERE  rs.ref_legal_rep_id = rlr.ref_legal_rep_id
            AND    ((p_court_id_in IS NULL) OR (rlr.court_id = p_court_id_in))
            AND    rs.ref_solicitor_firm_id = rsf.ref_solicitor_firm_id
            AND    ((rs.obs_ind IS NULL) OR (rs.obs_ind = 'N'))
            AND    ((p_ref_legal_rep_id_in IS NULL)
                        OR (rs.ref_legal_rep_id            =    p_ref_legal_rep_id_in))
            AND    ((l_initials IS NULL)
                        OR (UPPER(rlr.initials)            LIKE l_initials))
            AND    ((l_first_name IS NULL)
                        OR (UPPER(rlr.first_name)          LIKE l_first_name))
            AND    ((l_middle_name IS NULL)
                        OR (UPPER(rlr.middle_name)         LIKE l_middle_name))
            AND    ((l_surname IS NULL)
                        OR (UPPER(rlr.surname)             LIKE l_surname))
            AND    ((l_crest_solicitor_name IS NULL)
                        OR (UPPER(rs.crest_solicitor_name) LIKE l_crest_solicitor_name))
            AND    ((l_solicitor_firm_name IS NULL)
                        OR (UPPER(rsf.solicitor_firm_name) LIKE l_solicitor_firm_name));
    END get_solicitor;


    /*
    -- All of these ones have a criteria object but am unable to complete the
    -- stored procedures for them, and therefore they are being excluded from
    -- the fast lane reader process for now...
    PROCEDURE get_plea(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_chamber(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_home_office_Proceeding(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_ref_prosecutor_agency(p_results_out OUT SYS_REFCURSOR);
    PROCEDURE get_verdict(p_results_out OUT SYS_REFCURSOR);
    */
END xhb_search_pkg;
/
show errors

CREATE OR REPLACE PACKAGE XHB_PUBLIC_DISPLAY_PKG AS
	-- Require this to be declared in the header as we are calling directly
	-- from SQL in the strored procedures
	FUNCTION convert_string(p_str_in IN VARCHAR2) RETURN xhb_number_table_typ DETERMINISTIC;

	PROCEDURE GET_SUMMARY_BY_NAME (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_SUMMARY_BY_NAME_U (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_JURY_STATUS_DAILY_LIST (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_JURY_STATUS_DAILY_LIST_U (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_COURT_LIST (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_ALL_COURT_STATUS (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2);
	PROCEDURE GET_COURT_DETAIL (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE);
	PROCEDURE GET_PUBLIC_NOTICES (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE);
	PROCEDURE GET_ACTIVE_CASES_IN_ROOM (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		LIST_ID_IN		IN	XHB_HEARING_LIST.LIST_ID%TYPE,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
		SCHEDULED_HEARING_ID_IN	IN	XHB_SCHEDULED_HEARING.SCHEDULED_HEARING_ID%TYPE);
END XHB_PUBLIC_DISPLAY_PKG;
/
show errors

CREATE OR REPLACE PACKAGE BODY XHB_PUBLIC_DISPLAY_PKG AS
	-- function used to convert a String of comma-delimited numbers into
	-- an array, declared deterministic as it is probable that the passed
	-- in values will be similar/same each call
    FUNCTION convert_string(p_str_in IN VARCHAR2) RETURN xhb_number_table_typ DETERMINISTIC
	AS
		l_size             CONSTANT NUMBER := LENGTH(p_str_in) + 1;
		l_min_index        NUMBER := 1;
		l_max_index        NUMBER;
        l_array            xhb_number_table_typ := xhb_number_table_typ();
	BEGIN
		-- If the passed in parameter is null, then there are no values!
		IF (p_str_in IS NOT NULL) THEN
			LOOP
				l_max_index := INSTR(p_str_in, ',', l_min_index);
				-- l_max_index will be 0 if there are no further occurences, but we need to get
				-- the remaining characters for the last entry
				IF l_max_index = 0 THEN
					l_max_index := l_size;
				END IF;

				l_array.EXTEND;
				l_array(l_array.last) := TO_NUMBER(trim(SUBSTR(p_str_in, l_min_index, (l_max_index - l_min_index))));

				EXIT WHEN l_max_index = l_size;
				l_min_index := l_max_index + 1;
			END LOOP;
		END IF;
       
		RETURN l_array;
	END convert_string;

        -- This stored procedure is used to get the summary by name data
	PROCEDURE GET_SUMMARY_BY_NAME (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT
				SITTING.IS_FLOATING AS IS_FLOATING,
				COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME, 
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
			AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
			AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID
			AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
			AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID
			AND HEARING_LIST.COURT_ID = COURT_ID_IN
			AND HEARING_LIST.START_DATE = START_DATE_IN	
			AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
			AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
			AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
			AND SITTING.IS_FLOATING='0'
			AND DEFENDANT_ON_CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
			ORDER BY
				DEFENDANT_SURNAME,
				DEFENDANT_FIRST_NAME, 
				DEFENDANT_MIDDLE_NAME;

	END GET_SUMMARY_BY_NAME;

        -- This stored procedure is used to get the summary by name data
	PROCEDURE GET_SUMMARY_BY_NAME_U (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT
				SITTING.IS_FLOATING AS IS_FLOATING,
				COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME, 
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
			AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
			AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID
			AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
			AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID
			AND HEARING_LIST.COURT_ID = COURT_ID_IN
			AND HEARING_LIST.START_DATE = START_DATE_IN	
			AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
			AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
			AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
			AND DEFENDANT_ON_CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
			ORDER BY
				DEFENDANT_SURNAME,
				DEFENDANT_FIRST_NAME, 
				DEFENDANT_MIDDLE_NAME;
	END GET_SUMMARY_BY_NAME_U;

	-- This stored procedure is used to get the jury current status and daily list WITHOUT unassigned cases
	PROCEDURE GET_JURY_STATUS_DAILY_LIST (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
				SITTING.IS_FLOATING AS IS_FLOATING,
				COURT_ROOM.CREST_COURT_ROOM_NO AS CREST_COURT_ROOM_NO,
				SITTING.SITTING_SEQUENCE_NO AS SITTING_SEQUENCE_NO,
				NVL(SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME) AS SCHEDULED_HEARING_TIME_SORT,
				SCHEDULED_HEARING.SEQUENCE_NO AS SCHEDULED_HEARING_SEQUENCE_NO,
				COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				TMP_COURT_ROOM.COLUMN_VALUE AS LIST_COURT_ROOM_ID,  -- What court room is currently being listed.
				                                                    -- Used to determine how the case has moved.
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
				REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
				REF_JUDGE.SURNAME AS JUDGE_SURNAME, 
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
				SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_REF_JUDGE REF_JUDGE,
				XHB_COURT_SITE COURT_SITE,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                        TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
				AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND HEARING.CASE_ID = CASE.CASE_ID
				AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
				AND HEARING_LIST.COURT_ID = COURT_ID_IN
				AND HEARING_LIST.START_DATE = START_DATE_IN	
				AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
				AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)	
				AND SITTING.IS_FLOATING = 0
			ORDER BY COURT_SITE_CODE,
				IS_FLOATING,
				COURT_ROOM.CREST_COURT_ROOM_NO,
				SITTING_SEQUENCE_NO,
				NVL(SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME),
				SCHEDULED_HEARING.SEQUENCE_NO;
				
	END GET_JURY_STATUS_DAILY_LIST;

	-- This stored procedure is used to get the jury current status and daily list WITH unassigned cases
	PROCEDURE GET_JURY_STATUS_DAILY_LIST_U (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
				SITTING.IS_FLOATING AS IS_FLOATING,
				COURT_ROOM.CREST_COURT_ROOM_NO AS CREST_COURT_ROOM_NO,
				SITTING.SITTING_SEQUENCE_NO AS SITTING_SEQUENCE_NO,
				NVL(SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME) AS SCHEDULED_HEARING_TIME_SORT,
				SCHEDULED_HEARING.SEQUENCE_NO AS SCHEDULED_HEARING_SEQUENCE_NO,
				COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				TMP_COURT_ROOM.COLUMN_VALUE AS LIST_COURT_ROOM_ID,  -- What court room is currently being listed.
				                                                    -- Used to determine how the case has moved.
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
				REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
				REF_JUDGE.SURNAME AS JUDGE_SURNAME, 
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
				SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_REF_JUDGE REF_JUDGE,
				XHB_COURT_SITE COURT_SITE,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
				AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND HEARING.CASE_ID = CASE.CASE_ID
				AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
				AND HEARING_LIST.COURT_ID = COURT_ID_IN
				AND HEARING_LIST.START_DATE = START_DATE_IN	
				AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
				AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)	
				AND SITTING.IS_FLOATING = 0
		UNION
			SELECT COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
				SITTING.IS_FLOATING AS IS_FLOATING,
				COURT_ROOM.CREST_COURT_ROOM_NO AS CREST_COURT_ROOM_NO,
				SITTING.SITTING_SEQUENCE_NO AS SITTING_SEQUENCE_NO,
				NVL(SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME) AS SCHEDULED_HEARING_TIME_SORT,
				SCHEDULED_HEARING.SEQUENCE_NO AS SCHEDULED_HEARING_SEQUENCE_NO, 
				COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				COURT_ROOM.COURT_ROOM_ID AS LIST_COURT_ROOM_ID,  -- What court room is currently being listed.
				                                                    -- Used to determine how the case has moved.
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
				REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
				REF_JUDGE.SURNAME AS JUDGE_SURNAME, 
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
				SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_REF_JUDGE REF_JUDGE,
				XHB_COURT_SITE COURT_SITE,
				XHB_CASE_REFERENCE CASE_REFERENCE
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
				AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND HEARING.CASE_ID = CASE.CASE_ID
				AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
				AND HEARING_LIST.COURT_ID = COURT_ID_IN
				AND HEARING_LIST.START_DATE = START_DATE_IN	
				AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)	
				AND SITTING.IS_FLOATING = 1
		ORDER BY COURT_SITE_CODE,
			IS_FLOATING,
			CREST_COURT_ROOM_NO,
			SITTING_SEQUENCE_NO,
			SCHEDULED_HEARING_TIME_SORT,
			SCHEDULED_HEARING_SEQUENCE_NO;
				
	END GET_JURY_STATUS_DAILY_LIST_U;

	-- This stored procedure is used to get the court list
	PROCEDURE GET_COURT_LIST (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME, 
				TMP_COURT_ROOM.COLUMN_VALUE AS LIST_COURT_ROOM_ID,  -- What court room is currently being listed.
				                                                    -- Used to determine how the case has moved.
				COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
				MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID, 
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
				SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME, 
				SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_REF_JUDGE REF_JUDGE,
				XHB_COURT_SITE COURT_SITE,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                        TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
                	        AND SITTING.IS_FLOATING = '0'
				AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
				AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND HEARING.CASE_ID = CASE.CASE_ID
				AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
				AND HEARING_LIST.COURT_ID = COURT_ID_IN
				AND HEARING_LIST.START_DATE = START_DATE_IN	
				AND (
					SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                			OR SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
				)
				AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)	
		ORDER BY COURT_SITE_CODE,
				IS_FLOATING,
				COURT_ROOM.CREST_COURT_ROOM_NO,
				SITTING_SEQUENCE_NO,
				NVL(SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME),
				SCHEDULED_HEARING.SEQUENCE_NO;
				
	END GET_COURT_LIST;

	-- This stored procedure is used to get the all court status
	PROCEDURE GET_ALL_COURT_STATUS (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_IDS_IN	IN	VARCHAR2) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME, 
				COURT_ROOM.CREST_COURT_ROOM_NO AS CREST_COURT_ROOM_NO,
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				CR_LIVE_STATUS.PUBLIC_DISPLAY_STATUS AS PUBLIC_DISPLAY_STATUS,
				CR_LIVE_STATUS.TIME_STATUS_SET AS TIME_STATUS_SET,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_COURT_SITE COURT_SITE,
				XHB_CR_LIVE_STATUS CR_LIVE_STATUS,
				XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
			AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
			AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
			AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
			AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
			AND HEARING.CASE_ID = CASE.CASE_ID
			AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
			AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
			AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
			AND CR_LIVE_STATUS.SCHEDULED_HEARING_ID = SCHEDULED_HEARING.SCHEDULED_HEARING_ID(+)
			AND CR_LIVE_STATUS.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID(+)
			AND SCHEDULED_HEARING.IS_CASE_ACTIVE = 'Y'
			AND HEARING_LIST.COURT_ID = COURT_ID_IN
			AND HEARING_LIST.START_DATE = START_DATE_IN
			AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
			AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
			UNION
			SELECT COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				COURT_ROOM.CREST_COURT_ROOM_NO AS CREST_COURT_ROOM_NO,
				NULL AS DEFENDANT_ID,
				NULL AS DEFENDANT_FIRST_NAME,
				NULL AS DEFENDANT_MIDDLE_NAME,
				NULL AS DEFENDANT_SURNAME,
				NULL AS CASE_NUMBER, 
				NULL AS CASE_TITLE,
				NULL AS PUBLIC_DISPLAY_STATUS, 
				NULL AS TIME_STATUS_SET,
				NULL AS REPORTING_RESTRICTIONS
			FROM XHB_COURT_ROOM COURT_ROOM,
                TABLE(cast(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
			WHERE COURT_ROOM.COURT_ROOM_ID NOT IN (
				SELECT SITTING.COURT_ROOM_ID 
				FROM XHB_HEARING_LIST HEARING_LIST, 
					XHB_SITTING SITTING, 
					XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
					XHB_CR_LIVE_STATUS CR_LIVE_STATUS
				WHERE 
					HEARING_LIST.LIST_ID = SITTING.LIST_ID
					AND SITTING.SITTING_ID = SCHEDULED_HEARING.SITTING_ID
					AND SCHEDULED_HEARING.IS_CASE_ACTIVE = 'Y'
					AND HEARING_LIST.COURT_ID = COURT_ID_IN
					AND HEARING_LIST.START_DATE = START_DATE_IN
					AND CR_LIVE_STATUS.SCHEDULED_HEARING_ID = SCHEDULED_HEARING.SCHEDULED_HEARING_ID )
            AND COURT_ROOM.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
			ORDER BY CREST_COURT_ROOM_NO;
				
	END GET_ALL_COURT_STATUS;

	-- This stored procedure is used to get the COURT DETAIL
	PROCEDURE GET_COURT_DETAIL (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ID_IN		IN	XHB_HEARING_LIST.court_id%TYPE,
		START_DATE_IN		IN 	XHB_HEARING_LIST.start_date%TYPE,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE) AS
	BEGIN
		
		OPEN RESULTS_OUT FOR
			SELECT COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
				REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
				REF_JUDGE.SURNAME AS JUDGE_SURNAME, 
				DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
				DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME, 
				DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME, 
				DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
				CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
				CASE.CASE_TITLE AS CASE_TITLE,
				REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
				CR_LIVE_STATUS.PUBLIC_DISPLAY_STATUS AS PUBLIC_DISPLAY_STATUS,
				CR_LIVE_STATUS.TIME_STATUS_SET AS TIME_STATUS_SET,
				CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
			FROM  XHB_HEARING_LIST HEARING_LIST,
				XHB_SITTING SITTING,
				XHB_COURT_ROOM COURT_ROOM,
				XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				XHB_HEARING HEARING,
				XHB_CASE CASE,
				XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				XHB_DEFENDANT DEFENDANT,
				XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				XHB_REF_JUDGE REF_JUDGE,
				XHB_CR_LIVE_STATUS CR_LIVE_STATUS,
				XHB_CASE_REFERENCE CASE_REFERENCE
			WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
			AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
			AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
			AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
			AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
			AND HEARING.CASE_ID = CASE.CASE_ID
			AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
			AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
			AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
			AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
			AND CR_LIVE_STATUS.SCHEDULED_HEARING_ID(+) = SCHEDULED_HEARING.SCHEDULED_HEARING_ID
			AND SCHEDULED_HEARING.IS_CASE_ACTIVE = 'Y'
			AND HEARING_LIST.COURT_ID = COURT_ID_IN
			AND HEARING_LIST.START_DATE = START_DATE_IN
			AND SITTING.COURT_ROOM_ID = COURT_ROOM_ID_IN
			AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+);
				
	END GET_COURT_DETAIL;

	-- This stored procedure is used to get the public notices for a court room
	PROCEDURE GET_PUBLIC_NOTICES (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE) AS
	BEGIN
		
		OPEN RESULTS_OUT FOR
			SELECT PUBLIC_NOTICE.PUBLIC_NOTICE_DESC AS PUBLIC_NOTICE_DESC,
				CONFIGURED_PUBLIC_NOTICE.IS_ACTIVE AS IS_ACTIVE,
				DEFINITIVE_PUBLIC_NOTICE.PRIORITY AS PRIORITY
			FROM XHB_PUBLIC_NOTICE PUBLIC_NOTICE,
				XHB_CONFIGURED_PUBLIC_NOTICE CONFIGURED_PUBLIC_NOTICE,
				XHB_DEFINITIVE_PUBLIC_NOTICE DEFINITIVE_PUBLIC_NOTICE
			WHERE PUBLIC_NOTICE.PUBLIC_NOTICE_ID = CONFIGURED_PUBLIC_NOTICE.PUBLIC_NOTICE_ID
			AND DEFINITIVE_PUBLIC_NOTICE.DEFINITIVE_PN_ID = PUBLIC_NOTICE.DEFINITIVE_PN_ID
			AND CONFIGURED_PUBLIC_NOTICE.IS_ACTIVE = 1
			AND CONFIGURED_PUBLIC_NOTICE.COURT_ROOM_ID = COURT_ROOM_ID_IN
			AND ROWNUM <= 5
            ORDER BY DEFINITIVE_PUBLIC_NOTICE.PRIORITY;
	END GET_PUBLIC_NOTICES;

	-- This stored procedure is to get all active cases in a court room except for the 
	-- one passed in. Used to identify active cases to turn off
	PROCEDURE GET_ACTIVE_CASES_IN_ROOM (
		RESULTS_OUT		OUT	SYS_REFCURSOR,
		LIST_ID_IN		IN	XHB_HEARING_LIST.LIST_ID%TYPE,
		COURT_ROOM_ID_IN	IN	XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
		SCHEDULED_HEARING_ID_IN	IN	XHB_SCHEDULED_HEARING.SCHEDULED_HEARING_ID%TYPE) AS
	BEGIN
		OPEN RESULTS_OUT FOR
			SELECT	SH.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
			FROM	XHB_SCHEDULED_HEARING SH, XHB_SITTING S
			WHERE	SH.IS_CASE_ACTIVE='Y'
			AND	S.SITTING_ID = SH.SITTING_ID
			AND	S.LIST_ID = LIST_ID_IN 
			AND	S.COURT_ROOM_ID = COURT_ROOM_ID_IN
			AND	SH.SCHEDULED_HEARING_ID != SCHEDULED_HEARING_ID_IN;
	END GET_ACTIVE_CASES_IN_ROOM;

END XHB_PUBLIC_DISPLAY_PKG;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_list_distribution_pkg AS

  PROCEDURE get_wll_unsub_rec_by_court_id (p_unsub_recip_cur IN OUT SYS_REFCURSOR,
                                           p_court_id        IN     NUMBER) IS

    BEGIN

      OPEN p_unsub_recip_cur FOR

      /*
       * For the 'solicitor_firm_address' values, a comma is only required after a
       * non-NULL value and also not at the end of the concatenation.  Due to the fact
       * that the last non-NULL value may not be the last field selected (a.country),
       * the last comma in that instace needs to be removed.  The RTRIM removes any
       * trailing unwanted commas from the full concatenation. ie. Postcode, becomes
       * Postcode.
       */

        SELECT rsf.crest_sof_id,
               rsf.solicitor_firm_name,
               RTRIM(NVL2(a.address_1,a.address_1||',',NULL)
                     ||NVL2(a.address_2,a.address_2||',',NULL)
                     ||NVL2(a.address_3,a.address_3||',',NULL)
                     ||NVL2(a.address_4,a.address_4||',',NULL)
                     ||NVL2(a.town,a.town||',',NULL)
                     ||NVL2(a.county,a.county||',',NULL)
                     ||NVL2(a.postcode,a.postcode||',',NULL)
                     ||NVL(a.country,NULL),',') solictior_firm_address,
               NULL solicitor_firm_fax,
               NULL solicitor_firm_email,
               rsf.court_id, 
               NULL wll_recipient_id
        FROM   xhb_ref_solicitor_firm rsf,
               xhb_address a
        WHERE  rsf.court_id = p_court_id
        AND    rsf.crest_sof_id NOT IN (SELECT NVL(crest_solicitor_firm_id, -1)
                                         FROM   xhb_wll_recipient)
        AND    rsf.address_id = a.address_id(+)
		AND	   rsf.obs_ind = 'N'
        UNION    
        SELECT wr.crest_solicitor_firm_id crest_sof_id,
               wr.solicitor_firm_name,
               wr.solictior_firm_address,
               wr.solicitor_firm_fax,
               wr.solicitor_firm_email,
               wr.court_id, 
               wr.wll_recipient_id
        FROM   xhb_wll_recipient wr
        WHERE  wr.court_id = p_court_id
        AND    NOT EXISTS (SELECT 1
                           FROM   xhb_document_distribution 
                           WHERE  wr.wll_recipient_id = xhb_document_distribution.wll_recipient_id)
        ORDER BY solicitor_firm_name;

  END get_wll_unsub_rec_by_court_id;

  PROCEDURE  get_dist_stat_by_court_id (p_dist_stat_cur IN OUT SYS_REFCURSOR,
                                        p_court_id      IN     NUMBER) IS

    BEGIN

      OPEN p_dist_stat_cur FOR

      SELECT dc.DOC_CONTROL_ID,
             dc.STATUS,
             dc.EXPIRY_DATE,
             dc.DISTRIBUTION_TYPE,
             dc.MIME_TYPE,
             dc.DOCUMENT_TYPE,
             dc.LAST_UPDATE_DATE,
             dc.CREATION_DATE,
             dc.CREATED_BY,
             dc.LAST_UPDATED_BY,
             dc.VERSION,
             dc.FORMATTING_ID,
             dc.COURT_ID,
             dc.DISTRIBUTED_DATE,
             dc.XML_DOCUMENT_ID,
             dr.DOCUMENT_RECIPIENT_ID,
             dr.DOC_RECIPIENT_NAME,
             dr.DOC_RECIPIENT_FAX,
             dr.DOC_RECIPIENT_EMAIL
      FROM   XHB_DOCUMENT_CONTROL dc,
             XHB_DOCUMENT_RECIPIENT dr
      WHERE  dc.STATUS != 'XX'
      AND    dc.STATUS != 'XA'
      AND   (dc.DOCUMENT_TYPE != 'IWP' OR  (dc.DOCUMENT_TYPE = 'IWP'
                                       AND (dc.STATUS != 'SE' AND dc.STATUS != 'SF')))
      AND    dc.COURT_ID = p_court_id
      AND    dc.DOC_CONTROL_ID = dr.DOC_CONTROL_ID
      ORDER BY dc.CREATION_DATE DESC;

  END get_dist_stat_by_court_id;

  PROCEDURE  get_wll_dist_stat_by_court_id (p_wll_dist_stat_cur IN OUT SYS_REFCURSOR,
                                            p_court_id          IN     NUMBER) IS

    BEGIN

      OPEN p_wll_dist_stat_cur FOR

      SELECT DISTINCT xd1.XML_DOCUMENT_ID,
             xd1.DATE_CREATED,
             xd1.DOCUMENT_TITLE,
             xd2.STATUS,
             xd1.EXPIRY_DATE,
             xd1.DOCUMENT_TYPE,
             xd1.LAST_UPDATE_DATE,
             xd1.CREATION_DATE,
             xd1.CREATED_BY,
             xd1.LAST_UPDATED_BY,
             xd1.VERSION,
             xd1.COURT_ID
      FROM   XHB_XML_DOCUMENT xd1,
             XHB_WLL_CONTROL wc,
             XHB_WLL_DOCUMENT wd,
             XHB_XML_DOCUMENT xd2
      WHERE  xd1.XML_DOCUMENT_ID = wc.XML_DOCUMENT_ID
      AND    wc.WLL_CONTROL_ID = wd.WLL_CONTROL_ID
      AND    wd.XML_DOCUMENT_ID = xd2.XML_DOCUMENT_ID
      AND    xd1.DOCUMENT_TYPE = 'WL'
      AND    (xd2.STATUS != 'XX' AND xd2.STATUS != 'XA')
      AND    xd1.COURT_ID = p_court_id;

  END get_wll_dist_stat_by_court_id;

END xhb_list_distribution_pkg;
/
show errors


/*
 * Changes, additions or deletion of standing data
 */

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 30200)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Total_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM XHB_COURT_LOG_EVENT_DESC
                        WHERE EVENT_TYPE = 30300)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Total_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 30400)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Total_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 30200)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Scheduled_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 30300)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Scheduled_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 30400)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Scheduled_Hearing_Time_Stop');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 40713)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Directions_By_Case');

DELETE FROM XHB_COURT_LOG_CATEGORY
WHERE  EVENT_DESC_ID = (SELECT EVENT_DESC_ID
                        FROM   XHB_COURT_LOG_EVENT_DESC
                        WHERE  EVENT_TYPE = 40714)
AND    CATEGORY_DESC_ID = (SELECT CATEGORY_DESC_ID
                           FROM   XHB_COURT_LOG_CATEGORY_DESC
                           WHERE  CATEGORY_DESCRIPTION = 'Directions_By_Case');

-- End Hearing - Case level
INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 131, 30500, 'Hearing Ended', 1, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 1 , 'Hearing_Ended_By_Case');

-- End Hearing - Defendant level
INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 132, 30600, 'Hearing Ended', 1, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 1 , 'Hearing_Ended_By_Defendant');

-- End Hearing
INSERT INTO XHB_COURT_LOG_CATEGORY_DESC ( CATEGORY_TYPE, CATEGORY_DESCRIPTION, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( 13, 'End_Hearing', 'Xhibit', 'Xhibit' );

-- End Hearing - Case level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30500),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Total_Hearing_Time_Stop'),
         'Xhibit', 'Xhibit' );         

-- End Hearing - Defendant level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30600),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Total_Hearing_Time_Stop'),
         'Xhibit', 'Xhibit' );         

-- End Hearing - Case level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30500),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Scheduled_Hearing_Time_Stop'),
         'Xhibit', 'Xhibit' );          

-- End Hearing - Defendant level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30600),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Scheduled_Hearing_Time_Stop'),
         'Xhibit', 'Xhibit' );          

-- Insert Categories for End Hearings

-- End Hearing - Case level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30500),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'End_Hearing'),
         'Xhibit', 'Xhibit' );          

-- End Hearing - Defendant level
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 30600),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'End_Hearing'),
         'Xhibit', 'Xhibit' );

COMMIT;

INSERT INTO XHB_SYS_AUDIT VALUES (NULL, 'XHB_HEARING_LEG_REP', 'AUD_HEARING_LEG_REP', 'Y');

COMMIT;

/*
 * FILE_NAME:          XHIBIT2_Standing_PublicDisplay_Court.sql
 *
 * ENVIRONMENT:        
 *
 * DESCRIPTION:        
 *
 * STATUS DESCRIPTION: 
 *
 * DEPENDENCIES:       
 *
 * OWNER:              Rakesh Lakhani
 */ 

DELETE FROM XHB_DISPLAY_COURT_ROOM;

DELETE FROM XHB_DISPLAY;

DELETE FROM XHB_DISPLAY_LOCATION;

DELETE FROM XHB_ROTATION_SET_DD
WHERE  ROTATION_SET_ID IN (SELECT rotation_set_id
                           FROM   XHB_ROTATION_SETS
                           WHERE  DEFAULT_YN = 'Y');

DELETE FROM XHB_ROTATION_SETS WHERE DEFAULT_YN='Y';

COMMIT;

--_________________________
--Set up the Rotation Sets.
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  1, C.COURT_ID, 'Public View', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  2, C.COURT_ID, 'Court Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  3, C.COURT_ID, 'Jury Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  4, C.COURT_ID, 'Summary By Name', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  5, C.COURT_ID, 'Status', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  6, C.COURT_ID, 'Daily List', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  7, C.COURT_ID, 'All Lists', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '453';

INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  8, C.COURT_ID, 'Public View', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  9, C.COURT_ID, 'Court Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  10, C.COURT_ID, 'Jury Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  11, C.COURT_ID, 'Summary By Name', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  12, C.COURT_ID, 'Status', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  13, C.COURT_ID, 'Daily List', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  14, C.COURT_ID, 'All Lists', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '499';

INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  15, C.COURT_ID, 'Public View', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  16, C.COURT_ID, 'Court Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  17, C.COURT_ID, 'Jury Room', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  18, C.COURT_ID, 'Summary By Name', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  19, C.COURT_ID, 'Status', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  20, C.COURT_ID, 'Daily List', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';
INSERT INTO XHB_ROTATION_SETS ( ROTATION_SET_ID, COURT_ID, DESCRIPTION, DEFAULT_YN ) 
SELECT  21, C.COURT_ID, 'All Lists', 'Y'
FROM    XHB_COURT C
WHERE   C.CREST_COURT_ID = '475';

COMMIT;

INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (1, 1, 3, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (2, 1, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (3, 2, 1, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (4, 2, 2, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (5, 3, 6, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (6, 4, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (7, 5, 4, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (8, 6, 3, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (9, 7, 1, 10, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (10, 7, 2, 10, 3); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (11, 7, 3, 10, 4); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (12, 7, 4, 10, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (13, 7, 5, 10, 6); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (14, 7, 6, 10, 5);

INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (15, 8, 3, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (16, 8, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (17, 9, 1, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (18, 9, 2, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (19, 10, 6, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (20, 11, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (21, 12, 4, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (22, 13, 3, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (23, 14, 1, 10, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (24, 14, 2, 10, 3); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (25, 14, 3, 10, 4); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (26, 14, 4, 10, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (27, 14, 5, 10, 6); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (28, 14, 6, 10, 5);

INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (29, 15, 3, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (30, 15, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (31, 16, 1, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (32, 16, 2, 20, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (33, 17, 6, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (34, 18, 5, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (35, 19, 4, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (36, 20, 3, 20, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (37, 21, 1, 10, 2); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (38, 21, 2, 10, 3); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (39, 21, 3, 10, 4); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (40, 21, 4, 10, 1); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (41, 21, 5, 10, 6); 
INSERT INTO XHB_ROTATION_SET_DD ( ROTATION_SET_DD_ID, ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING ) 
VALUES (42, 21, 6, 10, 5); 
COMMIT;

--_____________________________
--Set up the Display Locations.
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  1,
        'court_room_1',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  2,
        'court_room_2',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  3,
        'court_room_3',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  4,
        'court_room_4',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  5,
        'court_room_5',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  6,
        'court_room_6',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  7,
        'court_room_7',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  8,
        'court_room_8',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  9,
        'court_room_9',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  10,
        'court_room_10',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  11,
        'court_room_11',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  12,
        'court_room_12',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  13,
        'court_room_13',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  14,
        'court_room_14',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  15,
        'court_room_15',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  16,
        'court_room_16',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  17,
        'court_room_17',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  18,
        'court_room_18',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  19,
        'court_room_19',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  20,
        'court_room_20',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  21,
        'e_v',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  22,
        'reception',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  23,
        'public_restaurant',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  24,
        'jury_lounge',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  25,
        'witness_service',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  26,
        'police_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  27,
        'ps_s_o_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  28,
        'n_w_c_w_a',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  29,
        'b_a_s_r',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  30,
        'j_l_a_b',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  31,
        'v_i_p',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '453' --Snaresbrook
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  32,
        'court_room_1',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  33,
        'court_room_2',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  34,
        'court_room_3',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  35,
        'court_room_4',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  36,
        'court_room_5',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  37,
        'court_room_6',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  38,
        'court_room_7',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  39,
        'court_room_8',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  40,
        'court_room_9',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  41,
        'court_room_10',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  42,
        'e_v',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  43,
        'reception',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  44,
        'public_restaurant',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  45,
        'jury_lounge',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  46,
        'witness_service',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  47,
        'police_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  48,
        'ps_s_o_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  49,
        'n_w_c_w_a',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  50,
        'b_a_s_r',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  51,
        'j_l_a_b',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  52,
        'v_i_p',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '499' --Dockford
AND     CS.COURT_SITE_CODE = 'A';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  53,
        'court_room_1',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  54,
        'court_room_2',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  55,
        'court_room_3',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  56,
        'court_room_4',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  57,
        'court_room_5',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  58,
        'court_room_6',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  59,
        'court_room_7',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  60,
        'court_room_8',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  61,
        'court_room_9',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  62,
        'court_room_10',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  63,
        'court_room_11',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  64,
        'court_room_12',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  65,
        'court_room_13',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  66,
        'court_room_14',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  67,
        'court_room_15',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  68,
        'court_room_16',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  69,
        'court_room_17',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  70,
        'court_room_18',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  71,
        'court_room_19',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
    DISPLAY_LOCATION_ID,
    DESCRIPTION_CODE,
    COURT_SITE_ID
)
SELECT  72,
        'court_room_20',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  73,
        'e_v',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  74,
        'reception',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  75,
        'public_restaurant',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  76,
        'jury_lounge',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  77,
        'witness_service',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  78,
        'police_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  79,
        'ps_s_o_1st_floor',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  80,
        'n_w_c_w_a',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  81,
        'b_a_s_r',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  82,
        'j_l_a_b',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

INSERT INTO XHB_DISPLAY_LOCATION
(
	DISPLAY_LOCATION_ID,
	DESCRIPTION_CODE,
	COURT_SITE_ID
)
SELECT  83,
        'v_i_p',
        CS.COURT_SITE_ID
FROM    XHB_COURT_SITE CS,
        XHB_COURT C
WHERE   C.COURT_ID = CS.COURT_ID
AND     C.CREST_COURT_ID = '475' --I
AND     CS.COURT_SITE_CODE = 'I';

--______________________
--Set up the displays...
--¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
1, 1, 1, 2, 'courtroom_1_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
2, 1, 2, 2, 'courtroom_2_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
3, 1, 3, 2, 'courtroom_3_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
4, 1, 4, 2, 'courtroom_4_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
5, 1, 5, 2, 'courtroom_5_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
6, 1, 6, 2, 'courtroom_6_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
7, 1, 7, 2, 'courtroom_7_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
8, 1, 8, 2, 'courtroom_8_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
9, 1, 9, 2, 'courtroom_9_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
10, 1, 10, 2, 'courtroom_10_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
11, 1, 11, 2, 'courtroom_11_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
12, 1, 12, 2, 'courtroom_12_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
13, 1, 13, 2, 'courtroom_13_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
14, 1, 14, 2, 'courtroom_14_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
15, 1, 15, 2, 'courtroom_15_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
16, 1, 16, 2, 'courtroom_16_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
17, 1, 17, 2, 'courtroom_17_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
18, 1, 18, 2, 'courtroom_18_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
19, 1, 19, 2, 'courtroom_19_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
20, 1, 20, 2, 'courtroom_20_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
21, 2, 21, 4, 'e_v_plasma_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
22, 2, 22, 6, 'reception_42_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
23, 1, 23, 1, 'public_rest_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
24, 1, 23, 1, 'public_rest_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
25, 1, 24, 3, 'jury_lounge_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
26, 1, 24, 3, 'jury_lounge_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
27, 1, 25, 1, 'witness_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
28, 1, 26, 1, 'police_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
29, 1, 27, 1, 'ps_sec_off_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
30, 2, 28, 5, 'nwcwa_plasma_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
31, 1, 29, 1, 'b_a_s_r_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
32, 2, 29, 1, 'b_a_s_r_42in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
33, 1, 30, 3, 'jl_ab_18in_display_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
34, 1, 30, 3, 'jl_ab_18in_display_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
35, 3, 31, 7, 'v_i_p', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
41, 1, 32, 9, 'courtroom_1_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
42, 1, 33, 9, 'courtroom_2_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
43, 1, 34, 9, 'courtroom_3_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
44, 1, 35, 9, 'courtroom_4_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
45, 1, 36, 9, 'courtroom_5_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
46, 1, 37, 9, 'courtroom_6_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
47, 1, 38, 9, 'courtroom_7_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
48, 1, 39, 9, 'courtroom_8_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
49, 1, 40, 9, 'courtroom_9_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
50, 1, 41, 9, 'courtroom_10_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
51, 2, 42, 11, 'e_v_plasma_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
52, 2, 43, 13, 'reception_42_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
53, 1, 44, 8, 'public_rest_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
54, 1, 44, 8, 'public_rest_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
55, 1, 45, 10, 'jury_lounge_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
56, 1, 45, 10, 'jury_lounge_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
57, 1, 46, 8, 'witness_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
58, 1, 47, 8, 'police_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
59, 1, 48, 8, 'ps_sec_off_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
60, 2, 49, 12, 'nwcwa_plasma_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
61, 1, 50, 8, 'b_a_s_r_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
62, 2, 50, 8, 'b_a_s_r_42in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
63, 1, 51, 10, 'jl_ab_18in_display_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
64, 1, 51, 10, 'jl_ab_18in_display_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
65, 3, 52, 14, 'v_i_p', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
71, 1, 53, 16, 'courtroom_1_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
72, 1, 54, 16, 'courtroom_2_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
73, 1, 55, 16, 'courtroom_3_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
74, 1, 56, 16, 'courtroom_4_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
75, 1, 57, 16, 'courtroom_5_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
76, 1, 58, 16, 'courtroom_6_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
77, 1, 59, 16, 'courtroom_7_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
78, 1, 60, 16, 'courtroom_8_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
79, 1, 61, 16, 'courtroom_9_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
80, 1, 62, 16, 'courtroom_10_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
81, 1, 63, 16, 'courtroom_11_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
82, 1, 64, 16, 'courtroom_12_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
83, 1, 65, 16, 'courtroom_13_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
84, 1, 66, 16, 'courtroom_14_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
85, 1, 67, 16, 'courtroom_15_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
86, 1, 68, 16, 'courtroom_16_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
87, 1, 69, 16, 'courtroom_17_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
88, 1, 70, 16, 'courtroom_18_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
89, 1, 71, 16, 'courtroom_19_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
90, 1, 72, 16, 'courtroom_20_display', 'enGB', 'N'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
91, 2, 73, 18, 'e_v_plasma_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
92, 2, 74, 20, 'reception_42_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
93, 1, 75, 15, 'public_rest_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
94, 1, 75, 15, 'public_rest_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
95, 1, 76, 17, 'jury_lounge_18in_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
96, 1, 76, 17, 'jury_lounge_18in_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
97, 1, 77, 15, 'witness_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
98, 1, 78, 15, 'police_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
99, 1, 79, 15, 'ps_sec_off_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
100, 2, 80, 19, 'nwcwa_plasma_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
101, 1, 81, 15, 'b_a_s_r_18in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
102, 2, 81, 15, 'b_a_s_r_42in_display', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
103, 1, 82, 17, 'jl_ab_18in_display_1', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
104, 1, 82, 17, 'jl_ab_18in_display_2', 'enGB', 'Y'); 
INSERT INTO XHB_DISPLAY ( DISPLAY_ID, DISPLAY_TYPE_ID, DISPLAY_LOCATION_ID, ROTATION_SET_ID,
DESCRIPTION_CODE, LOCALE, SHOW_UNASSIGNED_YN ) VALUES ( 
105, 3, 83, 21, 'v_i_p', 'enGB', 'Y'); 
COMMIT;

INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (1, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (2, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (3, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (4, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (5, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (6, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (7, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (8, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (9, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (10, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (11, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (12, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (13, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (14, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (15, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (16, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (17, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (18, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (19, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (20, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (21, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (22, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (23, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (24, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (25, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (26, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (27, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (28, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (29, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (30, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (31, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (32, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (33, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (34, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 1); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 2); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 3); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 4); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 5); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 6); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 7); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 8); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 9); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 10); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 11); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 12); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 13); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 14); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 15); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 16); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 17); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 18); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 19); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (35, 20); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (41, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (42, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (43, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (44, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (45, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (46, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (47, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (48, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (49, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (50, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (51, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (52, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (53, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (54, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (55, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (55, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (55, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (55, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (55, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (56, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (56, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (56, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (56, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (56, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (57, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (58, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (59, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (60, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (61, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (62, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (63, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (63, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (63, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (63, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (63, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (64, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (64, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (64, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (64, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (64, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 21); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 22); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 23); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 24); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 25); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 26); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 27); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 28); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 29); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (65, 30); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (71, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (72, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (73, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (74, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (75, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (76, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (77, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (78, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (79, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (80, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (81, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (82, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (83, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (84, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (85, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (86, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (87, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (88, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (89, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (90, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (91, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (92, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (93, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (94, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (95, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (96, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (97, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (98, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (99, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (100, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (101, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (102, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (103, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (104, 50); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 31); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 32); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 33); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 34); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 35); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 36); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 37); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 38); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 39); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 40); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 41); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 42); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 43); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 44); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 45); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 46); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 47); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 48); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 49); 
INSERT INTO XHB_DISPLAY_COURT_ROOM ( DISPLAY_ID, COURT_ROOM_ID ) VALUES (105, 50); 
COMMIT;

DROP SEQUENCE XHB_DISPLAY_LOCATION_SEQ;
CREATE SEQUENCE XHB_DISPLAY_LOCATION_SEQ START WITH 84
MAXVALUE 999999999999999999
MINVALUE -999999999999999999
NOCACHE  
NOCYCLE
NOORDER
;

DROP SEQUENCE XHB_DISPLAY_SEQ;
CREATE SEQUENCE XHB_DISPLAY_SEQ START WITH 106
MAXVALUE 999999999999999999
MINVALUE -999999999999999999
NOCACHE  
NOCYCLE
NOORDER
;

DROP SEQUENCE XHB_ROTATION_SET_DD_SEQ;
CREATE SEQUENCE XHB_ROTATION_SET_DD_SEQ START WITH 43
MAXVALUE 999999999999999999
MINVALUE -999999999999999999
NOCACHE  
NOCYCLE
NOORDER
;

DROP SEQUENCE XHB_ROTATION_SETS_SEQ;
CREATE SEQUENCE XHB_ROTATION_SETS_SEQ START WITH 22
MAXVALUE 999999999999999999
MINVALUE -999999999999999999
NOCACHE  
NOCYCLE
NOORDER
;


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = '5.12', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
