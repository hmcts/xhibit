//package uk.gov.courtservice.xhibit.business.services.publicdisplay.test;
//
//import java.net.URL;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSite;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBeanHelper;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSet;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBeanHelper;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBeanHelper;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;
//import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.DisplayConfigurationHelper;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.RotationSetNotFoundCheckedException;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
//
///**
// * <p>Title: Test DisplayLocationDataHelper</p>
// * <p>Description: Test class for DisplayLocationData Helper methods</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Rakesh Lakhani
// * @version $Id: TestDisplayConfigurationHelper.java,v 1.4 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestDisplayConfigurationHelper extends TransactionTestCase
//{
//    /**
//     * The name of the script relative to the classloader(s) that will be run
//     * to populate the database with test data.
//     * <p>
//     * <code>    sql\configuration_create.sql</code>
//     */
//    public static final String TEST_CONFIGURATION_SCRIPT = "/sql/configuration_create.sql";
//
//    private static final Long courtId = new Long(1);
//
//    public TestDisplayConfigurationHelper(String s)
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
//    public void testUpdateConfiguration() throws Exception
//    {
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        DisplayConfiguration dc = bd.getDisplayConfiguration(new Integer(-2));
//
//        // Change config
//        XhbCourtSite courtSite = XhbCourtSiteBeanHelper.findByPrimaryKey(new Integer(1));
//        XhbCourtRoomBasicValue[] courtRooms = courtSite.getXhbCourtRoomsData();
//
//        XhbRotationSetBasicValue rsBv = XhbRotationSetBeanHelper.findByPrimaryKeyValue(new Integer(-2));
//
//        dc.setCourtRoomBasicValues(courtRooms);
//        dc.setRotationSetBasicValue(rsBv);
//        PublicDisplayNotifier notifier = new PublicDisplayNotifier();
//        // Update config
//        try
//        {
//            DisplayConfigurationHelper.updateDisplayConfiguration(dc, notifier);
//        }
//        catch (RotationSetNotFoundCheckedException ex)
//        {
//            fail("Invalid rotation set");
//        }
//        notifier.close();
//
//        // Get it back out of the DB
//        DisplayConfiguration newDc = bd.getDisplayConfiguration(new Integer(-2));
//
//        // Check changes have been applied
//        assertEquals("Wrong number of court rooms", courtRooms.length, newDc.getCourtRoomBasicValues().length);
//        assertEquals("Wrong rotation set", -2, newDc.getRotationSetId().intValue());
//    }
//
//    public void testUpdateConfigurationInvalidRotationSet() throws Exception
//    {
//        // Create rotation set
//        XhbRotationSetBasicValue rsBv = XhbRotationSetBeanHelper.findByPrimaryKeyValue(new Integer(-3));
//
//        PDConfigurationControllerBeanBusinessDelegate bd =
//                PDConfigurationControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//        DisplayConfiguration dc = bd.getDisplayConfiguration(new Integer(-2));
//
//        // Change rotation set
//        XhbRotationSet rsToDelete = XhbRotationSetBeanHelper.findByPrimaryKey(rsBv.getPrimaryKey());
//        XhbRotationSetBasicValue rsBvToDelete = rsToDelete.getData();
//
//        dc.setRotationSetBasicValue(rsBv);
//
//        // Delete rotation set
//        XhbRotationSetDdBasicValue[] all = rsToDelete.getXhbRotationSetDdsData();
//        for (int i = 0; i < all.length; i++)
//        {
//            XhbRotationSetDdBeanHelper.remove(all[i]);
//        }
//        XhbRotationSetBeanHelper.remove(rsBvToDelete);
//
//        PublicDisplayNotifier notifier = new PublicDisplayNotifier();
//        // Update config
//        try
//        {
//            DisplayConfigurationHelper.updateDisplayConfiguration(dc, notifier);
//            fail("Did not catch deleted rotation set");
//        }
//        catch (RotationSetNotFoundCheckedException ex)
//        {
//        }
//        notifier.close();
//    }
//}