package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query;

/**
 * 
 * This interface enumerates the column names
 * 
 * @author pznwc5
 * 
 */
public interface ColumnNames {
    /** Court room name */
    String COURT_ROOM_NAME = "COURT_ROOM_NAME";

    /** Court room id */
    String COURT_ROOM_ID = "COURT_ROOM_ID";

    /** Case number */
    String CASE_NUMBER = "CASE_NUMBER";

    /** Live status */
    String PUBLIC_DISPLAY_STATUS = "PUBLIC_DISPLAY_STATUS";

    /** live status time */
    String TIME_STATUS_SET = "TIME_STATUS_SET";

    /** Hearing type */
    String HEARING_DESCRIPTION = "HEARING_DESCRIPTION";

    /** Hearing progress */
    String HEARING_PROGRESS = "HEARING_PROGRESS";

    /** Hearing progress */
    String REPORTING_RESTRICTIONS = "REPORTING_RESTRICTIONS";

    /** Column name for moved from court room */
    String MOVED_FROM_CS_SHORT_NAME = "MOVED_FROM_CS_SHORT_NAME";

    /** Column name for moved from court room */
    String MOVED_FROM_COURT_ROOM_NAME = "MOVED_FROM_COURT_ROOM_NAME";

    /** Column name for moved from court id */
    String MOVED_FROM_COURT_ROOM_ID = "MOVED_FROM_COURT_ROOM_ID";

    /** Column name for moved from court id */
    String LIST_COURT_ROOM_ID = "LIST_COURT_ROOM_ID";

    /** Column name for not before time */
    String NOT_BEFORE_TIME = "NOT_BEFORE_TIME";

    /** Public notice desc */
    String PUBLIC_NOTICE_DESC = "PUBLIC_NOTICE_DESC";

    /** Public notice active */
    String IS_ACTIVE = "IS_ACTIVE";

    /** Hearing type */
    String IS_FLOATING = "IS_FLOATING";

    /** Public notice priority */
    String PRIORITY = "PRIORITY";

    /** Case title */
    String CASE_TITLE = "CASE_TITLE";

    /** Scheduled hearing id */
    String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    /** Court Log Entry XML */
    String COURT_LOG_ENTRY = "COURT_LOG_ENTRY";

    /** Court Log Entry time */
    String COURT_LOG_ENTRY_TIME = "COURT_LOG_ENTRY_TIME";

    String COURT_SITE_CODE = "COURT_SITE_CODE";

    String COURT_SITE_SHORT_NAME = "SHORT_NAME";

    String COURT_SITE_NAME = "COURT_SITE_NAME";
    
    String CREST_COURT_ROOM_NO = "CREST_COURT_ROOM_NO";

}
