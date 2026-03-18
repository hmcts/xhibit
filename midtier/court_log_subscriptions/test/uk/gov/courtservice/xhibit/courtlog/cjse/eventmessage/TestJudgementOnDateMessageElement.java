//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.JudgementOnDateMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//
//public class TestJudgementOnDateMessageElement extends TestCase
//{
//
//  public TestJudgementOnDateMessageElement(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testGetElement()
//  {
//    JudgementOnDateMessageElement judgementOnDateMessageElement =
//                                            new JudgementOnDateMessageElement();
//    CourtLogSubscriptionValue value = null;
//    XhbCase theCase = null;
//    String stringRet =
//        judgementOnDateMessageElement.getElement(value, theCase);
//    assertEquals("Date Not Recorded", stringRet);
//  }
//}
//