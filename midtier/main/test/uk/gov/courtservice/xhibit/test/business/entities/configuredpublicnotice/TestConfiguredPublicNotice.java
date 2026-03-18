/**@todo PFOX: Remove this Test


package uk.gov.courtservice.xhibit.test.business.entities.configuredpublicnotice;

import junit.framework.*;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.test.TestUtils;
import uk.gov.courtservice.xhibit.business.entities.configuredpublicnotice.ConfiguredPublicNotice;
import uk.gov.courtservice.xhibit.business.entities.configuredpublicnotice.ConfiguredPublicNoticeHome;
import uk.gov.courtservice.xhibit.business.entities.courtroom.CourtRoom;
import uk.gov.courtservice.xhibit.business.entities.publicnotice.PublicNotice;


public class TestConfiguredPublicNotice extends TestCase
{
    private Logger log =  CSServices.getLogger(getClass());
    String addr= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_4 ) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1, NULL)";
    String courtSQL = "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID ) VALUES ( 1, 'court type', 'circuit', 'court name', '12', 'BA', 'basil',  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', '12/12/2002', 1, 1)";
    String courtSiteSQL = "INSERT INTO XHB_COURT_SITE ( COURT_SITE_ID, COURT_SITE_NAME, COURT_SITE_CODE, COURT_ID, ADDRESS_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES ( 1, 'site name', 's', 1, 1,  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:53 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
    String courtRoomSQL = "INSERT INTO XHB_COURT_ROOM ( COURT_ROOM_ID, COURT_ROOM_NAME, DESCRIPTION, LOCATION, CREST_COURT_ROOM_NO, COURT_SITE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION ) VALUES (  1, 'court room name', 'court room desc', 'location', 1, 1,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM') ,  TO_Date( '11/15/2002 03:43:07 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
    String publicNoticeSQL = "INSERT INTO XHB_PUBLIC_NOTICE (PUBLIC_NOTICE_ID, PUBLIC_NOTICE_DESC, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, 'public notice description', 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";
    String conPublicNoticeSQL1 = "INSERT INTO XHB_CONFIGURED_PUBLIC_NOTICE (CONFIGURED_PUBLIC_NOTICE_ID, IS_ACTIVE, COURT_ROOM_ID, PUBLIC_NOTICE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, 1, 1, 1, TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:14:09 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'paul', 'paul', 1)";

    public TestConfiguredPublicNotice(String s)
    {
        super(s);
    }

    protected void setUp() throws Exception
    {
        log("setUp()");
        log("deleting data");
        // dont forget to do in reverse order
        TestUtils.execSql("delete from xhb_configured_public_notice");
        TestUtils.execSql("delete from xhb_public_notice");
        TestUtils.execSql("delete from xhb_court_room");
        TestUtils.execSql("delete from xhb_court_site");
        TestUtils.execSql("delete from xhb_court");
        TestUtils.execSql("delete from xhb_address");
        TestUtils.execSql(addr);
        TestUtils.execSql(courtSQL);
        TestUtils.execSql(courtSiteSQL);
        TestUtils.execSql(courtRoomSQL);
        TestUtils.execSql(publicNoticeSQL);
        TestUtils.execSql(conPublicNoticeSQL1);
    }

    protected void tearDown()throws Exception
    {
        log("tearDown()");
        log("deleting data");
        // dont forget to do in reverse order
        TestUtils.execSql("delete from xhb_configured_public_notice");
        TestUtils.execSql("delete from xhb_public_notice");
        TestUtils.execSql("delete from xhb_court_room");
        TestUtils.execSql("delete from xhb_court_site");
        TestUtils.execSql("delete from xhb_court");
        TestUtils.execSql("delete from xhb_address");
    }

    public void testFindByPrimaryKey() throws Exception
    {
        log("testFindByPrimaryKey() start");
        Integer pk = new Integer(1);
        try
        {
            ConfiguredPublicNotice conPublicNotice = (ConfiguredPublicNotice)CSServices.getEJBServices().findLocalEntityByPrimaryKey(ConfiguredPublicNoticeHome.class, pk);
            log("configuredpublicnotice=" + conPublicNotice);
            assertEquals(pk, conPublicNotice.getConfiguredPublicNoticeId());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    public void testGetCourtRoom() throws Exception
    {
        log("testGetCourtRoom() start");
        Integer pk = new Integer(1);
        try
        {
            ConfiguredPublicNotice conPublicNotice = (ConfiguredPublicNotice)CSServices.getEJBServices().findLocalEntityByPrimaryKey(ConfiguredPublicNoticeHome.class, pk);
            log("configuredpublicnotice=" + conPublicNotice);
            assertEquals(pk, conPublicNotice.getConfiguredPublicNoticeId());
            CourtRoom courtRoom = conPublicNotice.getCourtRoom();
            assertEquals(pk, courtRoom.getCourtRoomId());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }

    }

    public void testGetPublicNotice() throws Exception
    {
        log("testGetPublicNotice() start");
        Integer pk = new Integer(1);
        try
        {
            ConfiguredPublicNotice conPublicNotice = (ConfiguredPublicNotice)CSServices.getEJBServices().findLocalEntityByPrimaryKey(ConfiguredPublicNoticeHome.class, pk);
            log("configuredpublicnotice=" + conPublicNotice);
            assertEquals(pk, conPublicNotice.getConfiguredPublicNoticeId());
            PublicNotice publicnotice = conPublicNotice.getPublicNotice();
            assertEquals(pk, publicnotice.getPublicNoticeId());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }
    }

    private void log(String msg)
    {
        log.debug(msg);
    }

}
*/
