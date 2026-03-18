CREATE TABLE EXT_XHB_CLOB TABLESPACE "XHIBIT_BASELINE" AS
SELECT CLOB_ID,
       CLOB_DATA
FROM   XHB_CLOB
WHERE  CLOB_ID IN (SELECT XML_DOCUMENT_CLOB_ID FROM XHB_XML_DOCUMENT) 
AND    1=0;
        
ALTER TABLE EXT_XHB_XML_DOCUMENT
 DROP (XML_DOCUMENT);

ALTER TABLE EXT_XHB_XML_DOCUMENT
  ADD XML_DOCUMENT_CLOB_ID NUMBER(10) NOT NULL;

ALTER TABLE EXT_XHB_PLEA
  ADD ALT_UNCODED_OFFENCE_DESC VARCHAR2(255);

ALTER TABLE EXT_XHB_VERDICT
  ADD ALT_UNCODED_OFFENCE_DESC VARCHAR2(255)
  ADD DISPOSAL2_ID NUMBER(8) 
  ADD DEFENDANT_ON_CASE_ID NUMBER(8) ;
  
INSERT INTO XHB_TRAINING_REF_DATA 
SELECT 'DAILY_LIST_DIST', COURT_ID, VALUE1, 'Daily_List_Dist.xml'
FROM   XHB_TRAINING_REF_DATA TR1
WHERE  PARAMETER = 'TOMORROWS_LIST'
AND    'DAILY_LIST_DIST' NOT IN (SELECT TR2.PARAMETER FROM XHB_TRAINING_REF_DATA TR2 WHERE TR2.COURT_ID = TR1.COURT_ID);

INSERT INTO XHB_TRAINING_REF_DATA 
SELECT 'WARNED_LIST_DIST', COURT_ID, VALUE1, 'Warned_List_Dist.xml'
FROM   XHB_TRAINING_REF_DATA TR1
WHERE  PARAMETER = 'WARNED_LIST'
AND    'WARNED_LIST_DIST' NOT IN (SELECT TR2.PARAMETER FROM XHB_TRAINING_REF_DATA TR2 WHERE TR2.COURT_ID = TR1.COURT_ID);

INSERT INTO XHB_REF_OFFENCE (OFFENCE_CODE, OFFENCE_DESC, COURT_ID)
SELECT 'ZZ99999', 'Uncoded Offence', COURT_ID
FROM   XHB_COURT C
WHERE  'ZZ99999' NOT IN (SELECT R.OFFENCE_CODE FROM XHB_REF_OFFENCE R WHERE R.COURT_ID = C.COURT_ID);

-- PR 56716  
UPDATE EXT_XHB_HEARING
SET    HEARING_TYPE_CODE = 'APL'
WHERE  HEARING_ID IN 
      (SELECT H.HEARING_ID 
       FROM EXT_XHB_CASE C, EXT_XHB_HEARING H 
       WHERE C.CASE_TYPE='A'
       AND H.HEARING_TYPE_CODE='TRL' 
       AND C.CASE_ID = H.CASE_ID );

UPDATE EXT_XHB_HEARING
SET    HEARING_TYPE_CODE = 'CSE'
WHERE  HEARING_ID IN 
      (SELECT H.HEARING_ID 
       FROM EXT_XHB_CASE C, EXT_XHB_HEARING H 
       WHERE C.CASE_TYPE='S'
       AND H.HEARING_TYPE_CODE='SEN' 
       AND C.CASE_ID = H.CASE_ID );
	   
	   
UPDATE XHB_TRAINING_REF_DATA
SET    VALUE2 = 'Warned_List.xml'
WHERE  PARAMETER = 'WARNED_LIST';


-- PACKAGES
CREATE OR REPLACE PACKAGE training_utils_pkg AS
    PROCEDURE get_court_details(p_results_out OUT SYS_REFCURSOR,
                                p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE request_xhibit_restore(p_results_out OUT SYS_REFCURSOR,
                                     p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE request_mld_restore(p_results_out OUT SYS_REFCURSOR,
                                  p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    PROCEDURE get_restore_states(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE restore_xhibit(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE restore_mld_data(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE insert_mld_data(p_court_id IN XHB_COURT.COURT_ID%TYPE);

    PROCEDURE check_xhibit_restore_state(p_results_out OUT SYS_REFCURSOR,
                                         p_court_id    IN  XHB_COURT.COURT_ID%TYPE);

    FUNCTION get_ref_justice_id(p_court_id_in         IN XHB_REF_JUSTICE.court_id%TYPE,
                                p_crest_justice_id_in IN XHB_REF_JUSTICE.crest_justice_id%TYPE)
                                RETURN XHB_REF_JUSTICE.ref_justice_id%TYPE;

    FUNCTION get_ref_judge_id(p_court_id_in       IN XHB_REF_JUDGE.court_id%TYPE,
                              p_crest_judge_id_in IN XHB_REF_JUDGE.crest_judge_id%TYPE)
                              RETURN XHB_REF_JUDGE.ref_judge_id%TYPE;

    FUNCTION get_court_site_id(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
                               RETURN XHB_COURT_SITE.court_site_id%TYPE;

    FUNCTION get_court_room_id(p_court_id_in            IN XHB_COURT_SITE.court_id%TYPE,
                               p_crest_court_room_no_in IN XHB_COURT_ROOM.crest_court_room_no%TYPE)
                               RETURN XHB_COURT_ROOM.court_room_id%TYPE;

    FUNCTION get_ref_court_id(p_court_id_in         IN XHB_REF_COURT.court_id%TYPE,
                              p_court_short_name_in IN XHB_REF_COURT.court_short_name%TYPE)
                              RETURN XHB_REF_COURT.ref_court_id%TYPE;

    FUNCTION get_ref_hearing_type_id(p_court_id_in          IN XHB_REF_HEARING_TYPE.court_id%TYPE,
                                     p_hearing_type_code_in IN XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                                     p_case_type_in         IN XHB_CASE.case_type%TYPE)
                                     RETURN XHB_REF_HEARING_TYPE.ref_hearing_type_id%TYPE;

    FUNCTION get_ref_system_code_id(p_court_id_in  IN XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                    p_code_in      IN XHB_REF_SYSTEM_CODE.code%TYPE,
                                    p_code_type_in IN XHB_REF_SYSTEM_CODE.code_type%TYPE)
                                    RETURN XHB_REF_SYSTEM_CODE.ref_system_code_id%TYPE;

    FUNCTION get_ref_offence_id(p_court_id_in     IN XHB_REF_OFFENCE.court_id%TYPE,
                                p_offence_code_in IN XHB_REF_OFFENCE.offence_code%TYPE)
                                RETURN XHB_REF_OFFENCE.ref_offence_id%TYPE;

    FUNCTION get_ref_prosecutor_agency_id(p_court_id_in         IN XHB_REF_PROSECUTOR_AGENCY.court_id%TYPE,
                                          p_crest_opposer_id_in IN XHB_REF_PROSECUTOR_AGENCY.crest_opposer_id%TYPE)
                                          RETURN XHB_REF_PROSECUTOR_AGENCY.ref_prosecutor_agency_id%TYPE;

    FUNCTION get_ref_app_result_id(p_court_id_in        IN XHB_REF_APP_RESULT.court_id%TYPE,
                                   p_app_result_code_in IN XHB_REF_APP_RESULT.app_result_code%TYPE)
                                   RETURN XHB_REF_APP_RESULT.ref_app_result_id%TYPE;


    FUNCTION get_ref_disposal_type_id(p_court_id_in         IN XHB_REF_DISPOSAL_TYPE.court_id%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_TYPE.template_version%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_TYPE.disposal_code%TYPE,
                                      p_menu_group_in       IN XHB_REF_DISPOSAL_TYPE.menu_group%TYPE)
                                      RETURN XHB_REF_DISPOSAL_TYPE.ref_disposal_type_id%TYPE;

    FUNCTION get_ref_disposal_line_id(p_court_id_in         IN XHB_REF_DISPOSAL_LINE.court_id%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_LINE.disposal_code%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_LINE.template_version%TYPE,
                                      p_dil_seq_no_in       IN XHB_REF_DISPOSAL_LINE.dil_seq_no%TYPE)
                                      RETURN XHB_REF_DISPOSAL_LINE.ref_disposal_line_id%TYPE;
                                   
    FUNCTION get_case_type(p_case_id_in IN EXT_XHB_CASE.case_id%TYPE)
                           RETURN EXT_XHB_CASE.case_type%TYPE;

    FUNCTION lookup_xhb_hearing_list_id(p_list_id_in IN XHB_HEARING_LIST.list_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sitting_id(p_sitting_id_in IN XHB_SITTING.sitting_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_case_id(p_case_id_in IN XHB_CASE.case_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_hearing_id(p_hearing_id_in IN XHB_HEARING.hearing_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sched_hearing_id(p_scheduled_hearing_id_in IN XHB_SCHEDULED_HEARING.scheduled_hearing_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_address_id(p_address_id_in IN XHB_ADDRESS.address_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_defendant_id(p_defendant_id_in IN XHB_DEFENDANT.defendant_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_def_on_case_id(p_defendant_on_case_id_in IN XHB_DEFENDANT_ON_CASE.defendant_on_case_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_charge_id(p_charge_id_in IN XHB_CHARGE.charge_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_defendant_charge_id(p_defendant_charge_id_in IN XHB_DEFENDANT_CHARGE.defendant_charge_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_offence_id(p_offence_id_in IN XHB_OFFENCE.offence_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_def_on_offence_id(p_defendant_on_offence_id_in IN XHB_DEFENDANT_ON_OFFENCE.offence_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_sched_def_id(p_sched_hearing_def_id_in IN XHB_SCHED_HEARING_DEFENDANT.sched_hear_def_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_breach_id(p_breach_id_in IN XHB_BREACH.breach_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_disposal2_id(p_disposal2_id_in IN XHB_DISPOSAL2.disposal2_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_clob_id(p_clob_id_in IN XHB_CLOB.clob_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_xml_document_id(p_xml_document_id_in IN XHB_XML_DOCUMENT.xml_document_id%TYPE) RETURN NUMBER;
    FUNCTION lookup_xhb_wll_control_id(p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE) RETURN NUMBER;

    PROCEDURE populate_tables(p_court_id_in IN XHB_COURT.court_id%TYPE);
END training_utils_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY training_utils_pkg AS
    TYPE pk_array_type IS TABLE OF NUMBER(8) INDEX BY BINARY_INTEGER;
    TYPE pk_array_array_type IS TABLE OF pk_array_type INDEX BY VARCHAR2(30);

    g_array_lookup_cache_t pk_array_array_type;

    FUNCTION get_id_lookup(p_cache_name_in    IN VARCHAR2,
                           p_sequence_name_in IN VARCHAR2,
                           p_id_in            IN NUMBER) RETURN NUMBER IS
        l_seq_next_val NUMBER;
    BEGIN
        IF (p_id_in IS NULL) THEN
            RETURN NULL;
        END IF;

        RETURN g_array_lookup_cache_t(p_cache_name_in)(p_id_in);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- If performance is significantly impacted, either use DBMS_SQL or
            -- extract this common code out to use the relevant sequence...
            EXECUTE IMMEDIATE 'SELECT ' || p_sequence_name_in || '.nextVal FROM dual'
            INTO l_seq_next_val;

            g_array_lookup_cache_t(p_cache_name_in)(p_id_in) := l_seq_next_val;
            RETURN l_seq_next_val;
    END get_id_lookup;


/*
 *
 * GET_COURT_DETAILS
 *
 */

  PROCEDURE get_court_details(p_results_out OUT SYS_REFCURSOR,
                              p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    OPEN p_results_out FOR

      SELECT court_id,
             court_name,
             display_name
      FROM   XHB_COURT
      WHERE ((p_court_id is null) or (court_id = p_court_id));

  END get_court_details;

/*
 *
 * REQUEST_XHIBIT_RESTORE
 *
 */

  PROCEDURE request_xhibit_restore(p_results_out OUT SYS_REFCURSOR,
                                   p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

    l_rec_count      NUMBER;
    l_complete_count NUMBER;

  BEGIN

    /*
     * Check for any purging taking place (need a Load complete combination to
     * continue)
     */

    SELECT count(*)
    INTO   l_rec_count
    FROM   xhb_training_status
    WHERE  court_id = p_court_id;

    SELECT count(*)
    INTO   l_complete_count
    FROM   xhb_training_status
    WHERE  court_id = p_court_id
    AND  ((operation_code = 'L' AND status_code = 'C')
    OR    (operation_code = 'P' AND status_code = 'F'));

    IF l_rec_count = 0 OR
      (l_rec_count > 0 AND
       l_complete_count > 0) THEN

      /*
       * Either the xhb_training_status table is empty (only very first time or just started
       * a purge) or there is a complete load record
       */

      DELETE FROM xhb_training_status
      WHERE  court_id = p_court_id;

      /*
       * Insert a record into the xhb_training_status tables to request the purging
       * of the XHIBIT data.  This returns immediately but creates a job that will
       * be run asynchronously (via a trigger on the xhb_training_status table).
       */

      INSERT INTO XHB_TRAINING_STATUS VALUES (p_court_id, 'XHIBIT','P','R',SYSDATE,NULL);
      --COMMIT;

      /*
       * Create an empty record set for return value (temporary until data required)
       */

      OPEN p_results_out FOR

        SELECT *
        FROM   xhb_training_status
        WHERE  court_id = -1;

    ELSE

      /*
       * Create an empty record set for return value (temporary until data required)
       */

      OPEN p_results_out FOR

        SELECT *
        FROM   xhb_training_status
        WHERE  court_id = -1;

      RAISE_APPLICATION_ERROR (-20007, 'A purge/load cycle is already in progress');

    END IF;

  END request_xhibit_restore;

/*
 *
 * REQUEST_MLD_RESTORE
 *
 */

  PROCEDURE request_mld_restore(p_results_out OUT SYS_REFCURSOR,
                                p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

    l_rec_count      NUMBER;
    l_complete_count NUMBER;

  BEGIN

    /*
     * Check for any purging taking place (need a Load complete combination to
     * continue)
     */

    SELECT count(*)
    INTO   l_rec_count
    FROM   xhb_training_status
    WHERE  court_id = p_court_id;

    SELECT count(*)
    INTO   l_complete_count
    FROM   xhb_training_status
    WHERE  court_id = p_court_id
    AND  ((operation_code = 'L' AND status_code = 'C')
    OR    (operation_code = 'P' AND status_code = 'F'));

    IF l_rec_count = 0 OR
      (l_rec_count > 0 AND
       l_complete_count > 0) THEN

      /*
       * Either the xhb_training_status table is empty (only very first time or just started
       * a purge) or there is a complete load record
       */

      DELETE FROM xhb_training_status
      WHERE  court_id = p_court_id;

      /*
       * Insert a record into the xhb_training_status tables to request the purging
       * of the MLD data.  This returns immediately but creates a job that will
       * be run asynchronously (via a trigger on the xhb_training_status table).
       */

      INSERT INTO XHB_TRAINING_STATUS VALUES (p_court_id, 'MLD','P','R',SYSDATE,NULL);
      --COMMIT;

      /*
       * Create an empty record set for return value (temporary until data required)
       */

      OPEN p_results_out FOR

        SELECT *
        FROM   xhb_training_status
        WHERE  court_id = -1;

    ELSE

      /*
       * Create an empty record set for return value (temporary until data required)
       */

      OPEN p_results_out FOR

        SELECT *
        FROM   xhb_training_status
        WHERE  court_id = -1;

      RAISE_APPLICATION_ERROR (-20007, 'A purge/load cycle is already in progress');

    END IF;

  END request_mld_restore;

/*
 *
 * GET_RESTORE_STATES
 *
 */

  PROCEDURE get_restore_states(p_results_out OUT SYS_REFCURSOR,
                               p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    OPEN p_results_out FOR

      SELECT court_id,
             operation_code,
             status_code,
             start_time,
             schema_name,
             end_time
      FROM   xhb_training_status
      WHERE  court_id = p_court_id;

  END get_restore_states;

/*
 *
 * RESTORE_XHIBIT
 *
 */

  PROCEDURE restore_xhibit(p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

  err_code      NUMBER;
  err_msg       VARCHAR2(2000);
--  l_results_out SYS_REFCURSOR;
--  p_court_id    NUMBER := 1;
  -- table to store the addresses that must be deleted...
  l_address_id_t XHB_NUMBER_TABLE_TYP;

  BEGIN

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'I'
    WHERE  court_id = p_court_id
    AND    schema_name = 'XHIBIT'
    AND    operation_code = 'P';

    BEGIN

      -- cache all of the address ids that we will want to delete afterwards...
      -- this could be moved to later on to reduce grab on memory if required...
      SELECT address_id
      BULK COLLECT INTO l_address_id_t
      FROM (SELECT address_id FROM XHB_DEFENDANT WHERE court_id = p_court_id
            UNION ALL
            SELECT R.RECIPIENT_ADDRESS_ID
            FROM   XHB_PSR_RECIPIENT R,
                   XHB_PSR_REQUEST PRQ,
                   XHB_DEFENDANT_ON_CASE DC,
                   XHB_CASE C
            WHERE  R.RECIPIENT_ID = PRQ.PSR_RECIPIENT_ID
            AND    PRQ.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
            AND    DC.CASE_ID = C.CASE_ID
            AND    C.COURT_ID = p_court_id);

      DELETE FROM XHB_LINKED_CASE LC
      WHERE  LC.LINKED_CASE_ID IN (SELECT CASE_ID
                                   FROM   XHB_CASE C,
                                          XHB_COURT CT
                                   WHERE  C.COURT_ID = CT.COURT_ID
                                   AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_LINKED_HEARING
      WHERE  LINKED_HEARING_ID IN (SELECT LINKED_HEARING_ID
                                   FROM   XHB_HEARING H
                                   WHERE  H.COURT_ID = p_court_id);

      DELETE FROM XHB_LINKED_SH LSH
      WHERE  LSH.LINKED_SH_ID IN (SELECT LINKED_SH_ID
                                  FROM   XHB_SCHEDULED_HEARING SH,
                                         XHB_HEARING H
                                  WHERE  SH.HEARING_ID = H.HEARING_ID
                                  AND    H.COURT_ID = p_court_id); 

      DELETE FROM XHB_OBJECT_STATUS
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_SUBSCR_EVENT_CONTROL SEC
      WHERE  SEC.CREST_COURT_ID IN (SELECT CREST_COURT_ID
                                    FROM   XHB_COURT C
                                    WHERE  C.COURT_ID = p_court_id);

      DELETE FROM XHB_SH_LEG_REP LR
      WHERE  LR.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                         FROM   XHB_SCHEDULED_HEARING SH,
                                                XHB_HEARING H
                                         WHERE  SH.HEARING_ID = H.HEARING_ID
                                         AND    H.COURT_ID = p_court_id);

      DELETE FROM XHB_PLEA P
      WHERE  P.DEFENDANT_CHARGE_ID IN (SELECT DEFENDANT_CHARGE_ID
                                       FROM   XHB_DEFENDANT_CHARGE DC,
                                              XHB_CHARGE CH,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DC.CHARGE_ID = CH.CHARGE_ID
                                       AND    CH.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_PLEA P
      WHERE  P.DEFENDANT_ON_OFFENCE_ID IN (SELECT DEFENDANT_ON_OFFENCE_ID
                                           FROM   XHB_DEFENDANT_ON_OFFENCE DO,
                                                  XHB_OFFENCE O,
                                                  XHB_CHARGE CH,
                                                  XHB_CASE C,
                                                  XHB_COURT CT
                                           WHERE  DO.OFFENCE_ID = O.OFFENCE_ID
                                           AND    O.CHARGE_ID = CH.CHARGE_ID
                                           AND    CH.CASE_ID = C.CASE_ID
                                           AND    C.COURT_ID = CT.COURT_ID
                                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_VERDICT V
      WHERE  V.DEFENDANT_CHARGE_ID IN (SELECT DEFENDANT_CHARGE_ID
                                       FROM   XHB_DEFENDANT_CHARGE DC,
                                              XHB_CHARGE CH,
                                              XHB_CASE C,
                                              XHB_COURT CT
                                       WHERE  DC.CHARGE_ID = CH.CHARGE_ID
                                       AND    CH.CASE_ID = C.CASE_ID
                                       AND    C.COURT_ID = CT.COURT_ID
                                       AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_VERDICT V
      WHERE  V.DEFENDANT_ON_OFFENCE_ID IN (SELECT DEFENDANT_ON_OFFENCE_ID
                                           FROM   XHB_DEFENDANT_ON_OFFENCE DO,
                                                  XHB_OFFENCE O,
                                                  XHB_CHARGE CH,
                                                  XHB_CASE C,
                                                  XHB_COURT CT
                                           WHERE  DO.OFFENCE_ID = O.OFFENCE_ID
                                           AND    O.CHARGE_ID = CH.CHARGE_ID
                                           AND    CH.CASE_ID = C.CASE_ID
                                           AND    C.COURT_ID = CT.COURT_ID
                                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_VERDICT WHERE case_id IN (SELECT case_id FROM XHB_CASE WHERE court_id = p_court_id);

      DELETE FROM XHB_SH_JUDGE J
      WHERE  J.SH_ATTENDEE_ID IN (SELECT SH_ATTENDEE_ID
                                  FROM   XHB_SCHED_HEARING_ATTENDEE SHA,
                                         XHB_SCHEDULED_HEARING SH,
                                         XHB_HEARING H
                                  WHERE  SHA.SCHEDULED_HEARING_ID = SH.SCHEDULED_HEARING_ID
                                  AND    SH.HEARING_ID = H.HEARING_ID
                                  AND    H.COURT_ID = p_court_id);

      DELETE FROM XHB_IMPORT_EXPORT_STATUS
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_WITNESS W
      WHERE  W.CASE_ID IN (SELECT CASE_ID
                           FROM   XHB_CASE C,
                                  XHB_COURT CT
                           WHERE  C.COURT_ID = CT.COURT_ID
                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_PSR_REQUEST PRQ
      WHERE  PRQ.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                          FROM   XHB_DEFENDANT_ON_CASE DC,
                                                 XHB_CASE C,
                                                 XHB_COURT CT
                                          WHERE  DC.CASE_ID = C.CASE_ID
                                          AND    C.COURT_ID = CT.COURT_ID
                                          AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_JOINDER_DEFENDANT_ON_CASE JD
      WHERE  JD.defendant_on_case_id_1 IN (SELECT DEFENDANT_ON_CASE_ID
                                           FROM   XHB_DEFENDANT_ON_CASE DC,
                                                  XHB_CASE C,
                                                  XHB_COURT CT
                                           WHERE  DC.CASE_ID = C.CASE_ID
                                           AND    C.COURT_ID = CT.COURT_ID
                                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_JOINDER_DEFENDANT_ON_CASE JD
      WHERE  JD.defendant_on_case_id_2 IN (SELECT DEFENDANT_ON_CASE_ID
                                           FROM   XHB_DEFENDANT_ON_CASE DC,
                                                  XHB_CASE C,
                                                  XHB_COURT CT
                                           WHERE  DC.CASE_ID = C.CASE_ID
                                           AND    C.COURT_ID = CT.COURT_ID
                                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DIRECTIONS_FOR_DEFENDANT DD
      WHERE  DD.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                         FROM   XHB_DEFENDANT_ON_CASE DC,
                                                XHB_CASE C,
                                                XHB_COURT CT
                                         WHERE  DC.CASE_ID = C.CASE_ID
                                         AND    C.COURT_ID = CT.COURT_ID
                                         AND    CT.COURT_ID = p_court_id);
/*
      DELETE FROM TMP_ORDER_IDS;

      INSERT INTO TMP_ORDER_IDS (SELECT ORDER_DELIVERY_STATUS_ID,
                                        ORDER_STATUS_ID
                                 FROM   XHB_ORDER O
                                 WHERE  O.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                                                   FROM   XHB_DEFENDANT_ON_CASE DC,
                                                                          XHB_CASE C,
                                                                          XHB_COURT CT
                                                                   WHERE  DC.CASE_ID = C.CASE_ID
                                                                   AND    C.COURT_ID = CT.COURT_ID
                                                                   AND    CT.COURT_ID = p_court_id));
*/

      DELETE FROM XHB_ORDER O
      WHERE  O.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                        FROM   XHB_DEFENDANT_ON_CASE DC,
                                               XHB_CASE C,
                                               XHB_COURT CT
                                        WHERE  DC.CASE_ID = C.CASE_ID
                                        AND    C.COURT_ID = CT.COURT_ID
                                        AND    CT.COURT_ID = p_court_id);

/*
      DELETE FROM XHB_ORDER_STATUS
      WHERE  ORDER_STATUS_ID IN (SELECT ORDER_STATUS_ID
                                 FROM   TMP_ORDER_IDS);

      DELETE FROM XHB_ORDER_DELIVERY_STATUS
      WHERE  ORDER_DELIVERY_STATUS_ID IN (SELECT ORDER_DELIVERY_STATUS_ID
                                          FROM   TMP_ORDER_IDS);
*/

      DELETE FROM XHB_COURT_LOG_ENTRY CL
      WHERE  CL.CASE_ID IN (SELECT CASE_ID
                            FROM   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DIRECTION_ATTEND DA
      WHERE  DA.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                         FROM   XHB_DEFENDANT_ON_CASE DC,
                                                XHB_CASE C,
                                                XHB_COURT CT
                                         WHERE  DC.CASE_ID = C.CASE_ID
                                         AND    C.COURT_ID = CT.COURT_ID
                                         AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_SCHED_HEARING_DEFENDANT SHD
      WHERE  SHD.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                          FROM   XHB_SCHEDULED_HEARING SH,
                                                 XHB_HEARING H
                                          WHERE  SH.HEARING_ID = H.HEARING_ID
                                          AND    H.COURT_ID = p_court_id); 

      DELETE FROM XHB_DEFENDANT_CHARGE DC
      WHERE  DC.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                         FROM   XHB_DEFENDANT_ON_CASE DC,
                                                XHB_CASE C,
                                                XHB_COURT CT
                                         WHERE  DC.CASE_ID = C.CASE_ID
                                         AND    C.COURT_ID = CT.COURT_ID
                                         AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DEF_HEARING_RECORD DHR
      WHERE  DHR.HEARING_ID IN (SELECT HEARING_ID
                                FROM   XHB_HEARING H
                                WHERE  H.COURT_ID = p_court_id);

      DELETE FROM XHB_CR_LIVE_STATUS CLS
      WHERE  CLS.COURT_ROOM_ID IN (SELECT COURT_ROOM_ID
                                   FROM   XHB_COURT_ROOM XC,
                                          XHB_COURT_SITE XS,
                                          XHB_COURT C
                                   WHERE  XC.COURT_SITE_ID = XS.COURT_SITE_ID
                                   AND    XS.COURT_ID = C.COURT_ID
                                   AND    C.COURT_ID = p_court_id);

      DELETE FROM XHB_DISPOSAL_LINE DL
      WHERE  DL.DISPOSAL2_ID IN (SELECT xd.disposal2_id
                                 FROM   xhb_case xc,
                                        xhb_defendant_on_case xdoc,
                                        xhb_defendant_on_offence xdoo,
                                        xhb_disposal2 xd
                                 WHERE  xdoc.case_id = xc.case_id
                                 AND    xdoo.defendant_on_case_id(+) = xdoc.defendant_on_case_id
                                 AND    (xd.defendant_on_case_id = xdoc.defendant_on_case_id
                                 OR     xd.defendant_on_offence_id = xdoo.defendant_on_offence_id)
                                 AND    xc.court_id = p_court_id);
      
      DELETE FROM XHB_DISPOSAL2 D2
      WHERE  D2.DISPOSAL2_ID IN (SELECT xd.disposal2_id
                                 FROM   xhb_case xc,
                                        xhb_defendant_on_case xdoc,
                                        xhb_defendant_on_offence xdoo,
                                        xhb_disposal2 xd
                                 WHERE  xdoc.case_id = xc.case_id
                                 AND    xdoo.defendant_on_case_id(+) = xdoc.defendant_on_case_id
                                 AND    (xd.defendant_on_case_id = xdoc.defendant_on_case_id
                                 OR     xd.defendant_on_offence_id = xdoo.defendant_on_offence_id)
                                 AND    xc.court_id = p_court_id);

      DELETE FROM XHB_DEFENDANT_ON_OFFENCE DO
      WHERE  DO.DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID
                                         FROM   XHB_DEFENDANT_ON_CASE DC,
                                                XHB_CASE C,
                                                XHB_COURT CT
                                         WHERE  DC.CASE_ID = C.CASE_ID
                                         AND    C.COURT_ID = CT.COURT_ID
                                         AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_SCHED_HEARING_ATTENDEE SHA
      WHERE  SHA.SCHEDULED_HEARING_ID IN (SELECT SCHEDULED_HEARING_ID
                                          FROM   XHB_SCHEDULED_HEARING SH,
                                                 XHB_HEARING H
                                          WHERE  SH.HEARING_ID = H.HEARING_ID
                                          AND    H.COURT_ID = p_court_id);

      /*
       * Due to the foreign key constraints on tables XHB_SKELETON_SESSION, XHB_SKELETON_DAY
       * and XHB_SKELETON_SCHEDULE the deletion from XHB_SKELETON_SESSION causes problems when
       * trying to delete from XHB_SKELETON_DAY and consequently XHB_SKELETON_SCHEDULE.
       *
       * To work around this, those records that should be deleted from XHB_SKELETON_DAY which
       * cannot be having already deleted its linked data to XHB_SKELETON_SESSION, will be stored
       * in a temporary table.  The ROWIDs will be stored as this will be all that is required.
       * This temporary table MUST be populated BEFORE the deletion from XHB_SKELETON_SESSION.
       */

      INSERT INTO TMP_SKELETON_DAY_DELETE (SELECT rowid
                                           FROM XHB_SKELETON_DAY SD
                                           WHERE  SD.SKELETON_DAY_ID IN (SELECT SKELETON_DAY_ID
                                                                         FROM   XHB_SKELETON_SESSION SS,
                                                                                XHB_SKELETON_SCHEDULE SSH,
                                                                                XHB_CASE C,
                                                                                XHB_COURT CT
                                                                         WHERE  SS.SKELETON_ID = SSH.SKELETON_ID
                                                                         AND    SSH.CASE_ID = C.CASE_ID
                                                                         AND    C.COURT_ID = CT.COURT_ID
                                                                         AND    CT.COURT_ID = p_court_id));

      DELETE FROM XHB_SKELETON_SESSION SS
      WHERE  SS.SKELETON_ID IN (SELECT SKELETON_ID
                                FROM   XHB_SKELETON_SCHEDULE SSH,
                                       XHB_CASE C,
                                       XHB_COURT CT
                                WHERE  SSH.CASE_ID = C.CASE_ID
                                AND    C.COURT_ID = CT.COURT_ID
                                AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DEFENDANT_REFERENCE DR
      WHERE  DR.DEFENDANT_ID IN (SELECT DEFENDANT_ID
                                 FROM   XHB_DEFENDANT D,
                                        XHB_COURT CT
                                 WHERE  D.COURT_ID = CT.COURT_ID
                                 AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_PSR_RECIPIENT PR
      WHERE  PR.RECIPIENT_ID IN (SELECT PSR_RECIPIENT_ID
                                 FROM   XHB_PSR_REQUEST PRQ,
                                        XHB_DEFENDANT_ON_CASE DC,
                                        XHB_CASE C,
                                        XHB_COURT CT
                                 WHERE  PRQ.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
                                 AND    DC.CASE_ID = C.CASE_ID
                                 AND    C.COURT_ID = CT.COURT_ID
                                 AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DEFENDANT_ON_CASE DC
      WHERE  DC.CASE_ID IN (SELECT CASE_ID
                            FROM   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DIRECTIONS_FOR_CASE DC
      WHERE  DC.CASE_ID IN (SELECT CASE_ID
                            FROM   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_SCHEDULED_HEARING SH
      WHERE  SH.HEARING_ID IN (SELECT HEARING_ID
                               FROM   XHB_HEARING H
                               WHERE  H.COURT_ID = p_court_id);

      DELETE FROM XHB_OFFENCE O
      WHERE  O.CHARGE_ID IN (SELECT CHARGE_ID
                             FROM   XHB_CHARGE CH,
                                    XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  CH.CASE_ID = C.CASE_ID
                             AND    C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_BREACH B
      WHERE  B.CHARGE_ID IN (SELECT CHARGE_ID
                             FROM   XHB_CHARGE CH,
                                    XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  CH.CASE_ID = C.CASE_ID
                             AND    C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_JOINDER_CHARGE JC
      WHERE  JC.CHARGE_ID IN (SELECT CHARGE_ID
                              FROM   XHB_CHARGE CH,
                                     XHB_CASE C,
                                     XHB_COURT CT
                              WHERE  CH.CASE_ID = C.CASE_ID
                              AND    C.COURT_ID = CT.COURT_ID
                              AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_EXPORTA E
      WHERE  E.HEARING_ID IN (SELECT HEARING_ID
                              FROM   XHB_HEARING H,
                                     XHB_COURT C
                              WHERE  H.COURT_ID = C.COURT_ID
                              AND    C.COURT_ID = p_court_id);

      DELETE FROM XHB_SH_JUSTICE J
      WHERE  J.HEARING_ID IN (SELECT HEARING_ID
                              FROM   XHB_HEARING H
                              WHERE  H.COURT_ID = p_court_id);

      DELETE FROM XHB_JOINDER_XML JX
      WHERE  JX.JOINDER_ID IN (SELECT JOINDER_ID
                               FROM   XHB_JOINDER_CHARGE JC,
                                      XHB_CHARGE CH,
                                      XHB_CASE C,
                                      XHB_COURT CT
                               WHERE  JC.CHARGE_ID = CH.CHARGE_ID
                               AND    CH.CASE_ID = C.CASE_ID
                               AND    C.COURT_ID = CT.COURT_ID
                               AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_SH_STAFF SS
      WHERE  SS.SH_STAFF_ID IN (SELECT SH_STAFF_ID
                                FROM   XHB_SCHED_HEARING_ATTENDEE SHA,
                                       XHB_SCHEDULED_HEARING SH,
                                       XHB_HEARING H
                                WHERE  SHA.SCHEDULED_HEARING_ID = SH.SCHEDULED_HEARING_ID 
                                AND    SH.HEARING_ID = H.HEARING_ID
                                AND    H.COURT_ID = p_court_id);

      DELETE FROM XHB_SKELETON_DAY
      WHERE  ROWID IN (SELECT del_row_id
                       FROM   TMP_SKELETON_DAY_DELETE);


      -- delete all of the defendants for the court...
      DELETE FROM XHB_DEFENDANT
      WHERE  COURT_ID = p_court_id;

      -- and finally delete the addresses for the defendant and psr request...
      DELETE FROM XHB_ADDRESS xa1
      WHERE EXISTS (SELECT 1
                    FROM   TABLE(cast(l_address_id_t AS xhb_number_table_typ)) t1,
                           XHB_ADDRESS xa
                    WHERE  xa.address_id = t1.column_value
                    AND    xa.ROWID = xa1.ROWID);

      -- .. and now clear the cache to allow the memory to be reclaimed...
      l_address_id_t.DELETE;

      DELETE FROM XHB_SITTING 
      WHERE  COURT_SITE_ID IN (SELECT COURT_SITE_ID
                               FROM   XHB_COURT_SITE CS,
                                      XHB_COURT C
                               WHERE  CS.COURT_ID = C.COURT_ID
                               AND    C.COURT_ID = p_court_id);

      DELETE FROM XHB_CASE_PROSECUTOR_AGENCY CPA
      WHERE  CPA.CASE_ID IN (SELECT CASE_ID
                             FROM   XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_CASE_REFERENCE CR
      WHERE  CR.CASE_ID IN (SELECT CASE_ID
                            FROM   XHB_CASE C,
                                   XHB_COURT CT
                            WHERE  C.COURT_ID = CT.COURT_ID
                            AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_CHARGE C
      WHERE  C.CASE_ID IN (SELECT CASE_ID
                           FROM   XHB_CASE C,
                                  XHB_COURT CT
                           WHERE  C.COURT_ID = CT.COURT_ID
                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_CHARGE_DIFFERENCES 
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_HEARING_LEG_REP
      WHERE  HEARING_ID IN (SELECT HEARING_ID
                            FROM   XHB_HEARING 
                            WHERE  COURT_ID = p_court_id);

      DELETE FROM XHB_HEARING 
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_TIME T
      WHERE  T.CASE_ID IN (SELECT CASE_ID
                           FROM   XHB_CASE C,
                                  XHB_COURT CT
                           WHERE  C.COURT_ID = CT.COURT_ID
                           AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_JOINDER J
      WHERE  J.JOINDER_ID IN (SELECT JOINDER_ID
                              FROM   XHB_JOINDER_CHARGE JC,
                                     XHB_CHARGE CH,
                                     XHB_CASE C,
                                     XHB_COURT CT
                              WHERE  JC.CHARGE_ID = CH.CHARGE_ID
                              AND    CH.CASE_ID = C.CASE_ID
                              AND    C.COURT_ID = CT.COURT_ID
                              AND    CT.COURT_ID = p_court_id);

/*
      DELETE FROM XHB_SKELETON_DELIVERY_STATUS SDS
      WHERE  SDS.SKELETON_DELIVERY_STATUS_ID IN (SELECT SKELETON_DELIVERY_STATUS_ID
                                                 FROM   XHB_SKELETON_SCHEDULE SSH,
                                                        XHB_CASE C,
                                                        XHB_COURT CT
                                                 WHERE  SSH.CASE_ID = C.CASE_ID
                                                 AND    C.COURT_ID = CT.COURT_ID
                                                 AND    CT.COURT_ID = p_court_id);
*/

      DELETE FROM XHB_SKELETON_SCHEDULE SSH
      WHERE  SSH.CASE_ID IN (SELECT CASE_ID
                             FROM   XHB_CASE C,
                                    XHB_COURT CT
                             WHERE  C.COURT_ID = CT.COURT_ID
                             AND    CT.COURT_ID = p_court_id);

/*
      DELETE FROM XHB_CONTACT_DETAIL
      WHERE  ADDRESS_ID IN (SELECT A.ADDRESS_ID
                            FROM   XHB_ADDRESS A,
                                   XHB_COURT C
                            WHERE  A.ADDRESS_ID = C.ADDRESS_ID
                            AND    C.COURT_ID = p_court_id);
*/

/*
      DELETE FROM XHB_ADDRESS A
      WHERE  A.ADDRESS_ID IN (SELECT R.RECIPIENT_ADDRESS_ID
                              FROM   XHB_PSR_RECIPIENT R,
                                     XHB_PSR_REQUEST PRQ,
                                     XHB_DEFENDANT_ON_CASE DC,
                                     XHB_CASE C,
                                     XHB_COURT CT
                              WHERE  R.RECIPIENT_ID = PRQ.PSR_RECIPIENT_ID
                              AND    PRQ.DEFENDANT_ON_CASE_ID = DC.DEFENDANT_ON_CASE_ID
                              AND    DC.CASE_ID = C.CASE_ID
                              AND    C.COURT_ID = CT.COURT_ID
                              AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_ADDRESS A
      WHERE  A.ADDRESS_ID IN (SELECT R.ADDRESS_ID
                              FROM   XHB_DEFENDANT R,
                                     XHB_COURT CT
                              WHERE  R.COURT_ID = CT.COURT_ID
                              AND    CT.COURT_ID = p_court_id);
*/

      DELETE FROM XHB_HEARING_LIST 
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_CASE_APP_REASON
      WHERE  CASE_ID IN (SELECT CASE_ID
                         FROM   XHB_CASE
                         WHERE  COURT_ID = p_court_id);

      DELETE FROM XHB_CASE_APP_REASON
      WHERE  CASE_ID IN (SELECT CASE_ID
                         FROM XHB_CASE
                         WHERE  COURT_ID = p_court_id);

      DELETE FROM XHB_CASE
      WHERE  COURT_ID = p_court_id;

      DELETE FROM TMP_SKELETON_DAY_DELETE;

      COMMIT;

      EXCEPTION

        WHEN OTHERS THEN

          err_code := SQLCODE;
          err_msg := SQLERRM;

          rollback;

          UPDATE XHB_TRAINING_STATUS
          SET    status_code = 'F'
          WHERE  court_id = p_court_id
          AND    schema_name = 'XHIBIT'
          AND    operation_code = 'P';
          
          commit;

          RAISE_APPLICATION_ERROR(-20900,err_code||': '||err_msg);

    END;

    /*
     * Call the MLD data restore proc since this could be called on its own
     */

    restore_mld_data(p_court_id);

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'C',
           end_time = sysdate
    WHERE  court_id = p_court_id
    AND    schema_name = 'XHIBIT'
    AND    operation_code = 'P';

    INSERT INTO XHB_TRAINING_STATUS VALUES (p_court_id, 'XHIBIT','L','I',SYSDATE,NULL);
    COMMIT;

    populate_tables(p_court_id);
    COMMIT;

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'C',
           end_time = sysdate
    WHERE  court_id = p_court_id
    AND    schema_name = 'XHIBIT'
    AND    operation_code = 'L';

    COMMIT;

  END restore_xhibit;

/*
 *
 * RESTORE_MLD_DATA (Delete step)
 *
 */

  PROCEDURE restore_mld_data(p_court_id    IN   XHB_COURT.COURT_ID%TYPE) IS

  err_code      NUMBER;
  err_msg       VARCHAR2(2000);
  l_clob_id_t   XHB_NUMBER_TABLE_TYP;
  l_blob_id_t   XHB_NUMBER_TABLE_TYP;

  BEGIN

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'I'
    WHERE  court_id = p_court_id
    AND    schema_name = 'MLD'
    AND    operation_code = 'P';

    BEGIN

--      DELETE FROM XHB_DOCUMENT_REPLY
--      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_DOCUMENT_RECIPIENT DR
      WHERE  DR.DOC_CONTROL_ID IN (SELECT DOC_CONTROL_ID
                                   FROM   XHB_DOCUMENT_CONTROL DC,
                                          XHB_COURT CT
                                   WHERE  DC.COURT_ID = CT.COURT_ID
                                   AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DOCUMENT_DISTRIBUTION
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_WLL_DOCUMENT WD
      WHERE  WD.XML_DOCUMENT_ID IN (SELECT XML_DOCUMENT_ID
                                    FROM   XHB_XML_DOCUMENT XD,
                                           XHB_COURT CT
                                    WHERE  XD.COURT_ID = CT.COURT_ID
                                    AND    CT.COURT_ID = p_court_id);

      DELETE FROM XHB_DOCUMENT_CONTROL
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_RECIPIENT
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_WLL_RECIPIENT
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_WLL_CONTROL WC
      WHERE  WC.XML_DOCUMENT_ID IN (SELECT XML_DOCUMENT_ID
                                    FROM   XHB_XML_DOCUMENT XD,
                                           XHB_COURT CT
                                    WHERE  XD.COURT_ID = CT.COURT_ID
                                    AND    CT.COURT_ID = p_court_id);


      -- cache all of the blob/clob ids that we will want to delete afterwards...
      SELECT CLOB_ID
      BULK   COLLECT INTO l_clob_id_t
      FROM ( SELECT XML_DOCUMENT_CLOB_ID as CLOB_ID
			 FROM   XHB_XML_DOCUMENT
			 WHERE  COURT_ID = p_court_id
			 UNION
			 SELECT XML_DOCUMENT_CLOB_ID as CLOB_ID
			 FROM   XHB_FORMATTING
			 WHERE  COURT_ID = p_court_id
			 );
      SELECT BLOB_ID
      BULK   COLLECT INTO l_blob_id_t
      FROM ( SELECT FORMATTED_DOCUMENT_BLOB_ID as BLOB_ID
			 FROM   XHB_DOCUMENT_CONTROL
			 WHERE  COURT_ID = p_court_id
			 UNION
			 SELECT MIME_BODY_BLOB_ID as BLOB_ID
			 FROM   XHB_EMAIL
			 WHERE  COURT_ID = p_court_id
			 UNION
			 SELECT FORMATTED_DOCUMENT_BLOB_ID as BLOB_ID
			 FROM   XHB_FORMATTING
			 WHERE  COURT_ID = p_court_id
			 UNION
			 SELECT HTML_BLOB_ID as BLOB_ID
			 FROM   XHB_INTERNET_HTML
			 WHERE  COURT_ID = p_court_id
			 );

      DELETE FROM XHB_INTERNET_HTML 
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_DOCUMENT_CONTROL
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_FORMATTING
      WHERE  COURT_ID = p_court_id;                                

      DELETE FROM XHB_XML_DOCUMENT
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_EMAIL
      WHERE  COURT_ID = p_court_id;

      DELETE FROM XHB_CLOB xa1
      WHERE EXISTS (SELECT 1
                    FROM   TABLE(cast(l_clob_id_t AS xhb_number_table_typ)) t1,
                           XHB_CLOB xa
                    WHERE  xa.clob_id = t1.column_value
                    AND    xa.ROWID = xa1.ROWID);
                    
      DELETE FROM XHB_BLOB xa1
      WHERE EXISTS (SELECT 1
                    FROM   TABLE(cast(l_blob_id_t AS xhb_number_table_typ)) t1,
                           XHB_BLOB xa
                    WHERE  xa.blob_id = t1.column_value
                    AND    xa.ROWID = xa1.ROWID);

      EXCEPTION

        WHEN OTHERS THEN

          err_code := SQLCODE;
          err_msg := SQLERRM;

          rollback;

          UPDATE XHB_TRAINING_STATUS
          SET    status_code = 'F'
          WHERE  court_id = p_court_id
          AND    schema_name = 'MLD'
          AND    operation_code = 'P';
          
          commit;

          RAISE_APPLICATION_ERROR(-20900,err_code||': '||err_msg);
    END;

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'C',
           end_time = sysdate
    WHERE  court_id = p_court_id
    AND    schema_name = 'MLD'
    AND    operation_code = 'P';

    INSERT INTO XHB_TRAINING_STATUS VALUES (p_court_id, 'MLD','L','I',SYSDATE,NULL);
    COMMIT;

    -- call insert script here
    insert_mld_data(p_court_id);

    UPDATE XHB_TRAINING_STATUS
    SET    status_code = 'C',
           end_time = sysdate
    WHERE  court_id = p_court_id
    AND    schema_name = 'MLD'
    AND    operation_code = 'L';

    COMMIT;
  END restore_mld_data;


/*
 *
 * RESTORE_MLD_DATA (Insert step)
 *
 */
PROCEDURE insert_mld_data(p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

BEGIN

-- WLL RECIPIENTS
INSERT INTO XHB_WLL_RECIPIENT ( WLL_RECIPIENT_ID, CREST_SOLICITOR_FIRM_ID, SOLICITOR_FIRM_NAME, SOLICTIOR_FIRM_ADDRESS, 
                                SOLICITOR_FIRM_FAX, SOLICITOR_FIRM_EMAIL, COURT_ID, RECIPIENT_TYPE ) 
VALUES ( XHB_WLL_RECIPIENT_SEQ.nextval, 12010, 'ABBOTT FORBES SOLICITORS', '69A STATION ROAD,WEST DRAYTON,MIDDLESEX,UB7 7LR', NULL, 'dummy.address@email.com', p_court_id, 'S'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID,
                                        WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'WLL', 'HTM', NULL, XHB_WLL_RECIPIENT_SEQ.currval, p_court_id, 'N'); 

INSERT INTO XHB_WLL_RECIPIENT ( WLL_RECIPIENT_ID, CREST_SOLICITOR_FIRM_ID, SOLICITOR_FIRM_NAME, SOLICTIOR_FIRM_ADDRESS, 
                                SOLICITOR_FIRM_FAX, SOLICITOR_FIRM_EMAIL, COURT_ID, RECIPIENT_TYPE ) 
VALUES ( XHB_WLL_RECIPIENT_SEQ.nextval, 32, 'ILIFFES BOOTH BENNETT', 'THE MARKET HOUSE,HIGH STREET,UXBRIDGE,MIDDLESEX,UB8 1AQ', NULL, 'dummy.address@email.com', p_court_id, 'S'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID,
                                        WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'WLL', 'PDF', NULL, XHB_WLL_RECIPIENT_SEQ.currval, p_court_id, 'N'); 

INSERT INTO XHB_WLL_RECIPIENT ( WLL_RECIPIENT_ID, CREST_SOLICITOR_FIRM_ID, SOLICITOR_FIRM_NAME, SOLICTIOR_FIRM_ADDRESS, 
                                SOLICITOR_FIRM_FAX, SOLICITOR_FIRM_EMAIL, COURT_ID, RECIPIENT_TYPE ) 
VALUES ( XHB_WLL_RECIPIENT_SEQ.nextval, 10113, 'OWEN WHITE '||chr(38)||' CATLIN', '12 BATH ROAD,HOUNSLOW,MIDDLESEX,TW3 3EB', '02071234567', NULL, p_court_id, 'S'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID,
                                        WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('FAX', 'WLL', 'PDF', NULL, XHB_WLL_RECIPIENT_SEQ.currval, p_court_id, 'N'); 



---  xhb_recipient

INSERT INTO XHB_RECIPIENT ( RECIPIENT_ID, RECIPIENT_NAME, FAX_NUMBER, EMAIL_ADDRESS, COURT_ID, PREF_DISTRIBUTION_TYPE, PREF_MIME_TYPE ) 
VALUES (XHB_RECIPIENT_SEQ.nextval, 'jim', '4123', 'jimbob@bobby.net', p_court_id, 'EMAIL', 'HTM'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DLP', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'RL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'FL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'WL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 


INSERT INTO XHB_RECIPIENT ( RECIPIENT_ID, RECIPIENT_NAME, FAX_NUMBER, EMAIL_ADDRESS, COURT_ID, PREF_DISTRIBUTION_TYPE, PREF_MIME_TYPE ) 
VALUES (XHB_RECIPIENT_SEQ.nextval, 'Dawes', '0207 833  1212', 'dawes@freeserve.co.uk', p_court_id, 'EMAIL', 'HTM'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DLP', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'RL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'FL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'WL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 


INSERT INTO XHB_RECIPIENT ( RECIPIENT_ID, RECIPIENT_NAME, FAX_NUMBER, EMAIL_ADDRESS, COURT_ID, PREF_DISTRIBUTION_TYPE, PREF_MIME_TYPE ) 
VALUES (XHB_RECIPIENT_SEQ.nextval, 'Henry Leach', '5234', 'leachandsons@coutts.com', p_court_id, 'EMAIL', 'HTM'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'DLP', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'RL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'FL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 
INSERT INTO XHB_DOCUMENT_DISTRIBUTION ( DISTRIBUTION_TYPE, DOCUMENT_TYPE, MIME_TYPE, RECIPIENT_ID, WLL_RECIPIENT_ID, COURT_ID, USE_PREF_DIST_TYPE ) 
VALUES ('EMAIL', 'WL', 'HTM', XHB_RECIPIENT_SEQ.currval, NULL, p_court_id, 'Y'); 

END insert_mld_data;

 PROCEDURE check_xhibit_restore_state(p_results_out OUT SYS_REFCURSOR,
                                      p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS

  BEGIN

    OPEN p_results_out FOR
     SELECT  court_id,
      operation_code,
      status_code,
      start_time,
      schema_name,
      end_time
      FROM   XHB_TRAINING_STATUS
      WHERE  court_id = p_court_id
      AND (operation_code = 'P' OR operation_code = 'L') 
      AND (status_code = 'R' OR status_code='I');

   END check_xhibit_restore_state;


    FUNCTION get_ref_justice_id(p_court_id_in         IN XHB_REF_JUSTICE.court_id%TYPE,
                                p_crest_justice_id_in IN XHB_REF_JUSTICE.crest_justice_id%TYPE)
                                RETURN XHB_REF_JUSTICE.ref_justice_id%TYPE IS
        l_ref_justice_id XHB_REF_JUSTICE.ref_justice_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_crest_justice_id_in IS NULL) THEN
           RETURN NULL;
        END IF;

        SELECT ref_justice_id
        INTO   l_ref_justice_id
        FROM   XHB_REF_JUSTICE
        WHERE  court_id = p_court_id_in
        AND    crest_justice_id = p_crest_justice_id_in;

        RETURN l_ref_justice_id;
    END get_ref_justice_id;


    FUNCTION get_ref_judge_id(p_court_id_in       IN XHB_REF_JUDGE.court_id%TYPE,
                              p_crest_judge_id_in IN XHB_REF_JUDGE.crest_judge_id%TYPE)
                              RETURN XHB_REF_JUDGE.ref_judge_id%TYPE IS
        l_ref_judge_id XHB_REF_JUDGE.ref_judge_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_crest_judge_id_in IS NULL) THEN
           RETURN NULL;
        END IF;

        SELECT ref_judge_id
        INTO   l_ref_judge_id
        FROM   XHB_REF_JUDGE
        WHERE  court_id = p_court_id_in
        AND    crest_judge_id = p_crest_judge_id_in;

        RETURN l_ref_judge_id;
    END get_ref_judge_id;

    FUNCTION get_court_site_id(p_court_id_in IN XHB_COURT_SITE.court_id%TYPE)
                               RETURN XHB_COURT_SITE.court_site_id%TYPE IS
        l_court_site_id XHB_COURT_SITE.court_site_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT court_site_id
        INTO   l_court_site_id
        FROM   XHB_COURT_SITE
        WHERE  court_id = p_court_id_in
        AND    rownum = 1;

        RETURN l_court_site_id;        
    END get_court_site_id;


    FUNCTION get_court_room_id(p_court_id_in            IN XHB_COURT_SITE.court_id%TYPE,
                               p_crest_court_room_no_in IN XHB_COURT_ROOM.crest_court_room_no%TYPE)
                               RETURN XHB_COURT_ROOM.court_room_id%TYPE IS
        l_court_room_id XHB_COURT_ROOM.court_room_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_crest_court_room_no_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT court_room_id
        INTO   l_court_room_id
        FROM   XHB_COURT_ROOM
        WHERE  crest_court_room_no = p_crest_court_room_no_in
        AND    court_site_id = get_court_site_id(p_court_id_in);

        RETURN l_court_room_id;
    END get_court_room_id;


    FUNCTION get_ref_court_id(p_court_id_in         IN XHB_REF_COURT.court_id%TYPE,
                              p_court_short_name_in IN XHB_REF_COURT.court_short_name%TYPE)
                              RETURN XHB_REF_COURT.ref_court_id%TYPE IS
        l_ref_court_id XHB_REF_COURT.ref_court_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_court_short_name_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_court_id
        INTO   l_ref_court_id
        FROM   XHB_REF_COURT
        WHERE  court_id = p_court_id_in
        AND    court_short_name = p_court_short_name_in;

        RETURN l_ref_court_id;
    END get_ref_court_id;


    FUNCTION get_ref_hearing_type_id(p_court_id_in          IN XHB_REF_HEARING_TYPE.court_id%TYPE,
                                     p_hearing_type_code_in IN XHB_REF_HEARING_TYPE.hearing_type_code%TYPE,
                                     p_case_type_in         IN XHB_CASE.case_type%TYPE)
                                     RETURN XHB_REF_HEARING_TYPE.ref_hearing_type_id%TYPE IS
        l_ref_hearing_type_id XHB_REF_HEARING_TYPE.ref_hearing_type_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_hearing_type_code_in IS NULL OR p_case_type_in IS NULL) THEN
            RETURN NULL;
        END IF;
	
        SELECT ref_hearing_type_id
        INTO   l_ref_hearing_type_id
        FROM   XHB_REF_HEARING_TYPE
        WHERE  court_id = p_court_id_in
        AND    hearing_type_code = p_hearing_type_code_in
        AND    category = p_case_type_in;

        RETURN l_ref_hearing_type_id;
    END get_ref_hearing_type_id;


    FUNCTION get_ref_system_code_id(p_court_id_in  IN XHB_REF_SYSTEM_CODE.court_id%TYPE,
                                    p_code_in      IN XHB_REF_SYSTEM_CODE.code%TYPE,
                                    p_code_type_in IN XHB_REF_SYSTEM_CODE.code_type%TYPE)
                                    RETURN XHB_REF_SYSTEM_CODE.ref_system_code_id%TYPE IS
        l_ref_system_code_id XHB_REF_SYSTEM_CODE.ref_system_code_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_code_in IS NULL OR p_code_type_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_system_code_id
        INTO   l_ref_system_code_id
        FROM   XHB_REF_SYSTEM_CODE
        WHERE  court_id = p_court_id_in
        AND    code = p_code_in
        AND    code_type = p_code_type_in;

        RETURN l_ref_system_code_id;
    END get_ref_system_code_id;


    FUNCTION get_ref_offence_id(p_court_id_in     IN XHB_REF_OFFENCE.court_id%TYPE,
                                p_offence_code_in IN XHB_REF_OFFENCE.offence_code%TYPE)
                                RETURN XHB_REF_OFFENCE.ref_offence_id%TYPE IS
        l_ref_offence_id XHB_REF_OFFENCE.ref_offence_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_offence_code_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_offence_id
        INTO   l_ref_offence_id
        FROM   XHB_REF_OFFENCE
        WHERE  court_id = p_court_id_in
        AND    offence_code = p_offence_code_in;

        RETURN l_ref_offence_id;
    END get_ref_offence_id;


    FUNCTION get_ref_prosecutor_agency_id(p_court_id_in         IN XHB_REF_PROSECUTOR_AGENCY.court_id%TYPE,
                                          p_crest_opposer_id_in IN XHB_REF_PROSECUTOR_AGENCY.crest_opposer_id%TYPE)
                                          RETURN XHB_REF_PROSECUTOR_AGENCY.ref_prosecutor_agency_id%TYPE IS
        l_ref_prosecutor_agency_id XHB_REF_PROSECUTOR_AGENCY.ref_prosecutor_agency_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_crest_opposer_id_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_prosecutor_agency_id
        INTO   l_ref_prosecutor_agency_id
        FROM   XHB_REF_PROSECUTOR_AGENCY
        WHERE  court_id = p_court_id_in
        AND    crest_opposer_id = p_crest_opposer_id_in;

        RETURN l_ref_prosecutor_agency_id;
    END get_ref_prosecutor_agency_id;


    FUNCTION get_ref_app_result_id(p_court_id_in        IN XHB_REF_APP_RESULT.court_id%TYPE,
                                   p_app_result_code_in IN XHB_REF_APP_RESULT.app_result_code%TYPE)
                                   RETURN XHB_REF_APP_RESULT.ref_app_result_id%TYPE IS
        l_ref_app_result_id XHB_REF_APP_RESULT.ref_app_result_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_app_result_code_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_app_result_id
        INTO   l_ref_app_result_id
        FROM   XHB_REF_APP_RESULT
        WHERE  court_id = p_court_id_in
        AND    app_result_code = p_app_result_code_in;

        RETURN l_ref_app_result_id;
    END get_ref_app_result_id;


    FUNCTION get_ref_disposal_type_id(p_court_id_in         IN XHB_REF_DISPOSAL_TYPE.court_id%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_TYPE.template_version%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_TYPE.disposal_code%TYPE,
                                      p_menu_group_in       IN XHB_REF_DISPOSAL_TYPE.menu_group%TYPE)
                                      RETURN XHB_REF_DISPOSAL_TYPE.ref_disposal_type_id%TYPE IS
        l_ref_disposal_type_id XHB_REF_DISPOSAL_TYPE.ref_disposal_type_id%TYPE;
    BEGIN
        SELECT ref_disposal_type_id
        INTO   l_ref_disposal_type_id
        FROM   XHB_REF_DISPOSAL_TYPE
        WHERE  court_id = p_court_id_in
        AND    template_version = p_template_version_in
        AND    disposal_code = p_disposal_code_in
        AND    menu_group = p_menu_group_in;

        RETURN l_ref_disposal_type_id;
    END get_ref_disposal_type_id;


    FUNCTION get_ref_disposal_line_id(p_court_id_in         IN XHB_REF_DISPOSAL_LINE.court_id%TYPE,
                                      p_disposal_code_in    IN XHB_REF_DISPOSAL_LINE.disposal_code%TYPE,
                                      p_template_version_in IN XHB_REF_DISPOSAL_LINE.template_version%TYPE,
                                      p_dil_seq_no_in       IN XHB_REF_DISPOSAL_LINE.dil_seq_no%TYPE)
                                      RETURN XHB_REF_DISPOSAL_LINE.ref_disposal_line_id%TYPE IS
        l_ref_disposal_line_id XHB_REF_DISPOSAL_LINE.ref_disposal_line_id%TYPE;
    BEGIN
        IF (p_court_id_in IS NULL OR p_disposal_code_in IS NULL OR p_template_version_in IS NULL OR p_dil_seq_no_in IS NULL) THEN
            RETURN NULL;
        END IF;

        SELECT ref_disposal_line_id
        INTO   l_ref_disposal_line_id
        FROM   XHB_REF_DISPOSAL_LINE
        WHERE  court_id = p_court_id_in
        AND    disposal_code = p_disposal_code_in
        AND    template_version = p_template_version_in
        AND    dil_seq_no = p_dil_seq_no_in;

        RETURN l_ref_disposal_line_id;
    END get_ref_disposal_line_id;


    FUNCTION get_case_type(p_case_id_in IN EXT_XHB_CASE.case_id%TYPE)
                           RETURN EXT_XHB_CASE.case_type%TYPE IS
        l_case_type EXT_XHB_CASE.case_type%TYPE;
    BEGIN
        IF (p_case_id_in IS NULL) THEN
            RETURN NULL;
        END IF;
        
        SELECT case_type
        INTO   l_case_type
        FROM   EXT_XHB_CASE
        WHERE  case_id = p_case_id_in;
        
        RETURN l_case_type;
    END get_case_type;


    FUNCTION lookup_xhb_hearing_list_id(p_list_id_in IN XHB_HEARING_LIST.list_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('hearing_list', 'XHB_HEARING_LIST_SEQ', p_list_id_in);
    END lookup_xhb_hearing_list_id;


    FUNCTION lookup_xhb_sitting_id(p_sitting_id_in IN XHB_SITTING.sitting_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('sitting', 'XHB_SITTING_SEQ', p_sitting_id_in);
    END lookup_xhb_sitting_id;


    FUNCTION lookup_xhb_case_id(p_case_id_in IN XHB_CASE.case_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('case', 'XHB_CASE_SEQ', p_case_id_in);
    END lookup_xhb_case_id;


    FUNCTION lookup_xhb_hearing_id(p_hearing_id_in IN XHB_HEARING.hearing_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('hearing', 'XHB_HEARING_SEQ', p_hearing_id_in);
    END lookup_xhb_hearing_id;


    FUNCTION lookup_xhb_sched_hearing_id(p_scheduled_hearing_id_in IN XHB_SCHEDULED_HEARING.scheduled_hearing_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('sched_hearing', 'XHB_SCHEDULED_HEARING_SEQ', p_scheduled_hearing_id_in);
    END lookup_xhb_sched_hearing_id;


    FUNCTION lookup_xhb_address_id(p_address_id_in IN XHB_ADDRESS.address_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('address', 'XHB_ADDRESS_SEQ', p_address_id_in);
    END lookup_xhb_address_id;


    FUNCTION lookup_xhb_defendant_id(p_defendant_id_in IN XHB_DEFENDANT.defendant_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('defendant', 'XHB_DEFENDANT_SEQ', p_defendant_id_in);
    END lookup_xhb_defendant_id;


    FUNCTION lookup_xhb_def_on_case_id(p_defendant_on_case_id_in IN XHB_DEFENDANT_ON_CASE.defendant_on_case_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('def_on_case', 'XHB_DEFENDANT_ON_CASE_SEQ', p_defendant_on_case_id_in);
    END lookup_xhb_def_on_case_id;


    FUNCTION lookup_xhb_charge_id(p_charge_id_in IN XHB_CHARGE.charge_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('charge', 'XHB_CHARGE_SEQ', p_charge_id_in);
    END lookup_xhb_charge_id;


    FUNCTION lookup_xhb_defendant_charge_id(p_defendant_charge_id_in IN XHB_DEFENDANT_CHARGE.defendant_charge_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('def_charge', 'XHB_DEFENDANT_CHARGE_SEQ', p_defendant_charge_id_in);
    END lookup_xhb_defendant_charge_id;


    FUNCTION lookup_xhb_offence_id(p_offence_id_in IN XHB_OFFENCE.offence_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('offence', 'XHB_OFFENCE_SEQ', p_offence_id_in);
    END lookup_xhb_offence_id;


    FUNCTION lookup_xhb_def_on_offence_id(p_defendant_on_offence_id_in IN XHB_DEFENDANT_ON_OFFENCE.offence_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('def_on_offence', 'XHB_DEFENDANT_ON_OFFENCE_SEQ', p_defendant_on_offence_id_in);
    END lookup_xhb_def_on_offence_id;


    FUNCTION lookup_xhb_sched_def_id(p_sched_hearing_def_id_in IN XHB_SCHED_HEARING_DEFENDANT.sched_hear_def_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('sched_hear_def', 'XHB_SCHEDULED_HEARING_DEF_SEQ', p_sched_hearing_def_id_in);
    END lookup_xhb_sched_def_id;


    FUNCTION lookup_xhb_breach_id(p_breach_id_in IN XHB_BREACH.breach_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('breach', 'XHB_BREACH_SEQ', p_breach_id_in);
    END lookup_xhb_breach_id;


    FUNCTION lookup_xhb_disposal2_id(p_disposal2_id_in IN XHB_DISPOSAL2.disposal2_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('disposal2', 'XHB_DISPOSAL2_SEQ', p_disposal2_id_in);
    END lookup_xhb_disposal2_id;


    FUNCTION lookup_xhb_clob_id(p_clob_id_in IN XHB_CLOB.clob_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('clob', 'XHB_CLOB_SEQ', p_clob_id_in);
    END lookup_xhb_clob_id;


    FUNCTION lookup_xhb_xml_document_id(p_xml_document_id_in IN XHB_XML_DOCUMENT.xml_document_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('xml_document', 'XHB_XML_DOCUMENT_SEQ', p_xml_document_id_in);
    END lookup_xhb_xml_document_id;


    FUNCTION lookup_xhb_wll_control_id(p_wll_control_id_in IN XHB_WLL_CONTROL.wll_control_id%TYPE) RETURN NUMBER IS
    BEGIN
        RETURN get_id_lookup('wll_control', 'XHB_WLL_CONTROL_SEQ', p_wll_control_id_in);
    END lookup_xhb_wll_control_id;



    PROCEDURE populate_tables(p_court_id_in IN XHB_COURT.court_id%TYPE) IS
    BEGIN
        -- first clear out our cache...
        g_array_lookup_cache_t.DELETE;

        INSERT INTO xhb_hearing_list
        SELECT training_utils_pkg.lookup_xhb_hearing_list_id(list_id) AS list_id,
               list_type,
               start_date,
               end_date,
               status,
               edition_no,
               published_time,
               print_reference,
               crest_list_id,
               p_court_id_in AS court_id,
               list_court_type,
               NULL AS last_update_date,
               NULL AS creation_date,
               NULL AS created_by,
               NULL AS last_updated_by,
               NULL AS version
        FROM   EXT_XHB_HEARING_LIST;

        INSERT INTO XHB_SITTING
        SELECT training_utils_pkg.lookup_xhb_sitting_id(sitting_id) AS sitting_id,
               SITTING_SEQUENCE_NO,
               IS_SITTING_JUDGE,
               sitting_time,
               SITTING_NOTE,
               training_utils_pkg.get_ref_justice_id(p_court_id_in, crest_justice1_id) AS ref_justice1_id,
               training_utils_pkg.get_ref_justice_id(p_court_id_in, crest_justice2_id) AS ref_justice2_id,
               training_utils_pkg.get_ref_justice_id(p_court_id_in, crest_justice3_id) AS ref_justice3_id,
               training_utils_pkg.get_ref_justice_id(p_court_id_in, crest_justice4_id) AS ref_justice4_id,
               IS_FLOATING,
               training_utils_pkg.lookup_xhb_hearing_list_id(list_id) AS list_id,
               training_utils_pkg.get_ref_judge_id(p_court_id_in, crest_judge_id) AS ref_judge_id,
               training_utils_pkg.GET_COURT_ROOM_ID(p_court_id_in, crest_court_room_no) AS court_room_id,
               training_utils_pkg.get_court_site_id(p_court_id_in) AS court_site_id,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               JUSTICENAME4,
               JUSTICENAME3,
               JUSTICENAME2,
               JUSTICENAME1
        FROM   EXT_XHB_SITTING;

        INSERT INTO XHB_CASE
        SELECT training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               CASE_NUMBER,
               CASE_TYPE,
               MAG_CONVICTION_DATE,
               CASE_SUB_TYPE,
               CASE_TITLE,
               CASE_DESCRIPTION,
               NULL AS LINKED_CASE_ID,
               BAIL_MAG_CODE,
               training_utils_pkg.get_ref_court_id(p_court_id_in, COURT_SHORT_NAME) AS REF_COURT_ID,
               p_court_id_in AS COURT_ID,
               CHARGE_IMPORT_INDICATOR,
               SEVERED_IND,
               INDICT_RESP,
               DATE_IND_REC,
               PROS_AGENCY_REFERENCE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               CASE_CLASS,
               JUDGE_REASON_FOR_APPEAL,
               RESULTS_VERIFIED,
               LENGTH_TAPE,
               NO_PAGE_PROS_EVIDENCE,
               NO_PROS_WITNESS,
               EST_PDH_TRIAL_LENGTH,
               INDICTMENT_INFO_1,
               INDICTMENT_INFO_2,
               INDICTMENT_INFO_3,
               INDICTMENT_INFO_4,
               INDICTMENT_INFO_5,
               INDICTMENT_INFO_6,
               POLICE_OFFICER_ATTENDING,
               CPS_CASE_WORKER,
               EXPORT_CHARGES,
               IND_CHANGE_STATUS,
               MAGISTRATES_CASE_REF,
               CLASS_CODE,
               OFFENCE_GROUP_UPDATE,
               NULL AS CCC_TRANS_TO_REF_COURT_ID,
               RECEIPT_TYPE
        FROM   EXT_XHB_CASE;

        INSERT INTO XHB_HEARING
        SELECT training_utils_pkg.lookup_xhb_hearing_id(HEARING_ID) AS HEARING_ID,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               training_utils_pkg.get_ref_hearing_type_id(p_court_id_in, HEARING_TYPE_CODE, training_utils_pkg.get_case_type(CASE_ID)) AS REF_HEARING_TYPE_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               MP_HEARING_TYPE,
               LAST_CALCULATED_DURATION,
               HEARING_START_DATE,
               HEARING_END_DATE,
               NULL AS LINKED_HEARING_ID
        FROM   EXT_XHB_HEARING;

        INSERT INTO XHB_SCHEDULED_HEARING
        SELECT training_utils_pkg.LOOKUP_XHB_SCHED_HEARING_ID(SCHEDULED_HEARING_ID) AS SCHEDULED_HEARING_ID,
               SEQUENCE_NO,
               NOT_BEFORE_TIME,
               ORIGINAL_TIME,
               LISTING_NOTE,
               HEARING_PROGRESS,
               training_utils_pkg.LOOKUP_XHB_SITTING_ID(SITTING_ID) AS SITTING_ID,
               training_utils_pkg.LOOKUP_XHB_HEARING_ID(HEARING_ID) AS HEARING_ID,
               MOVED_FROM,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               NULL AS LINKED_SH_ID,
               NULL AS END_TIME, --END_TIME,
               NULL AS START_TIME, --START_TIME,
               DATE_OF_HEARING,
               IS_CASE_ACTIVE,
               NULL AS MOVED_FROM_COURT_ROOM_ID
        FROM   EXT_XHB_SCHEDULED_HEARING;

        INSERT INTO XHB_ADDRESS
        SELECT training_utils_pkg.LOOKUP_XHB_ADDRESS_ID(ADDRESS_ID) AS ADDRESS_ID,
               ADDRESS_1,
               ADDRESS_2,
               ADDRESS_3,
               ADDRESS_4,
               TOWN,
               COUNTY,
               POSTCODE,
               COUNTRY,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_ADDRESS exa
        -- Only need to re-load a very small amount of data...
        WHERE  EXISTS (SELECT 1 FROM EXT_XHB_DEFENDANT exd WHERE exa.address_id = exd.address_id);

        INSERT INTO XHB_DEFENDANT
        SELECT training_utils_pkg.lookup_xhb_defendant_id(DEFENDANT_ID) AS DEFENDANT_ID,
               CREST_DEFENDANT_ID,
               FIRST_NAME,
               MIDDLE_NAME,
               SURNAME,
               INITIALS,
               DATE_OF_BIRTH,
               GENDER,
               LAST_CONVICTION_DATE, 
               IS_COMPANY,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               training_utils_pkg.lookup_xhb_address_id(ADDRESS_ID) AS ADDRESS_ID,
               p_court_id_in AS court_id,
               NULL AS PRISON_ID 
        FROM   EXT_XHB_DEFENDANT;

        INSERT INTO XHB_DEFENDANT_ON_CASE
        SELECT training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               NO_OF_TICS,
               FINAL_DRIVING_LICENCE_STATUS,
               PTIURN,
               IS_JUVENILE,
               IS_MASKED,
               MASKED_NAME,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               training_utils_pkg.lookup_xhb_defendant_id(DEFENDANT_ID) AS DEFENDANT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND,
               RESULTS_VERIFIED,
               DEFENDANT_NUMBER,
               DATE_OF_COMMITTAL,
               PNC_ID,
               COLLECT_MAGISTRATE_COURT_ID
        FROM   EXT_XHB_DEFENDANT_ON_CASE;

        INSERT INTO XHB_CHARGE
        SELECT training_utils_pkg.lookup_xhb_charge_id(CHARGE_ID) AS CHARGE_ID,
               CHARGE_TYPE,
               PROS_PAPER_SERVED_DATE,
               CREST_CHARGE_ID,
               CREST_CHARGE_SEQ_NO,
               training_utils_pkg.get_ref_system_code_id(p_court_id_in, CODE, CODE_TYPE) AS REF_SYSTEM_CODE_ID,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND,
               IND_SIGNED_DATE
        FROM   EXT_XHB_CHARGE;

        INSERT INTO XHB_DEFENDANT_CHARGE
        SELECT training_utils_pkg.lookup_xhb_defendant_charge_id(DEFENDANT_CHARGE_ID) AS DEFENDANT_CHARGE_ID,
               training_utils_pkg.lookup_xhb_charge_id(CHARGE_ID) AS CHARGE_ID,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               NULL AS LAST_UPDATED_BY,
               NULL AS CREATED_BY,
               NULL AS CREATION_DATE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS VERSION,
               OBS_IND
        FROM   EXT_XHB_DEFENDANT_CHARGE;

        INSERT INTO XHB_OFFENCE
        SELECT training_utils_pkg.lookup_xhb_offence_id(OFFENCE_ID) AS OFFENCE_ID,
               CREST_OFFENCE_ID,
               CREST_OFFENCE_SEQ_NO,
               CREST_OFFENCE_FREETEXT,
               MULTIPLE,
               CREST_HOO_CLASS_FREETEXT,
               CREST_HOO_SUBCLASS_FREETEXT,
               training_utils_pkg.get_ref_offence_id(p_court_id_in, OFFENCE_CODE) AS REF_OFFENCE_ID,
               training_utils_pkg.lookup_xhb_charge_id(CHARGE_ID) AS CHARGE_ID,
               OBS_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               training_utils_pkg.get_ref_system_code_id(p_court_id_in, CODE, CODE_TYPE) AS REF_SYSTEM_CODE_ID
        FROM   EXT_XHB_OFFENCE;

        INSERT INTO XHB_DEFENDANT_ON_OFFENCE
        SELECT training_utils_pkg.lookup_xhb_def_on_offence_id(DEFENDANT_ON_OFFENCE_ID) AS DEFENDANT_ON_OFFENCE_ID,
               APPEAL_AGAINST_TYPE,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               training_utils_pkg.lookup_xhb_offence_id(OFFENCE_ID) AS OFFENCE_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND,
               IS_STAYED,
               CRN_ID,
               VCO_FLAG,
               VCO_DATE
        FROM   EXT_XHB_DEF_ON_OFFENCE;

        INSERT INTO XHB_COURT_LOG_ENTRY
        SELECT XHB_COURT_LOG_ENTRY_SEQ.nextVal,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               NULL AS VERSION,
               NULL AS LAST_UPDATED_BY,
               NULL AS CREATED_BY,
               NULL AS CREATION_DATE,
               NULL AS LAST_UPDATE_DATE,
               DATE_TIME,
               EVENT_DESC_ID,
               LOG_ENTRY_XML,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               training_utils_pkg.lookup_xhb_def_on_offence_id(DEFENDANT_ON_OFFENCE_ID) AS DEFENDANT_ON_OFFENCE_ID,
               training_utils_pkg.lookup_xhb_sched_hearing_id(SCHEDULED_HEARING_ID) AS SCHEDULED_HEARING_ID
        FROM   EXT_XHB_COURT_LOG_ENTRY;

        INSERT INTO XHB_SCHED_HEARING_DEFENDANT
        SELECT training_utils_pkg.lookup_xhb_sched_def_id(SCHED_HEAR_DEF_ID) AS SCHED_HEAR_DEF_ID,
               training_utils_pkg.lookup_xhb_sched_hearing_id(SCHEDULED_HEARING_ID) AS SCHEDULED_HEARING_ID,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_SCHED_HEARING_DEF;

        INSERT INTO XHB_BREACH
        SELECT training_utils_pkg.lookup_xhb_breach_id(BREACH_ID) AS BREACH_ID,
               ORIGINAL_SENTENCE,
               ORIGINAL_SENTENCE_DATE,
               ORIGINAL_COURT_TYPE,
               DATE_PUT,
               BREACH_TYPE,
               BRING_BACK,
               training_utils_pkg.lookup_xhb_charge_id(CHARGE_ID) AS CHARGE_ID,
               training_utils_pkg.get_ref_court_id(p_court_id_in, court_short_name) AS REF_COURT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               OBS_IND
        FROM   EXT_XHB_BREACH;

/*
        INSERT INTO XHB_CONTACT_DETAIL
        SELECT XHB_CONTACT_DETAIL_SEQ.NEXTVAL,
               CONTACT_TYPE,
               CONTACT_VALUE,
               EMAIL_FORMAT,
               PAGER_NET,
               training_utils_pkg.LOOKUP_XHB_ADDRESS_ID(ADDRESS_ID) AS ADDRESS_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_CONTACT_DETAIL;
*/

        INSERT INTO XHB_DEFENDANT_REFERENCE
        SELECT XHB_DEFENDANT_REFERENCE_SEQ.NEXTVAL,
               REFERENCE_VALUE,
               REFERENCE_NAME,
               CATEGORY,
               training_utils_pkg.lookup_xhb_defendant_id(DEFENDANT_ID) AS DEFENDANT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_DEFENDANT_REFERENCE;

        INSERT INTO XHB_CASE_PROSECUTOR_AGENCY
        SELECT XHB_CASE_PROSECTOR_AGENCY_SEQ.NEXTVAL,
               PROSECUTOR_TYPE,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               training_utils_pkg.get_ref_prosecutor_agency_id(p_court_id_in, CREST_OPPOSER_ID) AS REF_PROSECUTOR_AGENCY_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_CASE_PROSECUTOR_AGENCY;

        INSERT INTO XHB_PLEA
        SELECT XHB_PLEA_SEQ.NEXTVAL,
               training_utils_pkg.get_ref_system_code_id(p_court_id_in, CODE, CODE_TYPE) AS REF_PLEA_ID,
               OTHER_PLEA_TEXT,
               BREACH_ADMITTED,
               training_utils_pkg.get_ref_offence_id(p_court_id_in, offence_code) AS ALT_REF_OFFENCE_ID,
               ARRAIGNMENT_DATE,
               DEF_ON_CHARGE_OR_OFFENCE,
               OBS_IND,
               training_utils_pkg.lookup_xhb_defendant_charge_id(DEFENDANT_CHARGE_ID) AS DEFENDANT_CHARGE_ID,
               training_utils_pkg.lookup_xhb_def_on_offence_id(DEFENDANT_ON_OFFENCE_ID) AS DEFENDANT_ON_OFFENCE_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               ALT_UNCODED_OFFENCE_DESC
        FROM   EXT_XHB_PLEA;

        INSERT INTO XHB_VERDICT
        SELECT XHB_VERDICT_SEQ.NEXTVAL,
               OBS_IND,
               DEF_ON_CHARGE_OR_OFFENCE,
               JURORS_DISSENTING,
               JURORS_ASSENTING,
               training_utils_pkg.get_ref_offence_id(p_court_id_in, offence_code) AS ALT_REF_OFFENCE_ID,
               VERDICT_DATE,
               training_utils_pkg.get_ref_system_code_id(p_court_id_in, CODE, CODE_TYPE) AS REF_VERDICT_ID,
               NULL AS VERSION,
               NULL AS LAST_UPDATED_BY,
               OTHER_VERDICT_TEXT,
               NULL AS CREATED_BY,
               NULL AS CREATION_DATE,
               NULL AS LAST_UPDATE_DATE,
               training_utils_pkg.lookup_xhb_def_on_offence_id(DEFENDANT_ON_OFFENCE_ID) AS DEFENDANT_ON_OFFENCE_ID,
               training_utils_pkg.lookup_xhb_defendant_charge_id(DEFENDANT_CHARGE_ID) AS DEFENDANT_CHARGE_ID,
               training_utils_pkg.get_ref_app_result_id(p_court_id_in, app_result_code) AS REF_APP_RESULT_ID,
               training_utils_pkg.lookup_xhb_case_id(CASE_ID) AS CASE_ID,
               APP_LESSER_OFF,
               ALT_UNCODED_OFFENCE_DESC,
               training_utils_pkg.lookup_xhb_disposal2_id(DISPOSAL2_ID),
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID)
        FROM   EXT_XHB_VERDICT;

        INSERT INTO XHB_DISPOSAL2
        SELECT training_utils_pkg.lookup_xhb_disposal2_id(DISPOSAL2_ID) AS DISPOSAL2_ID,
               training_utils_pkg.get_ref_disposal_type_id(p_court_id_in, template_version, disposal_code, menu_group) AS REF_DISPOSAL_TYPE_ID,
               training_utils_pkg.lookup_xhb_def_on_offence_id(DEFENDANT_ON_OFFENCE_ID) AS DEFENDANT_ON_OFFENCE_ID,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               DIS_ID,
               COURT_TYPE,
               training_utils_pkg.lookup_xhb_disposal2_id(PSD_DISPOSAL2_ID) AS PSD_DISPOSAL2_ID,
               OBS_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_DISPOSAL2 xd2;

        INSERT INTO XHB_DISPOSAL_LINE
        SELECT XHB_DISPOSAL_LINE_SEQ.NEXTVAL,
               training_utils_pkg.get_ref_disposal_line_id(p_court_id_in, disposal_code, template_version, dil_seq_no) AS REF_DISPOSAL_LINE_ID,
               training_utils_pkg.lookup_xhb_disposal2_id(DISPOSAL2_ID) AS DISPOSAL2_ID,
               LINE_NUMBER,
               DATA,
               DEL_DATA,
               DEL_G1,
               DEL_G2,
               OBS_IND,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
        FROM   EXT_XHB_DISPOSAL_LINE xdl;

		INSERT INTO XHB_CLOB
		SELECT training_utils_pkg.lookup_xhb_clob_id(CLOB_ID),
			   CLOB_DATA,
			   NULL AS LAST_UPDATE_DATE,
			   NULL AS CREATION_DATE,
			   NULL AS CREATED_BY,
			   NULL AS LAST_UPDATED_BY,
               NULL AS VERSION
		FROM   EXT_XHB_CLOB;
		
        INSERT INTO XHB_XML_DOCUMENT
        SELECT training_utils_pkg.lookup_xhb_xml_document_id(XML_DOCUMENT_ID) AS XML_DOCUMENT_ID,
               DATE_CREATED,
               DOCUMENT_TITLE,
               STATUS,
               EXPIRY_DATE,
               DOCUMENT_TYPE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               p_court_id_in AS COURT_ID,
               training_utils_pkg.lookup_xhb_clob_id(XML_DOCUMENT_CLOB_ID)
        FROM   EXT_XHB_XML_DOCUMENT;

        INSERT INTO XHB_WLL_CONTROL
        SELECT training_utils_pkg.lookup_xhb_wll_control_id(WLL_CONTROL_ID) AS WLL_CONTROL_ID,
               STATUS,
               EXPIRY_DATE,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               training_utils_pkg.lookup_xhb_xml_document_id(XML_DOCUMENT_ID) AS XML_DOCUMENT_ID
        FROM   EXT_XHB_WLL_CONTROL;

        INSERT INTO XHB_WLL_DOCUMENT
        SELECT XHB_WLL_DOCUMENT_SEQ.NEXTVAL AS WLL_DOCUMENT_ID,
               NULL AS LAST_UPDATE_DATE,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               NULL AS VERSION,
               NULL WLL_RECIPIENT_ID,
               training_utils_pkg.lookup_xhb_wll_control_id(WLL_CONTROL_ID) AS WLL_CONTROL_ID,
               training_utils_pkg.lookup_xhb_xml_document_id(XML_DOCUMENT_ID) AS XML_DOCUMENT_ID
        FROM   EXT_XHB_WLL_DOCUMENT;

        INSERT INTO XHB_ORDER
        SELECT XHB_ORDER_SEQ.NEXTVAL AS ORDER_ID,
               DELIVERY_DATE,
               SIGNED_BY,
               SIGNING_DATE,
               DATA_XML,
               ORDER_DELIVERY_STATUS_ID,
               ORDER_STATUS_ID,
               ORDER_TEMPLATE_ID,
               NULL AS VERSION,
               NULL AS LAST_UPDATE_DATE,
               training_utils_pkg.lookup_xhb_def_on_case_id(DEFENDANT_ON_CASE_ID) AS DEFENDANT_ON_CASE_ID,
               NULL AS CREATION_DATE,
               NULL AS CREATED_BY,
               NULL AS LAST_UPDATED_BY,
               DESCRIPTION
        FROM   EXT_XHB_ORDER;

        -- And finish off by clearing the cache...
        g_array_lookup_cache_t.DELETE;
    END;
END training_utils_pkg;
/
show errors

CREATE OR REPLACE PACKAGE training_list_pkg AS

  /*
   * Get daily list selection from CREST
   */

  PROCEDURE get_tomorrows_list(p_results_out OUT SYS_REFCURSOR,
                               p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                               p_date        IN  DATE);

  PROCEDURE get_warned_list(p_results_out OUT SYS_REFCURSOR,
                            p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                            p_date        IN  DATE);

  PROCEDURE get_daily_list_dates(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id   IN  XHB_COURT.COURT_ID%TYPE);

  PROCEDURE amend_daily_list_date(p_results_out OUT SYS_REFCURSOR,
                                  p_court_id    IN   XHB_COURT.COURT_ID%TYPE,
                                  p_list_id     IN   XHB_HEARING_LIST.LIST_ID%TYPE,
                                  p_date        IN   DATE);

END training_list_pkg;
/
show errors

CREATE OR REPLACE PACKAGE BODY training_list_pkg AS
    /*
     *
     * GET_XML_CLOB
     *
     * A package-private utility method to extract the common code for loading
     * a CLOB from a file on the local database server.
     *
     * This function loads the specified file from the default directory for
     * the training application and returns it as a CLOB.
     *
     */
    FUNCTION get_xml_clob(p_file_name_in IN VARCHAR2) RETURN CLOB IS
        daily_list_bfile BFILE := BFILENAME('DBDIR', p_file_name_in);
        daily_list CLOB := ' ';
        destination_offset INTEGER := 1;
        source_offset INTEGER := 1;
        language_context INTEGER := DBMS_LOB.default_lang_ctx;
        warning_message INTEGER;
    BEGIN
        DBMS_LOB.OPEN(daily_list_bfile);
        DBMS_LOB.OPEN(daily_list, DBMS_LOB.LOB_READWRITE);

        DBMS_LOB.loadClobFromFile(daily_list, daily_list_bfile, DBMS_LOB.LOBMAXSIZE,
        destination_offset, source_offset, NLS_CHARSET_ID('UTF8'),
        language_context, warning_message);

        DBMS_LOB.CLOSE(daily_list_bfile);
        DBMS_LOB.CLOSE(daily_list);

        -- Check for the only possible warning message...
        IF (warning_message = DBMS_LOB.WARN_INCONVERTIBLE_CHAR) THEN
            DBMS_OUTPUT.put_line('Warning! Some characters could not be converted');
        END IF;

        RETURN ltrim(daily_list);
    END get_xml_clob;


    /*
     *
     * GET_TOMORROWS_LIST
     *
     * This procedure will obtain the available list from an OS souce
     * file on the Xhibit database server and load it into the
     * XHB_XML_DOCUMENT_TABLE.
     *
     */
    PROCEDURE get_tomorrows_list(p_results_out OUT SYS_REFCURSOR,
                                 p_court_id    IN  XHB_COURT.COURT_ID%TYPE,
                                 p_date        IN  DATE)
    IS
        doc_filename  VARCHAR2(256);
        status_count  NUMBER;
        list_count    NUMBER;
        xml_clob      CLOB;
		l_court_name  VARCHAR2(100);
		l_xml         XMLType;	
		l_clob_id     NUMBER;
	BEGIN
        SELECT count(*)
        INTO   status_count
        FROM   xhb_training_status
        WHERE  court_id = p_court_id;

        IF status_count > 0 THEN
            -- There are rows for this court.
            -- Check that there is no purge or load in progress
            SELECT count(*)
            INTO   status_count
            FROM   xhb_training_status
            WHERE  court_id = p_court_id
            AND    (operation_code = 'P' OR operation_code = 'L')
            AND    (status_code = 'R' OR status_code = 'I');

            IF status_count > 0 THEN
                -- Database schema being purged so no actions allowed
                RAISE_APPLICATION_ERROR(-20007,'Purge/Load cycle already in progress');
            END IF;
        END IF;

        SELECT count(*)
        INTO   list_count
        FROM   xhb_xml_document
        WHERE  court_id = p_court_id
        AND    document_type = 'DL'
        AND    date_created = p_date;

        IF list_count > 0 THEN
            -- A list for tomorrow is already loaded into the XHB_XML_DOCUMENT table
            RAISE_APPLICATION_ERROR(-20002,'There is already a tomorrows list for this date');
        END IF;

	    SELECT COURT_NAME
		INTO   l_court_name
		FROM   XHB_COURT
		WHERE  COURT_ID = p_court_id;

		-- First add tomorrows list
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'TOMORROWS_LIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

		xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);
    
		SELECT UPDATEXML(l_xml, '/cs:DailyList/cs:ListHeader/cs:StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
						 		'/cs:DailyList/cs:ListHeader/cs:EndDate/text()',   TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/cs:DailyList/cs:CrownCourt/cs:CourtHouseName/text()', l_court_name, 
								'/cs:DailyList/cs:CourtLists/cs:CourtList/cs:CourtHouse/cs:CourtHouseName/text()', l_court_name,
								'/cs:DailyList/cs:ListHeader/cs:PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
								
						 )
	    INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID)
                              VALUES (p_date,
                                     'Daily List FINAL v1 ' || to_char(p_date, 'YYYY-MM-DD HH24:MI:SS'),
                                     'ND',
                                     NULL,
                                     'DL',
                                     p_court_id,
                                     l_clob_id);

		-- Now add a daily list for distribution
		SELECT value2
		INTO   doc_filename
		FROM   xhb_training_ref_data
		WHERE  parameter = 'DAILY_LIST_DIST'
		AND    court_id = p_court_id;

		IF doc_filename IS NULL THEN
			RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
		END IF;

		xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);

		SELECT UPDATEXML(l_xml, '/DailyList/ListHeader/StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/DailyList/ListHeader/EndDate/text()',   TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/DailyList/CrownCourt/CourtHouseName/text()', l_court_name, 
								'/DailyList/CrownCourt/CourtHouseAddress/Line[5]/text()', l_court_name,
								'/DailyList/CrownCourt/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/DailyList/CourtLists/CourtList/CourtHouse/CourtHouseName/text()', l_court_name,
								'/DailyList/ListHeader/PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 

						 )
		INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

		INSERT INTO XHB_CLOB
			   (clob_id, clob_data)
		VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
		RETURNING clob_id
		INTO      l_clob_id;

		INSERT INTO xhb_xml_document (XML_DOCUMENT_ID,
                                      DATE_CREATED,
									  DOCUMENT_TITLE,
									  STATUS,
									  EXPIRY_DATE,
									  DOCUMENT_TYPE,
									  COURT_ID,
									  XML_DOCUMENT_CLOB_ID)
							  VALUES (XHB_XML_DOCUMENT_SEQ.nextval,
                                      p_date,
									 'Daily List FINAL v1 ' || to_char(p_date, 'YYYY-MM-DD HH24:MI:SS'),
									 'ND',
									 NULL,
									 'DLD',
									 p_court_id,
									 l_clob_id);

        INSERT INTO xhb_wll_control (STATUS,
                                     EXPIRY_DATE,
                                     XML_DOCUMENT_ID)
                             VALUES ('ND',
                                     sysdate + 1,
                                     XHB_XML_DOCUMENT_SEQ.currval);

        OPEN p_results_out FOR
            SELECT *
            FROM   xhb_training_status
            WHERE  court_id = -1;
    END get_tomorrows_list;


    /*
     *
     * GET_WARNED_LIST
     *
     * This procedure will obtain the available list from an OS souce
     * file on the Xhibit database server and load it into the
     * XHB_XML_DOCUMENT_TABLE.
     *
     */
    PROCEDURE get_warned_list(p_results_out OUT SYS_REFCURSOR,
                              p_court_id    IN XHB_COURT.COURT_ID%TYPE,
                              p_date        IN DATE) IS
        doc_filename  VARCHAR2(256);
        status_count  NUMBER;
        list_count    NUMBER;
        xml_clob      CLOB;
		l_court_name  VARCHAR2(100);
		l_court_code  VARCHAR2(3);
		l_xml         XMLType;	
        l_clob_id     NUMBER;
    BEGIN
        SELECT count(*)
        INTO   status_count
        FROM   xhb_training_status
        WHERE  court_id = p_court_id;

        IF status_count > 0 THEN
            -- There are rows for this court.
            -- Check that there is no purge or load in progress
            SELECT count(*)
            INTO   status_count
            FROM   xhb_training_status
            WHERE  court_id = p_court_id
            AND    (operation_code = 'P' OR operation_code = 'L')
            AND    (status_code = 'R' OR status_code = 'I');

            IF status_count > 0 THEN
                -- Database schema being purged so no actions allowed
                RAISE_APPLICATION_ERROR(-20007,'Purge/Load cycle already in progress');
            END IF;
        END IF;

        SELECT count(*)
        INTO   list_count
        FROM   xhb_xml_document
        WHERE  court_id = p_court_id
        AND    document_type = 'WL'
        AND    date_created = p_date;

        IF list_count > 0 THEN
            -- A warned list is already loaded into the XHB_XML_DOCUMENT table
            RAISE_APPLICATION_ERROR(-20004,'There is already a warned list for the selected date');
        END IF;

	    SELECT COURT_NAME, CREST_COURT_ID
		INTO   l_court_name, l_court_code
		FROM   XHB_COURT
		WHERE  COURT_ID = p_court_id;

		-- Add the current warned list
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'WARNED_LIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

        xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);
    
		SELECT UPDATEXML(l_xml, '/cs:WarnedList/cs:ListHeader/cs:StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
						 		'/cs:WarnedList/cs:ListHeader/cs:EndDate/text()',   TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/cs:WarnedList/cs:CrownCourt/cs:CourtHouseName/text()', l_court_name, 
								'/cs:WarnedList/cs:CourtLists/cs:CourtList/cs:CourtHouse/cs:CourtHouseName/text()', l_court_name,
								'/cs:WarnedList/cs:ListHeader/cs:PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
								
						 )
	    INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID)
                              VALUES (p_date,
                                      'Warned List FINAL v1 ' || to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'),
                                      'ND',
                                      NULL,
                                      'WL',
                                      p_court_id,
                                      l_clob_id);

		-- Add the warned list for distribution (ie warned list letters)
        SELECT value2
        INTO   doc_filename
        FROM   xhb_training_ref_data
        WHERE  parameter = 'WARNED_LIST_DIST'
        AND    court_id = p_court_id;

        IF doc_filename IS NULL THEN
            RAISE_APPLICATION_ERROR(-20900, SQLCODE ||': '|| SQLERRM);
        END IF;

        xml_clob := get_xml_clob(doc_filename);
		l_xml := XMLType.CREATEXML(xml_clob);

		SELECT UPDATEXML(l_xml, '/WarnedList/ListHeader/StartDate/text()', TO_CHAR(p_date, 'YYYY-MM-DD'),
								'/WarnedList/ListHeader/EndDate/text()',   TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/WarnedList/CrownCourt/CourtHouseName/text()', l_court_name,
								'/WarnedList/CrownCourt/CourtHouseCode/text()', l_court_code,
								'/WarnedList/CrownCourt/CourtHouseAddress/Line[3]/text()', l_court_name,
								'/WarnedList/CrownCourt/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/WarnedList/WarnedListDetail/DeadLineDate/text()', TO_CHAR(p_date+14, 'YYYY-MM-DD'),
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseName/text()', 'at ' || l_court_name,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseCode/text()', l_court_code,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseAddress/Line[3]/text()', l_court_name,
								'/WarnedList/CourtLists/CourtList/CourtHouse/CourtHouseDX/text()', 'DX 97420 ' || l_court_name || ' 1',
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[1]/text()', 'at ' || l_court_name,
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[2]/text()', 'at ANNEXE - ' || l_court_name || ' SITE 2',
								'/WarnedList/CourtLists/CourtList/WarnedForCourts/WarnedForCourt[3]/text()', 'at VIRTUAL ANNEXE- ' || l_court_name || ' SITE 3',
								'/WarnedList/ListHeader/PublishedTime/text()', TO_CHAR(sysdate, 'YYYY-MM-DD') || 'T14:10:00' 
						 )
		INTO l_xml
		FROM dual;

		-- This section of code will add <?xml version="1.0" encoding="iso-8859-1"?> back 
		-- to the start of the xml_document if it is missing (UPDATEXML seems to strip it off).
		IF (DBMS_LOB.INSTR(xml_clob, '?>') > 0) THEN
		   IF (DBMS_LOB.INSTR(l_xml.getClobVal(), '?>') > 0) THEN
			   xml_clob := l_xml.getClobVal();
		   ELSE     
			   xml_clob := DBMS_LOB.SUBSTR(xml_clob, DBMS_LOB.INSTR(xml_clob, '?>') + 1);
			   DBMS_LOB.APPEND(xml_clob, l_xml.getClobVal());
		   END IF;
		ELSE
		   xml_clob := l_xml.getClobVal();
		END IF;

        INSERT INTO XHB_CLOB
               (clob_id, clob_data)
        VALUES (XHB_CLOB_SEQ.NEXTVAL, xml_clob)
        RETURNING clob_id
        INTO      l_clob_id;
		
        INSERT INTO xhb_xml_document (XML_DOCUMENT_ID,
                                      DATE_CREATED,
                                      DOCUMENT_TITLE,
                                      STATUS,
                                      EXPIRY_DATE,
                                      DOCUMENT_TYPE,
                                      COURT_ID,
                                      XML_DOCUMENT_CLOB_ID)
                              VALUES (XHB_XML_DOCUMENT_SEQ.nextval,
                                      p_date,
                                      'Warned List FINAL v1 ' || to_char(sysdate, 'YYYY-MM-DD HH24:MI:SS'),
                                      'ND',
                                      NULL,
                                      'WLD',
                                      p_court_id,
                                      l_clob_id);

        INSERT INTO xhb_wll_control (STATUS,
                                     EXPIRY_DATE,
                                     XML_DOCUMENT_ID)
                             VALUES ('ND',
                                     sysdate + 1,
                                     XHB_XML_DOCUMENT_SEQ.currval);

        OPEN p_results_out FOR
            SELECT *
            FROM   xhb_training_status
            WHERE  court_id = -1;
    END get_warned_list;


    /*
     *
     * GET_DAILY_LIST_DATES
     *
     */
    PROCEDURE get_daily_list_dates(p_results_out OUT SYS_REFCURSOR,
                                   p_court_id    IN  XHB_COURT.COURT_ID%TYPE) IS
    BEGIN
        OPEN p_results_out FOR
            SELECT  list_id,
                    list_type,
                    start_date,
                    end_date,
                    status,
                    edition_no,
                    published_time,
                    print_reference,
                    crest_list_id,
                    court_id,
                    list_court_type
            FROM    xhb_hearing_list
            WHERE   court_id = p_court_id;
    END get_daily_list_dates;


    /*
     *
     * AMEND_DAILT_LIST_DATE
     *
     */
    PROCEDURE amend_daily_list_date(p_results_out OUT SYS_REFCURSOR,
                                    p_court_id    IN   XHB_COURT.COURT_ID%TYPE,
                                    p_list_id     IN   XHB_HEARING_LIST.LIST_ID%TYPE,
                                    p_date        IN   DATE) IS
        daily_list_count   NUMBER;
        original_list_date DATE;
    BEGIN
        SELECT count(*)
        INTO   daily_list_count
        FROM   xhb_hearing_list
        WHERE  court_id = p_court_id
        AND    start_date = p_date;

        IF daily_list_count != 0 THEN
            RAISE_APPLICATION_ERROR(-20006, 'List already exists for that date');
        END IF;

        BEGIN
            SELECT start_date INTO original_list_date
            FROM   XHB_HEARING_LIST
            WHERE  list_id = p_list_id;

            UPDATE xhb_hearing_list
            SET    start_date = p_date,
                   end_date = p_date
            WHERE  list_id = p_list_id
            AND    court_id = p_court_id;

            UPDATE XHB_SCHEDULED_HEARING
            SET    original_time = TRUNC(p_date),
                   not_before_time = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')
                                     ||TO_CHAR(not_before_time,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  sitting_id IN (SELECT sitting_id
                                  FROM   XHB_SITTING
                                  WHERE  list_id = p_list_id);

            UPDATE XHB_COURT_LOG_ENTRY
            SET    DATE_TIME = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')||TO_CHAR(DATE_TIME,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  SCHEDULED_HEARING_ID IN (SELECT scheduled_hearing_id 
                                            FROM XHB_SCHEDULED_HEARING sh, XHB_SITTING s  
                                            WHERE sh.sitting_id = s.sitting_id
                                            AND   s.list_id = p_list_id);

            UPDATE XHB_COURT_LOG_ENTRY
            SET    DATE_TIME = TO_DATE(TO_CHAR(p_date,'DD/MON/YYYY')||TO_CHAR(DATE_TIME,'HH24:MI:SS'),'DD/MON/YYYYHH24:MI:SS')
            WHERE  TRUNC(DATE_TIME) = TRUNC(original_list_date)
            AND    scheduled_hearing_id IS NULL;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                RAISE_APPLICATION_ERROR(-20900,SQLCODE || ': ' || SQLERRM);
        END;

        OPEN p_results_out FOR
            SELECT  list_id,
                    list_type,
                    start_date,
                    end_date,
                    status,
                    edition_no,
                    published_time,
                    print_reference,
                    crest_list_id,
                    court_id,
                    list_court_type
            FROM    xhb_hearing_list
            WHERE   court_id = p_court_id
            AND     list_id  = p_list_id;
    END amend_daily_list_date;
END training_list_pkg;
/
show errors
