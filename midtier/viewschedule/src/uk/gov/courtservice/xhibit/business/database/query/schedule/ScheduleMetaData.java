package uk.gov.courtservice.xhibit.business.database.query.schedule;

/**
 * <p>
 * Title: ScheduleMetaData
 * </p>
 * <p>
 * Description: Coulmn indices in the schedule query
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

class ScheduleMetaData {

    static class Sitting {
        static final int SITTING_SEQUENCE_NO = 1;
    }

    static class CourtRoom {
        static final int COURT_ROOM_ID = 2;

        static final int DESCRIPTION = 2;

        static final int CREST_COURT_ROOM_NO = 3;

        static final int COURT_SITE_ID = 4;

        static final int LAST_UPDATE_DATE = 5;

        static final int CREATION_DATE = 6;

        static final int CREATED_BY = 7;

        static final int LAST_UPDATED_BY = 8;

        static final int VERSION = 9;

        static final int OBS_IND = 10;

        static final int DISPLAY_NAME = 11;
    }

    static class ScheduledHearing {
        static final int SCHEDULED_HEARING_ID = 12;

        static final int SEQUENCE_NO = 13;

        static final int NOT_BEFORE_TIME = 14;

        static final int ORIGINAL_TIME = 15;

        static final int LISTING_NOTE = 16;

        static final int HEARING_PROGRESS = 17;

        static final int SITTING_ID = 18;

        static final int HEARING_ID = 19;

        static final int MOVED_FROM = 20;

        static final int LAST_UPDATE_DATE = 21;

        static final int CREATION_DATE = 22;

        static final int CREATED_BY = 23;

        static final int LAST_UPDATED_BY = 24;

        static final int VERSION = 25;

        static final int LINKED_SH_ID = 26;

        static final int END_TIME = 27;

        static final int START_TIME = 28;

        static final int DATE_OF_HEARING = 29;

        static final int IS_CASE_ACTIVE = 30;
    }

    static class Case {
        static final int CASE_ID = 31;

        static final int CASE_NUMBER = 32;

        static final int CASE_TYPE = 33;

        static final int MAG_CONVICTION_DATE = 34;

        static final int CASE_SUB_TYPE = 35;

        static final int CASE_TITLE = 36;

        static final int CASE_DESCRIPTION = 37;

        static final int LINKED_CASE_ID = 38;

        static final int BAIL_MAG_CODE = 39;

        static final int REF_COURT_ID = 40;

        static final int COURT_ID = 41;

        static final int CHARGE_IMPORT_INDICATOR = 42;

        static final int SEVERED_IND = 43;

        static final int INDICT_RESP = 44;

        static final int DATE_IND_REC = 45;

        static final int PROS_AGENCY_REFERENCE = 46;

        static final int LAST_UPDATE_DATE = 47;

        static final int CREATION_DATE = 48;

        static final int CREATED_BY = 49;

        static final int LAST_UPDATED_BY = 50;

        static final int VERSION = 51;

        static final int CASE_CLASS = 52;

        static final int JUDGE_REASON_FOR_APPEAL = 53;

        static final int RESULTS_VERIFIED = 54;

        static final int LENGTH_TAPE = 55;

        static final int NO_PAGE_PROS_EVIDENCE = 56;

        static final int NO_PROS_WITNESS = 57;

        static final int EST_PDH_TRIAL_LENGTH = 58;

        static final int INDICTMENT_INFO_1 = 59;

        static final int INDICTMENT_INFO_2 = 60;

        static final int INDICTMENT_INFO_3 = 61;

        static final int INDICTMENT_INFO_4 = 62;

        static final int INDICTMENT_INFO_5 = 63;

        static final int INDICTMENT_INFO_6 = 64;

        static final int POLICE_OFFICER_ATTENDING = 65;

        static final int CPS_CASE_WORKER = 66;

        static final int EXPORT_CHARGES = 67;

        static final int IND_CHANGE_STATUS = 68;
    }

    static class RefHearingType {
        static final int REF_HEARING_TYPE_ID = 69;

        static final int HEARING_TYPE_CODE = 70;

        static final int HEARING_TYPE_DESC = 71;

        static final int CATEGORY = 72;

        static final int SEQ_NO = 73;

        static final int LIST_SEQUENCE = 74;

        static final int LAST_UPDATE_DATE = 75;

        static final int CREATION_DATE = 76;

        static final int CREATED_BY = 77;

        static final int LAST_UPDATED_BY = 78;

        static final int VERSION = 79;

        static final int COURT_ID = 80;

        static final int OBS_IND = 81;
    }

    static class DefendantOnCase {
        static final int DEFENDANT_ON_CASE_ID = 98;

        static final int FINAL_DRIVING_LICENCE_STATUS = 82;

        static final int PTIURN = 83;

        static final int IS_JUVENILE = 84;

        static final int IS_MASKED = 85;

        static final int MASKED_NAME = 86;

        static final int CASE_ID = 87;

        static final int DEFENDANT_ID = 88;

        static final int LAST_UPDATE_DATE = 89;

        static final int CREATION_DATE = 90;

        static final int CREATED_BY = 91;

        static final int LAST_UPDATED_BY = 92;

        static final int VERSION = 93;

        static final int OBS_IND = 94;

        static final int RESULTS_VERIFIED = 95;

        static final int DEFENDANT_NUMBER = 96;

        static final int DATE_OF_COMMITTAL = 97;
    }

    static class Defendant {
        static final int DEFENDANT_ID = 98;

        static final int CREST_DEFENDANT_ID = 99;

        static final int FIRST_NAME = 100;

        static final int MIDDLE_NAME = 101;

        static final int SURNAME = 102;

        static final int INITIALS = 103;

        static final int DATE_OF_BIRTH = 104;

        static final int GENDER = 105;

        static final int LAST_CONVICTION_DATE = 106;

        static final int IS_COMPANY = 107;

        static final int LAST_UPDATE_DATE = 108;

        static final int CREATION_DATE = 109;

        static final int CREATED_BY = 110;

        static final int LAST_UPDATED_BY = 111;

        static final int VERSION = 112;

        static final int ADDRESS_ID = 113;

        static final int COURT_ID = 114;
    }

    static class RefJudge {
        static final int TITLE = 115;

        static final int FIRST_NAME = 116;

        static final int MIDDLE_NAME = 117;

        static final int SURNAME = 118;
    }

}
