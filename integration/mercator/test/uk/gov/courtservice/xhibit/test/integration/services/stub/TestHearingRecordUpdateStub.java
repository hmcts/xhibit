//package uk.gov.courtservice.xhibit.test.integration.services.stub;
//
//import java.sql.ResultSet;
//import java.sql.Statement;
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.integration.services.stub.HearingRecordUpdateStub;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//
///**
// * <p>Title: TestHearingRecordUpdateStub</p>
// * <p>Description: Tests stubbed out mercator methods which fall into the
// * hearing record area.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Sarah Tong
// * @version $Id: TestHearingRecordUpdateStub.java,v 1.3 2006/07/11 14:16:52 xzfdtb Exp $
// */
//public class TestHearingRecordUpdateStub extends TransactionTestCase
//{
//    private int exportaId;
//
//    public TestHearingRecordUpdateStub(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        TestUtils.execSql("INSERT INTO XHB_EXPORTA (hearing_id) VALUES (1)");
//        Statement stmt = connection.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT MAX(export_a_id) FROM XHB_EXPORTA");
//        while (rs.next())
//        {
//            exportaId = rs.getInt(1);
//        }
//    }
//
//
//    /**
//     * Tests exportHearingRecord - the status flag should be set to 'S'
//     */
//    public void testExportHearingRecord()
//    {
//        HearingRecordUpdateStub hrUpdateStub = new HearingRecordUpdateStub();
//
//        try
//        {
//            hrUpdateStub.exportHearingRecord(new Integer(exportaId));
//            Statement stmt = connection.createStatement();
//            ResultSet rs = stmt.executeQuery(
//                    "SELECT STATUS_FLAG FROM XHB_EXPORTA WHERE export_a_id = " +
//                    exportaId);
//            if (rs.next())
//            {
//                String statusFlag = rs.getString(1);
//                assertEquals("Status flag not set correctly",
//                             HearingRecordUpdateStub.SUCCESSFUL, statusFlag);
//            }
//            else
//            {
//                fail("Could not fine exporta record for id " + exportaId);
//            }
//        }
//        catch(Exception e)
//        {
//            System.err.println("Exception thrown:  "+e);
//        }
//    }
//}
//