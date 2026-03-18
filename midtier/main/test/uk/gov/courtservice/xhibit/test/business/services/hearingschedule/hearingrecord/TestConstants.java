//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord;
//
///**
// * <p>Title: TestConstants</p>
// * <p>Description: Test scripts for building up the hearing record tests.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//
//public interface TestConstants {
//
//  //keys
//  static final Integer defID = new Integer(-9900);
//  static final Integer caseID = new Integer(-9901);
//  static final Integer caseID2 = new Integer(-9919);
//  static final Integer defOnCaseID = new Integer(-9902);
//  static final Integer refHrgTypeID = new Integer(-9903);
//  static final Integer hearingID = new Integer(-9904);
//  static final Integer hearingID2 = new Integer(-9920);
//  static final Integer defHrgRecID = new Integer(-9905);
//  static final Integer hrgListID = new Integer(-9906);
//  static final Integer sittingID = new Integer(-9907);
//  static final Integer shHrg1ID = new Integer(-9908);
//  static final Integer shHrg2ID = new Integer(-9909);
//  static final Integer shHrg3ID = new Integer(-9922);
//  static final Integer refJudgeID = new Integer(-9910);
//  static final Integer shAttID = new Integer(9999911); // has to be positive HearingRecordRetrievalForViewHelper looks for highest
//  static final Integer shJudgeID = new Integer(-9912);
//  static final Integer refLegRep1ID = new Integer(-9913);
//  static final Integer refLegRep2ID = new Integer(-9914);
//  static final Integer shLegRep1ID = new Integer(-9915);
//  static final Integer shLegRep2ID = new Integer(-9916);
//  static final Integer ccInfoID = new Integer(-9917);
//  static final Integer solFirmID = new Integer(-9918);
//  static final Integer schedHearDefID = new Integer(-9912);
//  static final Integer exportA = new Integer(-9913);
//  static final Integer courtID = new Integer(-9990);
//  static final Integer courtSiteID = new Integer(-9991);
//  static final Integer courtRoomID = new Integer(-9992);
//  static final Integer refCourtID = new Integer(-99993);
//  static final Integer addressID = new Integer(-9994);
//  static final Integer linkedHearingId = new Integer(-999);
//
//  static final Integer CR_FIRM = new Integer(920);
//  static final Integer COURT_REPORTER = new Integer(921);
//  static final Integer SH_ATT_CR = new Integer(923);
//
//  static final Integer CHAMBER = new Integer(940);
//  static final Integer ADVOCATE = new Integer(941);
//  static final Integer SOL_FIRM = new Integer(942);
//  static final Integer SOLICITOR = new Integer(943);
//
//  static final Integer SH_JUSTICE_ID = new Integer(944);
//
//  static final Integer CASE_ID_1 = new Integer(945);
//  static final Integer CASE_ID_2 = new Integer(946);
//  static final Integer CASE_ID_3 = new Integer(947);
//  static final Integer defRefID1 = new Integer(980);
//  static final Integer defRefID2 = new Integer(981);
//
//
//  //court
//  static final String delCourt = "delete from xhb_court where court_id = "+courtID.intValue();
//  static final String insCourt = "insert into xhb_court (COURT_ID, COURT_TYPE, COURT_NAME, CREST_COURT_ID, COURT_PREFIX,"+
//                                 "CREST_IP_ADDRESS, IN_SERVICE_FLAG, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME) values ("+
//                                 courtID.intValue() +", 'CR', 'Chelmsford', '123', 'CH', '123.456.789.1', 'Y', 'Probation off.', 'CH-internet')";
// //address
//  static final String delAddr = "delete from xhb_address where address_id = "+addressID.intValue();
//  static final String insAddr = "insert into xhb_address (address_id, town) values ("+addressID.intValue()+", 'London')";
//
// //courtSite
//  static final String insCourtSite = "insert into xhb_court_site (court_site_id, court_id, address_id) values ("+
//                                     courtSiteID.intValue() +", "+courtID.intValue()+", "+addressID.intValue()+")";
//  static final String delCourtSite = "delete from xhb_court_site where court_site_id = " + courtSiteID.intValue() ;
//
// //courtroom
//  static final String insCourtRoom = "insert into xhb_court_room (court_room_id, crest_court_room_no, court_site_id) values ("+
//                                     courtRoomID.intValue()+ ", 7, " + courtSiteID.intValue() +")";
//  static final String delCourtRoom = "delete from xhb_court_room where court_room_id = "+courtRoomID.intValue();
//
//  //refCourt
//  static final String insRefCourt = "insert into xhb_ref_court (ref_court_id, court_full_name, court_id) values ("+
//                                    refCourtID.intValue() +", 'BAS magistrates court', "+courtID.intValue()+")";
//  static final String delRefCourt = "delete from xhb_ref_court where ref_court_id = "+refCourtID.intValue();
//
//  //Defendant details
//  static final String delDef = "delete from xhb_defendant where defendant_id = " + defID.intValue();
//  static final String insDef = "insert into xhb_defendant (defendant_id, first_name, surname, court_id, last_conviction_date) values ("
//                             + defID.intValue() + ", 'Marie', 'Holmberg', "+courtID.intValue()+", TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//  //Case
//  static final String delCase = "delete from xhb_case where court_id = " + courtID.intValue();
//  static final String insCase = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+caseID.intValue()+", 20030310, 'T', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//  static final String insCase2 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+caseID2.intValue()+", 20030399, 'S', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//
//  //DefOnOffence
//  static final String delDefOnOffence =
//          "delete from xhb_defendant_on_offence where defendant_on_case_id in " +
//          "(SELECT defendant_on_case_id FROM XHB_DEFENDANT_ON_CASE WHERE case_id = " +
//          caseID.intValue()+" or case_id = "+caseID2.intValue() + ")";
//
//  //DefOnCase
//  static final String delDefOnCase = "delete from xhb_defendant_on_case where case_id = "+caseID.intValue()+" or case_id = "+caseID2.intValue();
//  static final String insDefOnCase = "insert into xhb_defendant_on_case (defendant_on_case_id, case_id, defendant_id, no_of_tics, FINAL_DRIVING_LICENCE_STATUS, COLLECT_MAGISTRATE_COURT_ID) values ("+
//                                     defOnCaseID.intValue()+", "+caseID.intValue()+", " + defID.intValue() + ", 5, 1, 666)";
//
////refhearing type
//  static final String delRefHrgType = "delete from xhb_ref_hearing_type where ref_hearing_type_id = "+refHrgTypeID.intValue();
//  static final String insRefHrgType = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+refHrgTypeID.intValue()+", 'TRI', 'TRIAL', "+courtID.intValue()+")";
//
////hearing
//  static final String delHrg = "delete from xhb_hearing where ref_hearing_type_id = "+refHrgTypeID.intValue();
//  static final String insHrg = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) "+
//                               "values ("+hearingID.intValue()+", "+caseID.intValue()+", "+refHrgTypeID.intValue()+", "+courtID.intValue()+", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , " + linkedHearingId + ")";
//  static final String insHrg2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, mp_hearing_type, last_calculated_duration, hearing_start_date, hearing_end_date, linked_hearing_id) "+
//                               "values ("+hearingID2.intValue()+", "+caseID2.intValue()+", "+refHrgTypeID.intValue()+", "+courtID.intValue()+", 'P', 123456, TO_Date( '02/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , " + linkedHearingId + ")";
//
//
////defhearingrecord
//  static final String delDefHrgRec = "delete from xhb_def_hearing_record where hearing_id = "+hearingID.intValue()+" or hearing_id = "+hearingID2.intValue();
//// String insDefHrgRec = "insert into xhb_def_hearing_record (hearing_record_id, hearing_id, defendant_on_case_id) values ("+defHrgRecID.intValue()+", "+hearingID.intValue()+", "+defOnCaseID.intValue()+")";
//  static final String insDefHrgRec = "insert into xhb_def_hearing_record (hearing_record_id, hearing_id, defendant_on_case_id, REF_ADJOURNMENT_ID, ADJOURNED_DATE, IS_ADJOURNED, START_DATE_NEW_BAIL_STATUS,"+
//                                     "NEW_BAIL_STATUS, DATE_BAIL_APPLICATION, SUBST_BAIL_APPLICATION, ORAL_EVIDENCE, RESULT_BAIL_APPLICATION,IS_HRA_APPLICATION, REF_DEF_HEARING_TYPE_ID, END_BAIL_STATUS, START_BAIL_STATUS, HEARING_DATES_FREETEXT_1,"+
//                                     "HEARING_DATES_FREETEXT_2, HEARING_DATES_FREETEXT_3"+
//                                     ") values ("+defHrgRecID.intValue()+", "+hearingID.intValue()+", "+defOnCaseID.intValue()+", "+
//                                     "555, TO_Date( '04/20/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'Y', TO_Date( '02/25/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'),"+
//                                     "'J', TO_Date( '02/28/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'N', 'N', 'R','Y', 777, 'B', 'C', 'free1','free2', 'free3')";
////hearing_list
//  static final String delHrgList = "delete from xhb_hearing_list where list_id = "+hrgListID.intValue();
//  static final String insHrgList = "insert into xhb_hearing_list (list_id, crest_list_id, court_id) values ("+hrgListID.intValue()+", 123, "+courtID.intValue()+")";
//
////sitting
//  static final String delSitting = "delete from xhb_sitting where sitting_id = "+sittingID.intValue();
//  static final String insSitting = "insert into xhb_sitting (sitting_id, is_floating, list_id, court_room_id, court_site_id) values ("+sittingID.intValue()+", 'T', "+hrgListID.intValue()+", "+courtRoomID.intValue()+", "+courtSiteID.intValue()+")";
//
////scheduledhearings
//  static final String delShHrg1 = "delete from xhb_scheduled_hearing where sitting_id = "+sittingID.intValue();
//  static final String insShHrg1 = "insert into xhb_scheduled_hearing(scheduled_hearing_id, sequence_no, sitting_id, hearing_id, original_time, IS_CASE_ACTIVE) values ("+
//                                  shHrg1ID.intValue()+", 1, "+sittingID.intValue()+", "+hearingID.intValue()+", TO_Date( '02/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'N')";
//  static final String insShHrg2 = "insert into xhb_scheduled_hearing(scheduled_hearing_id, sequence_no, sitting_id, hearing_id, original_time, IS_CASE_ACTIVE) values ("+
//                                  shHrg2ID.intValue()+", 1, "+sittingID.intValue()+", "+hearingID.intValue()+", TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'N')";
//  static final String insShHrg3 = "insert into xhb_scheduled_hearing(scheduled_hearing_id, sequence_no, sitting_id, hearing_id, original_time, IS_CASE_ACTIVE) values ("+
//                                  shHrg3ID.intValue()+", 1, "+sittingID.intValue()+", "+hearingID2.intValue()+", TO_Date( '03/11/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM'), 'N')";
//
////refJudge
//  static final String delJudge = "delete from xhb_ref_judge where ref_judge_id = "+refJudgeID.intValue();
//  //static final String insJudge = "insert into xhb_ref_judge (ref_judge_id, surname, court_id) values ("+refJudgeID.intValue()+", 'Her Honour Holmberg', "+courtID.intValue()+")";
//  static final String insJudge = "insert into xhb_ref_judge (ref_judge_id, first_name, middle_name, surname, court_id, full_list_title1, "+
//                                 "full_list_title2, full_list_title3) values ("+refJudgeID.intValue()+", 'Marie', 'UE', 'Holmberg', "+
//                                 courtID.intValue()+", '1 Her Honour Holmberg', '2 Her Honour Holmberg', '3 Her Honour Holmberg')";
////shAttendee
//  static final String delShAtt = "delete from xhb_sched_hearing_attendee where sh_attendee_id = "+shAttID.intValue();
//  static final String insShAtt = "insert into xhb_sched_hearing_attendee (sh_attendee_id, attendee_type, scheduled_hearing_id, ref_judge_id) values ("+shAttID.intValue()+", 'J', "+shHrg1ID.intValue()+", "+refJudgeID.intValue()+")";
//
////shAttendee for judge
//  static final String delShJudge = "delete from xhb_sh_judge where sh_attendee_id = "+shAttID.intValue();
//  static final String insShJudge = "insert into xhb_sh_judge (sh_judge_id, deputy_hcj, ref_judge_id, sh_attendee_id) values ("+shJudgeID.intValue()+", 'Y', "+refJudgeID.intValue()+", "+shAttID.intValue()+") ";
//
////refLegalRep
//  static final String delRefLegRep = "delete from xhb_ref_legal_representative where ref_legal_rep_id = "+refLegRep1ID.intValue()+" or ref_legal_rep_id = "+refLegRep2ID.intValue();
//  static final String insRefLegRep1 = "insert into xhb_ref_legal_representative (ref_legal_rep_id, surname, court_id, legal_rep_type) values ("+
//                                      refLegRep1ID.intValue()+", 'Holmis1', "+courtID.intValue()+", 'A')";
//  static final String insRefLegRep2 = "insert into xhb_ref_legal_representative (ref_legal_rep_id, surname, court_id, legal_rep_type) values ("+
//                                      refLegRep2ID.intValue()+", 'Holmis2', "+courtID.intValue()+", 'S')";
//
////shLegRep
//  static final String delShLegRep = "delete from xhb_sh_leg_rep where sh_leg_rep_id = "+shLegRep1ID.intValue()+" or sh_leg_rep_id = "+shLegRep2ID.intValue();
////  String insShLegRep1 = "insert into xhb_sh_leg_rep (sh_leg_rep_id, scheduled_hearing_id, ref_defence_category_id, ref_legal_rep_id) values ("+
////                               shLegRep1ID.intValue()+", "+shHrg1ID.intValue()+", 1, "+refLegRep1ID.intValue()+")";
//
//  static final String insShLegRep1 = "insert into xhb_sh_leg_rep (sh_leg_rep_id, scheduled_hearing_id, ref_defence_category_id, ref_legal_rep_id, "+
//                                     "crest_sequence_no, legal_role, is_signed_in, sol_firm_or_ref_legal_rep, sched_hear_def_id, cc_info_id, ref_solicitor_firm_id) "+
//                                     "values ("+shLegRep1ID.intValue()+", "+shHrg1ID.intValue()+", 1, "+refLegRep1ID.intValue()+
//                                     ", 99, 'D', 'Y', 'R', "+schedHearDefID.intValue()+", "+ccInfoID.intValue()+", "+solFirmID.intValue()+")";
//  static final String insShLegRep2 = "insert into xhb_sh_leg_rep (sh_leg_rep_id, scheduled_hearing_id, ref_defence_category_id, ref_legal_rep_id, legal_role) values ("+
//                                     shLegRep2ID.intValue()+", "+shHrg2ID.intValue()+", 1, "+refLegRep2ID.intValue()+", 'P')";
//
//  // defence barrister with category set
//  static final String insShLegRep3 = "insert into xhb_sh_leg_rep (sh_leg_rep_id, scheduled_hearing_id, ref_defence_category_id, ref_legal_rep_id, "+
//                                     "crest_sequence_no, legal_role, is_signed_in, sol_firm_or_ref_legal_rep, sched_hear_def_id) "+
//                                     "values ("+shLegRep1ID.intValue()+", "+shHrg2ID.intValue()+", 1, "+refLegRep1ID.intValue()+
//                                     ", 99, 'D', 'Y', 'L', "+schedHearDefID.intValue()+")";
//  // defence barrister without category set
//  static final String insShLegRep4 = "insert into xhb_sh_leg_rep (sh_leg_rep_id, scheduled_hearing_id, ref_legal_rep_id, "+
//                                     "crest_sequence_no, legal_role, is_signed_in, sol_firm_or_ref_legal_rep, sched_hear_def_id) "+
//                                     "values ("+shLegRep2ID.intValue()+", "+shHrg3ID.intValue()+", "+refLegRep2ID.intValue()+
//                                     ", 99, 'D', 'Y', 'L', "+schedHearDefID.intValue()+")";
//
//  //ccInfo
//  static final String delCCInfo = "delete from xhb_cc_info where cc_info_id = "+ccInfoID.intValue();
//  static final String insCCInfo = "insert into xhb_cc_info (cc_info_id, cc_info_text) values ("+ccInfoID.intValue()+", 'CC information text')";
//
//  //solfirm
//  static final String delSolFirm = "delete from xhb_ref_solicitor_firm where ref_solicitor_firm_id = "+solFirmID.intValue();
//  static final String insSolFirm = "insert into xhb_ref_solicitor_firm (ref_solicitor_firm_id, solicitor_firm_name, court_id) "+
//                                   "values ("+solFirmID.intValue()+", 'Marie Solicitor', "+courtID.intValue()+")";
//
//  //ScheduledHearingDefendant
//  static final String delSchedHearDef =
//          "delete from xhb_sched_hearing_defendant where sched_hear_def_id = " + schedHearDefID.intValue() +
//          " or defendant_on_case_id in " +
//          "(SELECT defendant_on_case_id FROM XHB_DEFENDANT_ON_CASE WHERE case_id = " +
//          caseID.intValue()+" or case_id = "+caseID2.intValue() + ")";
//  static final String insSchedHearDef = "insert into xhb_sched_hearing_defendant(sched_hear_def_id, scheduled_hearing_id, defendant_on_case_id) values ("+schedHearDefID.intValue()+", "+shHrg1ID.intValue()+ ", "+ defOnCaseID.intValue()+")";
//
////ExportA
//  static final String insExportA = "insert into xhb_exporta (export_a_id, court_clerk_export, status_flag, hearing_id)"+
//                              " values ("+exportA.intValue()+", 'Marie2', 'F', "+hearingID.intValue()+")";
//  static final String delExportA = "delete from xhb_exporta where export_a_id = "+exportA.intValue();
//
//
//  //court reporter firm
//  static final String DEL_CR_FIRM= "delete from xhb_ref_court_reporter_firm where ref_court_reporter_firm_id = "+CR_FIRM.intValue();
//
//  static final String INS_CR_FIRM = "insert into xhb_ref_court_reporter_firm (ref_court_reporter_firm_id, display_first, court_id, firm_name)"+
//                                  "values ("+CR_FIRM.intValue()+", 'Y', "+courtID.intValue()+", 'Marie - the court reporter firm')";
//
//  //Court reporter
//  static final String DEL_CR = "delete from xhb_ref_court_reporter where ref_court_reporter_id = "+COURT_REPORTER.intValue();
//  static final String INS_CR1 = "insert into xhb_ref_court_reporter (ref_court_reporter_id, first_name, surname, ref_court_reporter_firm_id, court_id)"+
//                              "values ("+COURT_REPORTER.intValue()+", 'Marie', 'Holmberg', "+CR_FIRM.intValue()+", "+courtID.intValue()+")";
//
//  //ShAttendee for court reporter
//  static final String DEL_SH_ATT_CR = "delete from xhb_sched_hearing_attendee where sh_attendee_id = "+SH_ATT_CR.intValue();
//  static final String INS_SH_ATT_CR1 = "insert into xhb_sched_hearing_attendee (sh_attendee_id, attendee_type, scheduled_hearing_id, ref_court_reporter_id)"+
//                                    "values ("+SH_ATT_CR.intValue()+", 'CR', "+shHrg1ID.intValue()+", "+COURT_REPORTER.intValue()+")";
//
//
//  //chamber
//  static final String DEL_CHAMBER = "delete from xhb_ref_chamber where ref_chamber_id = "+CHAMBER.intValue();
//  static final String INS_CHAMBER = "insert into xhb_ref_chamber (ref_chamber_id,court_id, firm_name, address_id)"+
//                                    "values ("+CHAMBER.intValue()+", "+courtID.intValue()+", 'Marie - Chamber ltd', "+addressID.intValue()+")";
//
//  //advocate
//  static final String DEL_ADV = "delete from xhb_ref_advocate where ref_advocate_id = "+ADVOCATE.intValue();
//  static final String INS_ADV = "insert into xhb_ref_advocate (ref_advocate_id,ref_legal_rep_id, ref_chamber_id, bar_no)"+
//                              "values ("+ADVOCATE.intValue()+", "+refLegRep1ID.intValue()+", "+CHAMBER.intValue()+", 12345)";
//
//  //solfirm
//  static final String DEL_SOL_FIRM = "delete from xhb_ref_solicitor_firm where ref_solicitor_firm_id = "+SOL_FIRM.intValue();
//  static final String INS_SOL_FIRM = "insert into xhb_ref_solicitor_firm (ref_solicitor_firm_id,court_id, solicitor_firm_name, address_id)"+
//                                     "values ("+SOL_FIRM.intValue()+", "+courtID.intValue()+", 'Marie - SolFirm ltd', "+addressID.intValue()+")";
//
//  //solicitor
//  static final String DEL_SOLICITOR = "delete from xhb_ref_solicitor where solicitor_id = "+SOLICITOR.intValue();
//  static final String INS_SOLICITOR = "insert into xhb_ref_solicitor (solicitor_id, is_in_crest, ref_legal_rep_id, crest_solicitor_name, ref_solicitor_firm_id) "+
//                                      "values ("+SOLICITOR.intValue()+", 'N', "+refLegRep2ID.intValue()+", 'Marie - Chamber ltd', "+SOL_FIRM.intValue()+")";
//
//  //shJustice
//  static final String DEL_SH_JUSTICE = "delete from xhb_sh_justice where sh_justice_id = "+SH_JUSTICE_ID.intValue();
//  static final String INS_SH_JUSTICE = "insert into xhb_sh_justice (sh_justice_id, justice_name, hearing_id)"+
//                                      " values ("+SH_JUSTICE_ID.intValue()+", 'Marie the peace', "+hearingID.intValue()+")";
//
//
//  //Linked cases
//  static final String DEL_CASES = "delete from xhb_case where case_id = "+CASE_ID_1.intValue()+" or case_id = "+CASE_ID_2.intValue()+" or case_id = "+CASE_ID_3.intValue();
//  static final String INS_CASE_1 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID_1.intValue()+", 20030311, 'T', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//  static final String INS_CASE_2 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID_2.intValue()+", 20030312, 'S', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//  static final String INS_CASE_3 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID_3.intValue()+", 20030313, 'B', "+refCourtID.intValue()+", "+courtID.intValue()+")";
//
//
//  static final String DEL_DEF_REF = "delete from xhb_defendant_reference where defendant_id ="+defID.intValue();
//  static final String INS_DEL_REF1 = "insert into xhb_defendant_reference (def_ref_id, reference_value, reference_name, defendant_id) values "+
//                                    "("+defRefID1.intValue()+", 'ukdrivlic123', 'DRIVER_NO', "+defID.intValue()+")";
//  static final String INS_DEL_REF2 = "insert into xhb_defendant_reference (def_ref_id, reference_value, reference_name, defendant_id) values "+
//                                    "("+defRefID2.intValue()+", 'ukdrivlic123', 'CRO_NO', "+defID.intValue()+")";
//
//  static final String DEL_HEARING_LEG_REP1 = "delete from xhb_hearing_leg_rep " +
//                                             "where hearing_id = " + hearingID.intValue() + " " +
//                                             "and ref_legal_rep_id = " + refLegRep1ID.intValue();
//  static final String INS_HEARING_LEG_REP1_01 = "insert into xhb_hearing_leg_rep " +
//                                                "(hearing_id, ref_legal_rep_id, " +
//                                                "start_date, end_date) values " +
//                                                "("+hearingID.intValue()+","+refLegRep1ID.intValue()+","+
//                                                "to_date('10/04/2003','DD/MM/YYYY')"+","+"to_date('18/04/2003','DD/MM/YYYY'))";;
//  static final String INS_HEARING_LEG_REP1_02 = "insert into xhb_hearing_leg_rep " +
//                                                "(hearing_id, ref_legal_rep_id, " +
//                                                "start_date, end_date) values " +
//                                                "("+hearingID.intValue()+","+refLegRep1ID.intValue()+","+
//                                                "to_date('23/04/2003','DD/MM/YYYY')"+","+"to_date('30/04/2003','DD/MM/YYYY'))";;
//
//  static final String DEL_HEARING_LEG_REP2 = "delete from xhb_hearing_leg_rep " +
//                                             "where hearing_id = " + hearingID.intValue() + " " +
//                                             "and ref_legal_rep_id = " + refLegRep2ID.intValue();
//}
//
//
//