//
//package uk.gov.courtservice.xhibit.test.business.entities.hearing;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
//
//public class HearingValueTest extends TestCase
//{
//    private static final Logger log = CSServices.getLogger(HearingValueTest.class);
//
//    public HearingValueTest(String s)
//    {
//        super(s);
//    }
//
//    public void testBasic()
//    {
//        try
//        {
//            Integer hearingID = new Integer(1);
//            Integer caseID = new Integer(1);
//            Integer refHearingTypeID = new Integer(1);
//            Integer courtID = new Integer(1);
//            Integer version = new Integer(1);
//            Integer linkedHearingID = new Integer(1);
//            Date hearingStartDate = new Date();
//            Date hearingEndDate = new Date();
//            String mpHearingType = "mpHearingType";
//
//
//            HearingBasicValue basic = new HearingBasicValue(hearingID, version);
//            basic.setCaseID(caseID);
//            basic.setRefHearingTypeID(refHearingTypeID);
//            basic.setCourtID(courtID);
//            basic.setLinkedHearingID(linkedHearingID);
//            basic.setHearingStartDate(hearingStartDate);
//            basic.setHearingEndDate(hearingEndDate);
//            basic.setMpHearingType(mpHearingType);
//
//            log.debug("hearingID");
//            assertEquals(hearingID, basic.getId());
//            log.debug("caseID");
//            assertEquals(caseID, basic.getCaseID());
//            log.debug("refHearingTypeID");
//            assertEquals(refHearingTypeID, basic.getRefHearingTypeID());
//            log.debug("courtID");
//            assertEquals(courtID, basic.getCourtID());
//            log.debug("linkedHearingID");
//            assertEquals(linkedHearingID, basic.getLinkedHearingID());
//            log.debug("hearingStartDate");
//            assertEquals(hearingStartDate, basic.getHearingStartDate());
//            log.debug("hearingEndDate");
//            assertEquals(hearingEndDate, basic.getHearingEndDate());
//            log.debug("mpHearingType");
//            assertEquals(mpHearingType, basic.getMpHearingType());
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
//
//}