//package uk.gov.courtservice.business.services.witness.test;
//
//import javax.naming.NamingException;
//import junit.framework.Assert;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonController;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.*;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.CaseDetail;
//import uk.gov.courtservice.xhibit.business.entities.witness.XhbCaseValue;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.6 $
// *
// */
//public class TestCaseDetail  extends AbstractTestClass
//{
//  private static Logger log = CSServices.getLogger(TestCaseDetail.class);
//  private uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome home;
//  private uk.gov.courtservice.xhibit.business.services.witness.SkeletonController instance;
//  Integer intCASE_ID = new Integer(999);
//  Integer intDEF_ID_1 = new Integer(99998);
//  Integer intDEF_ID_2 = new Integer(99999);
//  Integer intDEF_ON_CASE_1 = new Integer(11111);
//  Integer intDEF_ON_CASE_2 = new Integer(11112);
//  float fltEstCaseDur = 5;
//  Integer intHearingID = new Integer(66666);
//  Integer intSchedHearID = new Integer(77777);
//  Integer intSittingID = new Integer(88888);
//  Integer intSHAttendeeId = new Integer(654321);
//
//
//  CaseDetail newCaseDetail = null;
//  SkeletonSchedule newSkeleton = null;
//
//  public TestCaseDetail(String s) throws NamingException
//    {
//        super(s);
//        home = (SkeletonControllerHome)
//        CSServices.getServiceLocator().getRemoteHome(SkeletonControllerHome.class);
//    }
//
//    public void setup() throws Exception
//    {
//      super.setUp();
//      instance = home.create();
//    }
//
//    public void testgetID()
//    {
//     log.debug("entered testgetID");
//      try
//      {
//      //set up case detail data
//     log.debug("call setUpCaseDetail");
//     setUpCaseDetail();
//     //need to create skeleton Schedule
//     log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//     newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//     log.debug("skel sched id =" + newSkeleton.getId() );
//     assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//     //create case detail
//     newCaseDetail =  newSkeleton.getCaseDetail();
//     log.debug("created new case detail");
//      //test stuff
//      log.debug("case detail case id (should be " +intCASE_ID + "=" + newCaseDetail.getId());
//      Integer intID = newCaseDetail.getId();
//      assertNotNull("ID must not be null", intID);
//      log.debug("exited testgetID");
//      }
//      catch (Exception e)
//      {
//        reportError(e);
//      }
//
//    }
//    public void testgetDefendantNames()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String[] strDefendantNames = newCaseDetail.getDefendantNames();
//        assertNotNull("1st Defendant name should not be null.strDefendantNames[0] =" + strDefendantNames[0],strDefendantNames[0]);
//      }
//      catch (Exception e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testgetCourtName()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("testgetCOurtName - call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        log.debug("test get court name - create newCaseDetail");
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        log.debug("test get court name - get court name");
//        String strCourtName = newCaseDetail.getCourtName();
//        assertNotNull("Court Name must not be null", strCourtName);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetCourtPrefix()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCourtPrefix = newCaseDetail.getCourtPrefix();
//        assertNotNull("Court Prefix must not be null", strCourtPrefix);
//        }
//        catch(Exception e)
//        {
//        reportError(e);
//      }
//    }
//    public void testgetCourtCode()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCourtCode = newCaseDetail.getCourtCode();
//        assertNotNull("Court Code must not be null", strCourtCode);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetBailMagCode()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strBailMagCode = newCaseDetail.getBailMagCode();
//        assertNotNull("BailMagCode must not be null", strBailMagCode);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetPoliceOfficerAttending()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strPoliceOfficerAttending = newCaseDetail.getPoliceOfficerAttending();
//        assertNotNull("PoliceOfficerAttending must not be null", strPoliceOfficerAttending);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetCpsCaseWorker()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCpsCaseWorker = newCaseDetail.getCpsCaseWorker();
//        assertNotNull("CPSCaseWorker must not be null", strCpsCaseWorker);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetCaseTitle()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCaseTitle = newCaseDetail.getCaseTitle();
//        assertNotNull("Case Title must not be null", strCaseTitle);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetCaseNumber()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCaseNumber = newCaseDetail.getCaseNumber();
//        assertNotNull("Case Number must not be null", strCaseNumber);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetCaseType()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCaseType = newCaseDetail.getCaseType();
//        assertNotNull("Case Type must not be null", strCaseType);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testsetCpsCaseWorker()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strCpsCaseWorker = "Brett Williams";
//        newCaseDetail.setCpsCaseWorker(strCpsCaseWorker);
//        //get cpscaseworker - must be equal to value set
//        assertEquals("CPSCaseWorkers values not the same", newCaseDetail.getCpsCaseWorker(), strCpsCaseWorker);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testsetPoliceOfficerAttending()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strPoliceOfficerAttending = "Brett Williams";
//        newCaseDetail.setPoliceOfficerAttending(strPoliceOfficerAttending);
//        //get cpscaseworker - must be equal to value set
//        assertEquals("PoliceOfficerAttending values not the same", newCaseDetail.getPoliceOfficerAttending(), strPoliceOfficerAttending);
//      }
//      catch(Exception e)
//      {
//        reportError(e);
//      }
//    }
//    public void testgetJudgeName()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        //test
//        String strJudgeName = newCaseDetail.getJudgeName();
//        assertNotNull("JudgeName must not be null", strJudgeName);
//      }
//      catch(NoJudgeForCaseException e)
//      {
//        reportError(e);
//      }
//      catch(ScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch (CaseNotFoundException e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testgetEstimatedCaseDuration()
//    {
//      try
//      {
//        //set up case detail data
//        log.debug("call setUpCaseDetail");
//        setUpCaseDetail();
//        //need to create skeleton Schedule
//        log.debug("Create Skeleton schedule. caseId = " + intCASE_ID);
//        newSkeleton = SkeletonScheduleFactory.getInstance().createSkeletonSchedule(intCASE_ID,true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + intCASE_ID + ")", newSkeleton);
//        //create CaseDetail
//        newCaseDetail =  newSkeleton.getCaseDetail();
//        float fltEstCaseDur2= newCaseDetail.getEstimatedCaseDuration();
//        float fltDelta = 0;
//        assertEquals("Est Case Duration not equal - fail",fltEstCaseDur, fltEstCaseDur2, fltDelta);
//      }
//      catch(NoDirectionsForCaseException e)
//      {
//        reportError(e);
//      }
//      catch(ScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch(CaseNotFoundException e)
//      {
//        reportError(e);
//      }
//    }
//    private void reportError(Exception e )
//    {
//      e.printStackTrace();
//          log.fatal(e);
//            Assert.fail(e.getMessage());
//    }
//
//    private void setUpCaseDetail()
//    {
//
//      try
//      {
//      log.debug("setUpCaseDetail entered");
//      //Connection con = null ;
//      Statement stmt = connection.createStatement();
//      log.debug("connection just created");
//      //Statement stmt = con.createStatement();
//      log.debug("setupcasedetail - after sql statement created");
//      //clear out DB just in case
//      int a = stmt.executeUpdate ("DELETE FROM XHB_DEFENDANT_ON_CASE where CASE_ID =" + intCASE_ID);
//      log.debug("sql=" + stmt.toString());
//      int b = stmt.executeUpdate ("DELETE FROM XHB_DEFENDANT where DEFENDANT_ID =" + intDEF_ID_1);
//      int c = stmt.executeUpdate ("DELETE FROM XHB_DEFENDANT where DEFENDANT_ID =" + intDEF_ID_2);
//      int e = stmt.executeUpdate("DELETE FROM XHB_SCHED_HEARING_ATTENDEE where SH_ATTENDEE_ID =" + intSHAttendeeId);
//      int f = stmt.executeUpdate("DELETE FROM XHB_SCHEDULED_HEARING where SCHEDULED_HEARING_ID =" + intSchedHearID);
//      int h = stmt.executeUpdate("DELETE FROM XHB_HEARING where HEARING_ID=" + intHearingID);
//      int d = stmt.executeUpdate("DELETE FROM XHB_SITTING where SITTING_ID=" + intSittingID);
//      int g = stmt.executeUpdate("DELETE FROM XHB_CASE where CASE_ID=" + intCASE_ID);
//      log.debug("sql=" + stmt.toString());
//      //create case
//      stmt.executeUpdate("INSERT INTO XHB_CASE (CASE_ID, CASE_NUMBER,CASE_TYPE,CASE_TITLE,BAIL_MAG_CODE,REF_COURT_ID,COURT_ID,CHARGE_IMPORT_INDICATOR,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION, POLICE_OFFICER_ATTENDING, CPS_CASE_WORKER)VALUES(999,20040001,'T','TEST CASE TITLE','BAIL_MAG_CODE TEST',404,3,'O',sysdate,sysdate,'brett test data','brett test data',1,'Police Officer','CPS Caseworker')");
//      System.out.println("sql=" + stmt.toString());
//      //create defendant data
//      stmt.executeUpdate("INSERT INTO XHB_DEFENDANT(DEFENDANT_ID, FIRST_NAME,MIDDLE_NAME,SURNAME,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,COURT_ID)VALUES(" +intDEF_ID_1 +" ,'Brett','Test','Defendant 1',sysdate,sysdate,'brett test data','brett test data',1,3)");
//      stmt.executeUpdate("INSERT INTO XHB_DEFENDANT(DEFENDANT_ID, FIRST_NAME,MIDDLE_NAME,SURNAME,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,COURT_ID)VALUES(" +intDEF_ID_2 +" ,'Brett','Test','Defendant 1',sysdate,sysdate,'brett test data','brett test data',1,3)");
//      //Create defendant on case data
//      stmt.executeUpdate("INSERT INTO XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID,CASE_ID, DEFENDANT_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION)VALUES(" + intDEF_ON_CASE_1 + "," + intCASE_ID+ "," + intDEF_ID_1 + ",sysdate,sysdate,'brett test data','brett test data',1)");
//      stmt.executeUpdate("INSERT INTO XHB_DEFENDANT_ON_CASE(DEFENDANT_ON_CASE_ID,CASE_ID, DEFENDANT_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION)VALUES(" + intDEF_ON_CASE_2 + "," + intCASE_ID+ "," + intDEF_ID_2 + ",sysdate,sysdate,'brett test data','brett test data',1)");
//      //Need a judge for the case (entries required in XHB_SITTING,XHB_HEARING, XHB_SCHED_HEARING,XHB_SCHED_HEARING_ATTENDEE
//      log.debug("INSERT INTO XHB_SITTING(SITTING_ID,IS_FLOATING,COURT_ROOM_ID,COURT_SITE_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION)VALUES("+intSittingID+",0,31,3,sysdate,sysdate,'brett test data','brett test data',1)");
//      stmt.executeUpdate("INSERT INTO XHB_SITTING(SITTING_ID,IS_FLOATING,COURT_ROOM_ID,COURT_SITE_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION)VALUES("+intSittingID+",0,31,3,sysdate,sysdate,'brett test data','brett test data',1)");
//      log.debug("INSERT INTO XHB_HEARING(HEARING_ID,CASE_ID,REF_HEARING_TYPE_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,COURT_ID)VALUES(" + intHearingID + "," + intCASE_ID + ",1,sysdate,sysdate,'brett test data','brett test data',1,3)");
//      stmt.executeUpdate("INSERT INTO XHB_HEARING(HEARING_ID,CASE_ID,REF_HEARING_TYPE_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,COURT_ID)VALUES(" + intHearingID + "," + intCASE_ID + ",1,sysdate,sysdate,'brett test data','brett test data',1,3)");
//      log.debug ("INSERT INTO XHB_SCHEDULED_HEARING(SCHEDULED_HEARING_ID,SITTING_ID,HEARING_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,IS_CASE_ACTIVE)VALUES(" +intSchedHearID + "," +intSittingID+"," +intHearingID+",sysdate,sysdate,'brett test data','brett test data',1,'N')");
//      stmt.executeUpdate("INSERT INTO XHB_SCHEDULED_HEARING(SCHEDULED_HEARING_ID,SITTING_ID,HEARING_ID,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY,VERSION,IS_CASE_ACTIVE)VALUES(" +intSchedHearID + "," +intSittingID+"," +intHearingID+",sysdate,sysdate,'brett test data','brett test data',1,'N')");
//      log.debug("INSERT INTO XHB_SCHED_HEARING_ATTENDEE(SH_ATTENDEE_ID,ATTENDEE_TYPE,SCHEDULED_HEARING_ID,VERSION,LAST_UPDATED_BY,CREATED_BY,CREATION_DATE,LAST_UPDATE_DATE)VALUES("+intSHAttendeeId+",'J',"+intSchedHearID+",1,'brett test data','brett test data',sysdate,sysdate)");
//      stmt.executeUpdate("INSERT INTO XHB_SCHED_HEARING_ATTENDEE(SH_ATTENDEE_ID,ATTENDEE_TYPE,SCHEDULED_HEARING_ID,VERSION,LAST_UPDATE_DATE,CREATION_DATE,CREATED_BY,LAST_UPDATED_BY)VALUES("+intSHAttendeeId+",'J',"+intSchedHearID+",1,sysdate,sysdate,'brett test data','brett test data')");
//      stmt.close();
//      log.debug("setUpCaseDetail exit");
//      }
//      catch(SQLException e)
//      {
//        e.printStackTrace();
//        log.error(e);
//            throw new CSUnrecoverableException(e);
//      }
//    }
//}
//
//
//