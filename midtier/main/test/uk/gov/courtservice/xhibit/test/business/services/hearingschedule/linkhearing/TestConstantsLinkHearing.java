//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.linkhearing;
//
///**
// * <p>Title: TestConstants</p>
// * <p>Description: Test scripts for building up the linked hearing tests.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//
//public interface TestConstantsLinkHearing {
//
//  //keys
//  static final Integer CASE_ID = new Integer(920);
//  static final Integer REF_HRG_TYPE = new Integer(9003);
//  static final Integer HRG_LIST_ID = new Integer(906);
//  static final Integer SITTING_ID = new Integer(907);
//  static final Integer SH_HRG_1_ID = new Integer(908);
//  static final Integer SH_HRG_2_ID = new Integer(909);
//  static final Integer COURT_ID = new Integer(9991);
//  static final Integer COURT_SITE_ID = new Integer(9991);
//  static final Integer COURT_ROOM_ID = new Integer(9992);
//  static final Integer REF_COURT_ID = new Integer(99993);
//  static final Integer ADDRESS_ID = new Integer(9994);
//  static final Integer HRG_1_ID = new Integer(904);
//  static final Integer HRG_2_ID = new Integer(905);
//  static final Integer HRG_3_ID = new Integer(906);
//  static final Integer HRG_4_ID = new Integer(907);
//  static final Integer HRG_5_ID = new Integer(908);
//  static final Integer HRG_6_ID = new Integer(909);
//  static final Integer HRG_7_ID = new Integer(910);
//  static final Integer L_HRG_1_ID = new Integer(911);
//  static final Integer L_HRG_2_ID = new Integer(912);
//  static final Integer L_HRG_3_ID = new Integer(913);
//
//
//  //new
//  static final Integer CASE_ID2 = new Integer(921);
//  static final Integer CASE_ID3 = new Integer(922);
//  static final Integer HRG_8_ID = new Integer(923);
//  static final Integer HRG_9_ID = new Integer(924);
//  static final Integer HRG_10_ID = new Integer(925);
//  static final Integer HRG_11_ID = new Integer(926);
//  static final Integer REF_HRG_TYPE2 = new Integer(927);
//  static final Integer REF_HRG_TYPE3 = new Integer(928);
//
//
//  //court
//  static final String DEL_COURT = "delete from xhb_court where court_id = "+COURT_ID.intValue();
//  static final String INS_COURT = "insert into xhb_court (COURT_ID, COURT_TYPE, COURT_NAME, CREST_COURT_ID, COURT_PREFIX,"+
//                                 "CREST_IP_ADDRESS, IN_SERVICE_FLAG, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME) values ("+
//                                 COURT_ID.intValue() +", 'CR', 'Chelmsford', '123', 'CH', '123.456.789.1', 'Y', 'Probation off.', 'CH-internet')";
// //address
//  static final String DEL_ADDR = "delete from xhb_address where address_id = "+ADDRESS_ID.intValue();
//  static final String INS_ADDR = "insert into xhb_address (address_id, town) values ("+ADDRESS_ID.intValue()+", 'London')";
//
// //courtSite
//  static final String INS_COURT_SITE = "insert into xhb_court_site (court_site_id, court_id, address_id) values ("+
//                                     COURT_SITE_ID.intValue() +", "+COURT_ID.intValue()+", "+ADDRESS_ID.intValue()+")";
//  static final String DEL_COURT_SITE = "delete from xhb_court_site where court_site_id = " + COURT_SITE_ID.intValue() ;
//
// //courtroom
//  static final String INS_COURT_ROOM = "insert into xhb_court_room (court_room_id, crest_court_room_no, court_site_id) values ("+
//                                     COURT_ROOM_ID.intValue()+ ", 7, " + COURT_SITE_ID.intValue() +")";
//  static final String DEL_COURT_ROOM = "delete from xhb_court_room where court_room_id = "+COURT_ROOM_ID.intValue();
//
//  //refCourt
//  static final String INS_REF_COURT = "insert into xhb_ref_court (ref_court_id, court_full_name, court_id) values ("+
//                                    REF_COURT_ID.intValue() +", 'BAS magistrates court', "+COURT_ID.intValue()+")";
//  static final String DEL_REF_COURT = "delete from xhb_ref_court where ref_court_id = "+REF_COURT_ID.intValue();
//
//  //Case
//  static final String DEL_CASE = "delete from xhb_case where court_id = " + COURT_ID.intValue();
//  static final String INS_CASE = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID.intValue()+", 20030310, 'T', "+REF_COURT_ID.intValue()+", "+COURT_ID.intValue()+")";
//  //new
//  static final String INS_CASE2 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID2.intValue()+", 2003222, 'S', "+REF_COURT_ID.intValue()+", "+COURT_ID.intValue()+")";
//  static final String INS_CASE3 = "insert into xhb_case (case_id, case_number, case_type, ref_court_id, court_id) values ("+CASE_ID3.intValue()+", 20032323, 'A', "+REF_COURT_ID.intValue()+", "+COURT_ID.intValue()+")";
//
//
//  //refhearing type
//  static final String DEL_REF_HRG_TYPE = "delete from xhb_ref_hearing_type where ref_hearing_type_id  = "+REF_HRG_TYPE.intValue()+" or ref_hearing_type_id = "+REF_HRG_TYPE2.intValue()+" or ref_hearing_type_id = "+REF_HRG_TYPE3.intValue();
//  static final String INS_REF_HRG_TYPE = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+REF_HRG_TYPE.intValue()+", 'TRI', 'TRIAL', "+COURT_ID.intValue()+")";
//  //new
//  static final String INS_REF_HRG_TYPE2 = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+REF_HRG_TYPE2.intValue()+", 'SEN', 'Sentence', "+COURT_ID.intValue()+")";
//  static final String INS_REF_HRG_TYPE3 = "insert into xhb_ref_hearing_type (ref_hearing_type_id, hearing_type_code, hearing_type_desc, court_id) values ("+REF_HRG_TYPE3.intValue()+", 'APP', 'APPEAL', "+COURT_ID.intValue()+")";
//
//  //Linked hearings
//  static final String DEL_L_HRG = "delete from xhb_linked_hearing where linked_hearing_id = "+L_HRG_1_ID.intValue()+" or linked_hearing_id = "+L_HRG_2_ID.intValue()+" or linked_hearing_id = "+L_HRG_3_ID.intValue();
//  static final String INS_L_HRG_1 = "insert into xhb_linked_hearing (linked_hearing_id) values ("+L_HRG_1_ID.intValue()+")";
//  static final String INS_L_HRG_2 = "insert into xhb_linked_hearing (linked_hearing_id) values ("+L_HRG_2_ID.intValue()+")";
//  static final String INS_L_HRG_3 = "insert into xhb_linked_hearing (linked_hearing_id) values ("+L_HRG_3_ID.intValue()+")";
//
//
//  //hearings
//  static final String DEL_HRG = "delete from xhb_hearing where case_id = "+CASE_ID.intValue()+" or case_id = "+CASE_ID2.intValue();
//
//  static final String INS_HRG_1 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_1_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_1_ID.intValue()+")";
//
//  static final String INS_HRG_2 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_2_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_1_ID.intValue()+")";
//
//  static final String INS_HRG_3 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_3_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_2_ID.intValue()+")";
//
//  static final String INS_HRG_4 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_4_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_3_ID.intValue()+")";
//
//  static final String INS_HRG_5 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_5_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_3_ID.intValue()+")";
//
//  static final String INS_HRG_6 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_6_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_3_ID.intValue()+")";
//
//  static final String INS_HRG_7 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_7_ID.intValue()+", "+CASE_ID.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", null)";
//
//
//  //new
//  static final String INS_HRG_8 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_8_ID.intValue()+", "+CASE_ID2.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_3_ID.intValue()+")";
//
//  static final String INS_HRG_9 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_9_ID.intValue()+", "+CASE_ID2.intValue()+", "+REF_HRG_TYPE.intValue()+", "+COURT_ID.intValue()+", "+L_HRG_3_ID.intValue()+")";
//
//  static final String INS_HRG_10 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_10_ID.intValue()+", "+CASE_ID2.intValue()+", "+REF_HRG_TYPE2.intValue()+", "+COURT_ID.intValue()+", null)";
//
//  static final String INS_HRG_11 = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id, court_id, linked_hearing_id)"+
//                                  "values ("+HRG_11_ID.intValue()+", "+CASE_ID2.intValue()+", "+REF_HRG_TYPE3.intValue()+", "+COURT_ID.intValue()+", null)";
//
//  static final String removeAllExportAs = "delete from xhb_exporta";
//
//  static final String insertExportAReadyforExport_HRG = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "hearing_id) values ('Marie', 'R', "+HRG_8_ID.intValue()+")";
//
//  static final  String insertExportAReadyforExport_L_HRG = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                       "linked_hearing_id) values ('Marie', 'R', "+L_HRG_3_ID.intValue()+")";
//
//}