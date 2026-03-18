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
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.JoinderCjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.MultiCaseCourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//public class TestJoinderCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//  public TestJoinderCjseEventLevelPopulator(String s) throws NamingException
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
//  public void testPopulate()
//  {
//    XhbCase theCase = null;
//    try
//    {
//      theCase = XhbCaseBeanHelper.findByPrimaryKey(caseId);
//    }
//    catch (ObjectNotFoundException ex)
//    {
//      System.out.println("Exception: Couldn't find case " + caseId);
//      ex.printStackTrace();
//      fail();
//    }
//    // Check that we get a DefendantCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("JOINDER");
//    assertTrue(
//            "Populator not an instance of JoinderCjseEventLevelPopulator",
//            populator instanceof JoinderCjseEventLevelPopulator);
//
//    // set up the value object
//    CourtLogSubscriptionValue eventInfo =  new CourtLogSubscriptionValue();
//    MultiCaseCourtLogViewValue viewValue = new MultiCaseCourtLogViewValue();
//    /**
//     * @todo CourtLogSubscriptionValue will be changed to primary keys
//     * as a Longs, when this happens the conversion below will no longer be
//     * necessary
//     */
//    viewValue.setCaseId(new Integer(caseId.intValue()));
//    viewValue.setCaseIds(intCaseIds);
//    eventInfo.setCourtLogViewValue(viewValue);
//    eventInfo.setHearingId(new Integer(schedHearingId.intValue()));
//
//    // Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population
//    // check case level attributes
//    checkJoinderCases(eventParameters);
//
//    // basic check to make sure we have all 3 defOnCase present,
//    // TestDefendantCjseEventLevelPopulator makes a full check of defendant
//    // level population
//    checkCaseDefendants(eventParameters);
//    // basic check to make sure we have all 3 defOnCase present,
//    // TestCrnCjseEventLevelPopulator makes a full check of crn
//    // level population
//    checkCrnsForCase(eventParameters);
//    // check crn level for case 22 (main case):
//    // defOnOffence 2, 3, 4, 5, 6, 7, 8, 9, 10
//    // basic check to make sure we have all 3 defOnCase present,
//    // TestCrnCjseEventLevelPopulator makes a full check of crn
//    // level population
//    checkCrnsForCase(eventParameters);
//  }
//}
//