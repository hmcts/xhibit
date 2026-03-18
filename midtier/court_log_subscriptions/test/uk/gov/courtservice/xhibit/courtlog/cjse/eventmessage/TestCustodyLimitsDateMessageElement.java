
package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.*;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.CustodyLimitsDateMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.*;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.*;



public class TestCustodyLimitsDateMessageElement extends TestCase
{
  private static final String xmlString =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='20200.xsd'>" +
      "  <E20200_Bail_And_Custody_Options>" +
      "    <E20200_BC_Type>E20200_Custody_limits_extended_to</E20200_BC_Type>" +
      "    <E20200_BC_Defendant_Name>William David Fardell</E20200_BC_Defendant_Name>" +
      "    <E20200_BC_Date>30-Jul-2003</E20200_BC_Date>" +
      "  </E20200_Bail_And_Custody_Options>" +
      "  <free_text>rerge</free_text>" +
      "  <type>20200</type>" +
      "</event>";

  public TestCustodyLimitsDateMessageElement(String s)
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
    CustodyLimitsDateMessageElement custodyLimitsDateMessageElement =
                                          new CustodyLimitsDateMessageElement();
    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setLogEntry(xmlString);
    value.setCourtLogViewValue(viewValue);

    // these elements not used in the message body
    XhbCase theCase = null;
    String stringRet =
        custodyLimitsDateMessageElement.getElement(value, theCase);
    assertEquals("30-Jul-2003",stringRet);
  }
}
