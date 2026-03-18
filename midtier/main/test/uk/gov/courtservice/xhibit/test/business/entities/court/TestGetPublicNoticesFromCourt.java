/**@todo PFOX: Remove Test because functionality has changed.
package uk.gov.courtservice.xhibit.test.business.entities.court;

//jdk
import junit.framework.*;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.test.TestUtils;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtHome;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import java.util.Collection;

public class TestGetPublicNoticesFromCourt extends TestCase
{

    public TestGetPublicNoticesFromCourt(String s)

    {
        super(s);
    }

    protected void setUp() throws Exception

    {
        log("tearDown()");
        log("deleting data");
        // dont forget to do in reverse order
        TestUtils.execSql("delete from xhb_public_notice");
        TestUtils.execSql("delete from xhb_court_site");
        TestUtils.execSql("delete from xhb_court");
        TestUtils.execSql("delete from xhb_address");
        TestUtils.execSql(TestCourt.addrSQL);
        TestUtils.execSql(TestCourt.courtSQL);
        TestUtils.execSql(TestCourt.courtSite1SQL);
        TestUtils.execSql(TestCourt.publicNotice1SQL);
        TestUtils.execSql(TestCourt.publicNotice2SQL);
    }

    protected void tearDown() throws Exception

    {
        log("tearDown()");
        log("deleting data");

        // dont forget to do in reverse order
        TestUtils.execSql("delete from xhb_public_notice");
        TestUtils.execSql("delete from xhb_court_site");
        TestUtils.execSql("delete from xhb_court");
        TestUtils.execSql("delete from xhb_address");
    }

    public void log(String msg)
    {
        System.out.println(msg);
        //log.debug(msg);
    }
    public void testGetPublicNotices() throws Exception
    {
        log("***testGetPublicNotices() start");

        Integer pk = new Integer(1);
        try
        {
            Context ic = new InitialContext();
            Court court = (Court)CSServices.getEJBServices().findLocalEntityByPrimaryKey(CourtHome.class, pk);
            log("***court =" + court);
            log("getting transaction");
            UserTransaction ut = (UserTransaction) ic.lookup("java:comp/UserTransaction");
            ut.begin();

            assertEquals(pk, court.getCourtId());
            log("getting public notices");
            Collection publicNotices = court.getPublicNotices();
            log("publicNotices=" + publicNotices);
            log("checking isempty");
            assertTrue(!publicNotices.isEmpty());
            log("checking size");
            assertEquals(2,publicNotices.size());

            ut.commit();


        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail();
        }

    }

}*/