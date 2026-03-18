//package uk.gov.courtservice.xhibit.test.business.entities.shlegrep;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
//
//
//
//public class SHLegRepValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(SHLegRepValueTest.class);
//
//    public SHLegRepValueTest(String s)
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
//            Integer shLegRepID = new Integer(1);
//            Integer crestSequenceNo = new Integer(1);
//            Integer schedHearDefID = new Integer(1);
//            Integer refLegalRepID = new Integer(1);
//            Integer ccInfoID = new Integer(1);
//            Integer refDefenceCategoryID = new Integer(1);
//            Integer refSolicitorFirmID = new Integer(1);
//            Integer version = new Integer(1);
//            String legalRole = "legalRole";
//            String solFirmOrRefLegalRep = "solFirmOrRefLegalRep";
//            String isSignIn = "Y";
//
//            SHLegRepBasicValue basic = new SHLegRepBasicValue(shLegRepID, version);
//            basic.setCrestSequenceNo(crestSequenceNo);
//            basic.setSchedHearDefID(schedHearDefID);
//            basic.setRefLegalRepID(refLegalRepID);
//            basic.setCcInfoID(ccInfoID);
//            basic.setRefDefenceCategoryID(refDefenceCategoryID);
//            basic.setRefSolicitorFirmID(refSolicitorFirmID);
//            basic.setLegalRole(legalRole);
//            basic.setSolFirmOrRefLegalRep(solFirmOrRefLegalRep);
//            basic.setIsSignIn(isSignIn);
//
//            log.debug("shLegRepID");
//            assertEquals(shLegRepID, basic.getId());
//            log.debug("crestSequenceNo");
//            assertEquals(crestSequenceNo, basic.getCrestSequenceNo());
//            log.debug("schedHearDefID");
//            assertEquals(schedHearDefID, basic.getSchedHearDefID());
//            log.debug("refLegalRepID");
//            assertEquals(refLegalRepID, basic.getRefLegalRepID());
//            log.debug("ccInfoID");
//            assertEquals(ccInfoID, basic.getCcInfoID());
//            log.debug("refDefenceCategoryID");
//            assertEquals(refDefenceCategoryID, basic.getRefDefenceCategoryID());
//            log.debug("refSolicitorFirmID");
//            assertEquals(refSolicitorFirmID, basic.getRefSolicitorFirmID());
//            log.debug("legalRole");
//            assertEquals(legalRole, basic.getLegalRole());
//            log.debug("solFirmOrRefLegalRep");
//            assertEquals(solFirmOrRefLegalRep, basic.getSolFirmOrRefLegalRep());
//            log.debug("isSignIn");
//            assertEquals(isSignIn, basic.getIsSignIn());
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