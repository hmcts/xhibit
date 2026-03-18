/*
 * Filename:    DB_Patch_6_4_Prod.sql
 *
 * System:      Pre-Production & Production
 *
 * Date:        8th October 2004
 */


/*
 * Changes to XHB_ table definitions, indexes and foreign keys
 *
 * Additions or deletion of XHB_ tables, indexes and foreign keys
 */

ALTER TABLE MTBL_MERC_GLOBAL_LOCAL_DATA ADD (CHA_OBS_IND VARCHAR2(1) DEFAULT 'N');
COMMENT ON COLUMN MTBL_MERC_GLOBAL_LOCAL_DATA.CHA_OBS_IND IS 'Obsolete indicator for the chambers table in crest';

-- Triggers will be re-compiled later...
ALTER TABLE AUD_DEFENDANT_ON_CASE MODIFY (ptiurn VARCHAR2(50));
ALTER TABLE XHB_DEFENDANT_ON_CASE MODIFY (ptiurn VARCHAR2(50));

/*
 * Changes, additions or deletion of views
 */

CREATE OR REPLACE VIEW daily_list_v AS
   SELECT SCHEDULED_HEARING.scheduled_hearing_id,
          trim(DECODE(CASE.case_type, 'U', CASE.case_title,
                                      'B', CASE.case_title,
                                      trim(nvl2(xd.surname, xd.surname || ',', NULL) || NVL2(xd.first_name, ' ' || xd.first_name, NULL) || NVL2(xd.middle_name, ' ' || xd.middle_name, NULL)))
          ) AS def_name,
          REF_HEARING_TYPE.HEARING_TYPE_DESC AS hearing_type,
          CASE.CASE_TYPE || CASE.CASE_NUMBER AS case_string,
          CASE.CASE_ID,
          COURT_SITE.short_name || NVL2(COURT_SITE.short_name, ' - ', NULL)  || COURT_ROOM.display_name AS court_room_name,
          NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time) AS not_before_time,
          SITTING.IS_FLOATING AS floating,
          HEARING_LIST.COURT_ID,
          HEARING_LIST.START_DATE,
          SCHEDULED_HEARING.SEQUENCE_NO,
          sitting_sequence_no,
          crest_court_room_no,
          court_site_code
   FROM   XHB_HEARING_LIST HEARING_LIST,
          XHB_SITTING SITTING,
          XHB_COURT_ROOM COURT_ROOM,
          XHB_COURT_SITE COURT_SITE,
          XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
          XHB_HEARING HEARING,
          XHB_CASE CASE,
          XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
          XHB_SCHED_HEARING_DEFENDANT xshd,
          XHB_DEFENDANT_ON_CASE xdoc,
          XHB_DEFENDANT xd
    WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
    AND   SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
    AND   SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
    AND   SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
    AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
    AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
    AND   HEARING.CASE_ID = CASE.CASE_ID
    AND   court_SITE.COURT_ID = HEARING_LIST.COURT_ID
    AND   REF_HEARING_TYPE.COURT_ID =HEARING_LIST.COURT_ID
    AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = xshd.scheduled_hearing_id(+)
    AND   xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
    AND   xdoc.defendant_id = xd.defendant_id(+);
show errors

CREATE OR REPLACE VIEW daily_list_with_judge_v AS
   SELECT dlv.*,
          NVL(rj.FULL_LIST_TITLE1, rj.SURNAME) AS judge_name
   FROM   daily_list_v dlv,
          XHB_REF_JUDGE rj
   WHERE  XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(dlv.SCHEDULED_HEARING_ID) = rj.REF_JUDGE_ID(+);
show errors

-- Due to alteration of xhb_defendant_on_case need to compile these...
ALTER VIEW XHB_COUNSEL_FACILITIES_SH_V COMPILE;
show errors

ALTER VIEW XHB_COUNSEL_FACILITIES_SHDID_V COMPILE;
show errors


/*
 * Changes to AUDIT tables (AUD_) as a result of any XHB_ table modifications
 *
 *     1. Rename existing audit table to _old
 *     2. Create new audit table as SELECT * FROM XHB_ table with no rows
 *     3. Add the INSERT_EVENT column to the end of the audit table
 *     4. Insert the data from the old audit table into the new audit table
 *     5. If successful, drop old audit table
 */


/*
 * Changes, additions or deletion of sequences
 */


/*
 * Changes to XHB_ table triggers as a result of any XHB_ table modifications
 */

-- Due to alteration of xhb_defendant_on_case need to compile these...
ALTER TRIGGER XHB_DEFENDANTONCASE_BIR_TR COMPILE;
show errors

ALTER TRIGGER XHB_DEFENDANTONCASE_BUR_TR COMPILE;
show errors

ALTER TRIGGER XHB_ORDER_IMP_EXP COMPILE;
show errors


/*
 * Changes, additions or deletion of packages/procedures/functions
 */

CREATE OR REPLACE PACKAGE xhb_view_schedule_pkg AS
   -- deprecated - see comment below...
    PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                           court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
                           start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
                           court_room_id_in IN  XHB_SITTING.court_room_id%TYPE);


   -- deprecated - see comment below...
    PROCEDURE get_daily_list(results_out   OUT SYS_REFCURSOR,
                             court_id_in   IN  XHB_HEARING_LIST.court_id%TYPE,
                             start_date_in IN  XHB_HEARING_LIST.start_date%TYPE);

    -- The below functions are considerably more efficient versions of the above two
    -- generic procedures.  These are currently only used by the thinclient for the daily list
    -- and summary by name pages (on both witness and probation servives sites).
    -- The thickclient code should be changed to also use this code, and the above procedures
    -- removed when possible.


    FUNCTION get_daily_list_with_judge(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                       p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                       RETURN SYS_REFCURSOR;


    FUNCTION get_daily_list_with_witness(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                         p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                         RETURN SYS_REFCURSOR;


    FUNCTION get_daily_list_by_defendant(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                         p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                         RETURN SYS_REFCURSOR;
END xhb_view_schedule_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_view_schedule_pkg AS
   PROCEDURE get_schedule(results_out      OUT SYS_REFCURSOR,
                          court_id_in      IN  XHB_HEARING_LIST.court_id%TYPE,
                          start_date_in    IN  XHB_HEARING_LIST.start_date%TYPE,
                          court_room_id_in IN  XHB_SITTING.court_room_id%TYPE) AS
   BEGIN
        -- would we want to validate the passed in parameters?
        -- court_id_in and start_date_in to be not null?

        OPEN results_out FOR
            SELECT
                HEARING_LIST.START_DATE,
                  SITTING.SITTING_SEQUENCE_NO, SITTING.IS_FLOATING,
                  COURT_ROOM.COURT_ROOM_ID, COURT_ROOM.COURT_ROOM_NAME, COURT_ROOM.DESCRIPTION, COURT_ROOM.CREST_COURT_ROOM_NO, COURT_ROOM.COURT_SITE_ID, COURT_ROOM.VERSION AS COURT_ROOM_VERSION, COURT_ROOM.DISPLAY_NAME,
                  SCHEDULED_HEARING.SCHEDULED_HEARING_ID, SCHEDULED_HEARING.SEQUENCE_NO, SCHEDULED_HEARING.NOT_BEFORE_TIME, SCHEDULED_HEARING.ORIGINAL_TIME, SCHEDULED_HEARING.LISTING_NOTE, SCHEDULED_HEARING.HEARING_PROGRESS, SCHEDULED_HEARING.SITTING_ID, SCHEDULED_HEARING.HEARING_ID, SCHEDULED_HEARING.MOVED_FROM, SCHEDULED_HEARING.VERSION AS SCHEDULED_HEARING_VERSION, SCHEDULED_HEARING.LINKED_SH_ID, SCHEDULED_HEARING.END_TIME, SCHEDULED_HEARING.START_TIME, SCHEDULED_HEARING.DATE_OF_HEARING, SCHEDULED_HEARING.IS_CASE_ACTIVE,
                  CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS, CASE.RECEIPT_TYPE,
                  REF_HEARING_TYPE.REF_HEARING_TYPE_ID, REF_HEARING_TYPE.HEARING_TYPE_CODE, REF_HEARING_TYPE.HEARING_TYPE_DESC, REF_HEARING_TYPE.CATEGORY, REF_HEARING_TYPE.SEQ_NO, REF_HEARING_TYPE.LIST_SEQUENCE, REF_HEARING_TYPE.VERSION AS REF_HEARING_TYPE_VERSION, REF_HEARING_TYPE.COURT_ID, REF_HEARING_TYPE.OBS_IND AS REF_HEARING_TYPE_OBS_IND,
                  DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID, DEFENDANT_ON_CASE.FINAL_DRIVING_LICENCE_STATUS, DEFENDANT_ON_CASE.PTIURN, DEFENDANT_ON_CASE.IS_JUVENILE, DEFENDANT_ON_CASE.IS_MASKED, DEFENDANT_ON_CASE.MASKED_NAME, DEFENDANT_ON_CASE.CASE_ID, DEFENDANT_ON_CASE.DEFENDANT_ID, DEFENDANT_ON_CASE.VERSION AS DEFENDANT_ON_CASE_VERSION, DEFENDANT_ON_CASE.OBS_IND AS DEFENDANT_ON_CASE_OBS_IND, DEFENDANT_ON_CASE.RESULTS_VERIFIED, DEFENDANT_ON_CASE.DEFENDANT_NUMBER, DEFENDANT_ON_CASE.DATE_OF_COMMITTAL, DEFENDANT_ON_CASE.NO_OF_TICS,
                  DEFENDANT.CREST_DEFENDANT_ID, DEFENDANT.FIRST_NAME, DEFENDANT.MIDDLE_NAME, DEFENDANT.SURNAME, DEFENDANT.INITIALS, DEFENDANT.DATE_OF_BIRTH, DEFENDANT.GENDER, DEFENDANT.LAST_CONVICTION_DATE, DEFENDANT.IS_COMPANY, DEFENDANT.VERSION AS DEFENDANT_ON_VERSION, DEFENDANT.ADDRESS_ID, DEFENDANT.COURT_ID,
                  REF_JUDGE.TITLE JUDGE_TITLE, REF_JUDGE.FIRST_NAME JUDGE_FIRST_NAME, REF_JUDGE.MIDDLE_NAME JUDGE_MIDDLE_NAME, REF_JUDGE.SURNAME JUDGE_SURNAME, REF_JUDGE.REF_JUDGE_ID JUDGE_ID, REF_JUDGE.FULL_LIST_TITLE1 JUDGE_FULL_TITLE1,
                  COURT_SITE.COURT_SITE_CODE, COURT_SITE.SHORT_NAME, COURT_SITE.CREST_COURT_ID
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
            AND   SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
            AND   SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND   SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
            AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
            AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
            AND   HEARING.CASE_ID = CASE.CASE_ID
            AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
            AND   SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
            AND   DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
            AND   XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
            AND   HEARING_LIST.COURT_ID = court_id_in
            AND   HEARING_LIST.START_DATE = start_date_in
            AND   ((court_room_id_in IS NULL) OR (SITTING.COURT_ROOM_ID = court_room_id_in))
            ORDER BY court_site_code,
                     is_floating,
                     crest_court_room_no,
                     sitting_sequence_no,
                     NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time),
                     SCHEDULED_HEARING.SEQUENCE_NO;
   END get_schedule;


   PROCEDURE get_daily_list(results_out   OUT SYS_REFCURSOR,
                            court_id_in   IN  XHB_HEARING_LIST.court_id%TYPE,
                            start_date_in IN  XHB_HEARING_LIST.start_date%TYPE) AS
   BEGIN
        OPEN results_out FOR
            SELECT
                  'Daily List' DOCUMENT_NAME, SYSDATE UNIQUEID, 'Criminal' LISTCATEGORY,
                  HEARING_LIST.LIST_ID, NVL(HEARING_LIST.LIST_TYPE,'Unspecified List TYPE') DOCUMENTTYPE, HEARING_LIST.START_DATE LIST_START_DATE, HEARING_LIST.END_DATE LIST_END_DATE, LTRIM(NVL(HEARING_LIST.STATUS,'Unspecified Status') || ' ' || NVL(HEARING_LIST.EDITION_NO,-1)) LIST_VERSION, NVL(HEARING_LIST.PRINT_REFERENCE,'Unspecified Print Reference') LIST_PRINT_REF, NVL(HEARING_LIST.PUBLISHED_TIME,'01/JAN/1900') LIST_PUBLISHED, HEARING_LIST.CREST_LIST_ID LIST_CREST_LIST_ID,
                  COURTHOUSE.COURT_ID COURTHOUSE_COURT_ID, COURTHOUSE.COURT_PREFIX COURTHOUSE_TYPE, COURTHOUSE.COURT_CODE COURTHOUSE_CODE, COURTHOUSE.COURT_NAME COURTHOUSE_NAME,
                  COURTHOUSE_ADDRESS.ADDRESS_ID COURTHOUSE_ADDRESS_ID, COURTHOUSE_ADDRESS.ADDRESS_1 COURTHOUSE_ADDRESS_1, COURTHOUSE_ADDRESS.ADDRESS_2 COURTHOUSE_ADDRESS_2, COURTHOUSE_ADDRESS.ADDRESS_3 COURTHOUSE_ADDRESS_3, COURTHOUSE_ADDRESS.ADDRESS_4 COURTHOUSE_ADDRESS_4, COURTHOUSE_ADDRESS.TOWN COURTHOUSE_TOWN, COURTHOUSE_ADDRESS.COUNTY COURTHOUSE_COUNTY, COURTHOUSE_ADDRESS.POSTCODE COURTHOUSE_POSTCODE,
                  COURTSITE.COURT_SITE_ID, COURTSITE.COURT_ID COURTSITE_COURT_ID, COURTSITE.COURT_SITE_CODE COURTSITE_CODE, COURTSITE.COURT_SITE_NAME COURTSITE_NAME, COURTSITE.SHORT_NAME, COURTSITE.CREST_COURT_ID,
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
                  CASE.CASE_ID, CASE.CASE_NUMBER, CASE.CASE_TYPE, CASE.MAG_CONVICTION_DATE, CASE.CASE_SUB_TYPE, CASE.CASE_TITLE, CASE.CASE_DESCRIPTION, CASE.LINKED_CASE_ID, CASE.BAIL_MAG_CODE, CASE.REF_COURT_ID, CASE.COURT_ID, CASE.CHARGE_IMPORT_INDICATOR, CASE.SEVERED_IND, CASE.INDICT_RESP, CASE.DATE_IND_REC, CASE.PROS_AGENCY_REFERENCE, CASE.VERSION AS CASE_VERSION, CASE.CASE_CLASS, CASE.JUDGE_REASON_FOR_APPEAL, CASE.RESULTS_VERIFIED, CASE.LENGTH_TAPE, CASE.NO_PAGE_PROS_EVIDENCE, CASE.NO_PROS_WITNESS, CASE.EST_PDH_TRIAL_LENGTH, CASE.INDICTMENT_INFO_1, CASE.INDICTMENT_INFO_2, CASE.INDICTMENT_INFO_3, CASE.INDICTMENT_INFO_4, CASE.INDICTMENT_INFO_5, CASE.INDICTMENT_INFO_6, CASE.POLICE_OFFICER_ATTENDING, CASE.CPS_CASE_WORKER, CASE.EXPORT_CHARGES, CASE.IND_CHANGE_STATUS, CASE.RECEIPT_TYPE,
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
            AND   SITTING.COURT_ROOM_ID(+) = COURT_ROOM.COURT_ROOM_ID
            AND   SCHEDULED_HEARING.SITTING_ID(+) = SITTING.SITTING_ID
            AND   SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID(+)
            AND   HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID(+)
            AND   HEARING.CASE_ID = CASE.CASE_ID(+)
            AND   SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
            AND   SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
            AND   DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
            AND   XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
            AND   SITTING.REF_JUSTICE1_ID = REF_JUSTICE1.REF_JUSTICE_ID(+)
            AND   SITTING.REF_JUSTICE2_ID = REF_JUSTICE2.REF_JUSTICE_ID(+)
            AND   SITTING.REF_JUSTICE3_ID = REF_JUSTICE3.REF_JUSTICE_ID(+)
            AND   SITTING.REF_JUSTICE4_ID = REF_JUSTICE4.REF_JUSTICE_ID(+)
            AND   CASE.REF_COURT_ID = REF_COURT.REF_COURT_ID(+)
            AND   COURT_ROOM.COURT_SITE_ID = COURTSITE.COURT_SITE_ID
            AND   COURTSITE.ADDRESS_ID = COURTSITE_ADDRESS.ADDRESS_ID(+)
            AND   HEARING_LIST.COURT_ID = COURTHOUSE.COURT_ID
            AND   COURTHOUSE.ADDRESS_ID = COURTHOUSE_ADDRESS.ADDRESS_ID(+)
            AND   HEARING_LIST.COURT_ID = court_id_in
            AND   HEARING_LIST.START_DATE = start_date_in
            ORDER BY court_site_code,
                     is_floating,
                     crest_court_room_no,
                     sitting_sequence_no,
                     NVL(scheduled_hearing.not_before_time, scheduled_hearing.original_time),
                     SCHEDULED_HEARING.SEQUENCE_NO;
   END get_daily_list;


   -- Function to acquire the daily list (see the "daily_list_with_judge_v") ordered as
   -- per requirements, and filtered by court id and date.
   FUNCTION get_daily_list_with_judge(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                      p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                      RETURN SYS_REFCURSOR
   IS
       l_return_cursor SYS_REFCURSOR;
   BEGIN
       OPEN l_return_cursor FOR
           -- daily list including the judge details...
           SELECT dlwjv.*
           FROM   DAILY_LIST_WITH_JUDGE_V dlwjv
           WHERE  dlwjv.court_id   = p_court_id_in
           AND    dlwjv.start_date = p_start_date_in
           ORDER BY court_site_code,
                    floating,
                    crest_court_room_no,
                    sitting_sequence_no,
                    not_before_time,
                    sequence_no,
                    scheduled_hearing_id;
       RETURN l_return_cursor;
   END get_daily_list_with_judge;


   -- Function to acquire the daily list (see the "daily_list_with_judge_v") with the
   -- additional information required to see if skeleton schedule exists, the schedule has been
   -- issued, and if there are any witnesses on the case. .Ordered as per requirements, and
   -- filtered by court id and date.
   FUNCTION get_daily_list_with_witness(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                        p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                        RETURN SYS_REFCURSOR
   IS
       l_return_cursor SYS_REFCURSOR;
   BEGIN
       OPEN l_return_cursor FOR
           -- daily list including witness and judge details...
           SELECT dlwjv.*,
                  DECODE(xss.deliverable, 'Y', DECODE(xsds.code, 'NOTREADY', NULL, 'Y'), 'Y') AS issued,
                  NVL2(xss.skeleton_id, 'Y', NULL) AS skeleton_schedule,
                  NVL2(xss.skeleton_id, DECODE((SELECT COUNT(1) FROM XHB_WITNESS WHERE case_id = xss.case_id AND ROWNUM = 1), 1, 'Y'), NULL) AS witnesses
           FROM   DAILY_LIST_WITH_JUDGE_V      dlwjv,
                  XHB_SKELETON_SCHEDULE        xss,
                  XHB_SKELETON_DELIVERY_STATUS xsds
           WHERE  dlwjv.case_id = xss.case_id(+)
           AND    xss.skeleton_delivery_status_id = xsds.skeleton_delivery_status_id(+)
           AND    dlwjv.court_id   = p_court_id_in
           AND    dlwjv.start_date = p_start_date_in
           ORDER BY court_site_code,
                    floating,
                    crest_court_room_no,
                    sitting_sequence_no,
                    not_before_time,
                    sequence_no,
                    scheduled_hearing_id;
       RETURN l_return_cursor;
   END get_daily_list_with_witness;


   -- Function to acquire the daily list (see the "daily_list_v") ordered by the formatted
   -- defendant name, as per requirements, and filtered by court id and date.
   FUNCTION get_daily_list_by_defendant(p_court_id_in   IN XHB_HEARING_LIST.court_id%TYPE,
                                        p_start_date_in IN XHB_HEARING_LIST.start_date%TYPE)
                                        RETURN SYS_REFCURSOR
   IS
       l_return_cursor SYS_REFCURSOR;
   BEGIN
       OPEN l_return_cursor FOR
           -- daily list ordered by defendant...
           SELECT dlwjv.*
           FROM   DAILY_LIST_V dlwjv
           WHERE  dlwjv.court_id   = p_court_id_in
           AND    dlwjv.start_date = p_start_date_in
           ORDER BY def_name;
       RETURN l_return_cursor;
   END get_daily_list_by_defendant;

END xhb_view_schedule_pkg;
/
show errors

CREATE OR REPLACE PACKAGE xhb_ref_advocate_pkg AS

  PROCEDURE xhb_comp_ref_advocate(p_court_id IN XHB_COURT.COURT_ID%TYPE);

  PROCEDURE xhb_comp_ref_advocate2(p_court_id IN XHB_COURT.COURT_ID%TYPE);

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
                                   ADV_CENTRAL_IND  VARCHAR2(1),
                   CHA_OBS_IND      VARCHAR2(1));

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
        AND    crest_advocate_id = l_advocate_record.adv_id;

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
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    COURT_ID = p_court_id
    AND    REF_LEGAL_REP_ID NOT IN (SELECT REF_LEGAL_REP_ID
                    FROM   XHB_REF_SOLICITOR
                    WHERE  NVL(OBS_IND, 'N') = 'N');

    UPDATE XHB_REF_ADVOCATE
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    REF_LEGAL_REP_ID IN (SELECT REF_LEGAL_REP_ID
                FROM   XHB_REF_LEGAL_REPRESENTATIVE
                WHERE  COURT_ID = p_court_id);

    UPDATE XHB_REF_CHAMBER
    SET    OBS_IND = 'Y'
    WHERE  TRUNC(last_update_date) != l_run_date
    AND    COURT_ID = p_court_id;

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate;

PROCEDURE xhb_comp_ref_advocate2(p_court_id IN XHB_COURT.COURT_ID%TYPE) IS

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
                                   ADV_CENTRAL_IND  VARCHAR2(1),
                   CHA_OBS_IND      VARCHAR2(1));

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
                                   VALUES (l_advocate_record.cha_obs_ind,
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
                     obs_ind = l_advocate_record.cha_obs_ind,
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
              SET    obs_ind = l_advocate_record.cha_obs_ind,
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
        AND    crest_advocate_id = l_advocate_record.adv_id;

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

    DELETE FROM mtbl_merc_global_local_data
    WHERE  court_id = p_court_id;

  END xhb_comp_ref_advocate2;
END xhb_ref_advocate_pkg;
/
show errors

CREATE OR REPLACE PACKAGE XHB_PUBLIC_DISPLAY_PKG AS
    -- Require this to be declared in the header as we are calling directly
    -- from SQL in the strored procedures
    FUNCTION convert_string(p_str_in IN VARCHAR2) RETURN xhb_number_table_typ DETERMINISTIC;
    FUNCTION get_log_entry_rowid(p_scheduled_hearing_id_in IN NUMBER,
                                 p_defendant_on_case_id_in IN NUMBER) RETURN ROWID;

    PROCEDURE GET_SUMMARY_BY_NAME (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_SUMMARY_BY_NAME_U (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_JURY_STATUS_DAILY_LIST (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_JURY_STATUS_DAILY_LIST_U (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_COURT_LIST (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_ALL_COURT_STATUS (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_ALL_COURT_STATUS_THIN (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_COURT_DETAIL (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE);
    PROCEDURE GET_PUBLIC_NOTICES (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE);
    PROCEDURE GET_ACTIVE_CASES_IN_ROOM (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        LIST_ID_IN      IN  XHB_HEARING_LIST.LIST_ID%TYPE,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
        SCHEDULED_HEARING_ID_IN IN  XHB_SCHEDULED_HEARING.SCHEDULED_HEARING_ID%TYPE);
    PROCEDURE GET_ALL_CASE_STATUS (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
    PROCEDURE GET_ALL_CASE_STATUS_U (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2);
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


    FUNCTION get_log_entry_rowid(p_scheduled_hearing_id_in IN NUMBER,
                                 p_defendant_on_case_id_in IN NUMBER) RETURN ROWID AS
        SHORT_ADJOURN CONSTANT NUMBER := 30100;
        LONG_ADJOURN  CONSTANT NUMBER := 30200;
        CASE_CLOSED   CONSTANT NUMBER := 30300;
        RESUME        CONSTANT NUMBER := 10500;

        l_first_rowid        ROWID;
        l_case_closed_rowid  ROWID;
        l_is_case_closed     BOOLEAN := false;

        CURSOR c1 IS
            SELECT ROWNUM AS row_num, row_id, event_type
            FROM   (SELECT xcle.ROWID AS row_id, xcled.event_type
                    FROM   XHB_COURT_LOG_ENTRY xcle,
                           XHB_COURT_LOG_EVENT_DESC xcled
                    WHERE  xcle.EVENT_DESC_ID = xcled.EVENT_DESC_ID
                    AND    xcled.EVENT_TYPE IN (SHORT_ADJOURN, LONG_ADJOURN, CASE_CLOSED, RESUME)
                    AND    xcle.scheduled_hearing_id = p_scheduled_hearing_id_in
                    AND    (xcle.defendant_on_case_id IS NULL OR xcle.defendant_on_case_id = p_defendant_on_case_id_in)
                    ORDER BY DATE_TIME DESC);
    BEGIN
        FOR rec IN c1 LOOP
            IF (rec.row_num = 1) THEN
                -- Resumes should be ignored at this point...
                IF (rec.event_type <> RESUME) THEN
                    l_first_rowid := rec.row_id;
                END IF;
            END IF;

            IF (rec.event_type = LONG_ADJOURN) THEN
                IF (l_is_case_closed) THEN
                    RETURN l_case_closed_rowid;
                END IF;

                RETURN rec.row_id;
            ELSIF (l_is_case_closed = FALSE AND rec.event_type = CASE_CLOSED) THEN
                l_is_case_closed    := true;
                l_case_closed_rowid := rec.row_id;
            END IF;
        END LOOP;

        -- If no long adjourn events, then return the first entry...
        RETURN l_first_rowid;
    END;


    -- This stored procedure is used to get the summary by name data
    PROCEDURE GET_SUMMARY_BY_NAME (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
                XHB_COURT_SITE COURT_SITE,
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
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
                XHB_COURT_SITE COURT_SITE,
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
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
            AND DEFENDANT_ON_CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
            ORDER BY
                DEFENDANT_SURNAME,
                DEFENDANT_FIRST_NAME,
                DEFENDANT_MIDDLE_NAME;
    END GET_SUMMARY_BY_NAME_U;
    -- This stored procedure is used to get the jury current status and daily list WITHOUT unassigned cases
    PROCEDURE GET_JURY_STATUS_DAILY_LIST (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
                SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
                SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
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
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
                SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
                XHB_COURT_SITE COURT_SITE,
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
                AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
            SELECT COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
                 XHB_COURT_SITE COURT_SITE,
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
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND COURT_ROOM.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
            ORDER BY COURT_SITE_CODE, CREST_COURT_ROOM_NO;
    END GET_ALL_COURT_STATUS;
    -- This stored procedure is used to get the all court status for thinclient Witness and Probation
    PROCEDURE GET_ALL_COURT_STATUS_THIN (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN   IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT  COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
            SELECT  COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
                XHB_COURT_SITE COURT_SITE,
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
            AND COURT_SITE.COURT_SITE_ID = COURT_ROOM.COURT_SITE_ID
            ORDER BY    COURT_SITE_CODE,
                    CREST_COURT_ROOM_NO;
    END GET_ALL_COURT_STATUS_THIN;
    -- This stored procedure is used to get the COURT DETAIL
    PROCEDURE GET_COURT_DETAIL (
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ID_IN     IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN       IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT
                COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
                COURT_ROOM.DISPLAY_NAME AS COURT_ROOM_NAME,
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
                XHB_COURT_SITE COURT_SITE,
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
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE) AS
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
        RESULTS_OUT     OUT SYS_REFCURSOR,
        LIST_ID_IN      IN  XHB_HEARING_LIST.LIST_ID%TYPE,
        COURT_ROOM_ID_IN    IN  XHB_COURT_ROOM.COURT_ROOM_ID%TYPE,
        SCHEDULED_HEARING_ID_IN IN  XHB_SCHEDULED_HEARING.SCHEDULED_HEARING_ID%TYPE) AS
    BEGIN
        OPEN RESULTS_OUT FOR
            SELECT  SH.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
            FROM    XHB_SCHEDULED_HEARING SH, XHB_SITTING S
            WHERE   SH.IS_CASE_ACTIVE='Y'
            AND S.SITTING_ID = SH.SITTING_ID
            AND S.LIST_ID = LIST_ID_IN
            AND S.COURT_ROOM_ID = COURT_ROOM_ID_IN
            AND SH.SCHEDULED_HEARING_ID != SCHEDULED_HEARING_ID_IN;
    END GET_ACTIVE_CASES_IN_ROOM;
    PROCEDURE GET_ALL_CASE_STATUS (
        RESULTS_OUT       OUT SYS_REFCURSOR,
        COURT_ID_IN       IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN     IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
SELECT main_query.*,
       COURT_LOG_ENTRY.log_entry_xml AS COURT_LOG_ENTRY,
       COURT_LOG_ENTRY.date_time     AS COURT_LOG_ENTRY_TIME,
       NVL(trim(NVL2(DEFENDANT_SURNAME, DEFENDANT_SURNAME || ',', NULL)
            || NVL2(DEFENDANT_FIRST_NAME, ' ' || DEFENDANT_FIRST_NAME, NULL)
            || NVL2(DEFENDANT_MIDDLE_NAME, ' ' || DEFENDANT_MIDDLE_NAME, NULL)), CASE_TITLE) AS ORDER_NAME
FROM (
SELECT COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
--              REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
--              REF_JUDGE.SURNAME AS JUDGE_SURNAME,
                DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
                DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME,
                DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME,
                DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
                CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
                CASE.CASE_TITLE AS CASE_TITLE,
                REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
                SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
                SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
                SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID,
                DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
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
--              XHB_REF_JUDGE REF_JUDGE,
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
--              AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND SITTING.IS_FLOATING = 0
) main_query,
  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY,
  XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
WHERE COURT_LOG_ENTRY.ROWID(+) = xhb_public_display_pkg.get_log_entry_rowid(main_query.scheduled_hearing_id, main_query.defendant_on_case_id)
AND   COURT_LOG_EVENT_DESC.EVENT_DESC_ID(+) = COURT_LOG_ENTRY.EVENT_DESC_ID
ORDER BY COURT_SITE_CODE,
            IS_FLOATING,
            CREST_COURT_ROOM_NO,
            SITTING_SEQUENCE_NO,
            SCHEDULED_HEARING_TIME_SORT,
            SCHEDULED_HEARING_SEQUENCE_NO,
            ORDER_NAME;
    END GET_ALL_CASE_STATUS;
    PROCEDURE GET_ALL_CASE_STATUS_U (
        RESULTS_OUT       OUT SYS_REFCURSOR,
        COURT_ID_IN       IN  XHB_HEARING_LIST.court_id%TYPE,
        START_DATE_IN     IN  XHB_HEARING_LIST.start_date%TYPE,
        COURT_ROOM_IDS_IN IN  VARCHAR2) AS
    BEGIN
        OPEN RESULTS_OUT FOR
SELECT main_query.*,
       COURT_LOG_ENTRY.log_entry_xml AS COURT_LOG_ENTRY,
       COURT_LOG_ENTRY.date_time     AS COURT_LOG_ENTRY_TIME,
          NVL(trim(NVL2(DEFENDANT_SURNAME, DEFENDANT_SURNAME || ',', NULL)
            || NVL2(DEFENDANT_FIRST_NAME, ' ' || DEFENDANT_FIRST_NAME, NULL)
            || NVL2(DEFENDANT_MIDDLE_NAME, ' ' || DEFENDANT_MIDDLE_NAME, NULL)), CASE_TITLE) AS ORDER_NAME
FROM (
SELECT COURT_SITE.COURT_SITE_NAME,
                COURT_SITE.SHORT_NAME,
                COURT_SITE.COURT_SITE_CODE AS COURT_SITE_CODE,
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
--              REF_JUDGE.FULL_LIST_TITLE1 AS FULL_LIST_TITLE1,
--              REF_JUDGE.SURNAME AS JUDGE_SURNAME,
                DEFENDANT.DEFENDANT_ID AS DEFENDANT_ID,
                DEFENDANT.FIRST_NAME AS DEFENDANT_FIRST_NAME,
                DEFENDANT.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME,
                DEFENDANT.SURNAME AS DEFENDANT_SURNAME,
                CASE.CASE_TYPE || CASE.CASE_NUMBER AS CASE_NUMBER,
                CASE.CASE_TITLE AS CASE_TITLE,
                REF_HEARING_TYPE.HEARING_TYPE_DESC AS HEARING_DESCRIPTION,
                SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
                SCHEDULED_HEARING.HEARING_PROGRESS AS HEARING_PROGRESS,
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
                SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID,
                DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
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
--              XHB_REF_JUDGE REF_JUDGE,
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
--              AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
) main_query,
  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY,
  XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
WHERE COURT_LOG_ENTRY.ROWID(+) = xhb_public_display_pkg.get_log_entry_rowid(main_query.scheduled_hearing_id, main_query.defendant_on_case_id)
AND   COURT_LOG_EVENT_DESC.EVENT_DESC_ID(+) = COURT_LOG_ENTRY.EVENT_DESC_ID
ORDER BY COURT_SITE_CODE,
            IS_FLOATING,
            CREST_COURT_ROOM_NO,
            SITTING_SEQUENCE_NO,
            SCHEDULED_HEARING_TIME_SORT,
            SCHEDULED_HEARING_SEQUENCE_NO,
            ORDER_NAME;
    END GET_ALL_CASE_STATUS_U;
END XHB_PUBLIC_DISPLAY_PKG;
/
show errors

CREATE OR REPLACE PACKAGE xhb_terminal_pkg AS
    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE) RETURN SYS_REFCURSOR;
    FUNCTION get_courts RETURN SYS_REFCURSOR;
    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE) RETURN SYS_REFCURSOR;
END xhb_terminal_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_terminal_pkg AS
    FUNCTION get_terminals(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_SITE xcs
            WHERE  xt.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            UNION
            SELECT xt.TERMINAL_NAME,
                   xt.TERMINAL_ID,
                   xcr.COURT_ROOM_NAME AS COURT_ROOM_NAME,
                   xcs.COURT_SITE_NAME
            FROM   XHB_TERMINAL xt, XHB_COURT_ROOM xcr, XHB_COURT_SITE xcs
            WHERE  xt.COURT_ROOM_ID = xcr.COURT_ROOM_ID
            AND    xcr.COURT_SITE_ID = xcs.COURT_SITE_ID
            AND    xcs.COURT_ID = p_court_id_in
            ORDER BY TERMINAL_NAME;

        RETURN v_return_cursor;
    END get_terminals;


    FUNCTION get_courts RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT COURT_NAME,
                   COURT_ID,
                   CREST_COURT_ID
            FROM   XHB_COURT
            ORDER BY COURT_NAME;

        RETURN v_return_cursor;
    END get_courts;


    FUNCTION get_terminal_by_primary_key(p_terminal_id_in IN XHB_TERMINAL.terminal_id%TYPE)
    RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_return_cursor FOR
            SELECT TERMINAL_NAME,
                   TERMINAL_ID,
                   NULL AS COURT_ROOM_NAME,
                   NULL AS COURT_SITE_NAME
            FROM   XHB_TERMINAL
            WHERE  TERMINAL_ID = p_terminal_id_in;

        RETURN v_return_cursor;
    END get_terminal_by_primary_key;
END xhb_terminal_pkg;
/
show errors

CREATE OR REPLACE PACKAGE xhb_orders_pkg AS

FUNCTION get_ref_courts_by_court_Id (
                COURT_ID_IN IN XHB_REF_COURT.COURT_ID%TYPE) RETURN SYS_REFCURSOR;
        
END xhb_orders_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY xhb_orders_pkg AS    
    FUNCTION get_ref_courts_by_court_Id (
                COURT_ID_IN IN XHB_REF_COURT.COURT_ID%TYPE) RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;                
                
    BEGIN
        OPEN v_return_cursor FOR
            SELECT
                REF_COURT_ID,
                COURT_FULL_NAME,
                COURT_SHORT_NAME,
                NAME_PREFIX,
                COURT_TYPE,
                CREST_CODE,
                REF_COURT.OBS_IND AS REF_COURT_OBS_IND,
                IS_PSD,
                DX_REF,
                REF_COURT.LAST_UPDATE_DATE AS COURT_LAST_UPDATE_DATE,
                REF_COURT.CREATION_DATE AS COURT_CREATION_DATE,
                REF_COURT.CREATED_BY AS COURT_CREATED_BY,
                REF_COURT.LAST_UPDATED_BY AS COURT_LAST_UPDATED_BY,
                REF_COURT.VERSION AS COURT_VERSION,
                REF_COURT.ADDRESS_ID AS COURT_ADDRESS_ID,
                COURT_ID,
                ADDRESS.ADDRESS_ID,
                ADDRESS_1,
                ADDRESS_2,
                ADDRESS_3,
                ADDRESS_4,
                TOWN,
                COUNTY,
                POSTCODE,
                COUNTRY,
                ADDRESS.LAST_UPDATE_DATE AS ADDRESS_LAST_UPDATE_DATE,
                ADDRESS.CREATION_DATE AS ADDRESS_CREATION_DATE,
                ADDRESS.CREATED_BY AS ADDRESS_CREATED_BY,
                ADDRESS.LAST_UPDATED_BY AS ADDRESS_LAST_UPDATED_BY,
                ADDRESS.VERSION AS ADDRESS_VERSION
            FROM  XHB_REF_COURT REF_COURT, XHB_ADDRESS ADDRESS
            WHERE REF_COURT.COURT_ID = COURT_ID_IN
            AND (REF_COURT.OBS_IND IS NULL OR REF_COURT.OBS_IND = 'N')
            AND REF_COURT.ADDRESS_ID = ADDRESS.ADDRESS_ID(+);
            RETURN v_return_cursor;
    END get_ref_courts_by_court_Id;
END xhb_orders_pkg;
/
show errors

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



    --
    -- NEW FUNCTION FOR CR51...
    --
    FUNCTION get_court_room_list(p_court_id_in      IN NUMBER,
                                 p_start_date_in    IN  DATE,
                                 p_court_room_id_in IN NUMBER) RETURN SYS_REFCURSOR;
END counselfacilities;
/
show errors

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


    --
    -- NEW FUNCTION FOR CR51...
    --
    FUNCTION get_court_room_list(p_court_id_in      IN NUMBER,
                                 p_start_date_in    IN DATE,
                                 p_court_room_id_in IN NUMBER) RETURN SYS_REFCURSOR IS
        v_return_cursor SYS_REFCURSOR;
    BEGIN
        -- Really do not like this query, will look into further for the 7 release...
        -- Do not like, however, it is still considerably more efficient than the previous
        -- version (see get_counsel_sign_in), and could be improved and integrated further
        -- with other areas with the use of more common views...

        OPEN v_return_cursor FOR
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    xs.ref_judge_id = xrj.ref_judge_id(+)
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    xshd.sched_hear_def_id = xslr.sched_hear_def_id(+)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
UNION
            SELECT -- court data...
                   xcourt.court_type,
                   xcourt.court_name,
                   xcourt.short_name AS court_short_name,
                   xhl.court_id,
                   xhl.start_date,
                   SYSDATE AS request_date,
                   -- sitting data...
                   xs.sitting_id,
                   xs.sitting_time,
                   xs.sitting_sequence_no,
                   xs.is_floating AS floating,
                   xcs.short_name AS court_site_short_name,
                   xcs.court_site_code,
                   xcr.display_name AS court_site_display_name,
                   xcr.crest_court_room_no,
                   -- Sitting judge...
                   NVL(xrj.full_list_title1, xrj.surname) AS judge_name,
                   -- scheduled hearing data...
                   xsh.scheduled_hearing_id,
                   xsh.sequence_no,
                   NVL(xsh.not_before_time, xsh.original_time) AS not_before_time,
                   xrht.hearing_type_desc AS hearing_type,
                   xc.case_id,
                   xc.case_type,
                   xc.case_number,
                   xc.case_title,
                   -- defendant details...
                   xd.defendant_id  AS def_id,
                   xd.first_name    AS def_first_name,
                   xd.middle_name   AS def_middle_name,
                   xd.surname       AS def_surname,
                   xdoc.is_masked   AS def_is_masked,
                   xdoc.masked_name AS def_masked_name,
                   -- Court staff...
                   xss.sh_staff_id AS staff_id,
                   xss.staff_role,
                   xss.staff_name,
                   -- Defence advocates...
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.ref_legal_rep_id END AS def_advocate_id,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.title END            AS def_advocate_title,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.first_name END       AS def_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.middle_name END      AS def_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('D') THEN xrlr.surname END          AS def_advocate_surname,
                   -- Prosecution and responent advocates and objectors...
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.ref_legal_rep_id END AS pros_advocate_id,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.title END            AS pros_advocate_title,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.first_name END       AS pros_advocate_first_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.middle_name END      AS pros_advocate_middle_name,
                   CASE WHEN xslr.legal_role IN ('R', 'O', 'P') THEN xrlr.surname END          AS pros_advocate_surname
            FROM   XHB_HEARING_LIST xhl,
                   XHB_SITTING xs,
                   XHB_COURT xcourt,
                   XHB_COURT_ROOM xcr,
                   XHB_COURT_SITE xcs,
                   XHB_SCHEDULED_HEARING xsh,
                   XHB_HEARING xh,
                   XHB_CASE xc,
                   XHB_REF_HEARING_TYPE xrht,
                   XHB_SCHED_HEARING_DEFENDANT xshd,
                   XHB_DEFENDANT_ON_CASE xdoc,
                   XHB_DEFENDANT xd,
                   XHB_SCHED_HEARING_ATTENDEE xsha,
                   XHB_SH_STAFF xss,
                   XHB_REF_JUDGE xrj,
                   XHB_SH_LEG_REP xslr,
                   XHB_REF_LEGAL_REPRESENTATIVE xrlr
            WHERE  xhl.list_id = xs.list_id
            AND    xs.court_room_id = xcr.court_room_id
            AND    xs.court_site_id = xcs.court_site_id
            AND    xsh.sitting_id = xs.sitting_id
            AND    xsh.hearing_id = xh.hearing_id
            AND    xh.ref_hearing_type_id = xrht.ref_hearing_type_id
            AND    xh.case_id = xc.case_id
            AND    xcs.court_id = xhl.court_id
            AND    xrht.court_id = xhl.court_id
            AND    xrht.court_id = xcourt.court_id
            AND    xsh.scheduled_hearing_id = xshd.scheduled_hearing_id(+)
            AND    xshd.defendant_on_case_id = xdoc.defendant_on_case_id(+)
            AND    xdoc.defendant_id = xd.defendant_id(+)
            AND    xsh.scheduled_hearing_id = xsha.scheduled_hearing_id(+)
            AND    xsha.sh_staff_id = xss.sh_staff_id(+)
            AND    xs.ref_judge_id = xrj.ref_judge_id(+)
            AND    xslr.ref_legal_rep_id = xrlr.ref_legal_rep_id(+)
            AND    (xsh.scheduled_hearing_id = xslr.scheduled_hearing_id(+) AND xslr.sched_hear_def_id IS NULL)
            AND    xhl.court_id = xcourt.court_id
            AND    xhl.start_date  = p_start_date_in
            AND    xhl.court_id    = p_court_id_in
            AND    xcs.court_id    = p_court_id_in
            AND    (p_court_room_id_in IS NULL OR (xcr.court_room_id = p_court_room_id_in AND xs.is_floating = 0))
            ORDER BY court_site_code, floating, crest_court_room_no,
                   sitting_sequence_no, not_before_time, sequence_no;

        RETURN v_return_cursor;
    END get_court_room_list;
END counselfacilities;
/
show errors

-- Due to alteration of xhb_defendant_on_case need to compile these...
ALTER PACKAGE xhb_psr_request_pkg COMPILE;
show errors

/*
 * Changes, additions or deletion of standing data
 */

--
-- Anonymous block to reset a couple of sequences...
--
DECLARE
    PROCEDURE reset_sequence(p_table_name VARCHAR2, p_sequence_name VARCHAR2) IS
        v_pk_col_name           USER_TAB_COLUMNS.column_name%TYPE;
        v_sequence_last_number  NUMBER;
        v_pk_last_number        NUMBER;
        v_sequence_increment_by NUMBER;

        v_temp_increment_by NUMBER;
        v_dummy_id          NUMBER;
    BEGIN
        -- primary key generated from a sequence should always be column 1...
        SELECT column_name
        INTO   v_pk_col_name
        FROM   user_tab_columns
        WHERE  table_name = p_table_name
        AND    column_id = 1; 

        -- Get the highest primary key stored in the table...
        EXECUTE IMMEDIATE 'SELECT MAX(' || v_pk_col_name || ') FROM ' || p_table_name
        INTO v_pk_last_number;

        -- Get the values for the sequence...
        SELECT last_number, increment_by
        INTO   v_sequence_last_number, v_sequence_increment_by
        FROM   user_sequences
        WHERE  sequence_name = p_sequence_name;

        -- only update those with sequence number low than last PK value, as there
        -- may be audited values that could cause confusion...
        IF (v_sequence_last_number < v_pk_last_number) THEN
           v_temp_increment_by := (v_pk_last_number - v_sequence_last_number) + 1;
           DBMS_OUTPUT.put_line('Altering SEQUENCE ' || p_sequence_name || ' BY ' || v_temp_increment_by);

           EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_sequence_name || ' INCREMENT BY ' || v_temp_increment_by;
           EXECUTE IMMEDIATE 'SELECT ' || p_sequence_name || '.NEXTVAL FROM dual' INTO v_dummy_id;
           -- reset the increment by back to its previous value...
           EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_sequence_name || ' INCREMENT BY ' || v_sequence_increment_by;
        END IF;

        DBMS_OUTPUT.put_line(p_table_name || '::SEQUENCE last = ' || v_sequence_last_number
                || '; pk last = ' || v_pk_last_number);
    END;
BEGIN
    reset_sequence('XHB_DISPLAY_DOCUMENT', 'XHB_DISPLAY_DOCUMENT_SEQ');
    reset_sequence('XHB_ROTATION_SET_DD', 'XHB_ROTATION_SET_DD_SEQ');
END;
/

DECLARE
    var_dd_id NUMBER;
    var_rs_id NUMBER;
    var_execute NUMBER; -- 0 execute, 1 do not execute
BEGIN
    SELECT NVL(MAX(1), 0)
    INTO   var_execute
    FROM   XHB_DISPLAY_DOCUMENT
    WHERE  description_code = 'AllCaseStatus'; 

    IF (var_execute = 0) THEN
        DBMS_OUTPUT.put_line('Executing insert scripts...');

        SELECT XHB_DISPLAY_DOCUMENT_SEQ.NEXTVAL
        INTO var_dd_id
        FROM DUAL;

        INSERT INTO XHB_DISPLAY_DOCUMENT (DISPLAY_DOCUMENT_ID, DESCRIPTION_CODE, DEFAULT_PAGE_DELAY, MULTIPLE_COURT_YN)
        VALUES (var_dd_id, 'AllCaseStatus', 10, 'Y');

        FOR r_rotationset IN (SELECT ROTATION_SET_ID FROM XHB_ROTATION_SETS WHERE UPPER(DESCRIPTION)='ALL LISTS') LOOP
            var_rs_id := r_rotationset.rotation_set_id;

            INSERT INTO XHB_ROTATION_SET_DD (ROTATION_SET_ID, DISPLAY_DOCUMENT_ID, PAGE_DELAY, ORDERING)
            VALUES (var_rs_id, var_dd_id, 10, 7);
        END LOOP;
    ELSE
        DBMS_OUTPUT.put_line('Not executing scripts, as values already exist...');
    END IF;
END;
/

INSERT INTO XHB_COURT_LOG_EVENT_DESC ( EVENT_DESC_ID, EVENT_TYPE, EVENT_DESCRIPTION, PUBLIC_DISPLAY, CREATED_BY, LAST_UPDATED_BY, LINKED_CASE_TEXT, FLAGGED_EVENT, EDITABLE, SEND_TO_MERCATOR, UPDATE_LINKED_CASES, PUBLISH_TO_SUBSCRIBERS, CLEAR_PUBLIC_DISPLAYS, E_INFORM, PUBLIC_NOTICE, SHORT_DESCRIPTION)
VALUES ( 154, 40736, 'Capture_Verdict', 0, 'Xhibit', 'Xhibit', 'LC_TEXT_', 0, 1, 0, 1, 1, 0, 0, 0, 'Guilty');

/*
 * Updating of table XHB_VERSION
 */

DELETE FROM XHB_VERSION;

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVACLIENT', '6.4', sysdate, 'RELEASE', 'Java Client Application', 1); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'JAVASERVER', '6.4', sysdate, 'RELEASE', 'Java Server Component', 2); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'XHIBIT', '6.4', sysdate , 'RELEASE', 'Database', 3); 

INSERT INTO XHB_VERSION ( SCHEMA_NAME, SCHEMA_VERSION, LAST_UPDATE_DATE, UPDATED_BY, DISPLAY_NAME, DISPLAY_SEQ ) 
VALUES ( 'MERCATOR', '6.4', sysdate, 'RELEASE', 'Mercator', 4); 

COMMIT;
