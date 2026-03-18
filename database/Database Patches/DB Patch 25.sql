/*
 * Patch for DB release 25
 *
 * 12th September 2003
 */

/*
 * Changes to XHB_ table definitions
 *
 * This may include removing foreign keys and possibly other constraints
 */

  /*
   * For this release, the script for converting CLOB to BLOB is appended
   * at the end.  This includes table def changes and trigger changes.
   */


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 * The standard procedure is as follows:
 *
 *     1. Drop the audit table (AUD_)
 *     2. Recreate audit table as select * from XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 *
 * Note that these are generally the BUR (update and delete) triggers as the BIR
 * (insert) triggers will only change on renaming the auditing columns within the
 * XHB_ table
 */

/*
 * Additional packages/procedures/functions or Changes to these
 */

-- See the body script for possible future enhancements
CREATE OR REPLACE PACKAGE counselfacilities AS
    TYPE counsel_type IS REF CURSOR;

    PROCEDURE get_counsel_sign_in(p_counsel_cursor_out OUT counsel_type,
                                  p_court_id_in        IN  NUMBER,
                                  p_start_date_in      IN  DATE,
                                  p_court_room_id_in   IN  NUMBER);


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2);


    PROCEDURE search_defendants(p_counsel_cursor_out OUT counsel_type,
                                p_court_id_in        IN  NUMBER,
                                p_start_date_in      IN  DATE,
                                p_first_name_in      IN  VARCHAR2,
                                p_surname_in         IN  VARCHAR2);
END counselfacilities;
/

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
                   OR (court_room_id = p_court_room_id_in));
    END get_counsel_sign_in;


    PROCEDURE search_counsel(p_counsel_cursor_out OUT counsel_type,
                             p_court_id_in        IN  NUMBER,
                             p_start_date_in      IN  DATE,
                             p_first_name_in      IN  VARCHAR2,
                             p_surname_in         IN  VARCHAR2) IS
    BEGIN
    	OPEN p_counsel_cursor_out FOR
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
                   OR (UPPER(NVL(leg_rep_surname, '%'))    LIKE UPPER(p_surname_in || '%')));
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

CREATE OR REPLACE PACKAGE xhb_view_schedule_pkg AS
	   PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                              court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							  start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
							  court_room_id_in IN  XHB_SITTING.court_room_id%TYPE);


	   PROCEDURE get_daily_list(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							    start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE);
END xhb_view_schedule_pkg;
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
				      SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING,
				      COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
				      SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
				      CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
				      REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
				      DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
				      DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, DEFENDANT.SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID,
				      REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, REF_JUDGE.SURNAME JUDGE_SURNAME, REF_JUDGE.REF_JUDGE_ID JUDGE_ID
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
				      XHB_REF_JUDGE REF_JUDGE
				WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
				AND	  SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
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
				ORDER BY SCHEDULED_HEARING.SCHEDULED_HEARING_ID;		   
       END get_schedule;


	   PROCEDURE get_daily_list(results_out      OUT SYS_REFCURSOR,
                                court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
							    start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE) AS
       BEGIN
	   		OPEN results_out FOR
				SELECT DISTINCT
					  'Daily List' DOCUMENT_NAME, SYSDATE UNIQUEID, 'Criminal' LISTCATEGORY,
					  HEARING_LIST.LIST_ID, HEARING_LIST.LIST_TYPE DOCUMENTTYPE, HEARING_LIST.START_DATE LIST_START_DATE, HEARING_LIST.END_DATE LIST_END_DATE, LTRIM(HEARING_LIST.STATUS || ' ' || HEARING_LIST.EDITION_NO) LIST_VERSION, HEARING_LIST.PRINT_REFERENCE LIST_PRINT_REF, HEARING_LIST.PUBLISHED_TIME LIST_PUBLISHED, HEARING_LIST.CREST_LIST_ID LIST_CREST_LIST_ID,
					  COURTHOUSE.COURT_ID COURTHOUSE_COURT_ID, COURTHOUSE.COURT_PREFIX COURTHOUSE_TYPE, COURTHOUSE.COURT_CODE COURTHOUSE_CODE, COURTHOUSE.COURT_NAME COURTHOUSE_NAME,
					  COURTHOUSE_ADDRESS.ADDRESS_ID COURTHOUSE_ADDRESS_ID, COURTHOUSE_ADDRESS.ADDRESS_1 COURTHOUSE_ADDRESS_1, COURTHOUSE_ADDRESS.ADDRESS_2 COURTHOUSE_ADDRESS_2, COURTHOUSE_ADDRESS.ADDRESS_3 COURTHOUSE_ADDRESS_3, COURTHOUSE_ADDRESS.ADDRESS_4 COURTHOUSE_ADDRESS_4, COURTHOUSE_ADDRESS.TOWN COURTHOUSE_TOWN, COURTHOUSE_ADDRESS.COUNTY COURTHOUSE_COUNTY, COURTHOUSE_ADDRESS.POSTCODE COURTHOUSE_POSTCODE,
					  COURTSITE.COURT_SITE_ID, COURTSITE.COURT_ID COURTSITE_COURT_ID, COURTSITE.COURT_SITE_CODE COURTSITE_CODE, COURTSITE.COURT_SITE_NAME COURTSITE_NAME,
					  COURTSITE_ADDRESS.ADDRESS_ID COURTSITE_ADDRESS_ID, COURTSITE_ADDRESS.ADDRESS_1 COURTSITE_ADDRESS_1, COURTSITE_ADDRESS.ADDRESS_2 COURTSITE_ADDRESS_2, COURTSITE_ADDRESS.ADDRESS_3 COURTSITE_ADDRESS_3, COURTSITE_ADDRESS.ADDRESS_4 COURTSITE_ADDRESS_4, COURTSITE_ADDRESS.TOWN COURTSITE_TOWN, COURTSITE_ADDRESS.COUNTY COURTSITE_COUNTY, COURTSITE_ADDRESS.POSTCODE COURTSITE_POSTCODE,
					  COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
					  SITTING.SITTING_ID, SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING, SITTING.SITTING_NOTE, SITTING.SITTING_TIME,
					  REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, REF_JUDGE.SURNAME JUDGE_SURNAME, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1,
					  REF_JUSTICE1.REF_JUSTICE_ID JUSTICE1_ID, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, '', REF_JUSTICE1.TITLE) JUSTICE1_TITLE, DECODE(REF_JUSTICE1.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME1, REF_JUSTICE1.JUSTICE_NAME) JUSTICE1_NAME,
					  REF_JUSTICE2.REF_JUSTICE_ID JUSTICE2_ID, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, '', REF_JUSTICE2.TITLE) JUSTICE2_TITLE, DECODE(REF_JUSTICE2.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME2, REF_JUSTICE2.JUSTICE_NAME) JUSTICE2_NAME,
					  REF_JUSTICE3.REF_JUSTICE_ID JUSTICE3_ID, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, '', REF_JUSTICE3.TITLE) JUSTICE3_TITLE, DECODE(REF_JUSTICE3.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME3, REF_JUSTICE3.JUSTICE_NAME) JUSTICE3_NAME,
					  REF_JUSTICE4.REF_JUSTICE_ID JUSTICE4_ID, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, '', REF_JUSTICE4.TITLE) JUSTICE4_TITLE, DECODE(REF_JUSTICE4.REF_JUSTICE_ID, NULL, SITTING.JUSTICENAME4, REF_JUSTICE4.JUSTICE_NAME) JUSTICE4_NAME,
					  SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
					  REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
					  CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS,
					  REF_COURT.REF_COURT_ID, REF_COURT.COURT_TYPE, REF_COURT.CREST_CODE, REF_COURT.COURT_SHORT_NAME, REF_COURT.COURT_FULL_NAME,
					  DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
					  DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, DEFENDANT.SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID
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
				AND	  SITTING.COURT_SITE_ID = COURTSITE.COURT_SITE_ID(+)
				AND	  COURTSITE.ADDRESS_ID = COURTSITE_ADDRESS.ADDRESS_ID(+)
				AND	  COURTSITE.COURT_ID = COURTHOUSE.COURT_ID(+)
				AND	  COURTHOUSE.ADDRESS_ID = COURTHOUSE_ADDRESS.ADDRESS_ID(+)
				AND	  HEARING_LIST.COURT_ID = court_id_in
				AND	  HEARING_LIST.START_DATE = start_date_in
				ORDER BY COURTSITE.COURT_SITE_CODE,
  					  	 SITTING.IS_FLOATING,
  						 COURT_ROOM.CREST_COURT_ROOM_NO,
  						 SITTING.SITTING_SEQUENCE_NO,
  						 SCHEDULED_HEARING.SEQUENCE_NO;
	   END get_daily_list;
END xhb_view_schedule_pkg;
/

/*
 * Any standing data inserts or changes
 */

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 121, 40704, 'Directions By Defendant Identified', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Defendant_Identified');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 122, 40705, 'Directions By Defendant Arraigned', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Defendant_Arraigned');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 123, 40706, 'Directions By Defendant Bail and Custody', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Defendant_Bail');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 124, 40707, 'Directions By Defendant Certificate of Attendance', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Defendant_Cert_Attend');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 125, 40708, 'Directions By Defendant Form B', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Defendant_Form_B');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 126, 40710, 'Directions By Case P+D Form', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Case_PDForm');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 127, 40711, 'Directions By Case Trial Time', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Case_Trial_Time');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 128, 40712, 'Directions By Case Directions', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Case_Directions');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 129, 40713, 'Directions By Case List As', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Case_ListAs');

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY,  LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION) 
VALUES ( 130, 40714, 'Directions By Case Defendant To Attend', 0, 'Xhibit', 'Xhibit',  'LC_TEXT_', 0, 1, 0, 1, 1, 0, 1, 0 , 'Directions_By_Case_Def_Attend');

COMMIT;

-- Directions By Case
INSERT INTO XHB_COURT_LOG_CATEGORY_DESC ( CATEGORY_TYPE, CATEGORY_DESCRIPTION, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( 10, 'Directions_By_Case', 'Xhibit', 'Xhibit' );

-- Directions By Defendant
INSERT INTO XHB_COURT_LOG_CATEGORY_DESC ( CATEGORY_TYPE, CATEGORY_DESCRIPTION, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( 11, 'Directions_By_Defendant', 'Xhibit', 'Xhibit' );

COMMIT;

-- Insert Categories for Directions By Defendant

-- Directions By Defendant Identified
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40704),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Defendant'),
         'Xhibit', 'Xhibit' );
         
-- Directions By Defendant Arraigned
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40705),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Defendant'),
         'Xhibit', 'Xhibit' );         
         
-- Directions By Defendant Bail and Custody
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40706),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Defendant'),
         'Xhibit', 'Xhibit' );         
         
-- Directions By Defendant Cert of Attendance
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40707),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Defendant'),
         'Xhibit', 'Xhibit' );         
         
-- Directions By Defendant Form B
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40708),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Defendant'),
         'Xhibit', 'Xhibit' ); 
         
COMMIT;         
         
-- Insert Categories for Directions By Case

-- Directions By Case P + D Form
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40710),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Case'),
         'Xhibit', 'Xhibit' );   
         
-- Directions By Case Trial Time
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40711),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Case'),
         'Xhibit', 'Xhibit' );          
         
-- Directions By Case Directions
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40712),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Case'),
         'Xhibit', 'Xhibit' );       
         
-- Directions By Case List As
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40713),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Case'),
         'Xhibit', 'Xhibit' );
         
-- Directions By Case To Attend
INSERT INTO XHB_COURT_LOG_CATEGORY ( EVENT_DESC_ID, CATEGORY_DESC_ID, CREATED_BY, LAST_UPDATED_BY) 
VALUES ( (Select EVENT_DESC_ID FROM XHB_COURT_LOG_EVENT_DESC WHERE EVENT_TYPE = 40714),
         (Select CATEGORY_DESC_ID FROM XHB_COURT_LOG_CATEGORY_DESC WHERE CATEGORY_DESCRIPTION = 'Directions_By_Case'),
         'Xhibit', 'Xhibit' );         
         
COMMIT;

/*
 * CLOB to BLOB conversion
 */

/*
 * Create the procedure that will do the conversion.
 */

CREATE OR REPLACE PROCEDURE prClobToBlob (inLob IN CLOB,
                                          outLob IN OUT NOCOPY BLOB) IS

/*
 *
 * outLob will be trimmed and filled with 
 * information from inLob
 * default charaterset for the database will be used.
 *
 * inlob and outLob need to be valid lob's.
 *
 */

  nPos INTEGER;
  vcBuff VARCHAR2(32000);
  rBuff   RAW(32000);
  nBuffSize BINARY_INTEGER;

BEGIN

  -- Check that we have valid locators.

  IF inlob IS NULL OR
     outlob IS NULL THEN

    RAISE_APPLICATION_ERROR(-20000,'Invalid lob locator (null value)', TRUE);

  END IF;

  BEGIN

    -- Make sure that the output is empty.
    DBMS_LOB.TRIM(outLob,0);

    -- Start at the beginning...
    npos := 0;

    -- loop until no data found.
    LOOP

      -- Set the maximum characters that can be read
      nBuffSize := 32000;

      -- Attempt to read as much as possible.
      DBMS_LOB.READ(inlob,nbuffSize,nPos +1, vcBuff);

      -- Set the current position in the clob to the characters read.
      nPos := nPos + nBuffSize;

      -- Turn it into raw in the buffer.
      rBuff := UTL_RAW.CAST_TO_RAW(vcBuff);

      -- Get the size of the raw buffer
      nBuffSize := UTL_RAW.LENGTH(rBuff);

      -- Write the contents of the raw buffer out to the BLOB.
      DBMS_LOB.WRITEAPPEND(outLob, nBuffSize, rBuff);

    END LOOP;

    EXCEPTION

      WHEN NO_DATA_FOUND THEN -- End of lob

         NULL;

  END;

  EXCEPTION

    WHEN OTHERS THEN

        RAISE;

END prClobToBlob;
/

/*
 * Convert the XHB_EMAIL table.
 * - Make the temporary table,
 * - Alter the table columns.
 * - Replace the trigger.
 * - Do the conversion
 * - Tidy up.
 */

CREATE TABLE xhb_email_tmp AS SELECT mail_id, mime_body FROM xhb_email;
ALTER TABLE xhb_email DROP COLUMN mime_body;
ALTER TABLE xhb_email ADD (mime_body BLOB);

DROP TABLE AUD_EMAIL;
CREATE TABLE AUD_EMAIL TABLESPACE AUDITD AS SELECT * FROM XHB_EMAIL WHERE 1 < 0;
ALTER TABLE AUD_EMAIL ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

CREATE OR REPLACE TRIGGER XHB_EMAIL_BUR_TR
    BEFORE UPDATE OR DELETE
    ON XHB_EMAIL
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
    IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_EMAIL') = 1) THEN
    
        INSERT INTO AUD_EMAIL
        VALUES (:old.MAIL_ID,
                :old.RECIPIENTS,
                :old.CCRECIPIENTS,
                :old.BCCRECIPIENTS,
                :old.SUBJECT,
                :old.SENDER,
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
                :old.MIME_BODY,
                l_trig_event);

    END IF;

END;
/

DECLARE

  -- Cursor iterating over the contents of the xhb_email table.
  CURSOR c_email_convert IS
    SELECT  mail_id,
            mime_body
    FROM xhb_email
    FOR UPDATE;
        
  cl_input CLOB;

BEGIN

  FOR rec IN c_email_convert LOOP

    -- If the row has a null mime_body create a temporary one.
    IF rec.mime_body IS NULL THEN

      DBMS_LOB.CREATETEMPORARY(rec.mime_body,true,DBMS_LOB.SESSION);

    END IF;

    -- Get the CLOB version of mime_body for conversion into the BLOB.
    SELECT  mime_body
    INTO    cl_input
    FROM    xhb_email_tmp
    WHERE   mail_id = rec.mail_id;

    -- Do the conversion.
    prClobToBlob(cl_input, rec.mime_body);
        
    -- If we have created a temporary BLOB then write it back to the xhb_email table.
    IF DBMS_LOB.ISTEMPORARY(rec.mime_body) = 1 THEN

      UPDATE  xhb_email
      SET     mime_body = rec.mime_body
      WHERE CURRENT OF c_email_convert;
            
      DBMS_LOB.FREETEMPORARY(rec.mime_body);

    END IF;

  END LOOP;

  COMMIT;

END;
/

DROP TABLE xhb_email_tmp;
ALTER TABLE xhb_email MODIFY (mime_body NOT NULL);

/*
 * Convert the XHB_DOCUMENT_CONTROL table.
 * - Make the temporary table,
 * - Alter the table columns.
 * - Replace the trigger.
 * - Do the conversion
 * - Tidy up.
 */

CREATE TABLE xhb_document_control_tmp AS SELECT doc_control_id, formatted_document FROM xhb_document_control;
ALTER TABLE xhb_document_control DROP COLUMN formatted_document;
ALTER TABLE xhb_document_control ADD (formatted_document BLOB);

DROP TABLE AUD_DOCUMENT_CONTROL;
CREATE TABLE AUD_DOCUMENT_CONTROL TABLESPACE AUDITD AS SELECT * FROM XHB_DOCUMENT_CONTROL WHERE 1 < 0;
ALTER TABLE AUD_DOCUMENT_CONTROL ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

CREATE OR REPLACE TRIGGER XHB_DOCUMENT_CONTROL_BUR_TR
    BEFORE UPDATE OR DELETE
    ON XHB_DOCUMENT_CONTROL
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
        
        SELECT  :OLD.VERSION + 1,
                SYSDATE
        INTO    :NEW.VERSION,
                :NEW.LAST_UPDATE_DATE
        FROM    DUAL;
        
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
    IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_DOCUMENT_CONTROL') = 1) THEN
    
        INSERT INTO AUD_DOCUMENT_CONTROL
        VALUES(
            :old.doc_control_id,
            :old.status,
            :old.expiry_date,
            :old.distribution_type,
            :old.mime_type,
            :old.document_type,
            :old.last_update_date,
            :old.creation_date,
            :old.created_by,
            :old.last_updated_by,
            :old.version,
            :old.formatting_id,
            :old.COURT_ID,
            :old.DISTRIBUTED_DATE,
            :old.xml_document_id,
            :old.formatted_document,
            l_trig_event);
    
    END IF;
    
END;
/

DECLARE

  -- Cursor iterating over the contents of the xhb_document_control table.
  CURSOR c_document_control_convert IS
    SELECT  doc_control_id,
            formatted_document
    FROM    xhb_document_control
    FOR UPDATE;
  cl_input CLOB;

BEGIN

  FOR rec IN c_document_control_convert LOOP

    IF rec.formatted_document IS NULL THEN

      DBMS_LOB.CREATETEMPORARY(rec.formatted_document,true,DBMS_LOB.SESSION);

    END IF;

    SELECT  formatted_document
    INTO    cl_input
    FROM    xhb_document_control_tmp
    WHERE   doc_control_id = rec.doc_control_id;

    prClobToBlob(cl_input, rec.formatted_document);
        
    IF DBMS_LOB.ISTEMPORARY(rec.formatted_document) = 1 THEN

      UPDATE  xhb_document_control
      SET     formatted_document = rec.formatted_document
      WHERE CURRENT OF c_document_control_convert;
            
      DBMS_LOB.FREETEMPORARY(rec.formatted_document);

    END IF;

  END LOOP;

  COMMIT;

END;
/

DROP TABLE xhb_document_control_tmp;

/*
 * Convert the XHB_FORMATTING table.
 * - Make the temporary table,
 * - Alter the table columns.
 * - Replace the trigger.
 * - Do the conversion
 * - Tidy up.
 */

CREATE TABLE xhb_formatting_tmp AS SELECT formatting_id, formatted_document FROM xhb_formatting;
ALTER TABLE xhb_formatting DROP COLUMN formatted_document;
ALTER TABLE xhb_formatting ADD (formatted_document BLOB);

DROP TABLE AUD_FORMATTING;
CREATE TABLE AUD_FORMATTING TABLESPACE AUDITD AS SELECT * FROM XHB_FORMATTING WHERE 1 < 0;
ALTER TABLE AUD_FORMATTING ADD (INSERT_EVENT VARCHAR2(1) DEFAULT 'X' NOT NULL);

CREATE OR REPLACE TRIGGER XHB_FORMATTING_BUR_TR
    BEFORE UPDATE OR DELETE
    ON XHB_FORMATTING
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
    IF (XHB_CUSTOM_PKG.IS_AUDIT_REQUIRED('XHB_FORMATTING') = 1) THEN
        
        INSERT INTO AUD_FORMATTING
        VALUES (:old.formatting_id,
                :old.date_in,
                :old.xml_document,
                :old.format_status,
                :old.distribution_type,
                :old.mime_type,
                :old.document_type,
                :old.process_type,
                :old.last_update_date,
                :old.creation_date,
                :old.created_by,
                :old.last_updated_by,
                :old.version,
                :old.COURT_ID,
                :old.formatted_document,
                l_trig_event);
    END IF;

END;
/

DECLARE

  CURSOR c_formatting_convert IS
    SELECT  formatting_id,
            formatted_document
    FROM    xhb_formatting
    FOR UPDATE;
  cl_input CLOB;

BEGIN

  FOR rec IN c_formatting_convert LOOP

    IF rec.formatted_document IS NULL THEN

      DBMS_LOB.CREATETEMPORARY(rec.formatted_document,true,DBMS_LOB.SESSION);

    END IF;

    SELECT  formatted_document
    INTO    cl_input
    FROM    xhb_formatting_tmp
    WHERE   formatting_id = rec.formatting_id;

    prClobToBlob(cl_input, rec.formatted_document);
        
    IF DBMS_LOB.ISTEMPORARY(rec.formatted_document) = 1 THEN

      UPDATE  xhb_formatting
      SET formatted_document = rec.formatted_document
      WHERE CURRENT OF c_formatting_convert;
      DBMS_LOB.FREETEMPORARY(rec.formatted_document);

    END IF;

  END LOOP;

  COMMIT;

END;
/

DROP TABLE xhb_formatting_tmp;

DROP PROCEDURE prClobToBlob;

UPDATE XHB_VERSION SET schema_version = 25, last_update_date = SYSDATE, updated_by = SYS_CONTEXT('USERENV', 'SESSION_USER');

COMMIT;
