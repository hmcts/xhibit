
package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.*;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.LongAdjournDateMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.*;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.*;



public class TestLongAdjournDateMessageElement extends TestCase
{
  private static final String xmlString =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='30200.xsd'>" +
      "  <E30200_Long_Adjourn_Options>" +
      "    <E30200_LAO_PSR_Deft_ID/>" +
      "    <E30200_LAO_Date>31-Jul-2003</E30200_LAO_Date>" +
      "    <E30200_LAO_Name/>" +
      "   <E30200_LAO_Type>E30200_Case_to_be_listed_on</E30200_LAO_Type>" +
      "  </E30200_Long_Adjourn_Options>" +
      "  <free_text/>" +
      "  <type>30200</type>" +
      "</event>";

  public TestLongAdjournDateMessageElement(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testGetElement()
  {
    LongAdjournDateMessageElement longAdjournDateMessageElement =
                                            new LongAdjournDateMessageElement();
    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setLogEntry(xmlString);
    value.setCourtLogViewValue(viewValue);

    XhbCase theCase =  null;
    String stringRet =
        longAdjournDateMessageElement.getElement(value, theCase);
    assertEquals("Incorrect long adjourn date retrieved from log entry",
                 "31-Jul-2003", stringRet);
  }
}
