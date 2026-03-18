//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
//
//
//public class TestReservedToJudgeMessageElement extends TestCase
//{
//  private String xmlString =
//      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//      "<event xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"30200.xsd\">" +
//      " <type>30200</type>" +
//      " <date>2002-11-25</date>" +
//      " <time/>" +
//      " <E30200_Long_Adjourn_Options>" +
//      "         <LAO-Type>E30200_Case_reserved_to_enter_Judge's_name</LAO-Type>" +
//      "         <E30200_LAO_Name>Judge Rebecca M Poulet</E30200_LAO_Name>" +
//      " </E30200_Long_Adjourn_Options>" +
//      "</event>";
//
//  public TestReservedToJudgeMessageElement(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testGetElement()
//  {
//    ReservedToJudgeMessageElement reservedToJudgeMessageElement =
//                                            new ReservedToJudgeMessageElement();
//
//    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    viewValue.setLogEntry(xmlString);
//    value.setCourtLogViewValue(viewValue);
//
//    // these attributes are not used in the message body
//    XhbCase theCase = null;
//    String stringRet = reservedToJudgeMessageElement.getElement(value, theCase);
//    assertEquals("Judge Rebecca M Poulet", stringRet);
//  }
//}
//