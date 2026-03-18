//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//// j2ee
//import javax.ejb.ObjectNotFoundException;
//
//// jdk
//import javax.naming.NamingException;
//
//// framework
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//
//// xhibit
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.DefendantNameMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
//public class TestDefendantNameMessageElement extends TransactionTestCase
//{
//  // values from the standard test data set
//  private static final Integer defendantOnCaseId = new Integer(11);
//  private static final Integer caseId = new Integer(40);
//
//  public TestDefendantNameMessageElement(String s) throws NamingException
//  {
//    super(s, true);
//  }
//
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//  }
//
//  public void testGetElement()
//  {
//      CourtLogViewValue viewValue = new CourtLogViewValue();
//      viewValue.setDefendantOnCaseId(defendantOnCaseId);
//      CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//      DefendantNameMessageElement defNameMessageElement =
//                                              new DefendantNameMessageElement();
//
//    // create a case reference to pass
//    XhbCase theCase = null;
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
//    String stringRet = defNameMessageElement.getElement(value, theCase);
//    assertEquals("TEST T DEFENDANT T20028897-3", stringRet);
//  }
//}
//