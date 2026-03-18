/*
 * Patch for DB release 29
 *
 * Date Here
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

ALTER TABLE XHB_CASE ADD (CLASS_CODE NUMBER(1) NULL,
                          OFFENCE_GROUP_UPDATE VARCHAR2(1) NULL);

ALTER TABLE xhb_cr_live_status MODIFY (scheduled_hearing_id NULL);

DROP INDEX xhb_ref_off_offence_code_idx;

CREATE UNIQUE INDEX xhb_ref_off_ocode_ctid_idx ON XHB_REF_OFFENCE
  (OFFENCE_CODE, COURT_ID)
  TABLESPACE XHIBITREFX
  STORAGE (INITIAL 1M
           NEXT 1M
           PCTINCREASE 0);


/*
 * Changes, additions or deletion of views
 */

CREATE OR REPLACE VIEW XHB_COUNSEL_FACILITIES_SH_V AS
SELECT DISTINCT
		sitting.court_room_id                         	COURT_ROOM_ID,
		sitting.is_floating                           	IS_FLOATING,
		sitting.sitting_sequence_no						SITTING_SEQUENCE_NO,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME,
		court_room.court_room_name                    	COURT_ROOM_NAME,
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		court_site.court_site_code						COURT_SITE_CODE,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME,
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID,
		scheduled_hearing.sequence_no					SH_SEQUENCE_NO,
		NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time) TIME_LISTED, 
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
		sh_leg_rep.sol_firm_or_ref_legal_rep				SOL_FIRM_OR_REF_LEGAL_REP,
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
             FROM
		XHB_HEARING_LIST             HEARING_LIST,
		XHB_SITTING                  SITTING,
		XHB_COURT_ROOM               COURT_ROOM,
		XHB_COURT_SITE				 COURT_SITE,
		XHB_SCHEDULED_HEARING        SCHEDULED_HEARING,
		XHB_HEARING                  HEARING,
		XHB_REF_HEARING_TYPE         REF_HEARING_TYPE,
		XHB_SCHED_HEARING_ATTENDEE   SH_ATTENDEE,
		XHB_SH_STAFF                 SH_STAFF,
		XHB_SCHED_HEARING_DEFENDANT  SCHED_HEARING_DEFENDANT,
		XHB_DEFENDANT_ON_CASE        DEFENDANT_ON_CASE,
		XHB_DEFENDANT                DEFENDANT,
		XHB_CASE                     XCASE,
		XHB_SH_LEG_REP               SH_LEG_REP,
		XHB_REF_LEGAL_REPRESENTATIVE LEGAL_REP,
		XHB_REF_ADVOCATE             ADVOCATE,
		XHB_REF_CHAMBER              CHAMBER,
		XHB_REF_SOLICITOR            SOLICITOR,
		XHB_REF_SOLICITOR_FIRM       SOLFIRM,
		XHB_ADDRESS                  ADV_ADDRESS,
		XHB_ADDRESS                  SOL_ADDRESS
             WHERE
		( hearing_list.list_id = sitting.list_id ) AND
		( sitting.court_room_id = court_room.court_room_id ) AND
		( court_room.court_site_id = court_site.court_site_id ) AND
		( scheduled_hearing.sitting_id = sitting.sitting_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) AND
		( scheduled_hearing.hearing_id = hearing.hearing_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) AND
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) AND
		( hearing.case_id = xcase.case_id ) AND
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) AND
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) AND
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_leg_rep.scheduled_hearing_id(+) AND sh_leg_rep.sched_hear_def_id IS NULL) AND
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) AND
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) AND
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) AND
		( chamber.address_id = adv_address.address_id(+) ) AND
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) AND
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) AND
		( solfirm.address_id = sol_address.address_id(+) );

CREATE OR REPLACE VIEW XHB_COUNSEL_FACILITIES_SHDID_V AS
SELECT DISTINCT
		sitting.court_room_id                         	COURT_ROOM_ID,
		sitting.is_floating                           	IS_FLOATING,
		sitting.sitting_sequence_no						SITTING_SEQUENCE_NO,
		hearing_list.start_date                       	START_DATE,
		hearing_list.court_id                       	COURT_ID,
		court_room.description                       	COURT_ROOM_DESCRIPTION,
		court_room.display_name                       	COURT_ROOM_DISPLAY_NAME,
		court_room.court_room_name                    	COURT_ROOM_NAME,
		court_room.crest_court_room_no                	CREST_COURT_ROOM_NO,
		court_site.court_site_code						COURT_SITE_CODE,
		ref_hearing_type.hearing_type_code            	HEARING_TYPE_CODE,
		ref_hearing_type.hearing_type_desc            	HEARING_TYPE_DESC,
		sh_staff.staff_name                           	STAFF_NAME,
		sh_staff.staff_role                           	STAFF_ROLE,
		scheduled_hearing.scheduled_hearing_id        	SCHEDULED_HEARING_ID,
		scheduled_hearing.sequence_no					SH_SEQUENCE_NO,
		NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time) TIME_LISTED, 
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
		sh_leg_rep.sol_firm_or_ref_legal_rep				SOL_FIRM_OR_REF_LEGAL_REP,
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
             FROM
		XHB_HEARING_LIST             HEARING_LIST,
		XHB_SITTING                  SITTING,
		XHB_COURT_ROOM               COURT_ROOM,
		XHB_COURT_SITE				 COURT_SITE,
		XHB_SCHEDULED_HEARING        SCHEDULED_HEARING,
		XHB_HEARING                  HEARING,
		XHB_REF_HEARING_TYPE         REF_HEARING_TYPE,
		XHB_SCHED_HEARING_ATTENDEE   SH_ATTENDEE,
		XHB_SH_STAFF                 SH_STAFF,
		XHB_SCHED_HEARING_DEFENDANT  SCHED_HEARING_DEFENDANT,
		XHB_DEFENDANT_ON_CASE        DEFENDANT_ON_CASE,
		XHB_DEFENDANT                DEFENDANT,
		XHB_CASE                     XCASE,
		XHB_SH_LEG_REP               SH_LEG_REP,
		XHB_REF_LEGAL_REPRESENTATIVE LEGAL_REP,
		XHB_REF_ADVOCATE             ADVOCATE,
		XHB_REF_CHAMBER              CHAMBER,
		XHB_REF_SOLICITOR            SOLICITOR,
		XHB_REF_SOLICITOR_FIRM       SOLFIRM,
		XHB_ADDRESS                  ADV_ADDRESS,
		XHB_ADDRESS                  SOL_ADDRESS
             WHERE
		( hearing_list.list_id = sitting.list_id ) AND
		( sitting.court_room_id = court_room.court_room_id ) AND
		( court_room.court_site_id = court_site.court_site_id ) AND
		( scheduled_hearing.sitting_id = sitting.sitting_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sched_hearing_defendant.scheduled_hearing_id(+) ) AND
		( scheduled_hearing.hearing_id = hearing.hearing_id ) AND
		( scheduled_hearing.scheduled_hearing_id = sh_attendee.scheduled_hearing_id(+) ) AND
		( sh_attendee.sh_staff_id = sh_staff.sh_staff_id(+) ) AND
		( hearing.case_id = xcase.case_id ) AND
		( hearing.ref_hearing_type_id = ref_hearing_type.ref_hearing_type_id ) AND
		( sched_hearing_defendant.defendant_on_case_id = defendant_on_case.defendant_on_case_id(+) ) AND
		( defendant_on_case.defendant_id = defendant.defendant_id(+) ) AND
		( sched_hearing_defendant.sched_hear_def_id = sh_leg_rep.sched_hear_def_id(+)) AND
		( sh_leg_rep.ref_legal_rep_id = legal_rep.ref_legal_rep_id(+) ) AND
		( legal_rep.ref_legal_rep_id = advocate.ref_legal_rep_id(+) ) AND
		( advocate.ref_chamber_id = chamber.ref_chamber_id(+) ) AND
		( chamber.address_id = adv_address.address_id(+) ) AND
		( legal_rep.ref_legal_rep_id = solicitor.ref_legal_rep_id(+) ) AND
		( solicitor.ref_solicitor_firm_id = solfirm.ref_solicitor_firm_id(+) ) AND
		( solfirm.address_id = sol_address.address_id(+) );


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

DROP TABLE AUD_CASE;

CREATE TABLE AUD_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_CASE;
TRUNCATE TABLE AUD_CASE;
ALTER TABLE AUD_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

ALTER TABLE aud_cr_live_status MODIFY (scheduled_hearing_id NULL);


/*
 * Changes, additions or deletion of sequences
 */

CREATE SEQUENCE MSEQ_MERC_REFRESH_STORAGE_SEQ 
NOMAXVALUE 
NOMINVALUE 
NOCACHE  
NOCYCLE
NOORDER;


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table.  However, always a good idea to recompile the BIR trigger.
 */

ALTER TRIGGER XHB_CASE_BIR_TR COMPILE;

CREATE OR REPLACE TRIGGER XHB_CASE_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CASE
  FOR EACH ROW

/* ERwin Builtin Tue May 06 14:13:58 2003 */
/* default body for XHB_CASE_BUR_TR */

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

    IF :NEW.CASE_ID = :OLD.CASE_ID AND
       :NEW.COURT_ID = :OLD.COURT_ID AND
      (:NEW.CASE_NUMBER = :OLD.CASE_NUMBER OR
       (:NEW.CASE_NUMBER IS NULL AND :OLD.CASE_NUMBER IS NULL)) AND
      (:NEW.CASE_TYPE = :OLD.CASE_TYPE OR
       (:NEW.CASE_TYPE IS NULL AND :OLD.CASE_TYPE IS NULL)) AND
      (:NEW.MAG_CONVICTION_DATE = :OLD.MAG_CONVICTION_DATE OR
       (:NEW.MAG_CONVICTION_DATE IS NULL AND :OLD.MAG_CONVICTION_DATE IS NULL)) AND
      (:NEW.CASE_SUB_TYPE = :OLD.CASE_SUB_TYPE OR
       (:NEW.CASE_SUB_TYPE IS NULL AND :OLD.CASE_SUB_TYPE IS NULL)) AND
      (:NEW.CASE_TITLE = :OLD.CASE_TITLE OR
       (:NEW.CASE_TITLE IS NULL AND :OLD.CASE_TITLE IS NULL)) AND
      (:NEW.CASE_DESCRIPTION = :OLD.CASE_DESCRIPTION OR
       (:NEW.CASE_DESCRIPTION IS NULL AND :OLD.CASE_DESCRIPTION IS NULL)) AND
      (:NEW.LINKED_CASE_ID = :OLD.LINKED_CASE_ID OR
       (:NEW.LINKED_CASE_ID IS NULL AND :OLD.LINKED_CASE_ID IS NULL)) AND
      (:NEW.BAIL_MAG_CODE = :OLD.BAIL_MAG_CODE OR
       (:NEW.BAIL_MAG_CODE IS NULL AND :OLD.BAIL_MAG_CODE IS NULL)) AND
      (:NEW.REF_COURT_ID = :OLD.REF_COURT_ID OR
       (:NEW.REF_COURT_ID IS NULL AND :OLD.REF_COURT_ID IS NULL)) AND
      (:NEW.SEVERED_IND = :OLD.SEVERED_IND OR
       (:NEW.SEVERED_IND IS NULL AND :OLD.SEVERED_IND IS NULL)) AND
      (:NEW.INDICT_RESP = :OLD.INDICT_RESP OR
       (:NEW.INDICT_RESP IS NULL AND :OLD.INDICT_RESP IS NULL)) AND
      (:NEW.DATE_IND_REC = :OLD.DATE_IND_REC OR
       (:NEW.DATE_IND_REC IS NULL AND :OLD.DATE_IND_REC IS NULL)) AND
      (:NEW.PROS_AGENCY_REFERENCE = :OLD.PROS_AGENCY_REFERENCE OR
       (:NEW.PROS_AGENCY_REFERENCE IS NULL AND :OLD.PROS_AGENCY_REFERENCE IS NULL)) AND
      (:NEW.CASE_CLASS = :OLD.CASE_CLASS OR
       (:NEW.CASE_CLASS IS NULL AND :OLD.CASE_CLASS IS NULL)) AND
      (:NEW.JUDGE_REASON_FOR_APPEAL = :OLD.JUDGE_REASON_FOR_APPEAL OR
       (:NEW.JUDGE_REASON_FOR_APPEAL IS NULL AND :OLD.JUDGE_REASON_FOR_APPEAL IS NULL)) AND
      (:NEW.RESULTS_VERIFIED = :OLD.RESULTS_VERIFIED OR
       (:NEW.RESULTS_VERIFIED IS NULL AND :OLD.RESULTS_VERIFIED IS NULL)) AND
      (:NEW.LENGTH_TAPE = :OLD.LENGTH_TAPE OR
       (:NEW.LENGTH_TAPE IS NULL AND :OLD.LENGTH_TAPE IS NULL)) AND
      (:NEW.NO_PAGE_PROS_EVIDENCE = :OLD.NO_PAGE_PROS_EVIDENCE OR
       (:NEW.NO_PAGE_PROS_EVIDENCE IS NULL AND :OLD.NO_PAGE_PROS_EVIDENCE IS NULL)) AND
      (:NEW.NO_PROS_WITNESS = :OLD.NO_PROS_WITNESS OR
       (:NEW.NO_PROS_WITNESS IS NULL AND :OLD.NO_PROS_WITNESS IS NULL)) AND
      (:NEW.EST_PDH_TRIAL_LENGTH = :OLD.EST_PDH_TRIAL_LENGTH OR
       (:NEW.EST_PDH_TRIAL_LENGTH IS NULL AND :OLD.EST_PDH_TRIAL_LENGTH IS NULL)) AND
      (:NEW.INDICTMENT_INFO_1 = :OLD.INDICTMENT_INFO_1 OR
       (:NEW.INDICTMENT_INFO_1 IS NULL AND :OLD.INDICTMENT_INFO_1 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_2 = :OLD.INDICTMENT_INFO_2 OR
       (:NEW.INDICTMENT_INFO_2 IS NULL AND :OLD.INDICTMENT_INFO_2 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_3 = :OLD.INDICTMENT_INFO_3 OR
       (:NEW.INDICTMENT_INFO_3 IS NULL AND :OLD.INDICTMENT_INFO_3 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_4 = :OLD.INDICTMENT_INFO_4 OR
       (:NEW.INDICTMENT_INFO_4 IS NULL AND :OLD.INDICTMENT_INFO_4 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_5 = :OLD.INDICTMENT_INFO_5 OR
       (:NEW.INDICTMENT_INFO_5 IS NULL AND :OLD.INDICTMENT_INFO_5 IS NULL)) AND
      (:NEW.INDICTMENT_INFO_6 = :OLD.INDICTMENT_INFO_6 OR
       (:NEW.INDICTMENT_INFO_6 IS NULL AND :OLD.INDICTMENT_INFO_6 IS NULL)) AND
      (:NEW.POLICE_OFFICER_ATTENDING = :OLD.POLICE_OFFICER_ATTENDING OR
       (:NEW.POLICE_OFFICER_ATTENDING IS NULL AND :OLD.POLICE_OFFICER_ATTENDING IS NULL)) AND
      (:NEW.CPS_CASE_WORKER = :OLD.CPS_CASE_WORKER OR
       (:NEW.CPS_CASE_WORKER IS NULL AND :OLD.CPS_CASE_WORKER IS NULL)) AND
      (:NEW.EXPORT_CHARGES = :OLD.EXPORT_CHARGES OR
       (:NEW.EXPORT_CHARGES IS NULL AND :OLD.EXPORT_CHARGES IS NULL)) AND
      (:NEW.IND_CHANGE_STATUS = :OLD.IND_CHANGE_STATUS OR
       (:NEW.IND_CHANGE_STATUS IS NULL AND :OLD.IND_CHANGE_STATUS IS NULL)) AND
      (:NEW.MAGISTRATES_CASE_REF = :OLD.MAGISTRATES_CASE_REF OR
       (:NEW.MAGISTRATES_CASE_REF IS NULL AND :OLD.MAGISTRATES_CASE_REF IS NULL)) AND
      (:NEW.CLASS_CODE = :OLD.CLASS_CODE OR
       (:NEW.CLASS_CODE IS NULL AND :OLD.CLASS_CODE IS NULL)) AND
      (:NEW.OFFENCE_GROUP_UPDATE = :OLD.OFFENCE_GROUP_UPDATE OR
       (:NEW.OFFENCE_GROUP_UPDATE IS NULL AND :OLD.OFFENCE_GROUP_UPDATE IS NULL)) THEN

      -- Only the CHARGE_IMPORT_INDICATOR has changed so do not increase VERSION
      -- This will even come into this section if the CHARGE_IMPORT_INDICATOR column is
      -- updated to the same value with all others staying the same

      SELECT SYSDATE
      INTO   :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    ELSE

      -- Other fields have changes so increase VERSION

      SELECT :OLD.VERSION + 1,
             SYSDATE
      INTO   :NEW.VERSION,
             :NEW.LAST_UPDATE_DATE
      FROM   DUAL;

    END IF;

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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CASE') = 1) THEN

    INSERT INTO AUD_CASE 
    VALUES (:old.CASE_ID, 
            :old.CASE_NUMBER, 
            :old.CASE_TYPE, 
            :old.MAG_CONVICTION_DATE, 
            :old.CASE_SUB_TYPE, 
            :old.CASE_TITLE, 
            :old.CASE_DESCRIPTION, 
            :old.LINKED_CASE_ID, 
            :old.BAIL_MAG_CODE, 
            :old.REF_COURT_ID, 
            :old.COURT_ID, 
            :old.CHARGE_IMPORT_INDICATOR, 
            :old.SEVERED_IND, 
            :old.INDICT_RESP, 
            :old.DATE_IND_REC, 
            :old.PROS_AGENCY_REFERENCE, 
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION, 
            :old.CASE_CLASS, 
            :old.JUDGE_REASON_FOR_APPEAL, 
            :old.RESULTS_VERIFIED, 
            :old.LENGTH_TAPE, 
            :old.NO_PAGE_PROS_EVIDENCE, 
            :old.NO_PROS_WITNESS, 
            :old.EST_PDH_TRIAL_LENGTH, 
            :old.indictment_info_1, 
            :old.indictment_info_2, 
            :old.indictment_info_3, 
            :old.indictment_info_4, 
            :old.indictment_info_5, 
            :old.indictment_info_6, 
            :old.POLICE_OFFICER_ATTENDING, 
            :old.CPS_CASE_WORKER, 
            :old.EXPORT_CHARGES, 
            :old.IND_CHANGE_STATUS,
            :old.MAGISTRATES_CASE_REF,
            :old.CLASS_CODE,
            :old.OFFENCE_GROUP_UPDATE,
            l_trig_event);

  END IF;

END;
/

CREATE OR REPLACE TRIGGER MTRG_MERC_REF_STOR_BIR_TR
  BEFORE INSERT
  ON MTBL_MERC_REFRESH_STORAGE
  FOR EACH ROW

BEGIN

  IF :NEW.MERC_REFRESH_STORAGE_ID IS NULL THEN

    SELECT MSEQ_MERC_REFRESH_STORAGE_SEQ.NEXTVAL
    INTO   :NEW.MERC_REFRESH_STORAGE_ID
    FROM   DUAL;

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-- Possible future enhancements:
-------------------------------------------------------------------------------
--   Change the types for the in parameters to be the column types instead;
--   Replace counsel_type type declaration to use SYS_REFCURSOR;
--   Investigate the views to see if they can be improved;
--   For search_counsel & search_defendants see if we can ignore the firstname
--       and surname fields on the database if they are null (similar to how
--       the checks for null on the past in parameters are done);
--   See if the passed in values (for VARCHAR2's) is 0 length or just spaces,
--       if so, would we want to convert to null, and therefore ignore it?;
--   Change name of package to be consistent with Oracle coding standards
--       e.g. counsel_facilities_pkg
-------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY counselfacilities AS
    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER) IS
    BEGIN
        -- Vastly improved this query by removing the two sub-queries with the
        -- minus operations
        OPEN p_counsel_cursor_out FOR
			 SELECT * FROM (
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_court_room_id_in IS NULL)
                   OR (court_room_id = p_court_room_id_in))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END get_counsel_sign_in;


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2) IS
    BEGIN
    	OPEN p_counsel_cursor_out FOR
            SELECT * FROM
            (
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SH_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT * 
            FROM   XHB_COUNSEL_FACILITIES_SHDID_V
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    sh_leg_rep_id IS NOT NULL
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(leg_rep_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
		) ORDER BY court_site_code, is_floating, crest_court_room_no,
				   sitting_sequence_no, time_listed, sh_sequence_no;
    END search_counsel;


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2) IS
    BEGIN
        OPEN p_counsel_cursor_out FOR
            SELECT *
            FROM   xhb_counsel_facilities_sh_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')))
        UNION
            SELECT *
            FROM   xhb_counsel_facilities_shdid_v
            WHERE  court_id   = p_court_id_in
            AND    start_date = p_start_date_in
            AND    ((p_first_name_in IS NULL)
                   OR (UPPER(NVL(def_first_name, '%')) LIKE UPPER(p_first_name_in || '%')))
            AND    ((p_surname_in IS NULL)
                   OR (UPPER(NVL(def_surname, '%'))    LIKE UPPER(p_surname_in || '%')));
    END search_defendants;
END counselfacilities;
/

CREATE OR REPLACE PACKAGE BODY xhb_view_schedule_pkg AS
	   PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                              court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							  start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
							  court_room_id_in IN  XHB_SITTING.court_room_id%TYPE) AS
       BEGIN
	   		-- would we want to validate the passed in parameters?
			-- court_id_in and start_date_in to be not null?
	   
	        OPEN results_out FOR
				SELECT DISTINCT
					HEARING_LIST.START_DATE,
				      SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING,
				      COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
				      SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
				      CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
				      REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
				      DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
				      DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, DEFENDANT.SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID,
				      REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, REF_JUDGE.SURNAME JUDGE_SURNAME, REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1,
					  COURT_SITE.COURT_SITE_CODE				
				FROM  XHB_HEARING_LIST HEARING_LIST,
				      XHB_SITTING SITTING,
				      XHB_COURT_ROOM COURT_ROOM,
					  XHB_COURT_SITE COURT_SITE,
				      XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
				      XHB_HEARING HEARING,
				      XHB_CASE CASE,
				      XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
				      XHB_DEFENDANT DEFENDANT,
				      XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
				      XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
				      XHB_REF_JUDGE REF_JUDGE
				WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND	  SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND	  SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
				AND   SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND	  HEARING.CASE_ID = CASE.CASE_ID
				AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND   SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND   DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND   XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND   HEARING_LIST.COURT_ID = court_id_in
				AND   HEARING_LIST.START_DATE = start_date_in
				AND   ((court_room_id_in IS NULL) OR (SITTING.COURT_ROOM_ID = court_room_id_in))	
				--ORDER BY SCHEDULED_HEARING.SCHEDULED_HEARING_ID;
				ORDER BY court_site_code,
					  	 is_floating,
						 crest_court_room_no,
				   	  	 sitting_sequence_no,
						 NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time),
						 SCHEDULED_HEARING.SEQUENCE_NO; 	   
       END get_schedule;


	   PROCEDURE get_daily_list(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							    start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE) AS
       BEGIN
	   		OPEN results_out FOR
				SELECT DISTINCT
					  'Daily List' DOCUMENT_NAME, SYSDATE UNIQUEID, 'Criminal' LISTCATEGORY,
					  HEARING_LIST.LIST_ID, NVL(HEARING_LIST.LIST_TYPE,'Unspecified List TYPE') DOCUMENTTYPE, HEARING_LIST.START_DATE LIST_START_DATE, HEARING_LIST.END_DATE LIST_END_DATE, LTRIM(NVL(HEARING_LIST.STATUS,'Unspecified Status') || ' ' || NVL(HEARING_LIST.EDITION_NO,-1)) LIST_VERSION, NVL(HEARING_LIST.PRINT_REFERENCE,'Unspecified Print Reference') LIST_PRINT_REF, NVL(HEARING_LIST.PUBLISHED_TIME,'01/JAN/1900') LIST_PUBLISHED, HEARING_LIST.CREST_LIST_ID LIST_CREST_LIST_ID,
					  COURTHOUSE.COURT_ID COURTHOUSE_COURT_ID, COURTHOUSE.COURT_PREFIX COURTHOUSE_TYPE, COURTHOUSE.COURT_CODE COURTHOUSE_CODE, COURTHOUSE.COURT_NAME COURTHOUSE_NAME,
					  COURTHOUSE_ADDRESS.ADDRESS_ID COURTHOUSE_ADDRESS_ID, COURTHOUSE_ADDRESS.ADDRESS_1 COURTHOUSE_ADDRESS_1, COURTHOUSE_ADDRESS.ADDRESS_2 COURTHOUSE_ADDRESS_2, COURTHOUSE_ADDRESS.ADDRESS_3 COURTHOUSE_ADDRESS_3, COURTHOUSE_ADDRESS.ADDRESS_4 COURTHOUSE_ADDRESS_4, COURTHOUSE_ADDRESS.TOWN COURTHOUSE_TOWN, COURTHOUSE_ADDRESS.COUNTY COURTHOUSE_COUNTY, COURTHOUSE_ADDRESS.POSTCODE COURTHOUSE_POSTCODE,
					  COURTSITE.COURT_SITE_ID, COURTSITE.COURT_ID COURTSITE_COURT_ID, COURTSITE.COURT_SITE_CODE COURTSITE_CODE, COURTSITE.COURT_SITE_NAME COURTSITE_NAME,
					  COURTSITE_ADDRESS.ADDRESS_ID COURTSITE_ADDRESS_ID, COURTSITE_ADDRESS.ADDRESS_1 COURTSITE_ADDRESS_1, COURTSITE_ADDRESS.ADDRESS_2 COURTSITE_ADDRESS_2, COURTSITE_ADDRESS.ADDRESS_3 COURTSITE_ADDRESS_3, COURTSITE_ADDRESS.ADDRESS_4 COURTSITE_ADDRESS_4, COURTSITE_ADDRESS.TOWN COURTSITE_TOWN, COURTSITE_ADDRESS.COUNTY COURTSITE_COUNTY, COURTSITE_ADDRESS.POSTCODE COURTSITE_POSTCODE,
					  COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
					  SITTING.SITTING_ID, SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING, SITTING.SITTING_NOTE, SITTING.SITTING_TIME,
					  REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, NVL(REF_JUDGE.SURNAME,' ') JUDGE_SURNAME, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1,
					  REF_JUSTICE1.REF_JUSTICE_ID JUSTICE1_ID, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, '', REF_JUSTICE1.TITLE) JUSTICE1_TITLE, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME1, REF_JUSTICE1.JUSTICE_NAME) JUSTICE1_NAME,
					  REF_JUSTICE2.REF_JUSTICE_ID JUSTICE2_ID, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, '', REF_JUSTICE2.TITLE) JUSTICE2_TITLE, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME2, REF_JUSTICE2.JUSTICE_NAME) JUSTICE2_NAME,
					  REF_JUSTICE3.REF_JUSTICE_ID JUSTICE3_ID, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, '', REF_JUSTICE3.TITLE) JUSTICE3_TITLE, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME3, REF_JUSTICE3.JUSTICE_NAME) JUSTICE3_NAME,
					  REF_JUSTICE4.REF_JUSTICE_ID JUSTICE4_ID, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, '', REF_JUSTICE4.TITLE) JUSTICE4_TITLE, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME4, REF_JUSTICE4.JUSTICE_NAME) JUSTICE4_NAME,
					  SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
					  REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
					  CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
					  REF_COURT.REF_COURT_ID, REF_COURT.COURT_TYPE, REF_COURT.CREST_CODE, REF_COURT.COURT_SHORT_NAME, REF_COURT.COURT_FULL_NAME,
					  DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
					  DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, NVL(DEFENDANT.SURNAME,' ') SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID, HEARING_LIST.START_DATE
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
  					  XHB_REF_JUSTICE REF_JUSTICE1,
  					  XHB_REF_JUSTICE REF_JUSTICE2,
  					  XHB_REF_JUSTICE REF_JUSTICE3,
  					  XHB_REF_JUSTICE REF_JUSTICE4,
  					  XHB_REF_COURT REF_COURT,
  					  XHB_COURT_SITE COURTSITE,
  					  XHB_COURT COURTHOUSE,
  					  XHB_ADDRESS COURTSITE_ADDRESS,
  					  XHB_ADDRESS COURTHOUSE_ADDRESS
				WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND	  SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
				AND	  SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
				AND	  SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
				AND	  HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
				AND	  HEARING.CASE_ID = CASE.CASE_ID
				AND	  SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
				AND	  SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
				AND	  DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
				AND	  XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
				AND	  SITTING.REF_JUSTICE1_ID = REF_JUSTICE1.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE2_ID = REF_JUSTICE2.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE3_ID = REF_JUSTICE3.REF_JUSTICE_ID(+)
				AND	  SITTING.REF_JUSTICE4_ID = REF_JUSTICE4.REF_JUSTICE_ID(+)
				AND	  CASE.REF_COURT_ID = REF_COURT.REF_COURT_ID(+)
				AND	  COURT_ROOM.COURT_SITE_ID = COURTSITE.COURT_SITE_ID
				AND	  COURTSITE.ADDRESS_ID = COURTSITE_ADDRESS.ADDRESS_ID(+)
				AND	  HEARING_LIST.COURT_ID = COURTHOUSE.COURT_ID
				AND	  COURTHOUSE.ADDRESS_ID = COURTHOUSE_ADDRESS.ADDRESS_ID(+)
				AND	  HEARING_LIST.COURT_ID = court_id_in
				AND	  HEARING_LIST.START_DATE = start_date_in
--				ORDER BY COURTSITE.COURT_SITE_CODE,
--  					  	 SITTING.IS_FLOATING,
--  						 COURT_ROOM.CREST_COURT_ROOM_NO,
--  						 SITTING.SITTING_SEQUENCE_NO,
--  						 SCHEDULED_HEARING.SEQUENCE_NO;
				ORDER BY court_site_code,
					  	 is_floating,
						 crest_court_room_no,
				   	  	 sitting_sequence_no,
						 NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time),
						 SCHEDULED_HEARING.SEQUENCE_NO;		 
	   END get_daily_list;
END xhb_view_schedule_pkg;
/

/*
 * Procedure for updating the XHB_CR_LIVE_STATUS table
 * at the end of each day.  Called from an Oracle job
 * at a predefined time (probably say 6 in the morning).
 */

CREATE OR REPLACE PACKAGE xhb_cr_live_status_pkg AS

  PROCEDURE update_xhb_cr_live_status;

END xhb_cr_live_status_pkg;
/
show errors

/*
 * Procedure for updating the XHB_CR_LIVE_STATUS table
 * at the end of each day.  Called from an Oracle job
 * at a predefined time (probably say 6 in the morning).
 */

CREATE OR REPLACE PACKAGE BODY xhb_cr_live_status_pkg AS

  PROCEDURE update_xhb_cr_live_status IS

    BEGIN

    UPDATE XHB_CR_LIVE_STATUS
    SET    SCHEDULED_HEARING_ID = NULL,
           TIME_STATUS_SET = SYSDATE,
           INTERNET_HELP_CODE = NULL,
           INTERNET_STATUS = 'No Information to display',
           PUBLIC_DISPLAY_STATUS = NULL;

  END;

END xhb_cr_live_status_pkg;
/
show errors

/*
 * Script to set up the job for resetting varous pieces of
 * data in the XHB_CR_LIVE_STATUS table.  The job needs to
 * be scheduled after all other backups, imports etc. have
 * been done.  Suggested time for this would be 06:00am.
 *
 * The job calls the update_xhb_cr_live_status procedure.
 */

VARIABLE l_jobno NUMBER

BEGIN

  DBMS_JOB.SUBMIT(:l_jobno,
                  'XHB_CR_LIVE_STATUS_PKG.update_xhb_cr_live_status;',
                  trunc(sysdate)+1+6/24,
                  'SYSDATE + 1');

  COMMIT;

END;
/

PRINT l_jobno

CREATE OR REPLACE PACKAGE xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE);

	   PROCEDURE get_by_case_id_date(results_out   OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                cat_desc_in       IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE);

	   PROCEDURE get_by_case_eventdesc_date(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_desc_id_in  IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_eventtype_date_gt(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

	   PROCEDURE get_by_case_id_eventtype(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                event_type_in     IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE);

	   PROCEDURE get_by_case_id_date_pd(results_out  OUT SYS_REFCURSOR,
                                case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE);

END xhb_court_log_pkg;
/

CREATE OR REPLACE PACKAGE BODY xhb_court_log_pkg AS
	   PROCEDURE get_by_case_id(results_out     OUT SYS_REFCURSOR,
                                    case_id_in      IN  XHB_COURT_LOG_ENTRY.case_id%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE COURT_LOG_ENTRY.CASE_ID = case_id_in	
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id;


       PROCEDURE get_by_case_id_date(results_out    OUT SYS_REFCURSOR,
                                     case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                     start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                     end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND   COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date;


       PROCEDURE get_by_case_id_date_catdesc(results_out    OUT SYS_REFCURSOR,
                                             case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                             start_date_in  IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             end_date_in    IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                             cat_desc_in    IN  XHB_COURT_LOG_CATEGORY_DESC.category_description%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_CATEGORY_DESC CATEGORY_DESC, XHB_COURT_LOG_CATEGORY CATEGORY
                WHERE CATEGORY_DESC.category_desc_id = CATEGORY.category_desc_id
                AND   CATEGORY_DESC.category_description = cat_desc_in
                AND   COURT_LOG_ENTRY.EVENT_DESC_ID = CATEGORY.EVENT_DESC_ID
                AND   COURT_LOG_ENTRY.CASE_ID = case_id_in	
                AND   COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_date_catdesc;


       PROCEDURE get_by_case_eventdesc_date(results_out       OUT SYS_REFCURSOR,
                                            case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                            event_desc_id_in  IN  XHB_COURT_LOG_ENTRY.event_desc_id%TYPE,
                                            start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY
                WHERE COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND   COURT_LOG_ENTRY.EVENT_DESC_ID = event_desc_id_in
                AND   COURT_LOG_ENTRY.DATE_TIME >= start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventdesc_date;


       PROCEDURE get_by_case_eventtype_date_gt(results_out       OUT SYS_REFCURSOR,
                                               case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                               event_type_in  IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE,
                                               start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND   COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND   COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                AND   COURT_LOG_ENTRY.DATE_TIME > start_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_eventtype_date_gt;


       PROCEDURE get_by_case_id_eventtype(results_out    OUT SYS_REFCURSOR,
                                          case_id_in     IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                          event_type_in  IN  XHB_COURT_LOG_EVENT_DESC.event_type%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND   COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND   COURT_LOG_EVENT_DESC.EVENT_TYPE = event_type_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID;		   
       END get_by_case_id_eventtype;


       PROCEDURE get_by_case_id_date_pd(results_out       OUT SYS_REFCURSOR,
                                        case_id_in        IN  XHB_COURT_LOG_ENTRY.case_id%TYPE,
                                        public_display_in IN  XHB_COURT_LOG_EVENT_DESC.public_display%TYPE,
                                        start_date_in     IN  XHB_COURT_LOG_ENTRY.date_time%TYPE,
                                        end_date_in       IN  XHB_COURT_LOG_ENTRY.date_time%TYPE) AS
       BEGIN
	        OPEN results_out FOR
                SELECT 
                      COURT_LOG_ENTRY.ENTRY_ID, COURT_LOG_ENTRY.CASE_ID, COURT_LOG_ENTRY.VERSION, 
                      COURT_LOG_ENTRY.LAST_UPDATED_BY, COURT_LOG_ENTRY.CREATED_BY, COURT_LOG_ENTRY.CREATION_DATE, 
                      COURT_LOG_ENTRY.LAST_UPDATE_DATE, COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.EVENT_DESC_ID, 
                      COURT_LOG_ENTRY.LOG_ENTRY_XML, COURT_LOG_ENTRY.DEFENDANT_ON_CASE_ID, COURT_LOG_ENTRY.DEFENDANT_ON_OFFENCE_ID
                FROM  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY, XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
                WHERE COURT_LOG_ENTRY.EVENT_DESC_ID = COURT_LOG_EVENT_DESC.EVENT_DESC_ID
                AND   COURT_LOG_ENTRY.CASE_ID = case_id_in
                AND   COURT_LOG_EVENT_DESC.PUBLIC_DISPLAY = public_display_in
                AND   COURT_LOG_ENTRY.DATE_TIME BETWEEN start_date_in AND end_date_in
                ORDER BY COURT_LOG_ENTRY.DATE_TIME, COURT_LOG_ENTRY.ENTRY_ID DESC;		   
       END get_by_case_id_date_pd;


END xhb_court_log_pkg;
/


/*
 * Changes, additions or deletion of standing data
 */

INSERT INTO xhb_cr_live_status (court_room_id, time_status_set, internet_status)
select court_room_id,
       SYSDATE,
       'No Information to display'
FROM   xhb_court_room;

UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 110;
UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 112;
UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 210;
UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 212;
UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 310;
UPDATE XHB_SCREEN SET type = 'plasma' WHERE screen_id = 312;

-- Witness Released
INSERT INTO XHB_COURT_LOG_CATEGORY_DESC ( CATEGORY_TYPE, CATEGORY_DESCRIPTION, CREATED_BY, LAST_UPDATED_BY)
VALUES ( 12, 'Witness_Released', 'Xhibit', 'Xhibit' );

-- Insert Categories for Witness Released

-- Appeal Witness Released
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY)
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 20604),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Witness_Released'),
         'Xhibit', 'Xhibit' );

-- Trail Witness Released
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY)
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 20905),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Witness_Released'),
         'Xhibit', 'Xhibit' );

COMMIT;


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = 29, last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
