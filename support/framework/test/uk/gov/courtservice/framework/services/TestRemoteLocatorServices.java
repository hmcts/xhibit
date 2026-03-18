//package uk.gov.courtservice.framework.services;
//
//import javax.naming.InitialContext;
//import javax.sql.DataSource;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//public class TestRemoteLocatorServices extends TestCase {
//    // logging
//    private static Logger log = Logger.getLogger(TestRemoteLocatorServices.class.getName());
//
//    public TestRemoteLocatorServices(String s) {
//        super(s);
//    }
//
//    // run before each test
//    protected void setUp() {
//    }
//
//    // run after each test
//    protected void tearDown() throws Exception {
//        // log.debug("tearDown()");
//        // String q = "delete from defendant";
//        // TestUtils.execSql(q);
//    }
//
//    public void testGetInitialContext() {
//        log.debug("[testGetInitialContext]");
//        InitialContext ctx = CSServices.getServiceLocator().getInitialContext();
//        assertNotNull("initial context returned by CSServices was null", ctx);
//    }
//
//    public void testGetDataSource() {
//        log.debug("[testGetDataSource]");
//        // TestUtils.execSql("insert into defendant (DEF_ID, COURT_CASE_ID)
//        // vos(1, 2)");
//        DataSource source = CSServices.getServiceLocator().getDataSource();
//        assertNotNull("data source returned by CSServices was null", source);
//    }
//
//    // public void testGetRemoteHome() throws Exception
//    // {
//    // DefendantRemoteHome defendantHome =
//    // (DefendantRemoteHome)CSServices.getServiceLocator().getRemoteHome(DefendantRemoteHome.class);
//    // System.out.println("defendantHome=" + defendantHome);
//    // assertNotNull(defendantHome);
//    // }
//
//}