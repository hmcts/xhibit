//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage.ChargeIdentificationMessageElement;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//
//
//
//public class TestChargeIdentificationMessageElement extends TestCase
//{
//  /**
//   * Charge Types Reminder:
//   * Criminal Appeal              C
//   * Miscelleanous Appeal         M
//   * Breach                       B
//   * Summary Offence (section 41) O
//   * Committal for Sentence       S
//   * Indictment                   I
//   */
//  // values from the standard test data set
//  private static final Integer IND_DEF_ON_OFFENCE = new Integer(2);
//  private static final Integer SO_DEF_ON_OFFENCE = new Integer(1);
//  private static final Integer BREACH_DEF_ON_OFFENCE = new Integer(292);
//  private static final Integer APP_DEF_ON_OFFENCE = new Integer(263);
//  private static final Integer C4S_DEF_ON_OFFENCE = new Integer(197);
//
//  public TestChargeIdentificationMessageElement(String s)
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
//  public void testGetElementIndictment()
//  {
//    ChargeIdentificationMessageElement chargeIdMessageElement =
//                                       new ChargeIdentificationMessageElement();
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    viewValue.setDefendantOnOffenceId(IND_DEF_ON_OFFENCE);
//
//    CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//    // these attributes not used in the method body
//    XhbCase theCase = null;
//
//    String stringRet = chargeIdMessageElement.getElement(value, theCase);
//    assertEquals("1/1", stringRet);
//  }
//
//  public void testGetElementSummaryOffence()
//  {
//      CourtLogViewValue viewValue = new CourtLogViewValue();
//      viewValue.setDefendantOnOffenceId(SO_DEF_ON_OFFENCE);
//
//      CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//      ChargeIdentificationMessageElement chargeIdMessageElement =
//                                       new ChargeIdentificationMessageElement();
//
//    // these attributes not used in the method body
//    XhbCase theCase = null;
//
//    String stringRet = chargeIdMessageElement.getElement(value, theCase);
//    assertEquals("Summary Offence-1", stringRet);
//  }
//
//  public void testGetElementBreach()
//  {
//      CourtLogViewValue viewValue = new CourtLogViewValue();
//      viewValue.setDefendantOnOffenceId(BREACH_DEF_ON_OFFENCE);
//
//      CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//
//      ChargeIdentificationMessageElement chargeIdMessageElement =
//                                       new ChargeIdentificationMessageElement();
//
//    // these attributes not used in the method body
//    XhbCase theCase = null;
//
//    String stringRet = chargeIdMessageElement.getElement(value, theCase);
//    assertEquals("Breach-1", stringRet);
//  }
//
//  public void testGetElementAppeal()
//  {
//      CourtLogViewValue viewValue = new CourtLogViewValue();
//      viewValue.setDefendantOnOffenceId(APP_DEF_ON_OFFENCE);
//      CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//      ChargeIdentificationMessageElement chargeIdMessageElement =
//                                       new ChargeIdentificationMessageElement();
//
//    // these attributes not used in the method body
//    XhbCase theCase = null;
//
//    String stringRet = chargeIdMessageElement.getElement(value, theCase);
//    assertEquals("Appeal-1", stringRet); // S?
//  }
//
//  public void testGetElementCommittalForSentence()
//  {
//      CourtLogViewValue viewValue = new CourtLogViewValue();
//      viewValue.setDefendantOnOffenceId(C4S_DEF_ON_OFFENCE);
//      CourtLogSubscriptionValue value = new CourtLogSubscriptionValue(viewValue);
//
//      ChargeIdentificationMessageElement chargeIdMessageElement =
//                                       new ChargeIdentificationMessageElement();
//
//    // these attributes not used in the method body
//    XhbCase theCase = null;
//
//    String stringRet = chargeIdMessageElement.getElement(value, theCase);
//    assertEquals("Committal For Sentence-1", stringRet);
//  }
//}
//