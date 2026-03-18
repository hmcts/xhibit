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
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.DefendantNamesMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
//import java.util.Vector;
//
//public class TestDefendantNamesMessageElement extends TransactionTestCase
//{
//  // values from the standard test data set
//  private static final Integer defendantOnCaseId = new Integer(11);
//  private static final Integer caseId = new Integer(40);
//
//  private Vector defNamesAndOthers = new Vector();
//
//  public TestDefendantNamesMessageElement(String s) throws NamingException
//  {
//    super(s, true);
//    // don't know which defendant on the case the msg element will pick
//    // so could be any of these
//    defNamesAndOthers.add("TEST T DEFENDANT T20028897-1 and others");
//    defNamesAndOthers.add("TEST T DEFENDANT T20028897-2 and others");
//    defNamesAndOthers.add("TEST T DEFENDANT T20028897-3 and others");
//
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
//    DefendantNamesMessageElement defNamesMessageElement =
//                                              new DefendantNamesMessageElement();
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
//    String stringRet = defNamesMessageElement.getElement(value, theCase);
//    assertTrue(defNamesAndOthers.contains(stringRet));
//  }
//}
//