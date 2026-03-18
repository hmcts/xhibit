//package uk.gov.courtservice.business.services.witness.test;
//
//import java.rmi.RemoteException;
//import java.sql.Time;
//import java.util.Date;
//
//import javax.naming.NamingException;
//
//import junit.framework.Assert;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.witness.XhbSkeletonDeliveryStatusValue;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonController;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome;
//import uk.gov.courtservice.xhibit.business.services.witness.WitnessController;
//import uk.gov.courtservice.xhibit.business.services.witness.WitnessControllerHome;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CaseNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.CouldNotCreateSessionException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonDayHasDateException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonScheduleModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.SkeletonSessionNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionModificationException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.TrialSessionNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessCreationException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
//
//public class TestSkeletonControllerBean extends AbstractTestClass
//{
//    private SkeletonControllerHome home;
//    private SkeletonController instance;
//    private WitnessControllerHome witController;
//    private WitnessController witInstance;
//
//    public TestSkeletonControllerBean(String s) throws NamingException
//    {
//        super(s);
//        home = (SkeletonControllerHome)
//               CSServices.getServiceLocator().getRemoteHome(SkeletonControllerHome.class);
//
//        witController = (WitnessControllerHome)
//                        CSServices.getServiceLocator().getRemoteHome(WitnessControllerHome.class);
//
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        instance = home.create();
//        witInstance = witController.create();
//
//    }
//
//    public void testCreateSkeletonSchedule()
//    {
//        try
//        {
//            SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//            assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", skeletonSchedule);
//
//        }
//        catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//        catch (java.rmi.RemoteException e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//    }
//
//
//    public void testGetSkeletonSchedule()
//    {
//        try
//        {
//            SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//            assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", skeletonSchedule);
//            SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//            assertNotNull("Got a null pointer from getSkeletonSchedule(" + NEW_CASE_ID + ")", skeleton);
//
//        }
//        catch (uk.gov.courtservice.xhibit.business.services.witness.exceptions.ScheduleModificationException e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//        catch (java.rmi.RemoteException e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//        catch (ScheduleNotFoundException e)
//        {
//           handleException(e);
//        }
//    }
//
//    public void testCreateTrialSession()
//    {
//      /*public TrialSession createTrialSession(final Integer scheduleId,
//                                               final Integer dayNumber,
//                                               final String session,
//                                               final Date trialDate)
//      throws ScheduleModificationException, CouldNotCreateSessionException, SkeletonDayHasDateException*/
//      try
//      {  instance.createSkeletonSchedule(NEW_CASE_ID, true);
//	 SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//         assertNotNull("skeletonSchedule", skeleton);
//         Integer scheduleId = skeleton.getId();
//	 Integer daynumber = new Integer(10);
//	 String session = "M";
//	 Date date = null;
//
//	 TrialSession trial = instance.createTrialSession(scheduleId, daynumber, session, date);
//	 assertNotNull("Trial session not created for sheduleID, daynumber, session, date "
//		       +scheduleId+" "+ daynumber+" "+ session+" "+ date, trial);
//	 assertTrue("Id", trial.getId().intValue() > 0 );
//	 assertTrue("day number", trial.getDayNumber() == daynumber.intValue());
//	 assertEquals("session ", session, trial.getSessionType());
//      }
//      catch(ScheduleModificationException sme)
//      {
//	reportError(sme);
//      }
//      catch(CouldNotCreateSessionException cncse)
//      {
//	reportError(cncse);
//      }
//      catch(SkeletonDayHasDateException shde)
//      {
//	reportError(shde);
//      }
//      catch(ScheduleNotFoundException e )
//      {
//	reportError(e);
//      }
//      catch(RemoteException rmi)
//      {
//	reportError(rmi);
//      }
//
//      //instance.createTrialSession();
//    }
//
//    public void testGetDeliveryStatusValues()
//    {
//      try
//      {
//	//there are at least four of these so far
//	XhbSkeletonDeliveryStatusValue[] res = instance.getDeliveryStatusValues();
//
//	//check that something was returned
//	assertNotNull("Delivery Status returned is null", res);
//	assertTrue(res.length > 0);
//      }
//      catch(RemoteException e )
//      {
//        reportError(e);
//      }
//    }
//
//    public void testGetIssuedSkeletonSchedules() throws Exception
//    //create skel sched, issue skel sched, get skelsched
//    {
//      try
//      {
//        //create skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //set skeleton schedule issue
//        skeletonSchedule.isIssued();
//        //create another (for a diff case) and set another as issued
//        Integer intNew_Case_Id_2 = new Integer(11);
//        SkeletonSchedule skeletonSchedule2 = instance.createSkeletonSchedule(intNew_Case_Id_2, true);
//        skeletonSchedule2.isIssued();
//        //get issued skel schedules
//        SkeletonSchedule[] skeleton = instance.getIssuedSkeletonSchedules(new Integer(3));
//        //is null returned then fail
//        assertNotNull("No Issued skeleton schedules returned - error",skeleton[0]);
//        assertTrue("Check that Issued sched array length is greater than 0", skeleton.length > 0);
//      }
//      catch(java.rmi.RemoteException e )
//     {
//       reportError(e);
//      }
//      catch(ScheduleModificationException e )
//      {
//        reportError(e);
//      }
//    }
//
//    public void testGetTrialSessionBySessionId()
//    {
//      Integer sessionID = new Integer(1);
//
//      try
//      {
//        TrialSession newTrialSession = instance.getTrialSession(sessionID);
//        assertNotNull("No Trial session returned", newTrialSession);
//      }
//        catch( TrialSessionNotFoundException e )
//      {
//        reportError(e);
//      }
//      catch ( java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testGetTrialSessionsByCaseId()
//        //BW - actually create a schedule, and sessions so this can be tested properly
//    {
//      try
//      {
//      //create skeleton schedule
//      SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//      //get skeleton schedule
//      SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//      //create TrialSessions - hanging from schedule
//      Integer scheduleId1 = skeleton.getId();
//      int schedID = scheduleId1.intValue();
//      Integer daynumber = new Integer(10);
//      String session = "M";
//      Date date = null;
//      skeleton.createTrialSession (daynumber, session);
//      session = "P";
//      skeleton.createTrialSession(daynumber,session);
//      //get Trial Session with CaseID
//      TrialSession[] newTrialSessions = instance.getTrialSessions(NEW_CASE_ID);
//      //test that value returned
//      assertNotNull("No Trial sessions returned"  , newTrialSessions);
//      }
//      catch (java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//
//      catch (ScheduleNotFoundException e)
//      {
//        reportError(e);
//      }
//
//      catch (ScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch (CouldNotCreateSessionException e)
//      {
//        reportError(e);
//      }
//      catch (SkeletonDayHasDateException e)
//     {
//       reportError(e);
//      }
//    }
//    public void testGetTrialSessionsByCaseAndDuration()
//  {
//      try
//      {
//        //create skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //get skeleton schedule
//        SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//        //create TrialSessions
//        Integer scheduleId = skeleton.getId();
//        Integer daynumber = new Integer(10);
//        String session = "M";
//        Date date = null;
//        TrialSession trial1 = instance.createTrialSession(scheduleId, daynumber, session, date);
//        Integer scheduleId2 = skeleton.getId();
//        Integer daynumber2 = new Integer(11);
//        String session2 = "M";
//        Date date2 = null;
//        TrialSession trial2 = instance.createTrialSession(scheduleId2, daynumber2, session2, date2);
//        //get trial sessions - think that with two day sessions then duration will be 2
//        float fltDuration = 2;
//        TrialSession[] newTrialSessions = instance.getTrialSessions(NEW_CASE_ID, fltDuration);
//        //test that value returned
//        assertNotNull("No Trial sessions returned", newTrialSessions);
//      }
//    catch (java.rmi.RemoteException e)
//    {
//      reportError(e);
//    }
//
//    catch (ScheduleNotFoundException e)
//    {
//      reportError(e);
//    }
//
//    catch (ScheduleModificationException e)
//    {
//      reportError(e);
//    }
//    catch (CouldNotCreateSessionException e)
//    {
//      reportError(e);
//    }
//    catch (SkeletonDayHasDateException e)
//   {
//     reportError(e);
//    }
//  }
//
//    public void testGetTrialSessionByCaseidDaySession()
//    {
//      try
//      {
//        //create skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //get skeleton schedule
//        SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//        //create TrialSessions
//        Integer scheduleId = skeleton.getId();
//        Integer daynumber = new Integer(10);
//        String session = "M";
//        Date date = null;
//        TrialSession trial1 = instance.createTrialSession(scheduleId, daynumber, session, date);
//        Integer scheduleId2 = skeleton.getId();
//        Integer daynumber2 = new Integer(11);
//        String session2 = "M";
//        Date date2 = null;
//        TrialSession trial2 = instance.createTrialSession(scheduleId2, daynumber2, session2, date2);
//        //get trial sessions - use daysession
//        String strSession = "M";
//        Integer intDay = new Integer(10);
//        TrialSession newTrialSession = instance.getTrialSession(NEW_CASE_ID,intDay, strSession);
//        //test that value returned
//        assertNotNull("No Trial session returned", newTrialSession);
//      }
//    catch (java.rmi.RemoteException e)
//    {
//      reportError(e);
//    }
//
//    catch (ScheduleNotFoundException e)
//    {
//      reportError(e);
//    }
//
//    catch (ScheduleModificationException e)
//    {
//      reportError(e);
//    }
//    catch (CouldNotCreateSessionException e)
//    {
//      reportError(e);
//    }
//    catch (SkeletonDayHasDateException e)
//   {
//     reportError(e);
//    }
//    catch (TrialSessionNotFoundException e)
//   {
//     reportError(e);
//    }
//  }
//
//    public void testHasSessionWitnesses()
//    //does a session have witnesses
//    //create skel schedule, create, session, add witnesses to session.
//    {
//      try
//   {
//     //create skeleton schedule
//     SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//     //get skeleton schedule
//     SkeletonSchedule skeleton = instance.getSkeletonSchedule(NEW_CASE_ID);
//     //create TrialSession
//     Integer scheduleId = skeleton.getId();
//     Integer daynumber = new Integer(10);
//     String session = "M";
//     Date date = null;
//     skeleton.createTrialSession(daynumber,session,date);
//     //Add Witness to sesssion - need to use 'createWitnessSession' in TestWitnessControllerBean?
//     Integer sessionId = new Integer(4);
//     String witnessFullname = "Brett Witness";
//     String witnessType = "Prosecution";
//     String witnessStatus = "Expert";
//     Integer intAge = new Integer(30);
//     Time expectArrivalTime = new Time(240000);
//     String notes = "ABCDEFGHIJKL";
//     //Integer integer, Integer integer1, String string, String string3, String string4, int int5, Time time, String string7
//     //WitnessSession witSession = witInstance.createWitnessSession(NEW_CASE_ID,sessionId,witnessFullname,witnessType,witnessStatus,intAge,expectArrivalTime,notes);
//     WitnessSession witSession = witInstance.createWitnessSession
//     (NEW_CASE_ID, sessionId, witnessFullname, witnessType, witnessStatus, intAge.intValue(), expectArrivalTime, notes);
//     //get trial session - use daysession
//     String strSession = "M";
//     Integer intDay = new Integer(10);
//     TrialSession newTrialSession = instance.getTrialSession(NEW_CASE_ID,intDay, strSession);
//     //need to create a witness session - part of WitnessControllerBean
//   }
//   catch (CaseNotFoundException e)
//   {
//     reportError(e);
//   }
//   catch (WitnessCreationException e)
//   {
//     reportError(e);
//   }
//   catch (java.rmi.RemoteException e)
//   {
//     reportError(e);
//   }
//   catch (ScheduleNotFoundException e)
//   {
//     reportError(e);
//   }
//   catch (ScheduleModificationException e)
//   {
//     reportError(e);
//   }
//   catch (TrialSessionNotFoundException e)
//   {
//     reportError(e);
//   }
//
//   catch (SkeletonSessionNotFoundException e)
//   {
//     reportError(e);
//   }
//   catch (CouldNotCreateSessionException e)
//      {
//        reportError(e);
//   }
//   catch (SkeletonDayHasDateException e)
//      {
//        reportError(e);
//   }
//}
//
//    public void testRemoveSkeletonSchedule()
//    {
//      try
//      {
//
//       // create then remove skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        assertNotNull("Got a null pointer from createSkeletonSchedule(" + NEW_CASE_ID + ")", skeletonSchedule);
//        Integer skelID = skeletonSchedule.getId();
//        //remove skel schedule
//        instance.removeSkeletonSchedule(skelID);
//        //try and get something attached to the deleted skel sched - this should be null
//        assertNull("No case details for skeleton schedule ID:" + skelID + " should be returned",instance.getSkeletonSchedule(skelID));
//
//      }
//      catch (java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//      catch (ScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch (SkeletonScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch (ScheduleNotFoundException e)
//      {
//        reportError(e);
//      }
//    }
//
//
//    public void testRemoveTrialSession()
//    {
//
//      try{
//        //create skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //create trial session
//        Integer intDayNumber = new Integer(8);
//        String session = "M";
//        TrialSession newTrialSession =  skeletonSchedule.createTrialSession(intDayNumber,session);
//        Integer intSessionID = newTrialSession.getId();
//        //check that this ID came back
//        assertNotNull("This sessionid = " + intSessionID, intSessionID);
//        String strSessID = intSessionID.toString();
//        assertTrue("sessionID string length is not null",strSessID.length()>0);
//        //remove trial session
//        instance.removeTrialSession(newTrialSession,true);
//        //try and get trial session with that ID - should fail
//        assertNull("Error - No trial session with this ID should be returned",instance.getTrialSession(intSessionID));
//
//      }
//
//      catch (java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//      catch (ScheduleModificationException e)
//      {
//        reportError(e);
//      }
//      catch (SkeletonDayHasDateException e)
//      {
//        reportError(e);
//      }
//      catch (CouldNotCreateSessionException e)
//      {
//        reportError(e);
//      }
//      catch (TrialSessionModificationException e)
//      {
//        reportError(e);
//      }
//      catch (TrialSessionNotFoundException e)
//     {
//       //this is what is supposed to happen as no trial session is available - it has been removed!
//       //do not report this error
//      }
//    }
//
//    public void testUpdateSkeletonSchedule()
//    {
//      try{
//        //create skeleton schedule
//        System.out.println("testUpdateSkeletonSchedule() NEW_CASE_ID = " + NEW_CASE_ID);
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //get the id of created schedule
//        Integer intSchedId = skeletonSchedule.getId();
//        assertNotNull("This schedule is null, it shouldn't be ", intSchedId);
//        //update some values in skeletonschedule
//        String strStatus = "READY";
//        skeletonSchedule.setDeliveryStatus(strStatus);
//        //check that delivery status has been updated
//        assertEquals("Delivery status' should be equal - they are not",strStatus,skeletonSchedule.getDeliveryStatus());
//        //update skeletonschedule
//        instance.updateSkeletonSchedule(skeletonSchedule);
//        //get the skeletonschedule
//        skeletonSchedule = instance.getSkeletonSchedule(NEW_CASE_ID);
//        assertNotNull("skeleton schedule should have been returned - it was not. intSchedId =" + intSchedId, skeletonSchedule);
//        //test fails if deliverystatus is not 'READY'
//        String strRetrievedStatus = skeletonSchedule.getDeliveryStatus();
//        assertEquals("Delivery status' should be equal - they are not",strStatus,strRetrievedStatus);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//      catch(ScheduleModificationException e)
//        {
//          reportError(e);
//      }
//      catch(ScheduleNotFoundException e)
//        {
//          reportError(e);
//      }
//      catch(ModificationException e)
//        {
//          reportError(e);
//      }
//    }
//
//    public void testUpdateTrialSession()
//    {
//      try
//      {
//      //create skeleton schedule
//        SkeletonSchedule skeletonSchedule = instance.createSkeletonSchedule(NEW_CASE_ID, true);
//        //create trial session
//        Integer intDayNumber = new Integer(8);
//        String session = "M";
//        TrialSession newTrialSession =  skeletonSchedule.createTrialSession(intDayNumber,session);
//        Integer intSessionID = newTrialSession.getId();
//        //update trial session details
//        String strNotes = "Junit Test Notes";
//        newTrialSession.setNotes(strNotes);
//        //update trial session
//        instance.updateTrialSession(newTrialSession);
//        //get trial session
//        newTrialSession = instance.getTrialSession(intSessionID);
//        //test to see if notes were updated.
//        assertEquals(strNotes,newTrialSession.getNotes());
//      }
//      catch (java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//      catch (ScheduleModificationException e)
//      {
//       reportError(e);
//      }
//      catch (SkeletonDayHasDateException e)
//      {
//       reportError(e);
//      }
//      catch (CouldNotCreateSessionException e)
//      {
//      reportError(e);
//      }
//      catch (TrialSessionModificationException e)
//      {
//      reportError(e);
//      }
//      catch (TrialSessionNotFoundException e)
//      {
//      reportError(e);
//      }
//    }
//
//    private void reportError(Exception e )
//    {
//      e.printStackTrace();
//	  log.fatal(e);
//            Assert.fail(e.getMessage());
//    }
//
//
//
//
//
//}
//