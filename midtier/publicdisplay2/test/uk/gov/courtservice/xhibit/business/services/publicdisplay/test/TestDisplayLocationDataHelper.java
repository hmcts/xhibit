//package uk.gov.courtservice.xhibit.business.services.publicdisplay.test;
//
//import java.net.URL;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.DisplayLocationDataHelper;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.CourtSitePDComplexValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayLocationComplexValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;
//import java.util.Locale;
//
///**
// * <p>Title: Test DisplayLocationDataHelper</p>
// * <p>Description: Test class for DisplayLocationData Helper methods</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Rakesh Lakhani
// * @version $Id: TestDisplayLocationDataHelper.java,v 1.4 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestDisplayLocationDataHelper extends TransactionTestCase
//{
//    /**
//     * The name of the script relative to the classloader(s) that will be run
//     * to populate the database with test data.
//     * <p>
//     * <code>    sql\configuration_create.sql</code>
//     */
//    public static final String TEST_CONFIGURATION_SCRIPT = "/sql/configuration_create.sql";
//
//    private static final Integer courtId = new Integer(1);
//
//    public TestDisplayLocationDataHelper(String s)
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
//        URL scriptURL = this.getClass().getClassLoader().getResource(TEST_CONFIGURATION_SCRIPT);
//        DatabaseUtil.executeScript(scriptURL,';', this.connection);
//    }
//
//    public void testGetDisplaysForCourt()
//    {
//        CourtSitePDComplexValue[] sites = DisplayLocationDataHelper.getDisplaysForCourt(courtId);
//        assertEquals("Wrong number of sites returned", 1, sites.length);
//
//        CourtSitePDComplexValue siteComplex = sites[0];
//        assertNotNull("Site basic value is null", siteComplex.getCourtSiteBasicValue());
//        assertEquals("Wrong number of locations", 2, siteComplex.getDisplayLocationComplexValue().length);
//
//        DisplayLocationComplexValue displayComplex = siteComplex.getDisplayLocationComplexValue()[0];
//        assertEquals("Court site incorrect", 1, displayComplex.getDisplayLocationBasicValue().getCourtSiteId().intValue());
//        assertEquals("Incorrect number of displays", 2, displayComplex.getDisplayBasicValue().length);
//
//    }
//
//    public void testGetRotationSetsDetailForCourt()
//    {
//        RotationSetComplexValue[] rotation = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, Locale.getDefault());
//        assertEquals("Wrong number of rotation sets", 4, rotation.length);
//
//        RotationSetComplexValue rotationSet = rotation[0];
//        assertEquals("Wrong number of display documents in rotation set", 3, rotationSet.getRotationSetDDComplexValues().length);
//
//        RotationSetDDComplexValue dd = rotationSet.getRotationSetDDComplexValues()[0];
//        assertNotNull("Rotation set DD BV is null!", dd.getRotationSetDDBasicValue());
//        assertEquals("Wrong rotation set id is incorrect", rotationSet.getRotationSetId(), dd.getRotationSetDDBasicValue().getRotationSetId());
//
//    }
//}