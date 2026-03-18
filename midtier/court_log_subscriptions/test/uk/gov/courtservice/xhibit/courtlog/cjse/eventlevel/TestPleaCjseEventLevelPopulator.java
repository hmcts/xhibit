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
///**
// * <p>Title: TestPleaCjseEventLevelPopulator</p>
// * <p>Description: Tests the PleaCjseEventLevelPopulator</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Sarah Tong
// * @version $Id: TestPleaCjseEventLevelPopulator.java,v 1.3 2006/07/11 14:16:53 xzfdtb Exp $
// */
//public class TestPleaCjseEventLevelPopulator extends AbstractTestEventLevelPopulator
//{
//    private static String DEFENDANT_TYPE = "B_Y";
//    private static String CRN_TYPE       = "I_CPNG";
//
//    public TestPleaCjseEventLevelPopulator(String s) throws NamingException
//    {
//        super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//        // test data is set up in the super class
//        super.setUp();
//    }
//
//    /**
//     * Test the Plea populator with a defendant level event
//     */
//    public void testPopulateDefendantLevel()
//    {
//        XhbCase theCase = null;
//        try
//        {
//            theCase = XhbCaseBeanHelper.findByPrimaryKey(
//                                        AbstractTestEventLevelPopulator.caseId);
//        }
//        catch (ObjectNotFoundException ex)
//        {
//            System.out.println("Exception: Couldn't find case " +
//                               AbstractTestEventLevelPopulator.caseId);
//            ex.printStackTrace();
//            fail();
//        }
//
//        CourtLogViewValue viewValue = new CourtLogViewValue();
//        viewValue.setLogEntry(getXml(DEFENDANT_TYPE));
//        viewValue.setDefendantOnCaseId(defendantOnCaseId);
//
//        CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue(viewValue);
//        eventInfo.setHearingId(schedHearingId);
//
//        //Check that we get a PleaCjseEventLevelPopulator.
//        CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//                getInstance().getCjseEventLevelPopulator("PLEA");
//        assertTrue(
//                "Populator not an instance of PleaCjseEventLevelPopulator",
//                populator instanceof PleaCjseEventLevelPopulator);
//
//        //Attempt Population.
//        EventParameters eventParameters = new EventParameters();
//        populator.populate(eventParameters, theCase, eventInfo);
//
//        // use super class utility methods to check population, check the values
//        // which should have been set for a defendant level event
//        // check attributes for case 22
//        checkSingleCase(eventParameters);
//        // check parameters for defendantOnCase 4
//        checkSingleDefAttributes(eventParameters);
//        // check parameters for defendantOnOffence 7, 8 from defendantOnCase 4
//        checkCrnsForDef(eventParameters);
//    }
//
//    public void testPopulateCrnLevel()
//    {
//        XhbCase theCase = null;
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
//    viewValue.setLogEntry(getXml(CRN_TYPE));
//
//    CourtLogSubscriptionValue eventInfo =  new  CourtLogSubscriptionValue(viewValue);
//    eventInfo.setHearingId(new Integer(schedHearingId.intValue()));
//
//    //Check that we get a PleaCjseEventLevelPopulator.
//    CjseEventLevelPopulator populator = CjseEventLevelPopulatorFactory.
//            getInstance().getCjseEventLevelPopulator("PLEA");
//    assertTrue(
//            "Populator not an instance of PleaCjseEventLevelPopulator",
//            populator instanceof PleaCjseEventLevelPopulator);
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
//    }
//
//    private String getXml(String type)
//    {
//        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
//               "<event xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='60100.xsd'>" +
//               "  <free_text>Guilty by DEFENDANT T20028850-1 for count 3 on Indictment null.</free_text>" +
//               "  <E60100_Ref_Plea_Code>" + type + "</E60100_Ref_Plea_Code>" +
//               "  <type>60100</type>" +
//               "</event>";
//    }
//}
//