//package uk.gov.courtservice.business.services.witness.test;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.services.CSServices;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Hashtable;
//import java.sql.ResultSet;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author qzd3k3
// *
// */
//public class WitnessTestHelper
//{
//    private static Logger log = CSServices.getLogger(WitnessTestHelper.class);
//
//
//    public static void init()
//    {
//    }
//
//
//    public static void initializeTerminalAndDate(String terminalName,
//                                                 Connection con)
//    {
//        init();
////        try
////        {
////            Statement stmt = con.createStatement();
////            int i = stmt.executeUpdate("update xhb_hearing_list set start_date = trunc(sysdate), end_date = trunc(sysdate) where list_id = 6");
////            int j = stmt.executeUpdate("update xhb_scheduled_hearing set not_before_time = sysdate, original_time = trunc(sysdate) where sitting_id in (select sitting_id from xhb_sitting where list_id = 6)");
////            int l = stmt.executeUpdate("delete from xhb_terminal where terminal_name='" + terminalName + "'");
////            int k = stmt.executeUpdate("insert into xhb_terminal (LOCATION, TERMINAL_IP, terminal_name, COURT_ROOM_ID, COURTROOM_OR_SITE, COURT_SITE_ID ) values ('My Location', 'Ha Ha', '" + terminalName + "', 6, 'CR', 1)");
////            stmt.close();
////        }
////        catch (SQLException e)
////        {
////            log.error(e);
////            throw new CSUnrecoverableException(e);
////        }
//
//    }
//
//
//    public static void setUpWitnessData(String day1, int case1, int case2, int case3, int scheduledHearingId,
//                                        Connection con)
//    {
//        init();
//        try
//        {
//            Date today = new Date();
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//            if (day1 == null)
//            {
//                day1 = format.format(today);
//                log.debug("setUpWitnessData() using date:" + day1);
//            }
//            Statement stmt = con.createStatement();
//            log.debug("setUpWitnessData() : " + 1 + "a");
//            stmt.executeUpdate("DELETE FROM XHB_WITNESS WHERE WITNESS_ID = -1");
//            stmt.executeUpdate("DELETE FROM XHB_SKELETON_SESSION WHERE SKELETON_SESSION_ID < 0");
//            stmt.executeUpdate("DELETE FROM XHB_SKELETON_DAY WHERE SKELETON_DAY_ID < 0");
//            stmt.executeUpdate("DELETE FROM XHB_SKELETON_SCHEDULE WHERE SKELETON_ID < 0");
//
//            stmt.executeUpdate("DELETE FROM XHB_CASE WHERE CASE_ID < 0");
////            stmt.executeUpdate("DELETE FROM XHB_CR_LIVE_STATUS");
////            stmt.executeUpdate("DELETE FROM XHB_DIRECTIONS_FOR_CASE WHERE CASE_ID =10");
////            stmt.executeUpdate("DELETE FROM XHB_DIRECTIONS_FOR_CASE WHERE CASE_ID =9");
//
//            stmt.executeUpdate("INSERT INTO XHIBIT.XHB_CASE ( CASE_ID,CASE_NUMBER,CASE_TYPE,MAG_CONVICTION_DATE,CASE_SUB_TYPE,CASE_TITLE,CASE_DESCRIPTION,LINKED_CASE_ID,BAIL_MAG_CODE,REF_COURT_ID,COURT_ID,CHARGE_IMPORT_INDICATOR,SEVERED_IND,INDICT_RESP,DATE_IND_REC,PROS_AGENCY_REFERENCE,CASE_CLASS,JUDGE_REASON_FOR_APPEAL,RESULTS_VERIFIED,LENGTH_TAPE,NO_PAGE_PROS_EVIDENCE,NO_PROS_WITNESS,EST_PDH_TRIAL_LENGTH,INDICTMENT_INFO_1,INDICTMENT_INFO_2,INDICTMENT_INFO_3,INDICTMENT_INFO_4,INDICTMENT_INFO_5,INDICTMENT_INFO_6,POLICE_OFFICER_ATTENDING,CPS_CASE_WORKER,EXPORT_CHARGES,IND_CHANGE_STATUS,MAGISTRATES_CASE_REF,CLASS_CODE,OFFENCE_GROUP_UPDATE,CCC_TRANS_TO_REF_COURT_ID,RECEIPT_TYPE ) VALUES (-1,12345678,'T',NULL,' ',NULL,' ',NULL,'JOINDER-DEFENDANT-NAME00000000',1291,1,'O','N',NULL,NULL,'CPS Ref: S3(1)',NULL,NULL,'N',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'S3 (1)',4,NULL,NULL,'CT')");
//            stmt.executeUpdate("INSERT INTO XHIBIT.XHB_CASE ( CASE_ID,CASE_NUMBER,CASE_TYPE,MAG_CONVICTION_DATE,CASE_SUB_TYPE,CASE_TITLE,CASE_DESCRIPTION,LINKED_CASE_ID,BAIL_MAG_CODE,REF_COURT_ID,COURT_ID,CHARGE_IMPORT_INDICATOR,SEVERED_IND,INDICT_RESP,DATE_IND_REC,PROS_AGENCY_REFERENCE,CASE_CLASS,JUDGE_REASON_FOR_APPEAL,RESULTS_VERIFIED,LENGTH_TAPE,NO_PAGE_PROS_EVIDENCE,NO_PROS_WITNESS,EST_PDH_TRIAL_LENGTH,INDICTMENT_INFO_1,INDICTMENT_INFO_2,INDICTMENT_INFO_3,INDICTMENT_INFO_4,INDICTMENT_INFO_5,INDICTMENT_INFO_6,POLICE_OFFICER_ATTENDING,CPS_CASE_WORKER,EXPORT_CHARGES,IND_CHANGE_STATUS,MAGISTRATES_CASE_REF,CLASS_CODE,OFFENCE_GROUP_UPDATE,CCC_TRANS_TO_REF_COURT_ID,RECEIPT_TYPE ) VALUES (-2,12345678,'T',NULL,' ',NULL,' ',NULL,'JOINDER-DEFENDANT-NAME00000000',1291,2,'O','N',NULL,NULL,'CPS Ref: S3(1)',NULL,NULL,'N',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'S3 (1)',4,NULL,NULL,'CT')");
//            stmt.executeUpdate("INSERT INTO XHIBIT.XHB_CASE ( CASE_ID,CASE_NUMBER,CASE_TYPE,MAG_CONVICTION_DATE,CASE_SUB_TYPE,CASE_TITLE,CASE_DESCRIPTION,LINKED_CASE_ID,BAIL_MAG_CODE,REF_COURT_ID,COURT_ID,CHARGE_IMPORT_INDICATOR,SEVERED_IND,INDICT_RESP,DATE_IND_REC,PROS_AGENCY_REFERENCE,CASE_CLASS,JUDGE_REASON_FOR_APPEAL,RESULTS_VERIFIED,LENGTH_TAPE,NO_PAGE_PROS_EVIDENCE,NO_PROS_WITNESS,EST_PDH_TRIAL_LENGTH,INDICTMENT_INFO_1,INDICTMENT_INFO_2,INDICTMENT_INFO_3,INDICTMENT_INFO_4,INDICTMENT_INFO_5,INDICTMENT_INFO_6,POLICE_OFFICER_ATTENDING,CPS_CASE_WORKER,EXPORT_CHARGES,IND_CHANGE_STATUS,MAGISTRATES_CASE_REF,CLASS_CODE,OFFENCE_GROUP_UPDATE,CCC_TRANS_TO_REF_COURT_ID,RECEIPT_TYPE ) VALUES (-3,12345678,'T',NULL,' ',NULL,' ',NULL,'JOINDER-DEFENDANT-NAME00000000',1291,3,'O','N',NULL,NULL,'CPS Ref: S3(1)',NULL,NULL,'N',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'S3 (1)',4,NULL,NULL,'CT')");
//
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SCHEDULE(skeleton_id,skeleton_delivery_status_id,CASE_ID,DELIVERABLE )VALUES(-1,1," + case1 + ",'Y') ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-1,1,to_date('" + day1 + "', 'dd-mm-yyyy'),1,-1 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-1,'M','First witness for first day',-1,-1 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-1,'Basil Fawlty','Juvenile',12,'PROSECUTION',null,to_date('10:00', 'HH24:MI'),null,null,null,null,null,null,-1," + case1 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-2,'Arthur Askey','Police Officer',48,'PROSECUTION',null,to_date('10:00', 'HH24:MI'),null,null,null,null,null,null,-1," + case1 + ") ");
//
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SCHEDULE(skeleton_id,skeleton_delivery_status_id,CASE_ID,DELIVERABLE )VALUES(-2,null," + case2 + ",'N') ");
//
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-2,1,to_date('" + day1 + "', 'dd-mm-yyyy'),1,-2 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-2,'M',null,-2,-2 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-3,'Atilla the Hun','Ordinary',null,'PROSECUTION',null,to_date('10:00', 'HH24:MI'),null,null,null,null,'Watch out - history of unstable behaviour',null,-2," + case2 + ") ");
//
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-4,'Sybil Boggins','Ordinary',37,'PROSECUTION',null,to_date('11:30', 'HH24:MI'),null,null,null,null,null,null,-2," + case2 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-3,'A',null,-2,-2 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-5,'Leonard Nimoy','Ordinary',61,'PROSECUTION',null,to_date('14:00', 'HH24:MI'),null,null,null,null,null,null,-3," + case2 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SCHEDULE(skeleton_id,skeleton_delivery_status_id,CASE_ID,DELIVERABLE )VALUES(-3,null," + case3 + ",'N') ");
//
//
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-4,1,to_date('" + day1 + "', 'dd-mm-yyyy'),-1,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-4,'M',null,-4,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-6,'Carmina Detroit','Expert',18,'PROSECUTION',null,to_date('10:00', 'HH24:MI'),null,null,null,null,null,null,-4," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-7,'Horace Walpole','Ordinary',286,'PROSECUTION',null,to_date('10:00', 'HH24:MI'),null,null,null,null,null,null,-4," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-5,'A',null,-4,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-8,'Daphne Jane Doe','Ordinary',21,'PROSECUTION',null,to_date('16:30', 'HH24:MI'),null,null,null,null,null,null,-5," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-5,2,null,-1,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-6,'M',null,-5,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-9,'Terry Thatcher','Expert',null,'PROSECUTION',null,to_date('12:30', 'HH24:MI'),null,null,null,null,'Shifty type - could be MI6',null,-6," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-6,3,null,-1,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-7,'A',null,-6,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-10,'Doug Climie','Ordinary',45,'PROSECUTION',null,to_date('16:30', 'HH24:MI'),null,null,null,null,'Honest upright person doing their duty',null,-7," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-7,4,null,-1,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-8,'M',null,-7,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-11,'Louis Pasteur','Expert',181,'PROSECUTION',null,to_date('10:30', 'HH24:MI'),null,null,null,null,'Clean-living type',null,-8," + case3 + ") ");
//
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_DAY(skeleton_day_id,day_number, skeleton_date,WEEK_NUMBER,skeleton_id )VALUES(-8,5,null,-1,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_SKELETON_SESSION (skeleton_session_id,morning_or_afternoon,notes,skeleton_day_id,skeleton_id )             VALUES(-9,'M',null,-8,-3 ) ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-12,'Michael Jackson','Expert',181,'PROSECUTION',null,to_date('10:30', 'HH24:MI'),null,null,null,null,'Clean-living type',null,-8," + case3 + ") ");
//            stmt.executeUpdate("INSERT INTO XHB_WITNESS (witness_id,name,status,age,witness_type,actual_arrival_date_time,EXPECTED_ARRIVAL_TIME,released_date_time,mobileNumber,pagerNumber,pagerNet,notes,calculated_witness_time,session_id,CASE_ID)VALUES(-13,'Michael Jacksons son','Expert',18,'PROSECUTION',null,to_date('10:30', 'HH24:MI'),null,null,null,null,'Clean-living type',null,-8," + case3 + ") ");
////
////            stmt.executeUpdate("INSERT INTO XHB_CR_LIVE_STATUS(COURT_ROOM_ID, SCHEDULED_HEARING_ID, TIME_STATUS_SET) VALUES(6," + scheduledHearingId + ",sysdate)");
////
////            stmt.executeUpdate("INSERT INTO XHB_DIRECTIONS_FOR_CASE(CASE_ID,TRIAL_TIME_ESTIMATE,last_update_date,creation_date,created_by,last_updated_by,version) VALUES(10,12,sysdate,sysdate,user,user,1)");
////            stmt.executeUpdate("INSERT INTO XHB_DIRECTIONS_FOR_CASE(CASE_ID,TRIAL_TIME_ESTIMATE,last_update_date,creation_date,created_by,last_updated_by,version) VALUES(9,12,sysdate,sysdate,user,user,1)");
//
//            stmt.close();
////
//        }
//        catch (SQLException e)
//        {
//            log.error(e);
//            throw new CSUnrecoverableException(e);
//        }
//
//    }
//
//
//    public void testDummyMethod()
//    {  ;
//    }
//
//
//}
//