//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
///**
// * <p>Title: Util class used for Loading Data Only </p>
// * <p>Description: Loads and cleans data required for Public Display
// *    unit tests </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Pat Fox
// * @version 1.0
// */
//public class HearingScheduleTestDataLoader {
//
//  final static String [] LOAD_DATA ={
//    "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, ADDRESS_ID, CREST_IP_ADDRESS, IN_SERVICE_FLAG, OBS_IND, PROBATION_OFFICE_NAME, INTERNET_COURT_NAME, DISPLAY_NAME ) VALUES ( 9999, 'Funky Court', 'WEST', 'BATCOURT', '999', 'PAT COURT', 'PFCOU', 80, 'CSA00110:90', 'Y','N', 'BAT COURT OFFICE', 'BAT CROWN COURT','BAT CROWN COURT')",
//    "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, DISPLAY_NAME ) VALUES (99999 , 'BATFORD', 'A', 9999, 80,  'BAT ford Court Site A' )",
//    "INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES (999991, 'Court 1', 'Court Room 1', 66, 99999, 'N', 'Court Room 1')",
//    "INSERT INTO XHB_COURT_ROOM (COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, CREST_COURT_ROOM_NO, COURT_SITE_ID, OBS_IND, DISPLAY_NAME ) VALUES (999992, 'Court 2', 'Court Room 2', 67, 99999, 'N', 'Court Room 2')",
//    "insert into Xhb_hearing_list(LIST_ID, START_DATE, END_DATE,STATUS,CREST_LIST_ID,COURT_ID) values (100,trunc(sysdate),trunc(sysdate),1,100,9999)",
//    "insert into xhb_sitting(SITTING_ID,IS_FLOATING,LIST_ID,COURT_ROOM_ID,COURT_SITE_ID,REF_JUDGE_ID) values ( 998, '0', 100, 999991 ,99999,668)",
//    "insert into xhb_sitting(SITTING_ID,IS_FLOATING,LIST_ID,COURT_ROOM_ID,COURT_SITE_ID,REF_JUDGE_ID) values ( 999, '0', 100, 999992 ,99999,668)",
//    "insert into Xhb_hearing (HEARING_ID,CASE_ID,REF_HEARING_TYPE_ID,COURT_ID ) Values ( 10000, 1, 1, 9999)",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10001,101,sysdate, trunc(sysdate),  1,  998, 10000,'Y')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10002,102,sysdate, trunc(sysdate),  1,  998, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10003,103,sysdate, trunc(sysdate),  1,  998, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10004,104,sysdate, trunc(sysdate),  1,  998, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10005,105,sysdate, trunc(sysdate),  1,  998, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10021,101,sysdate, trunc(sysdate),  1,  999, 10000,'Y')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10022,102,sysdate, trunc(sysdate),  1,  999, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10023,103,sysdate, trunc(sysdate),  1,  999, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10024,104,sysdate, trunc(sysdate),  1,  999, 10000,'N')",
//    "insert into xhb_scheduled_hearing (SCHEDULED_HEARING_ID,SEQUENCE_NO,NOT_BEFORE_TIME,ORIGINAL_TIME,HEARING_PROGRESS,SITTING_ID,HEARING_ID,IS_CASE_ACTIVE) Values (10025,105,sysdate, trunc(sysdate),  1,  999, 10000,'N')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1001,999991,10001,trunc(sysdate),'Public Display Test Status 1')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1002,999991,10002,trunc(sysdate),'Public Display Test Status 2')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1003,999991,10003,trunc(sysdate),'Public Display Test Status 3')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1004,999991,10004,trunc(sysdate),'Public Display Test Status 4')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1005,999991,10005,trunc(sysdate),'Public Display Test Status 5')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1006,999992,10021,trunc(sysdate),'Public Display Test Status 6')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1007,999992,10022,trunc(sysdate),'Public Display Test Status 7')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1008,999992,10023,trunc(sysdate),'Public Display Test Status 8')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1009,999992,10024,trunc(sysdate),'Public Display Test Status 9')",
//    "insert into xhb_cr_live_status (CR_LIVE_STATUS_ID, COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET, PUBLIC_DISPLAY_STATUS) values (1010,999992,10025,trunc(sysdate),'Public Display Test Status 10')",
//    "insert into xhb_case_reference (CASE_REFERENCE_ID, REPORTING_RESTRICTIONS,CASE_ID) values ( 100, 0, 2)"
//  };
//
//  final static String [] REMOVE_DATA = {
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1001",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1002",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1003",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1004",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1005",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1006",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1007",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1008",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1009",
//    "delete from xhb_cr_live_status  where CR_LIVE_STATUS_ID = 1010",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10025",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10024",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10023",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10022",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10021",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10005",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10004",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10003",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10002",
//    "delete from xhb_scheduled_hearing where SCHEDULED_HEARING_ID = 10001",
//    "delete from Xhb_hearing where HEARING_ID=10000",
//    "delete from xhb_sitting where SITTING_ID=998",
//    "delete from xhb_sitting where SITTING_ID=999",
//    "delete from Xhb_hearing_list where list_ID=100",
//    "delete from XHB_COURT_ROOM where COURT_ROOM_ID=999991",
//    "delete from XHB_COURT_ROOM where COURT_ROOM_ID=999992",
//    "delete from XHB_COURT_SITE where COURT_SITE_ID=99999",
//    "delete from XHB_COURT where COURT_ID = 9999",
//    "delete from xhb_case_reference where CASE_REFERENCE_ID = 100"
//  };
//
//
//  public static void load() throws Exception {
//
//    Logger log = CSServices.getLogger(HearingScheduleTestDataLoader.class);
//
//    log.debug("LOAD DATA");
//
//
//    // load the data
//    for( int i =0 ; i < LOAD_DATA.length; i++){
//
//      log.debug("Running SQL statement number["+i+"]" + LOAD_DATA[i]);
//      TestUtils.execSql(LOAD_DATA[i]);
//    }
//
//  }
//
//  public static void clean()throws Exception{
//
//    Logger log = CSServices.getLogger(HearingScheduleTestDataLoader.class);
//    log.debug("CLEANING DATA");
//
//    // remove the data
//
//    for( int i =0 ; i < REMOVE_DATA.length; i++){
//
//      log.debug("Running SQL statement number ["+i+"]" + REMOVE_DATA[i]);
//      TestUtils.execSql(REMOVE_DATA[i]);
//    }
//
//
//  }
//
//
//}