package uk.gov.courtservice.xhibit.business.database.query.counsel;

/**
 * <p>
 * Title: CounselConstants
 * </p>
 * <p>
 * Description: Constant class that holds all the static data for the Counsel
 * query.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Ian Hannaford / Marie Holmberg
 * @version 1.0
 */

public interface CounselConstants {
    public static final String APPEAL_CASE_TYPE = "A";

    public static final String CASE_SUB_TYPE_MISC_APPEAL = "O";

    // constans representing the different types of legal reps
    public static final String LEGAL_TYPE_SOLICITOR = "LEGAL_TYPE_SOLICITOR";

    public static final String LEGAL_TYPE_ADVOCATE = "LEGAL_TYPE_ADVOCATE";

    // Column names
    public static final String SCHEDULED_HEARING_ID = "SCHEDULED_HEARING_ID";

    public static final String COURT_ROOM_ID = "COURT_ROOM_ID";

    public static final String COURT_ROOM_DESCRIPTION = "COURT_ROOM_DESCRIPTION";

    public static final String COURT_ROOM_DISPLAY_NAME = "COURT_ROOM_DISPLAY_NAME";

    public static final String COURT_ROOM_NAME = "COURT_ROOM_NAME";

    // PRE00180
    // public static final String SH_ORIGINAL_TIME = "SH_ORIGINAL_TIME";
    // public static final String SH_NOT_BEFORE_TIME = "SH_NOT_BEFORE_TIME";
    public static final String TIME_LISTED = "TIME_LISTED";

    public static final String SITTING_SEQUENCE_NO = "SITTING_SEQUENCE_NO";

    public static final String COURT_SITE_CODE = "COURT_SITE_CODE";

    public static final String COURT_SITE_SHORT_NAME = "COURT_SITE_SHORT_NAME";

    public static final String SH_SEQUENCE_NO = "SH_SEQUENCE_NO";

    public static final String SCHED_HEAR_DEF_ID = "SCHED_HEAR_DEF_ID";

    public static final String DEFENDANT_ON_CASE_ID = "DEFENDANT_ON_CASE_ID";

    public static final String IS_FLOATING = "IS_FLOATING";

    public static final String CASE_ID = "CASE_ID";

    public static final String CASE_NUMBER = "CASE_NUMBER";

    public static final String CASE_TYPE = "CASE_TYPE";

    public static final String CASE_TITLE = "CASE_TITLE";

    public static final String CASE_SUB_TYPE = "CASE_SUB_TYPE";

    public static final String LEGAL_ROLE = "LEGAL_ROLE";

    public static final String STAFF_NAME = "STAFF_NAME";

    public static final String STAFF_ROLE = "STAFF_ROLE";

    public static final String HEARING_TYPE_DESC = "HEARING_TYPE_DESC";

    public static final String HEARING_TYPE_CODE = "HEARING_TYPE_CODE";

    public static final String CREST_COURT_ROOM_NO = "CREST_COURT_ROOM_NO";

    // Party roles
    public static final String PARTY_ROLE_OBJECTOR = "O";

    public static final String PARTY_ROLE_RESPONDENT = "R";

    public static final String PARTY_ROLE_APPELLENT = "A";

    public static final String PARTY_ROLE_PROSECUTION = "P";

    public static final String PARTY_ROLE_DEFENDANT = "D";

    public static final String IN_PERSON = "I";

    public static final String LEGAL_REP = "L";

    public static final String SOLICITOR_FIRM = "S";

    public static final String ROLE_DEFENDANT = "defendant";

    public static final String ROLE_COUNSEL = "counsel";

    public static final String COURT_CLERK_ROLE = "CC";

    public static final String USHER_ROLE = "U";

}
