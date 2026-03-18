//package uk.gov.courtservice.business.services.witness.test;
//
//import javax.naming.NamingException;
//
//import junit.framework.Assert;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.WitnessSelectorHome;
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.NoScheduleForCaseException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
//
//public class TestWitnessSelectorBean extends AbstractTestClass
//{
//    private uk.gov.courtservice.xhibit.business.services.witness.WitnessSelectorHome home;
//    private uk.gov.courtservice.xhibit.business.services.witness.WitnessSelector instance;
//
//
//    public TestWitnessSelectorBean(String s) throws NamingException
//    {
//        super(s);
//        home = (WitnessSelectorHome)
//               CSServices.getServiceLocator().getRemoteHome(WitnessSelectorHome.class);
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        instance = home.create();
//    }
//
//
//    public void testGetTodaysWitnesses()
//    {
//        try
//        {
//            WitnessSession[] todaysWitnesses = instance.getTodaysWitnesses(CASE_ID);
//            for (int i = 0; i < todaysWitnesses.length; i++)
//            {
//                WitnessSession todaysWitness = todaysWitnesses[i];
//                log.debug("WitnessSummary[" + i + "] = " + todaysWitness);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//
//    }
//
//
//    public void testGetFutureWitnesses()
//    {
//        try
//        {
//            WitnessSession[] todaysWitnesses = instance.getFutureWitnesses(CASE_ID);
//            for (int i = 0; i < todaysWitnesses.length; i++)
//            {
//                WitnessSession todaysWitness = todaysWitnesses[i];
//                log.debug("WitnessSummary[" + i + "] = " + todaysWitness);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            log.fatal(e);
//            Assert.fail(e.getMessage());
//        }
//
//
//    }
//
//
//    public void testGetSignedInWitnesses()
//    {
//        try
//        {
//            WitnessSession[] signedInWitnesses = instance.getSignedInWitnesses(CASE_ID, new Integer(1));
//            log.info("Values returned from getSignedInWitnesses()");
//            for (int i = 0; i < signedInWitnesses.length; i++)
//            {
//                WitnessSession signedInWitness = signedInWitnesses[i];
//                log.debug(signedInWitness);
//            }
//        }
//        catch (java.rmi.RemoteException e)
//        {
//            reportError(e);
//        }
//    }
//
//    public void testAreWitnessesInWeek()
//    {
//      int intWeek = 1;
//      try
//      {
//      boolean blnWitnessesInWeek = instance.areWitnessesInWeek(CASE_ID, intWeek);
//      assertTrue("Error - There are witnesses recorded for first week",blnWitnessesInWeek);
//      }
//      catch (Exception e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testAreWitnessesOnCase()
//    {
//      try
//      {
//      boolean blnWitnessesOnCase = instance.areWitnessesOnCase(CASE_ID);
//      assertTrue("Error - There are witnesses on the case",blnWitnessesOnCase);
//      }
//      catch (Exception e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testGetAllWitnesses()
//    {
//      try
//      {
//        WitnessSession[] allWitnesses = instance.getAllWitnesses(CASE_ID);
//        assertNotNull("Error - there are witnesses on the case", allWitnesses);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//      catch (NoScheduleForCaseException  e)
//      {
//        reportError(e);
//      }
//    }
//
//    public void testGetPastWitnesses()
//    {
//      try
//      {
//        WitnessSession[] pastWitnesses = instance.getPastWitnesses(CASE_ID);
//        assertNotNull("Error - there are past witnesses",pastWitnesses);
//      }
//      catch(NoScheduleForCaseException e)
//      {
//        reportError(e);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//    }
//    public void testGetTodayAndFutureWitnesses()
//    {
//      try
//      {
//        WitnessSession[] todayFutureWitnesses = instance.getTodayAndFutureWitnesses(CASE_ID);
//        assertNotNull("Error - there today and future witnesses",todayFutureWitnesses);
//      }
//      catch(NoScheduleForCaseException e)
//      {
//        reportError(e);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//    }
//    public void testGetWitnessesForDay()
//    {
//      try
//      {
//        WitnessSession[] dayWitnesses = instance.getWitnessesForDay(CASE_ID,1);
//        assertNotNull("Error - witnesses exist for specified day",dayWitnesses);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//    }
//    public void testGetWitnessesForWeek()
//    {
//      try
//      {
//        WitnessSession[] weekWitnesses = instance.getWitnessesForWeek(CASE_ID,1);
//        assertNotNull("Error - witnesses exist for specified week",weekWitnesses);
//      }
//      catch(java.rmi.RemoteException e)
//      {
//        reportError(e);
//      }
//    }
//    public void testSetSessionContext()
//    {
//
//    }
//
//
//    private void reportError(Exception e )
//      {
//        e.printStackTrace();
//            log.fatal(e);
//              Assert.fail(e.getMessage());
//    }
//}
//