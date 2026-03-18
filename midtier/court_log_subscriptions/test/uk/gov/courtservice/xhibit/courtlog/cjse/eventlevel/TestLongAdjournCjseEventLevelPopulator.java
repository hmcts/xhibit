//
//package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;
//
//// jdk
//import javax.naming.NamingException;
//
//// j2ee
//import javax.ejb.ObjectNotFoundException;
//
//// xhibit
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulatorFactory;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.LongAdjournCjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//public class TestLongAdjournCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//  private static String CASE_TYPE      = "E30200_Case_to_be_listed_for_trial";
//  private static String DEFENDANT_TYPE = "E30200_Adjourned_for_Pre_Sentence_Report_to_date_on_enter_defendant's_name";
//
//  public TestLongAdjournCjseEventLevelPopulator(String s) throws NamingException
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
//   * Test the Long Adjourn populator when we have a defendant level event
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
//    //Check that we get a LongAdjournCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("LONG_ADJOURN");
//    assertTrue(
//            "Populator not an instance of LongAdjournCjseEventLevelPopulator",
//            populator instanceof LongAdjournCjseEventLevelPopulator);
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
//   * Test the Long Adjourn populator when we have a case level event
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
//    //Check that we get a LongAdjournCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("LONG_ADJOURN");
//    assertTrue(
//            "Populator not an instance of LongAdjournCjseEventLevelPopulator",
//            populator instanceof LongAdjournCjseEventLevelPopulator);
//
//    //Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population, check the values
//    // which should have been set for a case level event
//    // check attributes for case 22
//    checkSingleCase(eventParameters);
//    // check parameters for defendantOnCase 3,4,5
//    checkCaseDefendants(eventParameters);
//    // check parameters for defendantOnOffence 7, 8 from defendantOnCase 4
//    checkCrnsForCase(eventParameters);
//
//  }
//
//  private String getXml(String type)
//  {
//    return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//           "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='30200.xsd'>" +
//           "  <E30200_Long_Adjourn_Options>" +
//           "    <E30200_LAO_PSR_Deft_ID/>" +
//           "    <E30200_LAO_Date>31-Jul-2003</E30200_LAO_Date>" +
//           "    <E30200_LAO_Name/>" +
//           "    <E30200_LAO_Type>" + type + "</E30200_LAO_Type>" +
//           "  </E30200_Long_Adjourn_Options>" +
//           "  <free_text/>" +
//           "  <type>30200</type>" +
//           "</event>";
//  }
//}
//