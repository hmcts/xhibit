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
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.DefendantCjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
//public class TestDefendantCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//
//  public TestDefendantCjseEventLevelPopulator(String s) throws NamingException
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
//    viewValue.setDefendantOnCaseId(AbstractTestEventLevelPopulator.defendantOnCaseId);
//
//    CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue(viewValue);
//
//    eventInfo.setHearingId(AbstractTestEventLevelPopulator.schedHearingId);
//
//    //Check that we get a DefendantCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("DEFENDANT");
//    assertTrue(
//            "Populator not an instance of DefendantCjseEventLevelPopulator",
//            populator instanceof DefendantCjseEventLevelPopulator);
//
//    //Attempt Population.
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    // use super class utility methods to check population
//    // check attributes for case 22
//    checkSingleCase(eventParameters);
//    // check parameters for defendantOnCase 4
//    checkSingleDefAttributes(eventParameters);
//    // check parameters for defendantOnOffence 7, 8 from defendantOnCase 4
//    checkCrnsForDef(eventParameters);
//
//  }
//}
//