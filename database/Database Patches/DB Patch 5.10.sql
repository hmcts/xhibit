/*
 * Patch for DB Release 5.10
 *
 * Date Here 3rd December 2003
 */

/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 *
 */

ALTER TABLE XHB_CREST_IMPORT ADD (LAST_UPDATE_DATE  DATE DEFAULT SYSDATE NOT NULL,
                                  CREATION_DATE     DATE DEFAULT SYSDATE NOT NULL,
                                  CREATED_BY        VARCHAR2(30) DEFAULT USER NOT NULL,
                                  LAST_UPDATED_BY   VARCHAR2(30) DEFAULT USER NOT NULL,
                                  VERSION           NUMBER(5) DEFAULT 1 NOT NULL);

ALTER TABLE XHB_DEFENDANT_ON_CASE ADD (COLLECT_MAGISTRATE_COURT_ID NUMBER(8) NULL);

ALTER TABLE XHB_DEFENDANT_ON_CASE
      ADD (FOREIGN KEY (COLLECT_MAGISTRATE_COURT_ID)
           REFERENCES XHB_REF_COURT(REF_COURT_ID));

-- Need to delete some data before creating a UNIQUE index

DELETE FROM XHB_CR_LIVE_STATUS
WHERE  ROWID NOT IN(SELECT MIN(ROWID)
                    FROM   XHB_CR_LIVE_STATUS
                    GROUP  BY court_room_id);

DROP INDEX XHB_CR_LIVE_STATUS_COURT_RM_FK;

CREATE UNIQUE INDEX XHB_CR_LIVE_STATUS_COURT_RM_FK ON XHB_CR_LIVE_STATUS
  (COURT_ROOM_ID)
  TABLESPACE XHIBITX
  STORAGE (INITIAL 256K
           NEXT 256K
           PCTINCREASE 0);

CREATE INDEX XHB_REF_ADVOCATE_CREST_ADV_IDX ON XHB_REF_ADVOCATE
  (CREST_ADVOCATE_ID ASC)
  TABLESPACE XHIBITREFX
  STORAGE (INITIAL 256K
           NEXT 256K
           PCTINCREASE 0);

CREATE TABLE MTBL_MERC_GLOBAL_LOCAL_DATA (
       COURT_ID         NUMBER(8),
       ADDR1            VARCHAR2(30),
       ADDR2            VARCHAR2(30),
       ADDR3            VARCHAR2(30),
       ADDR4            VARCHAR2(30),
       TOWN             VARCHAR2(30),
       COUNTY           VARCHAR2(30),
       POSTCODE         VARCHAR2(8),
       TEL_NO           VARCHAR2(14),
       CLERK_NAME       VARCHAR2(35),
       FAX_NO           VARCHAR2(14),
       DX_REF           VARCHAR2(35),
       LOCATION_CODE    VARCHAR2(2),
       CHA_CENTRAL_IND  VARCHAR2(1),
       FIRM_NAME        VARCHAR2(35),
       ADV_ID           NUMBER(8),
       CHA_ID           NUMBER(8),
       SURNAME          VARCHAR2(35),
       FORENAME1        VARCHAR2(35),
       INITIALS         VARCHAR2(4),
       ADV_TYPE_IND     VARCHAR2(1),
       ADV_VERS         NUMBER(2),
       HONOURS          VARCHAR2(8),
       FORENAME2        VARCHAR2(35),
       OBS_IND          VARCHAR2(1),
       TITLE            VARCHAR2(25),
       BAR_NO           NUMBER(5),
       VAT_NO           VARCHAR2(9),
       YEAR_OF_CALL     NUMBER(4),
       ADV_TYPE         VARCHAR2(1),
       ADV_CENTRAL_IND  VARCHAR2(1))
         TABLESPACE MERCATORD
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
		xcase.case_title				CASE_TITLE,
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
		xcase.case_title				CASE_TITLE,
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

DROP TABLE AUD_CREST_IMPORT;

CREATE TABLE AUD_CREST_IMPORT TABLESPACE AUDITD AS SELECT * FROM XHB_CREST_IMPORT WHERE 1 = 0;
ALTER TABLE AUD_CREST_IMPORT ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

DROP TABLE AUD_DEFENDANT_ON_CASE;

CREATE TABLE AUD_DEFENDANT_ON_CASE TABLESPACE AUDITD AS SELECT * FROM XHB_DEFENDANT_ON_CASE WHERE 1 = 0;
ALTER TABLE AUD_DEFENDANT_ON_CASE ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);


/*
 * Changes, additions or deletion of sequences
 */

DROP SEQUENCE XHB_ROTATION_SET_SEQ;

CREATE SEQUENCE XHB_ROTATION_SET_SEQ
START WITH 34
INCREMENT BY 1
MINVALUE 1
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

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BIR_TR
  BEFORE INSERT
  ON XHB_CREST_IMPORT
  FOR EACH ROW

BEGIN

  IF :NEW.CREST_IMPORT_ID IS NULL THEN

    SELECT XHB_CREST_IMPORT_SEQ.NEXTVAL
    INTO   :NEW.CREST_IMPORT_ID
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

CREATE OR REPLACE TRIGGER XHB_CREST_IMPORT_BUR_TR
  BEFORE UPDATE OR DELETE
  ON XHB_CREST_IMPORT
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
  IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_CREST_IMPORT') = 1) THEN

    INSERT INTO AUD_CREST_IMPORT
    VALUES (:old.CREST_IMPORT_ID,
            :old.STATUS,
            :old.COURT_ID,
            :old.IMPORT_TYPE,
            :old.LAST_UPDATE_DATE, 
            :old.CREATION_DATE, 
            :old.CREATED_BY, 
            :old.LAST_UPDATED_BY, 
            :old.VERSION,
            l_trig_event);

  END IF;

END;
/

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
            l_trig_event);

  END IF;

END;
/


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_ref_advocate_pkg AS

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE);

END xhb_ref_advocate_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_ref_advocate_pkg AS

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

  TYPE rec_ref_advocate IS RECORD (COURT_ID         NUMBER(8),
                                   ADDR1            VARCHAR2(30),
                                   ADDR2            VARCHAR2(30),
                                   ADDR3            VARCHAR2(30),
                                   ADDR4            VARCHAR2(30),
                                   TOWN             VARCHAR2(30),
                                   COUNTY           VARCHAR2(30),
                                   POSTCODE         VARCHAR2(8),
                                   TEL_NO           VARCHAR2(14),
                                   CLERK_NAME       VARCHAR2(35),
                                   FAX_NO           VARCHAR2(14),
                                   DX_REF           VARCHAR2(35),
                                   LOCATION_CODE    VARCHAR2(2),
                                   CHA_CENTRAL_IND  VARCHAR2(1),
                                   FIRM_NAME        VARCHAR2(35),
                                   ADV_ID           NUMBER(8),
                                   CHA_ID           NUMBER(8),
                                   SURNAME          VARCHAR2(35),
                                   FORENAME1        VARCHAR2(35),
                                   INITIALS         VARCHAR2(4),
                                   ADV_TYPE_IND     VARCHAR2(1),
                                   ADV_VERS         NUMBER(2),
                                   HONOURS          VARCHAR2(8),
                                   FORENAME2        VARCHAR2(35),
                                   OBS_IND          VARCHAR2(1),
                                   TITLE            VARCHAR2(25),
                                   BAR_NO           NUMBER(5),
                                   VAT_NO           VARCHAR2(9),
                                   YEAR_OF_CALL     NUMBER(4),
                                   ADV_TYPE         VARCHAR2(1),
                                   ADV_CENTRAL_IND  VARCHAR2(1));

  l_advocate_record rec_ref_advocate;

  l_address_id_found  VARCHAR2(1) := 'N';
  l_advocate_count    NUMBER;
  l_cha_address_id    NUMBER;
  l_ref_chamber_id    NUMBER;
  l_ref_adv_id        NUMBER;
  l_ref_leg_rep_id    NUMBER;
  l_curr_address_id   NUMBER;
  l_curr_leg_rep_id   NUMBER;
  l_run_date          DATE := TRUNC(SYSDATE);

  CURSOR c_ref_advocate IS
    SELECT *
    FROM   mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  BEGIN

    OPEN c_ref_advocate;

    LOOP

      FETCH c_ref_advocate
      INTO  l_advocate_record;

      EXIT WHEN c_ref_advocate %NOTFOUND;

      BEGIN

        SELECT ref_chamber_id
        INTO   l_ref_chamber_id
        FROM   xhb_ref_chamber
        WHERE  TRUNC(last_update_date) = l_run_date
        AND    crest_chamber_id = l_advocate_record.cha_id
        AND    court_id = p_court_id;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN

            /*
             * XHB_ADDRESS and XHB_REF_CHAMBER
             */

            BEGIN        

              SELECT address_id
              INTO   l_cha_address_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              l_address_id_found := 'Y';

              EXCEPTION

                WHEN NO_DATA_FOUND THEN -- need to create both records

                  l_address_id_found := 'N';

            END;

            IF l_address_id_found = 'N' THEN -- no data returned so create both records

              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              INSERT INTO xhb_ref_chamber (obs_ind,
                                           is_global,
                                           dx_ref,
                                           location_code,
                                           crest_chamber_id,
                                           firm_name,
                                           address_id,
                                           court_id)
                                   VALUES (l_advocate_record.obs_ind,
                                           NVL(l_advocate_record.cha_central_ind,'N'),
                                           l_advocate_record.dx_ref,
                                           l_advocate_record.location_code,
                                           l_advocate_record.cha_id,
                                           l_advocate_record.firm_name,
                                           l_curr_address_id,
                                           p_court_id);

              SELECT XHB_REF_CHAMBER_SEQ.CURRVAL
              INTO   l_ref_chamber_id
              FROM   dual;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NULL THEN -- address_id found but is null so create address and then update
                                                -- XHB_REF_CHAMBER
              INSERT INTO xhb_address (address_1,
                                       address_2,
                                       address_3,
                                       address_4,
                                       town,
                                       county,
                                       postcode)
                               VALUES (l_advocate_record.addr1,
                                       l_advocate_record.addr2,
                                       l_advocate_record.addr3,
                                       l_advocate_record.addr4,
                                       l_advocate_record.town,
                                       l_advocate_record.county,
                                       l_advocate_record.postcode);

              /*
               * Need to get the address ID that was just used to create the above row.
               */

              SELECT XHB_ADDRESS_SEQ.CURRVAL
              INTO   l_curr_address_id
              FROM   dual;

              UPDATE XHB_REF_CHAMBER
              SET    address_id = l_curr_address_id,
                     obs_ind = l_advocate_record.obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            ELSIF l_address_id_found = 'Y' AND
                  l_cha_address_id IS NOT NULL THEN -- There was a crest_chamber_id and a valid address_id in the XHB_REF_CHAMBER table

              l_curr_address_id := l_cha_address_id;

              UPDATE xhb_address
              SET    address_1 = l_advocate_record.addr1,
                     address_2 = l_advocate_record.addr2,
                     address_3 = l_advocate_record.addr3,
                     address_4 = l_advocate_record.addr4,
                     town = l_advocate_record.town,
                     county = l_advocate_record.county,
                     postcode = l_advocate_record.postcode
              WHERE  address_id = l_curr_address_id;

              UPDATE xhb_ref_chamber
              SET    obs_ind = l_advocate_record.obs_ind,
                     is_global = NVL(l_advocate_record.cha_central_ind,'N'),
                     dx_ref = l_advocate_record.dx_ref,
                     location_code = l_advocate_record.location_code,
                     firm_name = l_advocate_record.firm_name,
                     address_id = l_curr_address_id
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

              SELECT ref_chamber_id
              INTO   l_ref_chamber_id
              FROM   xhb_ref_chamber
              WHERE  crest_chamber_id = l_advocate_record.cha_id
              AND    court_id = p_court_id;

            END IF;

            /*
             * XHB_CONTACT_DETAIL
             */

            IF l_cha_address_id IS NULL OR
               l_address_id_found = 'N' THEN  -- must have created new address so create contact details

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Phone',
                                              l_advocate_record.tel_no,
                                              l_curr_address_id);

              INSERT INTO xhb_contact_detail (contact_type,
                                              contact_value,
                                              address_id)
                                      VALUES ('Fax',
                                              l_advocate_record.fax_no,
                                              l_curr_address_id);

            ELSE 

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.tel_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Phone','Tel');

              UPDATE xhb_contact_detail
              SET    contact_value = l_advocate_record.fax_no
              WHERE  address_id = l_curr_address_id
              AND    contact_type IN ('Fax','FAX');

            END IF;

      END;

      /*
       * XHB_REF_ADVOCATE and XHB_REF_LEGAL_REPRESENTATIVE
       */

      /*
       * This part always done regardless of initial query to
       * obtain the ref_chamber_id.
       */

      BEGIN

        SELECT ref_advocate_id,
               ref_legal_rep_id
        INTO   l_ref_adv_id,
               l_ref_leg_rep_id
        FROM   xhb_ref_advocate
        WHERE  ref_legal_rep_id IN (SELECT ref_legal_rep_id
                                    FROM   xhb_ref_legal_representative
                                    WHERE  court_id = p_court_id)
        AND    crest_advocate_id = l_advocate_record.adv_id
        AND    crest_chamber_id = l_advocate_record.cha_id;

        l_advocate_count := 1;

        EXCEPTION

          WHEN NO_DATA_FOUND THEN -- need to create both records

            l_advocate_count := 0;

      END;

      IF l_advocate_count = 0 THEN -- insert records as does not exist

        INSERT INTO xhb_ref_legal_representative (first_name,
                                                  middle_name,
                                                  surname,
                                                  title,
                                                  initials,
                                                  legal_rep_type,
                                                  court_id,
                                                  obs_ind)
                                          VALUES (l_advocate_record.forename1,
                                                  l_advocate_record.forename2,
                                                  l_advocate_record.surname,
                                                  l_advocate_record.title,
                                                  l_advocate_record.initials,
                                                  l_advocate_record.adv_type,
                                                  p_court_id,
                                                  l_advocate_record.obs_ind);

        SELECT XHB_REF_LEGAL_REP_SEQ.CURRVAL
        INTO   l_curr_leg_rep_id
        FROM   dual;

        INSERT INTO xhb_ref_advocate (is_global,
                                      crest_advocate_id,
                                      crest_chamber_id,
                                      obs_ind,
                                      year_of_call,
                                      vat_no,
                                      bar_no,
                                      honours,
                                      adv_type_ind,
                                      ref_legal_rep_id,
                                      ref_chamber_id)
                              VALUES (NVL(l_advocate_record.adv_central_ind,'N'),
                                      l_advocate_record.adv_id,
                                      l_advocate_record.cha_id,
                                      l_advocate_record.obs_ind,
                                      l_advocate_record.year_of_call,
                                      l_advocate_record.vat_no,
                                      l_advocate_record.bar_no,
                                      l_advocate_record.honours,
                                      l_advocate_record.adv_type_ind,
                                      l_curr_leg_rep_id,
                                      l_ref_chamber_id);

      ELSIF l_advocate_count = 1 THEN

        UPDATE xhb_ref_legal_representative
        SET    first_name = l_advocate_record.forename1,
               middle_name = l_advocate_record.forename2,
               surname = l_advocate_record.surname,
               title = l_advocate_record.title,
               legal_rep_type = l_advocate_record.adv_type,
               court_id = p_court_id
        WHERE  ref_legal_rep_id = l_ref_leg_rep_id;

        UPDATE xhb_ref_advocate
        SET    is_global = NVL(l_advocate_record.adv_central_ind,'N'),
               crest_advocate_id = l_advocate_record.adv_id,
               crest_chamber_id = l_advocate_record.cha_id,
               obs_ind = l_advocate_record.obs_ind,
               year_of_call = l_advocate_record.year_of_call,
               vat_no = l_advocate_record.vat_no,
               bar_no = l_advocate_record.bar_no,
               honours = l_advocate_record.honours,
               adv_type_ind = l_advocate_record.adv_type_ind,
               ref_legal_rep_id = l_ref_leg_rep_id,
               ref_chamber_id = l_ref_chamber_id
        WHERE  ref_advocate_id = l_ref_adv_id;

      END IF;

    END LOOP;

    CLOSE c_ref_advocate;

    UPDATE XHB_REF_LEGAL_REPRESENTATIVE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date;

    UPDATE XHB_REF_ADVOCATE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date;

    UPDATE XHB_REF_CHAMBER
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date;

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate;

END xhb_ref_advocate_pkg;
/
show errors

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
                ORDER BY COURT_LOG_ENTRY.DATE_TIME DESC, COURT_LOG_ENTRY.ENTRY_ID DESC;		   
       END get_by_case_id_date_pd;


END xhb_court_log_pkg;
/


/*
 * Changes, additions or deletion of standing data
 */

UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.AllCourtStatusPanelGenerator' WHERE PANEL_ID = 1;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.SummaryByNamePanelGenerator' WHERE PANEL_ID = 2;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.DailyListPanelGenerator' WHERE PANEL_ID = 3;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.CourtListPanelGenerator' WHERE PANEL_ID = 4;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.CourtDetailPanelGenerator' WHERE PANEL_ID = 5;
UPDATE XHB_PANEL SET CLASS_SOURCE='uk.gov.courtservice.xhibit.web.publicdisplay.cma.panels.JuryCurrentStatusPanelGenerator' WHERE PANEL_ID = 6;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=1;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=6;
UPDATE XHB_PAGE SET NAME='SNARE~A_JuryCurrentStatus_1' WHERE PAGE_ID=6;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=21;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=26;
UPDATE XHB_PAGE SET NAME='DOCKF~A_JuryCurrentStatus_1' WHERE PAGE_ID=26;
COMMIT;

UPDATE XHB_PAGE SET DESCRIPTION='All Court Status' WHERE PAGE_ID=31;
UPDATE XHB_PAGE SET DESCRIPTION='Jury Current Status' WHERE PAGE_ID=36;
UPDATE XHB_PAGE SET NAME='ISLEW~I_JuryCurrentStatus_1' WHERE PAGE_ID=36;
COMMIT;


/*
 * Updating of table XHB_VERSION
 */

UPDATE XHB_VERSION SET schema_version = NULL;

ALTER TABLE XHB_VERSION MODIFY (schema_version VARCHAR2(10));

UPDATE XHB_VERSION SET schema_version = '5.10', last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
