//package uk.gov.courtservice.xhibit.test.business.entities.linkedsh;
//// 3RD PARTY
//import javax.naming.InitialContext;
//import javax.transaction.UserTransaction;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.linkedsh.LinkedSHMaintainer;
//import uk.gov.courtservice.xhibit.business.entities.linkedsh.LinkedSh;
//import uk.gov.courtservice.xhibit.business.vos.entities.LinkedSHBasicValue;
//
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
//public class LinkedSHMaintainerTest extends TestCase
//{
//    private static Logger log = CSServices.getLogger(LinkedSHMaintainerTest.class);
//    private UserTransaction ut;
//
//    public LinkedSHMaintainerTest(String s)
//    {
//        super(s);
//    }
//
//
//    protected void setUp()
//    {
//        try
//        {
//            TestUtils.execSql("delete from xhb_linked_sh");
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
//            TestUtils.execSql("delete from xhb_linked_sh");
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
//            LinkedSHMaintainer maintainer = new LinkedSHMaintainer();
//            LinkedSHBasicValue actual = new LinkedSHBasicValue();
//
//            ut.begin();
//            LinkedSh entity = (LinkedSh)maintainer.create(actual);
//            ut.commit();
//
//            log.debug("entity");
//            assertNotNull(entity);
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
//            LinkedSHMaintainer maintainer = new LinkedSHMaintainer();
//            LinkedSHBasicValue actual = new LinkedSHBasicValue();
//
//            ut.begin();
//            LinkedSh entity = (LinkedSh)maintainer.create(actual);
//            ut.commit();
//
//            Integer id = entity.getLinkedShId();
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
//    public void testGetLinkedSHBasicValue()
//    {
//        try
//        {
//            LinkedSHMaintainer maintainer = new LinkedSHMaintainer();
//            LinkedSHBasicValue actual = new LinkedSHBasicValue();
//
//            ut.begin();
//            LinkedSh entity = (LinkedSh)maintainer.create(actual);
//            ut.commit();
//            Integer id = entity.getLinkedShId();
//
//            LinkedSHBasicValue expected = maintainer.getLinkedSHBasicValue(entity);
//
//            log.debug("linkedShId");
//            assertEquals(id, expected.getId());
//        }
//        catch(Exception e)
//        {
//            log.debug("testGetLinkedSHBasicValue() is failed");
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
//            LinkedSHMaintainer maintainer = new LinkedSHMaintainer();
//            LinkedSHBasicValue actual = new LinkedSHBasicValue();
//
//            ut.begin();
//            LinkedSh entity = (LinkedSh)maintainer.create(actual);
//            ut.commit();
//            Integer version = entity.getVersion();
//
//            LinkedSHBasicValue update = maintainer.getLinkedSHBasicValue(entity);
//            maintainer.update(update);
//
//            log.debug("version");
//            log.debug("OLD: " + version + "     NEW: " + entity.getVersion());
//            assertEquals(new Integer(version.intValue() + 1), entity.getVersion());
//        }
//        catch(Exception e)
//        {
//            log.debug("testUpdate() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}