package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.EventDateTimeMessageElement;
import uk.gov.courtservice.xhibit.courtlog.vos.*;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.*;

import java.util.Date;
import java.text.SimpleDateFormat;



public class TestEventDateTimeMessageElement extends TestCase
{

  public TestEventDateTimeMessageElement(String s)
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
    EventDateTimeMessageElement eventDateTimeMessageElement =
                                              new EventDateTimeMessageElement();

    Date testDate = new Date();
    SimpleDateFormat formatter
                   = new SimpleDateFormat ("dd/MM/yy HH:mm");
    String dateString = formatter.format(testDate);



    CourtLogSubscriptionValue value =  new CourtLogSubscriptionValue();
    CourtLogViewValue viewValue = new CourtLogViewValue();
    viewValue.setEntryDate(testDate);
    value.setCourtLogViewValue(viewValue);

    // this attribute not used in the method body
    XhbCase theCase = null;

    String stringRet = eventDateTimeMessageElement.getElement(value, theCase);
    assertEquals("Incorrect date retrieved from the court log xml",
                 dateString, stringRet);
  }
}
