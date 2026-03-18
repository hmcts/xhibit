//package uk.gov.courtservice.xhibit.client.models.crestformsbf;
//
//import junit.framework.TestCase;
//import junit.framework.Assert;
//import junit.framework.TestSuite;
//import junit.framework.Test;
//
//import junit.textui.TestRunner;
//
//import uk.gov.courtservice.framework.security.AccessInfo;
//
//import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFForm;
//import uk.gov.courtservice.xhibit.client.models.crestformsbf.CrestFormsBFModel;
//
///**
// * Test the CrestFormsBFModel
// */
//public class CrestFormsBFModelTest extends TestCase
//{
//    /**
//     * Create a suite of tests from this class
//     * @return a new suite of tests built by refelection from this class
//     */
//    public static Test suite()
//    {
//        return new TestSuite(CrestFormsBFModelTest.class);
//    }
//
//    /**
//     * Use the text (command line) test runner to run the test suite
//     * @param args command line arguments
//     */
//    public static void main(String args[])
//    {
//        TestRunner.run(suite());
//    }
//
//    CrestFormsBFModel model;
//
//    public void setUp()
//    {
//        model = new CrestFormsBFModel(new Integer(1), new Integer(0)); // Jon Powell
//    }
//
//    public void tearDown()
//    {
//        model = null;
//    }
//
//    public void testForm()
//    {
//	fail("check tests - CrestFormsBFModel constructor changed");
//        assertEquals(4, model.getFormCount());
//
//        for(int i = 0; i < model.getFormCount(); i++) {
//            System.out.println(model.getForm(i));
//        }
//    }
//
//    public void testSelectedIndicies()
//    {
//        int[] selectedIndicies = model.getSelectedIndicies();
//        assertEquals(0, selectedIndicies.length);
//
//
//    }
//
//}
//
//
//