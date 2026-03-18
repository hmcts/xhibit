//package uk.gov.courtservice.xhibit.test.business.entities.linkedsh;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.LinkedSHBasicValue;
//
//
//
//public class LinkedSHValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(LinkedSHValueTest.class);
//
//    public LinkedSHValueTest(String s)
//    {
//        super(s);
//    }
//
//    protected void setUp()
//    {
//    }
//
//    protected void tearDown()
//    {
//    }
//
//    public void testBasic()
//    {
//        try
//        {
//            Integer linkedSHID = new Integer(1);
//            Integer version = new Integer(1);
//            LinkedSHBasicValue basic = new LinkedSHBasicValue(linkedSHID, version);
//
//
//            log.debug("linkedSHID");
//            assertEquals(linkedSHID, basic.getId());
//            log.debug("version");
//            assertEquals(version, basic.getVersion());
//        }
//        catch(Exception e)
//        {
//            log.debug("testBasic() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}