package uk.gov.courtservice.xhibit.business.services.hearingschedule.hearingrecord;

/**
 * <p>
 * Title: HearingRecordConstants
 * </p>
 * <p>
 * Description: This interface will hold all HearingRecord constants. They are
 * all declared public, static and final.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public interface HearingRecordConstants {

    // CourtLogCRUDValue key for HearingRecordUpdate
    public static final String ESTIMATED_PDH_TRIAL_TIME_COURT_LOG_EVENT_KEY = "E40711_Time_Estimate";

    // Represent the null value 0.
    public static final int NULL_VALUE = 0;

    // Export related flags.
    public static final String READY_FOR_EXPORT = "R";

    public static final String IN_PROGRESS = "P";

    public static final String EXPORT_SUCCESS = "S";

    public static final String EXPORT_FAILED = "F";

    public static final String EXPORT_LOCKED = "L";

    // Hearing record Bail statuses.
    // Valid entries 'B'=bail, 'C'=custody, 'J'=in care or 'N'=not
    // applicable
    public static final String BAIL_STATUS = "B";

    public static final String CUSTODY_STATUS = "C";

    public static final String IN_CASE_STATUS = "J";

    public static final String NOT_APPLICABLE_STATUS = "N";
    
    //S41 SECTION
    //s41 Application
    public static final String S41_APPLICATION_YES = "Y";
    
    //s41 Application
    public static final String S41_APPLICATION_NO = "N";
    
	//s41 Granted
    public static final String S41_APPLICATION_GRANTED = "G";
    
    //s41 Granted
    public static final String S41_APPLICATION_REFUSED = "R";
    
	//s41 ApplicationMade    
    public static final String S41_APPLICATION_MADE_ADVANCE_0F_TRIAL = "AT";
    
    //s41 ApplicationMade  
    public static final String S41_APPLICATION_MADE_AT_TRIAL = "T";

    // Case types and sub types
    public static final String CASE_TYPE_APPEAL = "A";

    public static final String CASE_TYPE_BAIL = "B";

    public static final String CASE_TYPE_COM_FOR_SENT = "S";

    public static final String CASE_TYPE_TRIAL = "T";

    public static final String CASE_TYPE_U_CASE = "U";

    public static final String CASE_SUB_TYPE_MISC_APPEAL = "O";

    public static final String CASE_SUB_TYPE_CRIM_APPEAL = "C";

    // MP Hearing types
    public static final String HEARING_TYPE_MAIN = "M";

    public static final String HEARING_TYPE_PREL = "P";

    // isHRApplicaton
    public static final String IS_HR_APP_YES = "Y";

    public static final String IS_HR_APP_NO = "N";

    // resultBailApplication
    public static final String RESULT_BAIL_APP_GRANTED = "G";

    public static final String RESULT_BAIL_APP_REFUSED = "R";

    // oralEvidence
    public static final String ORAL_EVIDENCE_YES = "Y";

    public static final String ORAL_EVIDENCE_NO = "N";

    // subsBailApplication
    public static final String SUBS_BAIL_APP_YES = "Y";

    public static final String SUBS_BAIL_APP_NO = "N";

    // isAdjourned and adjourned code
    public static final String ADJOURNED_YES = "Y";

    public static final String ADJOURNED_NO = "N";

    public static final String ADJOURNED_DS_CODE = "DS";

    // finalDrivingLicenseStatus
    public static final int FINAL_DRIVING_LIC_STATUS_NULL = 0;

    public static final int FINAL_DRIVING_LIC_STATUS_NOT_LIABLE = 1;

    public static final int FINAL_DRIVING_LIC_STATUS_DISQUALIFIED = 2;

    // severedIndictment
    public static final String SEVERED_IND_YES = "Y";

    public static final String SEVERED_IND_NO = "N";

    // Counsel values - legalRepType
    public static final String LEGAL_REP_TYPE_ADVOCATE = "A";

    public static final String LEGAL_REP_TYPE_SOLICITOR = "S";

    // SolFirmOrRefLegalRep
    public static final String BARRISTER = "L";

    // Counsel values - legalRole
    public static final String LEGAL_ROLE_DEFENCE = "D";

    public static final String LEGAL_ROLE_PROSECUTION = "P";

    public static final String LEGAL_ROLE_RESPONDANT = "R";

    public static final String LEGAL_ROLE_OBJECTOR = "O";

    public static final String LEGAL_ROLE_THIRD_PARTY = "TP";

    public static final String LEGAL_ROLE_IN_PERSON = "I";
    
    public static final String LEGAL_ROLE_NON_ATTENDANCE = "N";

    // Judge's deputyHCJ flag
    public static final String JUDGE_DEPUTY_HCJ_YES = "Y";

    public static final String JUDGE_DEPUTY_HCJ_NO = "N";

    // Scheduled Hearing Attendees
    public static final String SH_ATT_JUDGE = "J";

    public static final String SH_ATT_JUSTICE = "JP";

    public static final String SH_ATT_COURT_REPORTER = "CR";

    // Hearing type code
    public final static String REF_HEARING_CODE_P_AND_D = "PAD";

    // isDefCompany
    public static final String IS_DEF_COMPANY_YES = "Y";

    public static final String IS_DEF_COMPANY_NO = "N";

    // #################################### Exception Keys
    // ###########################################//
    // General
    public static final String SCHED_HEARING_NOT_FOUND = "hearingrecord.scheduledhearingnotfound";

    public static final String CASE_NOT_FOUND = "hearingrecord.casenotfound";

    public static final String HEARING_NOT_FOUND = "hearingrecord.hearingnotfound";

    public static final String SHATTENDEE_NOT_FOUND = "hearingrecord.courtstaffnotfound";

    public static final String DEF_ON_CASE_NOT_FOUND = "hearingrecord.defoncasenotfound";

    public static final String DEFENDANT_NOT_FOUND = "hearingrecord.defendantnotfound";

    public static final String SCHED_HEAR_DEF_NOT_FOUND = "hearingrecord.schedhearingdefnotfound";

    public static final String DEF_HEAR_REC_COULD_NOT_BE_CREATED = "hearingrecord.defhearingrecordcouldnotbecreated";

    // Update
    public static final String INVALID_MP_HEARING_TYPE_MISC_APPEAL_EXC = "hearingrecord.update.Illegal_mpHearingType_misc_appeal";

    public static final String INVALID_MP_HEARING_TYPE_EXC = "hearingrecord.update.Illegal_mpHearingType";

    public static final String INVALID_BAIL_STATUS_EXC = "hearingrecord.update.invalid_bailstatus";

    public static final String INVALID_DATE_OF_APPLICATION_EXC = "hearingrecord.update.invalid_date_of_application";

    public static final String INVALID_ADJOURNED_DATE_EXC = "hearingrecord.update.invalid_adjourned_date";

    public static final String NO_ADJOURNED_DATE_GIVEN_EXC = "hearingrecord.update.no_adjourned_date_given";

    public static final String ESTIMATED_TRIAL_TIME_FAILURE = "hearingrecord.update.trial_time_estimate_failure";

    public static final String INVALID_BAIL_STATUS_DEFENDANT_IS_COMPANY = "hearingrecord.update.invalid_bail_status_def_is_company";

    public static final String INVALID_BAIL_STATUS_DEFENDANT_IS_NOT_COMPANY = "hearingrecord.update.invalid_bail_status_def_is_not_company";

	public static final String NO_MP_HEARING_TYPE = "hearingrecord.update.no_mp_hearing_type";

	public static final String HEARING_START_DATE_IN_FUTURE = "hearingrecord.update.hearing_start_date";

	public static final String HEARING_END_DATE_IN_FUTURE = "hearingrecord.update.hearing_end_date";

	public static final String HEARING_START_DATE_AFTER_END_DATE = "hearingrecord.update.hearing_start_end_date";

	public static final String HEARING_START_DATE_VALID_COURT_DATE = "hearingrecord.update.hearing_start_court_date";

	public static final String HEARING_END_DATE_VALID_COURT_DATE = "hearingrecord.update.hearing_end_court_date";

	public static final String HEARING_ADJOURNED_DATE = "hearingrecord.update.hearing_adjourned_date";

	public static final String HEARING_ADJOURNED_DATE_HEARING_END_DATE = "hearingrecord.update.hearing_adjourned_date_hearing_end_date";

	public static final String HEARING_NEW_BAIL_DATE_HEARING_START_END_DATE = "hearingrecord.update.hearing_new_bail_date_hearing_start_end_date";

	public static final String HEARING_ESTIMATED_TRIAL_LENGTH = "hearingrecord.update.hearing_estimated_trial_length";

    // Export
    public static final String HEARING_NOT_ENDED_EXC = "hearingrecord.export.hearing_not_ended";

    public static final String EXPORT_IN_PROGRESS_EXC = "hearingrecord.export.export_in_progress";

    public static final String HEARING_ALREADY_EXPORTED_EXC = "hearingrecord.export.hearing_already_exported";

    public static final String EXPORT_A_LOCKED_EXC = "hearingrecord.export.export_A_locked";

    public static final String READY_FOR_EXPORT_EXC = "hearingrecord.export.ready_for_export";

    public static final String EXPORT_INVALID_CASE_EXC = "hearingrecord.export.invalid_case";

    public static final String CATEGORY_REQUIRED = "hearingrecord.export.category_required_for_barrister";

    public static final String EXPORT_INVALID_NO_CALCULATED_DURATION = "hearingrecord.export.no_calculated_duration";
    
    public static final String EXPORT_INVALID_NO_DEF_HEARING_TYPE = "hearingrecord.export.no_def_hearing_type";
    
    public static final String EXPORT_INVALID_NO_CASE_ADJOURNED = "hearingrecord.export.no_case_adjourned";

    // #################################### Exception Keys
    // ###########################################//
}