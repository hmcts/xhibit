
package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.endhearing;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.endhearing.HearingEndValidationHelper;


/**
 * <p>Title: TestHearingEndValidationHelperTest</p>
 * <p>Description: Test case to validate the start and end hearing.
 * - The start date cannot be after today
 * - The end date cannot be after today
 * - The start date cannot be after the end date
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Marie Holmberg
 * @version 1.0
 */
public class TestHearingEndValidationHelperTest extends TestCase {

  public TestHearingEndValidationHelperTest(String s) {
    super(s);
  }

  protected void setUp() {
  }

  protected void tearDown() {
  }

  /**
   * Test that tests valid start and end date. Start is before end.
   */
  public void testValidateDatesSuccess()
  {
    System.out.println("Run testValidateDatesSuccess()");

    //test the validation of start and end date.
    Calendar sdCal = Calendar.getInstance();
    sdCal.set(2003, 07, 07, 11, 30, 59);
    Calendar edCal = Calendar.getInstance();
    edCal.set(2003, 07, 07, 10, 15, 00);

    Date sd = sdCal.getTime();
    Date ed = edCal.getTime();
    System.out.println("sd : " + sd);
    System.out.println("ed : " + ed);

    try
    {
      HearingEndValidationHelper.validateDates(sd, ed);
      assertTrue(true);
    }
    catch(Exception e)
    {
      fail();
      System.err.println("Exception thrown:  "+e);
    }
    finally
    {
      System.out.println("End of testValidateDatesSuccess()\n\n");
    }
  }

  /**
   * This test is successful if an exception is thrown.
   * The start date is after today - should fail.
   */
  public void testValidateDatesStartAfterToday()
  {
    System.out.println("Run testValidateDatesStartAfterToday()");

    //test the validation of start and end date.
    Calendar sdCal = Calendar.getInstance();
    sdCal.setTime(new Date());
    sdCal.set(Calendar.DATE, (sdCal.get(Calendar.DATE)+1));
    Calendar edCal = Calendar.getInstance();
    edCal.set(2003, 07, 07, 10, 15, 00);

    Date sd = sdCal.getTime();
    Date ed = edCal.getTime();
    System.out.println("sd : " + sd);
    System.out.println("ed : " + ed);

    try
    {
      HearingEndValidationHelper.validateDates(sd, ed);
      fail();
    }
    catch(Exception e)
    {
      //if exception - the test is success since we expected this.
      System.out.println("Will set testValidateDatesStartAfterToday to success");
      assertTrue(true);
      System.err.println("Exception thrown:  "+e);
    }
    finally
    {
      System.out.println("End of testValidateDatesStartAfterToday()\n\n");
    }
  }

  /**
   * This test is successful if an exception is thrown.
   * The end date is after today - should fail.
   */
  public void testValidateDatesEndAfterToday()
  {
    System.out.println("Run testValidateDatesEndAfterToday()");

    //test the validation of start and end date.
    Calendar sdCal = Calendar.getInstance();
    sdCal.set(2003, 07, 07, 10, 15, 00);

    Calendar edCal = Calendar.getInstance();
    edCal.setTime(new Date());
    edCal.set(Calendar.DATE, (edCal.get(Calendar.DATE)+1));

    Date sd = sdCal.getTime();
    Date ed = edCal.getTime();
    System.out.println("sd : " + sd);
    System.out.println("ed : " + ed);

    try
    {
      HearingEndValidationHelper.validateDates(sd, ed);
      fail();
    }
    catch(Exception e)
    {
      //if exception - the test is success since we expected this.
      System.out.println("Will set testValidateDatesEndAfterToday to success");
      assertTrue(true);
      System.err.println("Exception thrown:  "+e);
    }
    finally
    {
      System.out.println("End of testValidateDatesEndAfterToday()\n\n");
    }
  }

  /**
   * This test is successful if an exception is thrown.
   * The start date is after end - should fail.
   */
  public void testValidateDatesStartAfterEnd()
  {
    System.out.println("Run testValidateDatesStartAfterEnd()");

    //test the validation of start and end date.
    Calendar sdCal = Calendar.getInstance();
    sdCal.set(2003, 8, 9, 10, 15, 00);

    Calendar edCal = Calendar.getInstance();
    edCal.set(2003, 07, 07, 10, 15, 00);

    Date sd = sdCal.getTime();
    Date ed = edCal.getTime();
    System.out.println("sd : " + sd);
    System.out.println("ed : " + ed);

    try
    {
      HearingEndValidationHelper.validateDates(sd, ed);
      fail();
    }
    catch(Exception e)
    {
      //if exception - the test is success since we expected this.
      System.out.println("Will set testValidateDatesStartAfterEnd to success");
      assertTrue(true);
      System.err.println("Exception thrown:  "+e);
    }
    finally
    {
      System.out.println("End of testValidateDatesStartAfterEnd()\n\n");
    }
  }
}