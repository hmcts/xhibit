//package uk.gov.courtservice.xhibit.test.business.entities.defendantreference;
//
//// jdk
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReference;
//import uk.gov.courtservice.xhibit.business.entities.defendantreference.DefendantReferenceHome;
//
//
//public class DefendantReferenceTest extends TestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  private DefendantReferenceHome home = (DefendantReferenceHome)CSServices.getServiceLocator().getLocalHome(DefendantReferenceHome.class);
//
//  public DefendantReferenceTest(String s)
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
//  public void testFindByKeyAndVersion() throws Exception
//  {
//    log("testFindByKeyAndVersion() start");
//
//    try
//    {
//      DefendantReference defendantreference1 = home.findByPrimaryKey(new Integer(1));
//      // try a search with a result
//      DefendantReference defendantreference = home.findByKeyAndVersion(new Integer(1), defendantreference1.getVersion());
//      assertEquals(1, defendantreference.getDefRefId().intValue());
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
//      DefendantReference defendantreference = home.findByKeyAndVersion(new Integer(200), new Integer(5));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  public void testFindByDefendantIdAndReferenceName() throws Exception
//  {
//    log("testFindByDefendantIdAndReferenceName() start");
//
//    try
//    {
//      // try a search with a result
//      DefendantReference defendantreference1 = home.findByDefendantIdAndReferenceName(new Integer(10), "Prisoner Location");
//      assertEquals(1, defendantreference1.getDefRefId().intValue());
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
//      DefendantReference defendantreference1 = home.findByDefendantIdAndReferenceName(new Integer(1), "Invalid Reference Name");
//      fail();
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//
//    log("testFindByDefendantIdAndReferenceName() end");
//  }
//
//  private void log(String msg)
//  {
//      log.debug(msg);
//  }
//}
//