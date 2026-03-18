//package uk.gov.courtservice.xhibit.xmlbinding.orders;
//
//import java.net.URL;
//import javax.naming.NamingException;
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.DatabaseUtil;
//import uk.gov.courtservice.framework.testutils.XMLMarshaller;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.caze.Case;
//import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCases;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.AssociatedCase;
//
///**
// * <p>Title: TestAssociatedCasesHelper </p>
// * <p>Description: JUnitEE Test case for the AssociatedCasesHelper</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Andy Daley
// * @version 1.0
// */
// /* Ref       Date          Author       Description
//  *
//  *            10/07/2003    AW Daley     Initial Version
//  */
//public class TestAssociatedCasesHelper extends TransactionTestCase
//{
//    private static Logger log = CSServices.getLogger
//                                          ( TestAssociatedCasesHelper.class );
//    private static final Integer TEST_CASE_ID = new Integer(2000);
//
//    private static final String SQL_SCRIPTS_FOLDER = "/sqlscripts/";
//    private static final String EXPECTED_RESULTS_FOLDER = "/xres/";
//    private AssociatedCasesHelper helper;
//    private AssociatedCases associatedCasesRes;
//    private AssociatedCases asscociatedCasesExp;
//    private Case mainCase;
//
//    public TestAssociatedCasesHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        //Reads test data drom an sql script and loads into the  database as a
//        //transaction. Tear down not required because we are rolling back the
//        //transaction
//        URL scriptURL = this.getClass().
//                        getResource(SQL_SCRIPTS_FOLDER+
//                        "TestAssociatedCasesHelperSetup.sql");
//        if (scriptURL == null)
//            this.fail("Unable to open resource at URL: " + SQL_SCRIPTS_FOLDER +
//                      "TestAssociatedCasesHelperSetup.sql");
//
//        DatabaseUtil.executeScript(scriptURL, ';', this.connection);
//
//        //Reads the expected object from an XML file.
//        URL expResURL = this.getClass().
//                        getResource(EXPECTED_RESULTS_FOLDER+
//                        "ExpTestAssociatedCasesHelper.xml");
//        if (expResURL == null)
//            this.fail("Unable to open resource at URL: "
//                      + EXPECTED_RESULTS_FOLDER +
//                      "ExpTestAssociatedCasesHelper.xml");
//
//        asscociatedCasesExp =(AssociatedCases)XMLMarshaller.unMarshall
//        (AssociatedCases.class, expResURL);
//
//        //Creates an instance of the Case EJB Entity
//        mainCase = (Case)CSServices.getEJBServices().
//                   findLocalEntityByPrimaryKey(CaseHome.class, TEST_CASE_ID);
//    }
//
//    /**
//     * Test the populateAssociatedCases on the AssociatedCasesHelper
//     */
//    public void testPopulateAssociatedCases()
//    {
//        try
//        {
////         //Calls method to test
//            associatedCasesRes = helper.populateAssociatedCases(null, mainCase);
//            assertNotNull(associatedCasesRes);
//
////        Shows how to create the expected results file.
////        XMLMarshaller.marshall(associatedCases,
////         "D:\\xhibit\\XHIBIT\\midtier\\orders\\xmlbinding\\test\\xres\\" +
////                                "ExpTestAssociatedCasesHelper.xml");
//
//            //Compares Expected and Actual Test Results
//            assertEquals(associatedCasesRes.getAssociatedCaseCount(),
//                         asscociatedCasesExp.getAssociatedCaseCount());
//
//            //If comapring a value object then the 'equals' method of the value
//            //object could be used. Since the object being compared here is
//            //not a value object and the AssociatedCase and AssociatedCases
//            //object cannot be modified to implement 'equals' the object need to
//            //be inspected to equate the actual and expected objects.
//            for (int i=0; i<associatedCasesRes.getAssociatedCaseCount(); i++)
//            {
//                AssociatedCase[] assocCasesRes =
//                                       associatedCasesRes.getAssociatedCase();
//                AssociatedCase[] assocCasesExp =
//                                      asscociatedCasesExp.getAssociatedCase();
//
//                this.assertEquals(assocCasesRes[i].getContent(),
//                                  assocCasesExp[i].getContent());
//            }
//        }
//
//        catch (Throwable t)
//        {
//            fail("Exception thrown by 'testPopulateAssociatedCases' " +
//                 t.getMessage());
//        }
//
//    }
//
//}