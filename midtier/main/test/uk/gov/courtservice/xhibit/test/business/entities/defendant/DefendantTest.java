//package uk.gov.courtservice.xhibit.test.business.entities.defendant;
//
//// jdk
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
//import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantHome;
//
//
//public class DefendantTest extends TestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  private DefendantHome home = (DefendantHome)CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
//
//  public DefendantTest(String s)
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
//    try
//    {
//      Defendant defendant1 = home.findByPrimaryKey(new Integer(5));
//      // try a search with a result
//      Defendant defendant = home.findByKeyAndVersion(new Integer(5), defendant1.getVersion());
//      assertEquals(5, defendant.getDefendantId().intValue());
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
//      Defendant defendant = home.findByKeyAndVersion(new Integer(200), new Integer(5));
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