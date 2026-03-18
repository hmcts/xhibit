//package uk.gov.courtservice.framework.services;
//
//import junit.framework.TestCase;
//
//public class TestRemoteEJBServices extends TestCase {
//
//    public TestRemoteEJBServices(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//
//    }
//
//    protected void tearDown() throws Exception {
//    }
///*
//    public void testThisClassPlease() {
//        fail("all tests in this class have been commented out");
//    }
//*/
//    /*
//     * public void testCreateRemoteSession()throws Exception { String rtnVal =
//     * "DelegateControllerBean successfully accessed.";
//     * DefendantControllerRemote defCon = (DefendantControllerRemote)
//     * CSServices.getEJBServices().createRemoteSession(DefendantControllerRemoteHome.class);
//     *
//     * assertEquals(rtnVal, defCon.checkAccess()); }
//     *
//     * public void testGetEJBObjectAndGetSerializedObject() throws Exception {
//     * System.out.println("testGetEJBObjectAndGetSerializedObject()"); String
//     * expVal = "DelegateControllerBean successfully accessed.";
//     * DefendantControllerRemote defCon1 = (DefendantControllerRemote)
//     * CSServices.getEJBServices().createRemoteSession(DefendantControllerRemoteHome.class);
//     *
//     *
//     * String actualVal = defCon1.checkAccess();
//     * System.out.println("defCon1.checkAccess = " + actualVal);
//     * assertEquals(expVal, actualVal ); String defConSer =
//     * CSServices.getEJBServices().getSerializedEJBObject(defCon1);
//     * DefendantControllerRemote defCon2 = (DefendantControllerRemote)
//     * CSServices.getEJBServices().getEJBObject(defConSer); String actualVal2 =
//     * defCon2.checkAccess(); System.out.println("actualVal2=" + actualVal2);
//     * assertEquals(expVal, actualVal2); }
//     *
//     *  // this has been also tested with TestValueCopier public void
//     * testCopyAttributes() throws Exception { // get a entity and copy all vos
//     * to it DefendantRemoteHome defHome =
//     * (DefendantRemoteHome)TestUtils.getInitialContext().lookup("DefendantRemoteHome");
//     *
//     * DefendantValue dv = TestUtils.getDefendantValue(); DefendantRemote def =
//     * defHome.create(dv); assertEquals(def.getFirstName(), dv.getFirstName());
//     *  }
//     *
//     * public void testGetJndiName() throws Exception { String name =
//     * "DefendantRemoteHome"; String jndiName =
//     * CSServices.getEJBServices().getJndiName(DefendantRemoteHome.class);
//     * assertEquals(name, jndiName); }
//     */
//
//}