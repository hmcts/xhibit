CREATE OR REPLACE PACKAGE BODY Xhb_Public_Display_Pkg AS
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
                l_array(l_array.LAST) := TO_NUMBER(trim(SUBSTR(p_str_in, l_min_index, (l_max_index - l_min_index))));
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
        l_is_case_closed     BOOLEAN := FALSE;

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
                l_is_case_closed    := TRUE;
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
                MOVED_FROM_COURT_ROOM.DISPLAY_NAME AS MOVED_FROM_COURT_ROOM_NAME,
                COURT_ROOM.COURT_ROOM_ID AS COURT_ROOM_ID,
                MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
                SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
                LTRIM(DEFENDANT.FIRST_NAME) AS DEFENDANT_FIRST_NAME,
                LTRIM(DEFENDANT.MIDDLE_NAME) AS DEFENDANT_MIDDLE_NAME,
                LTRIM(DEFENDANT.SURNAME) AS DEFENDANT_SURNAME,
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
            FROM XHB_HEARING_LIST HEARING_LIST,
                XHB_SITTING SITTING,
                XHB_COURT_ROOM COURT_ROOM,
                XHB_COURT_SITE COURT_SITE,
                XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
                XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
                XHB_DEFENDANT DEFENDANT,
                XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
                XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
                XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
            AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
            AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID
            AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
            AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID
            AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
            AND HEARING_LIST.COURT_ID = COURT_ID_IN
            AND HEARING_LIST.START_DATE = START_DATE_IN
            AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
            AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
            AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
                MOVED_FROM_COURT_ROOM.COURT_ROOM_ID AS MOVED_FROM_COURT_ROOM_ID,
                SCHEDULED_HEARING.NOT_BEFORE_TIME AS NOT_BEFORE_TIME,
                LTRIM(DEFENDANT.FIRST_NAME) AS DEFENDANT_FIRST_NAME,
                LTRIM(DEFENDANT.MIDDLE_NAME) AS DEFENDANT_MIDDLE_NAME,
                LTRIM(DEFENDANT.SURNAME) AS DEFENDANT_SURNAME,
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
            FROM XHB_HEARING_LIST HEARING_LIST,
                XHB_SITTING SITTING,
                XHB_COURT_SITE COURT_SITE,
                XHB_COURT_ROOM COURT_ROOM,
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
                XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
                XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
                XHB_DEFENDANT DEFENDANT,
                XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
                XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
                XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
            AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
            AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID
            AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID
            AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID
            AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
            AND HEARING_LIST.COURT_ID = COURT_ID_IN
            AND HEARING_LIST.START_DATE = START_DATE_IN
            AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
            AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
            AND COURT_ROOM.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
            AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
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
                        TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
                AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
                AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
                AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
                AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
                AND HEARING.CASE_ID = CASE.CASE_ID
                AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
                AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
                AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
                AND Xhb_Custom_Pkg.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
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
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
                AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
                AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
                AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
                AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
                AND HEARING.CASE_ID = CASE.CASE_ID
                AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
                AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
                AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
                AND Xhb_Custom_Pkg.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
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
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
                AND Xhb_Custom_Pkg.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                CASE_REFERENCE.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS,
        SCHEDULED_HEARING.SCHEDULED_HEARING_ID AS SCHEDULED_HEARING_ID
            FROM  XHB_HEARING_LIST HEARING_LIST,
                XHB_SITTING SITTING,
                XHB_COURT_SITE COURT_SITE,
                XHB_COURT_ROOM COURT_ROOM,
                XHB_COURT_ROOM MOVED_FROM_COURT_ROOM,
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
                XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
                XHB_HEARING HEARING,
                XHB_CASE CASE,
                XHB_REF_HEARING_TYPE REF_HEARING_TYPE,
                XHB_DEFENDANT DEFENDANT,
                XHB_SCHED_HEARING_DEFENDANT SCHED_HEARING_DEFENDANT,
                XHB_DEFENDANT_ON_CASE DEFENDANT_ON_CASE,
                XHB_REF_JUDGE REF_JUDGE,
                XHB_CASE_REFERENCE CASE_REFERENCE,
                        TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
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
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
                AND Xhb_Custom_Pkg.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
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
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
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
            SELECT cs.court_site_name
                  ,cs.short_name
                  ,cs.court_site_code
                  ,cr.display_name court_room_name
                  ,cr.crest_court_room_no
                  ,v.defendant_id
                  ,v.defendant_first_name
                  ,v.defendant_middle_name
                  ,v.defendant_surname
                  ,v.case_number
                  ,v.case_title case_title
                  ,crls.status AS PUBLIC_DISPLAY_STATUS
                  ,crls.time_status_set AS TIME_STATUS_SET
                  ,v.reporting_restrictions
            FROM   XHB_COURT_SITE cs
                  ,XHB_COURT_ROOM cr
                  ,XHB_CR_LIVE_DISPLAY crls
                  ,TABLE(CAST(Xhb_Public_Display_Pkg.convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
                  ,(SELECT d.defendant_id AS defendant_id
                          ,D.FIRST_NAME AS DEFENDANT_FIRST_NAME
                          ,D.MIDDLE_NAME AS DEFENDANT_MIDDLE_NAME
                          ,D.SURNAME AS DEFENDANT_SURNAME
                          ,C.CASE_TYPE || C.CASE_NUMBER AS CASE_NUMBER
                          ,C.CASE_TITLE AS CASE_TITLE
                          ,cr.REPORTING_RESTRICTIONS AS REPORTING_RESTRICTIONS
                          ,sh.scheduled_hearing_id
                    FROM   XHB_HEARING_LIST hl
                          ,XHB_SITTING s
                          ,XHB_SCHEDULED_HEARING sh
                          ,XHB_HEARING h
                          ,XHB_CASE c
                          ,XHB_CASE_REFERENCE cr
                          ,XHB_SCHED_HEARING_DEFENDANT shd
                          ,XHB_DEFENDANT_ON_CASE doc
                          ,XHB_DEFENDANT d
                    WHERE hl.court_id              =  COURT_ID_IN
                    AND   hl.start_date            =  START_DATE_IN
                    AND   hl.list_id               =  s.list_id
                    AND   s.sitting_id             =  sh.sitting_id
                    AND   sh.is_case_active        = 'Y'
                    AND   sh.hearing_id            =  h.hearing_id
                    AND   h.case_id                =  c.case_id
                    AND   c.case_id                =  cr.case_id(+)
                    AND   sh.scheduled_hearing_id  =  shd.scheduled_hearing_id(+)
                    AND   shd.defendant_on_case_id =  doc.defendant_on_case_id(+)
                    AND   doc.defendant_id         =  d.defendant_id(+)
                    AND   (doc.obs_ind IS NULL OR doc.obs_ind <> 'Y')) v
            WHERE cs.court_id                 = COURT_ID_IN
            AND   cs.court_site_id            = cr.court_site_id
            AND   cr.court_room_id            = TMP_COURT_ROOM.COLUMN_VALUE
            AND   TMP_COURT_ROOM.COLUMN_VALUE = crls.court_room_id(+)
            AND   crls.scheduled_hearing_id   = v.scheduled_hearing_id(+) 
            ORDER BY COURT_SITE_CODE
                    ,CREST_COURT_ROOM_NO
                    ,DEFENDANT_SURNAME;
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
                CR_LIVE_DISPLAY.STATUS AS PUBLIC_DISPLAY_STATUS,
                CR_LIVE_DISPLAY.TIME_STATUS_SET AS TIME_STATUS_SET,
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
                XHB_CR_LIVE_DISPLAY CR_LIVE_DISPLAY,
                XHB_CASE_REFERENCE CASE_REFERENCE,
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
            AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
            AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
            AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
            AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
            AND HEARING.CASE_ID = CASE.CASE_ID
            AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
            AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
            AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
            AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
            AND CR_LIVE_DISPLAY.SCHEDULED_HEARING_ID = SCHEDULED_HEARING.SCHEDULED_HEARING_ID(+)
            AND CR_LIVE_DISPLAY.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID(+)
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
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE COURT_ROOM.COURT_ROOM_ID NOT IN (
                SELECT SITTING.COURT_ROOM_ID
                FROM XHB_HEARING_LIST HEARING_LIST,
                    XHB_SITTING SITTING,
                    XHB_SCHEDULED_HEARING SCHEDULED_HEARING,
                    XHB_CR_LIVE_DISPLAY CR_LIVE_DISPLAY
                WHERE
                    HEARING_LIST.LIST_ID = SITTING.LIST_ID
                    AND SITTING.SITTING_ID = SCHEDULED_HEARING.SITTING_ID
                    AND SCHEDULED_HEARING.IS_CASE_ACTIVE = 'Y'
                    AND HEARING_LIST.COURT_ID = COURT_ID_IN
                    AND HEARING_LIST.START_DATE = START_DATE_IN
                    AND CR_LIVE_DISPLAY.SCHEDULED_HEARING_ID = SCHEDULED_HEARING.SCHEDULED_HEARING_ID )
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
                CR_LIVE_DISPLAY.STATUS AS PUBLIC_DISPLAY_STATUS,
                CR_LIVE_DISPLAY.TIME_STATUS_SET AS TIME_STATUS_SET,
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
                XHB_CR_LIVE_DISPLAY CR_LIVE_DISPLAY,
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
            AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
            AND Xhb_Custom_Pkg.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
            AND CR_LIVE_DISPLAY.SCHEDULED_HEARING_ID(+) = SCHEDULED_HEARING.SCHEDULED_HEARING_ID
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
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
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
                AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
                AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
                AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
                AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
                AND HEARING.CASE_ID = CASE.CASE_ID
                AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
                AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
                AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
--              AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
                AND SITTING.IS_FLOATING = 0
) main_query,
  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY,
  XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
WHERE COURT_LOG_ENTRY.ROWID(+) = Xhb_Public_Display_Pkg.get_log_entry_rowid(main_query.scheduled_hearing_id, main_query.defendant_on_case_id)
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
                MOVED_FROM_COURT_SITE.SHORT_NAME AS MOVED_FROM_CS_SHORT_NAME, --MOVED SITE
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
                XHB_COURT_SITE MOVED_FROM_COURT_SITE, --MOVED SITE
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
                TABLE(CAST(convert_string(COURT_ROOM_IDS_IN) AS xhb_number_table_typ)) TMP_COURT_ROOM
            WHERE HEARING_LIST.LIST_ID = SITTING.LIST_ID
                AND SITTING.COURT_SITE_ID = COURT_SITE.COURT_SITE_ID
                AND SCHEDULED_HEARING.SITTING_ID = SITTING.SITTING_ID
                AND SCHEDULED_HEARING.HEARING_ID = HEARING.HEARING_ID
                AND HEARING.REF_HEARING_TYPE_ID = REF_HEARING_TYPE.REF_HEARING_TYPE_ID
                AND HEARING.CASE_ID = CASE.CASE_ID
                AND SCHEDULED_HEARING.SCHEDULED_HEARING_ID = SCHED_HEARING_DEFENDANT.SCHEDULED_HEARING_ID(+)
                AND SCHED_HEARING_DEFENDANT.DEFENDANT_ON_CASE_ID = DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID(+)
                AND DEFENDANT_ON_CASE.DEFENDANT_ID = DEFENDANT.DEFENDANT_ID(+)
                AND (DEFENDANT_ON_CASE.OBS_IND IS NULL OR DEFENDANT_ON_CASE.OBS_IND <> 'Y')
--              AND XHB_CUSTOM_PKG.GET_REF_JUDGE_ID(SCHEDULED_HEARING.SCHEDULED_HEARING_ID) = REF_JUDGE.REF_JUDGE_ID(+)
                AND CASE.CASE_ID = CASE_REFERENCE.CASE_ID(+)
                AND HEARING_LIST.COURT_ID = COURT_ID_IN
                AND HEARING_LIST.START_DATE = START_DATE_IN
                AND SITTING.COURT_ROOM_ID = TMP_COURT_ROOM.COLUMN_VALUE
                AND SITTING.COURT_ROOM_ID = COURT_ROOM.COURT_ROOM_ID
                AND SCHEDULED_HEARING.MOVED_FROM_COURT_ROOM_ID = MOVED_FROM_COURT_ROOM.COURT_ROOM_ID(+)
                AND MOVED_FROM_COURT_ROOM.COURT_SITE_ID = MOVED_FROM_COURT_SITE.COURT_SITE_ID(+) --MOVED SITE
) main_query,
  XHB_COURT_LOG_ENTRY COURT_LOG_ENTRY,
  XHB_COURT_LOG_EVENT_DESC COURT_LOG_EVENT_DESC
WHERE COURT_LOG_ENTRY.ROWID(+) = Xhb_Public_Display_Pkg.get_log_entry_rowid(main_query.scheduled_hearing_id, main_query.defendant_on_case_id)
AND   COURT_LOG_EVENT_DESC.EVENT_DESC_ID(+) = COURT_LOG_ENTRY.EVENT_DESC_ID
ORDER BY COURT_SITE_CODE,
            IS_FLOATING,
            CREST_COURT_ROOM_NO,
            SITTING_SEQUENCE_NO,
            SCHEDULED_HEARING_TIME_SORT,
            SCHEDULED_HEARING_SEQUENCE_NO,
            ORDER_NAME;
    END GET_ALL_CASE_STATUS_U;

    PROCEDURE GET_VIP_DISPLAY_DOCS_FOR_SITE(
        RESULTS_OUT       OUT SYS_REFCURSOR,
        COURT_SITE_ID_IN       IN  XHB_COURT_SITE.court_site_id%TYPE) AS
    BEGIN
        OPEN RESULTS_OUT FOR
        SELECT 
            xdd.description_code,
            xdd.multiple_court_yn,
            xdd.LANGUAGE,
            xdd.country
        FROM
            XHB_DISPLAY_LOCATION xdl,  
            XHB_DISPLAY xd,
            XHB_ROTATION_SET_DD xrsdd,
            XHB_DISPLAY_DOCUMENT xdd
        WHERE
            xdl.court_site_id = COURT_SITE_ID_IN
        AND
            xdl.description_code = 'v_i_p'
        AND
            xd.display_location_id = xdl.display_location_id
        AND
            xrsdd.rotation_set_id = xd.rotation_set_id
        AND
            xdd.display_document_id = xrsdd.display_document_id                     
        ORDER BY
            xdd.description_code;
        
    END GET_VIP_DISPLAY_DOCS_FOR_SITE;
    
    PROCEDURE GET_VIP_COURT_ROOMS_FOR_SITE (
            RESULTS_OUT       OUT SYS_REFCURSOR,
            COURT_SITE_ID_IN       IN  XHB_COURT_SITE.court_site_id%TYPE) AS
        BEGIN
            OPEN RESULTS_OUT FOR
        SELECT 
            xcr.court_room_id,
            xcs.short_name,
            xcr.display_name,
            xd.show_unassigned_yn     
        FROM
            XHB_DISPLAY_LOCATION xdl,  
            XHB_DISPLAY xd,
            XHB_DISPLAY_COURT_ROOM xdcr,
            XHB_COURT_ROOM xcr,
            XHB_COURT_SITE xcs    
        WHERE
            xdl.court_site_id = COURT_SITE_ID_IN
        AND
            xdl.description_code = 'v_i_p'
        AND
            xd.display_location_id = xdl.display_location_id
        AND
            xdcr.display_id = xd.display_id
        AND
            xcr.court_room_id = xdcr.court_room_id
        AND
            xcs.court_site_id = xcr.court_site_id        
        ORDER BY
            xcs.short_name,
            xcr.crest_court_room_no;
           
    END GET_VIP_COURT_ROOMS_FOR_SITE ;
        
END Xhb_Public_Display_Pkg;
/
show errors