//package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;
//
//import javax.ejb.ObjectNotFoundException;
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//public class TestTrialWitnessSwornCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//  private static String CASE_TYPE      = "E20904_Defence_witness_character_sworn";
//  private static String DEFENDANT_TYPE = "E20904_Defendant_sworn";
//
//  public TestTrialWitnessSwornCjseEventLevelPopulator(String s) throws NamingException
//  {
//    super(s);
//  }
//
//  protected void setUp() throws Exception
//  {
//    // test data is set up in the super class
//    super.setUp();
//  }
//
//  /**
//   * Test the TWS populator when we have a defendant level event
//   */
//  public void testPopulateDefendantLevel()
//  {
//    XhbCase theCase = null;
//    try
//    {
//      theCase =
//          XhbCaseBeanHelper.findByPrimaryKey(AbstractTestEventLevelPopulator.caseId);
//    }
//    catch (ObjectNotFoundException ex)
//    {
//      System.out.println("Exception: Couldn't find case " +
//                         AbstractTestEventLevelPopulator.caseId);
//      ex.printStackTrace();
//      fail();
//    }
//
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    viewValue.setLogEntry(getXml(DEFENDANT_TYPE));
//    viewValue.setDefendantOnCaseId(defendantOnCaseId);
//
//    CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue(viewValue);
//
//    eventInfo.setHearingId(schedHearingId);
//
//    //Check that we get a TrialWitnessSwornCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("TRIAL_WITNESS_SWORN");
//    assertTrue(
//            "Populator not an instance of TrialWitnessSwornCjseEventLevelPopulator",
//            populator instanceof TrialWitnessSwornCjseEventLevelPopulator);
//
//    //Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population, check the values
//    // which should have been set for a defendant level event
//    // check attributes for case 22
//    checkSingleCase(eventParameters);
//    // check parameters for defendantOnCase 4
//    checkSingleDefAttributes(eventParameters);
//    // check parameters for defendantOnOffence 7, 8 from defendantOnCase 4
//    checkCrnsForDef(eventParameters);
//
//  }
//
//  /**
//   * Test the TWS populator when we have a case level event
//   */
//  public void testPopulateCaseLevel()
//  {
//    XhbCase theCase = null;
//    try
//    {
//      theCase =
//          XhbCaseBeanHelper.findByPrimaryKey(AbstractTestEventLevelPopulator.caseId);
//    }
//    catch (ObjectNotFoundException ex)
//    {
//      System.out.println("Exception: Couldn't find case " +
//                         AbstractTestEventLevelPopulator.caseId);
//      ex.printStackTrace();
//      fail();
//    }
//
//    CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue();
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    viewValue.setLogEntry(getXml(CASE_TYPE));
//
//    eventInfo.setCourtLogViewValue(viewValue);
//    /**
//     * @todo CourtLogSubscriptionValue will be changed to primary keys
//     * as a Longs, when this happens the conversion below will no longer be
//     * necessary
//     */
//    eventInfo.setHearingId(new Integer(schedHearingId.intValue()));
//
//    //Check that we get a TrialWitnessSwornCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("TRIAL_WITNESS_SWORN");
//    assertTrue(
//            "Populator not an instance of TrialWitnessSwornCjseEventLevelPopulator",
//            populator instanceof TrialWitnessSwornCjseEventLevelPopulator);
//
//    //Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population, check the values
//    // which should have been set for a case level event
//    // check attributes for case 22
//    checkSingleCase(eventParameters);
//    // check parameters for defendantOnCase 3, 4, 5
//    checkCaseDefendants(eventParameters);
//    // check parameters for defendantOnOffence 7, 8 from defendantOnCase 4
//    checkCrnsForCase(eventParameters);
//
//  }
//
//  private String getXml(String type)
//  {
//    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//           "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='20904.xsd'>" +
//           "  <E20904_Witness_Sworn_Options>" +
//           "    <E20904_WSO_Number>1</E20904_WSO_Number>" +
//           "    <E20904_WSO_Name>Rakesh the witness</E20904_WSO_Name>" +
//           "    <E20904_WSO_Type>" + type + "</E20904_WSO_Type>" +
//           "    <E20904_WSO_ID/>" +
//           "  </E20904_Witness_Sworn_Options>" +
//           "  <free_text>Test for Sarah</free_text>" +
//           "  <type>20904</type>" +
//           "</event>";
//  }
//}
//