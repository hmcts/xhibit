//package uk.gov.courtservice.xhibit.test.integration.services.stub;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.integration.services.stub.PreHearingUpdateStub;
//import javax.naming.NamingException;
//import java.sql.Statement;
//import java.sql.ResultSet;
//
///**
// *
// * <p>Title: TestPreHearingUpdateStub</p>
// * <p>Description: Tests stubbed out mercator methods which fall into the
// * pre hearing area.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Sarah Tong
// * @version $Id: TestPreHearingUpdateStub.java,v 1.3 2006/07/11 14:16:52 xzfdtb Exp $
// */
//public class TestPreHearingUpdateStub extends TransactionTestCase
//{
//    // from standard test data
//    private final Integer CASE_ID = new Integer(1);
//
//    public TestPreHearingUpdateStub(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//    }
//
//    /**
//     * Test exportCharges - the exportCharges flag should be set to 'E'
//     */
//    public void testExportCharges()
//    {
//        PreHearingUpdateStub phUpdateStub = new PreHearingUpdateStub();
//        try
//        {
//            phUpdateStub.exportCharges(CASE_ID);
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(
//                    "SELECT EXPORT_CHARGES FROM XHB_CASE WHERE case_id = " +
//                    CASE_ID);
//            if (rs.next())
//            {
//                String exportCharges = rs.getString(1);
//                assertEquals("Export charges not set correctly",
//                             "C", exportCharges);
//            }
//            else
//            {
//                fail("Could not fine exporta record for id " + CASE_ID);
//            }
//        }
//        catch(Exception e)
//        {
//            System.err.println("Exception thrown:  "+e);
//        }
//    }
//}
//