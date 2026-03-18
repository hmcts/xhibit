//package uk.gov.courtservice.xhibit.business.services.publicdisplay;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
//
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author unascribed
// * @version $Id: TestVipCourtRoomsQuery.java,v 1.2 2006/07/11 14:17:00 xzfdtb Exp $
// */
//
//public class TestVipCourtRoomsQuery extends TestCase
//{
//    public TestVipCourtRoomsQuery(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp() throws java.lang.Exception
//    {
//    }
//
//    public void testGetDataOneSite()
//    {
//        VipCourtRoomsQuery query = new VipCourtRoomsQuery(new StandAloneDataSource(), false);
//        XhbCourtRoomBasicValue[] results = query.getData(new Integer(1));
//        assertNotNull(results);
//        assertTrue("Results returned", results.length>0);
//        assertNotNull("Data populated", results[0].getCourtRoomId());
//        System.err.println("results[0].getCourtSiteCode()="+results[0].getCourtSiteCode());
//        assertNull("Court Site Not populated", results[0].getCourtSiteCode());
//    }
//
//    public void testGetDataMultiSite()
//    {
//        VipCourtRoomsQuery query = new VipCourtRoomsQuery(new StandAloneDataSource(), true);
//        XhbCourtRoomBasicValue[] results = query.getData(new Integer(1));
//        assertNotNull(results);
//        assertTrue("Results returned", results.length>0);
//        assertNotNull("Data populated", results[0].getCourtRoomId());
//        assertNotNull("Court Site populated", results[0].getCourtSiteCode());
//        System.err.println("results[0].getDisplayName()="+results[0].getDisplayName());
//        assertTrue("Contains -", results[0].getDisplayName().indexOf('-')>=0);
//    }
//}