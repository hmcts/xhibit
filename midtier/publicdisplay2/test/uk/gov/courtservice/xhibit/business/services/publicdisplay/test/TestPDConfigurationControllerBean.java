//package uk.gov.courtservice.xhibit.business.services.publicdisplay.test;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author unascribed
// * @version $Id: TestPDConfigurationControllerBean.java,v 1.3 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestPDConfigurationControllerBean extends TransactionTestCase
//{
//    public static final String TEST_CONFIGURATION_SCRIPT = "/sql/configuration_create.sql";
//
//    private static final Long courtId = new Long(1);
//
//    public TestPDConfigurationControllerBean(String s)
//            throws Exception
//    {
//        super(s, true);
//    }
//
//    protected void setUp()
//            throws Exception
//    {
//        //Insert the configuration data into the database.
//        super.setUp();
//        java.net.URL scriptURL = this.getClass().getClassLoader().getResource(TEST_CONFIGURATION_SCRIPT);
//        DatabaseUtil.executeScript(scriptURL,';', this.connection);
//    }
//
//    public void testGetDisplayConfiguration()
//    {
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        DisplayConfiguration dc = bd.getDisplayConfiguration(new Integer(-2));
//        assertEquals("Wrong number of court rooms", 3, dc.getCourtRoomBasicValues().length);
//        assertEquals("Wrong display Id", -2, dc.getDisplayId().intValue());
//        assertEquals("Wrong Rotation Set Id", -1, dc.getRotationSetId().intValue());
//    }
//
//    public void testGetRotationSet() throws Exception
//    {
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
////        RotationSetDDComplexValue[] rsDds = bd.getDisplayDocumentsForRotationSet(new Long(-2));
//        RotationSetDDComplexValue[] rsDds = bd.getRotationSet(new Integer(-2)).getRotationSetDDComplexValues();
//        assertEquals("Wrong number of documents", 3, rsDds.length);
//    }
//
//    public void testGetDisplayDocuments()
//    {
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        assertEquals("Wrong number of display documents", 6, bd.getDisplayDocuments().length);
//    }
//
//    public void testGetRotationSetByCourt()
//    {
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        assertEquals("Wrong number of rotation sets for 1", 4, bd.getRotationSetsForCourt(new Integer(1)).length);
//        assertEquals("Wrong number of rotation sets for 3", 1, bd.getRotationSetsForCourt(new Integer(3)).length);
//    }
//
//}
//