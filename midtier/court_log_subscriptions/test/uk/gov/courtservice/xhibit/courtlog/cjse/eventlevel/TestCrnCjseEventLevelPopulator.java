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
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CrnCjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//public class TestCrnCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//  public TestCrnCjseEventLevelPopulator(String s) throws NamingException
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
//
//  public void testPopulate()
//  {
//    XhbCase theCase = null;
//    try
//    {
//      theCase =
//          XhbCaseBeanHelper.findByPrimaryKey(AbstractTestEventLevelPopulator.caseId);
//    }
//    catch (ObjectNotFoundException ex)
//    {
//      System.out.println("Exception: Couldn't find case " + AbstractTestEventLevelPopulator.caseId);
//      ex.printStackTrace();
//      fail();
//    }
//
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    viewValue.setDefendantOnOffenceId(defendantOnOffenceId);
//
//    CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue(viewValue);
//
//    eventInfo.setHearingId(schedHearingId);
//
//    // Check that we get a CrnCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("CRN");
//    assertTrue(
//            "Populator not an instance of CrnCjseEventLevelPopulator",
//            populator instanceof CrnCjseEventLevelPopulator);
//
//    // Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population
//    // test case level attributes
//    checkSingleCase(eventParameters);
//    // test crn level attriubtes
//    checkSingleCrn(eventParameters);
//    // test defendant level attributes
//    checkSingleDefAttributes(eventParameters);
//
//  }
//}
//