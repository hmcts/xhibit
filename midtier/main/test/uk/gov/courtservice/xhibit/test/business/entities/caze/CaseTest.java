//package uk.gov.courtservice.xhibit.test.business.entities.caze;
//
//// jdk
//import java.util.Collection;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.caze.Case;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
//
//
//public class CaseTest extends TestCase
//{
//  private Logger log =  CSServices.getLogger(getClass());
//  private CaseHome home = (CaseHome)CSServices.getServiceLocator().getLocalHome(CaseHome.class);
//
//  public CaseTest(String s)
//  {
//      super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//    TestUtils.execSql("insert into xhb_linked_case (linked_case_id) values (1)");
//    TestUtils.execSql("insert into xhb_linked_case (linked_case_id) values (2)");
//    TestUtils.execSql("update xhb_case set linked_case_id = 1 where case_id = 6");
//    TestUtils.execSql("update xhb_case set linked_case_id = 2 where case_id = 7");
//    TestUtils.execSql("update xhb_case set linked_case_id = 2 where case_id = 8");
//  }
//
//  protected void tearDown()throws Exception
//  {
//    TestUtils.execSql("update xhb_case set linked_case_id = 0");
//    TestUtils.execSql("delete from xhb_linked_case");
//  }
//
//  public void testFindByKeyAndVersion() throws Exception
//  {
//    log("testFindByKeyAndVersion() start");
//    try
//    {
//      Case case1 = home.findByPrimaryKey(new Integer(12));
//      // try a search with a result
//      Case caze = home.findByKeyAndVersion(new Integer(12), case1.getVersion());
//      assertEquals(12, caze.getCaseId().intValue());
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
//      Case caze = home.findByKeyAndVersion(new Integer(200), new Integer(5));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  public void testFindByCaseIdTypeAndCourt() throws Exception
//  {
//    log("testFindByCaseIdTypeAndCourt start()");
//
//    try
//    {
//      // try a search with a result
//      Case caze = home.findByCaseIdTypeAndCourt(new Integer(6), "A", new Integer(1));
//      assertEquals(6, caze.getCaseId().intValue());
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
//      Case defOnCase = home.findByCaseIdTypeAndCourt(new Integer(6), "A", new Integer(11));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  public void testFindByNumberTypeAndCourt() throws Exception
//  {
//    log("testFindByNumberTypeAndCourt start()");
//
//    try
//    {
//      // try a search with a result
//      Case caze = home.findByNumberTypeAndCourt(new Integer(20020960), "T", new Integer(1));
//      assertEquals(14, caze.getCaseId().intValue());
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
//      Case defOnCase = home.findByNumberTypeAndCourt(new Integer(30020960), "T", new Integer(1));
//      fail();
//
//    }
//    catch(Exception e)
//    {
//      assertTrue(e instanceof ObjectNotFoundException);
//    }
//  }
//
//  public void testFindByLinkedCaseId() throws Exception
//  {
//    log("testFindByLinkedCaseId start()");
//
//    try
//    {
//      // try a search with one result
//      Collection cases = home.findByLinkedCaseId(new Integer(1));
//      assertEquals(1, cases.size());
//      Case caze = (Case)cases.iterator().next();
//      assertEquals(6, caze.getCaseId().intValue());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//
//    try
//    {
//      // try a search with > 1 result
//      Collection cases = home.findByLinkedCaseId(new Integer(2));
//      assertEquals(2, cases.size());
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
//      Collection cases = home.findByLinkedCaseId(new Integer(30020960));
//      assertEquals(0,cases.size());
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  private void log(String msg)
//  {
//      log.debug(msg);
//  }
//}
//