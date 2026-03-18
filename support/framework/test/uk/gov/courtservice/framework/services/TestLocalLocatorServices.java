//package uk.gov.courtservice.framework.services;
//
//import junit.framework.TestCase;
//
//public class TestLocalLocatorServices extends TestCase {
//
//    public TestLocalLocatorServices(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        System.out.println("setUp()");
//    }
//
//    protected void tearDown() throws Exception {
//        System.out.println("tearDown()");
//        // String q = "delete from defendant";
//        // TestUtils.execSql(q);
//    }
//
//    public void testThisClassPlease() {
//        fail("all tests in this class have been commented out");
//    }
//
//    /*
//     * public void testGetLocalHome() throws Exception {
//     * System.out.println("testGetLocalHome()"); TestUtils.execSql("insert into
//     * defendant (DEF_ID, COURT_CASE_ID) vos(1, 2)"); Class klass =
//     * DefendantHome.class; DefendantHome home = (DefendantHome)
//     * CSServices.getServiceLocator().getLocalHome(klass);
//     * System.out.println("localHome=" + home); Defendant defendant =
//     * home.findByPrimaryKey(new Integer(1)); System.out.println("defendant=" +
//     * defendant + " id=" + defendant.getDefId()); Integer defId =
//     * defendant.getDefId(); assertEquals(new Integer(1), defId);
//     * //assertNotNull(home); }
//     *
//     */
//}