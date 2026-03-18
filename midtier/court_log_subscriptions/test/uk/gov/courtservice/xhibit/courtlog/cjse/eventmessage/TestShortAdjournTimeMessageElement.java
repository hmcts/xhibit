
package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.*;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.ShortAdjournTimeMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.*;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.*;



public class TestShortAdjournTimeMessageElement extends TestCase
{
  private static final String xmlString =
      "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
      "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='30100.xsd'>" +
      "  <free_text/>" +
      "  <type>30100</type>" +
      "  <E30100_Short_Adjourn_Options>" +
      " <E30100_SAO_Time>12:15</E30100_SAO_Time>" +
      "  <E30100_SAO_Type>E30100_Case_released_until</E30100_SAO_Type>" +
      "  </E30100_Short_Adjourn_Options>" +
      "</event>";

  public TestShortAdjournTimeMessageElement(String s)
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
    ShortAdjournTimeMessageElement shortAdjournTimeMessageElement =
                                           new ShortAdjournTimeMessageElement();
    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setLogEntry(xmlString);
    value.setCourtLogViewValue(viewValue);

    XhbCase theCase =  null;
    String stringRet =
        shortAdjournTimeMessageElement.getElement(value, theCase);
    assertEquals("Incorrect short adjourn time retrieved from log entry",
                 "12:15", stringRet);
  }
}
