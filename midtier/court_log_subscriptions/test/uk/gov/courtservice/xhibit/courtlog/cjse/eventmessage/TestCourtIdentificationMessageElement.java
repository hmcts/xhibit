//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.CourtIdentificationMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
///**
// * <p>Title: TestCourtIdentificationMessageElement</p>
// * <p>Description: Tests the <code>CourtIdentificationMessageElement</code>
// * which builds the court identification section of the CJSE message</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Sarah Tong
// * @version $Id: TestCourtIdentificationMessageElement.java,v 1.3 2006/07/13 12:58:00 xzfdtb Exp $
// */
//public class TestCourtIdentificationMessageElement extends TestCase
//{
//  // values from the standard test data set
//  private static final Integer caseId = new Integer(1);
//  // Court Room 1, Isleworth
//  private static final Integer courtRoomId = new Integer(31);
//
//  public TestCourtIdentificationMessageElement(String s)
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
//  /**
//   * Test with court and court room both provided
//   */
//  public void testGetElement()
//  {
//    CourtIdentificationMessageElement courtIdMessageElement =
//                                        new CourtIdentificationMessageElement();
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
//    CourtLogSubscriptionValue clsValue = new CourtLogSubscriptionValue();
//    // this is the only attribute used in the method body
//    clsValue.setCourtRoomId(courtRoomId);
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    clsValue.setCourtLogViewValue(viewValue);
//
//    String stringRet = courtIdMessageElement.getElement(clsValue, theCase);
//
//    assertEquals("Isleworth, court 1", "ISLEW-1", stringRet);
//  }
//
//  /**
//   * Test with court provided but court room unknown (e.g. event generated out
//   * of court)
//   */
//  public void testGetElementUnknowCourt()
//  {
//    CourtIdentificationMessageElement courtIdMessageElement =
//                                        new CourtIdentificationMessageElement();
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
//    CourtLogSubscriptionValue clsValue = new CourtLogSubscriptionValue();
//    // don't specify the court room
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    clsValue.setCourtLogViewValue(viewValue);
//
//    String stringRet = courtIdMessageElement.getElement(clsValue, theCase);
//
//    assertEquals("Isleworth, unknown court", "ISLEW-U", stringRet);
//  }
//}
//