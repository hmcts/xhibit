//package uk.gov.courtservice.xhibit.business.services.publicdisplay.test;
//
//import java.util.ArrayList;
//
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_display_document.XhbDisplayDocumentBeanHelper;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_sets.XhbRotationSetBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDdBasicValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.RotationSetMaintainHelper;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.exceptions.PublicDisplayCheckedException;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.DisplayLocationDataHelper;
//import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;
//import uk.gov.courtservice.xhibit.common.publicdisplay.jms.PublicDisplayNotifier;
//import java.util.Locale;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author unascribed
// * @version $Id: TestRotationSetMaintainHelper.java,v 1.4 2006/07/11 14:17:01 xzfdtb Exp $
// */
//
//public class TestRotationSetMaintainHelper extends TransactionTestCase
//{
//    public static final String TEST_CONFIGURATION_SCRIPT = "/sql/configuration_create.sql";
//    private static final Locale locale = Locale.getDefault();
//    private static final Integer courtId = new Integer(1);
//
//    public TestRotationSetMaintainHelper(String s)
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
//    public void testNewRotationSet() throws Exception
//    {
//        // Create a rotation set basic and complex value
//        XhbRotationSetBasicValue newRot = new XhbRotationSetBasicValue();
//        newRot.setCourtId(courtId);
//        newRot.setDefaultYn("N");
//        newRot.setDescription("Rakesh");
//
//        RotationSetComplexValue rotComplex = new RotationSetComplexValue();
//        rotComplex.setRotationSetBasicValue(newRot);
//
//        // Get display documents
//        XhbDisplayDocumentBasicValue dd1 = XhbDisplayDocumentBeanHelper.findByPrimaryKeyValue(new Integer(1));
//        XhbDisplayDocumentBasicValue dd2 = XhbDisplayDocumentBeanHelper.findByPrimaryKeyValue(new Integer(2));
//
//        // Prepare RotationSetDDBasicValues
//        XhbRotationSetDdBasicValue rsddBv1 = new XhbRotationSetDdBasicValue();
//        rsddBv1.setOrdering(new Integer(0));
//        rsddBv1.setPageDelay(new Integer(30));
//        XhbRotationSetDdBasicValue rsddBv2 = new XhbRotationSetDdBasicValue();
//        rsddBv2.setOrdering(new Integer(1));
//        rsddBv2.setPageDelay(new Integer(20));
//
//        // Create an array of rotation set DD complex values
//        RotationSetDDComplexValue[] ddComplex = new RotationSetDDComplexValue[] {
//            new RotationSetDDComplexValue(rsddBv1, dd1),
//            new RotationSetDDComplexValue(rsddBv2, dd2) };
//
//        rotComplex.setRotationSetDDComplexValues(ddComplex);
//
//        RotationSetMaintainHelper.createRotationSets(rotComplex);
//
//        // Now check results by getting rotation set back out.
//        // NOTE TestDisplayLocationDataHelper MUST HAVE SUCCESSFULLY PASSED ITS TEST
//        // TO HAVE CONFIDENCE IN THIS WORKING
//        RotationSetComplexValue[] newComplex = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale);
//        assertEquals("Rotation Set not added", 5, newComplex.length);
//        RotationSetComplexValue newRs = newComplex[3];
//        assertEquals("Incorrect number of pages in new rotation set", 2, newRs.getRotationSetDDComplexValues().length);
//
//    }
//
//    public void testUpdateRotationSet() throws Exception
//    {
//        boolean first = true;
//        int firstId = -99;
//        int[] testDataIds = new int[] {-1,-2,-3};
//
//        // Get rotation set
//        RotationSetComplexValue[] allRs = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale);
//        RotationSetComplexValue rs = allRs[0];
//
//        // Put dd's into arraylist
//        RotationSetDDComplexValue[] dds = rs.getRotationSetDDComplexValues();
//        ArrayList al = new ArrayList();
//        for (int i = 0; i < dds.length; i++)
//        {
//            if (first) {
//                dds[i].getRotationSetDDBasicValue().setPageDelay(new Integer(5));
//                firstId = dds[i].getRotationSetDdId().intValue();
//                first = false;
//            }
//            al.add(dds[i]);
//        }
//
//        // create another dd complex
//        // Get display documents
//        XhbDisplayDocumentBasicValue dd1 = XhbDisplayDocumentBeanHelper.findByPrimaryKeyValue(new Integer(4));
//
//        // Prepare RotationSetDDBasicValues
//        XhbRotationSetDdBasicValue rsddBv1 = new XhbRotationSetDdBasicValue();
//        rsddBv1.setOrdering(new Integer(2));
//        rsddBv1.setPageDelay(new Integer(30));
//
//        // add the rotation set DD complex value
//        al.add( new RotationSetDDComplexValue(rsddBv1, dd1) );
//
//        rs.setRotationSetDDComplexValues(
//                (RotationSetDDComplexValue[])al.toArray(new RotationSetDDComplexValue[al.size()])
//                );
//
//        rs.getRotationSetBasicValue().setDescription("Rakesh");
////        rs.getRotationSetDDComplexValues()[1].getRotationSetDDBasicValue().setPageDelay(new Long(5));
//
//        // update
//        PublicDisplayNotifier publicDisplayNotifier = new PublicDisplayNotifier();
//        RotationSetMaintainHelper.setDisplayDocumentsForRotationSet(rs,
//                publicDisplayNotifier);
//        publicDisplayNotifier.close();
//
//        // Get RS from DB again.
//        RotationSetComplexValue[] allRs2 = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale);
//        RotationSetComplexValue checkRs = allRs2[0];
//
//        // Check if it has four Dd's
//        assertEquals("Wrong number of documents", 4, checkRs.getRotationSetDDComplexValues().length);
//
//        // Check rotation set name changed
//        assertEquals("Rotation Set Name not changed", "Rakesh", checkRs.getRotationSetBasicValue().getDescription());
//
//        // get New DD added
//        RotationSetDDComplexValue[] checkDDs = checkRs.getRotationSetDDComplexValues();
//        RotationSetDDComplexValue checkNewDD = null;
//        for (int i = 0; i < checkDDs.length; i++)
//        {
//            boolean oldFound = false;
//
//            for (int j = 0; j < testDataIds.length; j++)
//            {
//                if (testDataIds[j] == checkDDs[i].getRotationSetDdId().intValue())
//                {
//                    oldFound = true;
//                    break;
//                }
//            }
//            if (!oldFound)
//            {
//                checkNewDD = checkDDs[i];
//                break;
//            }
//        }
//
//        assertNotNull("New DD not found", checkNewDD);
//        // Check new doc time is correct
//        assertEquals("NEW Page delay incorrect", 30,
//                     checkNewDD.getRotationSetDDBasicValue().getPageDelay().intValue());
//
//
//        // get first DD
//        RotationSetDDComplexValue checkFirstDD = null;
//        for (int i = 0; i < checkDDs.length; i++)
//        {
//            if ( checkDDs[i].getRotationSetDdId().intValue() == firstId )
//            {
//                checkFirstDD = checkDDs[i];
//                break;
//            }
//        }
//
//        assertNotNull("First DD not found", checkNewDD);
//        // Check doc 2 time has changed
//        assertEquals("Page delay incorrect", 5,
//                     checkFirstDD.getRotationSetDDBasicValue().getPageDelay().intValue());
//
//    }
//
//    public void testNewRotationSetWithNulls()
//            throws Exception
//    {
//        // Create a rotation set basic and complex value
//        XhbRotationSetBasicValue newRot = new XhbRotationSetBasicValue();
//        newRot.setCourtId(courtId);
//        newRot.setDefaultYn("N");
////        newRot.setDescriptionCode("Rakesh");
//
//        RotationSetComplexValue rotComplex = new RotationSetComplexValue();
//        rotComplex.setRotationSetBasicValue(newRot);
//
//        // Get display documents
//        XhbDisplayDocumentBasicValue dd1 = XhbDisplayDocumentBeanHelper.findByPrimaryKeyValue(new Integer(1));
//        XhbDisplayDocumentBasicValue dd2 = XhbDisplayDocumentBeanHelper.findByPrimaryKeyValue(new Integer(2));
//
//        // Prepare RotationSetDDBasicValues
//        XhbRotationSetDdBasicValue rsddBv1 = new XhbRotationSetDdBasicValue();
//        rsddBv1.setOrdering(new Integer(0));
//        rsddBv1.setPageDelay(new Integer(30));
//        XhbRotationSetDdBasicValue rsddBv2 = new XhbRotationSetDdBasicValue();
//        rsddBv2.setOrdering(new Integer(1));
//        rsddBv2.setPageDelay(new Integer(20));
//
//        // Create an array of rotation set DD complex values
//        RotationSetDDComplexValue[] ddComplex = new RotationSetDDComplexValue[] {
//            new RotationSetDDComplexValue(rsddBv1, dd1),
//            new RotationSetDDComplexValue(rsddBv2, dd2) };
//
//        rotComplex.setRotationSetDDComplexValues(ddComplex);
//
//        try
//        {
//            RotationSetMaintainHelper.createRotationSets(rotComplex);
//            fail("Did not pick up null rotation set description");
//        }
//        catch (Exception ex)
//        {
//        }
//    }
//
//    /** DELETE A ROTATION SET  (-4) */
//    public void testDeleteRotationSet()
//        throws Exception
//    {
//        int start = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale).length;
//        RotationSetComplexValue rs = getRotationSet(-4);
//        RotationSetMaintainHelper.deleteRotationSet(rs);
//        assertEquals("RS not deleted", start-1, DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale).length);
//    }
//
//    /** CAN NOT DELETE SYSTEM ROTATION SET (-3) */
//    public void testDeleteSystemRotationSet()
//        throws Exception
//    {
//        RotationSetComplexValue rs = getRotationSet(-3);
//        try
//        {
//            RotationSetMaintainHelper.deleteRotationSet(rs);
//            fail("Incorrectly successfully deleted a system rotation set");
//        }
//        catch (PublicDisplayCheckedException ex)
//        {
//            assertEquals("Wrong error message", "pubdisp.rotationset.deletesystem", ex.getUserMessageAsMessage().getKey());
//            System.out.println("Error Message=" + ex.getUserMessage());
//        }
//    }
//
//    /** CAN NOT DELETE A ROTATION SET THAT IS ASSIGNED (-1) */
//    public void testDeleteRotationSetAssignedToDisplay()
//        throws Exception
//    {
//        RotationSetComplexValue rs = getRotationSet(-1);
//        try
//        {
//            RotationSetMaintainHelper.deleteRotationSet(rs);
//            fail("Incorrectly successfully deleted a rotation set currently assigned to a display");
//        }
//        catch (PublicDisplayCheckedException ex)
//        {
//            assertEquals("Wrong error message", "pubdisp.rotationset.assigned", ex.getUserMessageAsMessage().getKey());
//            System.out.println("Error Message=" + ex.getUserMessage());
//        }
//
//    }
//
//    private RotationSetComplexValue getRotationSet(int rsId) throws Exception
//    {
//        RotationSetComplexValue[] rs = DisplayLocationDataHelper.getRotationSetsDetailForCourt(courtId, locale);
//        for (int i = 0; i < rs.length; i++)
//        {
//            if (rs[i].getRotationSetId().intValue() == rsId) return rs[i];
//        }
//        throw new Exception("Rotation set not found");
//    }
//
//}
//