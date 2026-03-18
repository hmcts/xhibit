//package uk.gov.courtservice.xhibit.test.business.entities.hearinglist;
//
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingListBasicValue;
//import java.util.Date;
//
//
//
//
//public class HearingListValueTest extends TestCase
//{
//    private Logger log =  CSServices.getLogger(HearingListValueTest.class);
//
//    public HearingListValueTest(String s)
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
//            Integer listId = new Integer(1);
//            Integer edittionNo = new Integer(1);
//            Integer crestListId = new Integer(1);
//            Integer courtId = new Integer(1);
//            Integer dailyListXMLId = new Integer(1);
//            Integer version = new Integer(1);
//            String listType = "listType";
//            String status = "status";
//            String printReference = "printReference";
//            String listCourtType = "listCourtType";
//            Date startDate = new Date();
//            Date endDate = new Date();
//            Date publishedTime = new Date();
//
//            HearingListBasicValue basic = new HearingListBasicValue(listId, version);
////            basic.setListId(listId);
//            basic.setEditionNo(edittionNo);
//            basic.setCrestListId(crestListId);
//            basic.setCourtId(courtId);
//            //basic.setDailyListXMLId(dailyListXMLId);
//            basic.setListType(listType);
//            basic.setStatus(status);
//            basic.setPrintReference(printReference);
//            basic.setListCourtType(listCourtType);
//            basic.setStartDate(startDate);
//            basic.setEndDate(endDate);
//            basic.setPublishedTime(publishedTime);
//
//            log.debug("listId");
//            assertEquals(listId, basic.getId());
//            log.debug("edittionNo");
//            assertEquals(edittionNo, basic.getEditionNo());
//            log.debug("crestListId");
//            assertEquals(crestListId, basic.getCrestListId());
//            log.debug("courtId");
//            assertEquals(courtId, basic.getCourtId());
//            //log.debug("dailyListXMLId");
//            //assertEquals(dailyListXMLId, basic.getDailyListXMLId());
//            log.debug("listType");
//            assertEquals(listType, basic.getListType());
//            log.debug("status");
//            assertEquals(status, basic.getStatus());
//            log.debug("printReference");
//            assertEquals(printReference, basic.getPrintReference());
//            log.debug("listCourtType");
//            assertEquals(listCourtType, basic.getListCourtType());
//            log.debug("startDate");
//            assertEquals(startDate, basic.getStartDate());
//            log.debug("endDate");
//            assertEquals(endDate, basic.getEndDate());
//            log.debug("publishedTime");
//            assertEquals(publishedTime, basic.getPublishedTime());
//        }
//        catch(Exception e)
//        {
//            log.debug("testBasic() is failed");
//            e.printStackTrace();
//            fail();
//        }
//    }
//}