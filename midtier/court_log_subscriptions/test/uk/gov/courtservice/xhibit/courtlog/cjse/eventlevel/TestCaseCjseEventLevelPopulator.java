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
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CaseCjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulator;
//import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.CjseEventLevelPopulatorFactory;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
//
///**
// * <p>Title:TestCaseCjseEventLevelPopulator </p>
// * <p>Description:Test class for unit test of CaseCjseEventLevelPopulator </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Eds</p>
// * @author S Sangha
// * @version 1.0
// */
//
//public class TestCaseCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//
// public TestCaseCjseEventLevelPopulator(String s) throws NamingException
// {
//   super(s);
// }
//
// protected void setUp() throws Exception
// {
//   // test data is set up in the super class
//    super.setUp();
// }
//
// public void testPopulate()
// {
//   XhbCase theCase=null;
//   try
//   {
//     theCase = XhbCaseBeanHelper.findByPrimaryKey(AbstractTestEventLevelPopulator.caseId);
//   }
//   catch (ObjectNotFoundException ex)
//   {
//     System.out.println("Exception: Couldn't find case " + AbstractTestEventLevelPopulator.caseId);
//     ex.printStackTrace();
//     fail();
//    }
//
//    //set up value objects
//    CourtLogSubscriptionValue eventInfo = new CourtLogSubscriptionValue();
//    CourtLogViewValue viewValue = new CourtLogViewValue();
//    /**
//     * @todo CourtLogSubscriptionValue will be changed to primary keys
//     * as a Longs, when this happens the conversion below will no longer be
//     * necessary
//     */
//    viewValue.setCaseId(new Integer(AbstractTestEventLevelPopulator.caseId.intValue()));
//    eventInfo.setCourtLogViewValue(viewValue);
//    eventInfo.setHearingId(new Integer(AbstractTestEventLevelPopulator.schedHearingId.intValue()));
//
//    // Check that we get a CaseCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//       getInstance().getCjseEventLevelPopulator("CASE");
//    assertTrue(
//     "Populator is not an instance of CaseCjseEventLevelPopulator",
//     populator instanceof CaseCjseEventLevelPopulator);
//
//    //attempt population
//    EventParameters eventParameters = new EventParameters();
//    populator.populate(eventParameters, theCase, eventInfo);
//
//    //check parameters
//    checkSingleCase(eventParameters);
//    checkCaseDefendants(eventParameters);
//    checkCrnsForCase(eventParameters);
// }
//
//}