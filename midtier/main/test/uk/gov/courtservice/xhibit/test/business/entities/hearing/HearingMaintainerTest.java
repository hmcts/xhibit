//package uk.gov.courtservice.xhibit.test.business.entities.hearing;
//
//import java.util.Date;
//
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.hearing.Hearing;
//import uk.gov.courtservice.xhibit.business.entities.hearing.HearingMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//
//public class HearingMaintainerTest extends TestCase
//{
//    private static final Logger log = CSServices.getLogger(HearingMaintainerTest.class);
//    private UserTransaction ut;
//
//    public HearingMaintainerTest(String s)
//    {
//        super(s);
//    }
//
//
//    protected void setUp()
//    {
//        try
//        {
//            TestUtils.execSql("delete from xhb_hearing");
//            InitialContext initContext = new InitialContext();
//            ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
//
//        }
//        catch(Exception e)
//        {
//            log.debug ("setUp Error: " + e.toString());
//        }
//    }
//
//
//    protected void tearDown()
//    {
//        try
//        {
//            TestUtils.execSql("delete from xhb_hearing");
//        }
//        catch (Exception e)
//        {
//            log.debug ("tearDown() Error: " + e.toString( ));
//        }
//    }
//
//
//    public void testCreate()
//    {
//        try
//        {
//            HearingMaintainer maintainer = new HearingMaintainer();
//            HearingBasicValue actual = createBasicVO();
//
//            ut.begin();
//            Hearing entity = (Hearing)maintainer.create(actual);
//            ut.commit();
//
//            log.debug ("caseId");
//            assertEquals(actual.getCaseID(), entity.getCaseId());
//            log.debug ("courtId");
//            assertEquals(actual.getCourtID(), entity.getCourtId());
//            log.debug ("linkedHearingId");
//            assertEquals(actual.getLinkedHearingID(), entity.getLinkedHearingId());
//            log.debug ("refHearingTypeId");
//            assertEquals(actual.getRefHearingTypeID(), entity.getRefHearingTypeId());
//            log.debug ("hearingEndDate");
//            assertEquals(actual.getHearingEndDate().getTime(), entity.getHearingEndDate().getTime());
//            log.debug ("HearingStartDate");
//            assertEquals(actual.getHearingStartDate().getTime(), entity.getHearingStartDate().getTime());
//            log.debug ("mpHearingType");
//            assertEquals(actual.getMpHearingType(), entity.getMpHearingType());
//        }
//        catch(Exception e)
//        {
//            log.debug("testCreate() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testDelete()
//    {
//        try
//        {
//            HearingMaintainer maintainer = new HearingMaintainer();
//            HearingBasicValue actual = createBasicVO();
//
//            ut.begin();
//            Hearing entity = (Hearing)maintainer.create(actual);
//            ut.commit();
//
//            Integer id = entity.getHearingId();
//            Integer version = entity.getVersion();
//
//            log.debug("hearingId: " + id + "    version: " + version);
//            log.debug("version: " + version);
//
//            maintainer.delete(id, version);
//            assertTrue(true);
//        }
//        catch(Exception e)
//        {
//            log.debug("testDelete() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testGetHearingBasicValue()
//    {
//        try
//        {
//            HearingMaintainer maintainer = new HearingMaintainer();
//            HearingBasicValue actual = createBasicVO();
//
//            ut.begin();
//            Hearing entity = (Hearing)maintainer.create(actual);
//            ut.commit();
//
//            HearingBasicValue expected = maintainer.getHearingBasicValue(entity);
//
//            log.debug ("caseId");
//            assertEquals(actual.getCaseID(), expected.getCaseID());
//            log.debug ("courtId");
//            assertEquals(actual.getCourtID(), expected.getCourtID());
//            log.debug ("linkedHearingId");
//            assertEquals(actual.getLinkedHearingID(), expected.getLinkedHearingID());
//            log.debug ("refHearingTypeId");
//            assertEquals(actual.getRefHearingTypeID(), expected.getRefHearingTypeID());
//            log.debug ("hearingEndDate");
//            assertEquals(actual.getHearingEndDate().getTime(), expected.getHearingEndDate().getTime());
//            log.debug ("HearingStartDate");
//            assertEquals(actual.getHearingStartDate().getTime(), expected.getHearingStartDate().getTime());
//            log.debug ("mpHearingType");
//            assertEquals(actual.getMpHearingType(), expected.getMpHearingType());
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetHearingBasicValue() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    public void testUpdate()
//    {
//        try
//        {
//            HearingMaintainer maintainer = new HearingMaintainer();
//            HearingBasicValue actual = createBasicVO();
//
//            ut.begin();
//            Hearing entity = (Hearing)maintainer.create(actual);
//            ut.commit();
//
//            HearingBasicValue update = maintainer.getHearingBasicValue(entity);
//
//            update.setCaseID(new Integer(2));
//            update.setCourtID(new Integer(2));
//            update.setLinkedHearingID(new Integer(2));
//            update.setMpHearingType("M");
//            update.setRefHearingTypeID(new Integer(2));
//            maintainer.update(update);
//
//            HearingBasicValue expected = maintainer.getHearingBasicValue(entity);
//
//            log.debug ("caseId");
//            assertEquals(update.getCaseID(), expected.getCaseID());
//            log.debug ("courtId");
//            assertEquals(update.getCourtID(), expected.getCourtID());
//            log.debug ("linkedHearingId");
//            assertEquals(update.getLinkedHearingID(), expected.getLinkedHearingID());
//            log.debug ("refHearingTypeId");
//            assertEquals(update.getRefHearingTypeID(), expected.getRefHearingTypeID());
//            log.debug ("hearingEndDate");
//            assertEquals(update.getHearingEndDate().getTime(), expected.getHearingEndDate().getTime());
//            log.debug ("HearingStartDate");
//            assertEquals(update.getHearingStartDate().getTime(), expected.getHearingStartDate().getTime());
//            log.debug ("mpHearingType");
//            assertEquals(update.getMpHearingType(), expected.getMpHearingType());
//        }
//        catch(Exception e)
//        {
//            log.debug("testUpdate() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//
//    private HearingBasicValue createBasicVO()
//    {
//        HearingBasicValue bv = new HearingBasicValue();
//
//        // As the database does not record time in millisecond...
//        long currentTime = (System.currentTimeMillis()/1000)*1000;
//
//        bv.setCaseID(new Integer(1));
//        bv.setCourtID(new Integer(1));
//        bv.setHearingEndDate(new Date(currentTime));
//        bv.setHearingStartDate(new Date(currentTime));
//        bv.setLinkedHearingID(new Integer(1));
//        bv.setMpHearingType("m");
//        bv.setRefHearingTypeID(new Integer(1));
//
//        return bv;
//    }
//}