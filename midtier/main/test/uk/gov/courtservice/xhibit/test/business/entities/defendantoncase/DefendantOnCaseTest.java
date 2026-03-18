//package uk.gov.courtservice.xhibit.test.business.entities.defendantoncase;
//
//// jdk
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Vector;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
//import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseHome;
//
//
//public class DefendantOnCaseTest extends TestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  private DefendantOnCaseHome home = (DefendantOnCaseHome)CSServices.getServiceLocator().getLocalHome(DefendantOnCaseHome.class);
//
//  public DefendantOnCaseTest(String s)
//  {
//      super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//
//  }
//
//  protected void tearDown()throws Exception
//  {
//
//  }
//
//  public void testFindByCaseId() throws Exception
//  {
//    log("testFindByCaseId() start");
//    try
//    {
//      // try a caseId with only one defendantOnCase
//      Collection defOnCases = home.findByCaseId(new Integer(6));
//      assertEquals(defOnCases.size(), 1);
//      Iterator it = defOnCases.iterator();
//      DefendantOnCase defOnCase = (DefendantOnCase)it.next();
//      assertEquals(defOnCase.getDefendantOnCaseId().intValue(), 1);
//
//      // try a caseId with many defendantOnCases
//      Collection defOnCases2 = home.findByCaseId(new Integer(18));
//      assertEquals(defOnCases2.size(), 3);
//
//      Vector defOnCaseIds = new Vector();
//      Iterator it2 = defOnCases2.iterator();
//      defOnCase = (DefendantOnCase)it2.next();
//      defOnCaseIds.add(defOnCase.getDefendantOnCaseId());
//      defOnCase = (DefendantOnCase)it2.next();
//      defOnCaseIds.add(defOnCase.getDefendantOnCaseId());
//      defOnCase = (DefendantOnCase)it2.next();
//      defOnCaseIds.add(defOnCase.getDefendantOnCaseId());
//      assertTrue(defOnCaseIds.contains(new Integer(14)));
//      assertTrue(defOnCaseIds.contains(new Integer(15)));
//      assertTrue(defOnCaseIds.contains(new Integer(16)));
//
//      // try an invalid caseId
//      Collection defOnCases3 = home.findByCaseId(new Integer(111));
//      assertEquals(defOnCases3.size(), 0);
//      log("\n**********\ndefOnCases3.size() = " + defOnCases3.size());
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        fail();
//    }
//  }
//
//  public void testFindByDefendantAndCase() throws Exception
//  {
//    log("testFindByDefendantAndCase() start");
//    try
//    {
//      // try a search with a result
//      DefendantOnCase defOnCase = home.findByDefendantAndCase(new Integer(11), new Integer(19));
//      assertEquals(defOnCase.getDefendantOnCaseId().intValue(), 17);
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        fail();
//    }
//
//    try
//    {
//      // try a search without a result
//      DefendantOnCase defOnCase = home.findByDefendantAndCase(new Integer(36), new Integer(6));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  public void testFindByKeyAndVersion() throws Exception
//  {
//    log("testFindByKeyAndVersion() start");
//    try
//    {
//      DefendantOnCase defOnCase1 = home.findByPrimaryKey(new Integer(7));
//      // try a search with a result
//      DefendantOnCase defOnCase = home.findByKeyAndVersion(new Integer(7), defOnCase1.getVersion());
//      assertEquals(defOnCase.getDefendantOnCaseId().intValue(), 7);
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        fail();
//    }
//
//    try
//    {
//      // try a search without a result
//      DefendantOnCase defOnCase = home.findByKeyAndVersion(new Integer(20), new Integer(5));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  private void log(String msg)
//  {
//      log.debug(msg);
//  }
//}
//