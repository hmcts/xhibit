//package uk.gov.courtservice.xhibit.business.database.crestformsbf;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFCase;
//import uk.gov.courtservice.xhibit.business.models.crestformsbf.CrestFormsBFDefendant;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//import java.sql.*;
//
///**
// * <p>Title: Crest Forms B-F fastdb test class</p>
// * <p>Description: The JunitEE test class.
// *
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author  Edward Cawley, Xdevelopment LLP (2003)
// *
// */
//public class TestCrestFormsBFDatabase extends TransactionTestCase {
//
//    public TestCrestFormsBFDatabase(String s) throws NamingException{
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception{
//      super.setUp();
//      TestCrestFormsBFHelper.setUpCrestFormsBFData(null, connection);
//
//    }
//
////    protected void tearDown() {
////
////    }
//
//    public void testGetLinkedCases(){
//        CrestFormsBFCase[] cases = CrestFormsBFDatabase.getLinkedCases(new CrestFormsBFCase(new Integer(15), "S", "", new Integer(0), false));
//        StringBuffer sb = new StringBuffer();
//        sb.append("Linked cases for case id 15 \n");
//        for (int i = 0; i < cases.length; i++) {
//            sb.append("Case Id : " + cases[i].getId() + ", Type : " + cases[i].getType() + " Sub type " + cases[i].getSubType() + " Number :  " + cases[i].getNumber() + " Is Linked : " + cases[i].isLinked() + "\n");
//        }
//        System.out.println(sb.toString());
//    }
//
//    public void testCaseQuery(){
//        CrestFormsBFCase crestcase = CrestFormsBFDatabase.getCase(new Integer(1304));
//        StringBuffer sb = new StringBuffer();
//        if (crestcase != null) {
//            sb.append("Case details for case id 1304 \n");
//            sb.append("Case Id : " + crestcase.getId() + ", Type : " + crestcase.getType() + " Sub type " + crestcase.getSubType() + " Number :  " + crestcase.getNumber() + " Is Linked : " + crestcase.isLinked() + "\n");
//        } else {
//            sb.append("No record found for case id 1304 \n");
//        }
//        System.out.println(sb.toString());
//    }
//
//    public void testCourtClerk(){
//        StringBuffer sb = new StringBuffer("Court clerk for hearing 126 : " + CrestFormsBFDatabase.getCourtClerkName(new Integer(126)));
//        System.out.println(sb.toString());
//    }
//
//    public void testDefendantOnCaseId()
//    {
////        Integer id = CrestFormsBFDatabase.getDefendantOnCaseId(new Integer(26),new Integer(13));
////        System.out.println("Defendant on case id = " + id + " for case 26 defendant 13");
//        fail("test not updated in line with method class");
//    }
//
//    public void testNumberOfIndictments(){
//          int number = CrestFormsBFDatabase.getNumberOfIndictmentCounts(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//          System.out.println("NumberOfIndictments = " + number + " for case -1 defendant 1");
//          assertEquals(10, number);
//    }
//
//    public void testNumberOf41Offences(){
//        int number = CrestFormsBFDatabase.getNumberOfSection41Offences(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//        System.out.println("NumberOfSection41Offences = " + number + " for case -1 defendant -1");
//        assertEquals(10, number);
//    }
//
//    public void testNumberOfCommittalForSentenceOffences(){
//        int number = CrestFormsBFDatabase.getNumberOfCommittalForSentenceOffences(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//        System.out.println("NumberOfCommittalForSentenceOffences = " + number + " for case -1 defendant -1");
//        assertEquals(10, number);
//    }
//
//    public void testNumberOfBreachOffences(){
//        int number = CrestFormsBFDatabase.getNumberOfBreachOffences(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//        System.out.println("NumberOfBreachOffences = " + number + " for case -1 defendant -1");
//        assertEquals(10, number);
//    }
//
//    public void testNumberOfCriminalAppealOffences(){
//        int number = CrestFormsBFDatabase.getNumberOfCriminalAppealOffences(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//        System.out.println("NumberOfCriminalAppealOffences = " + number + " for case 1 defendant 1");
//        assertEquals(10, number);
//    }
//
//    public void testNumberOfMiscAppealCharges(){
//        int number = CrestFormsBFDatabase.getNumberOfCriminalAppealOffences(new CrestFormsBFCase(new Integer(-1), "S", "", new Integer(0), false),new CrestFormsBFDefendant(new Integer(1), "a", "b", "c"));
//        System.out.println("NumberOfMiscAppealCharges = " + number + " for case -1 defendant -1");
//        assertEquals(10, number);
//    }
//
//    public void testNumberOfUnrelatedDisposals(){
//        int number = CrestFormsBFDatabase.getNumberOfUnrelatedDisposals(new Integer(-1));
//        System.out.println("NumberOfUnrelatedDisposals = " + number + " for case -1 defendant -1");
//        assertEquals(4, number);
//    }
//
//    public void testDefendantsOnCase(){
//        CrestFormsBFDefendant[] defendants = CrestFormsBFDatabase.getDefendantsOnCase(new CrestFormsBFCase(new Integer(15), "S", "", new Integer(0), false));
//        StringBuffer sb = new StringBuffer();
//        sb.append("Defendants for case id 15 \n");
//        for (int i = 0; i < defendants.length; i++) {
//            sb.append("Defendant Id : " + defendants[i].getId() + ", First Name : " + defendants[i].getFirstName() + " Middle Name " + defendants[i].getMiddleName() + " Last name :  " + defendants[i].getLastName() + " \n");
//        }
//        System.out.println(sb.toString());
//    }
//}
//