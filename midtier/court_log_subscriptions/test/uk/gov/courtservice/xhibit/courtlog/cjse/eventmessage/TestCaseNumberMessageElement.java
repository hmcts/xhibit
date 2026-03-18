//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import junit.framework.*;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.CaseNumberMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.*;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.*;
//
//import javax.ejb.ObjectNotFoundException;
//
//
//
//public class TestCaseNumberMessageElement extends TestCase
//{
//  // values from the standard test data set
//  private static final Integer caseId = new Integer(1);
//
//  public TestCaseNumberMessageElement(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//    // none required
//  }
//
//  protected void tearDown()
//  {
//    // none required
//  }
//
//  public void testGetElement()
//  {
//    CaseNumberMessageElement caseNumMessageElement = new CaseNumberMessageElement();
//
//    XhbCase theCase = null;
//
//    // create a case reference to pass
//    try
//    {
//      theCase = XhbCaseBeanHelper.findByPrimaryKey(caseId);
//    }
//    catch (ObjectNotFoundException ex)
//    {
//      System.out.println("Exception: Couldn't find case " + caseId);
//      ex.printStackTrace();
//      fail();
//    }
//
//    // these parameters are not used in the method body
//    CourtLogSubscriptionValue value =  null;
//
//    String stringRet = caseNumMessageElement.getElement(value, theCase);
//    assertEquals("T20028779", stringRet);
//  }
//}
//