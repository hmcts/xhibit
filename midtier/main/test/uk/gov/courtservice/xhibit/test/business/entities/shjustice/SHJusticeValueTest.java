//
//package uk.gov.courtservice.xhibit.test.business.entities.shjustice;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
//
//
//
//public class SHJusticeValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SHJusticeValueTest.class);
//
//    public SHJusticeValueTest(String s)
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
//            Integer shJusticeID = new Integer(1);
//            Integer refJusticeID = new Integer(1);
//            Integer version = new Integer(1);
//            String justiceName = "justiceName";
//
//            SHJusticeBasicValue basic = new SHJusticeBasicValue(shJusticeID, version);
//            basic.setJusticeName(justiceName);
//
//            log.debug("shJusticeID");
//            assertEquals(shJusticeID, basic.getId());
//            log.debug("justiceName");
//            assertEquals(justiceName, basic.getJusticeName());
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