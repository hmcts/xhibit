//package uk.gov.courtservice.xhibit.test.business.entities.ccinfo;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.vos.entities.CCInfoBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.ccinfo.CCInfoMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfo;
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
///**
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Khanh Tran
// * @version 1.0
// */
//
//public class CCInfoMaintainerTest extends TestCase
//{
//    private static Logger log = CSServices.getLogger(CCInfoMaintainerTest.class);
//    private UserTransaction ut;
//
//    public CCInfoMaintainerTest(String s)
//    {
//        super(s);
//    }
//
//
//    protected void setUp()
//    {
//        try
//        {
//            TestUtils.execSql("delete from xhb_cc_info");
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
//            TestUtils.execSql("delete from xhb_cc_info");
//        }
//        catch (Exception e)
//        {
//            log.debug ("setUp Error: " + e.toString( ));
//        }
//    }
//
//    public void testCreate()
//    {
//        try
//        {
//            CCInfoMaintainer maintainer = new CCInfoMaintainer();
//            CCInfoBasicValue actual = createBasicVO();
//
//            ut.begin();
//            CcInfo ccInfo = (CcInfo)maintainer.create(actual);
//            ut.commit();
//
//            assertEquals(actual.getCcInfoText(), ccInfo.getCcInfoText());
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
//    public void testDelete() {
//
//        try
//        {
//            CCInfoMaintainer maintainer = new CCInfoMaintainer();
//            CCInfoBasicValue actual = createBasicVO();
//
//            ut.begin();
//            CcInfo ccInfo = (CcInfo)maintainer.create(actual);
//            ut.commit();
//
//            Integer id = ccInfo.getCcInfoId();
//            Integer version = ccInfo.getVersion();
//
//            log.debug("ccInfoId: " + id);
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
//    public void testGetCCInfoBasicValue()
//    {
//        try
//        {
//            CCInfoMaintainer maintainer = new CCInfoMaintainer();
//            CCInfoBasicValue actual = createBasicVO();
//
//            ut.begin();
//            CcInfo ccInfo = (CcInfo)maintainer.create(actual);
//            ut.commit();
//
//            CCInfoBasicValue expect = maintainer.getCCInfoBasicValue(ccInfo);
//            String ccInfoText = expect.getCcInfoText();
//
//            log.debug("Expected ccInfoText: " + ccInfoText);
//            assertEquals("ccInfoText", ccInfoText);
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetCCInfoBasicValue() is failed");
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
//            CCInfoMaintainer maintainer = new CCInfoMaintainer();
//            CCInfoBasicValue actual = createBasicVO();
//
//            ut.begin();
//            CcInfo ccInfo = (CcInfo)maintainer.create(actual);
//            ut.commit();
//
//            CCInfoBasicValue update = maintainer.getCCInfoBasicValue(ccInfo);
//            update.setCcInfoText("CCInfoText");
//            maintainer.update(update);
//
//            CCInfoBasicValue expect = maintainer.getCCInfoBasicValue(ccInfo);
//            String ccInfoText = expect.getCcInfoText();
//
//            log.debug("Expected ccInfoText: " + ccInfoText);
//            assertEquals("CCInfoText", ccInfoText);
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
//    private CCInfoBasicValue createBasicVO()
//    {
//        CCInfoBasicValue bv = new CCInfoBasicValue();
//        bv.setCcInfoText("ccInfoText");
//
//        return bv;
//    }
//}