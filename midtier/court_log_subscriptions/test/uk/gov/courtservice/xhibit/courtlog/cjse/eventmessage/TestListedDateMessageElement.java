
package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.ListedDateMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;



/**
 * <p>Title: TestListedDateMessageElement</p>
 * <p>Description: Tsts the listed date message element</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Sarah Tong
 * @version $Id: TestListedDateMessageElement.java,v 1.1 2004/04/20 14:43:53 pznwc5 Exp $
 */
public class TestListedDateMessageElement extends TestCase
{
  private static final String xmlStringDirections =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
      "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='40713.xsd'>" +
      "  <free_text/>" +
      "  <type>40713</type>" +
      "  <Directions_By_Case_Options>" +
      "         <E40713_List>E40713_Sentence</E40713_List>" +
      "         <E40713_Placed_In>" +
      "                <E40713_Placed_In_List_Options>E40713_Fixed_List</E40713_Placed_In_List_Options>" +
      "                <E40713_List_Date>17-Nov-2003</E40713_List_Date>" +
      "         </E40713_Placed_In>" +
      "  </Directions_By_Case_Options>" +
      "</event>";


  private static final String xmlStringLongAdjourn =
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

  public TestListedDateMessageElement(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  /**
   * Tests the getElement method when a Directions for Case event is supplied
   */
  public void testGetElementFromDirections()
  {
    ListedDateMessageElement listedDateMessageElement =
                                                 new ListedDateMessageElement();
    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setLogEntry(xmlStringDirections);
    viewValue.setEventType(new Integer(40713));
    value.setCourtLogViewValue(viewValue);

    XhbCase theCase =  null;
    String stringRet =
        listedDateMessageElement.getElement(value, theCase);
    assertEquals("Incorrect listed date retrieved from the log entry",
                 "17-Nov-2003", stringRet);
  }

  /**
   * Tests the getElement method when a Long Adjourn event is supplied
   */
  public void testGetElementFromLongAdjourn()
  {
    ListedDateMessageElement listedDateMessageElement =
                                                 new ListedDateMessageElement();
    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setLogEntry(xmlStringLongAdjourn);
    viewValue.setEventType(new Integer(30200));
    value.setCourtLogViewValue(viewValue);

    XhbCase theCase =  null;
    String stringRet =
        listedDateMessageElement.getElement(value, theCase);
    assertEquals("Incorrect long adjourn date retrieved from log entry",
                 "31-Jul-2003", stringRet);
  }
}
