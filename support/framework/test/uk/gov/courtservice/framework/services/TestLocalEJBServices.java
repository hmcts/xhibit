//package uk.gov.courtservice.framework.services;
//
//import junit.framework.TestCase;
//
//public class TestLocalEJBServices extends TestCase {
//
//    public TestLocalEJBServices(String s) {
//        super(s);
//    }
//
//    protected void setUp() throws Exception {
//        System.out.println("setUp()");
//        // String q = "delete from defendant";
//        // TestUtils.execSql(q);
//    }
//
//    protected void tearDown() throws Exception {
//        System.out.println("tearDown()");
//        // String q = "delete from defendant";
//        // TestUtils.execSql(q);
//    }
//
///*    public void testThisClassPlease() {
//        fail("all tests in this class have been commented out");
//    }*/
//
//    /*
//     * public void testCreateLocalEntity() {
//     *
//     * System.out.println("**************testCreateEntity() start"); try {
//     * System.out.println("time" + new Date()); DefendantValue defendantValue =
//     * TestUtils.getDefendantValue(); Defendant defendant1 =
//     * (Defendant)CSServices.getEJBServices().createLocalEntity(defendantValue);
//     * System.out.println("defendant1=" + defendant1); Integer pk =
//     * defendant1.getDefId(); System.out.println("defendant1 pk=" + pk); Context
//     * ctx = TestUtils.getInitialContext(); System.out.println("about to
//     * lookup"); Object ref = ctx.lookup("DefendantHome");
//     * System.out.println("got ref=" + ref); DefendantHome home =
//     * (DefendantHome)ref; System.out.println("got home=" + home);
//     *
//     * Defendant defendant2 = home.findByPrimaryKey(pk); System.out.println("got
//     * defendant2=" + defendant2); System.out.println("defendant2 pk=" +
//     * defendant2.getDefId()); System.out.println("about to compare");
//     * assertTrue(defendant1.isIdentical(defendant2)); } catch(Exception e) {
//     * System.err.println("Exception thrown: "+e); fail(); } } public void
//     * testCreateLocalSession()throws Exception {
//     * System.out.println("testCreateLocalSession() start"); String rtnVal =
//     * "DelegateControllerBean successfully accessed."; DefendantController
//     * defCon = (DefendantController)
//     * CSServices.getEJBServices().createLocalSession(DefendantControllerHome.class);
//     *
//     * assertEquals(rtnVal, defCon.checkAccess()); }
//     *
//     * public void testDeleteLocalEntity() {
//     *
//     * System.out.println("testDeleteLocalEntity() start"); try {
//     * System.out.println("time " + new Date()); DefendantValue dv =
//     * TestUtils.getDefendantValue(); // first create a defendant Context ctx =
//     * TestUtils.getInitialContext(); Object ref = ctx.lookup("DefendantHome");
//     * System.out.println("got ref=" + ref); DefendantHome home =
//     * (DefendantHome)ref; Defendant defendant = home.create(dv); Integer pk =
//     * (Integer)defendant.getPrimaryKey(); System.out.println("created
//     * defendant=" + defendant); // delete it
//     * CSServices.getEJBServices().deleteLocalEntity(DefendantHome.class, pk);
//     *
//     * try { // check its not there Defendant defendant2 =
//     * home.findByPrimaryKey(pk); } catch (FinderException e) {
//     * System.err.println("Exception thrown: "+e); // ok } }
//     *
//     * catch(Exception e) { System.err.println("Exception thrown: "+e); fail(); } }
//     */
//}