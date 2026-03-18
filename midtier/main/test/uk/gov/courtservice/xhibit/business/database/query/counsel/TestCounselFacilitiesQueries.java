//package uk.gov.courtservice.xhibit.business.database.query.counsel;
//
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import javax.sql.DataSource;
//
//import junit.framework.TestCase;
//
//import org.w3c.dom.Document;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;
//
///**
// * Test class for the <code>CounselFacilitiesQueries</code> class.  If required
// * to run, the <code>StandAloneDataSource</code> can be configured via
// * command-line parameters, e.g.
// * "-Durl_key=jdbc:oracle:thin:@localhost:1521:ora9utf8" to specify which
// * database to point to.
// *
// * The tests performed by this class are dependent on hard-coded values.  These
// * could be acquired, but for simplicity will leave for now as hardcoded values.
// *
// * @author tz0d5m
// * @version $Id: TestCounselFacilitiesQueries.java,v 1.2 2006/07/11 14:16:54 xzfdtb Exp $
// *
// * @see uk.gov.courtservice.xhibit.business.database.query.counsel
// *      .CounselFacilitiesQueries
// */
//public class TestCounselFacilitiesQueries extends TestCase
//{
//    // the instance of the custom implementation of the class under test...
//    private final CounselFacilitiesQueries counselFacilitiesQueries =
//            new CounselFacilitiesQueriesTestImpl();
//
//    /**
//     * Required constructor for JUnit framework to take the name of this test
//     * class as the parameter
//     *
//     * @param name The name of the test method
//     */
//    public TestCounselFacilitiesQueries(final String name)
//    {
//        super(name);
//    }
//
//    public void testGetCourtRoomList() throws Exception
//    {
//        final Integer courtId = new Integer(3);
//        final Date startDate = (new GregorianCalendar(2004, 3, 14)).getTime();
//
//        final CourtList cl = counselFacilitiesQueries.getCourtRoomList(courtId,
//                                                                       startDate,
//                                                                       null);
//        final List courtRooms = cl.getCourtRooms();
//
//        assertNotNull("Court rooms returned should never be null", courtRooms);
//        assertTrue("Test failed as no results returned", courtRooms.size() > 0);
//
//        // this is for development purposes to acquire the XML document for
//        // further development...
//        Document dom = CSServices.getXMLServices().createDocFromValue(cl);
//        System.out.println(CSServices.getXMLServices().getStringXML(dom));
//    }
//
//    /**
//     * A custom implementation of the <code>CounselFacilitiesQueries</code>
//     * class to allow testing to be performed outside of an application server.
//     * This class overrides the getDataSource() method to return an
//     * implementation of a <code>StandAloneDataSource</code>.
//     *
//     * @author tz0d5m
//     */
//    private class CounselFacilitiesQueriesTestImpl
//                    extends CounselFacilitiesQueries
//    {
//        /**
//         * Override the default implementation to return a
//         * <code>StandAloneDataSource</code>.
//         *
//         * @return A <code>StandAloneDataSource</code>.
//         *
//         * @see uk.gov.courtservice.xhibit.business.database.query.counsel
//         *      .CounselFacilitiesQueries#getDataSource()
//         */
//        protected DataSource getDataSource()
//        {
//            // this is set up using command-line parameters...
//            // e.g. -Durl_key=jdbc:oracle:thin:@localhost:1521:ora9utf8
//            return new StandAloneDataSource();
//        }
//    }
//}
//