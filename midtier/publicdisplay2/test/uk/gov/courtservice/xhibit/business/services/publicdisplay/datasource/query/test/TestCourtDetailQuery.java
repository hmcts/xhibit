//package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.test;
//
//import java.util.GregorianCalendar;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.test.MidTierTestConstants;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.query.CourtDetailQuery;
//import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
//
//
//
///**
// * DOCUMENT ME!
// *
// * @author pznwc5 To change the template for this generated type comment go to
// *         Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestCourtDetailQuery extends TransactionTestCase implements MidTierTestConstants
//{
//    /** DOCUMENT ME! */
//    private CourtDetailQuery query;
//
//    /**
//     * Creates a new TestSummaryByNameQuery object.
//     *
//     * @param name DOCUMENT ME!
//     *
//     * @throws NamingException DOCUMENT ME!
//     */
//    public TestCourtDetailQuery(String name) throws NamingException
//    {
//        super(name, false);
//    }
//
//    /**
//     * DOCUMENT ME!
//     *
//     * @throws Exception DOCUMENT ME!
//     */
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        query = new CourtDetailQuery();
//    }
//
//    public void testGetCourtDetailData_singleRow() throws Exception
//    {
//        CourtDetailValue value = query.getData(
//                new GregorianCalendar(2003, 06, 10).getTime(), 3, 31);
//        assertNotNull(value);
//
//		assertEquals("Court Room 1", value.getCourtRoomName());
//		assertEquals("For Trial", value.getHearingDescription());
//		assertEquals("PHIL ADER1", value.getJudgeName().toString());
//		assertEquals("[TEST T DEFENDANT T20029074-1]", value.getDefendantNames().toString());
//		//System.out.println(value.getLiveStatus());
//		//assertEquals("<event xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"21200.xsd\"><free_text/><E21200_Reporting_Restrictions><E21200_RR_Type>E21200_An_order_has_been_made_under_Section_4_of_the_Sexual_Offenders_(Amendment_Act)_1976</E21200_RR_Type></E21200_Reporting_Restrictions><type>21200</type></event>",
//		//	value.getLiveStatus());
//		assertEquals("2004-01-16 14:52:03.0", value.getLiveStatusTime().toString());
//		assertEquals(5, value.getPublicNotices().length);
//
//    }
//
//}
//